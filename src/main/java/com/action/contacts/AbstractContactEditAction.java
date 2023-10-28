package com.action.contacts;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.codes.CodeSoapRequests;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.GeneralCodes;
import com.entity.GeneralCodesFactory;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from a contact edit page.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractContactEditAction extends AbstractContactAction {
    /** Command name for saving a contact */
    protected static final String COMMAND_SAVE = "Contact.Search.save";

    /** Command name for deleting a contact */
    protected static final String COMMAND_DELETE = "Contact.Search.delete";

    /**
     * Command name for navigating back to the previous page from contact edit
     * page
     */
    protected static final String COMMAND_BACK = "Contact.Search.back";

    /** The current command */
    protected String command;

    private Logger logger;

    private int contactId;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AbstractContactEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("AbstractContactEditAction");
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        logger.log(Level.INFO, "Initializing Common Contact Api's");
    }

    /**
     * Driver for processing the client's Contact Search page. The following
     * commands are serviced in this action handler:
     * {@link com.action.contacts.AbstractContactEditAction.COMMAND_SAVE Save
     * Contact},
     * {@link com.action.contacts.AbstractContactEditAction.COMMAND_DELETE
     * Delete Contact},
     * {@link com.action.contacts.AbstractContactEditAction.COMMAND_BACK
     * Navigate Backwards}.
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
        if (command.equalsIgnoreCase(AbstractContactEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(AbstractContactEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(AbstractContactEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    protected List<GeneralCodes> getLookupData(int codeGroupId) throws ActionCommandException {
        GeneralCodes code = GeneralCodesFactory.create();
        code.setCodeGrpId(codeGroupId);

        // Call SOAP web service to get complete list of codes based on a
        // particular group
        try {
            LookupCodesResponse response = CodeSoapRequests.callGet(code);
            ReplyStatusType rst = response.getReplyStatus();
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

    /**
     * Gathers contact's address data from the request.
     * 
     * @throws ActionCommandException
     *             When the cotnact's address id is invalid or cannot be
     *             verified.
     */
    protected void receiveClientData() throws ActionCommandException {

        return;
    }

    /**
     * Sets the results of the personal conact update on the request object in
     * the form of a java object and XML as attributes,
     * {@link com.api.ContactsConst#CLIENT_DATA CLIENT_DATA} and
     * {@link com.constants.RMT2ServletConst.RESPONSE_NONJSP_DATA
     * RESPONSE_NONJSP_DATA}, respectively.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        super.sendClientData();
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.vwBusinessAddress);
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        return;
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        return;
    }

    /**
     * Stub implementation for descendant.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        return;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}