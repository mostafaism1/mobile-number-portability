package com.github.mostafaism1.mobile_number_portability.app.exception;

public class InvalidOperatorException extends RuntimeException {
  public InvalidOperatorException(String operatorName) {
    super(String.format("[%s] is not a valid operator.", operatorName));
  }
}