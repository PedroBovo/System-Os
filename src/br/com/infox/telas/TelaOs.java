/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Pedro
 */
public class TelaOs extends javax.swing.JInternalFrame {
    // usando a variavel conexão do DAL

    Connection conexao = null;
    // Criando variáveis especiais para conexão com o banco
    // Prepared Statement e ResultSet sao frameworks do pacote java.sql
    // e servem para preapara e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    // a linha abaixo cria uma variavel para armazenar um texto de acordo com um radion button selecionado
    private String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOs() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void pesquisar() {
        String sql = "select idcli as Id, nomecli as Nome, fonecli as Telefone from tbclientes where nomecli like ? ";
        try {
            pst = conexao.prepareStatement(sql);
            // pasando o conteudo da caixa de pesquisa para o ?
            // atenção ao % que é a continuação da String sql
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
    }
    // metodo para cadastrar uma OS

    private void emitirOs() {
        String sql = "insert into tbos (tipo, situacao, equipamento, defeito, servico, tecnico, valor,idcli) values (?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboOsSit.getSelectedItem().toString());
            pst.setString(3, txtOsEquip.getText());
            pst.setString(4, txtOsDef.getText());
            pst.setString(5, txtOsServ.getText());
            pst.setString(6, txtOsTec.getText());
            // .replace subistitui o a virgula pelo ponto
            pst.setString(7, txtOsValor.getText().replace(",", "."));
            pst.setString(8, txtCliId.getText());
            if ((txtCliId.getText().isEmpty()) || (txtOsEquip.getText().isEmpty()) || (txtOsDef.getText().isEmpty()) || (cboOsSit.getSelectedItem().equals(" "))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
                if (txtCliId.getText().isEmpty()) {
                    txtCliId.setBackground(Color.red);
                } else {
                    txtCliId.setBackground(Color.white);
                }
                if (txtOsEquip.getText().isEmpty()) {
                    txtOsEquip.setBackground(Color.red);
                } else {
                    txtOsEquip.setBackground(Color.white);
                }
                if (txtOsDef.getText().isEmpty()) {
                    txtOsDef.setBackground(Color.red);
                } else {
                    txtOsDef.setBackground(Color.white);
                }
                
                if(cboOsSit.getSelectedItem().equals(" ")){
                    cboOsSit.setBackground(Color.red);
                }else{
                    cboOsSit.setBackground(Color.white);
                }
            } else {
                int emitido = pst.executeUpdate();
                if (emitido > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastro emitido com sucesso");
                    
                    limpar();
                    //recuperar o numero da os
                    recuperarOs();
                    btnOsImprimir.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // metodo para limpar os caches
    public void limpar() {
        txtCliPesquisar.setText(null);
        txtOsEquip.setText(null);
        txtOsDef.setText(null);
        txtOsServ.setText(null);
        txtOsTec.setText(null);
        txtOsValor.setText(null);
        txtCliId.setText(null);
        txtOs.setText(null);
        txtData.setText(null);
        txtCliPesquisar.setText(null);
        ((DefaultTableModel)tblClientes.getModel()).setRowCount(0);
        cboOsSit.setSelectedItem(" ");
        txtCliId.setBackground(Color.white);
        cboOsSit.setBackground(Color.white);
        txtOsDef.setBackground(Color.white);
        txtOsEquip.setBackground(Color.white);

        btnOsAdicionar.setEnabled(true);
        txtCliPesquisar.setEnabled(true);
        tblClientes.setVisible(true);
        
        // desabilitar os botoes
        btnOsAlterar.setEnabled(false);
        btnOsExcluir.setEnabled(false);
        btnOsImprimir.setEnabled(false);

    }

    // metodo par pesquisar Os
    private void pesquisarOs() {
        // a linha abaixo cria uma caixa de entrada do tipo JOpitonPane
        String num_os = JOptionPane.showInputDialog("Numero da OS");
        // fazendo a pesquisa no banco
        String sql = "select os, date_format(data_os,'%d/%m/%Y - %H:%i'), tipo, situacao, equipamento, defeito, servico, tecnico, valor, idcli from tbos where os =" + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtOs.setText(rs.getString(1));
                txtData.setText(rs.getString(2));
                // setando os radion buttons
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("OS")) {
                    rbtOs.setSelected(true);
                    tipo = "OS";
                } else {
                    rbtOrc.setSelected(true);
                    tipo = "Orçamento";
                }
                cboOsSit.setSelectedItem(rs.getString(4));
                txtOsEquip.setText(rs.getString(5));
                txtOsDef.setText(rs.getString(6));
                txtOsServ.setText(rs.getString(7));
                txtOsTec.setText(rs.getString(8));
                txtOsValor.setText(rs.getString(9));
                txtCliId.setText(rs.getString(10));
                btnOsAdicionar.setEnabled(false);
                txtCliPesquisar.setEnabled(false);
                tblClientes.setVisible(false);
                btnOsAlterar.setEnabled(true);
                btnOsExcluir.setEnabled(true);
                btnOsImprimir.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(null, "OS não cadastrada");
            }
        } catch (java.sql.SQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "OS invalida");
            //System.out.println(e);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }

    // metodo para alterar OS
    private void alterarOs() {
        String sql = "update tbos set tipo =?, situacao =?, equipamento=?, defeito=?, servico=?, tecnico=?, valor=? where os=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboOsSit.getSelectedItem().toString());
            pst.setString(3, txtOsEquip.getText());
            pst.setString(4, txtOsDef.getText());
            pst.setString(5, txtOsServ.getText());
            pst.setString(6, txtOsTec.getText());
            pst.setString(7, txtOsValor.getText());
            pst.setString(8, txtOs.getText());
            if ((txtCliId.getText().isEmpty()) ||(txtOsEquip.getText().isEmpty()) || (txtOsDef.getText().isEmpty())|| (cboOsSit.getSelectedItem().equals(" "))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
                if (txtCliId.getText().isEmpty()) {
                    txtCliId.setBackground(Color.red);
                } else {
                    txtCliId.setBackground(Color.white);
                }
                if (txtOsEquip.getText().isEmpty()) {
                    txtOsEquip.setBackground(Color.red);
                } else {
                    txtOsEquip.setBackground(Color.white);
                }
                if (txtOsDef.getText().isEmpty()) {
                    txtOsDef.setBackground(Color.red);
                } else {
                    txtOsDef.setBackground(Color.white);
                }
                
                if(cboOsSit.getSelectedItem().equals(" ")){
                    cboOsSit.setBackground(Color.red);
                }else{
                    cboOsSit.setBackground(Color.white);
                }

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS alterada com sucesso");
                    limpar();
                    

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // criando metodo para deletar uma Os
    private void deletaOs() {
        String sql = "delete from tbos where os =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtOs.getText());
            int opicao = JOptionPane.showConfirmDialog(null, "Tem certezq que dejesa deletar essa Os?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (opicao == JOptionPane.YES_OPTION) {
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "OS deletada com sucesso!");
                limpar();
               

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // metodo para imprimir uma os
    private void imprimir(){
        //imprimindo uma os
         int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão dessa OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            // imprimindo o relatório com o framework jasperReport
            try {
                //usando a classe HashMap para criar um filtro
                HashMap filtro = new HashMap();
                filtro.put("os", Integer.parseInt(txtOs.getText()));
                // Usando a classe JasperPrint para prepara a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/os.jasper"),filtro, conexao);
                // a linha abaixo exibe o relatórioa atraves da classe JasperViwer
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    // recuperar os
    private void recuperarOs(){
        String sql = "select max(os) from tbos";
        try {
            pst= conexao.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                txtOs.setText(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtOs = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        rbtOrc = new javax.swing.JRadioButton();
        rbtOs = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        cboOsSit = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtOsEquip = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtOsDef = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtOsServ = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtOsTec = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtOsValor = new javax.swing.JTextField();
        btnOsImprimir = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        btnOsAdicionar = new javax.swing.JButton();
        btnOsPesquisar = new javax.swing.JButton();
        btnOsAlterar = new javax.swing.JButton();
        btnOsExcluir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("OS");
        setPreferredSize(new java.awt.Dimension(640, 480));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Numero OS");

        jLabel2.setText("Data");

        txtOs.setEditable(false);

        txtData.setEditable(false);
        txtData.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N

        buttonGroup1.add(rbtOrc);
        rbtOrc.setText("Orçamento");
        rbtOrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrcActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOs);
        rbtOs.setText("OS");
        rbtOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtOs))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtOrc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbtOs)
                        .addGap(45, 45, 45))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOrc)
                    .addComponent(rbtOs))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jLabel3.setText("Situação");

        cboOsSit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Na Bancada", "Entrga OK", "Orçamento Reprovado", "Aguardando Aprovação ", "Aguardando peças", "Abandonado pelo cliente", "Retornou" }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search.png"))); // NOI18N

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel5.setText("* Id");

