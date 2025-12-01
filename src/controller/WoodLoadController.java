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
public class WoodLoadController {
    
    public boolean create(WoodLoad woodLoad){
        String sql="INSERT INTO `wood_load` (`customer_id`, `date`, `emp_id_1`, `emp_id_2`, `pieces`, `permit_no`) VALUES ('"+woodLoad.getCustomerId()+"', '"+woodLoad.getDate().toString()+"', '"+woodLoad.getEmpId1()+"', '"+woodLoad.getEmpId2()+"', '"+woodLoad.getPieces()+"', '"+woodLoad.getPermitNo()+"');";
        boolean b=woodLoad.create(sql);
        return b;        
    }
    public boolean Update(WoodLoad woodLoad){
        String sql="UPDATE `wood_load` SET `customer_id`='"+woodLoad.getCustomerId()+"',`date`='"+woodLoad.getDate().toString()+"',`emp_id_1`='"+String.valueOf(woodLoad.getEmpId1())+"',`emp_id_2`='"+String.valueOf(woodLoad.getEmpId2())+"',`pieces`='"+woodLoad.getPieces()+"', `permit_no` = '"+woodLoad.getPermitNo()+"' WHERE `wood_load`.`id` = '"+woodLoad.getId()+"';";
        boolean b=woodLoad.update(sql);
        return b;        
    }
    
    public boolean delete(WoodLoad woodLoad) {
        String sql = "DELETE FROM wood_load WHERE `wood_load`.`id` = '"+woodLoad.getId()+"';";
        boolean b = woodLoad.update(sql);
        return b;
    }
    
    
}
