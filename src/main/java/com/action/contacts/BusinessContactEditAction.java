package com.action.contacts;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AddressBookResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.Address;
import com.entity.Business;
import com.entity.ContactCriteria;
import com.entity.VwBusinessAddress;
import com.entity.VwBusinessAddressFactory;

/**
 * @author RTerrell
 *
 */
public class BusinessContactEditAction extends AbstractContactEditAction {
    /** Command name for saving changes to contact */
    protected static final String COMMAND_SAVE = "Business.Edit.save";

    /** Command name for deleting contact record */
    protected static final String COMMAND_DELETE = "Business.Edit.delete";

    /** Command name for navigating back to personal contact search page */
    protected static final String COMMAND_BACK = "Business.Edit.back";

    private Logger logger;



    /**
     * Creates an instance of BusinessContactEditAction by initializing the
     * logger.
     * 
     * @throws SystemException
     */
    public BusinessContactEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("BusinessContactEditAction");
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        logger.log(Level.INFO, "Initializing Business Contact Edit Action handler");
    }

    /**
     * Driver for processing the client's Business Contact Search page. The
     * following commands are serviced in this action handler:
     * {@link com.action.contacts.BusinessContactEditAction.COMMAND_NEWSEARCH
     * New Search},
     * {@link com.action.contacts.BusinessContactEditAction.COMMAND_SEARCH
     * Search},
     * {@link com.action.contacts.BusinessContactEditAction.COMMAND_ADD Add
     * Contact}, and
     * {@link com.action.contacts.BusinessContactEditAction.COMMAND_EDIT Edit
     * Contact}.
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Command issued by the client.
     * @Throws SystemException when an error needs to be reported.
     */
    public void processRequest(Request request, Response response, String command) throws ActionCommandException {
        super.processRequest(request, response, command);
        if (command.equalsIgnoreCase(BusinessContactEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(BusinessContactEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(BusinessContactEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Saves the modifications of a business contact which are persisted to the
     * database.
     * <p>
     * After successfully saving the data, the model contact object,
     * {@link com.bean.VwPersonAddress VwPersonAddress} is refreshed from the
     * database so that it may be sent to the client for presentation.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {

        // Call SOAP web service to persist Business contact record changes
        try {
            AddressBookResponse response = BusinessContactSoapRequests.callSave((VwBusinessAddress) this.vwBusinessAddress);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage() + ": " + rst.getExtMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }

            // In the event this is a contact, update the view object's business
            // id for display purposes.
            if (response.getProfile() != null && response.getProfile().getCommonContacts() != null) {
                int contactId = response.getProfile().getCommonContacts().get(0).getContactId().intValue();
                ((VwBusinessAddress) this.vwBusinessAddress).setBusinessId(contactId);
            }
            this.lookupBusServ = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_SERV);
            this.lookupBusType = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_TYPE);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Guarantees that the business contact instance is properly initialized as
     * a instance of {@link com.bean.VwPersonAddress VwPersonAddress} after an
     * update or delete operation.
     * 
     * @throws ActionCommandException
     */
    protected void refreshContact() throws ActionCommandException {
        super.refreshContact();
        // if (this.getContact() == null) {
        // this.setContact(BusinessFactory.createBusinessAddress());
        // }
    }

    /**
     * Build selection criteria for business contact query.
     * 
     * @return RDBMS selection criteria as a String.
     */
    protected String createRefreshCriteria() {
        // int busId = ((Business) this.getContact()).getBusinessId();
        // int addressId = ((Address) this.getAddrObj()).getAddrId();
        StringBuffer criteria = new StringBuffer(100);
        // criteria.append("business_id = " + busId);
        // if (criteria.length() > 0) {
        // criteria.append(" and ");
        // }
        // criteria.append(" addr_id = " + addressId);
        return criteria.toString();
    }

    /**
     * Gathers business contact data from the request.
     * 
     * @throws ActionCommandException
     *             Error obtaining business data.
     */
    protected void receiveClientData() throws ActionCommandException {
        super.receiveClientData();
        try {
            // Retrieve values from the request object into the User object.
            this.vwBusinessAddress = VwBusinessAddressFactory.create();
            RMT2WebUtility.packageBean(this.request, this.vwBusinessAddress);
        } catch (SystemException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Deletes a business contact from the database.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {

        return;
    }

    /**
     * Associates the business contact id with <i>address</i>.
     * 
     * @param address
     *            The address instance to update
     * @param contact
     *            The cotnact that is to be assoicated with the address.
     */
    protected void preAddressUpdate(Address address, Object contact) {
        Business contactObj = null;

        // Associate business id with address if we are adding contact
        if (contact instanceof Business) {
            contactObj = (Business) contact;
        }
        address.setBusinessId(contactObj.getBusinessId());
        return;
    }

    /**
     * Validates a instance of {@link com.api.Contact Contact} as a business
     * contact.
     * 
     * @param address
     *            The address instance to update
     * @param contact
     *            The business cotnact that is to be assoicated with the
     *            address.
     * @param contactId
     *            The id of the contact assoicated with the address.
     * @throws ContactException
     *             if <i>contact</i> is not an instance of
     *             {@link com.bean.Business Business} or is null, or
     *             <i>address</i> is null, or if either one of the id's of
     *             <i>contact</i> or <i>address</i> are invalid, or the business
     *             contact's long name is null.
     */
    protected void validateContact(Object contact, Address address) throws ContactException {
        Business bus = null;
        int intKey;
        String strKey;

        // Cast business object
        if (contact instanceof Business) {
            bus = (Business) contact;
        }

        // // Ensure that business object is valid
        // if (bus == null) {
        // throw new BusinessException("Business contact object is invalid");
        // }
        //
        // // Ensure that address object is valid
        // if (address == null) {
        // throw new
        // BusinessException("Address object of business contact is invalid");
        // }

        // // Ensure that the business id value is set
        // try {
        // strKey = this.request.getParameter("BusinessId");
        // intKey = Integer.valueOf(strKey).intValue();
        // bus.setBusinessId(intKey);
        // }
        // catch (NumberFormatException e) {
        // throw new BusinessException("Business Id is of an invalid type");
        // }
        //
        // // Ensure that the address id value is set
        // try {
        // strKey = this.request.getParameter("AddrId");
        // intKey = Integer.valueOf(strKey).intValue();
        // address.setAddrId(intKey);
        // }
        // catch (NumberFormatException e) {
        // throw new
        // BusinessException("Address Id of the business contact is of an invalid type");
        // }
        //
        // if (bus.getLongname() != null && bus.getLongname().length() > 0 &&
        // RMT2String.spaces(bus.getLongname().length()) != bus.getLongname()) {
        // // continue
        // }
        // else {
        // throw new BusinessException("Business name is required");
        // }
    }

    @Override
    protected void doBack() {
        // Fetch business contacts
        try {
            ContactCriteria criteria = (ContactCriteria) this.query.getCustomObj();
            this.vwBusinessAddress = this.getContacts(criteria);
        } catch (ContactException e) {

        }

        try {
            this.lookupBusServ = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_SERV);
            this.lookupBusType = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_TYPE);

            // Send data to client
            this.sendClientData();
        } catch (ActionCommandException e) {

        }
    }

    @Override
    protected void sendClientData() throws ActionCommandException {
        super.sendClientData();
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.vwBusinessAddress);
    }
}
