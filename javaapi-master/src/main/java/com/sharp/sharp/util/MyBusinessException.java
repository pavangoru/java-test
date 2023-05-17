package com.sharp.sharp.util;

public class MyBusinessException extends Exception{

	 private static final long serialVersionUID = 7718828512143293558L;

	    private final int code;

	    public MyBusinessException(int code) {
	        super();
	        this.code = code;
	    }

	    public MyBusinessException(String message, Throwable cause, int scInternalServerError) {
	        super(message, cause);
	        this.code = scInternalServerError;
	    }

	    public MyBusinessException(String message, int expectationFailed) {
	        super(message);
	        this.code = expectationFailed;
	    }

	    public MyBusinessException(Throwable cause, int code) {
	        super(cause);
	        this.code = code;
	    }
	    public int getCode() {
	        return this.code;
	    }
}
