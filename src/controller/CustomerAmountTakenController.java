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
public class CustomerAmountTakenController {
    
    public boolean Create(CustomerAmountTaken cat){
        String sql="INSERT INTO `amount_taken` (`cus_id`, `date`, `amount`) VALUES ('"+String.valueOf(cat.getCusId())+"', '"+cat.getDate().toString()+"', '"+String.valueOf(cat.getAmount())+"');";
        boolean b=cat.create(sql);
        return b;
    }
    public boolean Update(CustomerAmountTaken cat){
        String sql="UPDATE `amount_taken` SET `cus_id`='"+String.valueOf(cat.getCusId())+"',`date`='"+cat.getDate().toString()+"',`amount`='"+String.valueOf(cat.getAmount())+"' WHERE `id`='"+cat.getId()+"';";
        boolean b=cat.update(sql);
        return b;
    }
    public boolean Delete(CustomerAmountTaken cat){
        String sql="DELETE FROM `amount_taken` WHERE `amount_given`.`id` ='"+cat.getId()+"';";
        boolean b=cat.delete(sql);
        return b;
    }
    
}
