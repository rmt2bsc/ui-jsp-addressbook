package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ZipcodeFullType;



/**
 * A factory that creates new VwZipcode related instances.
 * 
 * @author roy.terrell
 * 
 */
public class VwZipcodeFactory {

    /**
     * Create a new instance of a VwZipcode class.
     * 
     * @return {@link VwZipcode}
     */
    public static VwZipcode create() {
        try {
            return new VwZipcode();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a VwZipcode class.
     * 
     * @param item
     *            {@link ZipcodeFullType}
     * @return {@link VwZipcode}
     */
    public static VwZipcode create(ZipcodeFullType item) {
        try {
            VwZipcode o = VwZipcodeFactory.create();
            o.setZipId(item.getZipId() == null ? 0 : item.getZipId().intValue());
            o.setZip(item.getZipcode() == null ? 0 : item.getZipcode().intValue());
            o.setCity(item.getCity());
            o.setState(item.getState());
            o.setAreaCode(item.getAreaCode());
            o.setCityAliasName(item.getCityAliasName());
            o.setCityAliasAbbr(item.getCityAliasAbbr());
            o.setCountyName(item.getCountyName());
            o.setCountyFips(item.getCountyFips());
            o.setTimeZone(item.getTimeZoneId().getTimezoneId().intValue());
            o.setTimezoneDescr(item.getTimeZoneId().getTimeszoneDesc());
            o.setDayLightSaving(item.getDayLightSaving());
            o.setLatitude(item.getLatitude());
            o.setLongitude(item.getLongitude());
            o.setElevation(item.getElevation());
            o.setMsa(item.getMsa());
            o.setPmsa(item.getPmsa());
            o.setCbsa(item.getCbsa());
            o.setCbsaDiv(item.getCbsaDiv());
            o.setPersonsPerHousehold(item.getPersonsPerHousehold());
            o.setZipcodePopulation(item.getZipcodePopulation());
            o.setCountiesArea(item.getCountiesArea());
            o.setHouseholdsPerZipcode(item.getHouseholdsPerZipcode());
            o.setWhitePopulation(item.getWhitePopulation());
            o.setBlackPopulation(item.getBlackPopulation());
            o.setHispanicPopulation(item.getHispanicPopulation());
            o.setIncomePerHousehold(item.getIncomePerHousehold());
            o.setAverageHouseValue(item.getAverageHouseValue());
            return o;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a List of new VwZipcode instances.
     * 
     * @param items
     *            List of {@link ZipcodeFullType}
     * @return List<{@link VwZipcode}>} or null when <I>items</i> is found to be
     *         null or when an error occurs.
     */
    public static List<VwZipcode> create(List<ZipcodeFullType> items) {
        if (items == null) {
            return null;
        }

        try {
            List<VwZipcode> obj = new ArrayList<>();
            for (ZipcodeFullType item : items) {
                obj.add(VwZipcodeFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
