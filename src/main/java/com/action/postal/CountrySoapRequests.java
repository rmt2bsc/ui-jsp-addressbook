package com.action.postal;

import java.math.BigInteger;
import java.util.Date;

import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.CountryCriteriaType;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.PostalRequest;
import org.rmt2.jaxb.PostalRequest.PostalCriteria;
import org.rmt2.jaxb.PostalResponse;
import org.rmt2.util.HeaderTypeBuilder;

import com.action.contacts.ContactException;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.util.RMT2String2;
import com.entity.Country;

/**
 * Help class for constructing and invoking SOAP calls pertaining to time zone
 * 
 * @author appdev
 *
 */
public class CountrySoapRequests {
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging for country operation";

    /**
     * SOAP call to fetch one or more time zone records.
     * 
     * @param loginId
     *            the id of logged in user
     * @param sessionId
     *            the web session id of the logged in user.
     * @return {@link PostalResponse}
     * @throws ContactException
     * 
     *             UI-37: Added loginId and sessionId parameters to method
     *             signature.
     */
    public static final PostalResponse callGet(Country parms, String loginId, String sessionId) throws ContactException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        PostalRequest req = fact.createPostalRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_POSTAL)
                .withTransaction(ApiTransactionCodes.COUNTRY_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                // UI-37: Added login id and session id to the request
                .withUserId(loginId)
                .withSessionId(sessionId)
                .build();

        PostalCriteria postalCriteria = fact.createPostalRequestPostalCriteria();
        CountryCriteriaType criteria = fact.createCountryCriteriaType();
        if (parms != null) {
            if (parms.getCountryId() > 0) {
                criteria.setCountryId(BigInteger.valueOf(parms.getCountryId()));
            }
            if (RMT2String2.isNotEmpty(parms.getName())) {
                criteria.setCountyName(parms.getName());
            }
        }
        postalCriteria.setCountry(criteria);
        req.setPostalCriteria(postalCriteria);
        req.setHeader(head);

        PostalResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new ContactException(CountrySoapRequests.MSG, e);
        }
    }


}
