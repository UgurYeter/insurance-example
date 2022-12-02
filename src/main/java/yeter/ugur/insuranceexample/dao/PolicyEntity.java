package yeter.ugur.insuranceexample.dao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "policy")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "insuredPersons")
public class PolicyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "policy_insured_person",
            joinColumns = @JoinColumn(name = "policy_internal_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    @Builder.Default
    private List<InsuredPersonEntity> insuredPersons = new ArrayList<>();

    public void addPersons(List<InsuredPersonEntity> insuredPersons) {
        insuredPersons.forEach(insuredPerson -> {
                    this.insuredPersons.add(insuredPerson);
                    insuredPerson.addPolicy(this);
                });
    }
}
