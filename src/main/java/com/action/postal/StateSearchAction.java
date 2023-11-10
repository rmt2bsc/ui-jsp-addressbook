package com.action.postal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.PostalResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.contacts.ContactException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.security.RMT2TagQueryBean;
import com.api.util.RMT2Money;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.Country;
import com.entity.CountryFactory;
import com.entity.VwStateCountry;
import com.entity.VwStateCountryFactory;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from the State/Province search page. The following
 * commands are handled:
 * {@link com.action.postal.StateSearchAction#COMMAND_NEWSEARCH New Search},
 * {@link com.action.postal.StateSearchAction#COMMAND_SEARCH Search},
 * {@link com.action.postal.StateSearchAction#COMMAND_LIST List Results},
 * {@link com.action.postal.StateSearchAction#COMMAND_EDIT Edit state/province
 * details}, {@link com.action.postal.StateSearchAction#COMMAND_BACK Navigate to
 * Home page}, and {@link com.action.postal.StateSearchAction#COMMAND_RESET Page
 * reset}.
 * 
 * @author Roy Terrell
 * 
 */
public class StateSearchAction extends AbstractActionHandler implements ICommand {

    /** Command name for new state/province search */
    protected static final String COMMAND_NEWSEARCH = "State.Search.newsearch";

    /** Command name for state/province search */
    protected static final String COMMAND_SEARCH = "State.Search.search";

    /**
     * Command name for listing multiple state/province records based on
     * selection criteria
     */
    public static final String COMMAND_LIST = "State.Search.list";

    /** Command name for adding a state/province record */
    protected static final String COMMAND_ADD = "State.Search.add";

    /** Command name for view state/province record */
    protected static final String COMMAND_EDIT = "State.Search.edit";

    /** Command name for navigating to previous page */
    protected static final String COMMAND_BACK = "State.Search.back";

    /** Command name for resetting the state/province search page */
    protected static final String COMMAND_RESET = "State.Search.reset";

    private Object list;

    private Object lookup;

    private VwStateCountry state;

    private String stateIdStr;

    private Logger logger;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public StateSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("StateSearchAction");
    }

    /**
     * Performs the initialization needed to properly utilize this handler.
     * Initializes this object using <i>context</i> and <i>request</i>.
     * 
     * @param context
     *            the servet context.
     * @param request
     *            the http servlet request.
     * @throws SystemException
     */
    protected void init(Context context, Request request) throws SystemException {
        super.init(context, request);
    }

    /**
     * Driver for processing the client's zip code Search page.
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
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_NEWSEARCH)) {
            this.doNewSearch();
        }
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_SEARCH)) {
            this.doSearch();
        }
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_LIST)) {
            this.doList();
        }
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_ADD)) {
            this.addData();
        }
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_EDIT)) {
            this.editData();
        }
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_BACK)) {
            this.doBack();
        }
        if (command.equalsIgnoreCase(StateSearchAction.COMMAND_RESET)) {
            this.doReset();
        }
        return;
    }

    /**
     * Creates an instance of StateCriteria and attempts to obtain its data from
     * the user's request based on the DataSource view,
     * <i>VwStateCountryView</i>.
     * 
     * @return {@link com.bean.criteria.StateCriteria StateCriteria}
     * @throws ActionCommandException
     *             problem occurs creating the criteria object.
     */
    protected Object doCustomInitialization() throws ActionCommandException {
        StateCriteria criteriaObj = StateCriteria.getInstance();
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

    protected String doInitialCriteria(RMT2TagQueryBean _query) throws ActionCommandException {
        return "state_id = -1";
    }

    /**
     * Refreshes the selection criteria and list sections of the state/province
     * search page.
     * 
     * @throws ActionCommandException
     */
    protected void doNewSearch() throws ActionCommandException {
        this.setFirstTime(true);
        this.startSearchConsole();
        this.list = new ArrayList<VwStateCountry>();
        this.lookup = this.getLookupData();
        this.sendClientData();
    }

    /**
     * Verifies that the state/province search criteria exists and is found on
     * the user's session. This logic is used to respond to the "Search"
     * command.
     * 
     * @throws ActionCommandException
     *             When the selection criteria object is null or invalid.
     */
    protected void doSearch() throws ActionCommandException {
        this.buildXMLSearchCriteria();
        this.doList();
        return;
    }

    /**
     * Gathers the list of state/province records from the database based on the
     * user's selection criteria. After the data is fetched, the data is stored
     * onto the request in order to be sent to the client for processing.
     * 
     * @throws ActionCommandException
     *             General database errors.
     */
    public void doList() throws ActionCommandException {
        // Fetch state/province information
        try {
            StateCriteria criteria = (StateCriteria) this.query.getCustomObj();

            // UI-37: added login id and session id parameters to the callSave
            // method invocation
            PostalResponse response = StateSoapRequests.callGet(criteria, this.loginId, this.session.getId());

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                return;
            }
            else {
                this.msg += " (" + rst.getRecordCount() + ")";
            }

            List<VwStateCountry> results = null;
            if (response.getStates() != null) {
                results = VwStateCountryFactory.create(response.getStates());
            }
            else {
                results = new ArrayList<>();
            }
            this.list = results;
            this.lookup = this.getLookupData();
            this.sendClientData();
        } catch (ContactException e) {
            throw e;
        }
    }

    /**
     * Retrieves all Country records.
     * 
     * @return List<{@link Country}>
     * @throws ActionCommandException
     */
    private List<Country> getLookupData() throws ActionCommandException {
        try {
            // UI-37: added login id and session id parameters to the callSave
            // method invocation
            PostalResponse response = CountrySoapRequests.callGet(null, this.loginId, this.session.getId());

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return null;
            }

            List<Country> results = null;
            if (response.getCountries() != null) {
                results = CountryFactory.create(response.getCountries());
            }
            else {
                results = new ArrayList<>();
            }
            return results;
        } catch (ContactException e) {
            throw e;
        }
    }

    /**
     * Creates a new {@link com.bean.State State} object that will be used for
     * the "Add State" client presentation.
     * 
     * @throws ActionCommandException
     *             N/A
     */
    public void add() throws ActionCommandException {
        this.state = VwStateCountryFactory.create();
        this.lookup = this.getLookupData();
        this.msg = "A new State instance was created for an Add operation";
        return;
    }

    /**
     * Retrieves a single {@link com.bean.VwStateCountry VwStateCountry}
     * instance from the database using the state/province id obtain from
     * {@link com.action.postal.StateSearchAction#receiveClientData()
     * receiveClientData()} method.
     * 
     * @throws ActionCommandException
     *             When target zip code id is null or contains an invalid value.
     */
    public void edit() throws ActionCommandException {
        this.lookup = this.getLookupData();
    }

    /**
     * Navigates the user back to the Contacts home page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        return;
    }

    /**
     * Resets the selection criteria and list sections of the state/province
     * search page.
     * 
     * @throws ActionCommandException
     */
    protected void doReset() throws ActionCommandException {
        this.doNewSearch();
        return;
    }



    /**
     * Gathers state/province input data from the client's request.
     * 
     * @throws ActionCommandException
     */
    protected void receiveClientData() throws ActionCommandException {
        String rowStr = this.request.getParameter(GeneralConst.CLIENTROW_PROPERTY);

        // Client must select a row to edit.
        if (rowStr == null) {
            logger.log(Level.ERROR, RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED);
            throw new ActionCommandException(RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED,
                    RMT2SystemExceptionConst.RC_ITEM_NOT_SELECTED);
        }
        // Get index of the row that is to be processed from the
        // HttpServeltRequest object
        int selectedRow = RMT2Money.stringToNumber(rowStr).intValue();

        this.state = VwStateCountryFactory.create();
        try {
            // Update criteria object with user input.
            RMT2WebUtility.packageBean(this.request, this.state, selectedRow);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Stores the results of queries that produces either a single
     * state/province object or a list of state/province objects to the request
     * for the client to process. Single state/province query results are
     * identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_RECORD CLIENT_DATA_RECORD}.
     * A resultset of a List of state/provinces are identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_LIST CLIENT_DATA_LIST}.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.state);
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.list);
        this.request.setAttribute(PostalConst.CLIENT_DATA_COUNTRIES, this.lookup);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    /**
     * N/A
     */
    public void save() throws ActionCommandException {
        return;
    }

    /**
     * N/A
     */
    public void delete() throws ActionCommandException {
        return;
    }

}