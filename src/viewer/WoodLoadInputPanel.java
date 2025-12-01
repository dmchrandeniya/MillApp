/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.ReduceAmount;
import controller.WoodCategory;
import controller.WoodLoad;
import controller.WoodLoadDetails;
import controller.WoodLoadDetailsController;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.UiSettings;

/**
 *
 * @author Chalindu
 */
public class WoodLoadInputPanel extends javax.swing.JPanel {

    /**
     * @return the woodLoadDetailsId
     */
    public int getWoodLoadDetailsId() {
        return woodLoadDetailsId;
    }

    /**
     * @param woodLoadDetailsId the woodLoadDetailsId to set
     */
    public void setWoodLoadDetailsId(int woodLoadDetailsId) {
        this.woodLoadDetailsId = woodLoadDetailsId;
    }

    MainView mainView;
    private int woodLoadId;
    WoodLoadDetails woodLoadDetails;
    WoodCategory woodCategory;
    private int woodLoadDetailsId;

    /**
     * Creates new form WoodLoadInputPanel
     *
     * @param mainView
     */
    public WoodLoadInputPanel(MainView mainView) {
        initComponents();
        this.mainView = mainView;
        UiSettings.tableProperties(tblInputPanel);
        Thread t = new Thread(() -> {
            setUnitPrice();
            loadCategory();
        });
        t.start();

    }

    /**
     * @return the woodLoadId
     */
    public int getWoodLoadId() {
        return woodLoadId;
    }

