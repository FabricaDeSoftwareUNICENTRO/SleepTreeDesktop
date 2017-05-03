/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
//
import Model.Unidade;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author João Felipe
 */
public class ConectandoBanco {

    private Connection conexao;
    public Statement statement;
    public ResultSet resultset;
    public PreparedStatement prep;

    public ConectandoBanco() {
    }

    public void conecta() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexao = DriverManager.getConnection("jdbc:sqlite:SleepTreeDB.db");
            statement = conexao.createStatement();
            System.out.println("Conecto Banco");
            conexao.setAutoCommit(false);
            conexao.setAutoCommit(true);

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null,"Erro de Conexão com o Banco!\n"+"Erro: "+erro,"Erro",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public void desconecta() throws SQLException {
        System.out.println("Desconecto Banco");
        conexao.close();
        //JOptionPane.showMessageDialog(null,"banco fechado");  
    }

    public void desconecta2() {
        try {
            System.out.println("Desconecto Banco");
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectandoBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param sql Linha de comando que deseja executar no BD ex: SELECT * FROM
     * AREA; Precisa conectar e desconectar
     */
    public void exec(String sql) {
        try {
            resultset = statement.executeQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex, "Erro!", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ConectandoBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Por algum motivo bizarro a execução é mais rápida se conectar e
     * desconectar toda vez que usar o método.
     *
     * @param sql Linha de comando que deseja executar no BD tipo UPDATE,
     * INSERT, DELETE. Conecta e desconceta dentro do metodo.
     */
    public void execUp(String sql) {
        this.conecta();
        try {
            int i = statement.executeUpdate(sql);
            System.out.println("Comando SQL Executado: " + sql); // comando de teste
            this.desconecta();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex, "Erro!", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ConectandoBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Mesma função que o método execUp porém precisa conectar e desconectar
     * fora do método.
     *
     * @param sql Comando sql.
     */
    public void execUpdate(String sql) {
        try {
            int i = statement.executeUpdate(sql);
            System.out.println("Comando SQL Executado: " + sql); // comando de teste

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex, "Erro!", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ConectandoBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Mesma função que a exec porém retorna o result set Precisa conectar antes
     * de usar e depois de usar desconectar
     *
     * @param sql Comando SQL que deseja executar. Ex: SELECT * FROM AREA;
     * @return ResultSet de retorno do BD
     * @throws Exception teste
     */
    public ResultSet execRetorna(String sql) throws Exception {
        ResultSet RS = null;
        RS = statement.executeQuery(sql);
        return RS;
    }

    /**
     * Puxa as área cadastradas no banco e insere como itens em um jComboBox
     *
     * @param box ComboBox que deseja configrar
     */
    public void configuraComboBox(JComboBox box) {
        try {
            this.conecta();
            ResultSet RS = this.execRetorna("Select nomearea  from area order by 1");
            box.removeAllItems();

            while (RS.next()) {
                box.addItem(RS.getString("nomearea"));
            }
            this.desconecta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método utilizado para inserir novas áres cadastradas na janela
     * VAdicionarArea para o banco
     *
     * @param nome Nome da área
     * @param desc Descrição da área
     * @param local Localidade da área
     * @param tipo Tipo da plantação, ex: laranjeira
     * @param calculoTemp Número que define como será calculada a interpolação
     * de temperaturas de determinada área
     * @return Retorna 0 para Sim e 0 para Não
     * @throws Exception teste
     */
    public int insereAreaBanco(String nome, String desc, String local, String tipo, int calculoTemp) throws Exception {
        this.conecta();
        this.execUp("INSERT INTO area " + "(nomearea, descarea,localarea,tipoarea,calculoTemp)" + "values ('" + nome + "', '" + desc + "', '" + local + "', '" + tipo + "', '" + calculoTemp + "')");

        //JOptionPane.showMessageDialog(null,"Área cadastrada com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        int r = JOptionPane.showConfirmDialog(null, "Área cadastrada com sucesso!\nDeseja cadastrar outra área?", "Sucesso", JOptionPane.YES_NO_OPTION);
        this.desconecta();
        return r;
    }

    /**
     * Método utilizado na janela VRemoverArea para remover áreas do banco
     *
     * @param del nome da área que deseja deletar do banco
     * @throws Exception teste
     */
    public void removeArea(String del) throws Exception {
        this.conecta();
        this.execUp("DELETE FROM area WHERE nomeArea='" + del + "'");
        this.desconecta();
        //JOptionPane.showMessageDialog(null,"Área removida com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void removeQuinzena(String del) throws Exception {
        this.conecta();
        this.execUp("DELETE FROM quinzena WHERE nomeArea='" + del + "'");
        this.desconecta();
        //JOptionPane.showMessageDialog(null,"Área removida com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sempre desconectar do banco após usar este metodo
     *
     * @param linha nome da area
     * @param campo campo que irá retornar
     * @return ResultSet com os dados da área  SELECT %campo% from area where
     * nomearea = '%linha%'
     */
    public ResultSet dadosArea(String linha, String campo) {
        try {
            this.conecta();
            System.out.println("Comando executa SQL: " + "SELECT " + campo + " from area where nomearea = '" + linha + "'");
            ResultSet RS = this.execRetorna("SELECT " + campo + " from area where nomearea = '" + linha + "'");
            //this.desconecta(); //se desativar buga a parada
            return RS;
        } catch (Exception ex) {
            Logger.getLogger(ConectandoBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void insereUtah(ArrayList<ArrayList> unidades, String codArea, GregorianCalendar gc) {

    }

    // TEM QUE DEIXAR IGUAL ESTA O INSERINDO DADOS, POR CAUSA DOS 5 DIAS.
    public void atualizarDadosBanco(ArrayList<ArrayList> temps, String codArea, String dataTemperatura,
            GregorianCalendar gc) throws Exception {
        // CHAMAR O METODO DAS UNIDADES, PASSANDO AS TEMPERATURAS E RECEBENDO UM ARRAY COM AS UNIDADES
        Unidade uni = new Unidade();
        ArrayList<ArrayList> unidadesUtah = new ArrayList<>();
        ArrayList<ArrayList> unidadesCarolina = new ArrayList<>();
        ArrayList<ArrayList> unidadesPonderada = new ArrayList<>();
        unidadesUtah = uni.utah(temps);
        unidadesCarolina = uni.carolinaDoNorte(temps);
        unidadesPonderada = uni.ponderada(temps);

        int i;
        System.out.println("Temperaturas: " + temps.toString());
        System.out.println("Unidades Utah: " + unidadesUtah.toString());
        System.out.println("Unidades Carolina: " + unidadesCarolina.toString());
        System.out.println("Unidades Ponderada: " + unidadesPonderada.toString());
        System.out.println("Primeira temperatura do dia: " + temps.get(1).get(0).toString());

        // Calculando Total parcial das 0h até as 21h. 
        Float totalU = Float.parseFloat(unidadesUtah.get(2).get(0).toString())
                + Float.parseFloat(unidadesUtah.get(0).get(3).toString());
        if (totalU < 0) {
            totalU = Float.parseFloat("0");
        }
        String totalUtah = String.valueOf(totalU);
        System.out.println("testando");
        System.out.println("Total UTAH: " + totalUtah);

        Float totalCarol = Float.parseFloat(unidadesCarolina.get(2).get(0).toString())
                + Float.parseFloat(unidadesCarolina.get(0).get(3).toString());

        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);
        nf.setMaximumFractionDigits(4);
        String totalCarolina = nf.format(totalCarol);

        System.out.println("testando");
        System.out.println("Total Carolina: " + totalCarolina);

        Float totalPond = Float.parseFloat(unidadesPonderada.get(2).get(0).toString())
                + Float.parseFloat(unidadesPonderada.get(0).get(3).toString());
        String totalPonderada = String.valueOf(totalPond);
        System.out.println("testando");
        System.out.println("Total Ponderada: " + totalPonderada);

        // Atualizando Totais
        this.execUp("UPDATE Unidade set totalUnidade ='" + totalUtah + "' "
                + "WHERE codTemperatura ='" + dataTemperatura + "' and codArea='" + codArea + "' and codTipo='2'");
        this.execUp("UPDATE Unidade set totalUnidade ='" + totalCarolina + "' "
                + "WHERE codTemperatura ='" + dataTemperatura + "' and codArea='" + codArea + "' and codTipo='1'");
        this.execUp("UPDATE Unidade set totalUnidade ='" + totalPonderada + "' "
                + "WHERE codTemperatura ='" + dataTemperatura + "' and codArea='" + codArea + "' and codTipo='3'");

        // INSERINDO TEMPERATURAS e UNIDADES NO DIA ATUAL
        this.execUp("UPDATE Temperatura set hora0 = '" + temps.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' ");
        this.execUp("UPDATE Unidade set unidadeHora0 = '" + unidadesUtah.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='2'");
        this.execUp("UPDATE Unidade set unidadeHora0 = '" + unidadesCarolina.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='1'");
        this.execUp("UPDATE Unidade set unidadeHora0 = '" + unidadesPonderada.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='3'");

        this.execUp("UPDATE Temperatura set hora1='" + temps.get(1).get(0).toString() + "', hora2='" + temps.get(1).get(1).toString() + "', "
                + "hora3='" + temps.get(1).get(2).toString() + "', hora4='" + temps.get(1).get(3).toString() + "', hora5='" + temps.get(1).get(4).toString() + "',"
                + "hora6='" + temps.get(1).get(5).toString() + "', hora7='" + temps.get(1).get(6).toString() + "', hora8='" + temps.get(1).get(7).toString() + "',"
                + "hora9='" + temps.get(1).get(8).toString() + "', hora10='" + temps.get(1).get(9).toString() + "', hora11='" + temps.get(1).get(10).toString() + "',"
                + "hora12='" + temps.get(1).get(11).toString() + "', hora13='" + temps.get(1).get(12).toString() + "', hora14='" + temps.get(1).get(13).toString() + "', "
                + "hora15='" + temps.get(1).get(14).toString() + "', hora16='" + temps.get(1).get(15).toString() + "', hora17='" + temps.get(1).get(16).toString() + "',"
                + "hora18='" + temps.get(1).get(17).toString() + "', hora19='" + temps.get(1).get(18).toString() + "', hora20='" + temps.get(1).get(19).toString() + "',"
                + "hora21='" + temps.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "'");

        this.execUp("UPDATE Unidade set unidadehora1='" + unidadesUtah.get(1).get(0).toString() + "', unidadehora2='" + unidadesUtah.get(1).get(1).toString() + "', "
                + "unidadehora3='" + unidadesUtah.get(1).get(2).toString() + "', unidadehora4='" + unidadesUtah.get(1).get(3).toString() + "', unidadehora5='" + unidadesUtah.get(1).get(4).toString() + "',"
                + "unidadehora6='" + unidadesUtah.get(1).get(5).toString() + "', unidadehora7='" + unidadesUtah.get(1).get(6).toString() + "', unidadehora8='" + unidadesUtah.get(1).get(7).toString() + "',"
                + "unidadehora9='" + unidadesUtah.get(1).get(8).toString() + "', unidadehora10='" + unidadesUtah.get(1).get(9).toString() + "', unidadehora11='" + unidadesUtah.get(1).get(10).toString() + "',"
                + "unidadehora12='" + unidadesUtah.get(1).get(11).toString() + "', unidadehora13='" + unidadesUtah.get(1).get(12).toString() + "', unidadehora14='" + unidadesUtah.get(1).get(13).toString() + "', "
                + "unidadehora15='" + unidadesUtah.get(1).get(14).toString() + "', unidadehora16='" + unidadesUtah.get(1).get(15).toString() + "', unidadehora17='" + unidadesUtah.get(1).get(16).toString() + "',"
                + "unidadehora18='" + unidadesUtah.get(1).get(17).toString() + "', unidadehora19='" + unidadesUtah.get(1).get(18).toString() + "', unidadehora20='" + unidadesUtah.get(1).get(19).toString() + "',"
                + "unidadehora21='" + unidadesUtah.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='2'");

        this.execUp("UPDATE Unidade set unidadehora1='" + unidadesCarolina.get(1).get(0).toString() + "', unidadehora2='" + unidadesCarolina.get(1).get(1).toString() + "', "
                + "unidadehora3='" + unidadesCarolina.get(1).get(2).toString() + "', unidadehora4='" + unidadesCarolina.get(1).get(3).toString() + "', unidadehora5='" + unidadesCarolina.get(1).get(4).toString() + "',"
                + "unidadehora6='" + unidadesCarolina.get(1).get(5).toString() + "', unidadehora7='" + unidadesCarolina.get(1).get(6).toString() + "', unidadehora8='" + unidadesCarolina.get(1).get(7).toString() + "',"
                + "unidadehora9='" + unidadesCarolina.get(1).get(8).toString() + "', unidadehora10='" + unidadesCarolina.get(1).get(9).toString() + "', unidadehora11='" + unidadesCarolina.get(1).get(10).toString() + "',"
                + "unidadehora12='" + unidadesCarolina.get(1).get(11).toString() + "', unidadehora13='" + unidadesCarolina.get(1).get(12).toString() + "', unidadehora14='" + unidadesCarolina.get(1).get(13).toString() + "', "
                + "unidadehora15='" + unidadesCarolina.get(1).get(14).toString() + "', unidadehora16='" + unidadesCarolina.get(1).get(15).toString() + "', unidadehora17='" + unidadesCarolina.get(1).get(16).toString() + "',"
                + "unidadehora18='" + unidadesCarolina.get(1).get(17).toString() + "', unidadehora19='" + unidadesCarolina.get(1).get(18).toString() + "', unidadehora20='" + unidadesCarolina.get(1).get(19).toString() + "',"
                + "unidadehora21='" + unidadesCarolina.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='1'");

        this.execUp("UPDATE Unidade set unidadehora1='" + unidadesPonderada.get(1).get(0).toString() + "', unidadehora2='" + unidadesPonderada.get(1).get(1).toString() + "', "
                + "unidadehora3='" + unidadesPonderada.get(1).get(2).toString() + "', unidadehora4='" + unidadesPonderada.get(1).get(3).toString() + "', unidadehora5='" + unidadesPonderada.get(1).get(4).toString() + "',"
                + "unidadehora6='" + unidadesPonderada.get(1).get(5).toString() + "', unidadehora7='" + unidadesPonderada.get(1).get(6).toString() + "', unidadehora8='" + unidadesPonderada.get(1).get(7).toString() + "',"
                + "unidadehora9='" + unidadesPonderada.get(1).get(8).toString() + "', unidadehora10='" + unidadesPonderada.get(1).get(9).toString() + "', unidadehora11='" + unidadesPonderada.get(1).get(10).toString() + "',"
                + "unidadehora12='" + unidadesPonderada.get(1).get(11).toString() + "', unidadehora13='" + unidadesPonderada.get(1).get(12).toString() + "', unidadehora14='" + unidadesPonderada.get(1).get(13).toString() + "', "
                + "unidadehora15='" + unidadesPonderada.get(1).get(14).toString() + "', unidadehora16='" + unidadesPonderada.get(1).get(15).toString() + "', unidadehora17='" + unidadesPonderada.get(1).get(16).toString() + "',"
                + "unidadehora18='" + unidadesPonderada.get(1).get(17).toString() + "', unidadehora19='" + unidadesPonderada.get(1).get(18).toString() + "', unidadehora20='" + unidadesPonderada.get(1).get(19).toString() + "',"
                + "unidadehora21='" + unidadesPonderada.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='3'");

        gc.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println("Nova data função insereTemperatura do ConectandoBanco: " + gc.getTime().toString());
        String novaData = dataAntString(gc);

        // INSERINDO TEMPERATURAS DO DIA ANTERIOR
        System.out.println("");
        System.out.println("Atualizando dados do dia anterior, dia " + novaData);
        System.out.println("");
        this.conecta();
        ResultSet rs = this.execRetorna("SELECT codTemperatura FROM Temperatura WHERE codArea ='" + codArea + "' "
                + "and codTemperatura='" + novaData + "'");
        if (!rs.next()) {
            //Se não possuir o dia anterior então não insere dados no dia anterior.
            this.desconecta();
//            System.out.println("Tempo Total: " + (System.currentTimeMillis() - tempoInicio));
            System.out.println("caiu no if insereTemperaturas, não possui dia anterior");
        } else {
            this.desconecta();
            System.out.println("caiu no else insereTemperaturas, tem dia anterior");
            int cont = 22;
//            this.conecta();
            for (i = 1; i < 3; i++) {
                this.execUp("UPDATE Temperatura set hora" + cont + " = '" + temps.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "'");
                this.execUp("UPDATE Unidade set unidadeHora" + cont + " = '" + unidadesUtah.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "' and codTipo='2'");
                this.execUp("UPDATE Unidade set unidadeHora" + cont + " = '" + unidadesCarolina.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "' and codTipo='1'");
                this.execUp("UPDATE Unidade set unidadeHora" + cont + " = '" + unidadesPonderada.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "' and codTipo='3'");
                cont++;
            }
            cont = 0;

            // Atualizando total do dia anterior UTAH *************************************************
            System.out.println("Atualiza UTAH /n");
            this.conecta();
            ResultSet resultUtah = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo = '2'");
            // somaTotalUTAH é o valor total do dia ja atualizado com as 3 ultimas horas
            float somaTotalUTAH = 0;

            for (i = 22; i < 24; i++) {
                String coluna = "unidadeHora" + i;
                somaTotalUTAH += Float.parseFloat(resultUtah.getString(coluna));
            }

            somaTotalUTAH = somaTotalUTAH + Float.parseFloat(resultUtah.getString("totalUnidade"));
            System.out.println("Total UTAH: " + somaTotalUTAH);
            this.desconecta();

            //Calculo dos 5 dias de UTAH
            String data = this.dataAntString(gc);
            GregorianCalendar aux = new GregorianCalendar(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)) - 1, Integer.parseInt(data.substring(8, 10)));
            ArrayList<String> diasNegativosUtah = new ArrayList<>();
            ArrayList<String> totaisUtah = new ArrayList<>();
            int contaNegativos = 0;
            boolean zeroUtah = false; // caso true total vira 0.

            //Se dia for negativo faz verificação dos cinco dias.
            System.out.println("/n Dia: " + data + " Valor total do dia: " + somaTotalUTAH);

            System.out.println("Adiciono dia normal: " + somaTotalUTAH);
            this.execUp("UPDATE Unidade set totalUnidade ='" + somaTotalUTAH + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo='2'");
//            }

            // Atualiando total dia anterior Carolina **************************************************
            System.out.println("Atualiza Carolina /n");
            this.conecta();
            ResultSet resultCarolina = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo = '1'");

            System.out.println("cincodias data " + novaData + " total " + resultCarolina.getString("totalUnidade"));

            float somaTotalCarolina = 0;
            for (i = 22; i < 24; i++) {
                String coluna = "unidadeHora" + i;
                somaTotalCarolina += Float.parseFloat(resultCarolina.getString(coluna));
            }
            somaTotalCarolina = somaTotalCarolina + Float.parseFloat(resultCarolina.getString("totalUnidade"));
            this.desconecta();

            //Calculo dos 5 dias de Carolina
            String dataC = this.dataAntString(gc);
            GregorianCalendar auxC = new GregorianCalendar(Integer.parseInt(dataC.substring(0, 4)), Integer.parseInt(dataC.substring(5, 7)) - 1, Integer.parseInt(dataC.substring(8, 10)));
            ArrayList<String> diasNegativosCarolina = new ArrayList<>();
            ArrayList<String> totaisCarolina = new ArrayList<>();
            contaNegativos = 0;
            boolean zeroCarolina = false; // caso true total vira 0.

            this.execUp("UPDATE Unidade set totalUnidade ='" + somaTotalCarolina + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo='1'");
//            }
            // Atualiando total dia anterior Ponderada **************************************************
            this.conecta();
            System.out.println("Atualiza Ponderada /n");
            ResultSet resultPonderada = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo = '3'");

            float somaTotalPonderada = 0;
            for (i = 22; i < 24; i++) {
                String coluna = "unidadeHora" + i;
                somaTotalPonderada += Float.parseFloat(resultPonderada.getString(coluna));
            }
            somaTotalPonderada = somaTotalPonderada + Float.parseFloat(resultPonderada.getString("totalUnidade"));
            System.out.println("Total Ponderada: " + somaTotalPonderada);
            this.desconecta();
            this.execUp("UPDATE Unidade set totalUnidade ='" + somaTotalPonderada + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo='3'");
            System.out.println("");
            System.out.println("Terminando de atualizar dia anterior");
        }
    }

    /**
     *
     * @param temps temperaturas
     * @param codArea codigo da area
     * @param dataTemperatura data selecionada
     * @param gc GregorianCalendar do dia ante
     * @throws java.lang.Exception teste
     */
    public void insereDadosBanco(ArrayList<ArrayList> temps, String codArea, String dataTemperatura,
            GregorianCalendar gc) throws Exception {
        /**
         * PRECISA CRIAR UM JEITO DE INSERIR DADOS QUANDO INSERE UM DIA ANTERIOR
         * QUE POSSUI DIA SUCESSOR.
         */

        // CONTANDO TEMPO DE EXECUÇÃO
        long tempoInicio = System.currentTimeMillis();

        inserindoDados(temps, dataTemperatura, codArea, gc, tempoInicio);
        System.out.println("Tempo Total: " + (System.currentTimeMillis() - tempoInicio));
        JOptionPane.showMessageDialog(null, "Temperatura registrada com sucesso.", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     * @param temps temperaturas
     * @param dataTemperatura data da temperatura atual
     * @param codArea codigo da area
     * @param gc variavel para usar o gregorian calendar
     * @param tempoInicio temperatura inicial
     * @throws Exception teste
     * @throws NumberFormatException teste
     */
    public void inserindoDados(ArrayList<ArrayList> temps, String dataTemperatura, String codArea, GregorianCalendar gc, long tempoInicio) throws Exception, NumberFormatException {
        // CHAMAR O METODO DAS UNIDADES, PASSANDO AS TEMPERATURAS E RECEBENDO UM ARRAY COM AS UNIDADES

        Unidade uni = new Unidade();
        ArrayList<ArrayList> unidadesUtah = new ArrayList<>();
        ArrayList<ArrayList> unidadesCarolina = new ArrayList<>();
        ArrayList<ArrayList> unidadesPonderada = new ArrayList<>();
        unidadesUtah = uni.utah(temps);
        unidadesCarolina = uni.carolinaDoNorte(temps);
        unidadesPonderada = uni.ponderada(temps);

        // 
        //############################################################################################
        int i;
        System.out.println("Data do dia:" + dataTemperatura);
        System.out.println("Temperaturas: " + temps.toString());
        System.out.println("Unidades Utah: " + unidadesUtah.toString());
        System.out.println("Unidades Carolina: " + unidadesCarolina.toString());
        System.out.println("Unidades Ponderada: " + unidadesPonderada.toString());
        System.out.println("Primeira temperatura do dia: " + temps.get(1).get(0).toString());

        // Calculando Total parcial das 0h até as 21h. ********************************************************************************
        Float totalU = Float.parseFloat(unidadesUtah.get(2).get(0).toString())
                + Float.parseFloat(unidadesUtah.get(0).get(3).toString());
        String totalUtah = String.valueOf(totalU);
        System.out.println("testando 2");
        System.out.println("Total UTAH: " + totalUtah);

        Float totalCarol = Float.parseFloat(unidadesCarolina.get(2).get(0).toString())
                + Float.parseFloat(unidadesCarolina.get(0).get(3).toString());

        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);
        nf.setMaximumFractionDigits(4);
        String totalCarolina = nf.format(totalCarol);
        totalCarolina = totalCarolina.replaceAll(",", ".");

        System.out.println("testando 2");
        System.out.println("Total Carolina: " + totalCarolina);
        // ******** final Calculando Total parcial das 0h até as 21h. ***

        //VARIAVEIS USADAS PARA VER OS 5 DIAS ANTERIORES XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        boolean zeroCarolina = false; // caso true total vira 0.
        boolean zeroUtah = false; // caso true total vira 0.
        String data = this.dataAntString(gc);
        GregorianCalendar auxCarolina = new GregorianCalendar(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)) - 1, Integer.parseInt(data.substring(8, 10)));
        GregorianCalendar auxUTAH = new GregorianCalendar(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)) - 1, Integer.parseInt(data.substring(8, 10)));
        ArrayList<String> totaisUtah = new ArrayList<>();
        ArrayList<String> totaisUtahfinal = new ArrayList<>();
        ArrayList<String> totaisCarolina = new ArrayList<>();
        ArrayList<String> totaisCarolinafinal = new ArrayList<>();
        ArrayList<String> diasNegativosUtah = new ArrayList<>();
        ArrayList<String> diasNegativosUtahfinal = new ArrayList<>();
        ArrayList<String> diasNegativosCarolina = new ArrayList<>();
        ArrayList<String> diasNegativosCarolinafinal = new ArrayList<>();

        Float totalPond = Float.parseFloat(unidadesPonderada.get(2).get(0).toString())
                + Float.parseFloat(unidadesPonderada.get(0).get(3).toString());
        String totalPonderada = String.valueOf(totalPond);
        System.out.println("Total Ponderada: " + totalPonderada);

        //verifica o total do dia anterior caso seja positivo os cinco dias n conta
        float ajeitaCarolina = Float.parseFloat(totalCarolina);
        float ajeitaUtah = Float.parseFloat(totalUtah);

        // Preparando campos no banco
        this.execUp("INSERT INTO Temperatura (codTemperatura, codArea) values "
                + "('" + dataTemperatura + "','" + codArea + "')");
        // IF E ELSE NO CAROLINA E UTAH PARA VERIFICAR OS 5 DIAS ANTERIORES E INSERIR 0.
        if (zeroUtah == true && ajeitaUtah < 0) {
            this.execUp("INSERT INTO Unidade (codTemperatura, codArea, codTipo, totalUnidade) values "
                    + "('" + dataTemperatura + "','" + codArea + "','" + 2 + "','0')");
        } else {
            this.execUp("INSERT INTO Unidade (codTemperatura, codArea, codTipo, totalUnidade) values "
                    + "('" + dataTemperatura + "','" + codArea + "','" + 2 + "','" + totalUtah + "')");
        }
        if (zeroCarolina == true && ajeitaCarolina < 0) {
            this.execUp("INSERT INTO Unidade (codTemperatura, codArea, codTipo, totalUnidade) "
                    + "values ('" + dataTemperatura + "','" + codArea + "','" + 1 + "','0')");
        } else {
            this.execUp("INSERT INTO Unidade (codTemperatura, codArea, codTipo, totalUnidade) "
                    + "values ('" + dataTemperatura + "','" + codArea + "','" + 1 + "','" + totalCarolina + "')");
        }

        this.execUp("INSERT INTO Unidade (codTemperatura, codArea, codTipo, totalUnidade) "
                + "values ('" + dataTemperatura + "','" + codArea + "','" + 3 + "','" + totalPonderada + "')");

        // INSERINDO TEMPERATURAS e UNIDADES NO DIA ATUAL
        this.execUp("UPDATE Temperatura set hora0 = '" + temps.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' ");
        this.execUp("UPDATE Unidade set unidadeHora0 = '" + unidadesUtah.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='2'");
        this.execUp("UPDATE Unidade set unidadeHora0 = '" + unidadesCarolina.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='1'");
        this.execUp("UPDATE Unidade set unidadeHora0 = '" + unidadesPonderada.get(0).get(3).toString() + "' "
                + "where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='3'");

        // Inserindo temperaturas e unidades no dia selecionado
        this.execUp("UPDATE Temperatura set hora1='" + temps.get(1).get(0).toString() + "', hora2='" + temps.get(1).get(1).toString() + "', "
                + "hora3='" + temps.get(1).get(2).toString() + "', hora4='" + temps.get(1).get(3).toString() + "', hora5='" + temps.get(1).get(4).toString() + "',"
                + "hora6='" + temps.get(1).get(5).toString() + "', hora7='" + temps.get(1).get(6).toString() + "', hora8='" + temps.get(1).get(7).toString() + "',"
                + "hora9='" + temps.get(1).get(8).toString() + "', hora10='" + temps.get(1).get(9).toString() + "', hora11='" + temps.get(1).get(10).toString() + "',"
                + "hora12='" + temps.get(1).get(11).toString() + "', hora13='" + temps.get(1).get(12).toString() + "', hora14='" + temps.get(1).get(13).toString() + "', "
                + "hora15='" + temps.get(1).get(14).toString() + "', hora16='" + temps.get(1).get(15).toString() + "', hora17='" + temps.get(1).get(16).toString() + "',"
                + "hora18='" + temps.get(1).get(17).toString() + "', hora19='" + temps.get(1).get(18).toString() + "', hora20='" + temps.get(1).get(19).toString() + "',"
                + "hora21='" + temps.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "'");

        this.execUp("UPDATE Unidade set unidadehora1='" + unidadesUtah.get(1).get(0).toString() + "', unidadehora2='" + unidadesUtah.get(1).get(1).toString() + "', "
                + "unidadehora3='" + unidadesUtah.get(1).get(2).toString() + "', unidadehora4='" + unidadesUtah.get(1).get(3).toString() + "', unidadehora5='" + unidadesUtah.get(1).get(4).toString() + "',"
                + "unidadehora6='" + unidadesUtah.get(1).get(5).toString() + "', unidadehora7='" + unidadesUtah.get(1).get(6).toString() + "', unidadehora8='" + unidadesUtah.get(1).get(7).toString() + "',"
                + "unidadehora9='" + unidadesUtah.get(1).get(8).toString() + "', unidadehora10='" + unidadesUtah.get(1).get(9).toString() + "', unidadehora11='" + unidadesUtah.get(1).get(10).toString() + "',"
                + "unidadehora12='" + unidadesUtah.get(1).get(11).toString() + "', unidadehora13='" + unidadesUtah.get(1).get(12).toString() + "', unidadehora14='" + unidadesUtah.get(1).get(13).toString() + "', "
                + "unidadehora15='" + unidadesUtah.get(1).get(14).toString() + "', unidadehora16='" + unidadesUtah.get(1).get(15).toString() + "', unidadehora17='" + unidadesUtah.get(1).get(16).toString() + "',"
                + "unidadehora18='" + unidadesUtah.get(1).get(17).toString() + "', unidadehora19='" + unidadesUtah.get(1).get(18).toString() + "', unidadehora20='" + unidadesUtah.get(1).get(19).toString() + "',"
                + "unidadehora21='" + unidadesUtah.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='2'");

        this.execUp("UPDATE Unidade set unidadehora1='" + unidadesCarolina.get(1).get(0).toString() + "', unidadehora2='" + unidadesCarolina.get(1).get(1).toString() + "', "
                + "unidadehora3='" + unidadesCarolina.get(1).get(2).toString() + "', unidadehora4='" + unidadesCarolina.get(1).get(3).toString() + "', unidadehora5='" + unidadesCarolina.get(1).get(4).toString() + "',"
                + "unidadehora6='" + unidadesCarolina.get(1).get(5).toString() + "', unidadehora7='" + unidadesCarolina.get(1).get(6).toString() + "', unidadehora8='" + unidadesCarolina.get(1).get(7).toString() + "',"
                + "unidadehora9='" + unidadesCarolina.get(1).get(8).toString() + "', unidadehora10='" + unidadesCarolina.get(1).get(9).toString() + "', unidadehora11='" + unidadesCarolina.get(1).get(10).toString() + "',"
                + "unidadehora12='" + unidadesCarolina.get(1).get(11).toString() + "', unidadehora13='" + unidadesCarolina.get(1).get(12).toString() + "', unidadehora14='" + unidadesCarolina.get(1).get(13).toString() + "', "
                + "unidadehora15='" + unidadesCarolina.get(1).get(14).toString() + "', unidadehora16='" + unidadesCarolina.get(1).get(15).toString() + "', unidadehora17='" + unidadesCarolina.get(1).get(16).toString() + "',"
                + "unidadehora18='" + unidadesCarolina.get(1).get(17).toString() + "', unidadehora19='" + unidadesCarolina.get(1).get(18).toString() + "', unidadehora20='" + unidadesCarolina.get(1).get(19).toString() + "',"
                + "unidadehora21='" + unidadesCarolina.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='1'");

        this.execUp("UPDATE Unidade set unidadehora1='" + unidadesPonderada.get(1).get(0).toString() + "', unidadehora2='" + unidadesPonderada.get(1).get(1).toString() + "', "
                + "unidadehora3='" + unidadesPonderada.get(1).get(2).toString() + "', unidadehora4='" + unidadesPonderada.get(1).get(3).toString() + "', unidadehora5='" + unidadesPonderada.get(1).get(4).toString() + "',"
                + "unidadehora6='" + unidadesPonderada.get(1).get(5).toString() + "', unidadehora7='" + unidadesPonderada.get(1).get(6).toString() + "', unidadehora8='" + unidadesPonderada.get(1).get(7).toString() + "',"
                + "unidadehora9='" + unidadesPonderada.get(1).get(8).toString() + "', unidadehora10='" + unidadesPonderada.get(1).get(9).toString() + "', unidadehora11='" + unidadesPonderada.get(1).get(10).toString() + "',"
                + "unidadehora12='" + unidadesPonderada.get(1).get(11).toString() + "', unidadehora13='" + unidadesPonderada.get(1).get(12).toString() + "', unidadehora14='" + unidadesPonderada.get(1).get(13).toString() + "', "
                + "unidadehora15='" + unidadesPonderada.get(1).get(14).toString() + "', unidadehora16='" + unidadesPonderada.get(1).get(15).toString() + "', unidadehora17='" + unidadesPonderada.get(1).get(16).toString() + "',"
                + "unidadehora18='" + unidadesPonderada.get(1).get(17).toString() + "', unidadehora19='" + unidadesPonderada.get(1).get(18).toString() + "', unidadehora20='" + unidadesPonderada.get(1).get(19).toString() + "',"
                + "unidadehora21='" + unidadesPonderada.get(1).get(20).toString() + "' where codArea='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='3'");

        gc.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println("Nova data função insereTemperatura do ConectandoBanco: " + gc.getTime().toString());
        String novaData = dataAntString(gc);

        // INSERINDO TEMPERATURAS DO DIA ANTERIOR
        this.conecta();
        ResultSet rs = this.execRetorna("SELECT codTemperatura FROM Temperatura WHERE codArea ='" + codArea + "' "
                + "and codTemperatura='" + novaData + "'");
        if (!rs.next()) {
            //Se não possuir o dia anterior então não insere dados no dia anterior.
            this.desconecta();
            System.out.println("Tempo Total: " + (System.currentTimeMillis() - tempoInicio));
            System.out.println("caiu no if insereTemperaturas, não possui dia anterior");
        } else {
            this.desconecta();
            System.out.println("caiu no else insereTemperaturas, tem dia anterior");
            int cont = 22;
//            this.conecta();
            for (i = 1; i < 3; i++) {
                this.execUp("UPDATE Temperatura set hora" + cont + " = '" + temps.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "'");
                this.execUp("UPDATE Unidade set unidadeHora" + cont + " = '" + unidadesUtah.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "' and codTipo='2'");
                this.execUp("UPDATE Unidade set unidadeHora" + cont + " = '" + unidadesCarolina.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "' and codTipo='1'");
                this.execUp("UPDATE Unidade set unidadeHora" + cont + " = '" + unidadesPonderada.get(0).get(i).toString() + "' "
                        + "where codArea='" + codArea + "' and codTemperatura='" + novaData + "' and codTipo='3'");
                cont++;
            }
            cont = 0;

            // Atualizando total do dia anterior UTAH *************************************************
            // se o total do dia anterior igual a zero então continua 0.
            this.conecta();
            ResultSet resultTotalUtah = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + " and codTipo = '2'");

            float auxtotalUtah = 0;
            float auxtotalUtah1 = 0;
            while (resultTotalUtah.next()) {
                auxtotalUtah = auxtotalUtah + Float.parseFloat(resultTotalUtah.getString("totalUnidade"));
                auxtotalUtah1 = Float.parseFloat(resultTotalUtah.getString("totalUnidade"));
            }
            auxtotalUtah = auxtotalUtah - auxtotalUtah1;

            this.desconecta();

            float auxcalc = 0;
            this.conecta();
            ResultSet resultUtah = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo = '2'");
            float somaTotalUTAH = 0;

            for (i = 22; i < 24; i++) {
                String coluna = "unidadeHora" + i;
                somaTotalUTAH += Float.parseFloat(resultUtah.getString(coluna));
                auxtotalUtah += Float.parseFloat(resultUtah.getString(coluna));
            }

            // Parte do cod que n deixa o total ficar negativo
                somaTotalUTAH = somaTotalUTAH + Float.parseFloat(resultUtah.getString("totalUnidade"));

            this.desconecta();
            this.execUp("UPDATE Unidade set totalUnidade ='" + somaTotalUTAH + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo='2'");

            //Verifica cinco dias em UTAH
            int contaNegativosUTAH = 0;
            this.conecta();
//            ResultSet resultTotalUtahFinal = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
//                    + " and codTipo = '2'");
            System.out.println("");
            System.out.println("Verificando os cinco dias Utah");
            System.out.println("Dia: " + novaData);
            System.out.println("total do dia: " + somaTotalUTAH);
            System.out.println("");
            this.desconecta();
            if (somaTotalUTAH <= 0) {
                for (i = 0; i < 5; i++) {
                    auxUTAH.add(Calendar.DAY_OF_MONTH, -1);
                    String diaAnt = dataAntString(auxUTAH);
                    this.conecta();
                    ResultSet rsAntUtah = this.execRetorna("SELECT totalUnidade FROM Unidade WHERE codArea ='" + codArea + "' and codTemperatura='" + diaAnt + "' and codTipo='" + 2 + "'");
                    if (rsAntUtah.next()) {
                        totaisUtahfinal.add(rsAntUtah.getString("totalUnidade"));
                        diasNegativosUtahfinal.add(diaAnt);
                    }
                    this.desconecta();
                }
                if (totaisUtahfinal.size() == 5) {
                    for (String totaisUtah1 : totaisUtahfinal) {
                        if (Float.parseFloat(totaisUtah1) <= 0) {
                            contaNegativosUTAH++;
                        }
                    }
                    if (contaNegativosUTAH == 5) {
                        for (int j = 0; j < diasNegativosUtahfinal.size(); j++) {
                            this.execUp("UPDATE Unidade set totalUnidade ='" + "0" + "' where codArea='" + codArea + "' "
                                    + "and codTemperatura='" + diasNegativosUtahfinal.get(j) + "' and codTipo='2'");

                        }

                    }
                }
                for (String totais2 : totaisUtahfinal) {
                    System.out.println("Lista Negativos: " + totais2);
                }
                System.out.println("");
            }

            // Atualiando total dia anterior Carolina **************************************************
            this.conecta();
            ResultSet resultTotalCarol = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + " and codTipo = '1'");

            float auxtotalCarol = 0;
            float auxtotalCarol1 = 0;
            while (resultTotalCarol.next()) {
                auxtotalCarol += Float.parseFloat(resultTotalCarol.getString("totalUnidade"));
                auxtotalCarol1 = Float.parseFloat(resultTotalCarol.getString("totalUnidade"));
            }
            auxtotalCarol = auxtotalCarol - auxtotalCarol1;

            this.desconecta();

            float auxcalc1 = 0;

            this.conecta();
            ResultSet resultCarolina = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo = '1'");

            float somaTotalCarolina = 0;
            String auxSoma = null;
            for (i = 22; i < 24; i++) {
                String coluna = "unidadeHora" + i;
                somaTotalCarolina += Float.parseFloat(resultCarolina.getString(coluna));
                auxtotalCarol += Float.parseFloat(resultCarolina.getString(coluna));
            }

               somaTotalCarolina = somaTotalCarolina + Float.parseFloat(resultCarolina.getString("totalUnidade"));


            this.desconecta();
            this.execUp("UPDATE Unidade set totalUnidade ='" + somaTotalCarolina + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo='1'");

            System.out.println("");
            int contaNegativosCarolina = 0;
            this.conecta();
            System.out.println("Verificando os cinco dias Carolina");
            System.out.println("Dia: " + novaData);
            System.out.println("total do dia: " + somaTotalCarolina);
            System.out.println("");
            this.desconecta();
            if (somaTotalCarolina <= 0) {
                for (i = 0; i < 5; i++) {
                    auxCarolina.add(Calendar.DAY_OF_MONTH, -1);
                    String diaAnt = dataAntString(auxCarolina);
                    this.conecta();
                    ResultSet rsAntCarolina = this.execRetorna("SELECT totalUnidade FROM Unidade WHERE codArea ='" + codArea + "' and codTemperatura='" + diaAnt + "' and codTipo='" + 1 + "'");
                    if (rsAntCarolina.next()) {
                        totaisCarolinafinal.add(rsAntCarolina.getString("totalUnidade"));
                        diasNegativosCarolinafinal.add(diaAnt);
                    }
                    this.desconecta();
                }
                if (totaisCarolinafinal.size() == 5) {
                    for (String totaisCarolina1 : totaisCarolinafinal) {
                        if (Float.parseFloat(totaisCarolina1) <= 0) {
                            contaNegativosCarolina++;
                        }
                    }
                    if (contaNegativosCarolina == 5) {
                        for (int j = 0; j < diasNegativosCarolinafinal.size(); j++) {
                            this.execUp("UPDATE Unidade set totalUnidade ='" + "0" + "' where codArea='" + codArea + "' "
                                    + "and codTemperatura='" + diasNegativosCarolinafinal.get(j) + "' and codTipo='1'");

                        }

                    }
                }
                for (String totais2 : totaisCarolinafinal) {
                    System.out.println("Lista Negativos Carolina: " + totais2);
                }
                System.out.println("");
            }

            // Atualiando total dia anterior Ponderada **************************************************
            this.conecta();
            ResultSet resultTotalPonderada = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + " and codTipo = '3'");

            float auxtotalPonde = 0;
            float auxtotalPonde1 = 0;
            while (resultTotalPonderada.next()) {
                auxtotalPonde += Float.parseFloat(resultTotalPonderada.getString("totalUnidade"));
                auxtotalPonde1 = Float.parseFloat(resultTotalPonderada.getString("totalUnidade"));
            }
            auxtotalPonde = auxtotalPonde - auxtotalPonde1;

            this.desconecta();

            float auxcalc2 = 0;

            this.conecta();
            ResultSet resultPonderada = execRetorna("SELECT * from Unidade where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo = '3'");

            float somaTotalPonderada = 0;
            for (i = 22; i < 24; i++) {
                String coluna = "unidadeHora" + i;
                somaTotalPonderada += Float.parseFloat(resultPonderada.getString(coluna));
                auxtotalPonde += Float.parseFloat(resultPonderada.getString(coluna));
            }
            somaTotalPonderada = somaTotalPonderada + Float.parseFloat(resultPonderada.getString("totalUnidade"));
            if (somaTotalPonderada < 0) {
                auxcalc2 = auxtotalPonde;

                if (auxtotalPonde == 0) {
                    somaTotalPonderada = 0;
                }
            }
            System.out.println("Total Ponderada teste: " + somaTotalPonderada);
            this.desconecta();
            this.execUp("UPDATE Unidade set totalUnidade ='" + somaTotalPonderada + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + novaData + "' and codTipo='3'");
        
        //Finaliza a temporada 10/09
        String auxData = dataTemperatura.substring(5, 10);

            if(auxData.equals("09-30")){
            System.out.println("macaco peludo");
            //Finaliza UTAH dia 30 de setembro 
            this.conecta();
            GregorianCalendar auxFinal = new GregorianCalendar(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)) - 1, Integer.parseInt(data.substring(8, 10)));
            float totaldodia = 0;
            ResultSet rsFinal = this.execRetorna("SELECT totalUnidade FROM Unidade WHERE codArea ='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='" + 2 + "'");
            totaldodia=Float.parseFloat(rsFinal.getString("totalUnidade"));
            this.desconecta();
            String diaAnt = dataTemperatura;
                    
            while(totaldodia <= 0){
                this.conecta();    
                this.execUp("UPDATE Unidade set totalUnidade ='" + 0 + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + diaAnt    + "' and codTipo='2'");
                this.desconecta();
                
                    auxFinal.add(Calendar.DAY_OF_MONTH, -1);
                    diaAnt = dataAntString(auxFinal);
                    this.conecta();
                    rsFinal = this.execRetorna("SELECT totalUnidade FROM Unidade WHERE codArea ='" + codArea + "' and codTemperatura='" + diaAnt + "' and codTipo='" + 2 + "'");
                    if (rsFinal.next()) {
                        totaldodia=Float.parseFloat(rsFinal.getString("totalUnidade"));
                    }else{
                        this.desconecta();
                        break;
                         };
                    this.desconecta();
                }
            //Finaliza Carolina dia 30 de setembro 
            this.conecta();
            GregorianCalendar auxFinalC = new GregorianCalendar(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5, 7)) - 1, Integer.parseInt(data.substring(8, 10)));
            float totaldodiaC = 0;
            ResultSet rsFinalC = this.execRetorna("SELECT totalUnidade FROM Unidade WHERE codArea ='" + codArea + "' and codTemperatura='" + dataTemperatura + "' and codTipo='" + 2 + "'");
            totaldodiaC=Float.parseFloat(rsFinalC.getString("totalUnidade"));
            this.desconecta();
            String diaAntC = dataTemperatura;
                    
            while(totaldodiaC <= 0){
                this.conecta();    
                this.execUp("UPDATE Unidade set totalUnidade ='" + 0 + "' where codArea='" + codArea + "' "
                    + "and codTemperatura='" + diaAntC    + "' and codTipo='1'");
                this.desconecta();
                
                    auxFinalC.add(Calendar.DAY_OF_MONTH, -1);
                    diaAntC = dataAntString(auxFinalC);
                    this.conecta();
                    rsFinalC = this.execRetorna("SELECT totalUnidade FROM Unidade WHERE codArea ='" + codArea + "' and codTemperatura='" + diaAntC + "' and codTipo='" + 1 + "'");
                    if (rsFinalC.next()) {
                        totaldodiaC=Float.parseFloat(rsFinalC.getString("totalUnidade"));
                    }else{
                        this.desconecta();
                        break;
                         };
                    this.desconecta();
                }
        }
        }
    }

    /**
     *
     * @param gc
     * @return a data do dia anterior ao selecionado no jcombobox da classe
     * VTemperatura no formato yyyy-mm-dd
     */
    public String dataAntString(GregorianCalendar gc) {
        System.out.println("Data do metodo dataAntString: " + gc.getTime().toString());
        // dando problemas com datas mais antigas
        String dia = gc.getTime().toString().substring(8, 10);
        System.out.println("Dia: "+ dia);
        String mes = gc.getTime().toString().substring(4, 7);
        System.out.println("Mes: "+ mes);
        String ano;
        if (gc.getTime().toString().length() == 28) {
            ano = gc.getTime().toString().substring(24, 28);
        } else {
            ano = gc.getTime().toString().substring(25, 29);
        }
          System.out.println("Ano:" + ano);
        
        String numMes = "00"; //Nunca deve ocorrer

        //String dataAntString= "00";
        if (mes.equals("Jan")) {
            numMes = "01";
        }
        if (mes.equals("Feb")) {
            numMes = "02";
        }
        if (mes.equals("Mar")) {
            numMes = "03";
        }
        if (mes.equals("Apr")) {
            numMes = "04";
        }
        if (mes.equals("May")) {
            numMes = "05";
        }
        if (mes.equals("Jun")) {
            numMes = "06";
        }
        if (mes.equals("Jul")) {
            numMes = "07";
        }
        if (mes.equals("Aug")) {
            numMes = "08";
        }
        if (mes.equals("Sep")) {
            numMes = "09";
        }
        if (mes.equals("Oct")) {
            numMes = "10";
        }
        if (mes.equals("Nov")) {
            numMes = "11";
        }
        if (mes.equals("Dec")) {
            numMes = "12";
        }

        String novaData = ano + "-" + numMes + "-" + dia;
        System.out.println("testando ver o problema: " + ano);
        System.out.println("Nova Data Metodos Conectando: " + novaData);
        return novaData;
    }

    /**
     *
     * @param tabela que deseja saber o numero de linhas
     * @return Numero de linhas da tabela passada como parametro
     * @throws Exception
     */
    public int numeroLinhas(String tabela, String area) throws Exception {
        this.conecta();
        ResultSet rs = this.execRetorna("SELECT count(*) as conta FROM " + tabela + " WHERE nomeArea='" + area + "'");
        String i = rs.getString("conta");
        //System.out.println("Numero de linhas: "+i);
        int linhas = Integer.parseInt(i);
        return linhas;
    }

    public void calculaQuinzena(String area) throws Exception {
        this.conecta();
        ResultSet resultquinzena = this.execRetorna("SELECT * FROM consultaArea where nomearea = '" + area + "'");
        String data = null;
        if (resultquinzena.next()) {
            resultquinzena = this.execRetorna("SELECT * FROM consultaArea where nomearea = '" + area + "'");
            int codArea = Integer.parseInt(resultquinzena.getString("codArea"));
            float mes, dia;
            float auxC = 0, auxU = 0, auxP = 0, auxdec = 0;
            float auxzera = 0;
            String auxdecimal = null;
            String auxdecimal1 = null;

            ArrayList<Float> primeiraC = new ArrayList<>();
            ArrayList<Float> segundaC = new ArrayList<>();
            ArrayList<Float> primeiraU = new ArrayList<>();
            ArrayList<Float> segundaU = new ArrayList<>();
            ArrayList<Float> primeiraP = new ArrayList<>();
            ArrayList<Float> segundaP = new ArrayList<>();

            // valores ArrayList 0=abril 1=maio 2=junho 3=julho 4=agosto 5=setembro
            //inicializa o arraylist com 0 para economizar verificacao
            for (int i = 0; i < 6; i++) {
                primeiraC.add(auxzera);
                primeiraU.add(auxzera);
                primeiraP.add(auxzera);
                segundaC.add(auxzera);
                segundaU.add(auxzera);
                segundaP.add(auxzera);

            }

            while (resultquinzena.next()) {
                data = resultquinzena.getString("codTemperatura");
                mes = Float.parseFloat(data.substring(5, 7));
                dia = Float.parseFloat(data.substring(8, 10));
                System.out.println("dia " + dia + " mes " + mes);
                if (mes == 4) {
                    if (dia < 16) {
                        auxC = primeiraC.get(0) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        primeiraC.set(0, auxC);
                        auxU = primeiraU.get(0) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        primeiraU.set(0, auxU);
                        auxP = primeiraP.get(0) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        primeiraP.set(0, auxP);
                    } else {
                        auxC = segundaC.get(0) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        segundaC.set(0, auxC);
                        auxU = segundaU.get(0) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        segundaU.set(0, auxU);
                        auxP = segundaP.get(0) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        segundaP.set(0, auxP);
                    }
                }
                if (mes == 5) {
                    if (dia < 16) {
                        auxC = primeiraC.get(1) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        primeiraC.set(1, auxC);
                        auxU = primeiraU.get(1) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        primeiraU.set(1, auxU);
                        auxP = primeiraP.get(1) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        primeiraP.set(1, auxP);
                    } else {
                        auxC = segundaC.get(1) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        segundaC.set(1, auxC);
                        auxU = segundaU.get(1) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        segundaU.set(1, auxU);
                        auxP = segundaP.get(1) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        segundaP.set(1, auxP);
                    }
                }
                if (mes == 6) {
                    if (dia < 16) {
                        auxC = primeiraC.get(2) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        primeiraC.set(2, auxC);
                        auxU = primeiraU.get(2) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        primeiraU.set(2, auxU);
                        auxP = primeiraP.get(2) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        primeiraP.set(2, auxP);
                    } else {
                        auxC = segundaC.get(2) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        segundaC.set(2, auxC);
                        auxU = segundaU.get(2) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        segundaU.set(2, auxU);
                        auxP = segundaP.get(2) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        segundaP.set(2, auxP);
                    }
                }
                if (mes == 7) {
                    if (dia < 16) {
                        auxC = primeiraC.get(3) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        primeiraC.set(3, auxC);
                        auxU = primeiraU.get(3) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        primeiraU.set(3, auxU);
                        auxP = primeiraP.get(3) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        primeiraP.set(3, auxP);
                    } else {
                        auxC = segundaC.get(3) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        segundaC.set(3, auxC);
                        auxU = segundaU.get(3) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        segundaU.set(3, auxU);
                        auxP = segundaP.get(3) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        segundaP.set(3, auxP);
                    }
                }
                if (mes == 8) {
                    if (dia < 16) {
                        auxC = primeiraC.get(4) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        primeiraC.set(4, auxC);
                        auxU = primeiraU.get(4) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        primeiraU.set(4, auxU);
                        auxP = primeiraP.get(4) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        primeiraP.set(4, auxP);
                    } else {
                        auxC = segundaC.get(4) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        segundaC.set(4, auxC);
                        auxU = segundaU.get(4) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        segundaU.set(4, auxU);
                        auxP = segundaP.get(4) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        segundaP.set(4, auxP);
                    }
                }
                if (mes == 9) {
                    if (dia < 16) {
                        auxC = primeiraC.get(5) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        primeiraC.set(5, auxC);
                        auxU = primeiraU.get(5) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        primeiraU.set(5, auxU);
                        auxP = primeiraP.get(5) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        primeiraP.set(5, auxP);
                    } else {
                        auxC = segundaC.get(5) + Float.parseFloat(resultquinzena.getString("totalCarolina"));
                        segundaC.set(5, auxC);
                        auxU = segundaU.get(5) + Float.parseFloat(resultquinzena.getString("totalUtah"));
                        segundaU.set(5, auxU);
                        auxP = segundaP.get(5) + Float.parseFloat(resultquinzena.getString("totalPadrao"));
                        segundaP.set(5, auxP);
                    }
                }
            }
            System.out.println("");

            java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
            nf.setMinimumFractionDigits(4);
            nf.setMaximumFractionDigits(4);

            for (int i = 0; i < 6; i++) {
                auxdecimal = nf.format(primeiraC.get(i));
                auxdecimal = auxdecimal.replaceAll(",", ".");
                primeiraC.set(i, Float.parseFloat(auxdecimal));

                auxdecimal1 = nf.format(segundaC.get(i));
                auxdecimal1 = auxdecimal1.replaceAll(",", ".");
                segundaC.set(i, Float.parseFloat(auxdecimal1));

                auxdecimal = nf.format(primeiraU.get(i));
                auxdecimal = auxdecimal.replaceAll(",", ".");
                primeiraU.set(i, Float.parseFloat(auxdecimal));

                auxdecimal1 = nf.format(segundaU.get(i));
                auxdecimal1 = auxdecimal1.replaceAll(",", ".");
                segundaU.set(i, Float.parseFloat(auxdecimal1));

                auxdecimal = nf.format(primeiraP.get(i));
                auxdecimal = auxdecimal.replaceAll(",", ".");
                primeiraP.set(i, Float.parseFloat(auxdecimal));

                auxdecimal1 = nf.format(segundaP.get(i));
                auxdecimal1 = auxdecimal1.replaceAll(",", ".");
                segundaP.set(i, Float.parseFloat(auxdecimal1));

            }

            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Abril" + "', '" + "1ª Quinzena" + "', '" + primeiraC.get(0) + "', '" + primeiraU.get(0) + "', '" + primeiraP.get(0) + "')");
            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Abril" + "', '" + "2ª Quinzena" + "', '" + segundaC.get(0) + "', '" + segundaU.get(0) + "', '" + segundaP.get(0) + "')");

            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Maio" + "', '" + "1ª Quinzena" + "', '" + primeiraC.get(1) + "', '" + primeiraU.get(1) + "', '" + primeiraP.get(1) + "')");
            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Maio" + "', '" + "2ª Quinzena" + "', '" + segundaC.get(1) + "', '" + segundaU.get(1) + "', '" + segundaP.get(1) + "')");

            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Junho" + "', '" + "1ª Quinzena" + "', '" + primeiraC.get(2) + "', '" + primeiraU.get(2) + "', '" + primeiraP.get(2) + "')");
            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Junho" + "', '" + "2ª Quinzena" + "', '" + segundaC.get(2) + "', '" + segundaU.get(2) + "', '" + segundaP.get(2) + "')");

            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Julho" + "', '" + "1ª Quinzena" + "', '" + primeiraC.get(3) + "', '" + primeiraU.get(3) + "', '" + primeiraP.get(3) + "')");
            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Julho" + "', '" + "2ª Quinzena" + "', '" + segundaC.get(3) + "', '" + segundaU.get(3) + "', '" + segundaP.get(3) + "')");

            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Agosto" + "', '" + "1ª Quinzena" + "', '" + primeiraC.get(4) + "', '" + primeiraU.get(4) + "', '" + primeiraP.get(4) + "')");
            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Agosto" + "', '" + "2ª Quinzena" + "', '" + segundaC.get(4) + "', '" + segundaU.get(4) + "', '" + segundaP.get(4) + "')");

            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Setembro" + "', '" + "1ª Quinzena" + "', '" + primeiraC.get(5) + "', '" + primeiraU.get(5) + "', '" + primeiraP.get(5) + "')");
            this.execUp("INSERT INTO quinzena " + "(codArea, nomeArea, mes, quinzena, totalCarolina, totalUtah, totalPadrao)"
                    + "values ('" + codArea + "', '" + area + "', '" + "Setembro" + "', '" + "2ª Quinzena" + "', '" + segundaC.get(5) + "', '" + segundaU.get(5) + "', '" + segundaP.get(5) + "')");

            this.desconecta();

        }
    }
}
