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
        logger = Logger.getLogger(BusinessContactEditAction.class);
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
            // UI-37: added login id and session id parameters to the callSave
            // method invocation
            AddressBookResponse response = BusinessContactSoapRequests.callSave((VwBusinessAddress) this.vwBusinessAddress,
                    this.loginId,
                    this.session.getId());
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage() + ": " + rst.getExtMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.throwActionError(rst.getMessage(), rst.getExtMessage());
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
     * Deletes a business contact from the database.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // Call SOAP web service to delete Business contact record changes
        try {
            // UI-37: added login id and session id parameters to the callSave
            // method invocation
            AddressBookResponse response = BusinessContactSoapRequests.callDelete((VwBusinessAddress) this.vwBusinessAddress,
                    this.loginId, this.session.getId());
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage() + ": " + rst.getExtMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.throwActionError(rst.getMessage(), rst.getExtMessage());
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

    @Override
    protected void sendClientData() throws ActionCommandException {
        super.sendClientData();
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.vwBusinessAddress);
    }
}
