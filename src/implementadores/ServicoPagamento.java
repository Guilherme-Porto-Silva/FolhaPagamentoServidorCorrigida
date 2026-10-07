package implementadores;

import RMI.Conexao;
import interfaces.InterfacePagamento;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;

public class ServicoPagamento extends UnicastRemoteObject implements InterfacePagamento {

    public ServicoPagamento () throws RemoteException {}

    private final Conexao LINK = new Conexao();

    private final String SQL_ACHAR_PAGAMENTO_FUNCIONARIO = "select pagamento from Funcionario where Funcionario.id = ?";

    @Override public double consultarPagamento (int funcionarioID) {

        double pagamento;

        try {
            LINK.conectar();

            PreparedStatement sentenca = LINK.link.prepareStatement(SQL_ACHAR_PAGAMENTO_FUNCIONARIO);

            sentenca.setInt(1, funcionarioID);

            var resultado = sentenca.getResultSet();

            pagamento = resultado.getDouble(1);

            LINK.link.close();
        }

        catch (Exception e) {

            System.out.println("\nErro ao demitir funcionário: " + e.getMessage());

            pagamento = -5;
        }

        return pagamento;
    }
}