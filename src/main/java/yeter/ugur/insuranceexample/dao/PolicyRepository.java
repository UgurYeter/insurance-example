package yeter.ugur.insuranceexample.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Integer> {

  Optional<PolicyEntity> findByExternalId(String externalId);
}
