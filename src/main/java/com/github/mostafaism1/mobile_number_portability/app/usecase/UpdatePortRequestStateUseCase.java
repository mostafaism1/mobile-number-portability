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
    validateAuthorized(command);
    final PortRequest portRequest = mapUpdateCommandToModel(command);
    final PortRequest updatedPortRequest =
        portRequest.transition(PortRequest.States.valueOf(command.transitionState().toUpperCase()));
    final PortRequest result = portRequestRepository.update(updatedPortRequest);
    if (command.transitionState().equalsIgnoreCase(PortRequest.States.ACCEPTED.toString()))
      cancelPendingRequestsForNumber(portRequest.mobileNumber().number());
    return PortRequestDTO.fromModel(result);
  }

  private void validateAuthorized(UpdatePortRequestStateCommand command) {
    final String recipient = portRequestRepository.getById(command.id()).get().recipient().name();
    final boolean isAuthorized = command.requestedBy().equalsIgnoreCase(recipient);
    if (!isAuthorized)
      throw new UnAuthorizedUpdateRequestException(command.requestedBy(), recipient);
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
    private UnAuthorizedUpdateRequestException(String requestedBy, String recipient) {
      super(String.format("[%s] cannot update a port request on behalf of [%s].", requestedBy,
          recipient));
    }
  }
}