    public void setUnitPrice() {
        WoodLoadDetails wld = new WoodLoadDetails();
        ResultSet rs = wld.read("SELECT * FROM `unit_price`");
        try {
            if (rs.last()) {
                txtUnitPrice.setText(String.valueOf(rs.getInt("unit_price")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCubicFeetAndCharge(int woodLoadId) {
        WoodLoadDetails wld = new WoodLoadDetails();
        ResultSet rs = wld.read("SELECT * FROM `charge_and_cubic_feet` WHERE `load_id`='" + woodLoadId + "';");
        try {
            if (rs.last()) {
                double d1 = rs.getDouble("cubic_feet");
                double d2 = rs.getDouble("charge");
                DecimalFormat df = new DecimalFormat("#,###.00");
                txtCubicFeet.setText(String.valueOf(df.format(d1)));
                txtCharge.setText(String.valueOf(df.format(d2)));
            } else {
                txtCubicFeet.setText("0");
                txtCharge.setText("0");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param woodLoadId the woodLoadId to set
     */
    public void setWoodLoadId(int woodLoadId) {
        this.woodLoadId = woodLoadId;
        WoodLoad wl = new WoodLoad();
        ResultSet rs = wl.read("SELECT `customer_name`,`date` FROM `wood_load_employee` WHERE `id`='" + woodLoadId + "';");
        try {
            if (rs.last()) {
                txtCustomerName.setText(rs.getString("customer_name"));
                txtDate.setText(rs.getDate("date").toString());
                Thread t = new Thread(() -> {
                    UiSettings.tableProperties(tblInputPanel);
                    loadTable(woodLoadId);
                    setRowcount(woodLoadId);
                    setCubicFeetAndCharge(woodLoadId);
                    btnSave.setEnabled(true);
                    txtLength.grabFocus();
                });
                t.start();

            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        ReduceAmount ra = new ReduceAmount();
        double d = 0;
        ResultSet rs1 = ra.read("SELECT * FROM `reduce_amount` WHERE `load_id`='" + woodLoadId + "';");
        try {
            if (rs1.last()) {
                d = rs1.getDouble("amount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtReduceAmount.setText(String.valueOf(d));
        btnApply.setEnabled(true);
    }

    public void saveReduceAmount(int woodLoadId) {
        if(mainView.isEmptyField(txtReduceAmount,"Please enter the amount.")){
            return;
        }
        try {
            Double.valueOf(txtReduceAmount.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter the valid amount");
        }
        String sql = "SELECT * FROM `reduce_amount` WHERE `load_id`='" + woodLoadId + "';";
        ReduceAmount ra = new ReduceAmount();
        ResultSet rs = ra.read(sql);
        try {
            if (rs.last()) {
                String s1 = "UPDATE `reduce_amount` SET `amount`='"+txtReduceAmount.getText()+"' WHERE `id`='"+String.valueOf(rs.getInt("id"))+"';";
                boolean b=ra.update(s1);
                if(b){
                    JOptionPane.showMessageDialog(this, "Reduce amount updated successfully.");
                }
            } else {
                String s2 = "INSERT INTO `reduce_amount` (`load_id`, `amount`) VALUES ('" + String.valueOf(woodLoadId) + "', '" + txtReduceAmount.getText() + "')";
                boolean b=ra.create(s2);
                if(b){
                    JOptionPane.showMessageDialog(this, "Reduce amount added successfully.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadTable(int woodLoadId) {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblInputPanel.getModel();
        dtm.setRowCount(0);
        woodLoadDetails = new WoodLoadDetails();
        ResultSet rs = woodLoadDetails.read("SELECT * FROM `load_details` WHERE `load_id`='" + woodLoadId + "';");
        try {
            while (rs.next()) {
                WoodLoadDetails wld = new WoodLoadDetails();
                wld.setId(rs.getInt("id"));
                wld.setLoadId(rs.getInt("load_id"));
                wld.setLength(rs.getDouble("length"));
                wld.setCircumference(rs.getDouble("circumference"));
                wld.setCategoryId(rs.getInt("category_id"));
                wld.setUnitPrice(rs.getInt("unit_price"));
                ArrayList<Object> al = new ArrayList();
                al.add(wld);
                al.add(wld.getCircumference());
                al.add(rs.getString("category"));
                al.add(wld.getUnitPrice());
                DecimalFormat df = new DecimalFormat("#,###.00");
                al.add(df.format(rs.getDouble("cubic_feet")));
                al.add(df.format(rs.getDouble("charge")));
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRowcount(int woodLoadId) {
        WoodLoad wl = new WoodLoad();
        ResultSet rs = wl.read("SELECT COUNT(`id`) as row_count FROM `wood_load_details` WHERE `load_id`='" + woodLoadId + "';");
        try {
            if (rs.last()) {
                txtrowCount.setText(String.valueOf(rs.getInt("row_count")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getRowCount(int woodLoadId) {
        int i = 0;
        WoodLoad wl = new WoodLoad();
        ResultSet rs = wl.read("SELECT COUNT(`id`) as row_count FROM `wood_load_details` WHERE `load_id`='" + woodLoadId + "';");
        try {
            if (rs.last()) {
                i = rs.getInt("row_count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public boolean checkCanAdd(int woodLoadId) {
        boolean b = false;
        WoodLoad wl = new WoodLoad();
        ResultSet rs = wl.read("SELECT `pieces` FROM `wood_load` WHERE `id`='" + woodLoadId + "';");
        try {
            if (rs.last()) {
                int pieces = rs.getInt("pieces");
                int rowCount = getRowCount(woodLoadId);
                if (!(pieces == rowCount)) {
                    b = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    public void loadCategory() {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) cmbCategory.getModel();
        dcbm.removeAllElements();
        WoodCategory wc = new WoodCategory();
        ResultSet rs = wc.read("SELECT * FROM `wood_category`;");
        try {
            while (rs.next()) {
                wc = new WoodCategory();
                wc.setId(rs.getInt("id"));
                wc.setCategory(rs.getString("category"));
                dcbm.addElement(wc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadCategoryInUpdate(WoodCategory wc) {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) cmbCategory.getModel();
        dcbm.removeAllElements();
        dcbm.addElement(wc);
        String sql = "SELECT * FROM `wood_category` WHERE NOT `id`='" + wc.getId() + "';";
        ResultSet rs = wc.read(sql);
        try {
            while (rs.next()) {
                wc = new WoodCategory();
                wc.setId(rs.getInt("id"));
                wc.setCategory(rs.getString("category"));
                dcbm.addElement(wc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoadInputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        if (mainView.isEmptyField(txtLength, "Please enter the length")) {
            return;
        }
        try {
            Double.valueOf(txtLength.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "please enter the length correctly.");
            return;
        }
        if (mainView.isEmptyField(txtCircumference, "Please enter the circumference")) {
            return;
        }
        try {
            Double.valueOf(txtCircumference.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "please enter the circumference correctly.");
            return;
        }
        if (getWoodLoadDetailsId() > 0) {
            WoodLoadDetails wld = new WoodLoadDetails();
            wld.setId(woodLoadDetails.getId());
            WoodCategory wc = (WoodCategory) cmbCategory.getSelectedItem();
            wld.setCategoryId(wc.getId());
            wld.setUnitPrice(Integer.parseInt(txtUnitPrice.getText()));
            wld.setLength(Double.parseDouble(txtLength.getText()));
            wld.setCircumference(Double.parseDouble(txtCircumference.getText()));
            WoodLoadDetailsController wldc = new WoodLoadDetailsController();
            boolean b = wldc.update(wld);
            if (b) {
                setWoodLoadId(getWoodLoadId());
                loadCategory();
                txtUnitPrice.setEnabled(false);
                txtLength.setText("");
                txtCircumference.setText("");
                txtLength.grabFocus();
            }
        } else {
            if (!(getWoodLoadId() > 0)) {
                JOptionPane.showMessageDialog(this, "Please select a wood load");
                return;
            }
            if (!checkCanAdd(getWoodLoadId())) {
                setWoodLoadId(getWoodLoadId());
                txtUnitPrice.setEnabled(false);
                txtLength.setText("");
                txtCircumference.setText("");
                txtLength.grabFocus();
                JOptionPane.showMessageDialog(this, "The required number of wood has been entered.");
                return;
            }
            WoodLoadDetails wld = new WoodLoadDetails();
            wld.setLoadId(getWoodLoadId());
            WoodCategory wc = (WoodCategory) cmbCategory.getSelectedItem();
            wld.setCategoryId(wc.getId());
            wld.setUnitPrice(Integer.parseInt(txtUnitPrice.getText()));
            wld.setLength(Double.parseDouble(txtLength.getText()));
            wld.setCircumference(Double.parseDouble(txtCircumference.getText()));
            WoodLoadDetailsController wldc = new WoodLoadDetailsController();
            boolean b = wldc.create(wld);
            if (b) {
                setWoodLoadId(getWoodLoadId());
                txtUnitPrice.setEnabled(false);
                txtLength.setText("");
                txtCircumference.setText("");
                txtLength.grabFocus();
            }
        }
        txtUnitPrice.setEnabled(false);
        btnUnlockUnit.setEnabled(true);
        btnSave.setText("Add");
        setWoodLoadDetailsId(0);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCubicFeet = new javax.swing.JTextField();
        txtCharge = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInputPanel = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        btnUnlockUnit = new javax.swing.JButton();
        btnSetPrice = new javax.swing.JButton();
        btnAddCategory = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtLength = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtCircumference = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtrowCount = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSelectLoad = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtReduceAmount = new javax.swing.JTextField();
        btnApply = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        jLabel5.setText("jLabel5");

        jTextField4.setText("jTextField4");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton4.setText("jButton4");

        jButton1.setText("jButton1");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Wood load input panel");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Customer:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        txtCustomerName.setEditable(false);
        txtCustomerName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 182;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        jPanel1.add(txtCustomerName, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        txtDate.setEditable(false);
        txtDate.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 182;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel1.add(txtDate, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Cubic feet:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Charge:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel2.add(jLabel7, gridBagConstraints);

        txtCubicFeet.setEditable(false);
        txtCubicFeet.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 118;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 10);
        jPanel2.add(txtCubicFeet, gridBagConstraints);

        txtCharge.setEditable(false);
        txtCharge.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 118;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 11, 10);
        jPanel2.add(txtCharge, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tblInputPanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblInputPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Length", "Circumference", "Category", "Unit price", "Cubic feet", "Charge"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInputPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInputPanelMouseClicked(evt);
            }
        });
        tblInputPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblInputPanelKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblInputPanel);
        if (tblInputPanel.getColumnModel().getColumnCount() > 0) {
            tblInputPanel.getColumnModel().getColumn(0).setResizable(false);
            tblInputPanel.getColumnModel().getColumn(2).setResizable(false);
            tblInputPanel.getColumnModel().getColumn(3).setResizable(false);
            tblInputPanel.getColumnModel().getColumn(4).setResizable(false);
            tblInputPanel.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Category:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel5.add(jLabel10, gridBagConstraints);

        cmbCategory.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 98;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        jPanel5.add(cmbCategory, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setText("Unit price:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel5.add(jLabel8, gridBagConstraints);

        txtUnitPrice.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtUnitPrice.setEnabled(false);
        txtUnitPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnitPriceKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel5.add(txtUnitPrice, gridBagConstraints);

        btnUnlockUnit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUnlockUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/unlock.png"))); // NOI18N
        btnUnlockUnit.setText("Unlock");
        btnUnlockUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnlockUnitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 0, 10);
        jPanel5.add(btnUnlockUnit, gridBagConstraints);

        btnSetPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/settings.png"))); // NOI18N
        btnSetPrice.setText("Sett default");
        btnSetPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetPriceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 140, 0, 10);
        jPanel5.add(btnSetPrice, gridBagConstraints);

        btnAddCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/add.png"))); // NOI18N
        btnAddCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 6, 0, 0);
        jPanel5.add(btnAddCategory, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setText("Length (Feet)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        jPanel6.add(jLabel9, gridBagConstraints);

        txtLength.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtLength.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLengthKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 184;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 11, 194);
        jPanel6.add(txtLength, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel11.setText("Circumference (Inch)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        jPanel7.add(jLabel11, gridBagConstraints);

        txtCircumference.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtCircumference.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCircumferenceKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 184;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 11, 194);
        jPanel7.add(txtCircumference, gridBagConstraints);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/save.png"))); // NOI18N
        btnSave.setText("Add");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 11, 293);
        jPanel8.add(btnSave, gridBagConstraints);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Row count:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel10.add(jLabel13, gridBagConstraints);

        txtrowCount.setEditable(false);
        txtrowCount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 77;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 0, 0);
        jPanel10.add(txtrowCount, gridBagConstraints);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        jPanel10.add(btnEdit, gridBagConstraints);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/drop.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 185);
        jPanel10.add(btnDelete, gridBagConstraints);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        btnSelectLoad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSelectLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/select.png"))); // NOI18N
        btnSelectLoad.setText("Select wood load");
        btnSelectLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectLoadActionPerformed(evt);
            }
        });

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Reduce amount ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 10);
        jPanel11.add(jLabel12, gridBagConstraints);

        txtReduceAmount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 123;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 10);
        jPanel11.add(txtReduceAmount, gridBagConstraints);

        btnApply.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnApply.setText("apply");
        btnApply.setEnabled(false);
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 64, 11, 10);
        jPanel11.add(btnApply, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectLoad)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClose)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(btnSelectLoad))))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)))
                .addGap(15, 15, 15)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnUnlockUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnlockUnitActionPerformed
        txtUnitPrice.setEnabled(true);
        btnUnlockUnit.setEnabled(false);
    }//GEN-LAST:event_btnUnlockUnitActionPerformed

    private void btnSelectLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectLoadActionPerformed
        WoodLoadPanel wlp = new WoodLoadPanel(mainView, this);
        mainView.addTab("Select wood load", wlp);
    }//GEN-LAST:event_btnSelectLoadActionPerformed

    private void tblInputPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInputPanelMouseClicked
        if (tblInputPanel.getRowCount() > 0 && tblInputPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            woodLoadDetails = (WoodLoadDetails) tblInputPanel.getValueAt(tblInputPanel.getSelectedRow(), 0);
            woodCategory = new WoodCategory();
            woodCategory.setId(woodLoadDetails.getCategoryId());
            woodCategory.setCategory(tblInputPanel.getValueAt(tblInputPanel.getSelectedRow(), 2).toString());

        }
    }//GEN-LAST:event_tblInputPanelMouseClicked

    private void btnSetPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetPriceActionPerformed
        UpdateUnitPrice uup = new UpdateUnitPrice(mainView, this);
        mainView.addTab("Update unit price", uup);
    }//GEN-LAST:event_btnSetPriceActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtLengthKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLengthKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCircumference.grabFocus();
        }
    }//GEN-LAST:event_txtLengthKeyReleased

    private void txtCircumferenceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCircumferenceKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            save();
        }
    }//GEN-LAST:event_txtCircumferenceKeyReleased

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        loadCategoryInUpdate(woodCategory);
        setWoodLoadDetailsId(woodLoadDetails.getId());
        txtUnitPrice.setText(String.valueOf(woodLoadDetails.getUnitPrice()));
        txtLength.setText(String.valueOf(woodLoadDetails.getLength()));
        txtCircumference.setText(String.valueOf(woodLoadDetails.getCircumference()));
        btnSave.setText("Update");
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCategoryActionPerformed
        WoodCategoryPanel wcp = new WoodCategoryPanel(mainView, this);
        mainView.addTab("Wood category Panel", wcp);
    }//GEN-LAST:event_btnAddCategoryActionPerformed

    private void tblInputPanelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblInputPanelKeyReleased
        if (tblInputPanel.getRowCount() > 0 && tblInputPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            woodLoadDetails = (WoodLoadDetails) tblInputPanel.getValueAt(tblInputPanel.getSelectedRow(), 0);
            woodCategory = new WoodCategory();
            woodCategory.setId(woodLoadDetails.getCategoryId());
            woodCategory.setCategory(tblInputPanel.getValueAt(tblInputPanel.getSelectedRow(), 2).toString());

        }
    }//GEN-LAST:event_tblInputPanelKeyReleased

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        WoodLoadDetailsController wldc = new WoodLoadDetailsController();
        boolean b = wldc.delete(woodLoadDetails);
        if (b) {
            setWoodLoadId(getWoodLoadId());
        }

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        saveReduceAmount(getWoodLoadId());
    }//GEN-LAST:event_btnApplyActionPerformed

    private void txtUnitPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitPriceKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            txtLength.grabFocus();
        }
    }//GEN-LAST:event_txtUnitPriceKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCategory;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelectLoad;
    private javax.swing.JButton btnSetPrice;
    private javax.swing.JButton btnUnlockUnit;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTable tblInputPanel;
    private javax.swing.JTextField txtCharge;
    private javax.swing.JTextField txtCircumference;
    private javax.swing.JTextField txtCubicFeet;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtLength;
    private javax.swing.JTextField txtReduceAmount;
    private javax.swing.JTextField txtUnitPrice;
    private javax.swing.JTextField txtrowCount;
    // End of variables declaration//GEN-END:variables
}
