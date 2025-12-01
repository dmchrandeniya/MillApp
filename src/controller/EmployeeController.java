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
public class EmployeeController {

    public boolean create(Employee employee) {
        String sql = "INSERT INTO `employee` (`name`, `address`, `phone`, `note`) VALUES ('" + employee.getName() + "', '" + employee.getAddress() + "', '" + employee.getPhone() + "', '" + employee.getNote() + "');";
        boolean b = employee.create(sql);
        return b;
    }

    public boolean update(Employee employee) {
        String sql = "UPDATE `employee` SET `name` = '" + employee.getName() + "',`address`='" + employee.getAddress() + "',`phone`='" + employee.getPhone() + "',`note`='" + employee.getNote() + "' WHERE `employee`.`id` = " + employee.getId() + ";";
        boolean b = employee.update(sql);
        return b;
    }

    public boolean delete(Employee employee) {
        String sql = "DELETE FROM `employee` WHERE `employee`.`id` = '" + employee.getId() + "';";
        boolean b = employee.update(sql);
        return b;
    }
}
