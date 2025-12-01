/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.ResultSet;

/**
 *
 * @author Chalindu
 */
public interface DBOperations {
    public boolean create(String sql);
    public ResultSet read(String sql);
    public boolean update(String sql);
    public boolean delete(String sql);
    
}
