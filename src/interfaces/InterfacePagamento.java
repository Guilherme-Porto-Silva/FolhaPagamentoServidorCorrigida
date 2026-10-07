package interfaces;

import java.rmi.Remote;
import modelos.Pagamento;

public interface InterfacePagamento extends Remote {

    double consultarPagamento (int funcionarioID);

    void lancarPagamento(Pagamento p);// cadastra o salário, descontos, benefícios e data de pagamento

    void consultarFolhaPagamento(int mes, int ano);// retorna a lista de pagamentos do mês/ano

    void buscarHoleritePorFuncionario(int idFuncionario, int mes, int ano);// retorna os detalhes do pagamento de um funcionário específico
}