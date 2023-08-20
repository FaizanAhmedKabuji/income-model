package com.buildmart.models.income.purchase;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.buildmart.models.income.contants.ModelDefiningConstants.PROFILES;

@UtilityClass
public class PurchaseUtility {

    private static final Random RANDOM = new Random();
    public double getRandomNumber() {
        var randomDouble = RANDOM.nextInt(101);
        return Math.pow(randomDouble, 1) / 100;
    }
    private static final Map<String, List<Double>> PROFILE_TO_RP_MAPPING;

    static {
        PROFILE_TO_RP_MAPPING = new HashMap<>();
        for (var profile: PROFILES) {
            var rpList = new ArrayList<Double>();
            var rpCountWeightMap = profile.purchaseRpCountWeightMap();
            rpCountWeightMap.forEach(
                    countWeight -> {
                        for (int i = 0; i < countWeight.weight(); i++) {
                            rpList.add(countWeight.value());
                        }
                    }
            );
            Collections.shuffle(rpList);
            PROFILE_TO_RP_MAPPING.put(profile.profileName(), rpList);
        }
    }

    public static double makePurchaseBasedOnProfile(String profile) {
        var rpList = PROFILE_TO_RP_MAPPING.get(profile);
        if (rpList.isEmpty()) {
            return 0.0;
        }
        return rpList.get(RANDOM.nextInt(rpList.size()));
    }
}
