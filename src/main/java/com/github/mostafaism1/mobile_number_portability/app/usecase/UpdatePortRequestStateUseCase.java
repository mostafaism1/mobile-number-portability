package com.github.mostafaism1.mobile_number_portability.app.usecase;

import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.app.request.UpdatePortRequestStateCommand;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest.States;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePortRequestStateUseCase {

  private final PortRequestRepository portRequestRepository;

  public PortRequestDTO update(UpdatePortRequestStateCommand command) {
    final PortRequest portRequest = mapUpdateCommandToModel(command);
    validateAuthorized(command, portRequest);
    final PortRequest updatedPortRequest =
        portRequest.transition(PortRequest.States.valueOf(command.transitionState().toUpperCase()));
    final PortRequest result = portRequestRepository.update(updatedPortRequest);
    if (command.transitionState().equalsIgnoreCase(PortRequest.States.ACCEPTED.toString()))
      cancelPendingRequestsForNumber(portRequest.mobileNumber().number());
    return PortRequestDTO.fromModel(result);
  }

  private void validateAuthorized(UpdatePortRequestStateCommand command, PortRequest portRequest) {
    final boolean isAuthorized = command.requestedBy().equalsIgnoreCase(portRequest.donor().name());
    if (!isAuthorized)
      throw new UnAuthorizedUpdateRequestException(command.requestedBy(),
          portRequest.donor().name(), portRequest.id());
  }

  private PortRequest mapUpdateCommandToModel(UpdatePortRequestStateCommand command) {
    final PortRequest portRequest = portRequestRepository.getById(command.id())
        .orElseThrow(() -> new InvalidRequestIdException(command.id()));
    return portRequest;
  }

  private void cancelPendingRequestsForNumber(String number) {
    portRequestRepository.batchUpdateStateByMobileNumber(States.PENDING, States.CANCELED, number);
  }

  public static class InvalidRequestIdException extends RuntimeException {
    private InvalidRequestIdException(Long id) {
      super(String.format("[%d] is not a valid Port Request ID.", id));
    }
  }

  public static class UnAuthorizedUpdateRequestException extends RuntimeException {
    private UnAuthorizedUpdateRequestException(String requestedBy, String donor, Long requestId) {
      super(
          String.format("%s cannot change the state of port request [%s], only the donor [%s] can.",
              donor, donor));
    }
  }
}
