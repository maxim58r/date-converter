package ru.neoflex;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public class Converter {

    // пример корректной даты 2019-12-15T23:59:59
    // пример запроса  "10/20"

    public static LocalDateTime convertStringToLocalDateTime(String bankDate) {
        String s = "-01T23:59:59";
        String[] split = bankDate.split("/");
        String str = Optional.of(split)
                .filter(spl -> spl.length == 2)
                .map(strings -> "20" + split[1] + "-" + split[0] + s)
                .orElseThrow(() -> {
                    throw new RuntimeException("Invalid date format");
                });
//        "20" + split[1] + "-" + split[0] + s;
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
