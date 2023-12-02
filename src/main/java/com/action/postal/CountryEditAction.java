package com.action.postal;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.Country;

/**
 * Action handler for managing request coming from the Country Edit page. The
 * handler responds to save, delete, and back commands.
 * 
 * @author RTerrell
 *
 */
public class CountryEditAction extends AbstractActionHandler implements ICommand {
    /** Command name for saving changes to Country */
    protected static final String COMMAND_SAVE = "Country.Edit.save";

    /** Command name for deleting Country record */
    protected static final String COMMAND_DELETE = "Country.Edit.delete";

    /** Command name for navigating back to Country search page */
    protected static final String COMMAND_BACK = "Country.Edit.back";

    private Country country;

    private Logger logger;

    /**
     * Creates an instance of StateEditAction by initializing the logger.
     * 
     * @throws SystemException
     */
    public CountryEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("PersonContactEditAction");
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        logger.log(Level.INFO, "Initializing Country Edit Action handler");
    }

    /**
     * Driver for processing the client's Country Search page. The following
     * commands are serviced in this action handler:
     * {@link com.action.contacts.StateEditAction.COMMAND_SAVE Save},
     * {@link com.action.contacts.StateEditAction.COMMAND_DELETE Delete}, and
     * {@link com.action.contacts.StateEditAction.COMMAND_BACK Back}.
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
        if (command.equalsIgnoreCase(CountryEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(CountryEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(CountryEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Saves the modifications of a state/provinces record which are persisted
     * to the database. After successfully saving the data, the model
     * state/provinces object, {@link com.bean.Country Country} is refreshed
     * from the database so that it may be sent to the client for presentation.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        return;
    }

    /**
     * Navigates the user back to the Country search page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        try {
            CountrySearchAction action = new CountrySearchAction();
            action.processRequest(this.request, this.response, CountrySearchAction.COMMAND_LIST);
        } catch (SystemException e) {
            throw new ActionCommandException(e);
        }
        return;
    }

    /**
     * Gathers data from the request to populate a {@link com.bean.State State}
     * object.
     * 
     * @throws ActionCommandException
     *             Error obtaining client data.
     */
    protected void receiveClientData() throws ActionCommandException {
        return;
    }

    /**
     * Adds the results of single and list queries of state/provincess to the
     * request for the client to process. Single state/provinces query results
     * are identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_RECORD CLIENT_DATA_RECORD}.
     * Query results that are a list of state/provinces are identified on the
     * request as {@link com.constants.GeneralConst#CLIENT_DATA_LIST
     * CLIENT_DATA_LIST}.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.country);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    /**
     * Deletes a Country record from the database.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
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
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        return;
    }

}
