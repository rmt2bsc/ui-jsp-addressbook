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

        // this.grp = this.getGroupFromRequest();
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
     * Retrieve the group record from the database using the group id value
     * stored in the request.
     * 
     * @return {@link GeneralCodesGroup}
     */
    private GeneralCodesGroup getGroupFromRequest() throws ActionCommandException {
        GeneralCodesGroup grp = GeneralCodesGroupFactory.create();
        Object temp = this.request.getAttribute(GeneralConst.CLIENT_DATA_RECORD);
        if (temp instanceof GeneralCodes) {
            grp = (GeneralCodesGroup) temp;
        }
        else {
            this.msg = "Problem occurred obtaining group record for the current general code record";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }

        return grp;
    }

    /**
     * Drives the process of invoking the General Code Edit JSP page for adding
     * a general code item to the system.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.code = GeneralCodesFactory.create();
    }

    /**
     * Drives the process of invoking the General Code Edit JSP page for
     * modifying existing general code item in the system.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        // this.getGroupFromRequest();
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // CodesApi api = CodesFactory.createCodesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // // Get group from database.
        // this.code = (GeneralCodes) api.findCodeById(this.code.getCodeId());
        // } catch (Exception e) {
        // throw new ActionCommandException(e.getMessage());
        // } finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
    }

    /**
     * Drives the process of persisting any General Code item changes to the
     * database.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // this.validate();
        // Save data.
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // CodesApi api = CodesFactory.createCodesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // api.maintainCode(this.code);
        // tx.commitUOW();
        // this.msg = "Lookup code saved successfully";
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
     * Deletes one general codes record from the database based on client's
     * selection.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // CodesApi api = CodesFactory.createCodesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // api.deleteCode(this.code);
        // tx.commitUOW();
        // this.msg = "Code was deleted successfully";
        // this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO,
        // this.msg);
        // return;
        // } catch (GeneralCodeException e) {
        // tx.rollbackUOW();
        // this.msg = "Code delete failed.  " + e.getMessage();
        // this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO,
        // this.msg);
        // throw new ActionCommandException(e.getMessage());
        // } finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
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
        if (this.code == null) {
            this.msg = "Validation Error:  General Code object is invalid";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
        if (this.code.getLongdesc() == null || this.code.getLongdesc().equals("")) {
            this.msg = "Validation Error:  General Code's description property is invalid";
            this.logger.log(Level.ERROR, this.msg);
            throw new ActionCommandException(this.msg);
        }
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

        String strId[] = this.request.getParameterValues(CodeAction.CODE_ID_PROPERTY);
        if (strId == null) {
            return;
        }
        // Gather all selected group id's
        this.selCodeId = new int[strId.length];
        for (int ndx = 0; ndx < strId.length; ndx++) {
            try {
                this.selCodeId[ndx] = Integer.parseInt(strId[ndx]);
            } catch (NumberFormatException e) {
                this.msg = "The selected group contains an invalid value: (row=" + ndx + ", value=" + strId[ndx];
                this.logger.log(Level.ERROR, this.msg);
                throw new ActionCommandException(this.msg);
            }
        }

        // // Try to obtain all data for the a single group item, if applicable.
        // if (this.selCodeId.length == 1) {
        // try {
        // this.code = CodesFactory.createGeneralCodes();
        // CodesFactory.packageBean(this.request, this.code);
        // }
        // catch (Exception e) {
        // throw new ActionCommandException(e.getMessage());
        // }
        // }
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
    // return srvc.buildCodeDetailsResponsePayload(this.code, this.msg);
    // }
}