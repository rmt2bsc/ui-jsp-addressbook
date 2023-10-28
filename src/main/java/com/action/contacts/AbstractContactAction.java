package com.action.contacts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AddressBookResponse;
import org.rmt2.jaxb.LookupCodesResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.codes.CodeSoapRequests;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.util.assistants.Verifier;
import com.api.util.assistants.VerifyException;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.entity.ContactCriteria;
import com.entity.GeneralCodes;
import com.entity.GeneralCodesFactory;
import com.entity.VwBusinessAddress;
import com.entity.VwBusinessAddressFactory;

/**
 * This abstract action handler provides common functionality to respond to the
 * requests originating from maintaining an Addressbook contact.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class AbstractContactAction extends AbstractActionHandler implements ICommand {



    protected Object vwBusinessAddress;

    protected Object lookupBusType;

    protected Object lookupBusServ;

    /** The current contact id */
    protected int contactId;

    /** The current address id */
    protected int addressId;

    private Logger logger;


    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AbstractContactAction() throws SystemException {
        super();
        logger = Logger.getLogger(AbstractContactAction.class);
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Sets the results of an Addressbook contact operation.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(ContactsConst.CLIENT_DATA_BUSTYPE, this.lookupBusType);
        this.request.setAttribute(ContactsConst.CLIENT_DATA_SERVTYPE, this.lookupBusServ);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Retrieves one or more business contact profiles.
     * 
     * @param criteria
     *            instance of {@link ContactCriteria}
     * @return List<{@link VwBusinessAddress}>
     * @throws {@link ContactException}
     */
    protected List<VwBusinessAddress> getContacts(ContactCriteria criteria) throws ContactException {
        try {
            Verifier.verifyNotNull(criteria);
        } catch (VerifyException e) {
            throw new ContactException("Contact retrieval requires a valid criteria object");
        }

        // Fetch contact information
        try {
            AddressBookResponse response = BusinessContactSoapRequests.callGet(criteria);

            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            this.msg += " (" + rst.getRecordCount() + ")";

            List<VwBusinessAddress> results = null;
            if (response.getProfile() != null) {
                results = VwBusinessAddressFactory.create(response.getProfile().getBusinessContacts());
            }
            else {
                results = new ArrayList<>();
            }
            return results;
        } catch (ContactException e) {
            throw e;
        }
    }

    /**
     * Retrieves a list of General Code records based on general code group.
     * 
     * @param codeGroupId
     *            the id of the group to gather general codes.
     * @return List<{@link GeneralCodes}>
     * @throws ActionCommandException
     */
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
}