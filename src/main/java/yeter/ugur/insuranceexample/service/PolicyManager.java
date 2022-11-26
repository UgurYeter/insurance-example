package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;

import java.util.Optional;

@Slf4j
@Service
public class PolicyManager {

  private final ExternalPolicyIdCreator externalPolicyIdCreator;

  public PolicyManager(ExternalPolicyIdCreator externalPolicyIdCreator) {
    this.externalPolicyIdCreator = externalPolicyIdCreator;
  }

  public void createPolicy(PolicyCreationRequestDto creationRequestDto) {
      Optional<String> externalId = externalPolicyIdCreator.generatePolicyId();
      if(externalId.isEmpty()){
          log.error("Can't create external policy id!");
          throw new RuntimeException("Can not create policy!");
      }

  }
}
