package com.buildmart.models.income.income.impl;

import com.buildmart.models.income.income.Income;
import com.buildmart.models.income.user.data.User;
import org.springframework.stereotype.Component;

import static com.buildmart.models.income.contants.ModelDefiningConstants.COMMUNITY_SLAB_MULTIPLIER;
import static com.buildmart.models.income.contants.ModelDefiningConstants.MINIMUM_PURCHASE_EACH_MONTH_FOR_STREAK;
import static com.buildmart.models.income.contants.ModelDefiningConstants.RP_TO_MONEY_MULTIPLIER;
import static com.buildmart.models.income.metrics.Metrics.communityRpIncome;
import static com.buildmart.models.income.metrics.Metrics.onGoingMonth;
import static com.buildmart.models.income.metrics.Metrics.totalIncomeDistributed;
import static com.buildmart.models.income.metrics.MetricsUtility.addOrUpdateIncomeMetric;

@Component
public class CommunityIncome implements Income {
    @Override
    public boolean isEligible(User user) {
        return user.isLoyal() && user.getStreakRp() >= MINIMUM_PURCHASE_EACH_MONTH_FOR_STREAK;
    }

    @Override
    public void distributeRelativeIncome(User user) {
        var earnedIncome = user.getCommunityRp() * RP_TO_MONEY_MULTIPLIER * getSlabMultiplier();
        user.setIncome(user.getIncome() + earnedIncome);
        communityRpIncome += earnedIncome;
        totalIncomeDistributed += earnedIncome;

        var defaultIncome = onGoingMonth + " monCommunityIncome";
        addOrUpdateIncomeMetric(defaultIncome, earnedIncome);
    }

    @Override
    public void distributeAbsoluteIncome(User user) {
        // no absolute income
    }

    private double getSlabMultiplier() {
         return COMMUNITY_SLAB_MULTIPLIER;
    }
}
