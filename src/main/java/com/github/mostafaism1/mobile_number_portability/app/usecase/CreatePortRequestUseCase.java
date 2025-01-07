package com.github.mostafaism1.mobile_number_portability.app.usecase;

import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.mapper.PortRequestMapper;
import com.github.mostafaism1.mobile_number_portability.app.request.CreatePortRequestCommand;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import com.github.mostafaism1.mobile_number_portability.repository.MobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.repository.OperatorRepository;
import com.github.mostafaism1.mobile_number_portability.repository.PortRequestRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePortRequestUseCase {

  private final OperatorRepository operatorRepository;
  private final MobileNumberRepository mobileNumberRepository;
  private final PortRequestRepository portRequestRepository;

  public PortRequestDTO create(CreatePortRequestCommand command) {
    final PortRequestMapper portRequestMapper =
        new PortRequestMapper(operatorRepository, mobileNumberRepository);
    final PortRequest portRequest = portRequestMapper.mapCreateCommandToModel(command);
    validateRequest(portRequest);
    final PortRequest result = portRequestRepository.create(portRequest);
    return portRequestMapper.mapModelToDto(result);
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
