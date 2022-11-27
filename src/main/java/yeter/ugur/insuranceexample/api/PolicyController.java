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
  public PolicyCreationResponseDto createPolicy(@RequestBody PolicyCreationRequestDto policyCreationRequestDto) {
    log.info("policyCreationRequestDto:{}", policyCreationRequestDto);
    return policyManager.createPolicy(policyCreationRequestDto);
  }
}
