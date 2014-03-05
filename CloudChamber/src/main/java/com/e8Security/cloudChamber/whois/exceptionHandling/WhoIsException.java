package com.e8Security.cloudChamber.whois.exceptionHandling;

/**
 * WhoIsException class is used to provide meaningful exception messages to callers.
 * 
 * @author Abhijit
 *
 */
public class WhoIsException extends Exception{

private String message;

/**
 * Constructs a new whois exception with null as its detail message.
 * The cause is not initialized, and may subsequently be initialized by a call to 
 * Throwable.initCause(java.lang.Throwable).
 * 
 */
public WhoIsException() {
    super();
}

/**
 * Constructs a new exception with the specified detail message. The cause is not initialized, 
 * and may subsequently be initialized by a call to Throwable.initCause(java.lang.Throwable).
 * 
 * @param message
 */

public WhoIsException(String message){
	super(message);
	this.message=message;
}


/**
 * Constructs a new exception with the specified cause and a detail message of 
 * (cause==null ? null : cause.toString())(which typically contains the class and detail message of cause).
 * 
 * @param cause
 */


public WhoIsException(Throwable cause) {
    super(cause);
}


/**
 * 
 * Constructs a new exception with the specified detail message and cause.
 * @param message
 * @param cause
 */
public WhoIsException(String message,Throwable cause) {
    super(message,cause);
}


@Override
public String getMessage() {
	return message;
}

@Override
public String toString() {
	// TODO Auto-generated method stub
	return message;
}



}
