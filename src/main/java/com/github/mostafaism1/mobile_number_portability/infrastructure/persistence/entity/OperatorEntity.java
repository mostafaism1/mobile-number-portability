package com.github.mostafaism1.mobile_number_portability.infrastructure.persistence.entity;

import com.github.mostafaism1.mobile_number_portability.domain.model.Operator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operator")
@AllArgsConstructor
@NoArgsConstructor
public class OperatorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  @NotBlank
  private String name;

  public Operator toModel() {
    return new Operator(this.id, this.name);
  }

  public static OperatorEntity fromModel(Operator operator) {
    return new OperatorEntity(operator.id(), operator.name());
  }
}
