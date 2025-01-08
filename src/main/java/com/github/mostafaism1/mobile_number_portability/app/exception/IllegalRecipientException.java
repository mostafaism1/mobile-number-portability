package com.github.mostafaism1.mobile_number_portability.app.exception;

public class IllegalRecipientException extends RuntimeException {

  public IllegalRecipientException(String number, String operator) {
    super(String.format("Mobile number [%s] is already assigned to [%s]", number, operator));
  }
}