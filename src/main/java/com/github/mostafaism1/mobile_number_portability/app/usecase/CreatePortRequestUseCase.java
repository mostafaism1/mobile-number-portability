package com.github.mostafaism1.mobile_number_portability.app.usecase;

import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.repository.MobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.app.repository.OperatorRepository;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.app.request.CreatePortRequestCommand;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;
import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePortRequestUseCase {

  private final OperatorRepository operatorRepository;
  private final MobileNumberRepository mobileNumberRepository;
  private final PortRequestRepository portRequestRepository;

  public PortRequestDTO create(CreatePortRequestCommand command) {
    final PortRequest portRequest = mapCreateCommandToModel(command);
    validateRequest(portRequest);
    validateAuthroized(command);
    final PortRequest result = portRequestRepository.create(portRequest);
    return PortRequestDTO.fromModel(result);
  }

  private void validateAuthroized(CreatePortRequestCommand command) {
    final boolean isAuthorized = command.requestedBy().equalsIgnoreCase(command.recipient());
    if (!isAuthorized)
      throw new UnAuthorizedCreateRequestException(command.requestedBy(), command.recipient());
  }

  private PortRequest mapCreateCommandToModel(CreatePortRequestCommand command) {
    final MobileNumber mobileNumber =
        mobileNumberRepository.getMobileNumberByNumber(command.number())
            .orElseThrow(() -> new InvalidMobileNumberException(command.number()));
    final Operator donor = operatorRepository.getOperatorByName(command.donor())
        .orElseThrow(() -> new InvalidOperatorException(command.donor()));
    final Operator recipient = operatorRepository.getOperatorByName(command.recipient())
        .orElseThrow(() -> new InvalidOperatorException(command.recipient()));
    return new PortRequest(null, mobileNumber, donor, recipient, null, PortRequest.States.PENDING);
  }

  private void validateRequest(PortRequest portRequest) {
    if (recipientIsDonor(portRequest))
      throw new IllegalRecipientException(portRequest.mobileNumber().number(),
          portRequest.recipient().name());

    if (numberHasAPendingRequest(portRequest.mobileNumber().number()))
      throw new PortRequestConflictException(portRequest.mobileNumber().number());
  }

  private boolean numberHasAPendingRequest(String number) {
    return !portRequestRepository.getPendingByNumber(number).isEmpty();
  }

  private boolean recipientIsDonor(PortRequest portRequest) {
    return portRequest.recipient().equals(portRequest.donor());
  }

  public static class InvalidMobileNumberException extends RuntimeException {
    private InvalidMobileNumberException(String number) {
      super(String.format("[%s] is not a valid mobile number.", number));
    }
  }

  public static class UnAuthorizedCreateRequestException extends RuntimeException {
    private UnAuthorizedCreateRequestException(String requestedBy, String recipient) {
      super(String.format("[%s] cannot create a port request on behalf of [%s].", requestedBy,
          recipient));
    }
  }

  public static class InvalidOperatorException extends RuntimeException {
    private InvalidOperatorException(String operatorName) {
      super(String.format("[%s] is not a valid operator.", operatorName));
    }
  }

  public static class IllegalRecipientException extends RuntimeException {

    private IllegalRecipientException(String number, String operator) {
      super(String.format("Mobile number [%s] is already assigned to [%s]", number, operator));
    }
  }

  public static class PortRequestConflictException extends RuntimeException {

    private PortRequestConflictException(String number) {
      super(String.format(
          "Cannot create request for number [%s] because it has another pending request.", number));
    }
  }

}
