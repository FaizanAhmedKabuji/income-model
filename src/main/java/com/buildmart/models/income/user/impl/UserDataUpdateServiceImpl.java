package com.buildmart.models.income.user.impl;

import com.buildmart.models.income.user.UserDataUpdateService;
import com.buildmart.models.income.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.buildmart.models.income.contants.ModelDefiningConstants.MINIMUM_PURCHASE_EACH_MONTH_FOR_STREAK;
import static com.buildmart.models.income.contants.ModelDefiningConstants.RP_TO_VOLUME_MULTIPLIER;
import static com.buildmart.models.income.contants.ModelDefiningConstants.SLAB_LEVELS;
import static com.buildmart.models.income.contants.ModelDefiningConstants.STREAK_LENGTH_FOR_LOYAL;
import static com.buildmart.models.income.metrics.Metrics.companyTotalRp;
import static com.buildmart.models.income.metrics.Metrics.companyTotalVolume;
import static com.buildmart.models.income.metrics.Metrics.onGoingMonth;
import static com.buildmart.models.income.metrics.MetricsUtility.addOrUpdateIncomeMetric;
import static com.buildmart.models.income.user.impl.UserFactoryImpl.PARENT_USER;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserDataUpdateServiceImpl implements UserDataUpdateService {
    
    private static final Set<Integer> VISITED_USERS = new HashSet<>();

    @Override
    public void updateDataForUser() {
        VISITED_USERS.clear();
        updateDataForUser(PARENT_USER);
    }

    private void updateDataForUser(User parent) {
        VISITED_USERS.add(parent.getId());

         for (var user: parent.getDownline()) {
            if (!VISITED_USERS.contains(user.getId())) {
                updateDataForUser(user);
            }
        }   

        updatePurchaseData(parent);

        updateStreakData(parent);

        makeLoyalIfEligible(parent);
        
        updateSlabIfEligible(parent);
    }

    private void updatePurchaseData(User user) {
        user.setTotalRp(user.getTotalRp() + user.getCurrentMonthRp());
        user.setCurrentMonthVolume(user.getCurrentMonthRp() * RP_TO_VOLUME_MULTIPLIER);

        companyTotalRp += user.getCurrentMonthRp();
        companyTotalVolume += user.getCurrentMonthRp() * RP_TO_VOLUME_MULTIPLIER;

        var tolVol = onGoingMonth + " monTotVol";
        var tolRp = onGoingMonth + " monTotRp";
        addOrUpdateIncomeMetric(tolRp, user.getCurrentMonthRp());
        addOrUpdateIncomeMetric(tolVol, user.getCurrentMonthRp() * RP_TO_VOLUME_MULTIPLIER);
    }

    private void updateStreakData(User user) {
        user.getStreakPurchases().add(0, user.getCurrentMonthRp());
        if (user.getCurrentMonthRp() < MINIMUM_PURCHASE_EACH_MONTH_FOR_STREAK) {
            user.setLoyal(false);
            user.setStreakLength(0);
        } else {
            user.setStreakLength(user.getStreakLength() + 1);
        }
        if (STREAK_LENGTH_FOR_LOYAL <= user.getStreakPurchases().size()) {
            user.setStreakRp(Collections.min(user.getStreakPurchases().subList(0, STREAK_LENGTH_FOR_LOYAL)));
        }
    }

    private void makeLoyalIfEligible(User user) {
        if (user.getStreakLength() >= STREAK_LENGTH_FOR_LOYAL) {
            user.setLoyal(true);
        }
    } 

    private void updateSlabIfEligible(User user) {
        var totalRP = user.getTotalRp();

        for (var slab: SLAB_LEVELS) {
            var rpCollected = slab.rpCollected();
            var slabValue = slab.slabPercent();

            if (totalRP >= rpCollected) user.setSlab(slabValue);
        }
    }

    public void printFullHierarchy() {
        VISITED_USERS.clear();
        printFullHierarchy(PARENT_USER);
    }

    private void printFullHierarchy(User parent) {
        VISITED_USERS.add(parent.getId());

        for (var user: parent.getDownline()) {
            if (!VISITED_USERS.contains(user.getId())) {
                printFullHierarchy(user);
            }
        }
    }
}
