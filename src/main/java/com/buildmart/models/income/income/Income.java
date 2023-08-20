package com.buildmart.models.income.income;

import com.buildmart.models.income.user.data.User;

public interface Income {
    boolean isEligible(User user);
    void distributeRelativeIncome(User user);
    void distributeAbsoluteIncome(User user);
}
