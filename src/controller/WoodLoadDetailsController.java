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
public class WoodLoadDetailsController {
    
    public boolean create(WoodLoadDetails wld){
        String sql="INSERT INTO `wood_load_details` (`load_id`, `length`, `circumference`, `category_id`, `unit_price`) VALUES ('"+String.valueOf(wld.getLoadId())+"', '"+String.valueOf(wld.getLength())+"', '"+String.valueOf(wld.getCircumference())+"', '"+String.valueOf(wld.getCategoryId())+"', '"+wld.getUnitPrice()+"');";
        boolean b=wld.create(sql);
        return b;
    }
    public boolean update(WoodLoadDetails wld){
        String sql="UPDATE `wood_load_details` SET `length`='"+wld.getLength()+"',`circumference`='"+wld.getCircumference()+"',`category_id`='"+wld.getCategoryId()+"',`unit_price`='"+wld.getUnitPrice()+"' WHERE `id`='"+wld.getId()+"';";
        boolean b=wld.update(sql);
        return b;
    }
    public boolean delete(WoodLoadDetails wld){
        String sql="DELETE FROM wood_load_details WHERE `wood_load_details`.`id` = '"+wld.getId()+"'";
        boolean b=wld.delete(sql);
        return b;
    }
}
