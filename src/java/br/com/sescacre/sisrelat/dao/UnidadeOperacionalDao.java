/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.entidades.UnidadeOperacional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rennan Francisco
 */
public class UnidadeOperacionalDao {
    
    public List<UnidadeOperacional> ListaTodos() {
        Conexao con = new Conexao();
        List<UnidadeOperacional> lista = new ArrayList<UnidadeOperacional>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cduop, nmuop FROM UOP WHERE cdadmin = 3 ORDER BY nmuop");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UnidadeOperacional uop = new UnidadeOperacional();
                uop.setCodigo(rs.getLong("CDUOP"));
                uop.setNome(rs.getString("NMUOP"));
                lista.add(uop);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar a Unidade Operacional: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
    
    public UnidadeOperacional pegaPorId(Long id){
        Conexao con = new Conexao();
        UnidadeOperacional uop = new UnidadeOperacional();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement("SELECT cduop, nmuop FROM UOP WHERE cduop = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                uop.setCodigo(rs.getLong("CDUOP"));
                uop.setNome(rs.getString("NMUOP"));
            }
            return uop;
        } catch (Exception e) {
            System.out.println("Erro ao pesquisar pelo código a Unidade Operacional: "+e.getMessage());
            return null;
        }finally{
            con.fechaConexao();
        }
    }
    
}
