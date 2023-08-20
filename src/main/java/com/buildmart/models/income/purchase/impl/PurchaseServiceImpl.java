package com.buildmart.models.income.purchase.impl;

import com.buildmart.models.income.purchase.PurchaseService;
import com.buildmart.models.income.user.data.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.buildmart.models.income.contants.ModelDefiningConstants.COMMUNITY_LEVEL_DEPTH;
import static com.buildmart.models.income.purchase.PurchaseUtility.makePurchaseBasedOnProfile;
import static com.buildmart.models.income.user.impl.UserFactoryImpl.PARENT_USER;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private static final Set<Integer> VISITED_USERS = new HashSet<>();
    private static final Set<Integer> VISITED_COMMUNITY_USERS = new HashSet<>();

    public void makeCurrentMonthPurchases() {
        VISITED_USERS.clear();
        makeCurrentMonthPurchases(PARENT_USER);
    }

    private void makeCurrentMonthPurchases(User user) {
        VISITED_USERS.add(user.getId());
        for (var child: user.getDownline()) {
            if (!VISITED_USERS.contains(child.getId())) {
                makeCurrentMonthPurchases(child);
            }
        }
        var currentMonthRp = makePurchaseBasedOnProfile(user.getProfile());
        user.setCurrentMonthRp(currentMonthRp);
    }

    @Override
    public void fillGroupRp() {
        VISITED_USERS.clear();
        fillGroupRp(PARENT_USER);
    }

    public void fillGroupRp(User parent) {
        VISITED_USERS.add(parent.getId());
        for (var user: parent.getDownline()) {
            if (!VISITED_USERS.contains(user.getId())) {
                fillCommunityRp(user);
            }
        }
        parent.setGroupRp(0.0);
        for (var child: parent.getDownline()) {
            parent.setGroupRp(parent.getGroupRp() + child.getCurrentMonthRp());
        }
    }

    @Override
    public void fillCommunityRp() {
        VISITED_USERS.clear();
        fillCommunityRp(PARENT_USER);
    }

    private void fillCommunityRp(User parent) {
        VISITED_USERS.add(parent.getId());

        for (var user: parent.getDownline()) {
            if (!VISITED_USERS.contains(user.getId())) {
                fillCommunityRp(user);
            }
        }
        VISITED_COMMUNITY_USERS.clear();
        parent.setCommunityRp(fillCommunityRp(parent, COMMUNITY_LEVEL_DEPTH));
    }

    private double fillCommunityRp(User parent, int depth) {
        if (depth == 0) return parent.getCurrentMonthRp();

        VISITED_COMMUNITY_USERS.add(parent.getId());

        double res = parent.getCurrentMonthRp();
        for (var user: parent.getDownline()) {
            if (!VISITED_COMMUNITY_USERS.contains(user.getId())) {
                res += fillCommunityRp(user, depth - 1);
            }
        }
        return res;
    }
}
