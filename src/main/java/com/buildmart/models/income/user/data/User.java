package com.buildmart.models.income.user.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
//@ToString(exclude = {"parent", "purchaseProb", "streakRp", "streakPurchases", "addUserProb", "downline"})
@RequiredArgsConstructor
public class User {
    private final int id;
    private final User parent;
    private String profile;
    private List<User> downline = new ArrayList<>();
    private double purchaseProb;
    private boolean loyal;
    private int streakLength;
    private double streakRp;
    private List<Double> streakPurchases = new ArrayList<>(Arrays.asList(0.0, 0.0 ,0.0));
    private double totalRp;           
    private double currentMonthRp;    
    private double currentMonthVolume;    
    private double groupRp;           
    private double communityRp;       
    private double slab;
    private double income;
    private double addUserProb;

    private String getDownline(List<User> downline) {
        StringBuilder res = new StringBuilder();
        for (var user: downline) {
            res.append(user.getId());
            res.append(", ");
        }
        return res.toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", profile='" + profile + '\'' +
                ", downline=" + getDownline(downline) +
                ", purchaseProb=" + purchaseProb +
                ", loyal=" + loyal +
                ", streakLength=" + streakLength +
                ", streakRp=" + streakRp +
                ", streakPurchases=" + streakPurchases +
                ", totalRp=" + totalRp +
                ", currentMonthRp=" + currentMonthRp +
                ", currentMonthVolume=" + currentMonthVolume +
                ", groupRp=" + groupRp +
                ", communityRp=" + communityRp +
                ", slab=" + slab +
                ", income=" + income +
                '}';
    }
}