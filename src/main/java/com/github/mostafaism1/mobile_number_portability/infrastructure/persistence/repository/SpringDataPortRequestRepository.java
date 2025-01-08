package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.PortRequestEntity;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.PortRequestEntity.States;;

@Repository
public interface SpringDataPortRequestRepository extends CrudRepository<PortRequestEntity, Long> {

  List<PortRequestEntity> getByMobileNumberNumberAndState(String number, States state);

  @Transactional
  @Modifying
  @Query(value = "UPDATE PortRequestEntity SET state = :newState WHERE state = :matchingState")
  void batchUpdateStateByMobileNumber(@Param("matchingState") States matchingState,
      @Param("newState") States newState, String number);


  @Transactional
  @Modifying
  @Query(value = "UPDATE PortRequestEntity SET state = :newState WHERE createdAt < :instant")
  void batchUpdateStateByCreatedAtBefore(@Param("newState") States newState, Instant instant);

}
