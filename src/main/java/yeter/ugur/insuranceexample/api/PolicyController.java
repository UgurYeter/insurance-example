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
        log.info("Policy creation request is received!Policy start_date:'{}'", policyCreationRequestDto.getStartDate());
        policyRequestValidator.verifyCreatePolicyOrThrow(policyCreationRequestDto);
        return policyCreationService.createPolicy(policyCreationRequestDto);
    }

    @PutMapping("/modify")
    public PolicyModificationResponseDto modifyPolicy(@RequestBody PolicyModificationRequestDto policyModificationRequestDto) {
        log.info("Policy modification request is received!Policy with external_id:{} and effectiveDate:'{}'",
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate());
        policyRequestValidator.verifyModificationPolicyOrThrow(policyModificationRequestDto);
        return policyModificationService.modifyPolicy(policyModificationRequestDto);
    }

    @GetMapping("/{policyId}")
    public PolicyInformationResponseDto requestPolicyInformation(
            @PathVariable("policyId") String policyId,
            @RequestParam(required = false) String requestDate) {
        LocalDate requestLocalDate = LocalDate.now();
        log.info("Policy information request received with external_id:'{}' and request_date:'{}'!", policyId, requestDate);
        if (requestDate != null) {
            requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern(AppConfig.DATE_FORMAT));
        }
        return policyInformationService.getPolicyInformation(policyId, requestLocalDate);
    }
}
