/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Crud;

/**
 *
 * @author Chalindu
 */
public class Employee extends Crud {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
    private int id;
    private String name;
    private String address;
    private String phone;
    private String note;

    @Override
    public String toString() {
        return getName();
    }

    public boolean checkEmployee(int employeeId) {
        boolean t = false;
        boolean b1=false;
        boolean b2=false;
        String sql1 = "SELECT * FROM `wood_load` WHERE `emp_id_1`='"+employeeId+"';";
        ResultSet rs1 = read(sql1);
        try {
            b1 = rs1.last();
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql2 = "SELECT * FROM `wood_load` WHERE `emp_id_1`='"+employeeId+"';";
        ResultSet rs2 = read(sql2);
        try {
            b1 = rs2.last();
        } catch (SQLException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
        t= b1||b2;
        return t;
    }
}
