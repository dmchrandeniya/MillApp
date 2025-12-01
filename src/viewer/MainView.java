/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

//import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import controller.Inventory;
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
import model.Connect;
import java.awt.Component;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Chalindu
 */
public final class MainView extends javax.swing.JFrame {

    /**
     * Creates new form MainView
     */
    public MainView() {
        initComponents();
        try {
            Connect.createConnection();
        } catch (Exception e) {
            System.out.println("Database not started..");
        }
        setExtendedState(MAXIMIZED_BOTH);
        //loading cubic feet per month.
        setYear(txtYearCPM);
        loadMonth(cmbMonthCPM);
        setchargeAndCubicFeet();
        //load reduce per month.
        setYear(txtYearRPM);
        loadMonth(cmbMonthRPM);
        setReducedAmountPerMonth();
        //load expences
        setYear(txtExpencesYear);
        loadMonth(cmbMonthExpences);
        setExpense();
        //load inventory
        setYear(txtYearInventory);
        loadMonth(cmbMonthInventory);
        setCost();
    }

    public void addTab(String title, Component component) {
        tbpMain.addTab(title, component);
        tbpMain.setSelectedComponent(component);
    }

    public void removeTab(Component component) {
        tbpMain.remove(component);
    }

    public void selectComponent(Component component) {
        tbpMain.setSelectedComponent(component);
    }

