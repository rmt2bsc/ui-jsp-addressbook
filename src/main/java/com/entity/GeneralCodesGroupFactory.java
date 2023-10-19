package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.CodeGroupType;



/**
 * A factory that creates new General Code Group type related instances.
 * 
 * @author roy.terrell
 * 
 */
public class GeneralCodesGroupFactory {

    /**
     * Create a new instance of a GeneralCodesGroup class.
     * 
     * @return {@link GeneralCodesGroup}
     */
    public static GeneralCodesGroup create() {
        try {
            return new GeneralCodesGroup();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a GeneralCodesGroup class.
     * 
     * @param item
     *            {@link CodeGroupType}
     * @return {@link GeneralCodesGroup}
     */
    public static GeneralCodesGroup create(CodeGroupType item) {
        try {
            GeneralCodesGroup obj = GeneralCodesGroupFactory.create();
            obj.setCodeGrpId(item.getGroupId() == null ? 0 : item.getGroupId().intValue());
            obj.setDescription(item.getGroupDesc());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a List of new GeneralCodesGroup instances.
     * 
     * @param items
     *            List of {@link CodeGroupType}
     * @return List<{@link GeneralCodesGroup}>} or null when <I>items</i> is
     *         found to be null or when an error occurs.
     */
    public static List<GeneralCodesGroup> create(List<CodeGroupType> items) {
        if (items == null) {
            return null;
        }

        try {
            List<GeneralCodesGroup> obj = new ArrayList<>();
            for (CodeGroupType item : items) {
                obj.add(GeneralCodesGroupFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
