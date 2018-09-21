/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import user.credential;

public class SQLHelper {
    private Connection c;
    private Statement stmt;
    private String url;
    private String sqlStatement;
    
    public SQLHelper() {
        c = null;
        stmt = null;
    }
    public Connection connectDB(String fileName) throws ClassNotFoundException{
        c=null;
        url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/" + fileName;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            if (c != null) {
                DatabaseMetaData meta = c.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }
    
    public void createUserTable() throws ClassNotFoundException {
        c=null;
        stmt = null;
        //Creating users table
        url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/instantmessenger.db";
        sqlStatement = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "UserID INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                +"UserName text NOT NULL, \n"
                +"isAdmin int NOT NULL, \n"
                +"Hash text NOT NULL,\n"
                +"Salt blob NOT NULL\n"
                +");";
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            stmt = c.createStatement();
            stmt.execute(sqlStatement);
        } catch(SQLException e){e.printStackTrace();}
    }
    
    public void insert(String nameDB, String nameTable, String names, String values) throws ClassNotFoundException{
        c = null;
        stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/"+nameDB;
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            sqlStatement = "INSERT into "+nameTable.toUpperCase()+"("+names+") VALUES ("+values+");";
            stmt.executeUpdate(sqlStatement);
            stmt.close();
            c.commit();
            c.close();
        } catch(SQLException e)
        {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
        
    }
    
    public void insertUsers(String nameDB, String nameTable, String[] user, credential values) throws ClassNotFoundException{
            Class.forName("org.sqlite.JDBC");
            url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/"+nameDB;
            System.out.println("Opened database successfully");
            sqlStatement = "INSERT into "+nameTable.toUpperCase()+"(UserName, isAdmin, Hash, Salt) VALUES (?,?,?,?)";
            try (Connection conn = this.connectDB(nameDB);
                PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {
            int isAdmin;
            if(user[1]=="1" || user[1] == "true")
                isAdmin = 1;
            else
                isAdmin = 0;
            pstmt.setString(1, user[0]);
            pstmt.setInt(2, isAdmin);
            pstmt.setString(3, values.getHash());
            pstmt.setBytes(4, values.getSalt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
    }
            
            
            
    
    } 
    
    
    public void delete(){}
    
    
    
    public ResultSet select(String nameDB, String nameTable, String selection, String values) throws ClassNotFoundException{
        c = null;
        stmt = null;
        ResultSet temp = null;
        try{
            url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/"+nameDB;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            sqlStatement = "SELECT "+selection.toUpperCase()+" from "+nameTable.toUpperCase();
            if(values != null)
            {
                sqlStatement = "SELECT "+selection.toUpperCase()+" from "+nameTable.toUpperCase()
                        +"WHERE "+values+";";
            }   
            stmt = c.createStatement();
            temp = stmt.executeQuery(sqlStatement);
            stmt.close();
            c.close();
            
        }catch(SQLException e)
        {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
        return temp;
    }
}
