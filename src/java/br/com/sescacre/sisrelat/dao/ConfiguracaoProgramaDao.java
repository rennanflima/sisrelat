/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.entidades.ConfiguracaoPrograma;
import br.com.sescacre.sisrelat.entidades.Programa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rennan Francisco
 */
public class ConfiguracaoProgramaDao {

    
    public List<ConfiguracaoPrograma> listaConfiguracaoPrograma(Long prog) {
        Conexao con = new Conexao();
        List<ConfiguracaoPrograma> lista = new ArrayList<ConfiguracaoPrograma>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cdprograma, cdconfig, nmconfig FROM CONFPROG WHERE cdprograma = ? ORDER BY nmconfig");
            ps.setLong(1, prog);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ConfiguracaoPrograma config = new ConfiguracaoPrograma();
                config.setCodigo(rs.getLong("CDCONFIG"));
                config.setPrograma(rs.getLong("CDPROGRAMA"));
                config.setNome(rs.getString("NMCONFIG"));
                lista.add(config);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar a Configuração do Programa: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
        return lista;
    }
}
