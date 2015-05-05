/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.entidades.Programa;
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
public class ProgramaDao {
   
    public List<Programa> listaPrograma(Long uop) {
        Conexao con = new Conexao();
        List<Programa> lista = new ArrayList<Programa>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cdprograma, nmprograma, cduop FROM PROGRAMAS WHERE cduop = ? and cdprogsup is null  ORDER BY nmprograma");
            ps.setLong(1, uop);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Programa programa = new Programa();
                programa.setCodigo(rs.getLong("CDPROGRAMA"));
                programa.setNome(rs.getString("NMPROGRAMA"));
                programa.setUop(rs.getLong("CDUOP"));
                lista.add(programa);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar o Programa: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
    
    public List<Programa> listaProgramaFilho(Long prog) {
        Conexao con = new Conexao();
        List<Programa> lista = new ArrayList<Programa>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cdprograma, nmprograma, cdprogsup, cduop FROM PROGRAMAS WHERE cdprogsup = ? ORDER BY nmprograma");
            ps.setLong(1, prog);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Programa programa = new Programa();
                programa.setCodigo(rs.getLong("CDPROGRAMA"));
                programa.setNome(rs.getString("NMPROGRAMA"));
                programa.setProgramaSuperior(rs.getLong("CDPROGSUP"));
                programa.setUop(rs.getLong("CDUOP"));
                lista.add(programa);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar o Programa Filho: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
    
    public Programa pegaPorId(int id){
        Conexao con = new Conexao();
        Programa programa = new Programa();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement("SELECT cdprograma, nmprograma FROM PROGRAMAS WHERE cdprograma = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                programa.setCodigo(rs.getLong("CDPROGRAMA"));
                programa.setNome(rs.getString("NMPROGRAMA"));
            }
            return programa;
        } catch (Exception e) {
            System.out.println("Erro ao pesquisar pelo código o Programa: "+e.getMessage());
            return null;
        }finally{
            con.fechaConexao();
        }
    }
}
