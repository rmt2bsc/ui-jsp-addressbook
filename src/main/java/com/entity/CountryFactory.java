package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.CountryType;



/**
 * A factory that creates new Country type related instances.
 * 
 * @author roy.terrell
 * 
 */
public class CountryFactory {

    /**
     * Create a new instance of a Country class.
     * 
     * @return {@link Country}
     */
    public static Country create() {
        try {
            return new Country();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a Country class.
     * 
     * @param item
     *            {@link CountryType}
     * @return {@link Country}
     */
    public static Country create(CountryType item) {
        try {
            Country obj = CountryFactory.create();
            obj.setCountryId(item.getCountryId() == null ? 0 : item.getCountryId().intValue());
            obj.setCode(item.getCountryCode());
            obj.setName(item.getCountryName());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a List of new Country instances.
     * 
     * @param items
     *            List of {@link CountryType}
     * @return List<{@link Country}>} or null when <I>items</i> is found to be
     *         null or when an error occurs.
     */
    public static List<Country> create(List<CountryType> items) {
        if (items == null) {
            return null;
        }

        try {
            List<Country> obj = new ArrayList<>();
            for (CountryType item : items) {
                obj.add(CountryFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
