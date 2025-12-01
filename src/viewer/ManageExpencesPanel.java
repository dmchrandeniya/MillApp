/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewer;

import controller.Expences;
import controller.ExpencesCategory;
import controller.ExpencesController;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Chalindu
 */
public final class ManageExpencesPanel extends javax.swing.JPanel {

    MainView mainView;
    ExpencesPanel expencesPanel;
    Expences expences;
    
    /** Creates new form ManageExpencesPanel
     * @param mainView
     * @param expencesPanel
     * @param expences */
    public ManageExpencesPanel(MainView mainView,ExpencesPanel expencesPanel,Expences expences) {
        initComponents();
        this.mainView=mainView;
        this.expencesPanel=expencesPanel;
        this.expences=expences;
        dtpDate.setFormats("yyyy-M-dd");
        if(expences!=null){
            loadCategoryInUpdate(expences.getCategoryId());
            dtpDate.setDate(Date.valueOf(expences.getDate()));
            txtCost.setText(String.valueOf(expences.getCost()));
            txtNote.setText(expences.getNote());
            btnSave.setText("Update");
        }else{
            loadCategory();
        }
    }
    
    public void loadCategory() {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) cmbCategory.getModel();
        dcbm.removeAllElements();
        String sql = "SELECT * FROM `expences_category`;";
        Expences e=new Expences();
        ResultSet rs = e.read(sql);
        try {
            while (rs.next()) {
                ExpencesCategory ec=new ExpencesCategory();
                ec.setId(rs.getInt("id"));
                ec.setCategory(rs.getString("category"));
                dcbm.addElement(ec);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageInventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void loadCategoryInUpdate(int categoryId){
        DefaultComboBoxModel dcbm=(DefaultComboBoxModel) cmbCategory.getModel();
        dcbm.removeAllElements();
        String s1="SELECT * FROM `expences_category` WHERE `id`='"+String.valueOf(categoryId)+"';";
        ExpencesCategory ec=new ExpencesCategory();
        ResultSet rs1=ec.read(s1);
        try {
            if(rs1.last()){
                ec.setId(rs1.getInt("id"));
                ec.setCategory(rs1.getString("category"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageExpencesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        dcbm.addElement(ec);
        String s2="SELECT * FROM `expences_category` WHERE NOT `id`='"+String.valueOf(categoryId)+"';";
        ResultSet rs2=ec.read(s2);
        try {
            while(rs2.next()){
                ec=new ExpencesCategory();
                ec.setId(rs2.getInt("id"));
                ec.setCategory(rs2.getString("category"));
                dcbm.addElement(ec);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageExpencesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void save(){
        if(mainView.isEmptyDatePicker(dtpDate, "Please select the date.")){
            return;
        }
        if(mainView.isEmptyField(txtCost, "Please enter the cost.")){
            return;
        }
        try {
            Double.valueOf(txtCost.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "cost not valid");
            return;
        }
        if(expences!=null){
            Expences e=new Expences();
            e.setId(expences.getId());
            ExpencesCategory ec=(ExpencesCategory)cmbCategory.getSelectedItem();
            e.setCategoryId(ec.getId());
            Date d=new Date(dtpDate.getDate().getTime());
            e.setDate(d.toLocalDate());
            e.setCost(Double.parseDouble(txtCost.getText()));
            e.setNote(txtNote.getText());
            ExpencesController ec1=new ExpencesController();
            boolean b=ec1.update(e);
            if(b){
                expencesPanel.loadTable("All", "");
                mainView.selectComponent(expencesPanel);
                mainView.removeTab(this);
            }
        }else{
            Expences e=new Expences();
            ExpencesCategory ec=(ExpencesCategory)cmbCategory.getSelectedItem();
            e.setCategoryId(ec.getId());
            Date d=new Date(dtpDate.getDate().getTime());
            e.setDate(d.toLocalDate());
            e.setCost(Double.parseDouble(txtCost.getText()));
            e.setNote(txtNote.getText());
            ExpencesController ec1=new ExpencesController();
            boolean b=ec1.create(e);
            if(b){
                expencesPanel.loadTable("All", "");
                mainView.selectComponent(expencesPanel);
                mainView.removeTab(this);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        dtpDate = new org.jdesktop.swingx.JXDatePicker();
        txtCost = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnEditCategory = new javax.swing.JButton();

        jLabel5.setText("jLabel5");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Manage expences panel");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Category :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Cost :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Note :");

        cmbCategory.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        dtpDate.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        txtCost.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostKeyReleased(evt);
            }
        });

        txtNote.setColumns(20);
        txtNote.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtNote.setRows(5);
        txtNote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoteKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtNote);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 11, 10);
        jPanel1.add(btnClose, gridBagConstraints);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/save.png"))); // NOI18N
        btnSave.setText("Add");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        jPanel1.add(btnSave, gridBagConstraints);

        btnEditCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/add.png"))); // NOI18N
        btnEditCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1)
                                .addComponent(txtCost)
                                .addComponent(dtpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditCategory)
                .addContainerGap(255, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditCategory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dtpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtNoteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoteKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            save();
        }
    }//GEN-LAST:event_txtNoteKeyReleased

    private void txtCostKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtNote.grabFocus();
        }
    }//GEN-LAST:event_txtCostKeyReleased

    private void btnEditCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCategoryActionPerformed
        ExpensesCategoryPanel ecp=new ExpensesCategoryPanel(mainView,this);
        mainView.addTab("Manage Expenses category",ecp);
    }//GEN-LAST:event_btnEditCategoryActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnEditCategory;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCategory;
    private org.jdesktop.swingx.JXDatePicker dtpDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCost;
    private javax.swing.JTextArea txtNote;
    // End of variables declaration//GEN-END:variables

}
