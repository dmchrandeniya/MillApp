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
public class InventoryController {
    
    public boolean create(Inventory i){
        String sql="INSERT INTO `inventory`(`category_id`, `name`, `date`, `price`, `note`) "
                + "VALUES ('"+String.valueOf(i.getCategoryId())+"','"+i.getName()+"','"+i.getDate().toString()+"','"+String.valueOf(i.getPrice())+"','"+i.getNote()+"');";
        boolean b=i.create(sql);
        return b;
    }
    public boolean update(Inventory i){
        String sql="UPDATE `inventory` SET `category_id`='"+String.valueOf(i.getCategoryId())+"',`name`='"+i.getName()+"',`date`='"+i.getDate().toString()+"',`price`='"+String.valueOf(i.getPrice())+"',`note`='"+i.getNote()+"' WHERE `id`='"+i.getId()+"';";
        boolean b=i.update(sql);
        return b;
    }
    public boolean delete(Inventory i){
        String sql="DELETE FROM `inventory` WHERE `id`='"+i.getId()+"';";
        boolean b=i.delete(sql);
        return b;
    }
    
}
