package com.github.mostafaism1.mobile_number_portability.infrastructure.http;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

  private static final String AUTH_HEADER = "organization";

  private final CreatePortRequestUseCase createPortRequestUseCase;
  private final ListPortRequestsUseCase listPortRequestsUseCase;
  private final UpdatePortRequestStateUseCase updatePortRequestStatusUseCase;

  @PostMapping
  @ResponseStatus(code = HttpStatus.OK)
  public PortRequestDTO createPortRequest(@RequestBody CreatePortRequestBody createPortRequestBody,
      @RequestHeader(AUTH_HEADER) String organization) {
    final CreatePortRequestCommand command =
        new CreatePortRequestCommand(createPortRequestBody.number(), createPortRequestBody.donor(),
            createPortRequestBody.recipient(), organization);
    return createPortRequestUseCase.create(command);
  }

  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  public List<PortRequestDTO> getPortRequests(@RequestHeader(AUTH_HEADER) String organization) {
    return listPortRequestsUseCase.list(organization);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public PortRequestDTO updatePortRequest(@RequestBody UpdateRequestBody updateRequestBody,
      @RequestHeader(AUTH_HEADER) String organization, @PathVariable("id") Long id) {
    final UpdatePortRequestStateCommand command =
        new UpdatePortRequestStateCommand(id, updateRequestBody.state, organization);
    return updatePortRequestStatusUseCase.update(command);
  }

  public record CreatePortRequestBody(String number, String donor, String recipient) {
  }

  private static record UpdateRequestBody(String state) {
  }


}
