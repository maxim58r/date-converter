package ru.neoflex;

import lombok.SneakyThrows;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public class Converter {
    private static int offset;
    // пример корректной даты 2019-12-15T23:59:59
    // пример запроса  "10/20"

    @SneakyThrows
    public static XMLGregorianCalendar toXmlGregorianCalendar(OffsetDateTime date) {
        try {
            return DatatypeFactory
                    .newInstance()
                    .newXMLGregorianCalendar(
                            date.getYear(),
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            date.getHour(),
                            date.getMinute(),
                            date.getSecond(),
                            0,
                            date.getOffset().getTotalSeconds() / 60);
        } catch (DatatypeConfigurationException e) {
            throw new DatatypeConfigurationException(e.getMessage());
        }
    }

    @SneakyThrows
    public static XMLGregorianCalendar createXMLGregorianCalendar() {
        var nowOffsetDateTime = OffsetDateTime.now();
        return DatatypeFactory
                .newInstance()
                .newXMLGregorianCalendar(
                        nowOffsetDateTime.getYear(),
                        nowOffsetDateTime.getMonthValue(),
                        nowOffsetDateTime.getDayOfMonth(),
                        nowOffsetDateTime.getHour(),
                        nowOffsetDateTime.getMinute(),
                        nowOffsetDateTime.getSecond(),
                        0,
                        nowOffsetDateTime.getOffset().getTotalSeconds() / 60);
    }

    public static XMLGregorianCalendar convertStringBankDateToXMLGregorian(String bankDate) {
        XMLGregorianCalendar xmlGregorianCalendar = null;
        LocalDateTime time = convertStringBankDateToLocalDateTime(bankDate);
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(time.toString());
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlGregorianCalendar;
    }

    public static XMLGregorianCalendar convertLocalDateTimeToXMLGregorian(LocalDateTime localDateTime) {
        XMLGregorianCalendar xmlGregorianCalendar = null;
        String str = localDateTime.toString();
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    str);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlGregorianCalendar;
    }

    public static XMLGregorianCalendar convertStringToXMLGregorian(String stringDate) {
        XMLGregorianCalendar xmlGregorianCalendar = null;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(stringDate);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlGregorianCalendar;
    }

    public static XMLGregorianCalendar convertOffsetDateTimeToXmlGregorianCalendar(OffsetDateTime offsetDateTime) {
        try {
            return DatatypeFactory
                    .newInstance()
                    .newXMLGregorianCalendar(offsetDateTime.getYear(),
                            offsetDateTime.getMonthValue(),
                            offsetDateTime.getDayOfMonth(),
                            offsetDateTime.getHour(),
                            offsetDateTime.getMinute(),
                            offsetDateTime.getSecond(),
                            0,
                            offsetDateTime.getOffset().getTotalSeconds() / 60);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("ошибка преобразования времени MassageDate: " + e);
        }
    }

    public static LocalDateTime convertStringBankDateToLocalDateTime(String bankDate) {
        String s = "-01T23:59:59";
        String[] split = bankDate.split("/");
        String str = Optional.of(split)
                .filter(spl -> spl.length == 2)
                .map(strings -> "20" + split[1] + "-" + split[0] + s)
                .orElseThrow(() -> {
                    throw new RuntimeException("Invalid date format");
                });
        LocalDateTime time = LocalDateTime.parse(str);
        return time.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime convertXMLGregorianToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        return LocalDateTime.of(
                xmlGregorianCalendar.getYear(),
                xmlGregorianCalendar.getMonth(),
                xmlGregorianCalendar.getDay(),
                xmlGregorianCalendar.getHour(),
                xmlGregorianCalendar.getMinute(),
                xmlGregorianCalendar.getSecond()
        );
    }

    public static LocalDateTime convertStringToLocalDateTime(String stringDate) {
        return convertXMLGregorianToLocalDateTime(convertStringToXMLGregorian(stringDate));
    }

    public static OffsetDateTime convertXMLGregorianToOffsetDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        var localDateTime = convertXMLGregorianToLocalDateTime(xmlGregorianCalendar);
        return OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(offset));
    }

    public static OffsetDateTime convertStringBankDateToOffsetDateTime(String bankDate) {
        String s = "-01T23:59:59+01:00";
        String[] split = bankDate.split("/");
        String str = Optional.of(split)
                .filter(spl -> spl.length == 2)
                .map(strings -> "20" + split[1] + "-" + split[0] + s)
                .orElseThrow(() -> {
                    throw new RuntimeException("Invalid date format");
                });
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(str);
        return offsetDateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static String convertOffsetDateTimeToBankDateString(OffsetDateTime offsetDateTime) {
        var yearC = String.valueOf(offsetDateTime.getYear());
        var year = yearC.substring(2);
        var month = String.valueOf(offsetDateTime.getMonthValue());
        if (offsetDateTime.getMonthValue() < 10) {
            month = "0" + month;
        }
        return String.format("%s/%s", month, year);
    }

    public static String convertOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
        return LocalDateTime.of(
                offsetDateTime.getYear(),
                offsetDateTime.getMonth(),
                offsetDateTime.getDayOfMonth(),
                offsetDateTime.getHour(),
                offsetDateTime.getMinute()).toString();
    }
}