package ru.shaplov.models;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 * @author shaplov
 * @since 19.09.2019
 */
public class LocalDateTimeAdapter extends XmlAdapter<Calendar, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(Calendar calendar) throws Exception {
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public Calendar marshal(LocalDateTime localDateTime) throws Exception {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(zdt.toInstant().toEpochMilli());
        return calendar;
    }
}
