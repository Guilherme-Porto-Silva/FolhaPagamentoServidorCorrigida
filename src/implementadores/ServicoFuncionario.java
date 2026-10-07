package implementadores;

import RMI.Conexao;
import interfaces.InterfaceFuncionario;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;

public class ServicoFuncionario extends UnicastRemoteObject implements InterfaceFuncionario {

    public ServicoFuncionario () throws RemoteException {}

    private final Conexao LINK = new Conexao();

    private final String SQL_CADASTRO_FUNCIONARIO = "insert into Funcionario (nome, cargo) values (?, ?)";

    private final String SQL_DEMICAO_FUNCIONARIO = "delete from Funcionario where Funcionario.id = ?";

    private final String SQL_CADASTRO_CARGO = "insert into Cargo (salario, departamento, horario_chegada, horario_saida) values (?, ?, ?, ?)";

    private final String SQL_DEMICAO_JUSTIFICATIVA = "insert into Demissoes (funcionario_demitido, razao_demissao) values (?, ?)";

    private final String SQL_ACHAR_FUNCIONARIO = "select nome from Funcionario where Funcionario.id = ?";

    private boolean cadastrarCargo () {

        return false;
    }

    @Override public boolean cadastrarFuncionario () {

        boolean deuCerto = true;

        try{
            LINK.conectar();

            PreparedStatement sentencaFuncionario = LINK.link.prepareStatement(SQL_CADASTRO_FUNCIONARIO);

            PreparedStatement sentencaCargo = LINK.link.prepareStatement(SQL_CADASTRO_CARGO);

            sentencaFuncionario.setString(1, f.getNome());

            sentencaFuncionario.setFloat(2, f.getCargo());

            sentencaCargo.setString(1, c.getNome());

            sentencaCargo.setFloat(2, c.getCargo());

            sentencaCargo.setString(3, c.getNome());

            sentencaCargo.setFloat(4, c.getCargo());

            if (sentencaCargo.execute()) deuCerto = false;

            if (sentencaFuncionario.execute()) deuCerto = false;

            LINK.link.close();
        }

        catch (Exception e) {

            System.out.println("\nDeu a seguinte merda: " + e.getMessage());

            deuCerto = false;
        }

        return deuCerto;
    }

    @Override public boolean demitirFuncionario (int funcionarioID, String justificativa) {

        boolean deuCerto;

        try {
            LINK.conectar();

            PreparedStatement sentencaJustificativa = LINK.link.prepareStatement(SQL_DEMICAO_JUSTIFICATIVA);

            sentencaJustificativa.setInt(1, funcionarioID);

            sentencaJustificativa.setString(2, justificativa);

            int linhasJustificativa = sentencaJustificativa.executeUpdate();

            PreparedStatement sentencaDemissao = LINK.link.prepareStatement(SQL_DEMICAO_FUNCIONARIO);

            sentencaDemissao.setInt(1, funcionarioID);

            int linhasDemissao = sentencaDemissao.executeUpdate();

            deuCerto = (linhasJustificativa > 0 && linhasDemissao > 0);

            LINK.link.close();
        }

        catch (Exception e) {

            System.out.println("\nErro ao demitir funcionário: " + e.getMessage());

            deuCerto = false;
        }

        return deuCerto;
    }
}