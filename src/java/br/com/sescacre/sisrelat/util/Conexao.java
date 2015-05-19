/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Rennan Francisco
 */
public class Conexao {

    private String driver = "com.ibm.db2.jcc.DB2Driver";
    private String url = "jdbc:db2://192.168.0.220:50000/BDPROD";
    private String user = "db2dba";
    private String senha = "$3$c4cr3db2";
    
    Connection con = null;

    public Connection abreConexao() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, senha);
        } catch (ClassNotFoundException e) {
            System.out.println("Classe não encontrada. " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Problema com o BD. " + e.getMessage());
        }
        return con;
    }

    public void fechaConexao() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println("Problema ao fechar a conexão. " + e.getMessage());
        }
    }
}
