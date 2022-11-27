package yeter.ugur.insuranceexample.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yeter.ugur.insuranceexample.AppConfig;
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
            throw new PolicyCreationException("Policy start date can only be in future!");
        }
        return policyManager.createPolicy(policyCreationRequestDto);
    }

    @PutMapping("/modify")
    public PolicyModificationResponseDto modifyPolicy(@RequestBody PolicyModificationRequestDto policyModificationRequestDto) {
        LocalDate effectiveDate = policyModificationRequestDto.getEffectiveDate();
        if (!effectiveDate.isAfter(LocalDate.now())) {
            throw new PolicyCreationException("Policy effectiveDate date can only be in future!");
        }
        return policyManager.modifyPolicy(policyModificationRequestDto);
    }

    @GetMapping("/{policyId}")
    public void requestPolicy(@PathVariable("policyId") String policyId,
                              @RequestParam(required = false)
                              String requestDate) {
        LocalDate requestLocalDate = LocalDate.now();
        if (requestDate != null) {
            requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern(AppConfig.DATE_FORMAT));
        }
        log.debug("policyId:{} and requestDate:{}", policyId, requestDate);

    }
}
