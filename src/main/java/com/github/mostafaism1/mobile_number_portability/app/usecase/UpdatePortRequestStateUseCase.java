package com.github.mostafaism1.mobile_number_portability.app.usecase;

import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.app.request.UpdatePortRequestStateCommand;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePortRequestStateUseCase {

  private final PortRequestRepository portRequestRepository;

  public PortRequestDTO update(UpdatePortRequestStateCommand command) {
    final PortRequest portRequest = mapUpdateCommandToModel(command);
    final PortRequest updatedPortRequest =
        portRequest.transition(PortRequest.State.valueOf(command.transitionState().toUpperCase()));
    final PortRequest result = portRequestRepository.update(updatedPortRequest);
    if (command.transitionState().equalsIgnoreCase(PortRequest.State.ACCEPTED.toString()))
      cancelPendingRequestsForNumber(portRequest);
    return PortRequestDTO.fromModel(result);
  }

  private PortRequest mapUpdateCommandToModel(UpdatePortRequestStateCommand command) {
    final PortRequest portRequest = portRequestRepository.getById(command.id())
        .orElseThrow(() -> new InvalidRequestIdException(command.id()));
    return portRequest;
  }

  private void cancelPendingRequestsForNumber(final PortRequest portRequest) {
    portRequestRepository.getPendingByNumber(portRequest.mobileNumber().number()).stream()
        .map(r -> r.transition(PortRequest.State.CANCELED))
        .forEach(r -> portRequestRepository.update(r));
  }

  static class InvalidRequestIdException extends RuntimeException {
    private InvalidRequestIdException(Long id) {
      super(String.format("[%d] is not a valid Port Request ID.", id));
    }
  }
}