    public boolean isEmptyField(JTextField jTextField, String message) {
        if (jTextField.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, message);
            return true;
        }
        return false;
    }

    public boolean isEmptyDatePicker(JXDatePicker jXDatePicker, String message) {
        if (jXDatePicker.getDate() == null) {
            JOptionPane.showMessageDialog(this, message);
            return true;
        }
        return false;
    }

    public void setYear(JTextField jTextField) {
        int year = Year.now().getValue();
        jTextField.setText(String.valueOf(year));
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

    public void setchargeAndCubicFeet() {
        if (isEmptyField(txtYearCPM, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYearCPM.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            return;
        }
        int year = Integer.parseInt(txtYearCPM.getText());
        if (!(year > 2022 && year < 2050)) {
            JOptionPane.showMessageDialog(this, "please enter a year in 2022 to 2050.");
            return;
        }
        double cf = 0.0;
        double c = 0.0;
        Month m = (Month) cmbMonthCPM.getSelectedItem();

        String sql = "SELECT SUM(`cubic_feet`) AS cubic_feet,SUM(`charge`) AS charge FROM `charge_and_cubic_feet` \n"
                + "LEFT JOIN `wood_load` ON `charge_and_cubic_feet`.`load_id`=`wood_load`.`id` \n"
                + "WHERE YEAR(`date`)='" + txtYearCPM.getText() + "' AND MONTH(`date`)='" + String.valueOf(m.getId()) + "';";
        ResultSet rs = m.read(sql);
        
        try {
            if (rs.last()) {
                cf = rs.getDouble("cubic_feet");
                c = rs.getDouble("charge");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        lblCubicFeetCPM.setText(df.format(cf));
        lblChargeCPM.setText(df.format(c));

    }

    public void setReducedAmountPerMonth() {
        if (isEmptyField(txtYearRPM, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYearRPM.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            return;
        }
        int year = Integer.parseInt(txtYearRPM.getText());
        if (!(year > 2022 && year < 2050)) {
            JOptionPane.showMessageDialog(this, "please enter a year in 2022 to 2050.");
            return;
        }
        Month m = (Month) cmbMonthRPM.getSelectedItem();
        double d = 0.0;
        String sql = "SELECT SUM(`reduce_amount`.`amount`) AS month_amount FROM `reduce_amount` LEFT JOIN `wood_load` ON `reduce_amount`.`load_id`=`wood_load`.`id` WHERE YEAR(`date`)='" + txtYearRPM.getText() + "' && MONTH(`date`)='" + String.valueOf(m.getId()) + "';";
        ResultSet rs = m.read(sql);
        try {
            if (rs.last()) {
                d = rs.getDouble("month_amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        lblAmountSumRPM.setText(df.format(d));
    }

    public void setExpense() {
        if (isEmptyField(txtExpencesYear, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtExpencesYear.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            return;
        }
        int year = Integer.parseInt(txtExpencesYear.getText());
        if (!(year > 2022 && year < 2050)) {
            JOptionPane.showMessageDialog(this, "please enter a year in 2022 to 2050.");
            return;
        }
        Month m = (Month) cmbMonthExpences.getSelectedItem();
        double d = 0.0;
        String sql = "SELECT SUM(`cost`) AS expense FROM `expences` WHERE YEAR(`date`)='" + String.valueOf(year) + "' AND MONTH(`date`)='" + String.valueOf(m.getId()) + "';";
        ResultSet rs = m.read(sql);
        try {
            if (rs.last()) {
                d = rs.getDouble("expense");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtExpence.setText(df.format(d));
    }

    public void setCost() {
        if (isEmptyField(txtYearInventory, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYearInventory.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year.");
            return;
        }
        Month m = (Month) cmbMonthInventory.getSelectedItem();
        Inventory i = new Inventory();
        double c = 0.0;
        ResultSet rs = i.read("SELECT SUM(`price`) AS month_cost FROM `inventory` WHERE YEAR(`date`)='" + txtYearInventory.getText() + "' AND MONTH(`date`)='" + String.valueOf(m.getId()) + "';");
        try {
            if (rs.last()) {
                c = rs.getDouble("month_cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtCostInventory.setText(df.format(c));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnCustomer = new javax.swing.JButton();
        btnEmployee = new javax.swing.JButton();
        btnWoodLoad = new javax.swing.JButton();
        btnInputLoad = new javax.swing.JButton();
        btnEmployeeAmount = new javax.swing.JButton();
        btnCustomerAmount = new javax.swing.JButton();
        btnInventory = new javax.swing.JButton();
        btnExpences = new javax.swing.JButton();
        btnSalaryAndCost = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tbpMain = new javax.swing.JTabbedPane();
        homeTab = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtYearCPM = new javax.swing.JTextField();
        lblCubicFeetCPM = new javax.swing.JTextField();
        lblChargeCPM = new javax.swing.JTextField();
        cmbMonthCPM = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtYearRPM = new javax.swing.JTextField();
        cmbMonthRPM = new javax.swing.JComboBox<>();
        lblAmountSumRPM = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtExpencesYear = new javax.swing.JTextField();
        cmbMonthExpences = new javax.swing.JComboBox<>();
        txtExpence = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtYearInventory = new javax.swing.JTextField();
        cmbMonthInventory = new javax.swing.JComboBox<>();
        txtCostInventory = new javax.swing.JTextField();

        jTextField4.setText("jTextField4");

        jLabel8.setText("jLabel8");

        jLabel18.setText("jLabel18");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MILL APP");

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/customer.png"))); // NOI18N
        btnCustomer.setToolTipText("Customer");
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        jPanel1.add(btnCustomer);

        btnEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/employee.png"))); // NOI18N
        btnEmployee.setToolTipText("Employee");
        btnEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmployee);

        btnWoodLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/wood_load.png"))); // NOI18N
        btnWoodLoad.setToolTipText("Wood load");
        btnWoodLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWoodLoadActionPerformed(evt);
            }
        });
        jPanel1.add(btnWoodLoad);

        btnInputLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/load details.png"))); // NOI18N
        btnInputLoad.setToolTipText("Input load");
        btnInputLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputLoadActionPerformed(evt);
            }
        });
        jPanel1.add(btnInputLoad);

        btnEmployeeAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/amount.png"))); // NOI18N
        btnEmployeeAmount.setToolTipText("Employee Amount");
        btnEmployeeAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeAmountActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmployeeAmount);

        btnCustomerAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/CustomerAmount.png"))); // NOI18N
        btnCustomerAmount.setToolTipText("Customer amount");
        btnCustomerAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerAmountActionPerformed(evt);
            }
        });
        jPanel1.add(btnCustomerAmount);

        btnInventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/inventory.png"))); // NOI18N
        btnInventory.setToolTipText("Inventory");
        btnInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventoryActionPerformed(evt);
            }
        });
        jPanel1.add(btnInventory);

        btnExpences.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/expences.png"))); // NOI18N
        btnExpences.setToolTipText("Expences");
        btnExpences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpencesActionPerformed(evt);
            }
        });
        jPanel1.add(btnExpences);

        btnSalaryAndCost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/SalaryAndCost.png"))); // NOI18N
        btnSalaryAndCost.setToolTipText("Salary and cost");
        btnSalaryAndCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryAndCostActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalaryAndCost);

        tbpMain.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 4, true));
        tbpMain.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cubic feet per month", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Year :");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Month :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Cubic feet :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Charge :");

        txtYearCPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txtYearCPM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearCPMKeyReleased(evt);
            }
        });

        lblCubicFeetCPM.setEditable(false);
        lblCubicFeetCPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        lblChargeCPM.setEditable(false);
        lblChargeCPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        cmbMonthCPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        cmbMonthCPM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthCPMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblCubicFeetCPM)
                    .addComponent(cmbMonthCPM, javax.swing.GroupLayout.Alignment.LEADING, 0, 230, Short.MAX_VALUE)
                    .addComponent(txtYearCPM, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblChargeCPM))
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtYearCPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbMonthCPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblCubicFeetCPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblChargeCPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Redused amount per month", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Year :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Month :");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Amount sum :");

        txtYearRPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txtYearRPM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearRPMKeyReleased(evt);
            }
        });

        cmbMonthRPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        cmbMonthRPM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthRPMActionPerformed(evt);
            }
        });

        lblAmountSumRPM.setEditable(false);
        lblAmountSumRPM.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtYearRPM)
                    .addComponent(cmbMonthRPM, 0, 231, Short.MAX_VALUE)
                    .addComponent(lblAmountSumRPM))
                .addContainerGap(308, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtYearRPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbMonthRPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblAmountSumRPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Expenses", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Year : ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Month : ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expense : ");

        txtExpencesYear.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txtExpencesYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExpencesYearActionPerformed(evt);
            }
        });
        txtExpencesYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExpencesYearKeyReleased(evt);
            }
        });

        cmbMonthExpences.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        cmbMonthExpences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthExpencesActionPerformed(evt);
            }
        });

        txtExpence.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtExpencesYear)
                    .addComponent(cmbMonthExpences, 0, 240, Short.MAX_VALUE)
                    .addComponent(txtExpence))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtExpencesYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cmbMonthExpences, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtExpence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inventory cost", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Year : ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Month : ");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Month cost : ");

        txtYearInventory.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txtYearInventory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearInventoryKeyReleased(evt);
            }
        });

        cmbMonthInventory.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        cmbMonthInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthInventoryActionPerformed(evt);
            }
        });

        txtCostInventory.setEditable(false);
        txtCostInventory.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtYearInventory)
                    .addComponent(cmbMonthInventory, 0, 200, Short.MAX_VALUE)
                    .addComponent(txtCostInventory))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtYearInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cmbMonthInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtCostInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homeTabLayout = new javax.swing.GroupLayout(homeTab);
        homeTab.setLayout(homeTabLayout);
        homeTabLayout.setHorizontalGroup(
            homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        homeTabLayout.setVerticalGroup(
            homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeTabLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(homeTabLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(258, Short.MAX_VALUE))
        );

        tbpMain.addTab("Home", homeTab);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1409, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpMain)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed
        CustomerPanel cp = new CustomerPanel(this, null, null);
        addTab("Customer", cp);
    }//GEN-LAST:event_btnCustomerActionPerformed

    private void btnEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeActionPerformed
        EmployeePanel ep = new EmployeePanel(this, null, 0, null);
        addTab("Employee", ep);
    }//GEN-LAST:event_btnEmployeeActionPerformed

    private void btnWoodLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWoodLoadActionPerformed
        WoodLoadPanel wlp = new WoodLoadPanel(this, null);
        addTab("Wood load", wlp);
    }//GEN-LAST:event_btnWoodLoadActionPerformed

    private void btnInputLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputLoadActionPerformed
        WoodLoadInputPanel wlip = new WoodLoadInputPanel(this);
        addTab("Wood load input", wlip);
    }//GEN-LAST:event_btnInputLoadActionPerformed

    private void btnEmployeeAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeAmountActionPerformed
        EmployeeAmountPanel ap = new EmployeeAmountPanel(this);
        addTab("Employee amount", ap);
    }//GEN-LAST:event_btnEmployeeAmountActionPerformed

    private void btnCustomerAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerAmountActionPerformed
        CustomerAmountPanel cap = new CustomerAmountPanel(this);
        addTab("Customer amount", cap);
    }//GEN-LAST:event_btnCustomerAmountActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        InventoryPanel ip = new InventoryPanel(this);
        addTab("Inventory", ip);
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void cmbMonthCPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthCPMActionPerformed
        setchargeAndCubicFeet();
    }//GEN-LAST:event_cmbMonthCPMActionPerformed

    private void txtYearCPMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearCPMKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setchargeAndCubicFeet();
        }
    }//GEN-LAST:event_txtYearCPMKeyReleased

    private void txtYearRPMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearRPMKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setReducedAmountPerMonth();
        }
    }//GEN-LAST:event_txtYearRPMKeyReleased

    private void cmbMonthRPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthRPMActionPerformed
        setReducedAmountPerMonth();
    }//GEN-LAST:event_cmbMonthRPMActionPerformed

    private void btnExpencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpencesActionPerformed
        ExpencesPanel ep = new ExpencesPanel(this);
        addTab("Expenses", ep);
    }//GEN-LAST:event_btnExpencesActionPerformed

    private void txtYearInventoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearInventoryKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setCost();
        }
    }//GEN-LAST:event_txtYearInventoryKeyReleased

    private void cmbMonthInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthInventoryActionPerformed
        setCost();
    }//GEN-LAST:event_cmbMonthInventoryActionPerformed

    private void txtExpencesYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExpencesYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExpencesYearActionPerformed

    private void txtExpencesYearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpencesYearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setExpense();
        }
    }//GEN-LAST:event_txtExpencesYearKeyReleased

    private void cmbMonthExpencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthExpencesActionPerformed
        setExpense();
    }//GEN-LAST:event_cmbMonthExpencesActionPerformed

    private void btnSalaryAndCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryAndCostActionPerformed
        SalaryAndCostPanel sacp = new SalaryAndCostPanel(this);
        addTab("Salary and cost", sacp);
    }//GEN-LAST:event_btnSalaryAndCostActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        FlatDarkPurpleIJTheme.setup();
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        } catch (Exception e) {
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnCustomerAmount;
    private javax.swing.JButton btnEmployee;
    private javax.swing.JButton btnEmployeeAmount;
    private javax.swing.JButton btnExpences;
    private javax.swing.JButton btnInputLoad;
    private javax.swing.JButton btnInventory;
    private javax.swing.JButton btnSalaryAndCost;
    private javax.swing.JButton btnWoodLoad;
    private javax.swing.JComboBox<String> cmbMonthCPM;
    private javax.swing.JComboBox<String> cmbMonthExpences;
    private javax.swing.JComboBox<String> cmbMonthInventory;
    private javax.swing.JComboBox<String> cmbMonthRPM;
    private javax.swing.JPanel homeTab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField lblAmountSumRPM;
    private javax.swing.JTextField lblChargeCPM;
    private javax.swing.JTextField lblCubicFeetCPM;
    private javax.swing.JTabbedPane tbpMain;
    private javax.swing.JTextField txtCostInventory;
    private javax.swing.JTextField txtExpence;
    private javax.swing.JTextField txtExpencesYear;
    private javax.swing.JTextField txtYearCPM;
    private javax.swing.JTextField txtYearInventory;
    private javax.swing.JTextField txtYearRPM;
    // End of variables declaration//GEN-END:variables
}
