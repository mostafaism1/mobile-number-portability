package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.github.mostafaism1.mobile_number_portability.app.repository.PortRequestRepository;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity.PortRequestEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JpaPortRequestRepository implements PortRequestRepository {

  private final SpringDataPortRequestRepository springDataPortRequestRepository;


  @Override
  public PortRequest create(PortRequest portRequest) {
    final PortRequestEntity portRequestEntity = PortRequestEntity.fromModel(portRequest);
    final PortRequestEntity result = springDataPortRequestRepository.save(portRequestEntity);
    return result.toModel();
  }

  @Override
  public Optional<PortRequest> getById(long id) {
    return springDataPortRequestRepository.findById(id).map(PortRequestEntity::toModel);
  }

  @Override
  public List<PortRequest> getPendingByNumber(String number) {
    return springDataPortRequestRepository
        .getByMobileNumberNumberAndState(number, PortRequestEntity.States.PENDING).stream()
        .map(PortRequestEntity::toModel).collect(Collectors.toList());
  }

  @Override
  public List<PortRequest> getAll() {
    return StreamSupport.stream(springDataPortRequestRepository.findAll().spliterator(), false)
        .map(PortRequestEntity::toModel).collect(Collectors.toList());
  }

  @Override
  public PortRequest update(PortRequest portRequest) {
    final PortRequestEntity mnpRequestEntity = PortRequestEntity.fromModel(portRequest);
    final PortRequestEntity result = springDataPortRequestRepository.save(mnpRequestEntity);
    return result.toModel();
  }

  @Override
  public void batchUpdateStateByMobileNumber(PortRequest.States matchingState,
      PortRequest.States newState, String number) {
    final PortRequestEntity.States matchingEntityState =
        PortRequestEntity.States.fromModel(matchingState);
    final PortRequestEntity.States newEntityState = PortRequestEntity.States.fromModel(newState);
    springDataPortRequestRepository.batchUpdateStateByMobileNumber(matchingEntityState,
        newEntityState, number);
  }

}
