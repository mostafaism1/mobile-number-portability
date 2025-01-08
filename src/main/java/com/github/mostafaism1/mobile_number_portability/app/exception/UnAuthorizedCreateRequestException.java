package com.github.mostafaism1.mobile_number_portability.app.exception;

public class UnAuthorizedCreateRequestException extends RuntimeException {
  public UnAuthorizedCreateRequestException(String requestedBy, String recipient) {
    super(String.format("[%s] cannot create a port request on behalf of [%s].", requestedBy,
        recipient));
  }
}