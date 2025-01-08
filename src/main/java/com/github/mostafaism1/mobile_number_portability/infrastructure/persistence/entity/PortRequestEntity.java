package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity;

import java.time.Instant;
import com.github.mostafaism1.mobile_number_portability.domain.model.PortRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "port_request")
@NoArgsConstructor
@AllArgsConstructor
public class PortRequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "mobile_number_id")
  private MobileNumberEntity mobileNumber;

  @ManyToOne
  @JoinColumn(name = "donor_operator_id")
  private OperatorEntity donor;

  @ManyToOne
  @JoinColumn(name = "recipient_operator_id")
  private OperatorEntity recipient;

  @Column(name = "created_at")
  @NotNull
  private Instant createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  @NotNull(message = "port_request.state is required.")
  private States state;

  public static enum States {
    PENDING, CANCELED, REJECTED, ACCEPTED;

    public PortRequest.States toModel() {
      return PortRequest.States.valueOf(this.toString());
    }

    public static PortRequestEntity.States fromModel(PortRequest.States state) {
      return PortRequestEntity.States.valueOf(state.toString());
    }
  }

  @PrePersist
  public void onPrePersist() {
    createdAt = Instant.now();
  }

  public PortRequest toModel() {
    return new PortRequest(this.id, this.mobileNumber.toModel(), this.donor.toModel(),
        this.recipient.toModel(), this.createdAt, this.state.toModel());
  }

  public static PortRequestEntity fromModel(PortRequest portRequest) {
    return new PortRequestEntity(portRequest.id(),
        MobileNumberEntity.fromModel(portRequest.mobileNumber()),
        OperatorEntity.fromModel(portRequest.donor()),
        OperatorEntity.fromModel(portRequest.recipient()), portRequest.createdAt(),
        States.valueOf(portRequest.state().toString()));
  }

}
