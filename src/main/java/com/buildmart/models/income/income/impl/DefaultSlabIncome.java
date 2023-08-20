package com.buildmart.models.income.income.impl;

import com.buildmart.models.income.income.Income;
import com.buildmart.models.income.user.data.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.buildmart.models.income.metrics.Metrics.defaultSlabIncome;
import static com.buildmart.models.income.metrics.Metrics.onGoingMonth;
import static com.buildmart.models.income.metrics.Metrics.totalIncomeDistributed;
import static com.buildmart.models.income.metrics.MetricsUtility.addOrUpdateIncomeMetric;

@Component
public class DefaultSlabIncome implements Income {
    @Override
    public boolean isEligible(User user) {
        return Objects.nonNull(user);
    }

    @Override
    public void distributeRelativeIncome(User user) {
        double earnedIncome = user.getCurrentMonthVolume() * user.getSlab();
        user.setIncome(user.getIncome() + earnedIncome);
        defaultSlabIncome += earnedIncome;
        totalIncomeDistributed += earnedIncome;

        var defaultIncome = onGoingMonth + " monDefaultIncome";
        addOrUpdateIncomeMetric(defaultIncome, earnedIncome);
    }

    @Override
    public void distributeAbsoluteIncome(User user) {
        // no absolute income
    }
}
