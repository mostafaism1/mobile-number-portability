package com.github.mostafaism1.mobile_number_portability.infrastructure.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CancelTimedOutPortRequestsUseCase;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TimeOutPortRequestsCanceller {

  // TODO: Change to inject value as a config prop using @Value.
  private static final long PERIOD_IN_MIILISECOND = 5000;
  private final CancelTimedOutPortRequestsUseCase cancelTimedOutPortRequestsUseCase;

  @Scheduled(fixedRate = PERIOD_IN_MIILISECOND, initialDelay = PERIOD_IN_MIILISECOND)
  public void execute() {
    cancelTimedOutPortRequestsUseCase.cancel();
    // TODO: Replace print outs with a logger.
    System.out.println("Timed-out requests canceled at: " + java.time.LocalDateTime.now());
  }

}
