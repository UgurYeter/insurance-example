package yeter.ugur.insuranceexample.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Integer> {
    List<PolicyEntity> findByExternalId(String externalId);
    Optional<PolicyEntity> findByExternalIdAndStartDate(String externalId, LocalDate startDate);

}
