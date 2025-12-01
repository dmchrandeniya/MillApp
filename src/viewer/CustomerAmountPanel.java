/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.Customer;
import controller.CustomerAmountTaken;
import controller.CustomerAmountTakenController;
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
public class CustomerAmountPanel extends javax.swing.JPanel {

    /**
     * @return the cusId
     */
    public int getCusId() {
        return cusId;
    }

    /**
     * @param cusId the cusId to set
     */
    public void setCusId(int cusId) {
        this.cusId = cusId;
        Customer c = new Customer();
        ResultSet rs = c.read("SELECT `name` FROM `customer` WHERE `id`='" + String.valueOf(cusId) + "';");
        try {
            if (rs.last()) {
                txtCusName.setText(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtAmountTook.setText("");
    }

    /**
     * @return the amountTakenId
     */
    public int getAmountTakenId() {
        return amountTakenId;
    }

    /**
     * @param amountTakenId the amountTakenId to set
     */
    public void setAmountTakenId(int amountTakenId) {
        this.amountTakenId = amountTakenId;
    }

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
        if (customerId > 0) {
            Customer c = new Customer();
            ResultSet rs = c.read("SELECT `name` FROM `customer` WHERE `id`='" + customerId + "';");
            try {
                if (rs.last()) {
                    txtCustomerName.setText(rs.getString("name"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            txtCustomerName.setText("");
        }
    }

    MainView mainView;
    private int customerId;
    CustomerAmountTaken customerAmountTaken;
    private int amountTakenId;
    private int cusId;

    /**
     * Creates new form CustomerAmountPanel
     * @param mainView
     */
    public CustomerAmountPanel(MainView mainView) {
        initComponents();
        this.mainView = mainView;
        Thread t;
        t = new Thread(() -> {
            UiSettings.tableProperties(tblCustomerAmountPanel);
            loadTable("All", "");
            dtpDate1.setFormats("yyyy-M-dd");
        });
        t.start();
        mainView.setYear(txtYear);
        mainView.loadMonth(cmbMonth);
    }

    public void save() {
        if (mainView.isEmptyDatePicker(dtpDate1, "Please select the date.")) {
            return;
        }
        if (!(getCustomerId() > 0)) {
            JOptionPane.showMessageDialog(this, "Please select the customer.");
            return;
        }
        if (mainView.isEmptyField(txtAmount1, "Please enter the amount")) {
            return;
        }
        try {
            Double.valueOf(txtAmount1.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "amount not valid.");
            return;
        }
        if (getAmountTakenId() > 0) {
            CustomerAmountTaken cat = new CustomerAmountTaken();
            cat.setId(getAmountTakenId());
            Date d = new Date(dtpDate1.getDate().getTime());
            cat.setDate(d.toLocalDate());
            cat.setCusId(getCustomerId());
            cat.setAmount(Double.parseDouble(txtAmount1.getText()));
            CustomerAmountTakenController catc = new CustomerAmountTakenController();
            boolean b = catc.Update(cat);
            if (b) {
                loadTable("All", "");
                setCustomerId(0);
                dtpDate1.getEditor().setText("");
                txtAmount1.setText("");
            }
        } else {
            CustomerAmountTaken cat = new CustomerAmountTaken();
            Date d = new Date(dtpDate1.getDate().getTime());
            cat.setDate(d.toLocalDate());
            cat.setCusId(getCustomerId());
            cat.setAmount(Double.parseDouble(txtAmount1.getText()));
            CustomerAmountTakenController catc = new CustomerAmountTakenController();
            boolean b = catc.Create(cat);
            if (b) {
                loadTable("All", "");
                setCustomerId(0);
                dtpDate1.getEditor().setText("");
                txtAmount1.setText("");
            }
        }
        setAmountTakenId(0);
        setSinceDateAndAmountToTake();
        btnSave.setText("Add");
        dtpDate1.grabFocus();
        mainView.setchargeAndCubicFeet();
        mainView.setReducedAmountPerMonth();
        mainView.setExpense();
    }

    public void loadTable(String searchBy, String value) {
        btnEdit.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblCustomerAmountPanel.getModel();
        dtm.setRowCount(0);
        customerAmountTaken = new CustomerAmountTaken();
        String sql = "SELECT * FROM `amount_customer`";
        if (value.length() > 0) {
            if (searchBy.equalsIgnoreCase("Customer")) {
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
        ResultSet rs = customerAmountTaken.read(sql);
        try {
            while (rs.next()) {
                CustomerAmountTaken cat = new CustomerAmountTaken();
                cat.setId(rs.getInt("id"));
                cat.setCusId(rs.getInt("cus_id"));
                cat.setDate(rs.getDate("date").toLocalDate());
                cat.setAmount(rs.getDouble("amount"));
                ArrayList<Object> al = new ArrayList();
                al.add(cat);
                al.add(rs.getString("name"));
                DecimalFormat df = new DecimalFormat("#,###.00");
                al.add(df.format(cat.getAmount()));
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSinceDateAndAmountToTake() {
        if (customerAmountTaken.getCusId() > 0) {
            String sql = "SELECT `date` FROM `amount_taken` WHERE `cus_id`='" + customerAmountTaken.getCusId() + "' ORDER BY `date`;";
            ResultSet rs = customerAmountTaken.read(sql);
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

            String s1 = "SELECT SUM(`charge`) AS cost FROM `load_details_employee` WHERE `customer_id`='" + customerAmountTaken.getCusId() + "';";
            ResultSet rs1 = customerAmountTaken.read(s1);
            double cost = 0;
            try {
                if (rs1.last()) {
                    cost = rs1.getDouble("cost");
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            String s2 = "SELECT sum(`amount`) AS full_amount FROM `amount_taken` WHERE `cus_id`='" + customerAmountTaken.getCusId() + "';";
            ResultSet rs2 = customerAmountTaken.read(s2);
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
        } else {
            txtSinceDate.setText("");
            txtFullAmount.setText("");
        }

    }
    
    public void setAmountTaken(){
        if(!(getCusId()>0)){
            JOptionPane.showMessageDialog(this, "Please select the customer.");
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
        String sql="SELECT SUM(`amount`) AS amount_took FROM `amount_taken` WHERE `cus_id`='"+String.valueOf(getCusId())+"' AND YEAR(`date`)='"+String.valueOf(year)+"' AND MONTH(`date`)='"+String.valueOf(m.getId())+"';";
        double d=0.0;
        ResultSet rs=m.read(sql);
        try {
            if(rs.last()){
                d+=rs.getDouble("amount_took");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerAmountPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtAmountTook.setText(df.format(d));
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
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cmbSearchBy = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCustomerAmountPanel = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtSinceDate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFullAmount = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnSelectCustomer = new javax.swing.JButton();
        txtAmount1 = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        dtpDate1 = new org.jdesktop.swingx.JXDatePicker();
        btnEdit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCusName = new javax.swing.JTextField();
        txtYear = new javax.swing.JTextField();
        cmbMonth = new javax.swing.JComboBox<>();
        btnSelectCus = new javax.swing.JButton();
        btnCal = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtAmountTook = new javax.swing.JTextField();

        jTextField1.setText("jTextField1");

        jButton3.setText("jButton3");

        jLabel10.setText("jLabel10");

        jTextField4.setText("jTextField4");

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Customer amount panel");

        jPanel2.setLayout(new java.awt.GridBagLayout());

        cmbSearchBy.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbSearchBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Customer", "Date", "Amount" }));
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

        tblCustomerAmountPanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblCustomerAmountPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Customer", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCustomerAmountPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomerAmountPanelMouseClicked(evt);
            }
        });
        tblCustomerAmountPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCustomerAmountPanelKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblCustomerAmountPanel);
        if (tblCustomerAmountPanel.getColumnModel().getColumnCount() > 0) {
            tblCustomerAmountPanel.getColumnModel().getColumn(1).setResizable(false);
        }

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

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Amount to take: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel5.add(jLabel2, gridBagConstraints);

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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Add amount", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(46, 16, 0, 0);
        jPanel4.add(jLabel6, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Customer:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 16, 0, 0);
        jPanel4.add(jLabel8, gridBagConstraints);

        txtCustomerName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtCustomerName.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 228;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        jPanel4.add(txtCustomerName, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel4.add(jLabel9, gridBagConstraints);

        btnSelectCustomer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectCustomer.setText("...");
        btnSelectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 0, 28);
        jPanel4.add(btnSelectCustomer, gridBagConstraints);

        txtAmount1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtAmount1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmount1KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 287;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 28);
        jPanel4.add(txtAmount1, gridBagConstraints);

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
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 218, 18, 28);
        jPanel4.add(btnSave, gridBagConstraints);

        dtpDate1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        dtpDate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dtpDate1ActionPerformed(evt);
            }
        });
        dtpDate1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dtpDate1KeyReleased(evt);
            }
        });
        jPanel4.add(dtpDate1, new java.awt.GridBagConstraints());

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Amount taken", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Customer : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(46, 16, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Month : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        txtCusName.setEditable(false);
        txtCusName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 235;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(43, 4, 0, 0);
        jPanel1.add(txtCusName, gridBagConstraints);

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
        gridBagConstraints.ipadx = 235;
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
        gridBagConstraints.ipadx = 193;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel1.add(cmbMonth, gridBagConstraints);

        btnSelectCus.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSelectCus.setText("...");
        btnSelectCus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectCusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(42, 6, 0, 59);
        jPanel1.add(btnSelectCus, gridBagConstraints);

        btnCal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/Calculate.png"))); // NOI18N
        btnCal.setText("Cal");
        btnCal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 128, 0, 0);
        jPanel1.add(btnCal, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel11.setText("Amount took : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 16, 0, 0);
        jPanel1.add(jLabel11, gridBagConstraints);

        txtAmountTook.setEditable(false);
        txtAmountTook.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 252;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 18, 0);
        jPanel1.add(txtAmountTook, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 271, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClose))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadTable(cmbSearchBy.getSelectedItem().toString(), txtSearch.getText());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tblCustomerAmountPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomerAmountPanelMouseClicked
        if (tblCustomerAmountPanel.getRowCount() > 0 && tblCustomerAmountPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            customerAmountTaken = (CustomerAmountTaken) tblCustomerAmountPanel.getValueAt(tblCustomerAmountPanel.getSelectedRow(), 0);
            setSinceDateAndAmountToTake();
        }
    }//GEN-LAST:event_tblCustomerAmountPanelMouseClicked

    private void tblCustomerAmountPanelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCustomerAmountPanelKeyReleased
        if (tblCustomerAmountPanel.getRowCount() > 0 && tblCustomerAmountPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            customerAmountTaken = (CustomerAmountTaken) tblCustomerAmountPanel.getValueAt(tblCustomerAmountPanel.getSelectedRow(), 0);
            setSinceDateAndAmountToTake();
        }
    }//GEN-LAST:event_tblCustomerAmountPanelKeyReleased

    private void txtAmount1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmount1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            save();
        }
    }//GEN-LAST:event_txtAmount1KeyReleased

