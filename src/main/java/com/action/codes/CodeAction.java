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
import com.api.util.RMT2Money;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.GeneralCodes;
import com.entity.GeneralCodesFactory;
import com.entity.GeneralCodesGroup;
import com.entity.GeneralCodesGroupFactory;

/**
 * This class provides action handlers to respond to an associated controller
 * for searching, adding, and deleting General Codes.
 * 
 * @author appdev
 *
 */
public class CodeAction extends AbstractActionHandler implements ICommand {
    protected static final String COMMAND_BACK = "GeneralCode.GeneralCodeList.back";

    private static final String COMMAND_ADD = "GeneralCode.GeneralCodeList.add";

    private static final String COMMAND_EDIT = "GeneralCode.GeneralCodeList.edit";

    private static final String COMMAND_SAVE = "GeneralCode.GeneralCodeEdit.save";

    private static final String COMMAND_DELETE = "GeneralCode.GeneralCodeEdit.delete";

    private static final String COMMAND_LIST = "GeneralCode.GeneralCodeEdit.list";

    private static final String CODE_ID_PROPERTY = "CodeId";

    private Logger logger;

    private GeneralCodes code;

    private GeneralCodesGroup grp;

    private Object codes;

    private int selCodeId[];

    /**
     * Default constructor.
     *
     */
    public CodeAction() {
        super();
        logger = Logger.getLogger("CodeAction");
    }

    /**
     * Constructor for instantiating a TimesheetEditAction object using request,
     * response, and command.
     * 
     * @param request
     *            The HttpServletRequest containing the clients data.
     * @param response
     *            The HttpServletResponse
     * @param command
     *            The clients command.
     * @throws ActionCommandException
     */
    public CodeAction(Request request, Response response, String command) throws ActionCommandException {
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
        try {
            this.init(null, request);
            this.init();

            this.command = command;
            if (command.equalsIgnoreCase(CodeAction.COMMAND_LIST) || command.equalsIgnoreCase(CodeGroupAction.COMMAND_DETAILS)) {
                this.doList();
            }
            if (command.equalsIgnoreCase(CodeAction.COMMAND_ADD)) {
                this.addData();
            }
            if (command.equalsIgnoreCase(CodeAction.COMMAND_DELETE)) {
                this.deleteData();
            }
            if (command.equalsIgnoreCase(CodeAction.COMMAND_EDIT)) {
                this.editData();
            }
            if (command.equalsIgnoreCase(CodeAction.COMMAND_SAVE)) {
                this.saveData();
            }
            if (command.equalsIgnoreCase(CodeAction.COMMAND_BACK)) {
                this.doBack();
            }
        } catch (Exception e) {
            throw new ActionCommandException("General code client request failed.", e);
        } finally {
            // Ensure that any updates made to the the query object is set on
            // the session.
            if (this.query != null) {
                this.request.getSession().setAttribute(RMT2ServletConst.QUERY_BEAN, this.query);
            }
        }
    }



    /**
     * Drives the process of displaying the list of General Codes by Group
     * 
     * @throws ActionCommandException
     */
    protected void doList() throws ActionCommandException {
        this.receiveClientData();
        GeneralCodes code = GeneralCodesFactory.create();
        code.setCodeGrpId(this.grp.getCodeGrpId());

        // Call SOAP web service to get complete list of codes based on a
        // particular group
        try {
            LookupCodesResponse response = CodeSoapRequests.callGet(code);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<GeneralCodes> results = GeneralCodesFactory.create(response.getDetailCodes());
            this.codes = results;
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }


    /**
     * Drives the process of invoking the General Code Edit JSP page for adding
     * a general code item to the system.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.receiveClientData();
        this.code = GeneralCodesFactory.create();
    }

    /**
     * Drives the process of invoking the General Code Edit JSP page for
     * modifying existing general code item in the system.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        this.msg = null;
        this.validate();
    }

    /**
     * Drives the process of persisting any General Code item changes to the
     * database.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {

        // Get record from request
        this.code = this.getRecord();

        // Call SOAP web service to persist general code record changes
        try {
            LookupCodesResponse response = CodeSoapRequests.callSave(this.code);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<GeneralCodes> results = GeneralCodesFactory.create(response.getDetailCodes());
            this.code = results.get(0);
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Deletes one general codes record from the database based on client's
     * selection.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {

    }



    /**
     * Returns the user to the list of groups page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        return;
    }

    /**
     * This method is capable of obtaining the clients input from the either the
     * General Code List or Edit JSP's.
     * 
     * @throws ActionCommandException
     *             when one of the client's group id's a non-numeric value
     */
    protected void receiveClientData() throws ActionCommandException {
        // Get General Code Group data
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
        this.grp = data;
    }

    /**
     * This method sends a single GeneralCode object, if applicable, a list of
     * codes pertaining to the group, and a single GeneralCodesGroup object to
     * the client for presentation.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.code);
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.codes);
        this.request.setAttribute(GeneralConst.REQ_ATTRIB_DATA, this.grp);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_MESSAGES, this.msg);
    }

    /**
     * Validates the current General Code item. The precondition to invoking
     * this method is that this object is properly initialized with the general
     * code data that is to be validated.
     * 
     * @throws ActionCommandException
     *             data object was not properly initialized or the general code
     *             group description property is null.
     */
    protected void validate() throws ActionCommandException {
        this.code = this.getSelectedRecord();

        if (this.code == null) {
            this.msg = "Validation Error:  A General Code record must be selected for this operation";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
    }

    private GeneralCodes getSelectedRecord() throws ActionCommandException {
        // Get index of the row that is to be processed from the
        // HttpServeltRequest object
        try {
            String temp = this.request.getParameter(GeneralConst.CLIENTROW_PROPERTY);
            this.selectedRow = RMT2Money.stringToNumber(temp).intValue();
        } catch (Exception e) {
            return null;
        }

        // Retrieve values from the request object into the User object.
        try {
            GeneralCodes selectedRecord = GeneralCodesFactory.create();
            RMT2WebUtility.packageBean(this.request, selectedRecord, this.selectedRow);
            return selectedRecord;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    private GeneralCodes getRecord() throws ActionCommandException {
        // Retrieve values from the request object into the User object.
        try {
            GeneralCodes selectedRecord = GeneralCodesFactory.create();
            RMT2WebUtility.packageBean(this.request, selectedRecord);
            return selectedRecord;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }
}