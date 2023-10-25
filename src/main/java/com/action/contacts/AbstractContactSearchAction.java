package com.action.contacts;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.ContactCriteria;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from the contact search page.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractContactSearchAction extends AbstractActionHandler implements ICommand {
    protected static final String COMMONATTR_CONTACTID = "Id";

    protected static final String COMMONATTR_ADDRID = "AddrId";

    /** Command name for new contact search */
    protected static final String COMMAND_NEWSEARCH = "Contact.Search.newsearch";

    /** Command name for contact search */
    protected static final String COMMAND_SEARCH = "Contact.Search.search";

    /** Command name for add contact */
    protected static final String COMMAND_ADD = "Contact.Search.add";

    /** Command name for edit contact */
    protected static final String COMMAND_EDIT = "Contact.Search.edit";

    /** Contact api */
    // protected Contact api;

    /** The current contact id */
    protected int contactId;

    /** The current address id */
    protected int addressId;

    /** The current contact instance */
    protected Object contact;

    protected Object vwAddress;

    private Logger logger;

    private String contactIdAttr;

    private String addrIdAttr;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AbstractContactSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("AbstractContactSearchAction");
    }

    /**
     * Performs the initialization needed to properly utilize this api.
     * Initializes this object using <i>context</i> and <i>request</i>. Also,
     * the contact id and address id attribute names are set by default as
     * {@link com.action.contacts.AbstractContactSearchAction#COMMONATTR_CONTACTID
     * Contact Id} and
     * {@link com.action.contacts.AbstractContactSearchAction#COMMONATTR_ADDRID
     * Address Id}, respectively. This is needed in the event this object is
     * instantiated using the default constructor.
     * 
     * @param context
     *            the servet context.
     * @param request
     *            the http servlet request.
     * @throws SystemException
     */
    protected void init(Context context, Request request) throws SystemException {
        super.init(context, request);
        logger.log(Level.INFO, "Initializing Common Contact Api's");
        this.setAddrIdAttr(AbstractContactSearchAction.COMMONATTR_ADDRID);
        this.setContactIdAttr(AbstractContactSearchAction.COMMONATTR_CONTACTID);
    }

    /**
     * Driver for processing the client's Contact Search page. The following
     * commands are serviced in this action handler:
     * {@link com.action.contacts.AbstractContactSearchAction.COMMAND_NEWSEARCH
     * New Search},
     * {@link com.action.contacts.AbstractContactSearchAction.COMMAND_SEARCH
     * Search},
     * {@link com.action.contacts.AbstractContactSearchAction.COMMAND_ADD Add
     * Contact}, and
     * {@link com.action.contacts.AbstractContactSearchAction.COMMAND_EDIT Edit
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
        this.query = (RMT2TagQueryBean) this.request.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        return;
    }

    /**
     * Gathers contact input data from the client's search and edit requests and
     * compiles the data into contact selection criteria common to both personal
     * and business contacts.
     * <p>
     * For search requests, the normal route of building search criteria is
     * taken via the <i>buildSearchCriteria</i> method. For edit requests,
     * building search criteria via <i>buildSearchCriteria</i> is bypassed.
     * Instead the client must setup and pass the input parameters, <i>Id</i>
     * and <i>AddrId</i>, for contact id and address id selection criteria,
     * respectively. These values are used for business and personal contact
     * edit requests.
     * 
     * @throws ActionCommandException
     *             When the contact type is not found in the request.
     */
    protected void receiveClientData() throws ActionCommandException {
        // Only build selection criteria if not edit command. This will
        // preserve the state of the user's selection criteria for future
        // queries. Otherwise fetch the key values from the request in order to
        // retrieve contact information from some datasource.
        if (this.command.indexOf(".edit") == -1) {
            this.buildXMLSearchCriteria();
        }
        else {
            String temp = null;
            try {
                temp = this.getInputValue("BusinessId", null);
                this.contactId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                this.msg = "A row must be selected for this operation or the selected record contains an invalid contact id value";
                throw new ActionCommandException(this.msg);
            }
            try {
                temp = this.getInputValue("AddrId", null);
                this.addressId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                this.msg = "A row must be selected for this operation or the selected record contains an invalid address id value";
                throw new ActionCommandException(this.msg);
            }
        }
    }

    /**
     * Sets the results of the contact query to the client via the request
     * object as an attribute named, {@link com.api.ContactsConst#CLIENT_DATA
     * CLIENT_DATA}.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.contact);
    }

    /**
     * Creates an instance of ContactCriteria and attempts to obtain its data
     * from the user's request.
     * 
     * @return {@link UserCriteria}
     * @throws ActionCommandException
     *             problem occurs creating the criteria object.
     */
    protected Object doCustomInitialization() throws ActionCommandException {
        ContactCriteria criteriaObj = ContactCriteria.getInstance();
        if (!this.isFirstTime()) {
            try {
                // Update criteria object with user input.
                RMT2WebUtility.packageBean(this.request, criteriaObj);
            } catch (Exception e) {
                logger.log(Level.ERROR, e.getMessage());
                throw new ActionCommandException(e.getMessage());
            }
        }
        return criteriaObj;
    }

    /**
     * Action handler for responding to the client's request to display the new
     * Contacts Search page.
     * 
     * @throws ActionCommandException
     */
    protected void doNewSearch() throws ActionCommandException {
        this.setFirstTime(true);
        this.receiveClientData();
        this.startSearchConsole();
    }

    /**
     * Verifies that the search criteria exists and is found on the user's
     * session. This logic is used to respond to the "Search" command.
     * 
     * @throws ActionCommandException
     *             When the selection criteria object is null or invalid.
     */
    public void doSearch() throws ActionCommandException {
        this.setFirstTime(false);
        this.receiveClientData();
        this.query = (RMT2TagQueryBean) this.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        if (this.query == null) {
            this.msg = "Contact search failed.  User\'s query object is invalid";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
        if (this.query.getCustomObj() == null) {
            this.msg = "Contact search failed.  User\'s selection criteria object is invalid";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
        if (this.query.getCustomObj() instanceof ContactCriteria) {
            // Valid type
        }
        else {
            this.msg = "Contact search failed.  The data type of the selection criteria object incorrect";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }

        return;
    }

    /**
     * Performs basic functionality for the add operation.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.receiveClientData();
        return;
    }

    /**
     * Queries the database for a single contact using custom selection criteria
     * from the descendant, if available. The custom criteria should be managed
     * by an instance of class, {@link com.bean.criteria.ContactCriteria
     * ContactCriteria}.
     * <p>
     * Upon successfully fetching the contact, the contact data instance will be
     * of one of two types: {@link com.bean.VwPersonAddress VwPersonAddress} or
     * {@link com.bean.VwBusinessAddress VwBusinessAddress} and must be handled
     * accordingly at the descendant. It is of the developer's responsibility to
     * code the logic needed to build custom selection criteria at the
     * descendant via the
     * {@link com.action.contacts.AbstractContactSearchAction#createCotnactCriteria()
     * createCotnactCriteria()} method.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        // No need to contiune if api is not initialized
        // if (this.api == null) {
        // this.msg = "Edit contact failed.  Contact api is not initialized";
        // this.logger.log(Level.ERROR, this.msg);
        // throw new ActionCommandException(this.msg);
        // }
        //
        // // Build custom selection criteria.
        // String criteria = this.createCotnactCriteria();
        // // Query the database in order to create a contact object.
        // try {
        // List<Object> contactList = (List<Object>)
        // this.api.findContact(criteria);
        // if (contactList != null && contactList.size() > 0) {
        // this.contact = contactList.get(0);
        // }
        // }
        // catch (ContactException e) {
        // throw new ActionCommandException(e);
        // }
        return;
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        return;
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    protected void doReset() throws ActionCommandException {
        this.doNewSearch();
        return;
    }

    /**
     * Stub implementation for descendant.
     */
    public void save() throws ActionCommandException {
    }

    /**
     * Stub implementation for descendant.
     */
    public void delete() throws ActionCommandException {
        return;
    }

    /**
     * Override this method to provide custom selection criteria for retrieving
     * a single contact from an external data source.
     * 
     * @return null.
     */
    protected String createCotnactCriteria() {
        return null;
    }

    /**
     * Get the address id obtained from the client.
     * 
     * @return String the addressId
     */
    public int getAddressId() {
        return addressId;
    }

    /**
     * Get the contact id obtained from the client.
     * 
     * @return String the contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Set the attribute name regarding the address id which is used to obtain
     * data from the client.
     * 
     * @param attrName
     *            the name of the address id attribute to set
     */
    public void setAddrIdAttr(String attrName) {
        this.addrIdAttr = attrName;
    }

    /**
     * Set the attribute name regarding the contact id which is used to obtain
     * data from the client.
     * 
     * @param attrName
     *            the name of the contact id attribute to set.
     */
    public void setContactIdAttr(String attrName) {
        this.contactIdAttr = attrName;
    }
}