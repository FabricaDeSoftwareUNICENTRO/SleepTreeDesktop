/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.ConectandoBanco;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author João Felipe
 */
public class ExportarTabela {

    ConectandoBanco conect = new ConectandoBanco();

    public void criaTabela(String area, ArrayList<TabelaConsulta> areas, String diretorio) throws IOException, WriteException {
        WritableWorkbook workbook = Workbook.createWorkbook(new File(diretorio+".xls"));
        WritableSheet sheet = workbook.createSheet("Consulta " + area, 0);

        // Create a cell format for Arial 10 point font
        WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        WritableCellFormat arial12format = new WritableCellFormat(arial10font);

        Label label = new Label(0, 0, "Data", arial12format);
        sheet.addCell(label);
        Label label2 = new Label(1, 0, "Temperatura Mínima", arial12format);
        sheet.addCell(label2);
        Label label3 = new Label(2, 0, "Temperatura Máxima", arial12format);
        sheet.addCell(label3);
        Label label4 = new Label(3, 0, "UF Utah Modificado", arial12format);
        sheet.addCell(label4);
        Label label5 = new Label(4, 0, "UF Carolina do Norte Modificado", arial12format);
        sheet.addCell(label5);
        Label label6 = new Label(5, 0, "HF (≤7,2ºC)", arial12format);
        sheet.addCell(label6);

        int numLinhas = 0;
        try {
            numLinhas = conect.numeroLinhas("consultaArea", area);
            //System.out.println("Numero de linhas: "+numLinhas);
            conect.desconecta();

            // Inserindo dados
            int i, j;
            float utah = 0, carolina = 0, ponderada = 0;
            for (TabelaConsulta area1 : areas) {
                utah += Float.parseFloat(area1.getUtah());
                carolina += Float.parseFloat(area1.getCarolina());
                ponderada += Float.parseFloat(area1.getPonderada());
            }
            for (i = 0; i < 6; i++) {
                for (j = 0; j < numLinhas; j++) {
                    if (i == 0) {
                        Label labelIns = new Label(i, j+1, areas.get(j).getData());
                        sheet.addCell(labelIns);
                    }
                    if (i == 1) {
                        Label labelIns = new Label(i, j+1, areas.get(j).getMin());
                        sheet.addCell(labelIns);
                    }
                    if (i == 2) {
                        Label labelIns = new Label(i, j+1, areas.get(j).getMax());
                        sheet.addCell(labelIns);
                    }
                    if (i == 3) {
                        Label labelIns = new Label(i, j+1, areas.get(j).getUtah());
                        sheet.addCell(labelIns);
                    }
                    if (i == 4) {
                        Label labelIns = new Label(i, j+1, areas.get(j).getCarolina());
                        sheet.addCell(labelIns);
                    }
                    if (i == 5) {
                        Label labelIns = new Label(i, j+1, areas.get(j).getPonderada());
                        sheet.addCell(labelIns);
                    }
                }
            }
            Label labelTotal = new Label(2, numLinhas+1, "Total: ", arial12format);
            Label labelTotalUtah = new Label(3, numLinhas+1, String.valueOf(utah));
            Label labelTotalCarolina = new Label(4, numLinhas+1, String.valueOf(carolina));
            Label labelTotalPonderada = new Label(5, numLinhas+1, String.valueOf(ponderada));
            sheet.addCell(labelTotal);
            sheet.addCell(labelTotalUtah);
            sheet.addCell(labelTotalCarolina);
            sheet.addCell(labelTotalPonderada);

            // All sheets and cells added. Now write out the workbook
            workbook.write();
            workbook.close();
            JOptionPane.showMessageDialog(null, "Exportação realizada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            Logger.getLogger(ConsultaTabelaModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
