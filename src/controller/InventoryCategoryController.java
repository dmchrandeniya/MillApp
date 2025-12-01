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
public class InventoryCategoryController {
 
    public boolean create(InventoryCategory ic){
        String sql="INSERT INTO `inventory_category`(`category`) VALUES ('"+ic.getCategory()+"');";
        return ic.create(sql);
    }
    
    public boolean update(InventoryCategory ic){
        String sql="UPDATE `inventory_category` SET `category`='"+ic.getCategory()+"' WHERE `id`='"+String.valueOf(ic.getId())+"';";
        return ic.update(sql);
    }
    
    public boolean delete(InventoryCategory ic){
        String sql="DELETE FROM `inventory_category` WHERE `id`='"+String.valueOf(ic.getId())+"';";
        return ic.delete(sql);
    }
    
}
