//package model;
//
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
// /*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author donwismal
// */
//public class Connect {
//
////    public static String IP_ADDRESS;
//    public static PreparedStatement preState;
//    static Statement statement;
//    public static Connection con = null;
//    
//
//    public static void createConnection() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://localhost/mill_app", "root", "");
////            con = DriverManager.getConnection("jdbc:mysql://DESKTOP-BC51Q5R:3306/lms", "dbuser", "root@123");
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    public static boolean setQuery(String query) {
//
//        try {
//            preState = con.prepareStatement(query);
//            if (preState.executeUpdate() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return false;
//    }
//
//    public static ResultSet getQuery(String query) {
//        try {
//            statement = con.createStatement();
//            ResultSet rs = statement.executeQuery(query);
//            return rs;
//        } catch (SQLException ex) {
//            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//}
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connect {

    public static PreparedStatement preState;
    public static Statement statement;
    public static Connection con = null;

    public static void createConnection() {
        try {
            // Updated driver for MySQL 8+
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Updated URL with timezone and SSL options
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mill_app?useSSL=false&serverTimezone=UTC",
                    "root",
                    ""
            );

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean setQuery(String query) {
        try {
            preState = con.prepareStatement(query);
            return preState.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static ResultSet getQuery(String query) {
        try {
            // Fix: enable scrollable result set
            statement = con.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            return statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
