/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewer;

import controller.Customer;
import controller.Employee;
import controller.Month;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Chalindu
 */
public final class SalaryAndCostPanel extends javax.swing.JPanel {

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
        txtCubicFeet.setText("");
        txtCost.setText("");
        Customer c=new Customer();
        ResultSet rs=c.read("SELECT `name` FROM `customer` WHERE `id`='"+String.valueOf(customerId)+"';");
        try {
            if(rs.last()){
                txtCustomer.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryAndCostPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtCubicFeet.setText("");
        txtCost.setText("");
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
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        txtSalary.setText("");
        Employee e = new Employee();
        ResultSet rs = e.read("SELECT `name` FROM `employee` WHERE `id`='" + String.valueOf(employeeId) + "';");
        try {
            if (rs.last()) {
                txtEmployee.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageWoodLoadPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtSalary.setText("");
    }

    MainView mainView;
    private int employeeId;
    private int customerId;
    
    /** Creates new form SalaryAndCostPanel
     * @param mainView */
    public SalaryAndCostPanel(MainView mainView) {
        initComponents();
        this.mainView=mainView;
        //- set employee
        setYear(txtYearEmployee);
        loadMonth(cmbMonthEmployee);
        //- set customer
        setYear(txtYearCustomer);
        loadMonth(cmbMonthCustomer);
    }
    
    public void loadMonth(JComboBox jComboBox) {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) jComboBox.getModel();
        String sql = "SELECT * FROM `month`;";
        Month month = new Month();
        ResultSet rs = month.read(sql);
        try {
            while (rs.next()) {
                Month m = new Month();
                m.setId(rs.getInt("id"));
                m.setMonth(rs.getString("month"));
                dcbm.addElement(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setYear(JTextField jTextField) {
        int year = Year.now().getValue();
        jTextField.setText(String.valueOf(year));
    }
    
    public void calEmployeeSalary(){
        if(!(getEmployeeId()>0)){
            JOptionPane.showMessageDialog(this, "Please select the employee.");
            return;
        }
        if (mainView.isEmptyField(txtYearEmployee, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYearEmployee.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            return;
        }
        int year = Integer.parseInt(txtYearEmployee.getText());
        if (!(year > 2022 && year < 2050)) {
            JOptionPane.showMessageDialog(this, "please enter a year in 2022 to 2050.");
            return;
        }
        Month m=(Month)cmbMonthEmployee.getSelectedItem();
        String s1="SELECT SUM(`charge`/6) AS cost FROM `load_details_employee` WHERE `emp_id_1`='"+String.valueOf(getEmployeeId())+"' AND YEAR(`load_details_employee`.`date`)='"+String.valueOf(year)+"' AND MONTH(`load_details_employee`.`date`)='"+String.valueOf(m.getId())+"';";
        Employee e=new Employee();
        ResultSet rs1=e.read(s1);
        double d1=0.0;
        try {
            if(rs1.last()){
                d1+=rs1.getDouble("cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryAndCostPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        String s2="SELECT SUM(`charge`/6) AS cost FROM `load_details_employee` WHERE `emp_id_2`='"+String.valueOf(getEmployeeId())+"' AND YEAR(`load_details_employee`.`date`)='"+String.valueOf(year)+"' AND MONTH(`load_details_employee`.`date`)='"+String.valueOf(m.getId())+"';";
        ResultSet rs2=e.read(s2);
        double d2=0.0;
        try {
            if(rs2.last()){
                d2+=rs2.getDouble("cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryAndCostPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        double salary=d1+d2;
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtSalary.setText(df.format(salary));
        
    }
    
    public void calCustomer(){
        if(!(getCustomerId()>0)){
            JOptionPane.showMessageDialog(this, "Please select the customer.");
            return;
        }
        if (mainView.isEmptyField(txtYearCustomer, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYearCustomer.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            return;
        }
        int year = Integer.parseInt(txtYearCustomer.getText());
        if (!(year > 2022 && year < 2050)) {
            JOptionPane.showMessageDialog(this, "please enter a year in 2022 to 2050.");
            return;
        }
        Month m=(Month)cmbMonthCustomer.getSelectedItem();
        String sql="SELECT SUM(`cubic_feet`) AS cubic_feet,SUM(`charge`) AS cost FROM `load_details_employee` WHERE `customer_id`='"+String.valueOf(getCustomerId())+"' AND YEAR(`load_details_employee`.`date`)='"+String.valueOf(year)+"' AND MONTH(`load_details_employee`.`date`)='"+String.valueOf(m.getId())+"';";
        Customer c=new Customer();
        double cubic=0.0;
        double cost=0;
        ResultSet rs=c.read(sql);
        try {
            if(rs.last()){
                cubic+=rs.getDouble("cubic_feet");
                cost+=rs.getDouble("cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryAndCostPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtCubicFeet.setText(df.format(cubic));
        txtCost.setText(df.format(cost));
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
        jTextField3 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmployee = new javax.swing.JTextField();
        txtYearEmployee = new javax.swing.JTextField();
        cmbMonthEmployee = new javax.swing.JComboBox<>();
        btnSelectEmployee = new javax.swing.JButton();
        btnCalEmployee = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtSalary = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        txtYearCustomer = new javax.swing.JTextField();
        cmbMonthCustomer = new javax.swing.JComboBox<>();
        btnSelectCustomer = new javax.swing.JButton();
        btnCalCustomer = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtCubicFeet = new javax.swing.JTextField();
        txtCost = new javax.swing.JTextField();

        jLabel5.setText("jLabel5");

        jTextField3.setText("jTextField3");

        jLabel10.setText("jLabel10");

        jTextField4.setText("jTextField4");

        jTextField7.setText("jTextField7");

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Salary and cost Panel");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee salary", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Employee : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Month : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        txtEmployee.setEditable(false);
        txtEmployee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 209;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 0, 0);
        jPanel1.add(txtEmployee, gridBagConstraints);

        txtYearEmployee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtYearEmployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearEmployeeKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 209;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel1.add(txtYearEmployee, gridBagConstraints);

        cmbMonthEmployee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        cmbMonthEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthEmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 167;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel1.add(cmbMonthEmployee, gridBagConstraints);

        btnSelectEmployee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectEmployee.setText("...");
        btnSelectEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectEmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 10);
        jPanel1.add(btnSelectEmployee, gridBagConstraints);

        btnCalEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/Calculate.png"))); // NOI18N
        btnCalEmployee.setText("Cal");
        btnCalEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalEmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 136, 0, 0);
        jPanel1.add(btnCalEmployee, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel6.setText("Salary : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        txtSalary.setEditable(false);
        txtSalary.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 183;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 11, 10);
        jPanel2.add(txtSalary, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 11, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer cubic and cost", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Customer : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(46, 16, 0, 0);
        jPanel3.add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel3.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Month : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel3.add(jLabel9, gridBagConstraints);

        txtCustomer.setEditable(false);
        txtCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 208;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(43, 4, 0, 0);
        jPanel3.add(txtCustomer, gridBagConstraints);

        txtYearCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtYearCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearCustomerKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 208;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel3.add(txtYearCustomer, gridBagConstraints);

        cmbMonthCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        cmbMonthCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel3.add(cmbMonthCustomer, gridBagConstraints);

        btnSelectCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectCustomer.setText("...");
        btnSelectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(42, 6, 0, 108);
        jPanel3.add(btnSelectCustomer, gridBagConstraints);

        btnCalCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/Calculate.png"))); // NOI18N
        btnCalCustomer.setText("Cal");
        btnCalCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 135, 0, 0);
        jPanel3.add(btnCalCustomer, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Cubic feet : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel4.add(jLabel11, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Cost : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel4.add(jLabel12, gridBagConstraints);

        txtCubicFeet.setEditable(false);
        txtCubicFeet.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 178;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 10);
        jPanel4.add(txtCubicFeet, gridBagConstraints);

        txtCost.setEditable(false);
        txtCost.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 178;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 11, 10);
        jPanel4.add(txtCost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 18, 108);
        jPanel3.add(jPanel4, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 135, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(btnClose)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSelectEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectEmployeeActionPerformed
        EmployeePanel ep=new EmployeePanel(mainView, this);
        mainView.addTab("Select employee", ep);
    }//GEN-LAST:event_btnSelectEmployeeActionPerformed

    private void btnCalEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalEmployeeActionPerformed
        calEmployeeSalary();
    }//GEN-LAST:event_btnCalEmployeeActionPerformed

    private void btnSelectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectCustomerActionPerformed
        CustomerPanel cp=new CustomerPanel(mainView, this);
        mainView.addTab("Select Customer", cp);
    }//GEN-LAST:event_btnSelectCustomerActionPerformed

    private void btnCalCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalCustomerActionPerformed
        calCustomer();
    }//GEN-LAST:event_btnCalCustomerActionPerformed

    private void txtYearEmployeeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearEmployeeKeyReleased
        txtSalary.setText("");
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            calEmployeeSalary();
        }
    }//GEN-LAST:event_txtYearEmployeeKeyReleased

    private void txtYearCustomerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearCustomerKeyReleased
        txtCubicFeet.setText("");
        txtCost.setText("");
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            calCustomer();
        }
    }//GEN-LAST:event_txtYearCustomerKeyReleased

    private void cmbMonthEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthEmployeeActionPerformed
        txtSalary.setText("");
    }//GEN-LAST:event_cmbMonthEmployeeActionPerformed

    private void cmbMonthCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthCustomerActionPerformed
        txtCubicFeet.setText("");
        txtCost.setText("");
    }//GEN-LAST:event_cmbMonthCustomerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalCustomer;
    private javax.swing.JButton btnCalEmployee;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnSelectCustomer;
    private javax.swing.JButton btnSelectEmployee;
    private javax.swing.JComboBox<String> cmbMonthCustomer;
    private javax.swing.JComboBox<String> cmbMonthEmployee;
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
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField txtCost;
    private javax.swing.JTextField txtCubicFeet;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtEmployee;
    private javax.swing.JTextField txtSalary;
    private javax.swing.JTextField txtYearCustomer;
    private javax.swing.JTextField txtYearEmployee;
    // End of variables declaration//GEN-END:variables

}
