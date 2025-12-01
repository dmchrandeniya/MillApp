/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Asus
 */
public class UiSettings {
    
    public static void tableProperties(JTable Table){
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(Table.getModel());        
        Table.setRowSorter(sorter);
        Table.setRowHeight(30);
        JTableHeader tableHeader = Table.getTableHeader();
        tableHeader.setForeground(Color.BLACK);
        Font headerFont = new Font("Tahoma", Font.BOLD, 20);
        tableHeader.setFont(headerFont);
    }
    
    
}
