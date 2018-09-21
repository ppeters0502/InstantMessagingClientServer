/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    
    
    
    public ArrayList<SQLResult> select(String nameDB, String nameTable, String selection, String values) throws ClassNotFoundException{
        c = null;
        stmt = null;
        sqlStatement = "SELECT "+selection.toUpperCase()+" from "+nameTable.toUpperCase()+";";
        if(values != "")
        {
            sqlStatement = "SELECT "+selection.toUpperCase()+" from "+nameTable.toUpperCase()
                    +" WHERE "+values+";";
        }
        System.out.println("Query to run: \n"+sqlStatement);
        ResultSet temp = null;
        ArrayList<SQLResult> results = new ArrayList<SQLResult>();
        Class.forName("org.sqlite.JDBC");
        url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/"+nameDB;
        try(Connection conn = this.connectDB(nameDB);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement)){
            int iResult = getColumnCount(rs);  
              String[] colNames = getColumnNameArray(rs);
              String[] colTypes = getColumnTypeArray(rs);
              while(rs.next())
              {
                  SQLResult resultInstance = null;
                  for(int i=1; i<iResult; i++)
                  {
                     switch(colTypes[i]){
                     case "text":
                        resultInstance = new SQLResult(colTypes[i],colNames[i],rs.getString(colNames[i]));
                        break;
                     case "integer":
                         resultInstance = new SQLResult(colTypes[i],colNames[i],rs.getInt(colNames[i]));
                         break;
                     case "blob":
                         resultInstance = new SQLResult(colTypes[i],colNames[i],rs.getBytes(colNames[i]));
                         break;
                    }
                    results.add(resultInstance); 
                  }
                  
              }
            
        }catch(SQLException e)
        {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
        return results;
    }
    
    //Following methods return the column count, the column types and the column names, so that we can abstract the select query and return a list of SQLResults.
    
    
     private int getColumnCount(ResultSet rs) {
        int iOutput = 0;
            try {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                iOutput = rsMetaData.getColumnCount();
            } catch (Exception e) {
                System.out.println(e);
                return iOutput = -1;
            }
        return iOutput;
    }
    
     private String[] getColumnTypeArray(ResultSet rs) {
        String sArr[] = null;
        try {
            ResultSetMetaData rm = rs.getMetaData();
            String sArray[] = new String[rm.getColumnCount()];
            for (int ctr = 1; ctr <= sArray.length; ctr++) {
                String s = rm.getColumnTypeName(ctr);
                sArray[ctr - 1] = s;
            }
            return sArray;
        } catch (Exception e) {
            System.out.println(e);
            return sArr;
        }
    }
     
    public String[] getColumnNameArray(ResultSet rs) {
        String sArr[] = null;
        try {
            ResultSetMetaData rm = rs.getMetaData();
            String sArray[] = new String[rm.getColumnCount()];
            for (int ctr = 1; ctr <= sArray.length; ctr++) {
                String s = rm.getColumnName(ctr);
                sArray[ctr - 1] = s;
            }
            return sArray;
        } catch (Exception e) {
            System.out.println(e);
            return sArr;
        }
    }
    
    
    
    
    
    
    public void TestSQLSelect(String username) throws ClassNotFoundException{
        String sql = "SELECT * from Users where UserName like \'"+username+"\'";
        url = "jdbc:sqlite:C:/Users/ppeters/Documents/Git/db/instantmessenger.db";
        try(Connection conn = this.connectDB("instantmessenger.db");
            Statement stmt = conn.createStatement()){
                ResultSet rs = stmt.executeQuery(sql);
                
            //Loop through ResultSet
            while(rs.next()){
                System.out.println("ID: "+rs.getInt("UserID")+"\n"
                  +"UserName: "+rs.getString("UserName")+"\n"+"isAdmin: "+rs.getInt("isAdmin")
                  +"\nHash: "+rs.getString("Hash")
                );
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    
    }
    
    
}
