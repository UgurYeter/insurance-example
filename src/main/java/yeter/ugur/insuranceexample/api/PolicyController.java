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
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;
import yeter.ugur.insuranceexample.api.information.PolicyInformationResponseDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.service.PolicyCreationService;
import yeter.ugur.insuranceexample.service.PolicyInformationService;
import yeter.ugur.insuranceexample.service.PolicyModificationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/policies")
@Slf4j
public class PolicyController {

    private final PolicyCreationService policyCreationService;
    private final PolicyModificationService policyModificationService;
    private final PolicyInformationService policyInformationService;
    private final PolicyRequestValidator policyRequestValidator;

    public PolicyController(PolicyCreationService policyCreationService,
                            PolicyModificationService policyModificationService,
                            PolicyInformationService policyInformationService, PolicyRequestValidator policyRequestValidator) {
        this.policyCreationService = policyCreationService;
        this.policyModificationService = policyModificationService;
        this.policyInformationService = policyInformationService;
        this.policyRequestValidator = policyRequestValidator;
    }

    @PostMapping("/create")
    public PolicyCreationResponseDto createPolicy(@RequestBody PolicyCreationRequestDto policyCreationRequestDto) {
        policyRequestValidator.verifyCreatePolicyOrThrow(policyCreationRequestDto);
        return policyCreationService.createPolicy(policyCreationRequestDto);
    }

    @PutMapping("/modify")
    public PolicyModificationResponseDto modifyPolicy(@RequestBody PolicyModificationRequestDto policyModificationRequestDto) {
        LocalDate effectiveDate = policyModificationRequestDto.getEffectiveDate();
        if(effectiveDate == null){
            throw new PolicyDateException("Policy effectiveDate date can not be null!");
        }
        if (!effectiveDate.isAfter(LocalDate.now())) {
            throw new PolicyDateException("Policy effectiveDate date can only be in future!");
        }
        return policyModificationService.modifyPolicy(policyModificationRequestDto);
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
        return policyInformationService.getPolicyInformation(policyId, requestLocalDate);
    }
}
