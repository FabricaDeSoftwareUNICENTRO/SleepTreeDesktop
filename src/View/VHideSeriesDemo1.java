package View;

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space



import Controller.ConectandoBanco;
import Controller.ConsultaArea;
import Model.ConsultaTabelaModel;
import Model.TabelaConsulta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class VHideSeriesDemo1 extends ApplicationFrame
{
    @Override
    public void windowClosing(final WindowEvent event) {  
    if (event.getWindow() == this) {  
        dispose();  
        //System.exit(0); ~> COMENTADO!!!  
    }  
} 
    
    static Controller.ConectandoBanco conect = new Controller.ConectandoBanco();
            
            static class DemoPanel extends JPanel
                implements ActionListener{
                private XYItemRenderer renderer;
                
                private XYDataset createSampleDataset(String area) throws Exception
                {    
                        conect.conecta();
                        conect.execRetorna("SELECT * FROM consultaArea where nomeArea='"+area+"'");
                        conect.desconecta();
       
                        XYSeries xyseries = new XYSeries("UF(Utah Modificado)");
                         XYSeries xyseries1 = new XYSeries("UF(Carolina do Norte Modificado)");
                         XYSeries xyseries2 = new XYSeries("HF(<=7.2)");
        ConsultaArea cons = new ConsultaArea();
        ArrayList<TabelaConsulta> areas = new ArrayList<TabelaConsulta>();
        float utah = 0;
        float carolina = 0;
        float ponderada = 0;
        try {
            areas = cons.consultaArea(area);
            if(areas.isEmpty()){
                JOptionPane.showMessageDialog(null,"Área sem registros.","Mensagem", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                for(int i=0, j=1;i<areas.size();i++,j++){
                    utah += Float.parseFloat(areas.get(i).getUtah());
                    carolina += Float.parseFloat(areas.get(i).getCarolina());
                    ponderada += Float.parseFloat(areas.get(i).getPonderada());
                    xyseries.add(j, utah);
                    xyseries1.add(j, carolina);
                    xyseries2.add(j, ponderada);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(VConsultarArea.class.getName()).log(Level.SEVERE, null, ex);
        }
                         XYSeriesCollection xyseriescollection = new XYSeriesCollection();
                        xyseriescollection.addSeries(xyseries);
                        xyseriescollection.addSeries(xyseries1);
                        xyseriescollection.addSeries(xyseries2);
                        return xyseriescollection;
                }

                private JFreeChart createChart(XYDataset xydataset, String area)
                {
                JFreeChart jfreechart = ChartFactory.createXYLineChart(area, "Dias",
                    "Unidades de frio/Horas de frio", xydataset, PlotOrientation.VERTICAL, true, true, false);
                    XYPlot xyplot = (XYPlot) jfreechart.getPlot();
                    renderer = xyplot.getRenderer();
                    // Trocar a cor do fundo do grafico
                    xyplot.setBackgroundPaint(Color.decode("#F0F0F0"));
                    // desabilitar as linhas de marcação no grafico
                    xyplot.setRangeGridlinesVisible(false);
                    xyplot.setDomainGridlinesVisible(false);
                return jfreechart;
                }

                public void actionPerformed(ActionEvent actionevent)
                {
                        byte byte0 = -1;
                        if (actionevent.getActionCommand().equals("S1"))
                                byte0 = 0;
                        else
                        if (actionevent.getActionCommand().equals("S2"))
                                byte0 = 1;
                        else
                        if (actionevent.getActionCommand().equals("S3"))
                                byte0 = 2;
                        if (byte0 >= 0)
                        {
                                boolean flag = renderer.getItemVisible(byte0, 0);
                                renderer.setSeriesVisible(byte0, new Boolean(!flag));
                        }
                }

                public DemoPanel(String area) throws Exception
                {
                super(new BorderLayout());
                    XYDataset xydataset = createSampleDataset(area);
                    JFreeChart jfreechart = createChart(xydataset,area);
                    ChartPanel chartpanel = new ChartPanel(jfreechart);
                    JPanel jpanel = new JPanel();
                    JCheckBox jcheckbox = new JCheckBox("UF(Utah Modificado)");
                    jcheckbox.setActionCommand("S1");
                    jcheckbox.addActionListener(this);
                    jcheckbox.setSelected(true);
                    JCheckBox jcheckbox1 = new JCheckBox("UF(Carolina do Norte Modificado)");
                    jcheckbox1.setActionCommand("S2");
                    jcheckbox1.addActionListener(this);
                    jcheckbox1.setSelected(true);
                    JCheckBox jcheckbox2 = new JCheckBox("HF(<=7.2)");
                    jcheckbox2.setActionCommand("S3");
                    jcheckbox2.addActionListener(this);
                    jcheckbox2.setSelected(true);
                    jpanel.add(jcheckbox);
                    jpanel.add(jcheckbox1);
                    jpanel.add(jcheckbox2);
                    //ativar caixa de botoes em baixo
                    add(chartpanel);
                    add(jpanel, "South");
                    //dimensões do grafico
                    chartpanel.setPreferredSize(new Dimension(500, 270));
            
                }
        }


        public VHideSeriesDemo1(String s,String area) throws Exception
        {
                
                super(s);
                // Adicionar o Dispose aqui
                setContentPane(new DemoPanel(area));
                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                

        }

        
        
        public static JPanel createDemoPanel(String area) throws Exception
        {
                
                return new DemoPanel(area);
        }

}
