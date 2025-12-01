/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.Customer;
import controller.Employee;
import controller.WoodLoad;
import controller.WoodLoadController;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Chalindu
 */
public final class ManageWoodLoadPanel extends javax.swing.JPanel {

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
        Customer c = new Customer();
        ResultSet rs = c.read("SELECT `name` FROM `customer` WHERE `id`='" + customerId + "';");
        try {
            if (rs.last()) {
                txtcustomer.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageWoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the employee1_id
     */
    public int getEmployee1_id() {
        return employee1_id;
    }
    
    public void setDefaultEmployee(){
        WoodLoad wl=new WoodLoad();
        ResultSet rs=wl.read("SELECT * FROM `default_employee`;");
        try {
            if(rs.last()){
                int emp1_id=rs.getInt("emp_1_id");
                int emp2_id=rs.getInt("emp_2_id");
                setEmployee1_id(emp1_id);
                setEmployee2_id(emp2_id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageWoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param employee1_id the employee1_id to set
     */
    public void setEmployee1_id(int employee1_id) {
        this.employee1_id = employee1_id;
        Employee e = new Employee();
        ResultSet rs = e.read("SELECT `name` FROM `employee` WHERE `id`='" + employee1_id + "';");
        try {
            if (rs.last()) {
                lblEmp1.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageWoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the employee2_id
     */
    public int getEmployee2_id() {
        return employee2_id;
    }

    /**
     * @param employee2_id the employee2_id to set
     */
    public void setEmployee2_id(int employee2_id) {
        this.employee2_id = employee2_id;
        Employee e = new Employee();
        ResultSet rs = e.read("SELECT `name` FROM `employee` WHERE `id`='" + employee2_id + "';");
        try {
            if (rs.last()) {
                lblEmp2.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageWoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    MainView mainView;
    WoodLoadPanel woodLoadPanel;
    WoodLoad woodLoad;

    private int customerId;
    private int employee1_id;
    private int employee2_id;

    /**
     * Creates new form ManageWoodLoadPanel
     *
     * @param mainview
     * @param woodLoadPanel
     * @param woodLoad
     */
    public ManageWoodLoadPanel(MainView mainview, WoodLoadPanel woodLoadPanel, WoodLoad woodLoad) {
        initComponents();
        dtpWoodLoad.setFormats("yyyy-M-dd");
        this.mainView = mainview;
        this.woodLoadPanel = woodLoadPanel;
        this.woodLoad = woodLoad;
        if (this.woodLoad != null) {
            btnSave.setText("Update");
            dtpWoodLoad.setDate(Date.valueOf(this.woodLoad.getDate()));
            setCustomerId(this.woodLoad.getCustomerId());
            boolean b = this.woodLoad.checkWoodLoadDetails(woodLoad.getId());
            if (b) {
                btnSelectCustomer.setEnabled(false);
            }
            txtPieces.setText(String.valueOf(this.woodLoad.getPieces()));
            txtPermitNo.setText(String.valueOf(this.woodLoad.getPermitNo()));
            setEmployee1_id(this.woodLoad.getEmpId1());
            setEmployee2_id(this.woodLoad.getEmpId2());
        }else{
            setDefaultEmployee();
            btnSave.setText("Add");
        }
    }

    public void save() {
        if (mainView.isEmptyDatePicker(dtpWoodLoad, "Please select the date.")) {
            return;
        }
        if (!(getCustomerId() > 0)) {
            JOptionPane.showMessageDialog(this, "Please select the customer.");
            return;
        }
        if (mainView.isEmptyField(txtPieces, "Please enter the pieces.")) {
            return;
        }
        try {
            Integer.valueOf(txtPieces.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            return;
        }
        if (mainView.isEmptyField(txtPermitNo, "Please enter the permit number or 'No'")) {
            return;
        }
        if (!(getEmployee1_id() > 0)) {
            JOptionPane.showMessageDialog(this, "Please select the employee 1!");
            return;
        }
        if (!(getEmployee2_id() > 0)) {
            JOptionPane.showMessageDialog(this, "Please select the employee 1!");
            return;
        }
        if (woodLoad != null) {
            WoodLoad wl = new WoodLoad();
            wl.setId(woodLoad.getId());
            Date d = new Date(dtpWoodLoad.getDate().getTime());
            wl.setDate(d.toLocalDate());
            wl.setCustomerId(getCustomerId());
            wl.setPieces(Integer.parseInt(txtPieces.getText()));
            wl.setPermitNo(txtPermitNo.getText());
            wl.setEmpId1(getEmployee1_id());
            wl.setEmpId2(getEmployee2_id());
            WoodLoadController wlc = new WoodLoadController();
            boolean b = wlc.Update(wl);
            if (b) {
                woodLoadPanel.loadTable("All", "");
                mainView.selectComponent(woodLoadPanel);
                mainView.removeTab(this);
            }
        } else {
            WoodLoad wl = new WoodLoad();
            Date d = new Date(dtpWoodLoad.getDate().getTime());
            wl.setDate(d.toLocalDate());
            wl.setCustomerId(getCustomerId());
            wl.setPieces(Integer.parseInt(txtPieces.getText()));
            wl.setPermitNo(txtPermitNo.getText());
            wl.setEmpId1(getEmployee1_id());
            wl.setEmpId2(getEmployee2_id());
            WoodLoadController wlc = new WoodLoadController();
            boolean b = wlc.create(wl);
            if (b) {
                woodLoadPanel.loadTable("All", "");
                mainView.selectComponent(woodLoadPanel);
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
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtcustomer = new javax.swing.JTextField();
        txtPieces = new javax.swing.JTextField();
        txtPermitNo = new javax.swing.JTextField();
        lblEmp1 = new javax.swing.JLabel();
        lblEmp2 = new javax.swing.JLabel();
        dtpWoodLoad = new org.jdesktop.swingx.JXDatePicker();
        btnSelectEmp1 = new javax.swing.JButton();
        btnSelectEmp2 = new javax.swing.JButton();
        btnSelectCustomer = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnSetDefaultEmployees = new javax.swing.JButton();

        jLabel6.setText("jLabel6");

        jLabel9.setText("jLabel9");

        jTextField3.setText("jTextField3");

        jTextField5.setText("jTextField5");

        jButton3.setText("jButton3");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Manage wood load panel");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Date:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Customer:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Pieces:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Permit no:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Employee 1:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Employee 2:");

        txtcustomer.setEditable(false);
        txtcustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        txtPieces.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtPieces.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPiecesKeyReleased(evt);
            }
        });

        txtPermitNo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtPermitNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPermitNoActionPerformed(evt);
            }
        });
        txtPermitNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPermitNoKeyReleased(evt);
            }
        });

        lblEmp1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        lblEmp2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        dtpWoodLoad.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        dtpWoodLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dtpWoodLoadActionPerformed(evt);
            }
        });
        dtpWoodLoad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dtpWoodLoadKeyReleased(evt);
            }
        });

