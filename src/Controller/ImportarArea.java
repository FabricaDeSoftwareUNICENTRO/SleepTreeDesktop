/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.ImportaArea;
import Model.ImportaTabela;
import Model.Temperatura;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 *
 * @author João Felipe
 */
public class ImportarArea {

    
    ConectandoBanco conect = new ConectandoBanco();
//    VImportando impor = new VImportando();

    //private String area;
    public ImportarArea() {

    }
    
    /**
     * Insere dados da importação no Banco da área correspondente.
     * @param campo que receberá os dados.
     * @param dados
     * @param sobrescrever se atualiza os campos iguais ou ignora
     * @throws Exception
     */
    public void importaArea(String campo, ArrayList<ArrayList> dados, boolean sobrescrever) throws Exception {
        ImportaTabela imp = new ImportaTabela();
        ArrayList<ImportaArea> dadosTabela = new ArrayList<ImportaArea>();
        
        
        int cont=0;
        for (ArrayList importa1 : dados) {    //TESTE 
            ImportaArea impArea = new ImportaArea();
            for (Object dia1 : importa1) {
                System.out.println(dia1);
                if(cont==0){
                    System.out.println("Cont = 0. Dia: "+dia1.toString()+"");
                    impArea.setData(dia1.toString());
                }
                if(cont==1){
                    System.out.println("Cont = 1. Hora 21 ant: "+dia1.toString()+"");
                    impArea.setHora21ant(dia1.toString());
                }
                if(cont==2){
                    System.out.println("Cont = 2. Min: "+dia1.toString()+"");
                    impArea.setMin(dia1.toString());
                }
                if(cont==3){
                    System.out.println("Cont = 3. Max: "+dia1.toString()+"");
                    impArea.setMax(dia1.toString());
                }
                if(cont==4){
                    System.out.println("Cont = 4. 21h: "+dia1.toString()+"");
                    impArea.setHora21(dia1.toString());
                }
                cont++;
            }
            cont=0;
            dadosTabela.add(impArea);
        }
        insereDados(campo, dadosTabela, sobrescrever);
    }
    
    /**
     * Insere os dados importados no banco.
     * @param dadosTabela
     * @param areas
     */
    private void insereDados(String area, ArrayList<ImportaArea> dadosTabela, boolean sobrescrever) throws Exception{
        // precisa inserir no banco usando os metodos de inserir e atualizar, 
        // precisa fazer os calculos de interpolação e unidades
        Temperatura temp = new Temperatura();
        final int tamanho = dadosTabela.size();
        int i;
        
        //Pegando código da área.
            conect.conecta();
            ResultSet RS = conect.execRetorna("SELECT codArea FROM Area WHERE nomeArea='"+area+"'");
            String codArea = RS.getString("codArea");
            conect.desconecta();
        
        for(i=0;i<tamanho;i++){
            ArrayList<ArrayList> temperaturas = new ArrayList<ArrayList>();
            temperaturas.clear();
            temperaturas = temp.interpolaTempTres(Float.parseFloat(dadosTabela.get(i).getHora21ant()), Float.parseFloat(dadosTabela.get(i).getMin()), Float.parseFloat(dadosTabela.get(i).getMax()), Float.parseFloat(dadosTabela.get(i).getHora21()), area, null);
            ArrayList<String> dat = this.dataAnt(dadosTabela.get(i).getData());
            GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(dat.get(0)),Integer.parseInt(dat.get(1))-1,Integer.parseInt(dat.get(2)));// 2012-04-21  

            conect.conecta();
            ResultSet confere = conect.execRetorna("SELECT codTemperatura FROM Temperatura WHERE codTemperatura='"+dadosTabela.get(i).getData()+"' and codArea='"+codArea+"'");
            boolean possui = confere.next();
            conect.desconecta();
            
            if(possui && sobrescrever){
                System.out.println("caiu no possui");
                conect.atualizarDadosBanco(temperaturas, codArea, dadosTabela.get(i).getData(), gc);
            }
            else if(sobrescrever==false && possui==false){
                    System.out.println("Insere normal1.");
                    conect.inserindoDados(temperaturas, dadosTabela.get(i).getData(), codArea, gc, 0); 
            }
            else if(sobrescrever==true && possui==false){
                System.out.println("Insere normal2");
               // Método para inserir no banco.
                conect.inserindoDados(temperaturas, dadosTabela.get(i).getData(), codArea, gc, 0); 
            }
            else{
                System.out.println("caiu no else sem nd!");
            }
        }
//        impor.setVisible(false);
    }
    
    /**
     * Recebe uma data no formato YYYY-MM-DD e retorna a data quebrada.
     * @param data
     * @return
     */
    public ArrayList<String> dataAnt(String data){
        String ano = data.substring(0, 4);
        String mes = data.substring(5, 7);
        String dia = data.substring(8, 10);
        
        ArrayList<String> dataQuebrada = new ArrayList<String>();
        dataQuebrada.add(ano);
        dataQuebrada.add(mes);
        dataQuebrada.add(dia);
        return dataQuebrada;
    }
}
