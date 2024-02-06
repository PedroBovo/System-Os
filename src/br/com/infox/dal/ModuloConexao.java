/*
 * The MIT License
 *
 * Copyright 2024 Pedro.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.infox.dal;
// a linha abaixo importa o a biblioteca do sql

import java.sql.*;

/**
 * Conexão com o banco de dados
 *
 * @author Pedro
 * @version 1.1
 */
public class ModuloConexao {

    /**
     * metodo responsavel pela conexão com o banco
     *
     * @return conexao
     */
    // metodo responsavel por estabelecer a conexao com o banco
    public static Connection conector() {
        Connection conexao = null;
        // a linha abaixo chama o driver que eu importei para a biblioteca
        String driver = "com.mysql.cj.jdbc.Driver";
        // armazenando informações refenrente ao banco
        String url = "jdbc:mysql://localhost:3306/dbinfox?characterEncoding=utf-8";
        String user = "dba";
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
