/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.time.LocalDate;
import model.Crud;

/**
 *
 * @author Chalindu
 */
public class EmployeeAmountGiven extends Crud{

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
     * @return the empId
     */
    public int getEmpId() {
        return empId;
    }

    /**
     * @param emp_id the emp_id to set
     */
    public void setEmpId(int empId) {
        this.empId=empId;
    }

    /**
     * @return the Date
     */
    public LocalDate getDate() {
        return Date;
    }

    /**
     * @param Date the Date to set
     */
    public void setDate(LocalDate Date) {
        this.Date = Date;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    private int id;
    private int empId;
    private LocalDate Date;
    private double amount;

    @Override
    public String toString() {
        return getDate().toString();
    }
    
    
    
}
