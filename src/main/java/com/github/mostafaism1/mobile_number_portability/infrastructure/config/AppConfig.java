package com.github.mostafaism1.mobile_number_portability.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.github.mostafaism1.mobile_number_portability.app.repository.MobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.app.repository.OperatorRepository;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CancelTimedOutPortRequestsUseCase;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CreatePortRequestUseCase;
import com.github.mostafaism1.mobile_number_portability.app.usecase.ListPortRequestsUseCase;
import com.github.mostafaism1.mobile_number_portability.app.usecase.UpdatePortRequestStateUseCase;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository.JpaMobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository.JpaOperatorRepository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository.JpaPortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository.SpringDataMobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository.SpringDataOperatorRepository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository.SpringDataPortRequestRepository;

@Configuration
@EnableScheduling
public class AppConfig {

  @Bean
  public OperatorRepository getOperatorRepository(
      SpringDataOperatorRepository springDataOperatorRepository) {
    return new JpaOperatorRepository(springDataOperatorRepository);
  }

  @Bean
  public MobileNumberRepository getMobileNumberRepository(
      SpringDataMobileNumberRepository springDataMobileNumberRepository) {
    return new JpaMobileNumberRepository(springDataMobileNumberRepository);
  }

  @Bean
  public PortRequestRepository getPortRequestRepository(
      SpringDataPortRequestRepository springDataPortRequestRepository) {
    return new JpaPortRequestRepository(springDataPortRequestRepository);
  }

  @Bean
  public CreatePortRequestUseCase getCreatePortRequestUseCase(OperatorRepository operatorRepository,
      MobileNumberRepository mobileNumberRepository, PortRequestRepository portRequestRepository) {
    return new CreatePortRequestUseCase(operatorRepository, mobileNumberRepository,
        portRequestRepository);
  }

  @Bean
  public ListPortRequestsUseCase getListPortRequestsUseCase(
      PortRequestRepository portRequestRepository) {
    return new ListPortRequestsUseCase(portRequestRepository);
  }

  @Bean
  public UpdatePortRequestStateUseCase getUpdatePortRequestStateUseCase(
      PortRequestRepository portRequestRepository) {
    return new UpdatePortRequestStateUseCase(portRequestRepository);
  }

  @Bean
  public CancelTimedOutPortRequestsUseCase getCancelTimedOutPortRequestsUseCase(
      PortRequestRepository portRequestRepository) {
    return new CancelTimedOutPortRequestsUseCase(portRequestRepository);
  }

}

