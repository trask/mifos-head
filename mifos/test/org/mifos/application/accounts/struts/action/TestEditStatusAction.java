package org.mifos.application.accounts.struts.action;

import java.net.URISyntaxException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.mifos.application.accounts.business.AccountBO;
import org.mifos.application.accounts.business.AccountStateEntity;
import org.mifos.application.accounts.loan.business.LoanBO;
import org.mifos.application.accounts.loan.util.helpers.LoanConstants;
import org.mifos.application.accounts.savings.business.SavingsBO;
import org.mifos.application.accounts.savings.util.helpers.SavingsConstants;
import org.mifos.application.accounts.util.helpers.AccountStates;
import org.mifos.application.customer.business.CustomerBO;
import org.mifos.application.customer.client.util.helpers.ClientConstants;
import org.mifos.application.customer.group.util.helpers.GroupConstants;
import org.mifos.application.meeting.business.MeetingBO;
import org.mifos.application.productdefinition.business.LoanOfferingBO;
import org.mifos.application.productdefinition.business.SavingsOfferingBO;
import org.mifos.application.util.helpers.ActionForwards;
import org.mifos.framework.MifosMockStrutsTestCase;
import org.mifos.framework.hibernate.helper.HibernateUtil;
import org.mifos.framework.security.util.ActivityContext;
import org.mifos.framework.security.util.UserContext;
import org.mifos.framework.util.helpers.Constants;
import org.mifos.framework.util.helpers.ResourceLoader;
import org.mifos.framework.util.helpers.SessionUtils;
import org.mifos.framework.util.helpers.TestObjectFactory;

public class TestEditStatusAction extends MifosMockStrutsTestCase {
	private AccountBO accountBO;

	private UserContext userContext;
	
	private CustomerBO client;

	private CustomerBO group;

	private CustomerBO center;
	
	private MeetingBO meeting;
	
	private SavingsOfferingBO savingsOffering;

	protected void setUp() throws Exception {
		super.setUp();
		try {
			setServletConfigFile(ResourceLoader.getURI("WEB-INF/web.xml")
					.getPath());
			setConfigFile(ResourceLoader.getURI(
					"org/mifos/application/accounts/struts-config.xml")
					.getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		userContext = new UserContext();
		userContext.setId(new Short("1"));
		userContext.setLocaleId(new Short("1"));
		Set<Short> set = new HashSet<Short>();
		set.add(Short.valueOf("1"));
		userContext.setRoles(set);
		userContext.setLevelId(Short.valueOf("2"));
		userContext.setName("mifos");
		userContext.setPereferedLocale(new Locale("en", "US"));
		userContext.setBranchId(new Short("1"));
		userContext.setBranchGlobalNum("0001");
		request.getSession().setAttribute(Constants.USERCONTEXT, userContext);
		addRequestParameter("recordLoanOfficerId", "1");
		addRequestParameter("recordOfficeId", "1");
		ActivityContext ac = new ActivityContext((short) 0, userContext
				.getBranchId().shortValue(), userContext.getId().shortValue());
		request.getSession(false).setAttribute("ActivityContext", ac);
		request.getSession().setAttribute(Constants.USERCONTEXT, userContext);
	}

	public void tearDown() throws Exception {
		TestObjectFactory.cleanUp(accountBO);
		TestObjectFactory.cleanUp(client);
		TestObjectFactory.cleanUp(group);
		TestObjectFactory.cleanUp(center);
		HibernateUtil.closeSession();
		super.tearDown();
	}

	public void testLoad() {
		createInitialObjects();
		accountBO = getLoanAccount(client,meeting);
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("input", "loan");
		addRequestParameter("accountId", accountBO.getAccountId().toString());
		actionPerform();
		verifyForward("load_success");
		verifyNoActionErrors();
		verifyNoActionMessages();
		
		assertNotNull(SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession()));
		assertEquals("Size of the status list should be 2",2,((List<AccountStateEntity>)SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession())).size());
	}
	
