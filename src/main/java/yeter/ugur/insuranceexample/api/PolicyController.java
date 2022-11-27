package yeter.ugur.insuranceexample.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yeter.ugur.insuranceexample.service.PolicyManager;

import java.time.LocalDate;

@RestController
@RequestMapping("/policies")
@Slf4j
public class PolicyController {

  private final PolicyManager policyManager;

  public PolicyController(PolicyManager policyManager) {
    this.policyManager = policyManager;
  }

  @PostMapping("/create")
  public PolicyCreationResponseDto createPolicy(@RequestBody PolicyCreationRequestDto policyCreationRequestDto) {
    LocalDate startDate = policyCreationRequestDto.getStartDate();
    if(!startDate.isAfter(LocalDate.now())){
      throw new PolicyException("Policy start date can only be in future!");
    }
    return policyManager.createPolicy(policyCreationRequestDto);
  }

  @PostMapping("/modify")
  public void modifyPolicy(@RequestBody PolicyModificationRequestDto policyModificationRequestDto) {
    LocalDate effectiveDate = policyModificationRequestDto.getEffectiveDate();
    if(!effectiveDate.isAfter(LocalDate.now())){
      throw new PolicyException("Policy effectiveDate date can only be in future!");
    }
    policyManager.modifyPolicy(policyModificationRequestDto);
  }
}
