package com.github.mostafaism1.mobile_number_portability.app.usecase;

import java.util.List;
import java.util.stream.Collectors;
import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListPortRequestsUseCase {

  private final PortRequestRepository portRequestRepository;

  public List<PortRequestDTO> list() {
    return portRequestRepository.getAll().stream().map(PortRequestDTO::fromModel)
        .collect(Collectors.toList());
  }

}
