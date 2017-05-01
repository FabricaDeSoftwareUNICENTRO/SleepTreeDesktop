/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.ConectandoBanco;
import Model.TabelaExportaDados;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author João Felipe
 */
public class VExportar extends javax.swing.JFrame {
    ConectandoBanco conect = new ConectandoBanco();
    /**
     * Creates new form VExportar
     */
    public VExportar() {
        initComponents();
        
        ConectandoBanco conect = new ConectandoBanco();
        //Puxa dados do banco para o ComboBOx
        conect.configuraComboBox(jComboBoxArea);

        URL url = this.getClass().getResource("/Imagens/icone.ico");
        Image imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(imagemTitulo);
        
        chooser.setApproveButtonText("Exportar");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxArea = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Exportar Área");
        setResizable(false);

        jLabel1.setText("Área:");

        jButton1.setText("Exportar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxArea, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String nomeArea = jComboBoxArea.getSelectedItem().toString();
        int resultado = chooser.showOpenDialog(this);
//        System.out.println("Resultado: " + resultado);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                String endereco = chooser.getSelectedFile().toString();
                System.out.println("Arquivos Selecionado: " + endereco);
                
                // COLETANDO DADOS NO BANCO E CRIANDO VETOR DE TABELAEXPORTADADOS.
                conect.conecta();
                ResultSet RS = conect.execRetorna("SELECT * FROM Exporta_Tabela WHERE nomeArea='"+nomeArea+"'");
                ArrayList<TabelaExportaDados> dados = new ArrayList<>();
                int cont=0;
                float tUtah=0, tCarolina=0, tPonderada=0;
                while(RS.next()){
                    TabelaExportaDados exp = new TabelaExportaDados();
                    exp.setData(RS.getString("codTemperatura"));
                    // A hora21 do dia anterior do primeiro dia não é guardada.
                    if(cont==0){
                        System.out.println("caiu IF Desconhecida");
                        exp.setHora21ant("Desconhecida");
                    }
                    else{
                        System.out.println("caiu else, 21horaAnt="+dados.get(cont-1).getHora21()+"");
                        exp.setHora21ant(dados.get(cont-1).getHora21());
                    }
                    
                    exp.setMin(RS.getString("hora6"));
                    exp.setMax(RS.getString("hora15"));
                    exp.setHora21(RS.getString("hora21"));
                    exp.setTotalUtah(RS.getString("totalUtah"));
                    exp.setTotalCarolina(RS.getString("totalCarolina"));
                    exp.setTotalPonderada(RS.getString("totalPadrao"));
                    
                    tUtah += Float.parseFloat(RS.getString("totalUtah"));
                    tCarolina += Float.parseFloat(RS.getString("totalCarolina"));
                    tPonderada += Float.parseFloat(RS.getString("totalPadrao"));
                    
                    dados.add(exp);
                    cont++;
                }
                cont=0;
                conect.desconecta();
                //--------------------------------------------------------------\\
                
                WritableWorkbook workbook;
                workbook = Workbook.createWorkbook(new File(endereco + ".xls"));
                WritableSheet sheet = workbook.createSheet(nomeArea, 0);
                
                // Create a cell format for Arial 10 point font
                WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
                WritableCellFormat arial12format = new WritableCellFormat(arial10font);
                // Criando colunas Pais
                Label label = new Label(0, 0, "Data", arial12format);
                sheet.addCell(label);
                Label label2 = new Label(1, 0, "Temperatura Mínima", arial12format);
                sheet.addCell(label2);
                Label label3 = new Label(2, 0, "Temperatura Máxima", arial12format);
                sheet.addCell(label3);
                Label label4 = new Label(3, 0, "21H", arial12format);
                sheet.addCell(label4);
                Label label5 = new Label (4, 0, "UF Utah Modificado", arial12format);
                sheet.addCell(label5);
                Label label6 = new Label (5, 0, "UF Carolina do Norte Modificado", arial12format);
                sheet.addCell(label6);
                Label label7 = new Label (6, 0, "HF (≤7.2ºC)", arial12format);
                sheet.addCell(label7);
                
                // Populando linhas com o Array dados
                int i;
                for(i=0;i<dados.size();i++){
                    Label data = new Label(0, i+1, dados.get(i).getData());
                    sheet.addCell(data);
                    
                    Label min = new Label(1, i+1, dados.get(i).getMin());
                    sheet.addCell(min);
                    
                    Label max = new Label(2, i+1, dados.get(i).getMax());
                    sheet.addCell(max);
                    
                    Label noveH = new Label (3, i+1, dados.get(i).getHora21());
                    sheet.addCell(noveH);
                    
                    Label totalUtah = new Label(4,i+1, dados.get(i).getTotalUtah());
                    sheet.addCell(totalUtah);
                    
                    Label totalCarolina = new Label(5,i+1, dados.get(i).getTotalCarolina());
                    sheet.addCell(totalCarolina);
                    
                    Label totalPonderada = new Label(6,i+1, dados.get(i).getTotalPonderada());
                    sheet.addCell(totalPonderada);
                }
                Label total = new Label(3,dados.size()+1, "Total: ", arial12format);
                Label tTotalUtah = new Label(4, dados.size()+1, String.valueOf(tUtah));
                Label tTotalCarolina = new Label(5, dados.size()+1, String.valueOf(tCarolina));
                Label tTotalPonderada = new Label(6, dados.size()+1, String.valueOf(tPonderada));
                sheet.addCell(total);
                sheet.addCell(tTotalUtah);
                sheet.addCell(tTotalCarolina);
                sheet.addCell(tTotalPonderada);
                
                workbook.write();
                workbook.close();
                JOptionPane.showMessageDialog(null, "Exportação realizada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                Logger.getLogger(VPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                Logger.getLogger(VPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(VExportar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(VExportar.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VExportar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VExportar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VExportar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VExportar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VExportar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser chooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}