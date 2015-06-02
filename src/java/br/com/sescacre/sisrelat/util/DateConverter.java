/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sescacre.sisrelat.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Rennan Francisco
 */
public class DateConverter {

    public static LocalDateTime convertDateToLocalDateTime(Date dbData) {
        Instant instant = Instant.ofEpochMilli(dbData.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    
    public static LocalDate convertDateToLocalDate(Date dbData) {
        Instant instant = Instant.ofEpochMilli(dbData.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }
    
    public static LocalTime convertDateToLocalTime(Date dbData) {
        Instant instant = Instant.ofEpochMilli(dbData.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
    }
    
    public static Date convertLocalDateTimeToDate(LocalDateTime attribute) {
        Instant instant = attribute.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
    
    public static Date convertLocalDateToDate(LocalDate attribute) {
        Instant instant = attribute.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}

