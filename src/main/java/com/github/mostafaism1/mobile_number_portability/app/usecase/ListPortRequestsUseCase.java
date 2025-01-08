package com.github.mostafaism1.mobile_number_portability.app.usecase;

import java.util.List;
import java.util.stream.Collectors;
import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.exception.InvalidOperatorException;
import com.github.mostafaism1.mobile_number_portability.app.repository.OperatorRepository;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListPortRequestsUseCase {

  private final OperatorRepository operatorRepository;
  private final PortRequestRepository portRequestRepository;

  public List<PortRequestDTO> list(String requestedBy) {
    validateAuthenticated(requestedBy);
    return portRequestRepository.getAll().stream().filter(r -> canView(requestedBy, r))
        .map(PortRequestDTO::fromModel).collect(Collectors.toList());
  }

  private void validateAuthenticated(String requestedBy) {
    operatorRepository.getOperatorByName(requestedBy)
        .orElseThrow(() -> new InvalidOperatorException(requestedBy));
  }

  private boolean canView(String requestedBy, PortRequest portRequest) {
    return portRequest.state().equals(PortRequest.States.ACCEPTED)
        || requestedBy.equalsIgnoreCase(portRequest.donor().name())
        || requestedBy.equalsIgnoreCase(portRequest.recipient().name());
  }

}
