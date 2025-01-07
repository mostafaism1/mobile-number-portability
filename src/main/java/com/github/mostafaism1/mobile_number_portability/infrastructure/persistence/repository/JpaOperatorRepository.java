package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository;

import java.util.Optional;
import com.github.mostafaism1.mobile_number_portability.app.repository.OperatorRepository;
import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.OperatorEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JpaOperatorRepository implements OperatorRepository {

  private final SpringDataOperatorRepository springDataOperatorRepository;

  @Override
  public Optional<Operator> getOperatorByName(String operatorName) {
    return springDataOperatorRepository.getByName(operatorName).map(OperatorEntity::toModel);
  }
  
}
