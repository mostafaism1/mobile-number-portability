package com.github.mostafaism1.mobile_number_portability.app.usecase;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
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
import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.request.CreatePortRequestCommand;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CreatePortRequestUseCase.DuplicateRequestException;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CreatePortRequestUseCase.IllegalRecipientException;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CreatePortRequestUseCase.InvalidMobileNumberException;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CreatePortRequestUseCase.InvalidOperatorException;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;
import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import com.github.mostafaism1.mobile_number_portability.repository.MobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.repository.OperatorRepository;
import com.github.mostafaism1.mobile_number_portability.repository.PortRequestRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreatePortRequestUseCaseTest {

  @Mock
  private PortRequestRepository portRequestRepository;
  @Mock
  private OperatorRepository operatorRepository;
  @Mock
  private MobileNumberRepository mobileNumberRepository;

  @InjectMocks
  private CreatePortRequestUseCase createPortRequestUseCase;

  private CreatePortRequestCommand createPortRequestCommand;
  private Operator donor;
  private Operator recipient;
  private MobileNumber mobileNumber;
  private PortRequest expectedResponse;

  @BeforeEach
  void setup() {
    createPortRequestCommand = new CreatePortRequestCommand("01012345678", "vodafone", "orange");
    donor = new Operator(0L, "vodafone");;
    recipient = new Operator(1L, "orange");
    mobileNumber = new MobileNumber(0L, "01012345678", donor);
    expectedResponse = new PortRequest(0L, mobileNumber, donor, recipient, Instant.now(),
        PortRequest.State.PENDING);
  }

  @Test
  public void givenAValidRequest_thenShouldCreate() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.of(donor));
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.of(recipient));
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.of(mobileNumber));
    given(portRequestRepository.getPendingByNumber("01012345678"))
        .willReturn(Collections.emptyList());
    given(portRequestRepository.create(Mockito.any(PortRequest.class)))
        .willReturn(expectedResponse);
    ArgumentCaptor<PortRequest> portRequestCaptor = ArgumentCaptor.forClass(PortRequest.class);

    // When.
    createPortRequestUseCase.create(createPortRequestCommand);

    // Then.
    BDDMockito.then(portRequestRepository).should().create(portRequestCaptor.capture());
    then(portRequestCaptor.getValue().mobileNumber().number())
        .isEqualTo(createPortRequestCommand.number());
    then(portRequestCaptor.getValue().donor().name()).isEqualTo(createPortRequestCommand.donor());
    then(portRequestCaptor.getValue().recipient().name())
        .isEqualTo(createPortRequestCommand.recipient());
    then(portRequestCaptor.getValue().state()).isEqualTo(PortRequest.State.PENDING);
  }

  @Test
  public void givenAValidRequest_thenShouldReturnAValidResponse() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.of(donor));
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.of(recipient));
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.of(mobileNumber));
    given(portRequestRepository.getPendingByNumber("01012345678"))
        .willReturn(Collections.emptyList());
    given(portRequestRepository.create(Mockito.any(PortRequest.class)))
        .willReturn(expectedResponse);

    // When.
    PortRequestDTO actual = createPortRequestUseCase.create(createPortRequestCommand);

    // Then.
    then(actual.id()).isNotNull();
    then(actual.number()).isEqualTo("01012345678");
    then(actual.donor()).isEqualTo("vodafone");
    then(actual.recipient()).isEqualTo("orange");
    then(actual.state()).isEqualToIgnoringCase(PortRequest.State.PENDING.toString());
  }

  @Test
  void givenAnExistingRequest_thenShouldFail() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.of(donor));
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.of(recipient));
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.of(mobileNumber));
    List<PortRequest> existingPendingRequestsForNumber = List.of(new PortRequest(0L, mobileNumber,
        donor, recipient, Instant.now().minus(1, ChronoUnit.MINUTES), PortRequest.State.PENDING));
    given(portRequestRepository.getPendingByNumber("01012345678"))
        .willReturn(existingPendingRequestsForNumber);

    // When, then.
    thenThrownBy(() -> createPortRequestUseCase.create(createPortRequestCommand))
        .isInstanceOf(DuplicateRequestException.class);
  }

  @Test
  void givenAnInvalidMobileNumber_thenShouldFail() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.of(donor));
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.of(recipient));
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.empty());

    // When, then.
    thenThrownBy(() -> createPortRequestUseCase.create(createPortRequestCommand))
        .isInstanceOf(InvalidMobileNumberException.class);
  }

  @Test
  void givenAnInvalidDonor_thenShouldFail() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.empty());
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.of(recipient));
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.of(mobileNumber));

    // When, then.
    thenThrownBy(() -> createPortRequestUseCase.create(createPortRequestCommand))
        .isInstanceOf(InvalidOperatorException.class);
  }

  @Test
  void givenAnInvalidRecipient_thenShouldFail() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.of(donor));
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.empty());
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.of(mobileNumber));

    // When, then.
    thenThrownBy(() -> createPortRequestUseCase.create(createPortRequestCommand))
        .isInstanceOf(InvalidOperatorException.class);
  }

  @Test
  void givenDonorIsRecipient_thenShouldFail() {
    // Given.
    given(operatorRepository.getOperatorByName("vodafone")).willReturn(Optional.of(donor));
    given(operatorRepository.getOperatorByName("orange")).willReturn(Optional.of(recipient));
    given(mobileNumberRepository.getMobileNumberByNumber("01012345678"))
        .willReturn(Optional.of(mobileNumber));
    createPortRequestCommand = new CreatePortRequestCommand("01012345678", "vodafone", "vodafone");

    // When, then.
    thenThrownBy(() -> createPortRequestUseCase.create(createPortRequestCommand))
        .isInstanceOf(IllegalRecipientException.class);
  }

}
