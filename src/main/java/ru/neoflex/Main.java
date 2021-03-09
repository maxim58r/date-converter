package ru.neoflex;

import javax.xml.datatype.DatatypeConfigurationException;

public class Main {
    public static void main(String[] args) throws DatatypeConfigurationException {
        // пример запроса XMLGregorianCalendar 10/20
        String date = "02/21";

        System.out.println(Converter.convertStringToLocalDateTime(date));
        System.out.println(Converter.convertStringToXMLGregorian(date));
        System.out.println(Converter.convertLocalDateTimeToXMLGregorian(Converter.convertStringToLocalDateTime(date)));
        System.out.println(Converter.convertXMLGregorianToLocalDateTime(Converter.convertStringToXMLGregorian(date)));
    }
}
