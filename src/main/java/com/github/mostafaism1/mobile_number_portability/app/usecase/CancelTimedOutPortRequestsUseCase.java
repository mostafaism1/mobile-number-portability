package com.github.mostafaism1.mobile_number_portability.app.usecase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CancelTimedOutPortRequestsUseCase {

  // TODO: Change to inject value as a config prop using @Value.
  public static long TIME_OUT_IN_SECONDS = 10;

  private final PortRequestRepository portRequestRepository;

  public void cancel() {
    portRequestRepository.batchUpdateStateByCreatedAtBefore(PortRequest.States.CANCELED,
        Instant.now().minus(TIME_OUT_IN_SECONDS, ChronoUnit.SECONDS));
  }

}
