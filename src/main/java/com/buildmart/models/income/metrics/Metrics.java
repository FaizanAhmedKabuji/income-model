package com.buildmart.models.income.metrics;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.TreeMap;

@UtilityClass
public class Metrics {
    public static int totalUserCount = 0;

    public static double companyTotalRp = 0;
    public static double companyTotalVolume = 0;
    public static double defaultSlabIncome = 0;
    public static double groupRpIncome = 0;
    public static double communityRpIncome = 0;
    public static double totalIncomeDistributed = 0;

    public static int onGoingMonth;
    public static Map<String, Double> mapOfIncomes = new TreeMap<>();
    public static Map<String, Double> mapOfUsers = new TreeMap<>();
}
