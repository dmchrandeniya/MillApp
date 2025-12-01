/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.Inventory;
import controller.InventoryCategory;
import controller.InventoryController;
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
public final class ManageInventoryPanel extends javax.swing.JPanel {

    MainView mainView;
    InventoryPanel inventoryPanel;
    Inventory inventory;

    /**
     * Creates new form ManageInventoryPanel
     *
     * @param mainView
     * @param inventoryPanel
     * @param inventory
     */
    public ManageInventoryPanel(MainView mainView, InventoryPanel inventoryPanel, Inventory inventory) {
        initComponents();
        dtpDate.setFormats("yyyy-M-dd");
        this.mainView = mainView;
        this.inventoryPanel = inventoryPanel;
        this.inventory = inventory;
        if (this.inventory != null) {
            loadCategoryInupdate(this.inventory.getCategoryId());
            txtItemName.setText(this.inventory.getName());
            dtpDate.setDate(Date.valueOf(this.inventory.getDate()));
            txtPrice.setText(String.valueOf(this.inventory.getPrice()));
            txtNote.setText(this.inventory.getNote());
            btnSave.setText("Update");
        } else {
            loadCategory();
        }
        txtItemName.grabFocus();
    }

    public void loadCategory() {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) cmbItemCategory.getModel();
        dcbm.removeAllElements();
        String sql = "SELECT * FROM `inventory_category`;";
        Inventory i = new Inventory();
        ResultSet rs = i.read(sql);
        try {
            while (rs.next()) {
                InventoryCategory ic = new InventoryCategory();
                ic.setId(rs.getInt("id"));
                ic.setCategory(rs.getString("category"));
                dcbm.addElement(ic);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageInventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadCategoryInupdate(int categoryId) {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) cmbItemCategory.getModel();
        dcbm.removeAllElements();
        String sql1 = "SELECT * FROM `inventory_category` WHERE `id`='" + categoryId + "';";
        InventoryCategory ic = new InventoryCategory();
        ResultSet rs1 = ic.read(sql1);
        try {
            if (rs1.last()) {
                ic.setId(rs1.getInt("id"));
                ic.setCategory(rs1.getString("category"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageInventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        dcbm.addElement(ic);
        String sql2 = "SELECT * FROM `inventory_category` WHERE not `id`='" + categoryId + "';";
        ResultSet rs2 = ic.read(sql2);
        try {
            while (rs2.next()) {
                InventoryCategory ic1 = new InventoryCategory();
                ic1.setId(rs2.getInt("id"));
                ic1.setCategory(rs2.getString("category"));
                dcbm.addElement(ic1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageInventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        if (mainView.isEmptyField(txtItemName, "Please enter the item name.")) {
            return;
        }
        if (mainView.isEmptyDatePicker(dtpDate, "Please select the date.")) {
            return;
        }
        if (mainView.isEmptyField(txtPrice, "Please enter the item price.")) {
            return;
        }
        try {
            Double.valueOf(txtPrice.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.");
            return;
        }
        if (inventory != null) {
            Inventory i = new Inventory();
            i.setId(inventory.getId());
            InventoryCategory ic = (InventoryCategory) cmbItemCategory.getSelectedItem();
            i.setCategoryId(ic.getId());
            i.setName(txtItemName.getText());
            Date d = new Date(dtpDate.getDate().getTime());
            i.setDate(d.toLocalDate());
            i.setPrice(Double.parseDouble(txtPrice.getText()));
            i.setNote(txtNote.getText());
            InventoryController ic1 = new InventoryController();
            boolean b = ic1.update(i);
            if (b) {
                inventoryPanel.loadTable("All", "");
                inventoryPanel.setCost();
                mainView.selectComponent(inventoryPanel);
                mainView.removeTab(this);
            }
        } else {
            Inventory i = new Inventory();
            InventoryCategory ic = (InventoryCategory) cmbItemCategory.getSelectedItem();
            i.setCategoryId(ic.getId());
            i.setName(txtItemName.getText());
            Date d = new Date(dtpDate.getDate().getTime());
            i.setDate(d.toLocalDate());
            i.setPrice(Double.parseDouble(txtPrice.getText()));
            i.setNote(txtNote.getText());
            InventoryController ic1 = new InventoryController();
            boolean b = ic1.create(i);
            if (b) {
                inventoryPanel.loadTable("All", "");
                inventoryPanel.setCost();
                mainView.selectComponent(inventoryPanel);
                mainView.removeTab(this);
            }
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
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmbItemCategory = new javax.swing.JComboBox<>();
        txtItemName = new javax.swing.JTextField();
        dtpDate = new org.jdesktop.swingx.JXDatePicker();
        txtPrice = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        btnManageCategory = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        jLabel6.setText("jLabel6");

        jTextField1.setText("jTextField1");

        jButton4.setText("jButton4");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Manage inventory panel");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Category :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Item Name :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Date :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Price :");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Note :");

        cmbItemCategory.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        txtItemName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemNameKeyReleased(evt);
            }
        });

        dtpDate.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        txtPrice.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPriceKeyReleased(evt);
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

        btnManageCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/add.png"))); // NOI18N
        btnManageCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageCategoryActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridBagLayout());

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
        jPanel2.add(btnClose, gridBagConstraints);

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
        jPanel2.add(btnSave, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(txtPrice)
                            .addComponent(dtpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtItemName)
                            .addComponent(cmbItemCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnManageCategory)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbItemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnManageCategory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(dtpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 449, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtItemNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtPrice.grabFocus();
        }
    }//GEN-LAST:event_txtItemNameKeyReleased

    private void txtPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtNote.grabFocus();
        }        
     
    }//GEN-LAST:event_txtPriceKeyReleased

    private void txtNoteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoteKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            save();
        }
    }//GEN-LAST:event_txtNoteKeyReleased

    private void btnManageCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageCategoryActionPerformed
        ItemCategoryPanel icp=new ItemCategoryPanel(mainView,this);
        mainView.addTab("Manage Inventory Items", icp);
    }//GEN-LAST:event_btnManageCategoryActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnManageCategory;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbItemCategory;
    private org.jdesktop.swingx.JXDatePicker dtpDate;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextArea txtNote;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables
}
