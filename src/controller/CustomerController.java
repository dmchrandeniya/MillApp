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
public class CustomerController {

    public boolean create(Customer customer) {
        String sql = "INSERT INTO `customer` (`name`, `address`, `phone`, `note`) VALUES ('" + customer.getName() + "', '" + customer.getAddress() + "', '" + customer.getPhone() + "', '" + customer.getNote() + "');";
        boolean b = customer.create(sql);
        return b;
    }

    public boolean update(Customer customer) {
        String sql = "UPDATE `customer` SET `name` = '" + customer.getName() + "',`address`='" + customer.getAddress() + "',`phone`='" + customer.getPhone() + "',`note`='" + customer.getNote() + "' WHERE `customer`.`id` = " + customer.getId() + ";";
        boolean b = customer.update(sql);
        return b;
    }

    public boolean delete(Customer customer) {
        String sql = "DELETE FROM customer WHERE `customer`.`id` = '" + customer.getId() + "';";
        boolean b = customer.update(sql);
        return b;
    }
}
