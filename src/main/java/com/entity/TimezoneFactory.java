package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.TimezoneType;





/**
 * A factory that creates new TimeZone related instances.
 * 
 * @author roy.terrell
 * 
 */
public class TimezoneFactory {

    /**
     * Create a new instance of a TimeZone class.
     * 
     * @return {@link TimeZone}
     */
    public static TimeZone create() {
        try {
            return new TimeZone();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a TimeZone class.
     * 
     * @param item
     *            {@link TimezoneType}
     * @return {@link TimeZone}
     */
    public static TimeZone create(TimezoneType item) {
        try {
            TimeZone o = TimezoneFactory.create();
            o.setTimeZoneId(item.getTimezoneId() == null ? 0 : item.getTimezoneId().intValue());
            o.setDescr(item.getTimeszoneDesc());
            return o;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a List of new TimeZone instances.
     * 
     * @param items
     *            List of {@link TimezoneType}
     * @return List<{@link TimeZone}>} or null when <I>items</i> is found to be
     *         null or when an error occurs.
     */
    public static List<TimeZone> create(List<TimezoneType> items) {
        if (items == null) {
            return null;
        }

        try {
            List<TimeZone> obj = new ArrayList<>();
            for (TimezoneType item : items) {
                obj.add(TimezoneFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
