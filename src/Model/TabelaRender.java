/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Codigo para zebrar a tabela
 * @author João Felipe
 */
public class TabelaRender extends DefaultTableCellRenderer {
   public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
        TabelaConsulta t = ((ConsultaTabelaModel) table.getModel()).getTabelaData(row);  
  
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
        c.setOpaque(true);  
                Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(    
            table, value, isSelected, hasFocus, row, column);    
        
        ((JLabel) renderer).setOpaque(true);    
        
        Color foreground, background;    
        
          if (hasFocus) {  
            foreground = Color.white;    
            background = new Color(99, 184, 255);   
          } else if (row % 2 == 0) {    
            foreground = Color.BLACK;    
            background = Color.white;    
          } else {    
            foreground = Color.BLACK;    
            background = new Color(238, 233, 233);    
          }    
        
        renderer.setForeground(foreground);    
        renderer.setBackground(background);    
        return renderer;    
        
      } 
} 


