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
public class ExpencesCategory extends Crud{

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
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean checkCategory(int categoryId){
        boolean t=false;
        String sql="SELECT * FROM `expences` WHERE `category_id`='"+categoryId+"';";
        ResultSet rs=read(sql);
        try {
            if(rs.last()){
                t=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ExpencesCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }
    
    private int id;
    private String category;

    @Override
    public String toString() {
        return getCategory();
    }
    
    
}
