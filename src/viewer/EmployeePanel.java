/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

//import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import controller.Employee;
import controller.EmployeeController;
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
public class EmployeePanel extends javax.swing.JPanel {

    MainView mainView;
    Employee employee;
    ManageWoodLoadPanel manageWoodLoadPanel;
    EmployeeAmountPanel amountPanel;
    int employeeNumber;
    UpdateDefaultEmployeePanel udep;
    int empNo;
    SalaryAndCostPanel sacp;
    EmployeeAmountPanel eap;

    /**
     * Creates new form CustomerPanel
     *
     * @param mainView
     * @param manageWoodLoadPanel
     * @param employeeNumber
     * @param amountPanel
     */
    public EmployeePanel(MainView mainView, ManageWoodLoadPanel manageWoodLoadPanel, int employeeNumber, EmployeeAmountPanel amountPanel) {
        initComponents();
        this.mainView = mainView;
        this.manageWoodLoadPanel = manageWoodLoadPanel;
        this.employeeNumber = employeeNumber;
        this.amountPanel = amountPanel;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblEmployee);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();

    }

    public EmployeePanel(MainView mainView, UpdateDefaultEmployeePanel udep, int empNo) {
        initComponents();
        this.mainView = mainView;
        this.udep = udep;
        this.empNo = empNo;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblEmployee);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();
    }

    public EmployeePanel(MainView mainView, SalaryAndCostPanel sacp) {
        initComponents();
        this.mainView = mainView;
        this.sacp = sacp;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblEmployee);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();
    }

    public EmployeePanel(MainView mainView,EmployeeAmountPanel eap) {
        initComponents();
        this.mainView=mainView;
        this.eap=eap;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblEmployee);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            txtSearch.grabFocus();
        });
        t.start();
    }
    
    

    public void loadTable(String searchBy, String value) {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
        dtm.setRowCount(0);
        employee = new Employee();
        String sql = "SELECT * FROM `employee`";
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
        ResultSet rs = employee.read(sql);
        try {
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                e.setAddress(rs.getString("address"));
                e.setPhone(rs.getString("phone"));
                e.setNote(rs.getString("note"));
                ArrayList<Object> al = new ArrayList();
                al.add(e);
                al.add(e.getAddress());
                al.add(e.getPhone());
                al.add(e.getNote());
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeePanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setSinceDateAndAmountToGive() {
        String sql = "SELECT `date` FROM `amount_given` WHERE `emp_id`='" + employee.getId() + "' ORDER BY `date`;";
        ResultSet rs = employee.read(sql);
        String date;
        try {
            while (rs.next()) {
                date = rs.getDate("date").toString();
                txtSinceDate1.setText(date);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        String s = "SELECT SUM(`amount`) AS full_amount FROM `amount_given` WHERE `emp_id`='" + employee.getId() + "';";
        ResultSet rs1 = employee.read(s);
        double fullAmount = 0;
        try {
            if (rs1.last()) {
                fullAmount = rs1.getDouble("full_amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        String s1 = "SELECT SUM(`charge`/6) AS cost FROM `load_details_employee` WHERE `emp_id_1`='" + employee.getId() + "';";
        ResultSet rs2 = employee.read(s1);
        double cost1 = 0;
        try {
            if (rs2.last()) {
                cost1 += rs2.getDouble("cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        String s2 = "SELECT SUM(`charge`/6) AS cost FROM `load_details_employee` WHERE `emp_id_2`='" + employee.getId() + "';";
        ResultSet rs3 = employee.read(s2);
        double cost2 = 0;
        try {
            if (rs3.last()) {
                cost2 += rs3.getDouble("cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        double amountToGive = cost1 + cost2 - fullAmount;
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtFullAmount1.setText(String.valueOf(df.format(amountToGive)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        tblEmployee = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtSinceDate1 = new javax.swing.JTextField();
        txtFullAmount1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

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
        jLabel1.setText("Employee Panel");

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
                .addGap(10, 10, 10)
                .addComponent(cmbSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblEmployee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeMouseClicked(evt);
            }
        });
        tblEmployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblEmployeeKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmployee);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClose)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Since:");

        txtSinceDate1.setEditable(false);
        txtSinceDate1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtFullAmount1.setEditable(false);
        txtFullAmount1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Amount to give:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtFullAmount1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(txtSinceDate1))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSinceDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFullAmount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(btnDelete)
                        .addComponent(btnEdit)
                        .addComponent(btnAdd)))
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

    private void tblEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeMouseClicked
        if (tblEmployee.getRowCount() > 0 && tblEmployee.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            employee = (Employee) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0);
            setSinceDateAndAmountToGive();
            if (manageWoodLoadPanel != null && employeeNumber == 1 && evt.getClickCount() == 2) {
                manageWoodLoadPanel.setEmployee1_id(employee.getId());
                mainView.selectComponent(manageWoodLoadPanel);
                mainView.removeTab(this);
            } else if (manageWoodLoadPanel != null && employeeNumber == 2 && evt.getClickCount() == 2) {
                manageWoodLoadPanel.setEmployee2_id(employee.getId());
                mainView.selectComponent(manageWoodLoadPanel);
                mainView.removeTab(this);
            } else if (amountPanel != null && evt.getClickCount() == 2) {
                amountPanel.setEmployeeId(employee.getId());
                mainView.selectComponent(amountPanel);
                mainView.removeTab(this);
            } else if (udep != null && empNo == 1 && evt.getClickCount() == 2) {
                udep.setEmployee1_id(employee.getId());
                mainView.selectComponent(udep);
                mainView.removeTab(this);
            } else if (udep != null && empNo == 2 && evt.getClickCount() == 2) {
                udep.setEmployee2_id(employee.getId());
                mainView.selectComponent(udep);
                mainView.removeTab(this);
            } else if (sacp != null && evt.getClickCount() == 2) {
                sacp.setEmployeeId(employee.getId());
                mainView.selectComponent(sacp);
                mainView.removeTab(this);
            }else if(eap!=null && evt.getClickCount() == 2){
                eap.setEmpId(employee.getId());
                mainView.selectComponent(eap);
                mainView.removeTab(this);
            }
        }
    }//GEN-LAST:event_tblEmployeeMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ManageEmployeerPanel mep = new ManageEmployeerPanel(mainView, this, null);
        mainView.addTab("Add employee", mep);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        ManageEmployeerPanel mep = new ManageEmployeerPanel(mainView, this, employee);
        mainView.addTab("Edit employee", mep);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        boolean b = employee.checkEmployee(employee.getId());
        if (b) {
            JOptionPane.showMessageDialog(this, "You can't delete this Employee. because this employee has records in the system.");
        } else {
            int i = JOptionPane.showConfirmDialog(this, "Do you want to delete this customer");
            if (i == 0) {
                EmployeeController ec = new EmployeeController();
                boolean c = ec.delete(employee);
                if (c) {
                    String message = "Employee " + employee.getName() + " successfully deleted.";
                    JOptionPane.showMessageDialog(this, message);
                } else {
                    String message = "could not deleted.";
                    JOptionPane.showMessageDialog(this, message);
                }
                loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblEmployeeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmployeeKeyReleased
        if (tblEmployee.getRowCount() > 0 && tblEmployee.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            employee = (Employee) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0);
            setSinceDateAndAmountToGive();
        }
    }//GEN-LAST:event_tblEmployeeKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox<String> cmbSelect;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTextField txtFullAmount1;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSinceDate1;
    // End of variables declaration//GEN-END:variables
}
