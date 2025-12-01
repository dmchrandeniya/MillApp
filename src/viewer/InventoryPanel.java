/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.Inventory;
import controller.InventoryCategory;
import controller.Month;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Year;
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
public class InventoryPanel extends javax.swing.JPanel {

    MainView mainView;
    Inventory inventory;

    /**
     * Creates new form InventoryPanel
     *
     * @param mainView
     */
    public InventoryPanel(MainView mainView) {
        initComponents();
        this.mainView = mainView;        
        Thread t = new Thread(() -> {
            txtSearch.grabFocus();
            UiSettings.tableProperties(tblInventoryPanel);
            loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText().trim());
        });
        t.start();    
        setYear();
        loadMonth();
        loadCategory();
        setCost();
    }

    public void loadMonth() {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) cmbMonth.getModel();
        Month month = new Month();
        ResultSet rs = month.read("SELECT * FROM `month`");
        try {
            while (rs.next()) {
                Month m = new Month();
                m.setId(rs.getInt("id"));
                m.setMonth(rs.getString("month"));
                dcbm.addElement(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadCategory(){
        DefaultComboBoxModel dcbm=(DefaultComboBoxModel)cmbCategory.getModel();
        InventoryCategory ic=new InventoryCategory();
        ResultSet rs=ic.read("SELECT * FROM `inventory_category`");
        try {
            while(rs.next()){
                ic=new InventoryCategory();
                ic.setId(rs.getInt("id"));
                ic.setCategory(rs.getString("category"));
                dcbm.addElement(ic);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setYear() {
        int year = Year.now().getValue();
        txtYear.setText(String.valueOf(year));
    }

    public void setCost() {
        if (mainView.isEmptyField(txtYear, "please enter the year")) {
            return;
        }
        try {
            Integer.valueOf(txtYear.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year.");
            return;
        }
        Month m = (Month) cmbMonth.getSelectedItem();
        InventoryCategory ic=(InventoryCategory)cmbCategory.getSelectedItem();
        Inventory i = new Inventory();
        double c = 0.0;
        ResultSet rs = i.read("SELECT SUM(`price`) AS month_cost FROM `inventory` WHERE YEAR(`date`)='" + txtYear.getText() + "' AND MONTH(`date`)='" + String.valueOf(m.getId()) + "' AND `category_id`='"+String.valueOf(ic.getId())+"';");
        try {
            if (rs.last()) {
                c = rs.getDouble("month_cost");
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        txtMonthCost.setText(df.format(c));
    }

    public void loadTable(String searchBy, String value) {
        btnEdit.setEnabled(false);
        DefaultTableModel dtm = (DefaultTableModel) tblInventoryPanel.getModel();
        dtm.setRowCount(0);
        inventory = new Inventory();
        String sql = "SELECT * FROM `view_inventory`";
        if (value.length() > 0) {
            if (searchBy.equalsIgnoreCase("Category")) {
                sql += " WHERE `category` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Name")) {
                sql += " WHERE `name` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Date")) {
                sql += " WHERE `date` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Price")) {
                sql += " WHERE `price` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Note")) {
                sql += " WHERE `note` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("All")) {
                sql += " WHERE `category` LIKE '%" + value + "%' OR `name` LIKE '%" + value + "%' OR `date` LIKE '%" + value + "%' OR `price` LIKE '%" + value + "%' OR `note` LIKE '%" + value + "%';";
            }
        } else {
            sql += ";";
        }
        ResultSet rs = inventory.read(sql);
        try {
            while (rs.next()) {
                ArrayList<Object> al = new ArrayList();
                al.add(rs.getString("category"));
                Inventory i = new Inventory();
                i.setId(rs.getInt("id"));
                i.setCategoryId(rs.getInt("category_id"));
                i.setName(rs.getString("name"));
                i.setDate(rs.getDate("date").toLocalDate());
                i.setPrice(rs.getDouble("price"));
                i.setNote(rs.getString("note"));
                al.add(i);
                al.add(i.getDate());
                DecimalFormat df = new DecimalFormat("#,###.00");
                al.add(df.format(i.getPrice()));
                al.add(i.getNote());
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
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
        btnClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtYear = new javax.swing.JTextField();
        cmbMonth = new javax.swing.JComboBox<>();
        txtMonthCost = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cmbSelect = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInventoryPanel = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();

        jButton4.setText("jButton4");

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Inventory panel");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        txtYear.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtYearKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 138;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 10);
        jPanel1.add(txtYear, gridBagConstraints);

        cmbMonth.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMonthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 105;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 10);
        jPanel1.add(cmbMonth, gridBagConstraints);

        txtMonthCost.setEditable(false);
        txtMonthCost.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 138;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 11, 10);
        jPanel1.add(txtMonthCost, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 54;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Month : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Month cost : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        cmbCategory.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 105;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 10);
        jPanel1.add(cmbCategory, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Category : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        cmbSelect.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "Category", "Name", "Date", "Price", "Note" }));

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

        tblInventoryPanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblInventoryPanel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Name", "Date", "Price", "Note"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInventoryPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInventoryPanelMouseClicked(evt);
            }
        });
        tblInventoryPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblInventoryPanelKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblInventoryPanel);
        if (tblInventoryPanel.getColumnModel().getColumnCount() > 0) {
            tblInventoryPanel.getColumnModel().getColumn(0).setResizable(false);
            tblInventoryPanel.getColumnModel().getColumn(1).setResizable(false);
            tblInventoryPanel.getColumnModel().getColumn(2).setResizable(false);
            tblInventoryPanel.getColumnModel().getColumn(3).setResizable(false);
            tblInventoryPanel.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/pencil.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEdit)
                        .addComponent(btnAdd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed
        Thread t=new Thread(() -> {
            try {
                Thread.sleep(250);
                setCost();
            } catch (InterruptedException ex) {
                Logger.getLogger(InventoryPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        t.start();
    }//GEN-LAST:event_cmbMonthActionPerformed

    private void txtYearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setCost();
        }
    }//GEN-LAST:event_txtYearKeyReleased

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tblInventoryPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInventoryPanelMouseClicked
        if (tblInventoryPanel.getRowCount() > 0 && tblInventoryPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            inventory = (Inventory) tblInventoryPanel.getValueAt(tblInventoryPanel.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_tblInventoryPanelMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ManageInventoryPanel mip = new ManageInventoryPanel(mainView, this, null);
        mainView.addTab("Add item", mip);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        ManageInventoryPanel mip = new ManageInventoryPanel(mainView, this, inventory);
        mainView.addTab("Edit Item", mip);
    }//GEN-LAST:event_btnEditActionPerformed

    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
        setCost();
    }//GEN-LAST:event_cmbCategoryActionPerformed

    private void tblInventoryPanelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblInventoryPanelKeyReleased
        if (tblInventoryPanel.getRowCount() > 0 && tblInventoryPanel.getSelectedRow() > -1) {
            btnEdit.setEnabled(true);
            inventory = (Inventory) tblInventoryPanel.getValueAt(tblInventoryPanel.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_tblInventoryPanelKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbMonth;
    private javax.swing.JComboBox<String> cmbSelect;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblInventoryPanel;
    private javax.swing.JTextField txtMonthCost;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
