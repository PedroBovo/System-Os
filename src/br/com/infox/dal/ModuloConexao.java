/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.dal;
// a linha abaixo importa o a biblioteca do sql
import java.sql.*;

/**
 *
 * @author Pedro
 */
public class ModuloConexao {

    // metodo responsavel por estabelecer a conexao com o banco
    public static Connection conector() {
       Connection conexao = null;
        // a linha abaixo chama o driver que eu importei para a biblioteca
        String driver = "com.mysql.cj.jdbc.Driver";
        // armazenando informações refenrente ao banco
        String url = "jdbc:mysql://localhost:3306/dbinfox";
        String user = "root";
        String password = "J@guar456";
        // estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            // a linha abaixo serve de apoio para esclarecer o erro
            //System.out.println(e);

            return null;
        }
    }
}