    private void btnSelectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectCustomerActionPerformed
        CustomerPanel cp = new CustomerPanel(mainView, null, this);
        mainView.addTab("Select customer", cp);
    }//GEN-LAST:event_btnSelectCustomerActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void dtpWoodLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dtpDate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dtpDate1ActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        setAmountTakenId(customerAmountTaken.getId());
        dtpDate1.setDate(Date.valueOf(customerAmountTaken.getDate()));
        setCustomerId(customerAmountTaken.getCusId());
        txtAmount1.setText(String.valueOf(customerAmountTaken.getAmount()));
        btnSave.setText("Update");
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSelectCusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectCusActionPerformed
        CustomerPanel cp=new CustomerPanel(mainView, this);
        mainView.addTab("Select customer", cp);
    }//GEN-LAST:event_btnSelectCusActionPerformed

    private void txtYearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearKeyReleased
        txtAmountTook.setText("");
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            setAmountTaken();
        }
    }//GEN-LAST:event_txtYearKeyReleased

    private void btnCalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalActionPerformed
        setAmountTaken();
    }//GEN-LAST:event_btnCalActionPerformed

    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed
        txtAmountTook.setText("");
    }//GEN-LAST:event_cmbMonthActionPerformed

    private void dtpDate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dtpWoodLoadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dtpWoodLoadActionPerformed

    private void dtpDate1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtpDate1KeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtAmount1.grabFocus();
        }
    }//GEN-LAST:event_dtpDate1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCal;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelectCus;
    private javax.swing.JButton btnSelectCustomer;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbSearchBy;
    private org.jdesktop.swingx.JXDatePicker dtpDate1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTable tblCustomerAmountPanel;
    private javax.swing.JTextField txtAmount1;
    private javax.swing.JTextField txtAmountTook;
    private javax.swing.JTextField txtCusName;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtFullAmount;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSinceDate;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
