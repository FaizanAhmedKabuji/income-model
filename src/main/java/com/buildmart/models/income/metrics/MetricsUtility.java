package com.buildmart.models.income.metrics;

import com.buildmart.models.income.user.data.User;
import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;

import static com.buildmart.models.income.contants.ModelDefiningConstants.SIMULATION_RUN_MONTHS;
import static com.buildmart.models.income.metrics.Metrics.communityRpIncome;
import static com.buildmart.models.income.metrics.Metrics.companyTotalRp;
import static com.buildmart.models.income.metrics.Metrics.companyTotalVolume;
import static com.buildmart.models.income.metrics.Metrics.defaultSlabIncome;
import static com.buildmart.models.income.metrics.Metrics.groupRpIncome;
import static com.buildmart.models.income.metrics.Metrics.mapOfIncomes;
import static com.buildmart.models.income.metrics.Metrics.mapOfUsers;
import static com.buildmart.models.income.metrics.Metrics.onGoingMonth;
import static com.buildmart.models.income.metrics.Metrics.totalIncomeDistributed;
import static com.buildmart.models.income.metrics.Metrics.totalUserCount;
import static java.lang.System.out;

@UtilityClass
public class MetricsUtility {

    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(new Locale("en", "in"));


    public static void increaseUserTypeMetrics(User user, User newUser) {
        var addedBy = "Users added by " + user.getProfile() + ": ";
        var numberOfProfiles = "number of profile " + newUser.getProfile() + ": ";
        addOrUpdateUserMetric(addedBy, 1.0);
        addOrUpdateUserMetric(numberOfProfiles, 1.0);
    }

    public static void addOrUpdateIncomeMetric(String param, double value) {
        mapOfIncomes.putIfAbsent(param, 0.0);
        mapOfIncomes.put(param, mapOfIncomes.get(param) + value);
    }

    public static void addOrUpdateUserMetric(String param, double value) {
        mapOfUsers.putIfAbsent(param, 0.0);
        mapOfUsers.put(param, mapOfUsers.get(param) + value);
    }

    public static void updateMetrics() {
        try {
            var currMontTotVol = mapOfIncomes.get(onGoingMonth + " monTotVol");

            var currMonthCommunityIncome = (mapOfIncomes.get(onGoingMonth + " monCommunityIncome") / currMontTotVol) * 100;
            var currGroupIncome = (mapOfIncomes.get(onGoingMonth + " monGroupRpIncome") / currMontTotVol) * 100;
            var currSlabIncome = (mapOfIncomes.get(onGoingMonth + " monDefaultIncome") / currMontTotVol) * 100;
            var currMonthTotalMonthlyDistributedIncome = currMonthCommunityIncome + currGroupIncome +  currSlabIncome;

            var ratioCommunityIncome = onGoingMonth + " ratioCommunityIncome";
            var ratioGroup = onGoingMonth + " ratioGroup";
            var ratioSlab = onGoingMonth + " ratioSlab";
            var ratioTotalIncome = onGoingMonth + " ratioTotalIncome";


            addOrUpdateIncomeMetric(ratioCommunityIncome, currMonthCommunityIncome);
            addOrUpdateIncomeMetric(ratioGroup, currGroupIncome);
            addOrUpdateIncomeMetric(ratioSlab, currSlabIncome);
            addOrUpdateIncomeMetric(ratioTotalIncome, currMonthTotalMonthlyDistributedIncome);

        } catch (Exception e) {
            // do nothing
        }
    }

    public static void printFinalMetrics() {
        out.println();
        out.println("***** cumulative data *****");
        printCumulativeData();

        out.println();
        out.println("***** user related data *****");
        printUserCountData();


        out.println();
        out.println("***** monthly data *****");
        printMonthlyData();
    }

    private static void printCumulativeData() {
        out.println("total users: " + totalUserCount);
        out.println();
        out.println("defaultSlabIncome: " + FORMATTER.format(defaultSlabIncome));
        out.println("groupRpIncome: " + FORMATTER.format(groupRpIncome));
        out.println("communityRpIncome: " + FORMATTER.format(communityRpIncome));
        out.println("totalIncomeDistributed: " + FORMATTER.format(totalIncomeDistributed));

        out.println("companyTotalRp: " + companyTotalRp);
        out.println("companyTotalVolume: " + FORMATTER.format(companyTotalVolume));

        out.println("defaultSlabIncome ratio: " + (defaultSlabIncome / companyTotalVolume) * 100 + "%");
        out.println("groupRpIncome ratio: " + (groupRpIncome / companyTotalVolume) * 100 + "%");
        out.println("communityRpIncome ratio: " + (communityRpIncome / companyTotalVolume) * 100 + "%");
        out.println("totalIncomeDistributed ratio: " + (totalIncomeDistributed / companyTotalVolume) * 100 + "%");
    }

    private static void printMonthlyData() {
        for (var entrySet: mapOfIncomes.entrySet()) {
            var month = Integer.valueOf(entrySet.getKey().substring(0, entrySet.getKey().indexOf(' ')));
            if (month >= SIMULATION_RUN_MONTHS) {
                out.print(entrySet.getKey() + ": ");
                if (!entrySet.getKey().contains("monTotRp") && !entrySet.getKey().contains("ratio")) {
                    out.println(FORMATTER.format(entrySet.getValue()));
                } else {
                    out.print(entrySet.getValue());
                    if (entrySet.getKey().contains("ratio")) out.print("%");
                    out.println();
                }
            }
        }
    }

    private static void printUserCountData() {
        for (var entrySet: mapOfUsers.entrySet()) {
            out.print(entrySet.getKey() + ": ");
            out.println(entrySet.getValue());
        }
    }


}
