package yeter.ugur.insuranceexample.dao;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode
@Builder
@Entity
@Table(name = "insured_person")
public class InsuredPersonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Nonnull
    private String firstName;

    @Nonnull
    private String secondName;

    @Nonnull
    private BigDecimal premium;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private PolicyEntity policy;
}
