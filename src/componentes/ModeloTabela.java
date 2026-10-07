package componentes;

import view.Linha;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class ModeloTabela implements TableModel {
    
    private final List<Linha> tabela;

    public ModeloTabela (List<Linha> linhas) { tabela = linhas; }

    @Override public int getRowCount () {

        return tabela.size();
    }

    @Override public int getColumnCount () {

        return 10;
    }

    @Override public String getColumnName (int columnIndex) {

        return String.valueOf(getColumnClass(columnIndex));
    }

    @Override public Class<?> getColumnClass (int columnIndex) {

        if (columnIndex <= 0) throw new IllegalArgumentException("Insira um índice de coluna maior que 0.");

        if (columnIndex > 10) throw new IllegalArgumentException("Nossa tabela só tem 10 colunas.");

        return getValueAt(0, columnIndex).getClass();
    }

    @Override public boolean isCellEditable (int rowIndex, int columnIndex) {

        return false;
    }

    @Override public Object getValueAt (int rowIndex, int columnIndex) {

        try{
            return tabela.get(rowIndex).get(columnIndex);
        }

        catch (IndexOutOfBoundsException e) {

            return "Nossa tabela não tem " + rowIndex + " linhas.";
        }
    }

    @Override public void setValueAt (Object aValue, int rowIndex, int columnIndex) {

        System.out.println("A folha de pagamento não pode ser editada.");
    }

    @Override public void addTableModelListener (TableModelListener l) {

        System.out.println("Função ainda em desenvolvimento.");
    }

    @Override public void removeTableModelListener (TableModelListener l) {

        System.out.println("Escuta de modelo de mesa removida.");
    }
}