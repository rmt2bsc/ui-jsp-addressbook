package com.action.contacts;

import java.math.BigInteger;
import java.util.Date;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.AddressBookRequest;
import org.rmt2.jaxb.AddressBookResponse;
import org.rmt2.jaxb.AddressType;
import org.rmt2.jaxb.BusinessContactCriteria;
import org.rmt2.jaxb.BusinessType;
import org.rmt2.jaxb.CodeDetailType;
import org.rmt2.jaxb.ContactCriteriaGroup;
import org.rmt2.jaxb.ContactDetailGroup;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.ZipcodeType;
import org.rmt2.util.HeaderTypeBuilder;

import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.util.RMT2Money;
import com.api.util.assistants.Verifier;
import com.api.util.assistants.VerifyException;
import com.entity.ContactCriteria;
import com.entity.VwBusinessAddress;

/**
 * Help class for constructing and invoking SOAP calls pertaining to business
 * contacts
 * 
 * @author appdev
 *
 */
public class BusinessContactSoapRequests {
    private static final Logger logger = Logger.getLogger(BusinessContactSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging for business contact operation";

    /**
     * SOAP call to fetch Business contacts.
     * 
     * @return {@link AddressBookResponse}
     * @throws ContactException
     */
    public static final AddressBookResponse callGet(ContactCriteria parms) throws ContactException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        AddressBookRequest req = fact.createAddressBookRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_PROFILE)
                .withTransaction(ApiTransactionCodes.CONTACTS_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        BusinessContactCriteria criteria = BusinessContactSoapRequests.buildPaylodCriteriaSearchCriteria(parms);

        ContactCriteriaGroup ccg = fact.createContactCriteriaGroup();
        ccg.setBusinessCriteria(criteria);
        req.setCriteria(ccg);
        req.setHeader(head);

        AddressBookResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new ContactException(BusinessContactSoapRequests.MSG, e);
        }
    }


    private static final BusinessContactCriteria buildPaylodCriteriaSearchCriteria(ContactCriteria reqCriteria) {
        ObjectFactory fact = new ObjectFactory();
        BusinessContactCriteria jaxbCriteria = fact.createBusinessContactCriteria();

        // Get Contact ID
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_ContactId());
            if (RMT2Money.isNumeric(reqCriteria.getQry_ContactId())) {
                jaxbCriteria.setContactId(BigInteger.valueOf(Integer.valueOf(reqCriteria.getQry_ContactId()).intValue()));
            }
        } catch (VerifyException e) {

        }

        // Get Business ID
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusinessId());
            if (RMT2Money.isNumeric(reqCriteria.getQry_BusinessId())) {
                jaxbCriteria.setContactId(BigInteger.valueOf(Integer.valueOf(reqCriteria.getQry_BusinessId()).intValue()));
            }
        } catch (VerifyException e) {

        }

        // Get Business Name
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusLongname());
            jaxbCriteria.setBusinessName(reqCriteria.getQry_BusLongname());
        } catch (VerifyException e) {

        }

        // Get Service type
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusServTypeId());
            jaxbCriteria.setServiceType(BigInteger.valueOf(Integer.valueOf(reqCriteria.getQry_BusServTypeId()).intValue()));
        } catch (VerifyException e) {

        }

        // Get Entity type
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusEntityTypeId());
            jaxbCriteria.setEntityType(BigInteger.valueOf(Integer.valueOf(reqCriteria.getQry_BusEntityTypeId()).intValue()));
        } catch (VerifyException e) {

        }

        // Get Main Phone
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusContactPhone());
            jaxbCriteria.setMainPhone(reqCriteria.getQry_BusContactPhone());
        } catch (VerifyException e) {

        }

        // Get Tax Id
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusTaxId());
            jaxbCriteria.setTaxId(reqCriteria.getQry_BusTaxId());
        } catch (VerifyException e) {

        }

        // Get Contact Firstname
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusContactFirstname());
            jaxbCriteria.setContactFname(reqCriteria.getQry_BusContactFirstname());
        } catch (VerifyException e) {

        }

        // Get Contact Lastname
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusContactLastname());
            jaxbCriteria.setContactLname(reqCriteria.getQry_BusContactLastname());
        } catch (VerifyException e) {

        }

        // Get Contact Email
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BusContactEmail());
            jaxbCriteria.setContactEmail(reqCriteria.getQry_BusContactEmail());
        } catch (VerifyException e) {

        }
        
        // Get City
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_ZipCity());
            jaxbCriteria.setCity(reqCriteria.getQry_ZipCity());
        } catch (VerifyException e) {

        }

        // Get State
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_ZipState());
            jaxbCriteria.setState(reqCriteria.getQry_ZipState());
        } catch (VerifyException e) {

        }

        // Get Zip
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_AddrZip());
            jaxbCriteria.setZipcode(reqCriteria.getQry_AddrZip());
        } catch (VerifyException e) {

        }
        return jaxbCriteria;
    }


    /**
     * SOAP call to save Business contact data changes.
     * 
     * @param data
     *            {@link VwBusinessAddress}
     * @return {@link AddressBookResponse}
     * @throws ContactException
     */
    public static final AddressBookResponse callSave(VwBusinessAddress data) throws ContactException {
        ObjectFactory fact = new ObjectFactory();
        AddressBookRequest req = fact.createAddressBookRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_ADDRESSBOOK)
                .withModule(ApiTransactionCodes.MODULE_ADDRESSBOOK_PROFILE)
                .withTransaction(ApiTransactionCodes.CONTACTS_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_ADDRESSBOOK)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        ContactDetailGroup cdg = fact.createContactDetailGroup();

        CodeDetailType cdtEntityType = fact.createCodeDetailType();
        cdtEntityType.setCodeId(BigInteger.valueOf(data.getBusEntityTypeId()));
        
        CodeDetailType cdtServType = fact.createCodeDetailType();
        cdtServType.setCodeId(BigInteger.valueOf(data.getBusServTypeId()));
        
        BusinessType obj = fact.createBusinessType();
        obj.setBusinessId(BigInteger.valueOf(data.getBusinessId()));
        obj.setLongName(data.getBusLongname());
        obj.setShortName(data.getBusShortname());
        obj.setContactFirstname(data.getBusContactFirstname());
        obj.setContactLastname(data.getBusContactLastname());
        obj.setContactEmail(data.getContactEmail());
        obj.setContactPhone(data.getBusContactPhone());
        obj.setContactExt(data.getBusContactExt());
        obj.setTaxId(data.getBusTaxId());
        obj.setEntityType(cdtEntityType);
        obj.setServiceType(cdtServType);
        obj.setWebsite(data.getBusWebsite());

        AddressType addr = fact.createAddressType();
        addr.setAddrId(BigInteger.valueOf(data.getAddrId()));
        addr.setAddr1(data.getAddr1());
        addr.setAddr2(data.getAddr2());
        addr.setAddr3(data.getAddr3());
        addr.setAddr4(data.getAddr4());
        addr.setPhoneMain(data.getAddrPhoneMain());
        addr.setPhoneFax(data.getAddrPhoneFax());

        ZipcodeType zip = fact.createZipcodeType();
        zip.setCity(data.getZipCity());
        zip.setState(data.getZipState());
        zip.setZipcode(BigInteger.valueOf(data.getAddrZip()));

        addr.setZip(zip);
        obj.setAddress(addr);

        cdg.getBusinessContacts().add(obj);

        req.setProfile(cdg);
        req.setHeader(head);

        AddressBookResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new ContactException(BusinessContactSoapRequests.MSG, e);
        }
    }

}
