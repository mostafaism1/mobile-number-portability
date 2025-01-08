package com.github.mostafaism1.mobile_number_portability.infrastructure.http;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.github.mostafaism1.mobile_number_portability.app.exception.IllegalRecipientException;
import com.github.mostafaism1.mobile_number_portability.app.exception.InvalidMobileNumberException;
import com.github.mostafaism1.mobile_number_portability.app.exception.InvalidOperatorException;
import com.github.mostafaism1.mobile_number_portability.app.exception.InvalidRequestIdException;
import com.github.mostafaism1.mobile_number_portability.app.exception.PortRequestConflictException;
import com.github.mostafaism1.mobile_number_portability.app.exception.UnAuthorizedCreateRequestException;
import com.github.mostafaism1.mobile_number_portability.app.exception.UnAuthorizedUpdateRequestException;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest.IllegalRequestStateTransitionException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({UnAuthorizedCreateRequestException.class,
      UnAuthorizedUpdateRequestException.class})
  public ResponseEntity<Object> handleUnAuthorizationExceptions(
      UnAuthorizedCreateRequestException ex) {
    final Map<String, Object> body =
        Map.of("message", ex.getMessage(), "status", HttpStatus.FORBIDDEN);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
  }

  @ExceptionHandler({IllegalRequestStateTransitionException.class,
      InvalidMobileNumberException.class, InvalidOperatorException.class,
      IllegalRecipientException.class, InvalidRequestIdException.class,
      PortRequestConflictException.class})
  public ResponseEntity<Object> handleBadRequests(RuntimeException ex) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;
    final Map<String, Object> body = Map.of("message", ex.getMessage(), "status", status);
    return ResponseEntity.status(status).body(body);
  }

}
