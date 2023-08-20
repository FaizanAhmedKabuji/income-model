package com.buildmart.models.income.income.impl;

import com.buildmart.models.income.income.Income;
import com.buildmart.models.income.user.data.User;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

import static com.buildmart.models.income.contants.ModelDefiningConstants.GROUP_RP_LEVELS;
import static com.buildmart.models.income.metrics.Metrics.groupRpIncome;
import static com.buildmart.models.income.metrics.Metrics.onGoingMonth;
import static com.buildmart.models.income.metrics.Metrics.totalIncomeDistributed;
import static com.buildmart.models.income.metrics.MetricsUtility.addOrUpdateIncomeMetric;


@Component
public class GroupRpIncome implements Income {

    private static double minimumGroupRp;
    private static final TreeMap<Double, Double> groupRpLevels = new TreeMap<>();

    static {
        minimumGroupRp = Double.MAX_VALUE;
        for (var level: GROUP_RP_LEVELS) {
            minimumGroupRp = Double.min(minimumGroupRp, level.rpCollected());
            groupRpLevels.put(level.rpCollected(), level.incomeGenerated());
        }
    }
    @Override
    public boolean isEligible(User user) {
        return user.getGroupRp() >= minimumGroupRp;
    }

    @Override
    public void distributeRelativeIncome(User user) {
        // no relative incomes
    }

    @Override
    public void distributeAbsoluteIncome(User user) {
        var level = groupRpLevels.floorKey(user.getGroupRp());
        var earnedIncome = groupRpLevels.get(level);
        user.setIncome(user.getIncome() + earnedIncome);
        groupRpIncome += earnedIncome;
        totalIncomeDistributed += earnedIncome;

        var defaultIncome = onGoingMonth + " monGroupRpIncome";
        addOrUpdateIncomeMetric(defaultIncome, groupRpIncome);
    }
}
