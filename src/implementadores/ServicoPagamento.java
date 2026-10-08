package implementadores;

import RMI.Conexao;
import interfaces.InterfacePagamento;
import modelos.Pagamento;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ServicoPagamento extends UnicastRemoteObject implements InterfacePagamento {

    public ServicoPagamento () throws RemoteException {}

    private final String SQL_ACHAR_PAGAMENTO_FUNCIONARIO = "select pagamento from Funcionario where Funcionario.id = ?";

    private final String SQL_ACHAR_SALARIO = "select c.salario from Funcionario f join Cargo c on f.cargoID = c.id where f.id = ?";

    private final String SQL_PAGAMENTO_JA_EFETUADO = "select count(*) from Pagamento where funcionario_id = ? and mes_ano = ?";

    private final String SQL_EFETUAR_PAGAMENTO = "insert into Pagamento (funcionario_id, mes_ano, salario_receptor, aliquota_utilizada, imposto_cobrado, pagamento_pratico) values (?, ?, ?, ?, ?, ?)";

    private static final DateTimeFormatter FORMATO_MES_ANO = DateTimeFormatter.ofPattern("MM/uuuu");



    @Override public double consultarPagamento (int funcionarioID) throws RemoteException {

        double pagamento;

        try {
            Conexao comServidor = new Conexao();

            PreparedStatement sentenca = comServidor.link.prepareStatement(SQL_ACHAR_PAGAMENTO_FUNCIONARIO);

            sentenca.setInt(1, funcionarioID);

            var resultado = sentenca.getResultSet();

            pagamento = resultado.getDouble(1);

            comServidor.link.close();
        }

        catch (Exception e) {

            System.out.println("\nErro ao demitir funcionário: " + e.getMessage());

            pagamento = -5;
        }

        return pagamento;
    }



    /**
     * Alíquota (fração, ex.: 0.09 = 9%) aplicada ao salário.
     * Faixas ilustrativas, ajuste conforme a regra exigida no trabalho.
     */
    private double definirAliquota (double salario) {

        if (salario <= 1518.00) return 0.075;

        if (salario <= 2793.88) return 0.09;

        if (salario <= 4190.83) return 0.12;

        return 0.14;
    }
    
    
    
    private ResultSet verificarJahFoiPago (Connection comServidor, int funcionarioID, String mesAno) {

        try{
            PreparedStatement verificacao = comServidor.prepareStatement(SQL_PAGAMENTO_JA_EFETUADO);

            verificacao.setInt(1, funcionarioID);

            verificacao.setString(2, mesAno);

            ResultSet resultadoVerificacao = verificacao.executeQuery();

            resultadoVerificacao.next();

            if (resultadoVerificacao.getInt(1) > 0) throw new IllegalArgumentException("O funcionário " + funcionarioID + " já foi pago em " + mesAno + ".");

            return resultadoVerificacao;
        }
        
        catch (Exception e) {
            
            throw new IllegalArgumentException(funcionarioID + " já tinha sido pago.");
        }
    }



    private double conferirSalario (Connection comServidor, int deQuem) {

        try{
            PreparedStatement verificacao = comServidor.prepareStatement(SQL_ACHAR_SALARIO);

            verificacao.setInt(1, deQuem);

            ResultSet resultadoVerificacao = verificacao.executeQuery();

            if (!resultadoVerificacao.next()) throw new IllegalArgumentException("Funcionário " + deQuem + " não encontrado.");

            return resultadoVerificacao.getDouble(1);
        }

        catch (Exception e) {

            throw new IllegalArgumentException("Não consegui conferir o salário do funcionário com ID \"" + deQuem + "\". O problema foi o seguinte:\n\n" + e.getMessage());
        }
    }



    private PreparedStatement sePrepararParaEfetuarPagamento (Connection comServidor, int paraQuem, String referenteAQuando, double quanto, double aliquota, double imposto, double pagamentoPratico) {

        try{
            PreparedStatement insercao = comServidor.prepareStatement(SQL_EFETUAR_PAGAMENTO);

            insercao.setInt(1, paraQuem);

            insercao.setString(2, referenteAQuando);

            insercao.setDouble(3, quanto);

            insercao.setDouble(4, aliquota);

            insercao.setDouble(5, imposto);

            insercao.setDouble(6, pagamentoPratico);

            return insercao;
        }
        
        catch (Exception e) {

            throw new IllegalArgumentException("Não consegui me preparar para realizar o pagamento. O problema foi o seguinte:\n\n" + e.getMessage());
        }
    }



    @Override public void calcularEfetuarPagamento (int funcionarioID, String mesAno) throws RemoteException {

        try {
            YearMonth.parse(mesAno, FORMATO_MES_ANO);// Valida o mês/ano (formato MM/aaaa).
        }

        catch (DateTimeParseException | NullPointerException e) {

            throw new IllegalArgumentException("Mês/ano inválido: use o formato MM/aaaa (ex.: 03/2026).");
        }
        
        

        try {
            Conexao comServidor = new Conexao();

            ResultSet resultadoVerificacaoJahFoiPago = verificarJahFoiPago(comServidor.link, funcionarioID, mesAno);
            
            var salario = conferirSalario(comServidor.link, funcionarioID);
            
            Pagamento pagamento = new Pagamento(definirAliquota(salario), salario);
            
            var insercao = sePrepararParaEfetuarPagamento(comServidor.link, funcionarioID, mesAno, pagamento.getSalario(), pagamento.getAliquota(), pagamento.getImposto(), pagamento.getPagamentoPratico());
            
            insercao.executeUpdate();
        }

        catch (IllegalArgumentException ex) { throw ex; }

        catch (Exception ex) {

            throw new RuntimeException("Ocorreu um problema inesperado:\n\n" + ex.getMessage());
        }
    }
}