package com.buildmart.models.income.contants;

import java.util.List;

public class ModelDefiningConstants {
    // make changes here: 
    public static final int SIMULATION_RUN_MONTHS = 20;

    public static final double RP_TO_VOLUME_MULTIPLIER = 70;
    public static final double RP_TO_MONEY_MULTIPLIER = 24;

    // community depth can be varied, if infinite depth is needed, add a very large. 
    public static final int COMMUNITY_LEVEL_DEPTH = 5;
    public static final double COMMUNITY_SLAB_MULTIPLIER = 0.05;

    public static final int STREAK_LENGTH_FOR_LOYAL = 3;
    public static final double MINIMUM_PURCHASE_EACH_MONTH_FOR_STREAK = 25.0;

    public static final List<UserSlabLevel> SLAB_LEVELS = List.of(
        new UserSlabLevel(0.0, 0.10),
        new UserSlabLevel(75.0, 0.12),
        new UserSlabLevel(100.0, 0.15)
    );

    public static final List<GroupRpLevel> GROUP_RP_LEVELS = List.of(
        new GroupRpLevel(50, 200),
        new GroupRpLevel(100, 400),
        new GroupRpLevel(250, 1000),
        new GroupRpLevel(500, 2000),
        new GroupRpLevel(1000, 4500),
        new GroupRpLevel(1500, 8000),
        new GroupRpLevel(2000, 12500),
        new GroupRpLevel(2500, 15000),
        new GroupRpLevel(3500, 20000),
        new GroupRpLevel(5000, 25000)
    );

    /**
     * add new profiles here in the format:
     * <li> name of the profile</li>
     * <li> weight of getting this profile </li>
     * <li> count of user this profile will add each month with the weight of each count asd a list </li>
     * <li> rp of the purchases this user will make with the weight of each rp as a list </li>
     * <b> ALWAYS ADD A VALUE OF 0 WITH A SUFFICIENT WEIGHT </b>
     */
    public static final List<Profile> PROFILES = List.of(
            new Profile(
                    "parent", 0,
                    List.of(
                            new UserAddCountWeightMap(0, 5),
                            new UserAddCountWeightMap(3, 50),
                            new UserAddCountWeightMap(10, 5),
                            new UserAddCountWeightMap(20, 2)
                    ),
                    List.of(
                            new PurchaseRpCountWeightMap(25.0, 1)
                    )
            ),
            new Profile(
                    "seller", 3,
                    List.of(
                            new UserAddCountWeightMap(0, 30),
                            new UserAddCountWeightMap(2, 20),
                            new UserAddCountWeightMap(1, 50)
                    ),
                    List.of(
                            new PurchaseRpCountWeightMap(0, 5),
                            new PurchaseRpCountWeightMap(25, 4),
                            new PurchaseRpCountWeightMap(50.0, 10),
                            new PurchaseRpCountWeightMap(70.0, 9),
                            new PurchaseRpCountWeightMap(90.0, 8),
                            new PurchaseRpCountWeightMap(110.0, 7),
                            new PurchaseRpCountWeightMap(130.0, 6),
                            new PurchaseRpCountWeightMap(150.0, 2),
                            new PurchaseRpCountWeightMap(170.0, 1)
                    )
            ),
            new Profile(
                    "active", 50,
                    List.of(
                            new UserAddCountWeightMap(0, 4),
                            new UserAddCountWeightMap(1, 3),
                            new UserAddCountWeightMap(2, 2),
                            new UserAddCountWeightMap(3, 1)
                    ),
                    List.of(
                            new PurchaseRpCountWeightMap(0.0, 5),
                            new PurchaseRpCountWeightMap(10.0, 4),
                            new PurchaseRpCountWeightMap(20.0, 10),
                            new PurchaseRpCountWeightMap(25.0, 9),
                            new PurchaseRpCountWeightMap(50.0, 6),
                            new PurchaseRpCountWeightMap(70.0, 1)
                    )
            ),
            new Profile(
                    "passive", 30,
                    List.of(
                            new UserAddCountWeightMap(0, 70),
                            new UserAddCountWeightMap(1, 15)
                    ),
                    List.of(
                            new PurchaseRpCountWeightMap(0.0, 10),
                            new PurchaseRpCountWeightMap(10.0, 9),
                            new PurchaseRpCountWeightMap(20.0, 5),
                            new PurchaseRpCountWeightMap(25.0, 4),
                            new PurchaseRpCountWeightMap(35.0, 1)
                    )
            )
//            new Profile(
//                    "activeBuyer", 30,
//                    List.of(
//                            new UserAddCountWeightMap(0, 0)
//                    ),
//                    List.of(
//                            new PurchaseRpCountWeightMap(10.0, 1)
//                    )
//            )
    );


    public record UserSlabLevel(double rpCollected, double slabPercent) {}
    public record GroupRpLevel(double rpCollected, double incomeGenerated) {}
    public record Profile (
            String profileName,
            double weightOfGettingThisUser,
            List<UserAddCountWeightMap> userAddCountWeightMap,
            List<PurchaseRpCountWeightMap> purchaseRpCountWeightMap
    ) {}
    public record UserAddCountWeightMap(int value, int weight) {}
    public record PurchaseRpCountWeightMap(double value, int weight) {}
}
