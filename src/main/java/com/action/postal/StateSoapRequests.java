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
import org.rmt2.jaxb.StatesCriteriaType;
import org.rmt2.util.HeaderTypeBuilder;

import com.action.contacts.ContactException;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.util.RMT2Money;
import com.api.util.RMT2String2;

/**
 * Help class for constructing and invoking SOAP calls pertaining to
 * State/Province (Region)
 * 
 * @author appdev
 *
 */
public class StateSoapRequests {
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging for country operation";

    /**
     * SOAP call to fetch one or more state/province records.
     * 
     * @param parms
     *            {@link StateCriteria}
     * @return {@link PostalResponse}
     * @throws ContactException
     */
    public static final PostalResponse callGet(StateCriteria parms) throws ContactException {
        // Retrieve one or more State/Province records from the database
        ObjectFactory fact = new ObjectFactory();
        PostalRequest req = fact.createPostalRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_POSTAL)
                .withTransaction(ApiTransactionCodes.REGION_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        PostalCriteria postalCriteria = fact.createPostalRequestPostalCriteria();
        StatesCriteriaType criteria = fact.createStatesCriteriaType();
        if (parms != null) {
            if (RMT2String2.isNotEmpty(parms.getQry_StateId()) && RMT2Money.isNumeric(parms.getQry_StateId())) {
                criteria.setStateId(BigInteger.valueOf(Integer.valueOf(parms.getQry_StateId())));
            }
            if (RMT2String2.isNotEmpty(parms.getQry_CountryId()) && RMT2Money.isNumeric(parms.getQry_CountryId())) {
                criteria.setCountryId(BigInteger.valueOf(Integer.valueOf(parms.getQry_CountryId())));
            }
            if (RMT2String2.isNotEmpty(parms.getQry_StateName())) {
                criteria.setStateName(parms.getQry_StateName());
            }
            if (RMT2String2.isNotEmpty(parms.getQry_StateCode())) {
                criteria.setStateCode(parms.getQry_StateCode());
            }
        }
        postalCriteria.setProvince(criteria);
        req.setPostalCriteria(postalCriteria);
        req.setHeader(head);

        PostalResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new ContactException(StateSoapRequests.MSG, e);
        }
    }


}
