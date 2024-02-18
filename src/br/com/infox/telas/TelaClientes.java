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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
// a linha abaixo importa recursos da bibliote ca rs2xml.jar
import net.proteanit.sql.DbUtils;

/*
 *
 * @author Pedro
 */
public class TelaClientes extends javax.swing.JInternalFrame {
    // usando a variavel conexão do DAL

    Connection conexao = null;
    // Criando variáveis especiais para conexão com o banco
    // Prepared Statement e ResultSet sao frameworks do pacote java.sql
    // e servem para preapara e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCLientes
     */
    public TelaClientes() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    // metodo para adicionar clientes
    private void adicionar() {
        String sql = "insert into tbclientes (nomecli, endcli, fonecli,emailcli) values(?,?,?,?)";

        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliFone.getText());
            pst.setString(4, txtCliEmail.getText());

            if ((txtCliNome.getText().isEmpty()) || (txtCliFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Prencha todos os campos obrigatórios");
                if (txtCliNome.getText().isEmpty()) {
                    txtCliNome.setBackground(Color.red);
                } else {
                    txtCliNome.setBackground(Color.white);
                }
                if (txtCliFone.getText().isEmpty()) {
                    txtCliFone.setBackground(Color.red);
                } else {
                    txtCliFone.setBackground(Color.white);
                }
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente Cadastrado com sucesso");
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para adicionar clientes pelo nome com filtro
    private void pesquisar() {
        String sql = "select idcli as Id, nomecli as Nome, endcli as Endereço, fonecli as Telefone, emailcli as Email from tbclientes where nomecli like ? ";
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

    // metodo para setar os campos do formulario com o conteudo da tabela
    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEnd.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliFone.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliEmail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        txtCliNome.setBackground(Color.white);
        txtCliFone.setBackground(Color.white);
        // a linha abaixo desabilita o botao adicioar
        btnAdicionar.setEnabled(false);
    }

    // metodo para alterar cadastro de clientes
    private void alterar() {
        String sql = "update tbclientes set nomecli =?, endcli=?, fonecli =?, emailcli=? where idcli =?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliFone.getText());
            pst.setString(4, txtCliEmail.getText());
            pst.setString(5, txtCliId.getText());

            if ((txtCliNome.getText().isEmpty()) || (txtCliFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Prencha todos os campos obrigatórios");
                if (txtCliNome.getText().isEmpty()) {
                    txtCliNome.setBackground(Color.red);
                } else {
                    txtCliNome.setBackground(Color.white);
                }
                if (txtCliFone.getText().isEmpty()) {
                    txtCliFone.setBackground(Color.red);
                } else {
                    txtCliFone.setBackground(Color.white);
                }
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastro alterado com sucesso");
                    limpar();
                    btnAdicionar.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void deleta() {
        String sql = "delete from tbclientes where idcli = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliId.getText());
            int deletar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar esse regitro?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (deletar == JOptionPane.YES_OPTION) {
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso");
                limpar();
                btnAdicionar.setEnabled(true);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não é possivel deletar esse cliente pois ele tem algum serviço em espera!!");
        }
    }

    private void limpar() {
        txtCliPesquisar.setText(null);
        txtCliId.setText(null);
        txtCliNome.setText(null);
        txtCliEnd.setText(null);
        txtCliFone.setText(null);
        txtCliEmail.setText(null);
        txtCliNome.setBackground(Color.white);
        txtCliFone.setBackground(Color.white);
        ((DefaultTableModel)tblClientes.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCliEnd = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCliFone = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCliEmail = new javax.swing.JTextField();
        btnAlterar = new javax.swing.JButton();
        btnDeletar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Clientes");
        setPreferredSize(new java.awt.Dimension(640, 480));

        jLabel8.setText("* Campos Obrigatórios");

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search.png"))); // NOI18N

        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }

        };
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Nome", "Endereço", "Telefone", "Email"
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

        jLabel1.setText("* Nome");

        txtCliNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCliNomeKeyPressed(evt);
            }
        });

        jLabel6.setText("Endereço");

        txtCliEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCliEndKeyPressed(evt);
            }
        });

        jLabel9.setText("* Telefone");

        txtCliFone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCliFoneKeyPressed(evt);
            }
        });

        jLabel10.setText("E-mail");

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Atualizar");
        btnAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnDeletar.setToolTipText("Apagar");
        btnDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        jLabel2.setText("Id Cliente");

        txtCliId.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(86, 86, 86)
                                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(25, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(96, 96, 96))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // Chamando o metodo alterar
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        // chamando comando deleta
        deleta();
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // chamando o metodo adicionar
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed
    //  o evento abaixo é do tipo "enquanto for digitando"
    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // chamando o metodo pesquisar
        pesquisar();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void txtCliNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliNomeKeyPressed
        // vai para a a proxima caixa de texto com o enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCliEnd.requestFocus();
        }
    }//GEN-LAST:event_txtCliNomeKeyPressed

    private void txtCliEndKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliEndKeyPressed
        // vai para a a proxima caixa de texto com o enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCliFone.requestFocus();
        }
    }//GEN-LAST:event_txtCliEndKeyPressed

    private void txtCliFoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliFoneKeyPressed
        // vai para a a proxima caixa de texto com o enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCliEmail.requestFocus();
        }
    }//GEN-LAST:event_txtCliFoneKeyPressed
    // evento que será usado par setar os campos da tabela clicando com o mouse
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // chamando o metodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEnd;
    private javax.swing.JTextField txtCliFone;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    // End of variables declaration//GEN-END:variables
}
