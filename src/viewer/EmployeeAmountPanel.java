/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.EmployeeAmountGiven;
import controller.EmployeeAmountGivenController;
import controller.Employee;
import controller.Month;
import java.awt.event.KeyEvent;
import java.sql.Date;
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
public class EmployeeAmountPanel extends javax.swing.JPanel {

    /**
     * @return the empId
     */
    public int getEmpId() {
        return empId;
    }

    /**
     * @param empId the empId to set
     */
    public void setEmpId(int empId) {
        this.empId = empId;
        Employee e = new Employee();
        ResultSet rs = e.read("SELECT `name` FROM `employee` WHERE `id`='" + String.valueOf(empId) + "';");
        try {
            if (rs.last()) {
                txtEmpAmountGiven.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtAmountGiven.setText("");
    }

    /**
     * @return the amountGivenId
     */
    public int getAmountGivenId() {
        return amountGivenId;
    }

    /**
     * @param amountGivenId the amountGivenId to set
     */
    public void setAmountGivenId(int amountGivenId) {
        this.amountGivenId = amountGivenId;
    }

    /**
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    MainView mainView;
    EmployeeAmountGiven amountGiven;
    private int employeeId;
    private int amountGivenId;
    private int empId;

    /**
     * Creates new form AmountPanel
     * @param mainView
     */
    public EmployeeAmountPanel(MainView mainView) {
        initComponents();
        this.mainView = mainView;
        Thread t = new Thread(() -> {
            UiSettings.tableProperties(tblEmployeeAmountPanel);
            loadTable("All", "");
            dtpDate.setFormats("yyyy-M-dd");
        });
        t.start();
        mainView.setYear(txtYear);
        mainView.loadMonth(cmbMonth);
    }

    public void loadTable(String searchBy, String value) {
        btnEdit.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAmountPanel.getModel();
        dtm.setRowCount(0);
        amountGiven = new EmployeeAmountGiven();
        String sql = "SELECT * FROM `amount_employee`";
        if (value.length() > 0) {
            if (searchBy.equalsIgnoreCase("Employee")) {
                sql = sql + " WHERE `name` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Date")) {
                sql = sql + " WHERE `date` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Amount")) {
                sql = sql + " WHERE `amount` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("All")) {
                sql = sql + " WHERE `name` LIKE '%" + value + "%' OR `date` LIKE '%" + value + "%' OR `amount` LIKE '%" + value + "%';";
            }
        } else {
            sql = sql + ";";
        }
        ResultSet rs = amountGiven.read(sql);
        try {
            while (rs.next()) {
                EmployeeAmountGiven ag = new EmployeeAmountGiven();
                ag.setId(rs.getInt("id"));
                ag.setEmpId(rs.getInt("emp_id"));
                ag.setDate(rs.getDate("date").toLocalDate());
                ag.setAmount(rs.getDouble("amount"));
                ArrayList<Object> al = new ArrayList();
                al.add(ag);
                al.add(rs.getString("name"));
                DecimalFormat df = new DecimalFormat("#,###.00");
                al.add(df.format(ag.getAmount()));
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        if (employeeId > 0) {
            Employee e = new Employee();
            ResultSet rs = e.read("SELECT `name` FROM `employee` WHERE `id`='" + employeeId + "';");
            try {
                if (rs.last()) {
                    txtEmployeeName.setText(rs.getString("name"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            txtEmployeeName.setText("");
        }

    }

    public void setDateAndFullAmount() {
        if (amountGiven.getEmpId() > 0) {
            String sql = "SELECT `date` FROM `amount_given` WHERE `emp_id`='" + amountGiven.getEmpId() + "' ORDER BY `date`;";
            ResultSet rs = amountGiven.read(sql);
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

            String s = "SELECT SUM(`amount`) AS full_amount FROM `amount_given` WHERE `emp_id`='" + amountGiven.getEmpId() + "';";
            ResultSet rs1 = amountGiven.read(s);
            double fullAmount = 0;
            try {
                if (rs1.last()) {
                    fullAmount = rs1.getDouble("full_amount");
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            String s1 = "SELECT SUM(`charge`/6) AS cost FROM `load_details_employee` WHERE `emp_id_1`='" + amountGiven.getEmpId() + "';";
            ResultSet rs2 = amountGiven.read(s1);
            double cost1 = 0;
            try {
                if (rs2.last()) {
                    cost1 += rs2.getDouble("cost");
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            String s2 = "SELECT SUM(`charge`/6) AS cost FROM `load_details_employee` WHERE `emp_id_2`='" + amountGiven.getEmpId() + "';";
            ResultSet rs3 = amountGiven.read(s2);
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
            txtFullAmount.setText(String.valueOf(df.format(amountToGive)));
        } else {
            txtSinceDate.setText("");
            txtFullAmount.setText("");
        }
    }
    
    public void setAmountGiven(){
        if(!(getEmpId()>0)){
            JOptionPane.showMessageDialog(this, "Please select the employee.");
            return;
        }
        if (mainView.isEmptyField(txtYear, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYear.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            return;
        }
        int year = Integer.parseInt(txtYear.getText());
        if (!(year > 2022 && year < 2050)) {
            JOptionPane.showMessageDialog(this, "please enter a year in 2022 to 2050.");
            return;
        }
        Month m=(Month)cmbMonth.getSelectedItem();
        String sql="SELECT SUM(`amount`) AS month_amount FROM `amount_given` WHERE `emp_id`='"+String.valueOf(getEmpId())+"' AND YEAR(`date`)='"+String.valueOf(year)+"' AND MONTH(`date`)='"+String.valueOf(m.getId())+"';";
        ResultSet rs=m.read(sql);
        double d=0.0;
        try {
            if(rs.last()){
                d+=rs.getDouble("month_amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }       
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtAmountGiven.setText(df.format(d));
    }

    public void save() {
        if (mainView.isEmptyDatePicker(dtpDate, "Please select the date.")) {
            return;
        }
        if (!(getEmployeeId() > 0)) {
            JOptionPane.showMessageDialog(this, "Please select the employee.");
            return;
        }
        if (mainView.isEmptyField(txtAmount, "Please enter the amount")) {
            return;
        }
        try {
            Double.valueOf(txtAmount.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "amount not valid.");
            return;
        }
        if (getAmountGivenId() > 0) {
            EmployeeAmountGiven ag = new EmployeeAmountGiven();
            ag.setId(getAmountGivenId());
            Date d = new Date(dtpDate.getDate().getTime());
            ag.setDate(d.toLocalDate());
            ag.setEmpId(getEmployeeId());
            ag.setAmount(Double.parseDouble(txtAmount.getText()));
            EmployeeAmountGivenController agc = new EmployeeAmountGivenController();
            boolean b = agc.Update(ag);
            if (b) {
                loadTable("All", "");
                setEmployeeId(0);
                dtpDate.getEditor().setText("");
                txtAmount.setText("");
            }
        } else {
            EmployeeAmountGiven ag = new EmployeeAmountGiven();
            Date d = new Date(dtpDate.getDate().getTime());
            ag.setDate(d.toLocalDate());
            ag.setEmpId(getEmployeeId());
            ag.setAmount(Double.parseDouble(txtAmount.getText()));
            EmployeeAmountGivenController agc = new EmployeeAmountGivenController();
            boolean b = agc.Create(ag);
            if (b) {
                loadTable("All", "");
                setEmployeeId(0);
                dtpDate.getEditor().setText("");
                txtAmount.setText("");
            }
        }
        setAmountGivenId(0);
        setDateAndFullAmount();
        btnSave.setText("Add");
        dtpDate.grabFocus();
        mainView.setchargeAndCubicFeet();
        mainView.setReducedAmountPerMonth();
        mainView.setExpense();
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

        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnclose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployeeAmountPanel = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cmbSearchBy = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        dtpDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        btnSelectEmployee = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtSinceDate = new javax.swing.JTextField();
        txtFullAmount = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtEmpAmountGiven = new javax.swing.JTextField();
        txtYear = new javax.swing.JTextField();
        cmbMonth = new javax.swing.JComboBox<>();
        btnSelectEmp = new javax.swing.JButton();
        btncal = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtAmountGiven = new javax.swing.JTextField();

        jTextField1.setText("jTextField1");

        jTextField3.setText("jTextField3");

        jLabel6.setText("jLabel6");

        jLabel11.setText("jLabel11");

        jTextField5.setText("jTextField5");

        jButton1.setText("jButton1");

        btnclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnclose.setText("Close");
        btnclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncloseActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Employee amount panel");

        tblEmployeeAmountPanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblEmployeeAmountPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Employee", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeeAmountPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeAmountPanelMouseClicked(evt);
            }
        });
        tblEmployeeAmountPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblEmployeeAmountPanelKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmployeeAmountPanel);
        if (tblEmployeeAmountPanel.getColumnModel().getColumnCount() > 0) {
            tblEmployeeAmountPanel.getColumnModel().getColumn(0).setResizable(false);
            tblEmployeeAmountPanel.getColumnModel().getColumn(2).setResizable(false);
        }

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridBagLayout());

        cmbSearchBy.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbSearchBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Employee", "Date", "Amount" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 11, 0);
        jPanel2.add(cmbSearchBy, gridBagConstraints);

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 11, 10);
        jPanel2.add(txtSearch, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Add amount", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 16, 0, 0);
        jPanel3.add(jLabel3, gridBagConstraints);

        dtpDate.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 4, 0, 0);
        jPanel3.add(dtpDate, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Employee:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 0, 0);
        jPanel3.add(jLabel4, gridBagConstraints);

        txtEmployeeName.setEditable(false);
        txtEmployeeName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 197;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel3.add(txtEmployeeName, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel3.add(jLabel5, gridBagConstraints);

        txtAmount.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 197;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel3.add(txtAmount, gridBagConstraints);

        btnSelectEmployee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectEmployee.setText("...");
        btnSelectEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 16);
        jPanel3.add(btnSelectEmployee, gridBagConstraints);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/save.png"))); // NOI18N
        btnSave.setText("Add");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 134, 18, 0);
        jPanel3.add(btnSave, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Since:");

        txtSinceDate.setEditable(false);
        txtSinceDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtFullAmount.setEditable(false);
        txtFullAmount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Amount to give:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtFullAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(txtSinceDate))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSinceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFullAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Amount given", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Employee : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(46, 16, 0, 0);
        jPanel1.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Month : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel10, gridBagConstraints);

        txtEmpAmountGiven.setEditable(false);
        txtEmpAmountGiven.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(43, 4, 0, 0);
        jPanel1.add(txtEmpAmountGiven, gridBagConstraints);

        txtYear.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel1.add(txtYear, gridBagConstraints);

        cmbMonth.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        cmbMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 147;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel1.add(cmbMonth, gridBagConstraints);

        btnSelectEmp.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectEmp.setText("...");
        btnSelectEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(42, 6, 0, 16);
        jPanel1.add(btnSelectEmp, gridBagConstraints);

        btncal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/Calculate.png"))); // NOI18N
        btncal.setText("Cal");
        btncal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 94, 0, 0);
        jPanel1.add(btncal, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel12.setText("Full amount : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 16, 0, 0);
        jPanel1.add(jLabel12, gridBagConstraints);

        txtAmountGiven.setEditable(false);
        txtAmountGiven.setFont(new java.awt.Font("Tahoma", 0, 28)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 167;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 18, 0);
        jPanel1.add(txtAmountGiven, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(333, 333, 333)
                                .addComponent(btnEdit))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnclose, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(287, 287, 287)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 186, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(btnEdit))
                        .addGap(12, 12, 12)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnclose)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btncloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btncloseActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadTable(cmbSearchBy.getSelectedItem().toString(), txtSearch.getText());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tblEmployeeAmountPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeAmountPanelMouseClicked
        if (tblEmployeeAmountPanel.getRowCount() > 0 && tblEmployeeAmountPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            amountGiven = (EmployeeAmountGiven) tblEmployeeAmountPanel.getValueAt(tblEmployeeAmountPanel.getSelectedRow(), 0);
            setDateAndFullAmount();
        }
    }//GEN-LAST:event_tblEmployeeAmountPanelMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        setAmountGivenId(amountGiven.getId());
        dtpDate.setDate(Date.valueOf(amountGiven.getDate()));
        setEmployeeId(amountGiven.getEmpId());
        txtAmount.setText(String.valueOf(amountGiven.getAmount()));
        btnSave.setText("Update");
    }//GEN-LAST:event_btnEditActionPerformed

    private void tblEmployeeAmountPanelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmployeeAmountPanelKeyReleased
        if (tblEmployeeAmountPanel.getRowCount() > 0 && tblEmployeeAmountPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            amountGiven = (EmployeeAmountGiven) tblEmployeeAmountPanel.getValueAt(tblEmployeeAmountPanel.getSelectedRow(), 0);
            setDateAndFullAmount();
        }
    }//GEN-LAST:event_tblEmployeeAmountPanelKeyReleased

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            save();
        }
    }//GEN-LAST:event_txtAmountKeyReleased

    private void btnSelectEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmployeeActionPerformed
        EmployeePanel ep = new EmployeePanel(mainView, null, 0, this);
        mainView.addTab("Select employee", ep);
    }//GEN-LAST:event_btnSelectEmployeeActionPerformed

    private void btnSelectEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmpActionPerformed
        EmployeePanel ep=new EmployeePanel(mainView, this);
        mainView.addTab("Select employee", ep);
    }//GEN-LAST:event_btnSelectEmpActionPerformed

    private void btncalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncalActionPerformed
        setAmountGiven();
    }//GEN-LAST:event_btncalActionPerformed

    private void txtYearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearKeyReleased
        txtAmountGiven.setText("");
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            setAmountGiven();
        }
    }//GEN-LAST:event_txtYearKeyReleased

    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed
        txtAmountGiven.setText("");
    }//GEN-LAST:event_cmbMonthActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelectEmp;
    private javax.swing.JButton btnSelectEmployee;
    private javax.swing.JButton btncal;
    private javax.swing.JButton btnclose;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbSearchBy;
    private org.jdesktop.swingx.JXDatePicker dtpDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTable tblEmployeeAmountPanel;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtAmountGiven;
    private javax.swing.JTextField txtEmpAmountGiven;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtFullAmount;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSinceDate;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
