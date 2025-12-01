/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Crud;

/**
 *
 * @author Chalindu
 */
public class ReduceAmount extends Crud{

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
     * @return the load_id
     */
    public int getLoad_id() {
        return load_id;
    }

    /**
     * @param load_id the load_id to set
     */
    public void setLoad_id(int load_id) {
        this.load_id = load_id;
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
    private int load_id;
    private double amount;
    
}
