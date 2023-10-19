package com.action.codes;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.GeneralCodesGroup;
import com.entity.GeneralCodesGroupFactory;

/**
 * This class provides action handlers to respond to an associated controller
 * for searching, adding, and deleting General Code Group information.
 * 
 * @author appdev
 *
 */
public class CodeGroupAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_LIST = "GeneralCodeGroup.GeneralCodeGroupList.list";

    private static final String COMMAND_LIST2 = "GeneralCodeGroup.GeneralCodeGroupEdit.back";

    private static final String COMMAND_ADD = "GeneralCodeGroup.GeneralCodeGroupList.add";

    private static final String COMMAND_EDIT = "GeneralCodeGroup.GeneralCodeGroupList.edit";

    private static final String COMMAND_DETAILS = "GeneralCodeGroup.GeneralCodeGroupList.details";

    private static final String COMMAND_DELETE = "GeneralCodeGroup.GeneralCodeGroupEdit.delete";

    private static final String COMMAND_SAVE = "GeneralCodeGroup.GeneralCodeGroupEdit.save";

    private static final String COMMAND_BACK = "GeneralCodeGroup.GeneralCodeGroupEdit.back";

    private static final String GROUP_ID_PROPERTY = "CodeGrpId";

    private Logger logger;

    private Object rec;

    private Object list;

    private String command;

    private int selGroupId[];

    /**
     * Default constructor.
     *
     */
    public CodeGroupAction() {
        super();
        logger = Logger.getLogger("CodeGroupAction");
    }

    /**
     * Constructor for instantiating a TimesheetEditAction object using request,
     * response, and command.
     * 
     * @param request
     *            The HttpServletRequest containing the clinets data.
     * @param response
     *            The HttpServletResponse
     * @param command
     *            The clients command.
     * @throws ActionCommandException
     */
    public CodeGroupAction(Request request, Response response, String command) throws ActionCommandException {
        this();
        try {
            this.init(null, request);
            this.init();
        } catch (Exception e) {
            this.msg = e.getMessage();
            logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
    }

    /**
     * Initializes the general codes api.
     * 
     * @param _context
     *            the servet context
     * @param _request
     *            the http servlet request
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        return;
    }

    /**
     * Processes the client's request using request, response, and command.
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
        try {
            this.init(null, request);
            this.init();

            this.command = command;
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_LIST) || command.equalsIgnoreCase(CodeGroupAction.COMMAND_LIST2)) {
                this.doGroupList();
            }
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_ADD)) {
                this.addData();
            }
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_DELETE)) {
                this.deleteData();
            }
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_EDIT)) {
                this.editData();
            }
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_DETAILS)) {
                this.doGroupCodes();
            }
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_SAVE)) {
                this.saveData();
            }
            if (command.equalsIgnoreCase(CodeGroupAction.COMMAND_BACK)) {
                this.doBack();
            }
        } catch (Exception e) {
            this.msg = e.getMessage();
            // this.transObj.rollbackUOW();
            throw new ActionCommandException(this.msg);
        } finally {
            // Ensure that any updates made to the the query object is set on
            // the session.
            if (this.query != null) {
                this.request.getSession().setAttribute(RMT2ServletConst.QUERY_BEAN, this.query);
            }
        }
    }

    /**
     * Drives the process of displaying the list of General Code Groups
     * 
     * @throws ActionCommandException
     */
    protected void doGroupList() throws ActionCommandException {
        // Call SOAP web service to get complete list of code groups
        try {
            LookupCodesResponse response = CodeGroupSoapRequests.callGet();
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<GeneralCodesGroup> results = GeneralCodesGroupFactory.create(response.getGroupCodes());
            this.list = results;
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Drives the process of invoking the General Code Group Edit JSP page for
     * adding a general code group item to the system.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        // this.grp = CodesFactory.createGeneralGroup();
        this.sendClientData();
    }

    /**
     * Drives the process of invoking the General Code Group Edit JSP page for
     * modifying existing general code group item in the system.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        this.validate();
    }

    /**
     * Drives the process of persisting General Code Group item changes to a
     * data provider.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // this.validate();
        // // Save data.
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // CodesApi api = CodesFactory.createCodesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // api.maintainGroup(this.grp);
        // tx.commitUOW();
        // this.msg = "Group saved successfully";
        // this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO,
        // this.msg);
        // } catch (GeneralCodeException e) {
        // tx.rollbackUOW();
        // throw new ActionCommandException(e.getMessage());
        // } finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
    }

    /**
     * Drives the process of deleting one or more General Code Group items from
     * the system.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // CodesApi api = CodesFactory.createCodesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // for (int ndx = 0; ndx < this.selGroupId.length; ndx++) {
        // try {
        // this.grp = (GeneralCodesGroup)
        // api.findGroupById(this.selGroupId[ndx]);
        // api.deleteGroup(this.grp);
        // } catch (GeneralCodeException e) {
        // tx.rollbackUOW();
        // throw new ActionCommandException(e.getMessage());
        // }
        // }
        // tx.commitUOW();
        // this.msg = "Group deleted successfully";
        // } catch (ActionCommandException e) {
        // this.msg = e.getMessage();
        // throw e;
        // } finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
    }

    /**
     * Drives the process of invoking the General Codes Edit JSP page for
     * displaying all related general code items of a particular general code
     * group.
     * 
     * @throws ActionCommandException
     */
    protected void doGroupCodes() throws ActionCommandException {
        this.receiveClientData();
        // Get group record
        this.edit();

        // Get group from database.
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // CodesApi api = CodesFactory.createCodesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // this.codes = (List) api.findCodeByGroup(this.selGroupId[0]);
        // } catch (GeneralCodeException e) {
        // throw new ActionCommandException(e.getMessage());
        // } finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }

        this.sendClientData();
    }

    /**
     * Validates the current General Code Group item. The precondition to
     * invoking this method is that this object is properly initialized with the
     * general code group data that is to be validated.
     *
     * @throws ActionCommandException
     *             data object was not properly initialized or the general code
     *             group description property is null.
     */
    protected void validate() throws ActionCommandException {
        String temp = this.getInputValue("CodeGrpId", null);
        if (temp == null) {
            throw new ActionCommandException("A group record must be selected for this operation");
        }
    }

    /**
     * Action handler for returning the user to the home page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        return;
    }

    /**
     * This obtains the clients input from the JSP which is generally a list of
     * General Code Group Id's
     * 
     * @throws ActionCommandException
     *             when one of the client's group id's a non-numeric value
     */
    protected void receiveClientData() throws ActionCommandException {
        String temp = null;
        GeneralCodesGroup data = GeneralCodesGroupFactory.create();
        try {
            temp = this.getInputValue("CodeGrpId", null);
            data.setCodeGrpId(Integer.parseInt(temp));
        } catch (NumberFormatException e) {
            this.msg = "The selected group contains an invalid value: " + temp;
            throw new ActionCommandException(this.msg);
        }

        temp = this.getInputValue("Description", null);
        data.setDescription(temp);
        this.rec = data;

        // app.setDescription(temp);
        // temp = this.getInputValue("Status", null);
        //
        // String strId[] =
        // this.request.getParameterValues(CodeGroupAction.GROUP_ID_PROPERTY);
        // if (strId == null) {
        // return;
        // }
        // // Gather all selected group id's
        // this.selGroupId = new int[strId.length];
        // for (int ndx = 0; ndx < strId.length; ndx++) {
        // try {
        // this.selGroupId[ndx] = Integer.parseInt(strId[ndx]);
        // } catch (NumberFormatException e) {
        // this.msg = "The selecte group contains an invalid value: (row=" + ndx
        // + ", value=" + strId[ndx];
        // throw new ActionCommandException(this.msg);
        // }
        // }
        //
        // // Try to obtain all data for the a single group item, if applicable.
        // if (this.command.equalsIgnoreCase(CodeGroupAction.COMMAND_SAVE) &&
        // this.selGroupId.length == 1) {
        // // try {
        // // this.grp = CodesFactory.createGeneralGroup();
        // // CodesFactory.packageBean(this.request, this.grp);
        // // } catch (SystemException e) {
        // // throw new ActionCommandException(e.getMessage());
        // // }
        // }
    }

    /**
     * This method performs any initialization routines prior to displaying the
     * General Code Group list to the client.
     * <p>
     * The initialization tasks are:
     * <ul>
     * <li>Resets the session query bean</li>
     * </ul>
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        try {
            this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.rec);
            this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.list);
            // this.query = new RMT2TagQueryBean();
        } catch (SystemException e) {
            // do nothing
        }
        // String xml = this.getXmlResults();
        // this.request.setAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA,
        // xml);
    }

    // /**
    // * Sends a update confirmation to client as the XML message,
    // * RS_common_reply.
    // *
    // * @return The XML Message
    // * @throws ActionCommandException
    // */
    // protected String getXmlResults() throws ActionCommandException {
    // CodeDetailsFetchHandler srvc = new CodeDetailsFetchHandler(null,
    // this.request);
    // return srvc.buildCodeGroupResponsePayload(this.grp, this.msg);
    // }
}