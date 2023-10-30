package com.action.postal;

import java.math.BigInteger;
import java.util.Date;

import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.PostalRequest;
import org.rmt2.jaxb.PostalRequest.PostalCriteria;
import org.rmt2.jaxb.PostalResponse;
import org.rmt2.jaxb.ZipResultFormatType;
import org.rmt2.jaxb.ZipcodeCriteriaType;
import org.rmt2.util.HeaderTypeBuilder;

import com.action.contacts.ContactException;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.util.RMT2Money;
import com.api.util.RMT2String2;

/**
 * Help class for constructing and invoking SOAP calls pertaining to zip codes
 * 
 * @author appdev
 *
 */
public class ZipcodeSoapRequests {
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging for zip code operation";

    /**
     * SOAP call to fetch one or more zip code records.
     * 
     * @param parms
     *            {@link ZipcodeCriteria}
     * @return {@link PostalResponse}
     * @throws ContactException
     */
    public static final PostalResponse callGet(ZipcodeCriteria parms) throws ContactException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        PostalRequest req = fact.createPostalRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_POSTAL)
                .withTransaction(ApiTransactionCodes.ZIPCODE_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        PostalCriteria postalCriteria = fact.createPostalRequestPostalCriteria();
        ZipcodeCriteriaType criteria = fact.createZipcodeCriteriaType();
        if (parms != null) {
            if (RMT2String2.isNotEmpty(parms.getQry_ZipId()) && RMT2Money.isNumeric(parms.getQry_ZipId())) {
                criteria.setZipId(BigInteger.valueOf((Integer.valueOf(parms.getQry_ZipId()))));
            }
            if (RMT2String2.isNotEmpty(parms.getQry_Zip()) && RMT2Money.isNumeric(parms.getQry_Zip())) {
                criteria.setZipcode(BigInteger.valueOf((Integer.valueOf(parms.getQry_Zip()))));
            }
            if (RMT2String2.isNotEmpty(parms.getQry_City())) {
                criteria.setCity(parms.getQry_City());
            }
            if (RMT2String2.isNotEmpty(parms.getQry_AreaCode())) {
                criteria.setAreaCode(parms.getQry_AreaCode());
            }
            if (RMT2String2.isNotEmpty(parms.getQry_State())) {
                criteria.setState(parms.getQry_State());
            }
            if (RMT2String2.isNotEmpty(parms.getQry_CountyName())) {
                criteria.setCountyName(parms.getQry_CountyName());
            }
            if (RMT2String2.isNotEmpty(parms.getQry_TimeZone())) {
                criteria.setTimezone(BigInteger.valueOf(Integer.valueOf(parms.getQry_TimeZone())));
            }
        }
        criteria.setResultFormat(ZipResultFormatType.FULL);
        postalCriteria.setZipcode(criteria);
        req.setPostalCriteria(postalCriteria);
        req.setHeader(head);

        PostalResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new ContactException(ZipcodeSoapRequests.MSG, e);
        }
    }


}
