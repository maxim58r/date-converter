package ru.neoflex;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class Converter {

    // 2019-12-15T23:59:59
    // пример запроса XMLGregorianCalendar 10/20

    public static LocalDateTime convertStringToLocalDateTime(String bankDate) {
        String s = "-01T23:59:59";
        String[] split = bankDate.split("/");
        String str = "20" + split[1] + "-" + split[0] + s;
        LocalDateTime time = LocalDateTime.parse(str);
        return time.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static XMLGregorianCalendar convertStringToXMLGregorian(String bankDate) throws DatatypeConfigurationException {
        LocalDateTime time = convertStringToLocalDateTime(bankDate);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(time.toString());
    }

    public static XMLGregorianCalendar convertLocalDateTimeToXMLGregorian(LocalDateTime localDateTime) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(localDateTime.toString());
    }

    public static LocalDateTime convertXMLGregorianToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        return LocalDateTime.parse(xmlGregorianCalendar.toString());
    }
}