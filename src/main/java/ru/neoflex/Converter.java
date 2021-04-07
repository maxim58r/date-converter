package ru.neoflex;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public class Converter {

    // пример корректной даты 2019-12-15T23:59:59
    // пример запроса  "10/20"

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

    public static LocalDateTime convertStringToLocalDateTime(String stringDate) {
        return convertXMLGregorianToLocalDateTime(convertStringToXMLGregorian(stringDate));
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
}