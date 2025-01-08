package com.github.mostafaism1.mobile_number_portability.infrastructure.http;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.mostafaism1.mobile_number_portability.app.dto.PortRequestDTO;
import com.github.mostafaism1.mobile_number_portability.app.request.CreatePortRequestCommand;
import com.github.mostafaism1.mobile_number_portability.app.request.UpdatePortRequestStateCommand;
import com.github.mostafaism1.mobile_number_portability.app.usecase.CreatePortRequestUseCase;
import com.github.mostafaism1.mobile_number_portability.app.usecase.ListPortRequestsUseCase;
import com.github.mostafaism1.mobile_number_portability.app.usecase.UpdatePortRequestStateUseCase;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/port-requests")
@AllArgsConstructor
public class PortRequestController {

  private final CreatePortRequestUseCase createPortRequestUseCase;
  private final ListPortRequestsUseCase listPortRequestsUseCase;
  private final UpdatePortRequestStateUseCase updatePortRequestStatusUseCase;

  @PostMapping
  public PortRequestDTO createPortRequest(
      @RequestBody CreatePortRequestCommand createPortRequestCommand) {
    return createPortRequestUseCase.create(createPortRequestCommand);
  }

  @GetMapping
  public List<PortRequestDTO> getPortRequests() {
    return listPortRequestsUseCase.list();
  }

  @PatchMapping("/{id}")
  public PortRequestDTO updatePortRequest(@RequestBody UpdateRequestBody updateRequestBody,
      @PathVariable("id") Long id) {
    return updatePortRequestStatusUseCase
        .update(new UpdatePortRequestStateCommand(id, updateRequestBody.state));
  }

  private static record UpdateRequestBody(String state) {
  }


}