	public void testPreviewSuccess() {
		createInitialObjects();
		accountBO = getLoanAccount(client,meeting);
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("input", "loan");
		addRequestParameter("accountId", accountBO.getAccountId().toString());
		actionPerform();
		verifyForward("load_success");
		assertNotNull(SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession()));
		assertEquals("Size of the status list should be 2",2,((List<AccountStateEntity>)SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession())).size());
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "preview");
		addRequestParameter("input", "loan");
		addRequestParameter("notes", "Test");
		addRequestParameter("accountTypeId", accountBO.getAccountType().getAccountTypeId().toString());
		addRequestParameter("newStatusId", "8");
		addRequestParameter("flagId", "1");
		actionPerform();
		verifyForward("preview_success");
		verifyNoActionErrors();
		verifyNoActionMessages();
		assertEquals("Closed- Rescheduled",(String)SessionUtils.getAttribute(SavingsConstants.NEW_STATUS_NAME,request.getSession()));
		assertNull("Since new Status is not cancel,so flag should be null.",SessionUtils.getAttribute(SavingsConstants.FLAG_NAME,request.getSession()));
	}
	
	public void testPreviewFailure() {
		createInitialObjects();
		accountBO = getLoanAccount(client,meeting);
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("input", "loan");
		addRequestParameter("accountId", accountBO.getAccountId().toString());
		actionPerform();
		verifyForward("load_success");
		assertNotNull(SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession()));
		assertEquals("Size of the status list should be 2",2,((List<AccountStateEntity>)SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession())).size());
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "preview");
		addRequestParameter("input", "loan");
		addRequestParameter("accountTypeId", accountBO.getAccountType().getAccountTypeId().toString());
		addRequestParameter("newStatusId", "8");
		addRequestParameter("flagId", "1");
		actionPerform();
		assertEquals(1, getErrrorSize());
		verifyActionErrors(new String[] { LoanConstants.MANDATORY_TEXTBOX });
	}
	
	public void testPrevious() {
		createInitialObjects();
		accountBO = getLoanAccount(client,meeting);
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "previous");
		addRequestParameter("input", "loan");
		addRequestParameter("accountId", accountBO.getAccountId().toString());
		actionPerform();
		verifyForward("previous_success");
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
	
	public void testCancel() {
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "cancel");
		addRequestParameter("input", "loan");
		actionPerform();
		verifyForward("loan_detail_page");
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
	
	public void testUpdateSuccessForLoan() {
		createInitialObjects();
		accountBO = getLoanAccount(client,meeting);
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("input", "loan");
		addRequestParameter("accountId", accountBO.getAccountId().toString());
		actionPerform();
		verifyForward("load_success");
		assertNotNull(SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession()));
		assertEquals("Size of the status list should be 2",2,((List<AccountStateEntity>)SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession())).size());
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "preview");
		addRequestParameter("input", "loan");
		addRequestParameter("notes", "Test");
		addRequestParameter("accountTypeId", accountBO.getAccountType().getAccountTypeId().toString());
		addRequestParameter("newStatusId", "8");
		addRequestParameter("flagId", "1");
		actionPerform();
		verifyForward("preview_success");
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "update");
		addRequestParameter("notes", "Test");
		addRequestParameter("accountTypeId", accountBO.getAccountType().getAccountTypeId().toString());
		addRequestParameter("newStatusId", "8");
		addRequestParameter("flagId", "1");
		actionPerform();
		verifyForward(ActionForwards.loan_detail_page.toString());
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
	
	public void testUpdateSuccessForSavings() {
		createInitialObjects();
		savingsOffering = createSavingsOffering();
		accountBO = createSavingsAccount("000X00000000019", savingsOffering,
				AccountStates.SAVINGS_ACC_PARTIALAPPLICATION);
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "load");
		addRequestParameter("input", "savings");
		addRequestParameter("accountId", accountBO.getAccountId().toString());
		actionPerform();
		verifyForward("load_success");
		assertNotNull(SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession()));
		assertEquals("Size of the status list should be 2",2,((List<AccountStateEntity>)SessionUtils.getAttribute(SavingsConstants.STATUS_LIST,request.getSession())).size());
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "preview");
		addRequestParameter("input", "savings");
		addRequestParameter("notes", "Test");
		addRequestParameter("accountTypeId", accountBO.getAccountType().getAccountTypeId().toString());
		addRequestParameter("newStatusId", "15");
		addRequestParameter("flagId", "4");
		actionPerform();
		verifyForward("preview_success");
		
		setRequestPathInfo("/editStatusAction.do");
		addRequestParameter("method", "update");
		addRequestParameter("notes", "Test");
		addRequestParameter("accountTypeId", accountBO.getAccountType().getAccountTypeId().toString());
		addRequestParameter("newStatusId", "15");
		addRequestParameter("flagId", "4");
		actionPerform();
		verifyForward(ActionForwards.savings_details_page.toString());
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
	
	private void createInitialObjects() {
		meeting = TestObjectFactory.createMeeting(TestObjectFactory
				.getMeetingHelper(1, 1, 4, 2));
		center = TestObjectFactory.createCenter("Center", Short
				.valueOf("13"), "1.4", meeting, new Date(System
				.currentTimeMillis()));
		group = TestObjectFactory.createGroup("Group", GroupConstants.ACTIVE, "1.4.1", center, new Date(System
				.currentTimeMillis()));
		client = TestObjectFactory.createClient("Client",ClientConstants.STATUS_ACTIVE,"1.4.1.1",group,new Date(System
				.currentTimeMillis()));
	}
	
	private LoanBO getLoanAccount(CustomerBO customer, MeetingBO meeting) {
		Date startDate = new Date(System.currentTimeMillis());
		LoanOfferingBO loanOffering = TestObjectFactory.createLoanOffering(
				"Loan", Short.valueOf("2"), startDate, Short
						.valueOf("1"), 300.0, 1.2, Short.valueOf("3"), Short
						.valueOf("1"), Short.valueOf("1"), Short.valueOf("1"),
				Short.valueOf("1"), Short.valueOf("1"), meeting);
		return TestObjectFactory.createLoanAccount("42423142341", customer, Short
				.valueOf("5"), startDate, loanOffering);

	}
	
	private SavingsOfferingBO createSavingsOffering() {
		MeetingBO meetingIntCalc = TestObjectFactory
				.createMeeting(TestObjectFactory.getMeetingHelper(1, 1, 4, 2));
		MeetingBO meetingIntPost = TestObjectFactory
				.createMeeting(TestObjectFactory.getMeetingHelper(1, 1, 4, 2));
		return TestObjectFactory.createSavingsOffering("SavingPrd1", Short
				.valueOf("2"), new Date(System.currentTimeMillis()), Short
				.valueOf("2"), 300.0, Short.valueOf("1"), 1.2, 200.0, 200.0,
				Short.valueOf("2"), Short.valueOf("1"), meetingIntCalc,
				meetingIntPost);
	}

	private SavingsBO createSavingsAccount(String globalAccountNum,
			SavingsOfferingBO savingsOffering, short accountStateId) {
		return TestObjectFactory.createSavingsAccount(globalAccountNum, group,
				accountStateId, new Date(System.currentTimeMillis()), savingsOffering, userContext);
	}
}