        btnSelectEmp1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectEmp1.setText("...");
        btnSelectEmp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmp1ActionPerformed(evt);
            }
        });

        btnSelectEmp2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectEmp2.setText("...");
        btnSelectEmp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmp2ActionPerformed(evt);
            }
        });

        btnSelectCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectCustomer.setText("...");
        btnSelectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectCustomerActionPerformed(evt);
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
        btnSave.setText("Save");
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtcustomer)
                            .addComponent(txtPieces)
                            .addComponent(txtPermitNo)
                            .addComponent(lblEmp1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addComponent(lblEmp2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dtpWoodLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSelectEmp1)
                            .addComponent(btnSelectEmp2)
                            .addComponent(btnSelectCustomer))))
                .addContainerGap(176, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(dtpWoodLoad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtcustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectCustomer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPieces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPermitNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblEmp1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectEmp1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblEmp2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectEmp2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        btnSetDefaultEmployees.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/settings.png"))); // NOI18N
        btnSetDefaultEmployees.setText("Set default Employees");
        btnSetDefaultEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetDefaultEmployeesActionPerformed(evt);
            }
        });

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
                        .addComponent(btnSetDefaultEmployees)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnSetDefaultEmployees))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtPermitNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPermitNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPermitNoActionPerformed

    private void btnSelectEmp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmp2ActionPerformed
        EmployeePanel ep = new EmployeePanel(mainView, this, 2,null);
        mainView.addTab("Select Employee 2", ep);
    }//GEN-LAST:event_btnSelectEmp2ActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSelectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectCustomerActionPerformed
        CustomerPanel cp = new CustomerPanel(mainView, this,null);
        mainView.addTab("Select customer", cp);
    }//GEN-LAST:event_btnSelectCustomerActionPerformed

    private void btnSelectEmp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmp1ActionPerformed
        EmployeePanel ep = new EmployeePanel(mainView, this, 1,null);
        mainView.addTab("Select Employee 1", ep);
    }//GEN-LAST:event_btnSelectEmp1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();        

    }//GEN-LAST:event_btnSaveActionPerformed

    private void dtpWoodLoadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtpWoodLoadKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtPieces.grabFocus();
        }
    }//GEN-LAST:event_dtpWoodLoadKeyReleased

    private void txtPiecesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPiecesKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtPermitNo.grabFocus();
        }
    }//GEN-LAST:event_txtPiecesKeyReleased

    private void dtpWoodLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dtpWoodLoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dtpWoodLoadActionPerformed

    private void btnSetDefaultEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDefaultEmployeesActionPerformed
        UpdateDefaultEmployeePanel udep=new UpdateDefaultEmployeePanel(mainView,this);
        mainView.addTab("Update default employees", udep);
    }//GEN-LAST:event_btnSetDefaultEmployeesActionPerformed

    private void txtPermitNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPermitNoKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            save();
        }
    }//GEN-LAST:event_txtPermitNoKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelectCustomer;
    private javax.swing.JButton btnSelectEmp1;
    private javax.swing.JButton btnSelectEmp2;
    private javax.swing.JButton btnSetDefaultEmployees;
    private org.jdesktop.swingx.JXDatePicker dtpWoodLoad;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel lblEmp1;
    private javax.swing.JLabel lblEmp2;
    private javax.swing.JTextField txtPermitNo;
    private javax.swing.JTextField txtPieces;
    private javax.swing.JTextField txtcustomer;
    // End of variables declaration//GEN-END:variables
}
