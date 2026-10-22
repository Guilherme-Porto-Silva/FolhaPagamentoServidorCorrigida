package RMI;

import implementadores.ServicoFuncionario;
import implementadores.ServicoPagamento;
import interfaces.InterfaceFuncionario;
import interfaces.InterfacePagamento;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

    public static void main (String[] args) {

        try{
            Registry listaTelefonica = LocateRegistry.createRegistry(1500);

            InterfaceFuncionario servicoFuncionario = new ServicoFuncionario();

            String numeroServicoFuncionario = System.getenv("FUNCIONARIO_RMI");
            
            if (numeroServicoFuncionario == null || numeroServicoFuncionario.isEmpty()) numeroServicoFuncionario = "numeroServicoFuncionario";
    
            listaTelefonica.bind(numeroServicoFuncionario, (Remote) servicoFuncionario);

            InterfacePagamento servicoPagamento = new ServicoPagamento();

            String numeroServicoPagamento = System.getenv("PAGAMENTO_RMI");
            
            if (numeroServicoPagamento == null || numeroServicoPagamento.isEmpty()) numeroServicoPagamento = "numeroServicoPagamento";
            
            listaTelefonica.bind(numeroServicoPagamento, (Remote) servicoPagamento);

            System.out.println("Servidor iniciado.");
        }

        catch (RemoteException e) {

            System.out.println("Erro de conexão: " + e.getMessage());
        }

        catch (AlreadyBoundException e) {

            System.out.println("O serviço já estava conectado.");
        }
    }
}
