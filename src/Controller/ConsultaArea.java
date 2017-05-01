/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Model.TabelaConsulta;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João Felipe
 */
public class ConsultaArea {
    ConectandoBanco conect = new ConectandoBanco();
    private ArrayList<TabelaConsulta> areas = new ArrayList<TabelaConsulta>();

    //private String area;
    public ConsultaArea() {
        
    }
    
    public ArrayList<TabelaConsulta> consultaArea(String campo) throws Exception {
        conect.conecta();
        ResultSet rs = conect.execRetorna("SELECT * FROM consultaArea where nomeArea='"+campo+"'");
        while(rs.next()){
            TabelaConsulta area = new TabelaConsulta();
            area.setData(rs.getString("codtemperatura"));
            area.setMin(rs.getString("hora6"));
            area.setMax(rs.getString("hora15"));
            area.setUtah(rs.getString("totalUtah"));
            area.setCarolina(rs.getString("totalCarolina"));
            area.setPonderada(rs.getString("totalPadrao"));
            
            areas.add(area);
        }
        conect.desconecta();
        return areas;     
    }
    
    public boolean areaExiste(String area){
        boolean existe = false;
        
        try {
            conect.conecta();
            ResultSet RS = conect.execRetorna("SELECT nomeArea FROM Area WHERE nomeArea='"+area+"'");
            if(RS.next()){
                existe = true;
            }
            conect.desconecta();
        } catch (Exception ex) {
            Logger.getLogger(ConsultaArea.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return existe;
    }
}
