package yeter.ugur.insuranceexample.service;

import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.math.BigDecimal;
import java.util.List;

public final class PolicyPremiumHelper {

    private PolicyPremiumHelper(){

    }

    public static BigDecimal calculateTotalPremium(List<InsuredPersonEntity> insuredPersons) {
        if(insuredPersons == null){
            return BigDecimal.ZERO;
        }
        BigDecimal result = BigDecimal.ZERO;
        for (InsuredPersonEntity insuredPersonEntity : insuredPersons) {
            result = result.add(insuredPersonEntity.getPremium());
        }
        return result;
    }
}
