package yeter.ugur.insuranceexample.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Integer> {

  Optional<PolicyEntity> findByExternalId(String externalId);
}
