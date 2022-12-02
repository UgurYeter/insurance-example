package yeter.ugur.insuranceexample.service.helper;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PolicyPremiumHelper {

    public BigDecimal calculateTotalPremium(List<InsuredPersonEntity> insuredPersons) {
        if (insuredPersons == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal result = BigDecimal.ZERO;
        for (InsuredPersonEntity insuredPersonEntity : insuredPersons) {
            result = result.add(insuredPersonEntity.getPremium());
        }
        return result;
    }
}
