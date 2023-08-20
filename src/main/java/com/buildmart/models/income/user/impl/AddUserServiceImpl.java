package com.buildmart.models.income.user.impl;

import com.buildmart.models.income.user.AddUserService;
import com.buildmart.models.income.user.UserFactory;
import com.buildmart.models.income.user.UserUtility;
import com.buildmart.models.income.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.buildmart.models.income.metrics.MetricsUtility.increaseUserTypeMetrics;
import static com.buildmart.models.income.user.UserUtility.getNumberOfUsersToBeAdded;
import static com.buildmart.models.income.user.impl.UserFactoryImpl.PARENT_USER;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AddUserServiceImpl implements AddUserService {

    private static final Set<Integer> VISITED_USERS = new HashSet<>();

    private final UserFactory userFactory;

    public void addNewUsers() {
        VISITED_USERS.clear();
        addNewUsers(PARENT_USER);
    }

    private void addNewUsers(User user) {
        VISITED_USERS.add(user.getId());

        for (var child: user.getDownline()) {
            if (!VISITED_USERS.contains(child.getId())) {
                addNewUsers(child);
            }
        }

        var numberOfUsersToBeAdded = getNumberOfUsersToBeAdded(user.getProfile());
        for (int i = 0; i < numberOfUsersToBeAdded; ++i) {
            var userAddProb = UserUtility.getRandomNumber();
            var newUser = userFactory.getNewUserBasedOnProbablity(user.getId(), user, userAddProb);
            user.getDownline().add(newUser);
            increaseUserTypeMetrics(user, newUser);
        }
    }
}
