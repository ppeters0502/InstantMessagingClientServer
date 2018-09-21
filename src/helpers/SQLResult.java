/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.sql.Blob;

/**
 *
 * @author ppeters
 */
public class SQLResult {
    private String columnType;
    private String columnName;
    private int intResult;
    private String stringResult;
    private byte[] byteResult;
    
    
    public SQLResult(String ct, String cn, int ir)
    {
        this.columnType = ct;
        this.columnName = cn;
        this.intResult = ir;
    }
    public SQLResult(String ct, String cn, String sr)
    {
        this.columnType = ct;
        this.columnName = cn;
        this.stringResult = sr;
    }
    public SQLResult(String ct, String cn, byte[] br)
    {
        this.columnType = ct;
        this.columnName = cn;
        this.byteResult = br;
    }
    public String getColumnType(){
        return columnType;
    }
    public String getColumnName(){
        return columnName;
    }
    public String getStringResult(){
        return stringResult;
    }
    public int getIntResult(){
        return intResult;
    }
    public byte[] getByteResult(){
        return byteResult;
    }
    
}
