package com.github.mostafaism1.mobile_number_portability.repository;

import java.util.Optional;
import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;

public interface OperatorRepository {

  public Optional<Operator> getOperatorByName(String name);

}
