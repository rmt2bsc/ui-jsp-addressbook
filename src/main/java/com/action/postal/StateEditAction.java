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
import com.entity.VwStateCountry;

/**
 * Action handler for managing request coming from the State/Province Edit page.
 * The handler responds to save, delete, and back commands.
 * 
 * @author RTerrell
 *
 */
public class StateEditAction extends AbstractActionHandler implements ICommand {
    /** Command name for saving changes to State/Province */
    protected static final String COMMAND_SAVE = "State.Edit.save";

    /** Command name for deleting State/Province record */
    protected static final String COMMAND_DELETE = "State.Edit.delete";

    /** Command name for navigating back to State/Province search page */
    protected static final String COMMAND_BACK = "State.Edit.back";

    private VwStateCountry state;

    private Logger logger;

    /**
     * Creates an instance of StateEditAction by initializing the logger.
     * 
     * @throws SystemException
     */
    public StateEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("PersonContactEditAction");
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is inistantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        logger.log(Level.INFO, "Initializing State/Province Edit Action handler");
    }

    /**
     * Driver for processing the client's State/Province Search page. The
     * following commands are serviced in this action handler:
     * {@link com.action.contacts.StateEditAction.COMMAND_SAVE Save},
     * {@link com.action.contacts.StateEditAction.COMMAND_DELETE Delete}, and
     * {@link com.action.contacts.StateEditAction.COMMAND_BACK Back}
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
        if (command.equalsIgnoreCase(StateEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(StateEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(StateEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Saves the modifications of a state/province record which are persisted to
     * the database. After successfully saving the data, the model
     * state/province object, {@link com.bean.State State} is refreshed from the
     * database so that it may be sent to the client for presentation.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // this.api =
        // AddressComponentsFactory.createStatesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // this.api.maintainState(this.state);
        // tx.commitUOW();
        // this.refreshState();
        // this.msg = "State was saved successfully";
        // this.setError(false);
        // } catch (Exception e) {
        // tx.rollbackUOW();
        // this.setError(true);
        // this.msg = "Unable to save State/Province record [State Id = " +
        // this.state.getStateId() + "].  " + e.getMessage();
        // logger.error(this.msg);
        // throw new ActionCommandException(e);
        // } finally {
        // this.api.close();
        // tx.close();
        // this.api = null;
        // tx = null;
        // }
    }



    /**
     * Navigates the user back to the State/Province search page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        try {
            StateSearchAction action = new StateSearchAction();
            action.processRequest(this.request, this.response, StateSearchAction.COMMAND_LIST);
        } catch (SystemException e) {
            throw new ActionCommandException(e);
        }
        return;
    }


    /**
     * Deletes a State/Province record from the database.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // this.api =
        // AddressComponentsFactory.createStatesApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // int rows = this.api.deleteState(this.state);
        // this.msg = rows +
        // " State records deleted successfully pertaining to state, " +
        // this.state.getStateName();
        // this.setError(false);
        // tx.commitUOW();
        // return;
        // } catch (ContactException e) {
        // this.msg = "Unable to delete State/Province record [State Id = " +
        // this.state.getStateId() + "].  " + e.getMessage();
        // logger.error(this.msg);
        // this.setError(true);
        // tx.rollbackUOW();
        // } finally {
        // this.api.close();
        // tx.close();
        // this.api = null;
        // tx = null;
        // }
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

    /**
     * Gathers data from the request to populate a {@link com.bean.State State}
     * object.
     * 
     * @throws ActionCommandException
     *             Error obtaining client data.
     */
    protected void receiveClientData() throws ActionCommandException {
        // this.state = AddressComponentsFactory.createState();
        // try {
        // DataSourceAdapter.packageBean(this.request, this.state);
        // return;
        // } catch (Exception e) {
        // throw new ActionCommandException(e);
        // }
    }

    /**
     * Adds the results of single and list queries of state/provinces to the
     * request for the client to process. Single state/province query results
     * are identified on the request as
     * {@link com.constants.GeneralConst#CLIENT_DATA_RECORD CLIENT_DATA_RECORD}.
     * Query results that are a list of state/province are identified on the
     * request as {@link com.constants.GeneralConst#CLIENT_DATA_LIST
     * CLIENT_DATA_LIST}.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.state);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);

        String xml = this.getXmlResults();
        this.request.setAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA, xml);
    }

    // /**
    // * Get data for a single state/province instance.
    // *
    // * @throws ActionCommandException
    // */
    // private void refreshState() throws ActionCommandException {
    // try {
    // this.state = (State) this.api.findStateById(this.state.getStateId());
    // if (this.state == null) {
    // this.state = AddressComponentsFactory.createState();
    // }
    // return;
    // } catch (StatesException e) {
    // throw new ActionCommandException(e);
    // }
    // }
}
