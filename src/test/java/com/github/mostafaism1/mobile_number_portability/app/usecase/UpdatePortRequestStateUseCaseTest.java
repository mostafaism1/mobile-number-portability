package com.github.mostafaism1.mobile_number_portability.app.usecase;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.app.request.UpdatePortRequestStateCommand;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;
import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UpdatePortRequestStateUseCaseTest {

  @Mock
  private PortRequestRepository portRequestRepository;

  @InjectMocks
  private UpdatePortRequestStateUseCase updatePortRequestStateUseCase;

  private UpdatePortRequestStateCommand updatePortRequestStateCommand;
  private Operator donor;
  private Operator recipient;
  private MobileNumber mobileNumber;
  private PortRequest portRequest;
  private PortRequest expectedResponse;

  @BeforeEach
  void setup() {
    donor = new Operator(0L, "vodafone");;
    recipient = new Operator(1L, "orange");
    mobileNumber = new MobileNumber(0L, "01012345678", donor);
  }


  @Test
  void givenIdOfAPendingRequestAndAcceptedState_thenShouldTransition() {
    // Given
    portRequest = new PortRequest(0L, mobileNumber, donor, recipient, Instant.now(),
        PortRequest.States.PENDING);
    given(portRequestRepository.getById(0L)).willReturn(Optional.of(portRequest));
    updatePortRequestStateCommand = new UpdatePortRequestStateCommand(0L, "accepted", "orange");
    expectedResponse = new PortRequest(0L, mobileNumber, donor, recipient, portRequest.createdAt(),
        PortRequest.States.ACCEPTED);
    given(portRequestRepository.update(Mockito.any(PortRequest.class)))
        .willReturn(expectedResponse);
    ArgumentCaptor<PortRequest> portRequestCaptor = ArgumentCaptor.forClass(PortRequest.class);

    // When.
    updatePortRequestStateUseCase.update(updatePortRequestStateCommand);

    // Then.
    BDDMockito.then(portRequestRepository).should().update(portRequestCaptor.capture());
    then(portRequestCaptor.getValue().mobileNumber()).isEqualTo(portRequest.mobileNumber());
    then(portRequestCaptor.getValue().donor()).isEqualTo(portRequest.donor());
    then(portRequestCaptor.getValue().recipient()).isEqualTo(portRequest.recipient());
    then(portRequestCaptor.getValue().state()).isEqualTo(PortRequest.States.ACCEPTED);
  }

  @Test
  void givenIdOfAPendingRequestAndRejectedState_thenShouldTransition() {
    // Given
    portRequest = new PortRequest(0L, mobileNumber, donor, recipient, Instant.now(),
        PortRequest.States.PENDING);
    given(portRequestRepository.getById(0L)).willReturn(Optional.of(portRequest));
    updatePortRequestStateCommand = new UpdatePortRequestStateCommand(0L, "rejected", "orange");
    expectedResponse = new PortRequest(0L, mobileNumber, donor, recipient, portRequest.createdAt(),
        PortRequest.States.REJECTED);
    given(portRequestRepository.update(Mockito.any(PortRequest.class)))
        .willReturn(expectedResponse);
    ArgumentCaptor<PortRequest> portRequestCaptor = ArgumentCaptor.forClass(PortRequest.class);

    // When.
    updatePortRequestStateUseCase.update(updatePortRequestStateCommand);

    // Then.
    BDDMockito.then(portRequestRepository).should().update(portRequestCaptor.capture());
    then(portRequestCaptor.getValue().mobileNumber()).isEqualTo(portRequest.mobileNumber());
    then(portRequestCaptor.getValue().donor()).isEqualTo(portRequest.donor());
    then(portRequestCaptor.getValue().recipient()).isEqualTo(portRequest.recipient());
    then(portRequestCaptor.getValue().state()).isEqualTo(PortRequest.States.REJECTED);
  }

  @Test
  void givenIdOfAPendingRequestAndCanceledState_thenShouldTransition() {
    // Given
    portRequest = new PortRequest(0L, mobileNumber, donor, recipient, Instant.now(),
        PortRequest.States.PENDING);
    given(portRequestRepository.getById(0L)).willReturn(Optional.of(portRequest));
    updatePortRequestStateCommand = new UpdatePortRequestStateCommand(0L, "canceled", "orange");
    expectedResponse = new PortRequest(0L, mobileNumber, donor, recipient, portRequest.createdAt(),
        PortRequest.States.CANCELED);
    given(portRequestRepository.update(Mockito.any(PortRequest.class)))
        .willReturn(expectedResponse);
    ArgumentCaptor<PortRequest> portRequestCaptor = ArgumentCaptor.forClass(PortRequest.class);

    // When.
    updatePortRequestStateUseCase.update(updatePortRequestStateCommand);

    // Then.
    BDDMockito.then(portRequestRepository).should().update(portRequestCaptor.capture());
    then(portRequestCaptor.getValue().mobileNumber()).isEqualTo(portRequest.mobileNumber());
    then(portRequestCaptor.getValue().donor()).isEqualTo(portRequest.donor());
    then(portRequestCaptor.getValue().recipient()).isEqualTo(portRequest.recipient());
    then(portRequestCaptor.getValue().state()).isEqualTo(PortRequest.States.CANCELED);
  }

  // TODO: write test cases for unhappy paths.

}
