package com.action.codes;

import java.math.BigInteger;
import java.util.Date;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.CodeDetailType;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.LookupCodeCriteriaType;
import org.rmt2.jaxb.LookupCodesRequest;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.util.HeaderTypeBuilder;

import com.AddressbookUIException;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.entity.GeneralCodes;
import com.entity.GeneralCodesGroup;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the
 * Addressbook code groups.
 * 
 * @author Roy Terrell
 *
 */
public class CodeSoapRequests {
    private static final Logger logger = Logger.getLogger(CodeSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch all code groups.
     * 
     * @param parms
     *            {@link GeneralCodes}
     * @return {@link LookupCodesResponse}
     * @throws AddressbookUIException
     */
    public static final LookupCodesResponse callGet(GeneralCodes parms) throws AddressbookUIException {
        // Retrieve all code group records from the database
        ObjectFactory fact = new ObjectFactory();
        LookupCodesRequest req = fact.createLookupCodesRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_LOOKUP)
                .withTransaction(ApiTransactionCodes.LOOKUP_CODE_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        LookupCodeCriteriaType criteria = fact.createLookupCodeCriteriaType();
        criteria.setAll(true);
        if (parms != null) {
            if (parms.getCodeId() > 0) {
                criteria.setCode(BigInteger.valueOf(parms.getCodeId()));
            }
            if (parms.getCodeGrpId() > 0) {
                criteria.setGroup(BigInteger.valueOf(parms.getCodeGrpId()));
            }
        }
        req.setCriteria(criteria);
        req.setHeader(head);

        LookupCodesResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(CodeSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to save general code data.
     * 
     * @param data
     *            {@link GeneralCodes}
     * @return {@link LookupCodesResponse}
     * @throws AddressbookUIException
     */
    public static final LookupCodesResponse callSave(GeneralCodes data) throws AddressbookUIException {
        // Persist general code record changes to the database
        ObjectFactory fact = new ObjectFactory();
        LookupCodesRequest req = fact.createLookupCodesRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_LOOKUP)
                .withTransaction(ApiTransactionCodes.LOOKUP_CODE_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        CodeDetailType cdt = fact.createCodeDetailType();
        cdt.setCodeId(BigInteger.valueOf(data.getCodeId()));
        cdt.setGroupId(BigInteger.valueOf(data.getCodeGrpId()));
        cdt.setLongdesc(data.getLongdesc());
        cdt.setShortdesc(data.getShortdesc());

        req.getDetailCodes().add(cdt);
        req.setHeader(head);

        LookupCodesResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(CodeSoapRequests.MSG, e);
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
            throw new AuthenticationException(CodeSoapRequests.MSG, e);
        }
    }
}
