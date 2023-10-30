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
import com.api.jsp.action.AbstractActionHandler;
import com.api.persistence.db.orm.query.pagination.PaginationQueryResults;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.TimeZone;
import com.entity.TimezoneFactory;
import com.entity.VwZipcode;
import com.entity.VwZipcodeFactory;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from the zip code search page.
 * <p>
 * The following commands are handled:
 * {@link com.action.postal.ZipCodeSearchAction#COMMAND_NEWSEARCH New Search},
 * {@link com.action.postal.ZipCodeSearchAction#COMMAND_SEARCH Search},
 * {@link com.action.postal.ZipCodeSearchAction#COMMAND_LIST List Results},
 * {@link com.action.postal.ZipCodeSearchAction#COMMAND_VIEW View zip code
 * details}, {@link com.action.postal.ZipCodeSearchAction#COMMAND_BACK Navigate
 * to Home page}, and
 * {@link com.action.postal.ZipCodeSearchAction#COMMAND_RESET Page reset}.
 * 
 * @author Roy Terrell
 * 
 */
public class ZipCodeSearchAction extends AbstractActionHandler implements ICommand {

    /** Command name for new zip code search */
    protected static final String COMMAND_NEWSEARCH = "Zipcode.Search.newsearch";

    /** Command name for zip code search */
    protected static final String COMMAND_SEARCH = "Zipcode.Search.search";

    /**
     * Command name for listing multiple zip code records based on selection
     * criteria
     */
    public static final String COMMAND_LIST = "Zipcode.Search.list";

    /** Command name for view zip code record */
    protected static final String COMMAND_VIEW = "Zipcode.Search.view";

    /** Command name for navigating to previous page */
    protected static final String COMMAND_BACK = "Zipcode.Search.back";

    /** Command name for resetting the zip code search page */
    protected static final String COMMAND_RESET = "Zipcode.Search.reset";

    private Object zipList;

    private List<TimeZone> tzList;

    private Object tz;

    private VwZipcode zip;

    private int selectedZipId;

    private ZipcodeCriteria criteria;

    private Logger logger;

    private PaginationQueryResults pageResults;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public ZipCodeSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("ZipCodeSearchAction");
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
        try {
            this.init(null, request);
            this.init();
            this.command = command;
        } catch (SystemException e) {
            throw new ActionCommandException(e);
        }
        this.query = (RMT2TagQueryBean) this.request.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_NEWSEARCH)) {
            this.doNewSearch();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_SEARCH)) {
            this.doSearch();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_LIST)) {
            this.doList();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_VIEW)) {
            this.editData();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_BACK)) {
            this.doBack();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_RESET)) {
            this.doReset();
        }
        return;
    }


    /**
     * Refreshes the selection criteria and list sections of the zip code search
     * page.
     * 
     * @throws ActionCommandException
     */
    protected void doNewSearch() throws ActionCommandException {
        this.setFirstTime(true);
        this.receiveClientData();
        this.startSearchConsole();
        this.zipList = new ArrayList<VwZipcode>();
        this.tzList = this.getLookupData();
        this.sendClientData();
    }

    /**
     * Verifies that the zip code search criteria exists and is found on the
     * user's session. This logic is used to respond to the "Search" command.
     * 
     * @throws ActionCommandException
     *             When the selection criteria object is null or invalid.
     */
    protected void doSearch() throws ActionCommandException {
        this.buildXMLSearchCriteria();
        int pageNo;
        String temp = this.getInputValue("PageNo", null);
        try {
            pageNo = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            pageNo = 0;
        }
        // this.doList(pageNo);
        this.doList();
        return;
    }

    /**
     * Gathers the list of zip code records from the database based on the
     * user's selection criteria. After the data is fetched, the data is stored
     * onto the request in order to be sent to the client for processing.
     * 
     * @throws ActionCommandException
     *             General database errors.
     */
    protected void doList() throws ActionCommandException {
        // Fetch contact information
        try {
            ZipcodeCriteria criteria = (ZipcodeCriteria) this.query.getCustomObj();
            PostalResponse response = ZipcodeSoapRequests.callGet(criteria);

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            this.msg += " (" + rst.getRecordCount() + ")";

            List<VwZipcode> results = null;
            if (response.getZipFull() != null) {
                results = VwZipcodeFactory.create(response.getZipFull());
            }
            else {
                results = new ArrayList<>();
            }
            this.zipList = results;
            this.tzList = this.getLookupData();
            this.sendClientData();
        } catch (ContactException e) {
            throw e;
        }
    }

    /**
     * Retrieves a single {@link com.bean.VwZipcode VwZipcode} instance from the
     * database using the zip code id obtain from
     * {@link com.action.postal.ZipCodeSearchAction#receiveClientData()
     * receiveClientData()} method.
     * 
     * @throws ActionCommandException
     *             When target zip code id is null or contains an invalid value.
     * @deprecated no longer needed since recieveClientData does all the work.
     */
    public void edit() throws ActionCommandException {
        // Fetch contact information
        try {
            PostalResponse response = ZipcodeSoapRequests.callGet(this.criteria);

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            this.msg += " (" + rst.getRecordCount() + ")";

            List<VwZipcode> results = null;
            if (response.getZipFull() != null) {
                results = VwZipcodeFactory.create(response.getZipFull());
            }
            else {
                results = new ArrayList<>();
            }
            this.zip = results.get(0);
            this.tz = this.getTimezone(this.zip.getTimeZone());
            this.sendClientData();
        } catch (ContactException e) {
            throw e;
        }
    }

    /**
     * Retrieves all Timezone records.
     * 
     * @return List<{@link TimeZone}>
     * @throws ActionCommandException
     */
    protected List<TimeZone> getLookupData() throws ActionCommandException {
        // Call SOAP web service to get complete list of codes based on a
        // particular group
        try {
            PostalResponse response = TimezoneSoapRequests.callGet(null);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return null;
            }
            List<TimeZone> results = TimezoneFactory.create(response.getTimezones());
            return results;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    protected TimeZone getTimezone(int timezoneId) throws ActionCommandException {
        // Call SOAP web service to get complete list of codes based on a
        // particular group
        try {
            TimeZone criteria = TimezoneFactory.create();
            criteria.setTimeZoneId(timezoneId);
            PostalResponse response = TimezoneSoapRequests.callGet(criteria);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return null;
            }
            TimeZone results = TimezoneFactory.create(response.getTimezones().get(0));
            return results;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Gathers zip code input data from the client's request.
     * 
     * @throws ActionCommandException
     */
    protected void receiveClientData() throws ActionCommandException {
        String temp = this.getInputValue("ZipId", null);
        this.criteria = ZipcodeCriteria.getInstance();
        criteria.setQry_ZipId(temp);
        try {
            this.selectedZipId = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            this.selectedZipId = 0;
        }
        // if (!this.isFirstTime()) {
        // String rowStr =
        // this.request.getParameter(GeneralConst.CLIENTROW_PROPERTY);
        //
        // // Client must select a row to edit.
        // if (rowStr == null) {
        // logger.log(Level.ERROR,
        // RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED);
        // throw new
        // ActionCommandException(RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED,
        // RMT2SystemExceptionConst.RC_ITEM_NOT_SELECTED);
        // }
        // // Get index of the row that is to be processed from the
        // // HttpServeltRequest object
        // int selectedRow = RMT2Money.stringToNumber(rowStr).intValue();
        //
        // try {
        // // Get zip code data from the selected row.
        // this.zip = VwZipcodeFactory.create();
        // RMT2WebUtility.packageBean(this.request, this.zip, selectedRow);
        // } catch (Exception e) {
        // logger.log(Level.ERROR, e.getMessage());
        // throw new ActionCommandException(e.getMessage());
        // }
        // }
    }

    /**
     * Adds the results of single and list queries of zip codes to the request
     * for the client to process. Single zip code query results are identified
     * on the request as {@link com.constants.GeneralConst#CLIENT_DATA_RECORD
     * CLIENT_DATA_RECORD}. Query results that are a list of zip codes are
     * identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_LIST CLIENT_DATA_LIST}.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.zip);
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.zipList);
        this.request.setAttribute(PostalConst.CLIENT_DATA_TIMEZONES, this.tzList);
        this.request.setAttribute(PostalConst.CLIENT_DATA_TIMEZONE, this.tz);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    /**
     * Creates an instance of ZipcodeCriteria and attempts to obtain its data
     * from the user's request based on the DataSource view,
     * <i>VwZipcodeView</i>.
     * 
     * @return {@link com.bean.criteria.ZipcodeCriteria ZipcodeCriteria}
     * @throws ActionCommandException
     *             problem occurs creating the criteria object.
     */
    protected Object doCustomInitialization() throws ActionCommandException {
        ZipcodeCriteria criteriaObj = ZipcodeCriteria.getInstance();
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
     * Navigates the user back to the home page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        return;
    }

    /**
     * Resets the selection criteria and list sections of the Zipcode Search
     * page.
     * 
     * @throws ActionCommandException
     */
    protected void doReset() throws ActionCommandException {
        this.doNewSearch();
        return;
    }

    /**
     * N/A
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
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

    protected void doList(int pageNo) throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // this.api =
        // AddressComponentsFactory.createZipcodeApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // this.pageResults = null;
        // this.zipList = null;
        // try {
        // String criteria = this.query.getWhereClause();
        // if (pageNo > 0) {
        // this.pageResults = this.api.findZip(criteria, pageNo);
        // if (this.pageResults == null) {
        // this.msg = "Pagination API return invalid results";
        // logger.error(this.msg);
        // throw new ActionCommandException(this.msg);
        // }
        // this.zipList = this.pageResults.getResults();
        // }
        // else {
        // this.zipList = this.api.findZip(criteria);
        // }
        //
        // if (this.zipList == null) {
        // this.zipList = new ArrayList<VwZipcode>();
        // }
        // this.msg = ((List<VwZipcode>) this.zipList).size() + " rows found";
        // this.setError(false);
        // this.sendClientData();
        // } catch (ZipcodeException e) {
        // this.setError(true);
        // this.msg = e.getMessage();
        // this.sendClientData();
        // throw new ActionCommandException(e);
        // } finally {
        // this.api.close();
        // tx.close();
        // this.api = null;
        // tx = null;
        // }
    }

}