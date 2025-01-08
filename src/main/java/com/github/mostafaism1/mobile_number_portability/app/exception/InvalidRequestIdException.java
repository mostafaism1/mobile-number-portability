package com.github.mostafaism1.mobile_number_portability.app.exception;

public class InvalidRequestIdException extends RuntimeException {
  public InvalidRequestIdException(Long id) {
    super(String.format("[%d] is not a valid Port Request ID.", id));
  }
}