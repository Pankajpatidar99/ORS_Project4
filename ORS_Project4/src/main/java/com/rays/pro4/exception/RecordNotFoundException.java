package com.rays.pro4.exception;
/**
 * RecordNotFoundException thrown when a record not found occurred.
 * 
 * @author Pankaj Patidar
 * 
 */
public class RecordNotFoundException extends Exception {
	public RecordNotFoundException(String msg){
		super(msg);
	}
}
