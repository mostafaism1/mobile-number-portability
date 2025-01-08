package com.github.mostafaism1.mobile_number_portability.app.exception;

public class InvalidMobileNumberException extends RuntimeException {
  public InvalidMobileNumberException(String number) {
    super(String.format("[%s] is not a valid mobile number.", number));
  }
}