package com.buildmart.models.income.income.impl;

import com.buildmart.models.income.income.Income;
import com.buildmart.models.income.income.IncomeService;
import com.buildmart.models.income.user.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.buildmart.models.income.user.impl.UserFactoryImpl.PARENT_USER;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class IncomeServiceImpl implements IncomeService {

    private static final Set<Integer> VISITED_USERS = new HashSet<>();
    private final List<Income> allIncomes;

    @Override
    public void distributeIncomes() {
        VISITED_USERS.clear();
        distributeIncomes(PARENT_USER);
    }

    private void distributeIncomes(User user) {
        VISITED_USERS.add(user.getId());
        for (var child: user.getDownline()) {
            if (!VISITED_USERS.contains(child.getId())) {
                distributeIncomes(child);
            }
        }
        distributeEligibleIncomes(user);

    }
    public void distributeEligibleIncomes(User user) {
        for (var income: allIncomes) {
            if (income.isEligible(user)) {
                income.distributeAbsoluteIncome(user);
                income.distributeRelativeIncome(user);
            }
        }
    }
}
