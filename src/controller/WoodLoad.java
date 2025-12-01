/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Crud;

/**
 *
 * @author Chalindu
 */
public class WoodLoad extends Crud {

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

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
    }

    /**
     * @return the empId1
     */
    public int getEmpId1() {
        return empId1;
    }

    /**
     * @param empId1 the empId1 to set
     */
    public void setEmpId1(int empId1) {
        this.empId1 = empId1;
    }

    /**
     * @return the empId2
     */
    public int getEmpId2() {
        return empId2;
    }

    /**
     * @param empId2 the empId2 to set
     */
    public void setEmpId2(int empId2) {
        this.empId2 = empId2;
    }

    /**
     * @return the pieces
     */
    public int getPieces() {
        return pieces;
    }

    /**
     * @return the permitNo
     */
    public String getPermitNo() {
        return permitNo;
    }

    /**
     * @param permitNo the date to set
     */
    public void setPermitNo(String permitNo) {
        this.permitNo = permitNo;
    }

    /**
     * @param pieces the pieces to set
     */
    public void setPieces(int pieces) {
        this.pieces = pieces;
    }
    private int id;
    private int customerId;
    private LocalDate date;
    private int empId1;
    private int empId2;
    private int pieces;
    private String permitNo;

    public boolean checkWoodLoadDetails(int woodLoadId) {
        boolean t = false;
        String sql = "SELECT * FROM `wood_load_details` WHERE `load_id`='" + woodLoadId + "';";
        ResultSet rs = read(sql);
        try {
            t = rs.last();
        } catch (SQLException ex) {
            Logger.getLogger(WoodLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;

    }

    @Override
    public String toString() {
        return getDate().toString();
    }

}
