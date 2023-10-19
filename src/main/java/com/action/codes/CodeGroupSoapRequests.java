package com.action.codes;

import java.math.BigInteger;
import java.util.Date;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.CodeGroupType;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.LookupCodeCriteriaType;
import org.rmt2.jaxb.LookupCodesRequest;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.util.HeaderTypeBuilder;

import com.AddressbookUIException;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.entity.GeneralCodesGroup;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the
 * Addressbook code groups.
 * 
 * @author Roy Terrell
 *
 */
public class CodeGroupSoapRequests {
    private static final Logger logger = Logger.getLogger(CodeGroupSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch all code groups.
     * 
     * @return {@link LookupCodesResponse}
     * @throws AddressbookUIException
     */
    public static final LookupCodesResponse callGet() throws AddressbookUIException {
        // Retrieve all code group records from the database
        ObjectFactory fact = new ObjectFactory();
        LookupCodesRequest req = fact.createLookupCodesRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_LOOKUP)
                .withTransaction(ApiTransactionCodes.LOOKUP_GROUP_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        LookupCodeCriteriaType criteria = fact.createLookupCodeCriteriaType();
        req.setCriteria(criteria);
        req.setHeader(head);

        LookupCodesResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(CodeGroupSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to save general code group data.
     * 
     * @param data
     *            {@link GeneralCodesGroup}
     * @return {@link LookupCodesResponse}
     * @throws AddressbookUIException
     */
    public static final LookupCodesResponse callSave(GeneralCodesGroup data) throws AddressbookUIException {
        // Retrieve all code group records from the database
        ObjectFactory fact = new ObjectFactory();
        LookupCodesRequest req = fact.createLookupCodesRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_LOOKUP)
                .withTransaction(ApiTransactionCodes.LOOKUP_GROUP_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        CodeGroupType cgt = fact.createCodeGroupType();
        cgt.setGroupId(BigInteger.valueOf(data.getCodeGrpId()));
        cgt.setGroupDesc(data.getDescription());
        req.getGroupCodes().add(cgt);
        req.setHeader(head);

        LookupCodesResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(CodeGroupSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to save general code group data.
     * 
     * @param data
     *            {@link GeneralCodesGroup}
     * @return {@link LookupCodesResponse}
     * @throws AddressbookUIException
     */
    public static final LookupCodesResponse callDelete(GeneralCodesGroup data) throws AddressbookUIException {
        // Delete a code group record from the database
        ObjectFactory fact = new ObjectFactory();
        LookupCodesRequest req = fact.createLookupCodesRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_LOOKUP)
                .withTransaction(ApiTransactionCodes.LOOKUP_GROUP_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        LookupCodeCriteriaType criteria = fact.createLookupCodeCriteriaType();
        criteria.setGroup(BigInteger.valueOf(data.getCodeGrpId()));
        req.setCriteria(criteria);
        req.setHeader(head);

        LookupCodesResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(CodeGroupSoapRequests.MSG, e);
        }
    }
}
