package com.sigdoc.sigdoc_backend.expose.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class DateConverterUtil {
    public static LocalDate parseFechaDDMMYYYY(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null; // O lanzar una excepción, según tu lógica de negocio
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            return LocalDate.parse(fechaStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha '" + fechaStr + "': " + e.getMessage());
            throw new IllegalArgumentException("Formato de fecha inválido. Se esperaba DD/MM/AAAA.", e);
        }
    }

    public static String formatLocalDateToDDMMYYYY(LocalDate localDate) {
        if (localDate == null) {
            return null; // O una cadena vacía, según tu lógica de negocio
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return localDate.format(formatter);
    }
}
