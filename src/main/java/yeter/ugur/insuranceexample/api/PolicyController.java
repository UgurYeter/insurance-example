package yeter.ugur.insuranceexample.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/policies")
@Slf4j
public class PolicyController {

    @PostMapping
    public void createPolicy(@RequestBody PolicyCreationRequestDto policyCreationRequestDto){
        log.info("policyCreationRequestDto:{}",policyCreationRequestDto);
    }
}
