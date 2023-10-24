package com.action.contacts;

import com.RMT2Exception;

/**
 * An exception that provides data and information pertaining to general contact
 * operations.
 * 
 * @author RTerrell
 *
 */
public class ContactException extends RMT2Exception {
    private static final long serialVersionUID = 3776403355177086870L;

    /**
     * Constructs a ContactException object with a null message.
     *
     */
    public ContactException() {
        super();
    }

    /**
     * Creates a ContactException object by assigning the error message.
     * 
     * @param msg
     *            The error message.
     */
    public ContactException(String msg) {
        super(msg);
    }

    /**
     * Creates a ContactException object with a null message and an assigned
     * error code.
     * 
     * @param code
     *            The error code.
     */
    public ContactException(int code) {
        super(code);
    }

    /**
     * Creates a ContactException object with a error message and error code.
     * 
     * @param msg
     *            The error text.
     * @param code
     *            The error code.
     */
    public ContactException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates a ContactException object by reporting the error text, error
     * code, originating object and method name pertaining to the error.
     * 
     * @param msg
     *            Error text
     * @param code
     *            Error code
     * @param objectname
     *            Class name where error originated.
     * @param methodname
     *            The name of the method which error occurred.
     */
    public ContactException(String msg, int code, String objectname, String methodname) {
        super(msg, code, objectname, methodname);
    }

    /**
     * Creates a ContactException object by using data contained in an existing
     * exception object.
     * 
     * @param e
     *            The exception object.
     */
    public ContactException(Exception e) {
        super(e);
    }

    public ContactException(String msg, Throwable e) {
        super(msg, e);
    }
}
