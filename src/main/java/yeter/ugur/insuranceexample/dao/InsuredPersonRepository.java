package yeter.ugur.insuranceexample.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuredPersonRepository extends JpaRepository<InsuredPersonEntity, Integer> {
}