        txtCliId.setEditable(false);

        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Nome", "Fone"
            }
        ));
        tblClientes.setFocusable(false);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCliId, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jLabel6.setText("* Equipamento");

        txtOsEquip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsEquipActionPerformed(evt);
            }
        });
        txtOsEquip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOsEquipKeyPressed(evt);
            }
        });

        jLabel7.setText("* Defeito");

        txtOsDef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOsDefKeyPressed(evt);
            }
        });

        jLabel8.setText("Serviço");

        txtOsServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOsServActionPerformed(evt);
            }
        });
        txtOsServ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOsServKeyPressed(evt);
            }
        });

        jLabel9.setText("Tecnico");

        txtOsTec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOsTecKeyPressed(evt);
            }
        });

        jLabel10.setText("Valor Total");

        txtOsValor.setText("0");

        btnOsImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/print.png"))); // NOI18N
        btnOsImprimir.setToolTipText("Imprimir OS");
        btnOsImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsImprimir.setEnabled(false);
        btnOsImprimir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnOsImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsImprimirActionPerformed(evt);
            }
        });

        jLabel15.setText("* Campo Obrigatório");

        btnOsAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnOsAdicionar.setToolTipText("Adicionar");
        btnOsAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnOsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAdicionarActionPerformed(evt);
            }
        });

        btnOsPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/read.png"))); // NOI18N
        btnOsPesquisar.setToolTipText("pesquisar");
        btnOsPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnOsPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsPesquisarActionPerformed(evt);
            }
        });

        btnOsAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnOsAlterar.setToolTipText("Atualizar");
        btnOsAlterar.setEnabled(false);
        btnOsAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnOsAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAlterarActionPerformed(evt);
            }
        });

        btnOsExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnOsExcluir.setToolTipText("Deletar");
        btnOsExcluir.setEnabled(false);
        btnOsExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnOsExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(30, 30, 30)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtOsDef)
                    .addComponent(txtOsEquip)
                    .addComponent(txtOsServ)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtOsValor)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(btnOsAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnOsAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOsExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOsImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtOsEquip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtOsDef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtOsServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOsAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOsImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOsAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOsExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOsEquipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsEquipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsEquipActionPerformed
//  o evento abaixo é do tipo "enquanto for digitando"
    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // chamando o metodo pesquisar
        pesquisar();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        //chamando o metodo setar campos
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void rbtOrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrcActionPerformed
        // atribuindo um texto a variavel tipo se selecionado
        tipo = "Orçamento";
    }//GEN-LAST:event_rbtOrcActionPerformed

    private void rbtOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOsActionPerformed
        //a linha abaixo atribui um texto a variavel tipo se selecionado

        tipo = "OS";
    }//GEN-LAST:event_rbtOsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // ao abrir o form marcar o radion button orçamento
        rbtOrc.setSelected(true);
        tipo = "Orçamento";

    }//GEN-LAST:event_formInternalFrameOpened

    private void btnOsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAdicionarActionPerformed
        // atribuindo o metodo emitirOs
        emitirOs();
    }//GEN-LAST:event_btnOsAdicionarActionPerformed

    private void txtOsEquipKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsEquipKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtOsDef.requestFocus();
        }
    }//GEN-LAST:event_txtOsEquipKeyPressed

    private void txtOsDefKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsDefKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtOsServ.requestFocus();
        }
    }//GEN-LAST:event_txtOsDefKeyPressed

    private void txtOsServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOsServActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOsServActionPerformed

    private void txtOsServKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsServKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtOsTec.requestFocus();
        }
    }//GEN-LAST:event_txtOsServKeyPressed

    private void txtOsTecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOsTecKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtOsValor.requestFocus();
        }
    }//GEN-LAST:event_txtOsTecKeyPressed

    private void btnOsPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsPesquisarActionPerformed
        // chamando metodo pesquisarOs
        pesquisarOs();
    }//GEN-LAST:event_btnOsPesquisarActionPerformed

    private void btnOsAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAlterarActionPerformed
        // chamando metodo alterarOs
        alterarOs();
    }//GEN-LAST:event_btnOsAlterarActionPerformed

    private void btnOsExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsExcluirActionPerformed
        // chamando o metodo deletaOs
        deletaOs();
    }//GEN-LAST:event_btnOsExcluirActionPerformed

    private void btnOsImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsImprimirActionPerformed
        // chamando o metodo imprimir
        imprimir();
    }//GEN-LAST:event_btnOsImprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOsAdicionar;
    private javax.swing.JButton btnOsAlterar;
    private javax.swing.JButton btnOsExcluir;
    private javax.swing.JButton btnOsImprimir;
    private javax.swing.JButton btnOsPesquisar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboOsSit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbtOrc;
    private javax.swing.JRadioButton rbtOs;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtOs;
    private javax.swing.JTextField txtOsDef;
    private javax.swing.JTextField txtOsEquip;
    private javax.swing.JTextField txtOsServ;
    private javax.swing.JTextField txtOsTec;
    private javax.swing.JTextField txtOsValor;
    // End of variables declaration//GEN-END:variables
}
