package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.MobileNumberEntity;

public interface SpringDataMobileNumberRepository extends CrudRepository<MobileNumberEntity, Long> {

  Optional<MobileNumberEntity> getByNumber(String number);

}
