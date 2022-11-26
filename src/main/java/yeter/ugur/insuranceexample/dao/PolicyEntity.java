package yeter.ugur.insuranceexample.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
    name = "policy",
    indexes =
        @Index(
            name = "idx_on_policy_for_external_id_and_effective_date",
            columnList = "externalId, effectiveDate"))
public class PolicyEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String externalId;

  private Instant effectiveDate;

  private LocalDate createdAt;
}
