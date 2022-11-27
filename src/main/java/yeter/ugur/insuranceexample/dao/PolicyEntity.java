package yeter.ugur.insuranceexample.dao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "policy",
        indexes =
        @Index(
                name = "idx_on_policy_for_external_id_and_start_date",
                columnList = "externalId, startDate"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "insuredPersons")
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
    private LocalDate startDate;

    /**
     * Epoch milli second.
     */
    private long createdAt;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "policy_person",
            joinColumns = @JoinColumn(name = "policy_internal_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    @Builder.Default
    private List<InsuredPersonEntity> insuredPersons = new ArrayList<>();

    private void addPerson(InsuredPersonEntity insuredPerson) {
        insuredPersons.add(insuredPerson);
    }

    public void addPersons(List<InsuredPersonEntity> insuredPersons) {
        insuredPersons.forEach(this::addPerson);
    }
}
