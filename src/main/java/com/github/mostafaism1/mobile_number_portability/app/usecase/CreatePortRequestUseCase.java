package com.github.mostafaism1.mobile_number_portability.app.usecase;

import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.exception.IllegalRecipientException;
import com.github.mostafaism1.mobile_number_portability.app.exception.InvalidMobileNumberException;
import com.github.mostafaism1.mobile_number_portability.app.exception.InvalidOperatorException;
import com.github.mostafaism1.mobile_number_portability.app.exception.PortRequestConflictException;
import com.github.mostafaism1.mobile_number_portability.app.exception.UnAuthorizedCreateRequestException;
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

}
