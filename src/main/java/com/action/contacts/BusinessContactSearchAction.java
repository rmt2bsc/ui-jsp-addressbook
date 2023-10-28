package com.action.contacts;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.ContactCriteria;
import com.entity.VwBusinessAddress;

/**
 * This action handler provides functionality to service the searching needs of
 * business contact entities.
 * 
 * @author Roy Terrell
 * 
 */
public class BusinessContactSearchAction extends AbstractContactSearchAction implements ICommand {
    /** Command name for new contact search */
    protected static final String COMMAND_NEWSEARCH = "Business.Search.newsearch";

    /** Command name for contact search */
    protected static final String COMMAND_SEARCH = "Business.Search.search";

    /** Command name for add contact */
    protected static final String COMMAND_ADD = "Business.Search.add";

    /** Command name for edit contact */
    protected static final String COMMAND_EDIT = "Business.Search.edit";

    /** Command name for navigating back to home page */
    protected static final String COMMAND_BACK = "Business.Search.back";

    /** Command name for reseting the UI components. */
    protected static final String COMMAND_RESET = "Business.Search.reset";

    private Logger logger;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public BusinessContactSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("BusinessContactSearchAction");
    }

    /**
     * Initializes the personal contact api needed by this api.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        logger.log(Level.INFO, "Initializing Personal Contact Api's");
        // this.api = BusinessFactory.createBusinessApi(this.dbConn,
        // this.request);
    }

    /**
     * Driver for processing the client's Business Contact Search page. The
     * following commands are serviced in this action handler:
     * {@link com.action.contacts.BusinessContactSearchAction.COMMAND_NEWSEARCH
     * New Search},
     * {@link com.action.contacts.BusinessContactSearchAction.COMMAND_SEARCH
     * Search},
     * {@link com.action.contacts.BusinessContactSearchAction.COMMAND_ADD Add
     * Contact}, and
     * {@link com.action.contacts.BusinessContactSearchAction.COMMAND_EDIT Edit
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
        if (command.equalsIgnoreCase(BusinessContactSearchAction.COMMAND_NEWSEARCH)) {
            this.doNewSearch();
        }
        if (command.equalsIgnoreCase(BusinessContactSearchAction.COMMAND_SEARCH)) {
            this.doSearch();
        }
        if (command.equalsIgnoreCase(BusinessContactSearchAction.COMMAND_ADD)) {
            this.addData();
        }
        if (command.equalsIgnoreCase(BusinessContactSearchAction.COMMAND_EDIT)) {
            this.editData();
        }
        if (command.equalsIgnoreCase(BusinessContactSearchAction.COMMAND_BACK)) {
            this.doBack();
        }
        if (command.equalsIgnoreCase(BusinessContactSearchAction.COMMAND_RESET)) {
            this.doReset();
        }
    }

    @Override
    protected void doNewSearch() throws ActionCommandException {
        super.doNewSearch();

        // Get Lookup data
        this.lookupBusServ = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_SERV);
        this.lookupBusType = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_TYPE);
        this.vwBusinessAddress = new ArrayList<VwBusinessAddress>();
        this.sendClientData();
    }

    @Override
    public void doSearch() throws ActionCommandException {
        super.doSearch();

        // Fetch business contacts
        try {
            ContactCriteria criteria = (ContactCriteria) this.query.getCustomObj();
            this.vwBusinessAddress = this.getContacts(criteria);
        } catch (ContactException e) {

        }

        this.lookupBusServ = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_SERV);
        this.lookupBusType = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_TYPE);

        // Send data to client
        this.sendClientData();
    }

    /**
     * Retrieves the selection criteria from the request and returns the
     * selection criteria to the client.
     * 
     * @return {@link UserCriteria}
     * @throws ActionCommandException
     *             problem occurs creating the criteria object.
     */
    protected Object doCustomInitialization() throws ActionCommandException {
        Object criteriaObj = super.doCustomInitialization();
        return criteriaObj;
    }

    /**
     * Creates a new {@link com.bean.VwBusinessAddress VwBusinessAddress}
     * instance used to service the add contact command.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        // super.add();
        // this.contact = BusinessFactory.createBusinessAddress();
        return;
    }

    /**
     * Retrieves a single business contact instance and a lists of entity and
     * service type general codes.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        super.edit();

        this.lookupBusServ = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_SERV);
        this.lookupBusType = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_TYPE);

        // Send data to client
        this.sendClientData();
        return;
    }


    /**
     * Sets the results of the business contact query on the request object in
     * the form of a java object and XML as attributes,
     * {@link com.api.ContactsConst#CLIENT_DATA CLIENT_DATA} and
     * {@link com.constants.RMT2ServletConst.RESPONSE_NONJSP_DATA
     * RESPONSE_NONJSP_DATA}, respectively.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        super.sendClientData();
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.vwBusinessAddress);
    }

}