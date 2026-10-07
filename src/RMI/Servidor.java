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
            // Serviço de nomes onde o servidor regista os objetos remotos e o cliente pesquisa a sua localização.
            Registry listaTelefonica = LocateRegistry.createRegistry(1500);

            InterfaceFuncionario servicoFuncionario = new ServicoFuncionario();

            InterfacePagamento servicoPagamento = new ServicoPagamento();

            try{
                String numeroServicoFuncionario = System.getenv("FUNCIONARIO_RMI");
                        
                listaTelefonica.bind(numeroServicoFuncionario, (Remote) servicoFuncionario);

                String numeroServicoPagamento = System.getenv("PAGAMENTO_RMI");

                listaTelefonica.bind(numeroServicoPagamento, (Remote) servicoPagamento);
            }
            
            catch (NullPointerException e) {

                listaTelefonica.bind("numeroServicoFuncionario", (Remote) servicoFuncionario);

                listaTelefonica.bind("numeroServicoFuncionario", (Remote) servicoPagamento);
            }

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