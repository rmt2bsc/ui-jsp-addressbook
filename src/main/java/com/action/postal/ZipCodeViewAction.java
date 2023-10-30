package com.action.postal;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.SystemException;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;

/**
 * This abstract action handler provides common functionality to respond 
 * to the requests originating from the zip code view/edit page.  The only 
 * command that is handled is: {@link com.action.postal.ZipCodeViewAction#COMMAND_BACK Navigate back to search page}, and
 * 
 * @author Roy Terrell
 * 
 */
public class ZipCodeViewAction extends AbstractActionHandler implements ICommand {

    /** Command name for navigating to previous page */
    protected static final String COMMAND_BACK = "Zipcode.View.back";

    private Logger logger;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public ZipCodeViewAction() throws SystemException {
        super();
        logger = Logger.getLogger("ZipCodeViewAction");
        this.logger.log(Level.DEBUG, "Logger initialized");
    }

    /**
     * Performs the initialization needed to properly utilize this handler.   
     * Initializes this object using <i>context</i> and <i>request</i>. 
     * 
     * @param context the servet context.
     * @param request the http servlet request.
     * @throws SystemException
     */
    protected void init(Context context, Request request) throws SystemException {
        super.init(context, request);
    }

    /**
     * Driver for processing the client's zip code view page.
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
    	}
    	catch (SystemException e) {
            throw new ActionCommandException(e);
    	}
    	if (command.equalsIgnoreCase(ZipCodeViewAction.COMMAND_BACK)) {
    	    this.doBack();
    	}
    	return;
    }

    /**
     * Navigates the user back to the home page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        try {
            ZipCodeSearchAction search = new ZipCodeSearchAction();
            search.processRequest(this.request, this.response, ZipCodeSearchAction.COMMAND_LIST);
        } catch (SystemException e) {
            throw new ActionCommandException(e);
        }
        return;
    }

    /**
     * N/A
     * 
     * @throws ActionCommandException
     */
    protected void receiveClientData() throws ActionCommandException {
        return;
    }

    /**
     * N/A
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        return;
    }
    
    /**
     * N/A
     * 
     * @throws ActionCommandException
     *             When target zip code id is null or contains an invalid value.
     */
    public void edit() throws ActionCommandException {
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
   
}