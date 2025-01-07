package com.github.mostafaism1.mobile_number_portability.app.dto;

import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;

public record PortRequestDTO(long id, String number, String donor, String recipient, String state) {

  public static PortRequestDTO fromModel(PortRequest portRequest) {
    return new PortRequestDTO(portRequest.id(), portRequest.mobileNumber().number(),
        portRequest.donor().name(), portRequest.recipient().name(), portRequest.state().toString());
  }

}
