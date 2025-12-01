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
public class WoodCategoryController {
    
    public boolean create(WoodCategory woodCategory) {
        String sql = "INSERT INTO `wood_category` (`category`) VALUES ('"+woodCategory.getCategory()+"');";
        boolean b = woodCategory.create(sql);
        return b;
    }

    public boolean update(WoodCategory woodCategory) {
        String sql = "UPDATE `wood_category` SET `category` = '"+woodCategory.getCategory()+"' WHERE `wood_category`.`id` = '"+woodCategory.getId()+"';";
        boolean b = woodCategory.update(sql);
        return b;
    }
    
    public boolean delete(WoodCategory woodCategory){
        String sql="DELETE FROM wood_category WHERE `wood_category`.`id` = '"+woodCategory.getId()+"';";
        boolean b=woodCategory.delete(sql);
        return b;
    }
}
