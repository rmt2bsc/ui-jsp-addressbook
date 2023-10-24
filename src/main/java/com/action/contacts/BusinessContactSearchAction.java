package com.action.contacts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AddressBookResponse;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.codes.CodeSoapRequests;
import com.api.constants.GeneralConst;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.ContactCriteria;
import com.entity.GeneralCodes;
import com.entity.GeneralCodesFactory;
import com.entity.VwBusinessAddress;
import com.entity.VwBusinessAddressFactory;

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

    private Object lookupBusType;

    private Object lookupBusServ;

    private Object data;

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
        this.vwAddress = new ArrayList<VwBusinessAddress>();
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
     * Ensures that the business contact instance is properly initialized in the
     * event that the contact is not available from the database.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        return;
    }

    // /**
    // * Build selection criteria for business contact query.
    // *
    // * @return Selection criteria as a String.
    // */
    // protected String createCotnactCriteria() {
    // // Build selection criteria for person-contact query.
    // StringBuffer criteria = new StringBuffer(100);
    // if (this.getContactId() > 0) {
    // criteria.append("business_id = " + this.getContactId());
    // }
    // if (this.getAddressId() != null) {
    // if (criteria.length() > 0) {
    // criteria.append(" and ");
    // }
    // criteria.append(" addr_id = " + this.getAddressId());
    // }
    //
    // // TODO: do not delete right now...
    // // if (this.getContactId() != null) {
    // // criteria.append("business_id = " + this.getContactId());
    // // }
    // // if (this.getAddressId() != null) {
    // // if (criteria.length() > 0) {
    // // criteria.append(" and ");
    // // }
    // // criteria.append(" addr_id = " + this.getAddressId());
    // // }
    // return criteria.toString();
    // }

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
        this.request.setAttribute(ContactsConst.CLIENT_DATA_BUSTYPE, this.lookupBusType);
        this.request.setAttribute(ContactsConst.CLIENT_DATA_SERVTYPE, this.lookupBusServ);
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.vwAddress);
    }

    private List<GeneralCodes> getLookupData(int codeGroupId) throws ActionCommandException {
        GeneralCodes code = GeneralCodesFactory.create();
        code.setCodeGrpId(codeGroupId);

        // Call SOAP web service to get complete list of codes based on a
        // particular group
        try {
            LookupCodesResponse response = CodeSoapRequests.callGet(code);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
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

    @Override
    public void doSearch() throws ActionCommandException {
        super.doSearch();

        // Fetch business contacts
        try {
            ContactCriteria criteria = (ContactCriteria) this.query.getCustomObj();
            AddressBookResponse response = BusinessContactSoapRequests.callGet(criteria);

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();

            List<VwBusinessAddress> results = VwBusinessAddressFactory.create(response.getProfile().getBusinessContacts());
            this.vwAddress = results;
        } catch (ContactException e) {

        }

        this.lookupBusServ = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_SERV);
        this.lookupBusType = this.getLookupData(ContactsConst.CODEGROUP_KEY_BUS_TYPE);

        // Send data to client
        this.sendClientData();
    }

}