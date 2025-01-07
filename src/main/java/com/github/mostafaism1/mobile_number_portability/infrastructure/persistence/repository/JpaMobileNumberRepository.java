package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository;

import java.util.Optional;
import com.github.mostafaism1.mobile_number_portability.app.repository.MobileNumberRepository;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.MobileNumberEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JpaMobileNumberRepository implements MobileNumberRepository {

  private final SpringDataMobileNumberRepository springDataMobileNumberRepository;

  @Override
  public Optional<MobileNumber> getMobileNumberByNumber(String number) {
    return springDataMobileNumberRepository.getByNumber(number).map(MobileNumberEntity::toModel);
  }

}
