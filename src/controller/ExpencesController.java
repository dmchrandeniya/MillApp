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
public class ExpencesController {
    
    public boolean create(Expences e){
        String sql="INSERT INTO `expences` (`category_id`, `date`, `cost`, `note`) "
                + "VALUES ('"+String.valueOf(e.getCategoryId())+"', '"+e.getDate().toString()+"', '"+String.valueOf(e.getCost())+"', '"+e.getNote()+"');";
        return e.create(sql);
    }
    
    public boolean update(Expences e){
        String sql="UPDATE `expences` SET `category_id`='"+String.valueOf(e.getCategoryId())+"',`date`='"+e.getDate().toString()+"',`cost`='"+String.valueOf(e.getCost())+"',`note`='"+e.getNote()+"' WHERE `id`='"+String.valueOf(e.getId())+"';";
        return e.update(sql);
    }
    
}
