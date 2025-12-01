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
public class EmployeeAmountGivenController {
    
    public boolean Create(EmployeeAmountGiven amountGiven){
        String sql="INSERT INTO `amount_given` (`emp_id`, `date`, `amount`) VALUES ('"+String.valueOf(amountGiven.getEmpId())+"', '"+amountGiven.getDate().toString()+"', '"+String.valueOf(amountGiven.getAmount())+"');";
        boolean b=amountGiven.create(sql);
        return b;
    }
    public boolean Update(EmployeeAmountGiven amountGiven){
        String sql="UPDATE `amount_given` SET `emp_id`='"+String.valueOf(amountGiven.getEmpId())+"',`date`='"+amountGiven.getDate().toString()+"',`amount`='"+String.valueOf(amountGiven.getAmount())+"' WHERE `id`='"+amountGiven.getId()+"';";
        boolean b=amountGiven.update(sql);
        return b;
    }
    public boolean Delete(EmployeeAmountGiven amountGiven){
        String sql="DELETE FROM amount_given WHERE `amount_given`.`id` ='"+amountGiven.getId()+"';";
        boolean b=amountGiven.delete(sql);
        return b;
    }
    
}
