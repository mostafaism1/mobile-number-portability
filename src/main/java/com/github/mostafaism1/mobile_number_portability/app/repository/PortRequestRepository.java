package com.github.mostafaism1.mobile_number_portability.app.repository;

import java.util.List;
import java.util.Optional;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;

public interface PortRequestRepository {

  public PortRequest create(PortRequest portRequest);

  public Optional<PortRequest> getById(long id);

  public List<PortRequest> getPendingByNumber(String number);

  public List<PortRequest> getAll();

  public PortRequest update(PortRequest portRequest);

}
