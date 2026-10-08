package implementadores;

import RMI.Conexao;
import interfaces.InterfaceFuncionario;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

public class ServicoFuncionario extends UnicastRemoteObject implements InterfaceFuncionario {

    public ServicoFuncionario () throws RemoteException {}

    private final String SQL_CADASTRO_FUNCIONARIO = "insert into Funcionario (nome, cpf, cargoID) values (?, ?, ?)";

    private final String SQL_DEMICAO_FUNCIONARIO = "delete from Funcionario where Funcionario.id = ?";

    private final String SQL_CADASTRO_CARGO = "insert into Cargo (nome, salario, departamento) values (?, ?, ?)";

    private final String SQL_CADASTRO_DEPARTAMENTO = "insert into Departamento (nome) values (?)";

    private final String SQL_DEMICAO_JUSTIFICATIVA = "insert into Demissoes (funcionario_demitido, razao_demissao) values (?, ?)";

    private final String SQL_LISTAR_FUNCIONARIOS = "select nome from Funcionario";



    @Override public boolean cadastrarFuncionario (String nome, String cpf, int cargoID) throws RemoteException {

        boolean deuCerto;

        try{
            Conexao comServidor = new Conexao();

            PreparedStatement sentencaFuncionario = comServidor.link.prepareStatement(SQL_CADASTRO_FUNCIONARIO);

            sentencaFuncionario.setString(1, nome);

            sentencaFuncionario.setString(2, cpf);

            sentencaFuncionario.setInt(3, cargoID);

            deuCerto = !sentencaFuncionario.execute();

            comServidor.link.close();
        }

        catch (Exception e) {

            System.out.println("\nDeu a seguinte merda: " + e.getMessage());

            deuCerto = false;
        }

        return deuCerto;
    }



    @Override public boolean demitirFuncionario (int funcionarioID, String justificativa) throws RemoteException {

        boolean deuCerto;

        try {
            Conexao comServidor = new Conexao();

            PreparedStatement sentencaJustificativa = comServidor.link.prepareStatement(SQL_DEMICAO_JUSTIFICATIVA);

            sentencaJustificativa.setInt(1, funcionarioID);

            sentencaJustificativa.setString(2, justificativa);

            int linhasJustificativa = sentencaJustificativa.executeUpdate();

            PreparedStatement sentencaDemissao = comServidor.link.prepareStatement(SQL_DEMICAO_FUNCIONARIO);

            sentencaDemissao.setInt(1, funcionarioID);

            int linhasDemissao = sentencaDemissao.executeUpdate();

            deuCerto = (linhasJustificativa > 0 && linhasDemissao > 0);

            comServidor.link.close();
        }

        catch (Exception e) {

            System.out.println("\nErro ao demitir funcionário: " + e.getMessage());

            deuCerto = false;
        }

        return deuCerto;
    }



    @Override public List<String> listarFuncionarios () throws RemoteException {

        List<String> lista = new LinkedList<>();

        try {
            Conexao comServidor = new Conexao();

            PreparedStatement sentenca = comServidor.link.prepareStatement(SQL_LISTAR_FUNCIONARIOS);

            var resultado = sentenca.executeQuery();

            int posicaoAtual = 0;

            while (resultado.next()) {

                lista.add(resultado.getString(posicaoAtual));

                posicaoAtual++;
            }

            comServidor.link.close();
        }

        catch (Exception e) {

            System.out.println("\nErro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }



    @Override public boolean inserirCargo (String nome, double salario, int departamentoID) throws RemoteException {

        boolean deuCerto;

        try {
            Conexao comServidor = new Conexao();

            PreparedStatement sentenca = comServidor.link.prepareStatement(SQL_CADASTRO_CARGO);

            sentenca.setString(1, nome);

            sentenca.setDouble(2, salario);

            sentenca.setString(3, nome);

            deuCerto = !sentenca.execute();

            comServidor.link.close();
        }

        catch (Exception e) {

            System.out.println("\nProblema no cadastro de um cargo: " + e.getMessage());

            deuCerto = false;
        }

        return deuCerto;
    }



    @Override public boolean inserirDepartamento (String nome) throws RemoteException {

        boolean deuCerto;

        try {
            Conexao comServidor = new Conexao();

            PreparedStatement sentenca = comServidor.link.prepareStatement(SQL_CADASTRO_DEPARTAMENTO);

            sentenca.setString(1, nome);

            deuCerto = !sentenca.execute();

            comServidor.link.close();
        }

        catch (Exception e) {

            System.out.println("\nProblema no cadastro de um cargo: " + e.getMessage());

            deuCerto = false;
        }

        return deuCerto;
    }
}