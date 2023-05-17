/**
 * 
 */
package com.sharp.sharp.exception;

import org.apache.log4j.Logger;


/**
 *
 */
@SuppressWarnings("serial")
public class SHARPWebServiceException extends Exception {
	
	private static final Logger logger = Logger.getLogger(SHARPWebServiceException.class);

	/**
	 * 
	 */

	private String message = null;
	private String erroCode = null;

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	

	public String getErroCode() {
		return erroCode;
	}

	public void setErroCode(String erroCode) {
		this.erroCode = erroCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 */
	public SHARPWebServiceException() {
	}
	
	/**
	 * @param message
	 */
	public SHARPWebServiceException(String erroCode , String message ) {
		super(message);
		logger.error("Exception issued by KPOST Webservice  "+erroCode+"  "+message);
		this.message=message;
		this.erroCode=erroCode;
	}

	/**
	 * @param message
	 */
	public SHARPWebServiceException(String message) {
		super(message);
		this.message=message;
	}

	/**
	 * @param cause
	 */
	public SHARPWebServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SHARPWebServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SHARPWebServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
