package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.StateType;





/**
 * A factory that creates new VwStateCountry related instances.
 * 
 * @author roy.terrell
 * 
 */
public class VwStateCountryFactory {

    /**
     * Create a new instance of a VwStateCountry class.
     * 
     * @return {@link VwStateCountry}
     */
    public static VwStateCountry create() {
        try {
            return new VwStateCountry();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a VwStateCountry class.
     * 
     * @param item
     *            {@link StateType}
     * @return {@link VwStateCountry}
     */
    public static VwStateCountry create(StateType item) {
        try {
            VwStateCountry o = VwStateCountryFactory.create();
            o.setStateId(item.getStateId() == null ? 0 : item.getStateId().intValue());
            o.setCountryId(item.getCountryId() == null ? 0 : item.getCountryId().intValue());
            o.setStateName(item.getStateName());
            o.setStateCode(item.getStateCode());
            o.setCountryName(item.getCountryName());
            return o;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a List of new VwStateCountry instances.
     * 
     * @param items
     *            List of {@link StateType}
     * @return List<{@link VwStateCountry}>} or null when <I>items</i> is found
     *         to be null or when an error occurs.
     */
    public static List<VwStateCountry> create(List<StateType> items) {
        if (items == null) {
            return null;
        }

        try {
            List<VwStateCountry> obj = new ArrayList<>();
            for (StateType item : items) {
                obj.add(VwStateCountryFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
