package com.buildmart.models.income.user;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.buildmart.models.income.contants.ModelDefiningConstants.PROFILES;

@UtilityClass
public class UserUtility {
    private static final Random RANDOM = new Random();
    public double getRandomNumber() {
        var randomDouble = RANDOM.nextInt(101);
        return Math.pow(randomDouble, 1) / 100;
    }

    private static final Map<String, List<Integer>> PROFILE_TO_USER_COUNT_MAPPING;

    static {
        PROFILE_TO_USER_COUNT_MAPPING = new HashMap<>();

        for (var profile: PROFILES) {
            var userCountList = new ArrayList<Integer>();
            var userAddCountWeightMapList = profile.userAddCountWeightMap();
            userAddCountWeightMapList.forEach(
                    countWeight -> {
                        for (int i = 0; i < countWeight.weight(); i++) {
                            userCountList.add(countWeight.value());
                        }
                    }
            );
            Collections.shuffle(userCountList);
            PROFILE_TO_USER_COUNT_MAPPING.put(profile.profileName(), userCountList);
        }
    }

    public static int getNumberOfUsersToBeAdded(String profile) {
        var userCountList = PROFILE_TO_USER_COUNT_MAPPING.get(profile);
        if (userCountList.isEmpty()) {
            return 0;
        }
        return userCountList.get(RANDOM.nextInt(userCountList.size()));
    }
}
