package com.buildmart.models.income.user;

import com.buildmart.models.income.user.data.User;

public interface UserFactory {
    User getNewUserBasedOnProbablity(int id, User parent, double probability);
}
