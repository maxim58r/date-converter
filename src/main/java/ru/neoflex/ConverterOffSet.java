package ru.neoflex;

import lombok.SneakyThrows;
import lombok.Value;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ConverterOffSet {

    //    @Value("${offset.minutes}")
    private static int offset = 180;
    private final static int DEFAULT_OFFSET_FOR_GMT = 0;
    private final static Locale DEFAULT_LOCALE = null;
    private final static XMLGregorianCalendar DEFAULT_CALENDAR = null;

    public static XMLGregorianCalendar toCalendar(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
                .map(offsetDateTime1 -> {
                    try {
                        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(offsetDateTime1.toInstant(), offsetDateTime1.getOffset());
                        return DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(zonedDateTime));
                    } catch (DatatypeConfigurationException ex) {
                        throw new IllegalArgumentException(MessageFormat.format("Wrong value given to method toCalendar: {0}", offsetDateTime1));
                    }
                })
                .orElse(null);
        /*
        if (offsetDateTime == null) {
            return null;
        }
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(offsetDateTime.toInstant(), offsetDateTime.getOffset());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(zonedDateTime));
        } catch (DatatypeConfigurationException ex) {
            throw new IllegalArgumentException(MessageFormat.format("Wrong value given to method toCalendar: {0}", offsetDateTime));
        }
         */
    }

    public static OffsetDateTime toOffsetDateTime(XMLGregorianCalendar calendar) {
        return Optional.ofNullable(calendar)
                .map(calendar1 -> {
                    ZonedDateTime zonedDateTime = calendar1
                            .toGregorianCalendar(getCalendarTimeZone(calendar1), DEFAULT_LOCALE, DEFAULT_CALENDAR)
                            .toZonedDateTime();
                    return OffsetDateTime.of(zonedDateTime.toLocalDateTime(), zonedDateTime.getOffset());
                })
                .orElse(null);
        /*
        if (calendar == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = calendar
                .toGregorianCalendar(getCalendarTimeZone(calendar), null, null)
                .toZonedDateTime();
        return OffsetDateTime.of(zonedDateTime.toLocalDateTime(), zonedDateTime.getOffset());
         */
    }

    public static long getOffset(XMLGregorianCalendar calendar) {
        int timeZoneOffsetInMinutes = calendar.getTimezone();
        if (timeZoneOffsetInMinutes == DatatypeConstants.FIELD_UNDEFINED) {
            return 0;
        } else {
            return timeZoneOffsetInMinutes * TimeUnit.MINUTES.toSeconds(1);
        }
    }

    public static long getOffset(OffsetDateTime offsetDateTime) {
        return offsetDateTime.getOffset().getTotalSeconds();
    }

    private static TimeZone getCalendarTimeZone(XMLGregorianCalendar calendar) {
        return calendar.getTimeZone(DEFAULT_OFFSET_FOR_GMT);
    }

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

    public static OffsetDateTime convertXMLGregorianToOffsetDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        var localDateTime = convertXMLGregorianToLocalDateTime(xmlGregorianCalendar);
        return OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(offset));
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

    public static String convertOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
        return LocalDateTime.of(
                offsetDateTime.getYear(),
                offsetDateTime.getMonth(),
                offsetDateTime.getDayOfMonth(),
                offsetDateTime.getHour(),
                offsetDateTime.getMinute()).toString();
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
}