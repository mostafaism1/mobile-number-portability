package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity;

import org.hibernate.validator.constraints.Range;
import com.github.mostafaism1.mobile_number_portability.domain.model.MobileNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mobile_number")
@NoArgsConstructor
@AllArgsConstructor
public class MobileNumberEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "number")
  @Pattern(regexp = "^\\d{11}$", message = "Mobile number should be 11 digits long.")
  private String number;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "operator_id")
  private OperatorEntity operator;


  public MobileNumber toModel() {
    return new MobileNumber(this.id, this.number, operator.toModel());
  }

  public static MobileNumberEntity fromModel(MobileNumber mobileNumber) {
    return new MobileNumberEntity(mobileNumber.id(), mobileNumber.number(),
        OperatorEntity.fromModel(mobileNumber.operator()));
  }

}
