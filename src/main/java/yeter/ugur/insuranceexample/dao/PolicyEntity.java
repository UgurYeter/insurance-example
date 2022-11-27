package yeter.ugur.insuranceexample.dao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "policy",
        indexes =
        @Index(
                name = "idx_on_policy_for_external_id_and_effective_date",
                columnList = "externalId, effectiveDate"))
@Builder
@Getter
@EqualsAndHashCode
public class PolicyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Policy id known by outside.
     */
    private String externalId;

    /**
     * The date the policy start effective.
     */
    private LocalDate effectiveDate;

    /**
     * Epoch milli second.
     */
    private long createdAt;


    @OneToMany(
            mappedBy = "policy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InsuredPersonEntity> insuredPersons = new ArrayList<>();


    public void addPerson(InsuredPersonEntity insuredPerson) {
        insuredPersons.add(insuredPerson);
        insuredPerson.setPolicy(this);
    }

    public void removePerson(InsuredPersonEntity insuredPerson) {
        insuredPersons.remove(insuredPerson);
        insuredPerson.setPolicy(null);
    }

}
