package yeter.ugur.insuranceexample.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Integer> {

    List<PolicyEntity> findByExternalId(String externalId);
}
