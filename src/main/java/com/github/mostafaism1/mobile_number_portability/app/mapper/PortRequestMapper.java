package com.github.mostafaism1.mobile_number_portability.app.mapper;

import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.request.CreatePortRequestCommand;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;
import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import com.github.mostafaism1.mobile_number_portability.repository.MobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.repository.OperatorRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PortRequestMapper {

  private final OperatorRepository operatorRepository;
  private final MobileNumberRepository mobileNumberRepository;

  public PortRequest mapCreateCommandToModel(CreatePortRequestCommand command) {
    final MobileNumber mobileNumber =
        mobileNumberRepository.getMobileNumberByNumber(command.number())
            .orElseThrow(() -> new InvalidMobileNumberException(command.number()));
    final Operator donor = operatorRepository.getOperatorByName(command.donor())
        .orElseThrow(() -> new InvalidOperatorException(command.donor()));
    final Operator recipient = operatorRepository.getOperatorByName(command.recipient())
        .orElseThrow(() -> new InvalidOperatorException(command.recipient()));
    return new PortRequest(null, mobileNumber, donor, recipient, null, PortRequest.State.PENDING);
  }


  public PortRequestDTO mapModelToDto(PortRequest portRequest) {
    return new PortRequestDTO(portRequest.id(), portRequest.mobileNumber().number(),
        portRequest.donor().name(), portRequest.recipient().name(), portRequest.state().toString());
  }

  public static class InvalidMobileNumberException extends RuntimeException {
    private InvalidMobileNumberException(String number) {
      super(String.format("[%s] is not a valid mobile number.", number));
    }

  }

  public static class InvalidOperatorException extends RuntimeException {
    private InvalidOperatorException(String operatorName) {
      super(String.format("[%s] is not a valid operator.", operatorName));
    }
  }

}
