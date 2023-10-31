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

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from the Country search page. The following commands are
 * handled: {@link com.action.postal.StateSearchAction#COMMAND_LIST List
 * Results}, {@link com.action.postal.StateSearchAction#COMMAND_EDIT Edit
 * country details}, and
 * {@link com.action.postal.StateSearchAction#COMMAND_BACK Navigate to Home
 * page},
 * 
 * @author Roy Terrell
 * 
 */
public class CountrySearchAction extends AbstractActionHandler implements ICommand {
    /**
     * Command name for listing multiple country records based on selection
     * criteria
     */
    public static final String COMMAND_LIST = "Country.Search.list";

    /** Command name for adding a country record */
    protected static final String COMMAND_ADD = "Country.Search.add";

    /** Command name for view country record */
    protected static final String COMMAND_EDIT = "Country.Search.edit";

    /** Command name for navigating to previous page */
    protected static final String COMMAND_BACK = "Country.Search.back";

    private Object list;

    private Country country;

    private Logger logger;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public CountrySearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("CountrySearchAction");
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
        if (command.equalsIgnoreCase(CountrySearchAction.COMMAND_LIST)) {
            this.doList();
        }
        if (command.equalsIgnoreCase(CountrySearchAction.COMMAND_ADD)) {
            this.addData();
        }
        if (command.equalsIgnoreCase(CountrySearchAction.COMMAND_EDIT)) {
            this.editData();
        }
        if (command.equalsIgnoreCase(CountrySearchAction.COMMAND_BACK)) {
            this.doBack();
        }
        return;
    }

    /**
     * Gathers the list of country records from the database based on the user's
     * selection criteria. After the data is fetched, the data is stored onto
     * the request in order to be sent to the client for processing.
     * 
     * @throws ActionCommandException
     *             General database errors.
     */
    public void doList() throws ActionCommandException {
        // Fetch contact information
        try {
            PostalResponse response = CountrySoapRequests.callGet(null);

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            this.msg += " (" + rst.getRecordCount() + ")";

            List<Country> results = null;
            if (response.getCountries() != null) {
                this.list = CountryFactory.create(response.getCountries());
            }
            else {
                this.list = new ArrayList<>();
            }
            this.sendClientData();
        } catch (ContactException e) {
            throw e;
        }
    }

    /**
     * Creates a new {@link com.bean.Country Country} object that will be used
     * for the "Add Country" client presentation.
     * 
     * @throws ActionCommandException
     *             N/A
     */
    public void add() throws ActionCommandException {
        this.country = CountryFactory.create();
        return;
    }

    /**
     * Retrieves a single {@link com.bean.Country Country} instance from the
     * database using the country id obtain from
     * {@link com.action.postal.CountrySearchAction#receiveClientData()
     * receiveClientData()} method.
     * 
     * @throws ActionCommandException
     *             When target zip code id is null or contains an invalid value.
     */
    public void edit() throws ActionCommandException {
        return;
    }

    /**
     * Gathers country input data from the client's request.
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

        try {
            // Get country data from the selected row.
            this.country = CountryFactory.create();
            RMT2WebUtility.packageBean(this.request, this.country, selectedRow);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Stores the results of queries that produces either a single country
     * object or a list of country objects to the request for the client to
     * process. Single country query results are identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_RECORD CLIENT_DATA_RECORD}.
     * A resultset of a List of country are identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_LIST CLIENT_DATA_LIST}.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.country);
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.list);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
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