package com.github.mostafaism1.mobile_number_portability.app.exception;

public class PortRequestConflictException extends RuntimeException {

  public PortRequestConflictException(String number) {
    super(String.format(
        "Cannot create request for number [%s] because it has another pending request.", number));
  }
}