/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.Customer;
import controller.CustomerController;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
public class CustomerPanel extends javax.swing.JPanel {

    MainView mainView;
    Customer customer;
    ManageWoodLoadPanel manageWoodLoadPanel;
    CustomerAmountPanel customerAmountPanel;
    CustomerAmountPanel cap;
    SalaryAndCostPanel sacp;
    /**
     * Creates new form CustomerPanel
     *
     * @param mainView
     * @param manageWoodLoadPanel
     * @param customerAmountPanel
     */
    public CustomerPanel(MainView mainView,ManageWoodLoadPanel manageWoodLoadPanel,CustomerAmountPanel customerAmountPanel) {
        initComponents();
        this.mainView = mainView;
        this.manageWoodLoadPanel=manageWoodLoadPanel;
        this.customerAmountPanel=customerAmountPanel;
        Thread t = new Thread(() -> {
            txtSearch.grabFocus();
            UiSettings.tableProperties(tblCustomer);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();

    }

    public CustomerPanel(MainView mainView,SalaryAndCostPanel sacp) {
        initComponents();
        this.mainView=mainView;
        this.sacp=sacp;
        Thread t = new Thread(() -> {
            txtSearch.grabFocus();
            UiSettings.tableProperties(tblCustomer);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();
    }

    public CustomerPanel(MainView mainView,CustomerAmountPanel cap) {
        initComponents();
        this.mainView=mainView;
        this.cap=cap;
        Thread t;
        t = new Thread(() -> {
            txtSearch.grabFocus();
            UiSettings.tableProperties(tblCustomer);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();
    }
    
    
    
    
    
    public void setSinceDateAndAmountToTake(){
        String sql = "SELECT `date` FROM `amount_taken` WHERE `cus_id`='" + customer.getId() + "' ORDER BY `date`;";
            ResultSet rs = customer.read(sql);
            String date;
            try {
                while (rs.next()) {
                    date = rs.getDate("date").toString();
                    txtSinceDate.setText(date);
                    break;
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        String s1 = "SELECT SUM(`charge`) AS cost FROM `load_details_employee` WHERE `customer_id`='" + customer.getId() + "';";
            ResultSet rs1 = customer.read(s1);
            double cost = 0;
            try {
                if (rs1.last()) {
                    cost = rs1.getDouble("cost");
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            String s2 = "SELECT sum(`amount`) AS full_amount FROM `amount_taken` WHERE `cus_id`='" + customer.getId() + "';";
            ResultSet rs2 = customer.read(s2);
            double fullAmount = 0;
            try {
                if (rs2.last()) {
                    fullAmount = rs2.getDouble("full_amount");
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            double amountToTake = cost - fullAmount;
            DecimalFormat df = new DecimalFormat("#,###.00");
            txtFullAmount.setText(String.valueOf(df.format(amountToTake)));         
    }

    public void loadTable(String searchBy, String value) {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblCustomer.getModel();
        dtm.setRowCount(0);
        customer = new Customer();
        String sql = "SELECT * FROM `customer`";
        if (value.length() > 0) {
            if (searchBy.equalsIgnoreCase("Name")) {
                sql = sql + " WHERE `name` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Address")) {
                sql = sql + " WHERE `address` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Phone")) {
                sql = sql + " WHERE `phone` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Note")) {
                sql = sql + " WHERE `note` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("All")) {
                sql = sql + " WHERE `name` LIKE '%" + value + "%' OR `address` LIKE '%" + value + "%' OR `phone` LIKE '%" + value + "%' OR `note` LIKE '%" + value + "%';";
            }
        } else {
            sql += ";";
        }
        ResultSet rs = customer.read(sql);
        try {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setAddress(rs.getString("address"));
                c.setPhone(rs.getString("phone"));
                c.setNote(rs.getString("note"));
                ArrayList<Object> al = new ArrayList();
                al.add(c);
                al.add(c.getAddress());
                al.add(c.getPhone());
                al.add(c.getNote());
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerPanel.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cmbSelect = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCustomer = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnExtend = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtSinceDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFullAmount = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton4.setText("jButton4");

        jLabel2.setText("jLabel2");

        jButton5.setText("jButton5");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Customer Panel");

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/drop.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/add.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        cmbSelect.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "Name", "Address", "Phone", "Note" }));

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Address", "Phone", "Note"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomerMouseClicked(evt);
            }
        });
        tblCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCustomerKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCustomer);
        if (tblCustomer.getColumnModel().getColumnCount() > 0) {
            tblCustomer.getColumnModel().getColumn(0).setResizable(false);
            tblCustomer.getColumnModel().getColumn(1).setResizable(false);
            tblCustomer.getColumnModel().getColumn(2).setResizable(false);
            tblCustomer.getColumnModel().getColumn(3).setResizable(false);
        }

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnExtend.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnExtend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/forword.png"))); // NOI18N
        btnExtend.setText("Extend");
        btnExtend.setEnabled(false);
        btnExtend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExtendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExtend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnExtend))
                .addContainerGap())
        );

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Since:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 87;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        jPanel5.add(jLabel7, gridBagConstraints);

