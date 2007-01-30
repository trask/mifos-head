package org.mifos.framework.components.cronjob.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.mifos.application.accounts.business.AccountActionDateEntity;
import org.mifos.application.accounts.business.AccountBO;
import org.mifos.application.accounts.loan.business.TestLoanBO;
import org.mifos.application.accounts.util.helpers.AccountTypes;
import org.mifos.application.customer.business.CustomerBO;
import org.mifos.application.customer.util.helpers.CustomerStatus;
import org.mifos.application.meeting.business.MeetingBO;
import org.mifos.application.productdefinition.business.LoanOfferingBO;
import org.mifos.framework.MifosTestCase;
import org.mifos.framework.components.cronjobs.helpers.PortfolioAtRiskHelper;
import org.mifos.framework.components.cronjobs.helpers.PortfolioAtRiskTask;
import org.mifos.framework.hibernate.helper.HibernateUtil;
import org.mifos.framework.util.helpers.TestObjectFactory;

public class TestPortfolioAtRiskHelper extends MifosTestCase {

	protected AccountBO account1 = null;

	protected AccountBO account2 = null;

	protected CustomerBO center = null;

	protected CustomerBO group = null;

	protected CustomerBO client = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		TestObjectFactory.cleanUp(account2);
		TestObjectFactory.cleanUp(account1);
		TestObjectFactory.cleanUp(client);
		TestObjectFactory.cleanUp(group);
		TestObjectFactory.cleanUp(center);
		HibernateUtil.closeSession();
		super.tearDown();
	}

	public void testExecute() throws Exception {
		createInitialObject();

		group = TestObjectFactory.getObject(CustomerBO.class,
				group.getCustomerId());
		client = TestObjectFactory.getObject(CustomerBO.class,
				client.getCustomerId());
		for (AccountBO account : group.getAccounts()) {
			if (account.getAccountType().getAccountTypeId().equals(
					AccountTypes.LOANACCOUNT.getValue())) {
				changeFirstInstallmentDate(account, 7);
			}
		}
		for (AccountBO account : client.getAccounts()) {
			if (account.getAccountType().getAccountTypeId().equals(
					AccountTypes.LOANACCOUNT.getValue())) {
				changeFirstInstallmentDate(account, 7);
			}
		}
		TestObjectFactory.updateObject(client);
		TestObjectFactory.updateObject(group);
		TestObjectFactory.flushandCloseSession();

		PortfolioAtRiskTask portfolioAtRiskTask = new PortfolioAtRiskTask();
		PortfolioAtRiskHelper portfolioAtRiskHelper = (PortfolioAtRiskHelper) portfolioAtRiskTask
				.getTaskHelper();
		portfolioAtRiskHelper.execute(System.currentTimeMillis());
		HibernateUtil.closeSession();
		center = TestObjectFactory.getObject(CustomerBO.class,
				center.getCustomerId());
		group = TestObjectFactory.getObject(CustomerBO.class,
				group.getCustomerId());
		client = TestObjectFactory.getObject(CustomerBO.class,
				client.getCustomerId());
		account1 = TestObjectFactory.getObject(AccountBO.class,
				account1.getAccountId());
		account2 = TestObjectFactory.getObject(AccountBO.class,
				account2.getAccountId());
	}

	private void createInitialObject() {
		MeetingBO meeting = TestObjectFactory.createMeeting(TestObjectFactory
				.getTypicalMeeting());
		center = TestObjectFactory.createCenter("Center", meeting);
		group = TestObjectFactory.createGroupUnderCenter("Group", CustomerStatus.GROUP_ACTIVE, center);
		client = TestObjectFactory.createClient("Client",
				CustomerStatus.CLIENT_ACTIVE, group);
		LoanOfferingBO loanOffering = TestObjectFactory.createLoanOffering(
				"Loan", "LOAN", Short.valueOf("2"), new Date(System
						.currentTimeMillis()), Short.valueOf("1"), 300.0, 1.2,
				Short.valueOf("3"), Short.valueOf("1"), Short.valueOf("1"),
				Short.valueOf("1"), meeting);
		account1 = TestObjectFactory.createLoanAccount("42423142341", group,
				Short.valueOf("5"), new Date(System.currentTimeMillis()),
				loanOffering);
		loanOffering = TestObjectFactory.createLoanOffering("Loan123", "LOAP",
				Short.valueOf("1"), new Date(System.currentTimeMillis()), Short
						.valueOf("1"), 300.0, 1.2, Short.valueOf("3"), Short
						.valueOf("1"), Short.valueOf("1"), Short.valueOf("1"),
				meeting);
		account2 = TestObjectFactory.createLoanAccount("42427777341", client,
				Short.valueOf("5"), new Date(System.currentTimeMillis()),
				loanOffering);
	}

	private void changeFirstInstallmentDate(AccountBO accountBO,
			int numberOfDays) {
		Calendar currentDateCalendar = new GregorianCalendar();
		int year = currentDateCalendar.get(Calendar.YEAR);
		int month = currentDateCalendar.get(Calendar.MONTH);
		int day = currentDateCalendar.get(Calendar.DAY_OF_MONTH);
		currentDateCalendar = new GregorianCalendar(year, month, day
				- numberOfDays);
		for (AccountActionDateEntity accountActionDateEntity : accountBO
				.getAccountActionDates()) {
			TestLoanBO.setActionDate(accountActionDateEntity,new java.sql.Date(
					currentDateCalendar.getTimeInMillis()));
			break;
		}
	}
}
