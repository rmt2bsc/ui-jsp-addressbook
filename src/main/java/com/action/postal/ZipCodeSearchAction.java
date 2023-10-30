package com.action.postal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.PostalResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
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

    /** Command name for fetch single zip code record */
    protected static final String COMMAND_FETCHZIPCODE = "Zipcode.Search.fetchzip";

    /** Command name for fetch all timezone records */
    protected static final String COMMAND_FETCHTIMEZONES = "Zipcode.Search.fetchtimezones";

    /** Command name for navigating to previous page */
    protected static final String COMMAND_BACK = "Zipcode.Search.back";

    /** Command name for resetting the zip code search page */
    protected static final String COMMAND_RESET = "Zipcode.Search.reset";

    // private ZipcodeApi api;

    private Object zipList;

    private List<TimeZone> tzList;

    private VwZipcode zip;

    // private String zipIdStr;
    //
    // private String zipCodeStr;

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
        // if
        // (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_FETCHZIPCODE))
        // {
        // this.editZipcodeByCode();
        // }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_BACK)) {
            this.doBack();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_RESET)) {
            this.doReset();
        }
        if (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_FETCHTIMEZONES)) {
            this.doFetchTimezones();
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
        this.doList(pageNo);
        return;
    }

    /**
     * Retrieves a single {@link com.bean.VwZipcode VwZipcode} instance from the
     * database using the zip code id obtain from
     * {@link com.action.postal.ZipCodeSearchAction#receiveClientData()
     * receiveClientData()} method.
     * 
     * @throws ActionCommandException
     *             When target zip code id is null or contains an invalid value.
     */
    public void edit() throws ActionCommandException {
        // // Convert value to number
        // int zipId;
        // this.pageResults = null;
        // try {
        // if (this.zipIdStr == null) {
        // this.msg = "Zip code id must selected for the \"View\" command";
        // this.logger.log(Level.ERROR, this.msg);
        // throw new ActionCommandException(this.msg);
        // }
        // zipId = Integer.parseInt(this.zipIdStr);
        // } catch (NumberFormatException e) {
        // this.msg = this.zipIdStr + " is invalid zip code identifier";
        // this.logger.log(Level.ERROR, this.msg);
        // throw new ActionCommandException(this.msg);
        // }
        //
        // // Get data for a single zip code instance.
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // this.api =
        // AddressComponentsFactory.createZipcodeApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // this.zip = (VwZipcode) this.api.findZipExtById(zipId);
        // if (this.zip == null) {
        // this.zip = AddressComponentsFactory.createZipcodeExt();
        // }
        // return;
        // } catch (ZipcodeException e) {
        // throw new ActionCommandException(e);
        // } finally {
        // this.api.close();
        // tx.close();
        // this.api = null;
        // tx = null;
        // }
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
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // this.api =
        // AddressComponentsFactory.createZipcodeApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // String criteria = this.query.getWhereClause();
        // this.zipList = this.api.findZip(criteria);
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


    private void doFetchTimezones() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // TimezoneApi tzApi =
        // AddressComponentsFactory.createTimezoneApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // this.tzList = (List<TimeZone>) tzApi.findAllTimeZones();
        // if (this.tzList == null) {
        // this.tzList = new ArrayList<TimeZone>();
        // }
        // this.msg = this.tzList.size() + " rows found";
        // this.setError(false);
        // this.sendClientData();
        // } catch (TimezoneException e) {
        // this.setError(true);
        // this.msg = e.getMessage();
        // this.sendClientData();
        // throw new ActionCommandException(e);
        // } finally {
        // tzApi.close();
        // tx.close();
        // tzApi = null;
        // tx = null;
        // }
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

    /**
     * Gathers zip code input data from the client's request.
     * 
     * @throws ActionCommandException
     */
    protected void receiveClientData() throws ActionCommandException {
        if (!this.isFirstTime()) {
            try {
                // Get zip code data from the selected row.
                this.zip = VwZipcodeFactory.create();
                RMT2WebUtility.packageBean(this.request, this.zip);
            } catch (Exception e) {
                logger.log(Level.ERROR, e.getMessage());
                throw new ActionCommandException(e.getMessage());
            }
        }
        // this.zipIdStr = this.getInputValue("ZipId", null);
        // this.zipCodeStr = this.getInputValue("ZipCode", null);
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

    // /**
    // * Retrieves a single {@link com.bean.VwZipcode VwZipcode} instance from
    // the
    // * database using the zip code id obtain from
    // * {@link com.action.postal.ZipCodeSearchAction#receiveClientData()
    // * receiveClientData()} method.
    // *
    // * @throws ActionCommandException
    // * When target zip code id is null or contains an invalid value.
    // */
    // public void editZipcodeByCode() throws ActionCommandException {
    // this.receiveClientData();
    //
    // // Convert value to number
    // if (this.zipCodeStr == null) {
    // this.msg = "A zip code must selected for the \"View\" command";
    // this.logger.log(Level.ERROR, this.msg);
    // throw new ActionCommandException(this.msg);
    // }
    //
    // // Get data for a single zip code instance.
    // DatabaseTransApi tx = DatabaseTransFactory.create();
    // this.api =
    // AddressComponentsFactory.createZipcodeApi((DatabaseConnectionBean)
    // tx.getConnector(), this.request);
    // try {
    // this.zipList = this.api.findZipExtByCode(this.zipCodeStr);
    // if (this.zipList == null) {
    // this.zipList = new ArrayList<VwZipcode>();
    // }
    // this.sendClientData();
    // return;
    // } catch (ZipcodeException e) {
    // throw new ActionCommandException(e);
    // } finally {
    // this.api.close();
    // tx.close();
    // this.api = null;
    // tx = null;
    // }
    // }

    // /**
    // * Creates the response message, RS_postal_search, from the java results
    // * obtained from the query. When the client action code is <i>list</i>,
    // the
    // * body will contain one or more zip code records in short format. When
    // the
    // * client action code is <i>view</i>, the body will contain a single zip
    // * code record in full format. This method mimics a web service
    // * functionality by converting the personal contact, which is a java
    // object,
    // * to XML and appending the results of the conversion to the
    // * RS_postal_search message.
    // *
    // * @return The XML Message
    // * @throws ActionCommandException
    // */
    // protected String getXmlResults() throws ActionCommandException {
    // // Mimic web service to return response as XML!
    // if (this.isError()) {
    // RMT2SessionBean userSession = (RMT2SessionBean)
    // this.request.getSession().getAttribute(RMT2ServletConst.SESSION_BEAN);
    // return PayloadFactory.buildCommonErrorPayload(-1, this.msg,
    // userSession.getLoginId());
    // }
    //
    // String xml = null;
    // String responseId = "RS_postal_search";
    // PostalSearchHandler srvc = new PostalSearchHandler(null, this.request);
    // srvc.setResponseServiceId(responseId);
    // List<VwZipcode> results = new ArrayList<VwZipcode>();
    // if (this.command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_SEARCH)
    // ||
    // this.command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_FETCHZIPCODE))
    // {
    // if (this.zipList != null && this.pageResults == null) {
    // results = (List<VwZipcode>) this.zipList;
    // xml = srvc.buildZipShortResponsePayload(results, this.msg);
    // }
    // else {
    // xml = srvc.buildZipShortResponsePayload(this.pageResults, this.msg);
    // }
    // }
    // else if (this.command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_VIEW))
    // {
    // if (this.zip != null) {
    // results.add(this.zip);
    // }
    // xml = srvc.buildZipFullResponsePayload(results, this.msg);
    // }
    // else if
    // (command.equalsIgnoreCase(ZipCodeSearchAction.COMMAND_FETCHTIMEZONES)) {
    // xml = srvc.buildTimezoneResponsePayload(this.tzList, this.msg);
    // }
    // return xml;
    // }

    // /**
    // * Adds custom selection criteria for a particular database column using
    // one
    // * or more UI query fields mapped to
    // * {@link com.bean.criteria.ZipcodeCriteria ZipcodeCriteria}. Each
    // criteria
    // * field is mapped to its corresponding database column in order to build
    // * the SQL predicate. The UI criteria field(s) that are applied for the
    // * Zipcode search page are: <i>qry_AreaCode</i>.
    // *
    // * @param queryField
    // * The name of the field that is to be represented by customized
    // * selection criteria.
    // * @return String as the selection criteria or null when custom criteria
    // is
    // * not required.
    // */
    // protected String buildCustomClientCriteria(String queryField) {
    // StringBuffer sql = new StringBuffer(20);
    // if ("qry_AreaCode".equalsIgnoreCase(queryField)) {
    // String value = this.request.getParameter(queryField);
    // sql.append(" area_code like '%");
    // sql.append(value);
    // sql.append("%' ");
    // }
    //
    // if (sql.length() > 0) {
    // return sql.toString();
    // }
    // return null;
    // }

}