/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Chalindu
 */
public class ExpensesCategoryController {

    public boolean create(ExpencesCategory ec){
        String sql="INSERT INTO `expences_category` (`category`) VALUES ('"+ec.getCategory()+"');";
        return ec.create(sql);
    }
    
    public boolean update(ExpencesCategory ec){
        String sql="UPDATE `expences_category` SET `category`='"+ec.getCategory()+"' WHERE `id`='"+String.valueOf(ec.getId())+"';";
        return ec.update(sql);
    }
    public boolean delete(ExpencesCategory ec){
        String sql="DELETE FROM `expences_category` WHERE `id`='"+String.valueOf(ec.getId())+"';";
        return ec.delete(sql);
    }
    
        
}
