package com.github.mostafaism1.mobile_number_portability.app.exception;

public class UnAuthorizedUpdateRequestException extends RuntimeException {
  public UnAuthorizedUpdateRequestException(String requestedBy, String donor, Long requestId) {
    super(
        String.format("%s cannot change the state of port request [%s], only the donor [%s] can.",
            donor, donor));
  }
}