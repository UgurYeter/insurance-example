package yeter.ugur.insuranceexample.service.helper;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExternalPolicyIdProvider {

    public String generateExternalPolicyId() {
        return UUID.randomUUID().toString();
    }
}
