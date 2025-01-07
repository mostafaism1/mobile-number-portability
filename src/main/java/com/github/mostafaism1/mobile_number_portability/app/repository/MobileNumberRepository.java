package com.github.mostafaism1.mobile_number_portability.app.repository;

import java.util.Optional;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;

public interface MobileNumberRepository {

  public Optional<MobileNumber> getMobileNumberByNumber(String number);

}
