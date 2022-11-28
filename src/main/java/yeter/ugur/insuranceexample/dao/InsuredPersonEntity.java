package yeter.ugur.insuranceexample.dao;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "policies")
@Entity
@Table(name = "insured_person")
public class InsuredPersonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nonnull
    private String firstName;

    @Nonnull
    private String secondName;

    @Nonnull
    private BigDecimal premium;

    @Setter
    @ManyToMany(mappedBy = "insuredPersons")
    @Builder.Default
    private List<PolicyEntity> policies = new ArrayList<>();

    public void addPolicy(PolicyEntity policyEntity){
        this.policies.add(policyEntity);
    }
}
