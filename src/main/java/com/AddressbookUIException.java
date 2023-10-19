package com;


public class AddressbookUIException extends RMT2RuntimeException {
    private static final long serialVersionUID = 3146419736970380825L;

    /**
     * Default constructor that creates an AddressbookUIException object with a
     * null message.
     * 
     */
    public AddressbookUIException() {
        super();
    }

    /**
     * Creates an AddressbookUIException with a message.
     * 
     * @param msg
     *            The text message.
     */
    public AddressbookUIException(String msg) {
        super(msg);
    }

    /**
     * Creates an AddressbookUIException with a message and a code.
     * 
     * @param msg
     *            The text message.
     * @param code
     *            The integer code.
     */
    public AddressbookUIException(String msg, int code) {
        super(msg, code);
    }

    /**
     * Creates an AddressbookUIException using an Exception.
     * 
     * @param e
     *            An Exception object.
     */
    public AddressbookUIException(Exception e) {
        super(e);
    }

    /**
     * Creates a new AddressbookUIException with a the specified message and the
     * causing throwable instance.
     * 
     * @param msg
     *            the message that explains the error.
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     * 
     */
    public AddressbookUIException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