        txtSinceDate.setEditable(false);
        txtSinceDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 114;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 10);
        jPanel5.add(txtSinceDate, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Amount to take: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel5.add(jLabel3, gridBagConstraints);

        txtFullAmount.setEditable(false);
        txtFullAmount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 114;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 11, 10);
        jPanel5.add(txtFullAmount, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(btnDelete)
                        .addComponent(btnEdit)
                        .addComponent(btnAdd))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tblCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomerMouseClicked
        if (tblCustomer.getRowCount() > 0 && tblCustomer.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnExtend.setEnabled(true);
            customer = (Customer) tblCustomer.getValueAt(tblCustomer.getSelectedRow(), 0);
            setSinceDateAndAmountToTake();
            if(manageWoodLoadPanel!=null && evt.getClickCount()==2){
                manageWoodLoadPanel.setCustomerId(customer.getId());
                mainView.selectComponent(manageWoodLoadPanel);
                mainView.removeTab(this);
            }else if(customerAmountPanel!=null && evt.getClickCount()==2){
                customerAmountPanel.setCustomerId(customer.getId());
                mainView.selectComponent(customerAmountPanel);
                mainView.removeTab(this);
            }else if(sacp!=null && evt.getClickCount()==2){
                sacp.setCustomerId(customer.getId());
                mainView.selectComponent(sacp);
                mainView.removeTab(this);
            }else if(cap!=null && evt.getClickCount()==2){
                cap.setCusId(customer.getId());
                mainView.selectComponent(cap);
                mainView.removeTab(this);
            }
        }
    }//GEN-LAST:event_tblCustomerMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ManageCustomerPanel mcp = new ManageCustomerPanel(mainView, this, null);
        mainView.addTab("Add customer", mcp);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        ManageCustomerPanel mcp = new ManageCustomerPanel(mainView, this, customer);
        mainView.addTab("Edit customer", mcp);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        boolean b = customer.checkWoddLoad(customer.getId());
        if (b) {
            JOptionPane.showMessageDialog(this, "You can't delete this customer. because this customer has records in the system.");
        } else {
            int i = JOptionPane.showConfirmDialog(this, "Do you want to delete this customer");
            if (i == 0) {
                CustomerController cc = new CustomerController();
                boolean c = cc.delete(customer);
                if (c) {
                    String message = "Customer " + customer.getName() + " successfully deleted.";
                    JOptionPane.showMessageDialog(this, message);
                } else {
                    String message = "could not deleted.";
                    JOptionPane.showMessageDialog(this, message);
                }
                loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblCustomerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCustomerKeyReleased
        if (tblCustomer.getRowCount() > 0 && tblCustomer.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            customer = (Customer) tblCustomer.getValueAt(tblCustomer.getSelectedRow(), 0);
            setSinceDateAndAmountToTake();
        }
    }//GEN-LAST:event_tblCustomerKeyReleased

    private void btnExtendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExtendActionPerformed
        WoodLoadPanel wlp = new WoodLoadPanel(mainView);
        wlp.searchCustomer(customer.getId());
        mainView.addTab("Wood load", wlp);
    }//GEN-LAST:event_btnExtendActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExtend;
    private javax.swing.JComboBox<String> cmbSelect;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCustomer;
    private javax.swing.JTextField txtFullAmount;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSinceDate;
    // End of variables declaration//GEN-END:variables
}
