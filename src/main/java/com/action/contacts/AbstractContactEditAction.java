package com.action.contacts;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.codes.CodeSoapRequests;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.Address;
import com.entity.Business;
import com.entity.GeneralCodes;
import com.entity.GeneralCodesFactory;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from a contact edit page.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractContactEditAction extends AbstractContactAction {
    /** Command name for saving a contact */
    protected static final String COMMAND_SAVE = "Contact.Search.save";

    /** Command name for deleting a contact */
    protected static final String COMMAND_DELETE = "Contact.Search.delete";

    /**
     * Command name for navigating back to the previous page from contact edit
     * page
     */
    protected static final String COMMAND_BACK = "Contact.Search.back";

    /** The current command */
    protected String command;

    private Business contact;

    private Address address;

    private Logger logger;

    private int contactId;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AbstractContactEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("AbstractContactEditAction");
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        logger.log(Level.INFO, "Initializing Common Contact Api's");
    }

    /**
     * Driver for processing the client's Contact Search page. The following
     * commands are serviced in this action handler:
     * {@link com.action.contacts.AbstractContactEditAction.COMMAND_SAVE Save
     * Contact},
     * {@link com.action.contacts.AbstractContactEditAction.COMMAND_DELETE
     * Delete Contact},
     * {@link com.action.contacts.AbstractContactEditAction.COMMAND_BACK
     * Navigate Backwards}.
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
        try {
            this.init(null, request);
            this.init();
            this.command = command;
        } catch (SystemException e) {
            throw new ActionCommandException(e);
        }

        this.query = (RMT2TagQueryBean) this.request.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        if (command.equalsIgnoreCase(AbstractContactEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(AbstractContactEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(AbstractContactEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Contains generic logic that can persist the changes made to a
     * {@link com.api.Contact Contact}. THe update process involves validating
     * the contact and its address, saving the contact, ensuring theat the
     * contact and address are associated, and saving the contact's address.
     * 
     * @throws ActionCommandException
     *             General database errors.
     */
    public void save() throws ActionCommandException {
        // try {
        // this.validateContact(this.contact, this.addrObj);
        // this.api.maintainContact(this.contact);
        // this.preAddressUpdate(this.addrObj, this.contact);
        // this.addrApi.maintainContact(this.addrObj);
        // this.msg = "Contact update completed successfully";
        // }
        // catch (Exception e) {
        // this.msg = "Contact update completed with errors";
        // throw new ActionCommandException(e);
        // }

    }

    /**
     * Queries the database for a single contact using the primary keys of the
     * internal contact and address data members as selection criteria, if
     * available. It is of the developer's responsibility to code the logic
     * needed to build custom selection criteria at the descendent via the
     * {@link com.action.contacts.AbstractContactEditAction#createCotnactCriteria()
     * createCotnactCriteria()} method.
     * <p>
     * For contact modifications it is safe to assume that this refresh logic
     * will guarantee that the contact will be retrieved from the database as a
     * confirmation. For situations when deleting a contact, the confirmation is
     * dependent on the contact data stored internally which was initially used
     * to display the page before invoking the delete operation.
     * 
     * @throws ActionCommandException
     *             When the contact api is invalid or general database error
     *             when attempting to retrieve the contact.
     */
    protected void refreshContact() throws ActionCommandException {
        // No need to contiune if api is not initialized
        // if (this.api == null) {
        // this.msg =
        // "Refresh of contact failed.  Contact api is not initialized";
        // this.logger.log(Level.ERROR, this.msg);
        // throw new ActionCommandException(this.msg);
        // }
        //
        // // Get custom selection criteria.
        // String criteria = this.createRefreshCriteria();
        //
        // // Query the database in order to create a contact object.
        // try {
        // List<Object> contactList = (List<Object>)
        // this.api.findContact(criteria);
        // if (contactList != null && contactList.size() > 0) {
        // this.contact = contactList.get(0);
        // }
        // else {
        // this.contact = null;
        // }
        // }
        // catch (ContactException e) {
        // throw new ActionCommandException(e);
        // }
    }

    /**
     * Override this method to provide custom selection criteria for retrieving
     * a single contact from an external data source.
     * 
     * @return null.
     */
    protected String createRefreshCriteria() {
        return null;
    }

    /**
     * Gathers contact's address data from the request.
     * 
     * @throws ActionCommandException
     *             When the cotnact's address id is invalid or cannot be
     *             verified.
     */
    protected void receiveClientData() throws ActionCommandException {

        return;
    }

    /**
     * Sets the results of the personal conact update on the request object in
     * the form of a java object and XML as attributes,
     * {@link com.api.ContactsConst#CLIENT_DATA CLIENT_DATA} and
     * {@link com.constants.RMT2ServletConst.RESPONSE_NONJSP_DATA
     * RESPONSE_NONJSP_DATA}, respectively.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        super.sendClientData();
        this.request.setAttribute(ContactsConst.CLIENT_DATA, this.contact);
        this.request.setAttribute(ContactsConst.CLIENT_DATA_ADDRESS, this.address);
    }


    /**
     * Stub implementation for responding to the <i>back</i> request.
     *
     */
    protected void doBack() {
        return;
    }

    /**
     * Get target address object.
     * 
     * @return the addrObj
     */
    public Address getAddrObj() {
        return address;
    }

    /**
     * Set the target address object.
     * 
     * @param addrObj
     *            the addrObj to set
     */
    public void setAddrObj(Address addrObj) {
        this.address = addrObj;
    }

    /**
     * Validate the contact and its address.
     * 
     * @param contact
     *            The contact.
     * @param address
     *            The contact's address.
     * @throws ContactException
     *             validation error.
     */
    protected abstract void validateContact(Object contact, Address address) throws ContactException;

    /**
     * This method is triggered just before the address instance is updated.
     * 
     * @param address
     *            The address instance to update
     * @param contact
     *            The contact that is to be associated with the address.
     */
    protected abstract void preAddressUpdate(Address address, Object contact);

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        return;
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        return;
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        return;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Sets the target contact.
     * 
     * @param contact
     *            the contact to set
     */
    public void setContact(Business contact) {
        this.contact = contact;
    }

    /**
     * Get the target contact.
     * 
     * @return An arbitrary object representing the contact.
     */
    public Business getContact() {
        return this.contact;
    }

    protected List<GeneralCodes> getLookupData(int codeGroupId) throws ActionCommandException {
        GeneralCodes code = GeneralCodesFactory.create();
        code.setCodeGrpId(codeGroupId);

        // Call SOAP web service to get complete list of codes based on a
        // particular group
        try {
            LookupCodesResponse response = CodeSoapRequests.callGet(code);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return null;
            }
            List<GeneralCodes> results = GeneralCodesFactory.create(response.getDetailCodes());
            return results;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }
}