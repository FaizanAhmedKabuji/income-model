package com.buildmart.models.income;

import com.buildmart.models.income.income.IncomeService;
import com.buildmart.models.income.purchase.PurchaseService;
import com.buildmart.models.income.user.AddUserService;
import com.buildmart.models.income.user.UserDataUpdateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static com.buildmart.models.income.contants.ModelDefiningConstants.SIMULATION_RUN_MONTHS;
import static com.buildmart.models.income.metrics.Metrics.onGoingMonth;
import static com.buildmart.models.income.metrics.Metrics.totalUserCount;
import static com.buildmart.models.income.metrics.MetricsUtility.printFinalMetrics;
import static com.buildmart.models.income.metrics.MetricsUtility.updateMetrics;
import static java.lang.System.out;

@SpringBootApplication
public class IncomeModelApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(IncomeModelApplication.class, args);
		// todo: correct these beans
		UserDataUpdateService userDataUpdateService = (UserDataUpdateService) context.getBean("userDataUpdateServiceImpl");
		AddUserService addUserService = (AddUserService) context.getBean("addUserServiceImpl");
		IncomeService incomeService = (IncomeService) context.getBean("incomeServiceImpl");
		PurchaseService purchaseService = (PurchaseService) context.getBean("purchaseServiceImpl");

		for (int i = 0; i < SIMULATION_RUN_MONTHS; i++) {
			onGoingMonth = i + 1;
			addUserService.addNewUsers();
			purchaseService.makeCurrentMonthPurchases();
			userDataUpdateService.updateDataForUser();
			purchaseService.fillGroupRp();
			purchaseService.fillCommunityRp();
			incomeService.distributeIncomes();
			out.println("Total number of users in the month " + onGoingMonth + ": " + totalUserCount);

			updateMetrics();
		}
//		userDataUpdateService.printFullHierarchy();

		printFinalMetrics();
	}

}
