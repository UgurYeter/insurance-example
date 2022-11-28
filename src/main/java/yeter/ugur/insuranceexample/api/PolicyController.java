package yeter.ugur.insuranceexample.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yeter.ugur.insuranceexample.AppConfig;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.api.information.PolicyInformationResponseDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.service.PolicyManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        if (!startDate.isAfter(LocalDate.now())) {
            throw new PolicyDateException("Policy start date can only be in future!");
        }
        return policyManager.createPolicy(policyCreationRequestDto);
    }

    @PutMapping("/modify")
    public PolicyModificationResponseDto modifyPolicy(@RequestBody PolicyModificationRequestDto policyModificationRequestDto) {
        LocalDate effectiveDate = policyModificationRequestDto.getEffectiveDate();
        if (!effectiveDate.isAfter(LocalDate.now())) {
            throw new PolicyDateException("Policy effectiveDate date can only be in future!");
        }
        return policyManager.modifyPolicy(policyModificationRequestDto);
    }

    @GetMapping("/{policyId}")
    public PolicyInformationResponseDto requestPolicy(@PathVariable("policyId") String policyId,
                                                      @RequestParam(required = false)
                                                      String requestDate) {
        LocalDate requestLocalDate = LocalDate.now();
        if (requestDate != null) {
            requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern(AppConfig.DATE_FORMAT));
        }
        log.debug("policyId:{} and requestDate:{}", policyId, requestDate);
        return policyManager.getPolicy(policyId, requestLocalDate);
    }
}
