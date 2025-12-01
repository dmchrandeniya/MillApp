/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import controller.Expences;
import controller.ExpencesCategory;
import controller.Month;
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
public final class ExpencesPanel extends javax.swing.JPanel {
    
    MainView mainView;
    Expences expences;
    
    /**
     * Creates new form ExpencesPanel
     * @param mainView
     */
    public ExpencesPanel(MainView mainView) {
        initComponents();
        this.mainView=mainView;
        loadTable("All", "");
        mainView.setYear(txtYear);
        loadMonth();
        loadCategory();
        setCost();
        UiSettings.tableProperties(tblExpences);
    }
    
    public void loadTable(String searchBy, String value){
        btnEdit.setEnabled(false);
        DefaultTableModel dtm=(DefaultTableModel)tblExpences.getModel();
        dtm.setRowCount(0);
        expences=new Expences();
        String sql="SELECT * FROM `view_expences`";
        if(value.length()>0){
            if (searchBy.equalsIgnoreCase("Category")) {
                sql+=" WHERE `category` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Date")) {
                sql+=" WHERE `date` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Cost")) {
                sql+=" WHERE `cost` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("Note")) {
                sql+=" WHERE `note` LIKE '%" + value + "%';";
            }
            if (searchBy.equalsIgnoreCase("All")) {
                sql+=" WHERE `category` LIKE '%" + value + "%' OR `date` LIKE '%" + value + "%' OR `cost` LIKE '%" + value + "%' OR `note` LIKE '%" + value + "%';";
            }
        }else{
            sql+=";";
        }
        ResultSet rs=expences.read(sql);
        try {
            while(rs.next()){
                ArrayList<Object> al=new ArrayList<>();
                al.add(rs.getString("category"));
                Expences e=new Expences();
                e.setId(rs.getInt("id"));
                e.setCategoryId(rs.getInt("category_id"));
                e.setDate(rs.getDate("date").toLocalDate());
                e.setCost(rs.getDouble("cost"));
                e.setNote(rs.getString("note"));
                al.add(e);
                al.add(String.valueOf(e.getCost()));
                al.add(e.getNote());
                dtm.addRow(al.toArray());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExpencesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void loadMonth(){
        DefaultComboBoxModel dcbm=(DefaultComboBoxModel)cmbMonth.getModel();
        String sql="SELECT * FROM `month`;";
        Month month=new Month();
        ResultSet rs=month.read(sql);
        try {
            while(rs.next()){
                Month m=new Month();
                m.setId(rs.getInt("id"));
                m.setMonth(rs.getString("month"));
                dcbm.addElement(m);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExpencesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadCategory(){
        DefaultComboBoxModel dcbm=(DefaultComboBoxModel)cmbCategory.getModel();
        String sql="SELECT * FROM `expences_category`;";
        ExpencesCategory ec=new ExpencesCategory();
        ResultSet rs=ec.read(sql);
        try {
            while(rs.next()){
                ec=new ExpencesCategory();
                ec.setId(rs.getInt("id"));
                ec.setCategory(rs.getString("category"));
                dcbm.addElement(ec);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExpencesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setCost(){
        if(mainView.isEmptyField(txtYear, "please enter the year")){
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
        ExpencesCategory ec=(ExpencesCategory)cmbCategory.getSelectedItem();
        double d=0.0;
        String sql="SELECT SUM(`cost`) AS expense FROM `expences` WHERE YEAR(`date`)='"+txtYear.getText()+"' AND MONTH(`date`)='"+String.valueOf(m.getId())+"' AND `category_id`='"+String.valueOf(ec.getId())+"';";
        ResultSet rs=ec.read(sql);
        try {
            if(rs.last()){
                d=rs.getDouble("expense");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        DecimalFormat df=new DecimalFormat("#,###.00");
        txtCost.setText(df.format(d));
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
        btnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtYear = new javax.swing.JTextField();
        cmbMonth = new javax.swing.JComboBox<>();
        cmbCategory = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCost = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cmbSelect = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblExpences = new javax.swing.JTable();

        jButton4.setText("jButton4");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Expenses panel");

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/close.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

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
        gridBagConstraints.ipadx = 144;
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
        gridBagConstraints.ipadx = 111;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 10);
        jPanel1.add(cmbMonth, gridBagConstraints);

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
        gridBagConstraints.ipadx = 111;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 10);
        jPanel1.add(cmbCategory, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Month : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Category : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        txtCost.setEditable(false);
        txtCost.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 144;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 11, 10);
        jPanel1.add(txtCost, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("cost : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

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

        cmbSelect.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "Category", "Date", "Cost", "Note", " " }));

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

        tblExpences.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblExpences.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Date", "Cost", "Note"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblExpences.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblExpencesMouseClicked(evt);
            }
        });
        tblExpences.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblExpencesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblExpences);
        if (tblExpences.getColumnModel().getColumnCount() > 0) {
            tblExpences.getColumnModel().getColumn(0).setResizable(false);
            tblExpences.getColumnModel().getColumn(1).setResizable(false);
            tblExpences.getColumnModel().getColumn(2).setResizable(false);
            tblExpences.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnClose)))
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEdit)
                        .addComponent(btnAdd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        loadTable(cmbSelect.getSelectedItem().toString(), txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mainView.removeTab(this);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tblExpencesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblExpencesMouseClicked
        if(tblExpences.getRowCount()>0 && tblExpences.getSelectedRow()>-1){
            btnEdit.setEnabled(true);
            expences=(Expences)tblExpences.getValueAt(tblExpences.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_tblExpencesMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        ManageExpencesPanel mep=new ManageExpencesPanel(mainView, this, null);
        mainView.addTab("Add expenses", mep);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        ManageExpencesPanel mep=new ManageExpencesPanel(mainView, this, expences);
        mainView.addTab("Add expenses", mep);
    }//GEN-LAST:event_btnEditActionPerformed

    private void tblExpencesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblExpencesKeyReleased
        if(tblExpences.getRowCount()>0 && tblExpences.getSelectedRow()>-1){
            btnEdit.setEnabled(true);
            expences=(Expences)tblExpences.getValueAt(tblExpences.getSelectedRow(), 1);
        }
    }//GEN-LAST:event_tblExpencesKeyReleased

    private void txtYearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYearKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            setCost();
        }
    }//GEN-LAST:event_txtYearKeyReleased

    private void cmbMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMonthActionPerformed
        Thread t=new Thread(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExpencesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            setCost();
        });
        t.start();
    }//GEN-LAST:event_cmbMonthActionPerformed

    private void cmbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoryActionPerformed
        setCost();
    }//GEN-LAST:event_cmbCategoryActionPerformed


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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblExpences;
    private javax.swing.JTextField txtCost;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
