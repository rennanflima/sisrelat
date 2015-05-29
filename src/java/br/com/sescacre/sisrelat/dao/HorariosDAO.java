/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sescacre.sisrelat.dao;

import br.com.sescacre.sisrelat.entidades.Horarios;
import br.com.sescacre.sisrelat.entidades.ProgramaCorrente;
import br.com.sescacre.sisrelat.util.Conexao;
import br.com.sescacre.sisrelat.util.DateConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Rennan Francisco
 */
public class HorariosDAO {
    
    public Map<DayOfWeek, Horarios> pegaHorarioDaAtividade(Long atividade, Long configuracao, Long sequenciaOcorrcencia) {
        Conexao con = new Conexao();
        Map<DayOfWeek, Horarios> lista = new HashMap<>();
        try {
            Connection conn = con.abreConexao();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT H.HRINICIO, H.HRFIM, H.DDSEMANA "
                    + "FROM PROGOCORR PO INNER JOIN HORARIOS H ON H.CDELEMENT = SUBSTR(CAST(100000000 + PO.CDPROGRAMA AS CHAR(9)), 2, 8) || "
                    + "SUBSTR(CAST(100000000 + PO.CDCONFIG AS CHAR(9)), 2, 8) || SUBSTR(CAST(100000000 + PO.SQOCORRENC AS CHAR(9)), 2, 8) "
                    + "WHERE PO.CDPROGRAMA = ? AND PO.CDCONFIG = ? AND PO.SQOCORRENC = ?");
            ps.setLong(1, atividade);
            ps.setLong(2, configuracao);
            ps.setLong(3, sequenciaOcorrcencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Horarios hora = new Horarios();
                hora.setHoraInicio(DateConverter.convertDateToLocalDateTime(rs.getTime("HRINICIO")));
                hora.setHoraTermino(DateConverter.convertDateToLocalDateTime(rs.getTime("HRFIM")));
                switch(rs.getInt("DDSEMANA")){
                    case 1:
                        lista.put(DayOfWeek.SUNDAY, hora);
                        break;
                    case 2:
                        lista.put(DayOfWeek.MONDAY, hora);
                        break;
                    case 3:
                        lista.put(DayOfWeek.TUESDAY, hora);
                        break;
                    case 4:
                        lista.put(DayOfWeek.WEDNESDAY, hora);
                        break;
                    case 5:
                        lista.put(DayOfWeek.THURSDAY, hora);
                        break;
                    case 6:
                        lista.put(DayOfWeek.FRIDAY, hora);
                        break;
                    case 7:
                        lista.put(DayOfWeek.SATURDAY, hora);
                        break;
                }
                
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Erro ao pesquisar pelo código a Atividade: " + e.getMessage());
            return null;
        } finally {
            con.fechaConexao();
        }
    }
}
