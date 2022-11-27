package yeter.ugur.insuranceexample.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yeter.ugur.insuranceexample.service.PolicyManager;

@RestController
@RequestMapping("/policies")
@Slf4j
public class PolicyController {

  private final PolicyManager policyManager;

  public PolicyController(PolicyManager policyManager) {
    this.policyManager = policyManager;
  }

  @PostMapping
  public PolicyCreationResponse createPolicy(@RequestBody PolicyCreationRequest policyCreationRequest) {
    log.info("policyCreationRequestDto:{}", policyCreationRequest);
    return policyManager.createPolicy(policyCreationRequest);
  }
}
