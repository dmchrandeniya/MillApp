/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.WoodLoad;
import controller.WoodLoadController;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.UiSettings;

/**
 *
 * @author Chalindu
 */
public class WoodLoadPanel extends javax.swing.JPanel {

    MainView mainView;
    WoodLoad woodLoad;
    WoodLoadInputPanel woodLoadInputPanel;

    /**
     * Creates new form WoodLoadPanel
     *
     * @param mainView
     * @param woodLoadInputPanel
     */
    public WoodLoadPanel(MainView mainView, WoodLoadInputPanel woodLoadInputPanel) {
        initComponents();
        this.mainView = mainView;
        this.woodLoadInputPanel = woodLoadInputPanel;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblLoad);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();
    }

    public WoodLoadPanel(MainView mainView) {
        initComponents();
        this.mainView=mainView;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblLoad);
            txtSearch.grabFocus();
        });
        t.start();
    }
    
    

    public void loadTable(String searchBy, String value) {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnextend.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblLoad.getModel();
        dtm.setRowCount(0);
        woodLoad = new WoodLoad();
        String sql = "SELECT * FROM `wood_load_employee`";
        if (value.length() > 0) {
            if (searchBy.equalsIgnoreCase("Date")) {
                sql += "WHERE `date` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Customer name")) {
                sql += "WHERE `customer_name` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Pieces")) {
                sql += "WHERE `pieces LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Permit no")) {
                sql += "WHERE `permit_no` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Employee 1")) {
                sql += "WHERE `emp_name_1` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Employee 2")) {
                sql += "WHERE `emp_name_2` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("All")) {
                sql += "WHERE `date` LIKE '%" + value + "%' OR `customer_name` LIKE '%" + value + "%' OR `pieces` LIKE '%" + value + "%' OR `permit_no` LIKE '%" + value + "%' OR `emp_name_1` LIKE '%" + value + "%' OR `emp_name_2` LIKE '%" + value + "%';";
            }
        } else {
            sql += ";";
        }
        ResultSet rs = woodLoad.read(sql);
        try {
            while (rs.next()) {
                WoodLoad wl = new WoodLoad();
                wl.setId(rs.getInt("id"));
                wl.setCustomerId(rs.getInt("customer_id"));
                wl.setDate(rs.getDate("date").toLocalDate());
                wl.setEmpId1(rs.getInt("emp_id_1"));
                wl.setEmpId2(rs.getInt("emp_id_2"));
                wl.setPieces(rs.getInt("pieces"));
                wl.setPermitNo(rs.getString("permit_no"));
                ArrayList<Object> al = new ArrayList();
                al.add(wl);
                al.add(rs.getString("customer_name"));
                al.add(wl.getPieces());
                al.add(wl.getPermitNo());
                al.add(rs.getString("emp_name_1"));
                al.add(rs.getString("emp_name_2"));
                dtm.addRow(al.toArray());

            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void searchCustomer(int customerId){
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnextend.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblLoad.getModel();
        dtm.setRowCount(0);
        woodLoad = new WoodLoad();
        String s1="SELECT `name` FROM `customer` WHERE `id`='"+String.valueOf(customerId)+"';";
        ResultSet r=woodLoad.read(s1);
        String name="";
        try {
            if(r.first()){
                name=r.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtSearch.setText(name);
        String s2 = "SELECT * FROM `wood_load_employee` WHERE `customer_id`='"+String.valueOf(customerId)+"';";
        ResultSet rs = woodLoad.read(s2);
        try {
            while (rs.next()) {
                WoodLoad wl = new WoodLoad();
                wl.setId(rs.getInt("id"));
                wl.setCustomerId(rs.getInt("customer_id"));
                wl.setDate(rs.getDate("date").toLocalDate());
                wl.setEmpId1(rs.getInt("emp_id_1"));
                wl.setEmpId2(rs.getInt("emp_id_2"));
                wl.setPieces(rs.getInt("pieces"));
                wl.setPermitNo(rs.getString("permit_no"));
                ArrayList<Object> al = new ArrayList();
                al.add(wl);
                al.add(rs.getString("customer_name"));
                al.add(wl.getPieces());
                al.add(wl.getPermitNo());
                al.add(rs.getString("emp_name_1"));
                al.add(rs.getString("emp_name_2"));
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
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

        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cmbSelect = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLoad = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnextend = new javax.swing.JButton();

        jButton4.setText("jButton4");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Wood load panel");

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/drop.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/add.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jPanel3.setLayout(new java.awt.GridBagLayout());

        cmbSelect.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "Date", "Customer name", "Pieces", "Permit no", "Employee 1", "Employee 2", " " }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 56;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 11, 0);
        jPanel3.add(cmbSelect, gridBagConstraints);

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 225;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 11, 10);
        jPanel3.add(txtSearch, gridBagConstraints);

        tblLoad.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblLoad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Customer name", "pieces", "permit No", "Emp 1", "Emp 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLoad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoadMouseClicked(evt);
            }
        });
        tblLoad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblLoadKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblLoad);
        if (tblLoad.getColumnModel().getColumnCount() > 0) {
            tblLoad.getColumnModel().getColumn(0).setResizable(false);
            tblLoad.getColumnModel().getColumn(2).setResizable(false);
            tblLoad.getColumnModel().getColumn(3).setResizable(false);
            tblLoad.getColumnModel().getColumn(4).setResizable(false);
            tblLoad.getColumnModel().getColumn(5).setResizable(false);
        }

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnextend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/forword.png"))); // NOI18N
        btnextend.setText("Extend");
        btnextend.setEnabled(false);
        btnextend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnextendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnextend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 314, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnextend))
                .addContainerGap())
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnDelete)
                    .addComponent(btnEdit)
                    .addComponent(btnAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tblLoadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoadMouseClicked
        if (tblLoad.getRowCount() > 0 && tblLoad.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnextend.setEnabled(true);
            woodLoad = (WoodLoad) tblLoad.getValueAt(tblLoad.getSelectedRow(), 0);
            if (woodLoadInputPanel != null && evt.getClickCount() == 2) {
                woodLoadInputPanel.setWoodLoadId(woodLoad.getId());
                mainView.selectComponent(woodLoadInputPanel);
                mainView.removeTab(this);
            }
        }
    }//GEN-LAST:event_tblLoadMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ManageWoodLoadPanel mwlp = new ManageWoodLoadPanel(mainView, this, null);
        mainView.addTab("Add wood load", mwlp);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        ManageWoodLoadPanel mwlp = new ManageWoodLoadPanel(mainView, this, woodLoad);
        mainView.addTab("Edit wodd load", mwlp);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        boolean b = woodLoad.checkWoodLoadDetails(woodLoad.getId());
        if (b) {
            JOptionPane.showMessageDialog(this, "You can't delete this wood load. because this wood load has records in the system.");
        } else {
            int i = JOptionPane.showConfirmDialog(this, "Do you want to delete this wood load?");
            if (i == 0) {
                WoodLoadController wlc = new WoodLoadController();
                boolean c = wlc.delete(woodLoad);
                if (c) {
                    String message = "Wood load successfully deleted.";
                    JOptionPane.showMessageDialog(this, message);
                } else {
                    String message = "could not deleted.";
                    JOptionPane.showMessageDialog(this, message);
                }
                loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblLoadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblLoadKeyReleased
        if (tblLoad.getRowCount() > 0 && tblLoad.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            woodLoad = (WoodLoad) tblLoad.getValueAt(tblLoad.getSelectedRow(), 0);
        }
    }//GEN-LAST:event_tblLoadKeyReleased

    private void btnextendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnextendActionPerformed
        WoodLoadInputPanel wlip = new WoodLoadInputPanel(mainView);
        wlip.setWoodLoadId(woodLoad.getId());
        mainView.addTab("Wood load input", wlip);
        mainView.removeTab(this);
    }//GEN-LAST:event_btnextendActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnextend;
    private javax.swing.JComboBox<String> cmbSelect;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLoad;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
