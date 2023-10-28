package com.action.contacts;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.ContactCriteria;
import com.entity.VwBusinessAddress;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from the contact search page.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractContactSearchAction extends AbstractContactAction {
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

    private Logger logger;

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
     *            the servlet context.
     * @param request
     *            the http servlet request.
     * @throws SystemException
     */
    protected void init(Context context, Request request) throws SystemException {
        super.init(context, request);
        logger.log(Level.INFO, "Initializing Common Contact Api's");
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
                temp = this.getInputValue("ContactId", null);
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
        super.sendClientData();
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.vwBusinessAddress);
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
     * Retrieves a single business contact using custom selection criteria from
     * the descendant, if available.
     * <p>
     * The custom criteria should be managed by an instance of class,
     * {@link com.bean.criteria.ContactCriteria ContactCriteria}.
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
        // Create a separate instance of ContactCriteria so to not interfere
        // with the instance that exists in the session.
        ContactCriteria criteria = ContactCriteria.getInstance();
        criteria.setQry_ContactId(String.valueOf(this.contactId));

        // Fetch business contacts
        try {
            List<VwBusinessAddress> results = this.getContacts(criteria);
            if (results != null && results.size() == 1) {
                this.vwBusinessAddress = results.get(0);
            }
            else {
                String errMsg = "The edit business contact operation's result set is either null or too many business contacts were returned";
                throw new ActionCommandException(errMsg);
            }
        } catch (ContactException e) {
            throw new ActionCommandException(e);
        }
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

}