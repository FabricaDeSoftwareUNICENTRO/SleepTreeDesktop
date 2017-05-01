/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Controller.ConectandoBanco;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author João Felipe
 * Está classe monta a tabela para a View VConsultarArea
 */
public class ConsultaTabelaModel extends AbstractTableModel {  
    private static final int COL_DATA = 0;  
    private static final int COL_MINIMA = 1;  
    private static final int COL_MAXIMA = 2;  
    private static final int COL_UTAH = 3;  
    private static final int COL_CAROLINA = 4;  
    private static final int COL_PONDERADA = 5;  
    ConectandoBanco conect = new ConectandoBanco();
    private String area;
    private ArrayList<TabelaConsulta> areas = new ArrayList<TabelaConsulta>();
  
    //private List<Livro> valores;
    //Esse é um construtor, que recebe a nossa lista de livros
    public ConsultaTabelaModel(String area, ArrayList<TabelaConsulta> areas) {
        this.area = area;
        this.areas = new ArrayList<TabelaConsulta>(areas);
        
    }
  
    @Override
    public int getRowCount() {  
        //Quantas linhas tem sua tabela? Uma para cada item da lista.
        
        int numLinhas = 0;
        try {
            numLinhas = conect.numeroLinhas("consultaArea", area);
            //System.out.println("Numero de linhas: "+numLinhas);
            conect.desconecta();
            return numLinhas;
        } catch (Exception ex) {
            Logger.getLogger(ConsultaTabelaModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numLinhas;
        
    }  
  
    @Override
    public int getColumnCount() {  
        //Quantas colunas tem a tabela?
        return 6;  
    }  
  
    @Override
    public String getColumnName(int column) {  
        //Qual é o nome das nossas colunas?  
        if (column == COL_DATA) return "Data";  
        if (column == COL_MINIMA) return "Mínima";  
        if (column == COL_MAXIMA) return "Máxima";  
        if (column == COL_UTAH) return "UTAH";  
        if (column == COL_CAROLINA) return "Carolina do Norte";  
        if (column == COL_PONDERADA) return "<=7.2";    
        return ""; //Nunca deve ocorrer  
    }  
  
    @Override
    public Object getValueAt(int row, int column) {  
        try {
            if (column == COL_DATA) return areas.get(row).getData();  
            else if (column == COL_MINIMA) return areas.get(row).getMin();
            else if (column == COL_MAXIMA) return areas.get(row).getMax();  
            else if (column == COL_UTAH) return areas.get(row).getUtah();  
            else if (column == COL_CAROLINA) return areas.get(row).getCarolina();  
            else if (column == COL_PONDERADA) return areas.get(row).getPonderada();
            
        } catch (Exception ex) {
            System.out.println("Entrei no Catch");
            Logger.getLogger(ConsultaTabelaModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ""; //Nunca deve ocorrer  
    }  
  
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {  
//        Livro titulo = valores.get(row);  
//        //Vamos alterar o valor da coluna columnIndex na linha rowIndex com o valor aValue passado no parâmetro.  
//        //Note que vc poderia alterar 2 campos ao invés de um só.  
        //if (columnIndex == COL_DATA) titulo.setTitulo(aValue.toString());  
        //else if (columnIndex == COL_MINIMA) titulo.getAutor().setNome(aValue.toString());  
    }  
  
    @Override
    public Class<?> getColumnClass(int columnIndex) {  
        //Qual a classe das nossas colunas? Como estamos exibindo texto, é string.  
        return String.class;  
    }  
      
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {  
        //Indicamos se a célula da rowIndex e da columnIndex é editável. Nossa tabela toda é.  
        return true;  
    }  
    
    public TabelaConsulta getTabelaData(int rowIndex) {  
        return areas.get(rowIndex);  
    }
} 