package com.buildmart.models.income.user.impl;

import com.buildmart.models.income.contants.ModelDefiningConstants;
import com.buildmart.models.income.user.UserFactory;
import com.buildmart.models.income.user.data.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static com.buildmart.models.income.metrics.Metrics.totalUserCount;
import static com.buildmart.models.income.contants.NamingConstants.PARENT;

@Service
public class UserFactoryImpl implements UserFactory {

    private static final TreeMap<Double, String> WEIGHT_ADJUSTED_PROFILE_MAP;
    private static final Map<String, ModelDefiningConstants.Profile> PROFILE_MAP;
    private static final double SUM_OF_ALL_WEIGHTS = ModelDefiningConstants.PROFILES
            .stream()
            .map(ModelDefiningConstants.Profile::weightOfGettingThisUser)
            .mapToDouble(Double::doubleValue)
            .sum();
    public static final User PARENT_USER;

    static {
        WEIGHT_ADJUSTED_PROFILE_MAP = new TreeMap<>();
        for (var profile: ModelDefiningConstants.PROFILES) {
            WEIGHT_ADJUSTED_PROFILE_MAP.put(profile.weightOfGettingThisUser() / SUM_OF_ALL_WEIGHTS, profile.profileName());
        }
        PROFILE_MAP = new HashMap<>();
        for (var profileName: WEIGHT_ADJUSTED_PROFILE_MAP.values()) {
            for (var profile: ModelDefiningConstants.PROFILES) {
                if (profileName.equals(profile.profileName())) {
                    PROFILE_MAP.put(profileName, profile);
                }
            }
        }
        PARENT_USER = new User(0, null);
        PARENT_USER.setProfile(PARENT);
        PARENT_USER.setTotalRp(0.0);
        PARENT_USER.setCurrentMonthRp(0.0);
        PARENT_USER.setTotalRp(0.0);

        WEIGHT_ADJUSTED_PROFILE_MAP.remove(WEIGHT_ADJUSTED_PROFILE_MAP.firstKey());
        PROFILE_MAP.remove(PARENT);
    }

    @Override
    public User getNewUserBasedOnProbablity(int id, User parent, double probability) {
        var entry = WEIGHT_ADJUSTED_PROFILE_MAP.floorEntry(probability);
        if (Objects.isNull(entry)) {
            entry = WEIGHT_ADJUSTED_PROFILE_MAP.firstEntry();
        }
        var profileName = entry.getValue();
        ModelDefiningConstants.Profile profile = PROFILE_MAP.get(profileName);
        User user = new User(++totalUserCount, parent);
        user.setProfile(profile.profileName());
        return user;
    }
}
