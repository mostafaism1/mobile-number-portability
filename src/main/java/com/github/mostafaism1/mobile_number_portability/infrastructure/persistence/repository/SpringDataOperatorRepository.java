package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.OperatorEntity;

@Repository
public interface SpringDataOperatorRepository extends CrudRepository<OperatorEntity, Long> {

  Optional<OperatorEntity> getByName(String name);

}
