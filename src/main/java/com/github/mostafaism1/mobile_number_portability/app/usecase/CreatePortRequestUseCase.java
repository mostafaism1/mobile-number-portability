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
    final PortRequest result = portRequestRepository.create(portRequest);
    return PortRequestDTO.fromModel(result);
  }

  private PortRequest mapCreateCommandToModel(CreatePortRequestCommand command) {
    final MobileNumber mobileNumber =
        mobileNumberRepository.getMobileNumberByNumber(command.number())
            .orElseThrow(() -> new InvalidMobileNumberException(command.number()));
    final Operator donor = operatorRepository.getOperatorByName(command.donor())
        .orElseThrow(() -> new InvalidOperatorException(command.donor()));
    final Operator recipient = operatorRepository.getOperatorByName(command.recipient())
        .orElseThrow(() -> new InvalidOperatorException(command.recipient()));
    return new PortRequest(null, mobileNumber, donor, recipient, null, PortRequest.State.PENDING);
  }

  private void validateRequest(PortRequest portRequest) {
    if (recipientIsDonor(portRequest))
      throw new IllegalRecipientException(portRequest.mobileNumber().number(),
          portRequest.recipient().name());

    if (pendingRequestWithSameDetailsExist(portRequest))
      throw new DuplicateRequestException();
  }

  private boolean pendingRequestWithSameDetailsExist(PortRequest portRequest) {
    return portRequestRepository.getPendingByNumber(portRequest.mobileNumber().number()).stream()
        .anyMatch(r -> r.recipient().equals(portRequest.recipient()));
  }

  private boolean recipientIsDonor(PortRequest portRequest) {
    return portRequest.recipient().equals(portRequest.donor());
  }

  static class InvalidMobileNumberException extends RuntimeException {
    private InvalidMobileNumberException(String number) {
      super(String.format("[%s] is not a valid mobile number.", number));
    }
  }

  static class InvalidOperatorException extends RuntimeException {
    private InvalidOperatorException(String operatorName) {
      super(String.format("[%s] is not a valid operator.", operatorName));
    }
  }

  static class IllegalRecipientException extends RuntimeException {

    private IllegalRecipientException(String number, String operator) {
      super(String.format("Mobile number [%s] is already assigned to [%s]", number, operator));
    }
  }

  static class DuplicateRequestException extends RuntimeException {

    private DuplicateRequestException() {
      super("Request already created.");
    }
  }

}
