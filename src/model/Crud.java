package model;

import java.sql.ResultSet;

/**
 *
 * @author Chalindu
 */
public class Crud implements DBOperations{

    @Override
    public boolean create(String sql) {
        return Connect.setQuery(sql);
    }

    @Override
    public ResultSet read(String sql) {
        return Connect.getQuery(sql);
    }

    @Override
    public boolean update(String sql) {
        return Connect.setQuery(sql);
    }

    @Override
    public boolean delete(String sql) {
        return Connect.setQuery(sql);
    }
    
}
