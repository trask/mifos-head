package org.mifos.application.checklist.business.service;

import java.util.List;

import org.mifos.application.accounts.util.helpers.AccountState;
import org.mifos.application.checklist.business.AccountCheckListBO;
import org.mifos.application.checklist.business.CheckListBO;
import org.mifos.application.checklist.business.CustomerCheckListBO;
import org.mifos.application.checklist.util.helpers.CheckListMasterView;
import org.mifos.application.customer.util.helpers.CustomerLevel;
import org.mifos.application.customer.util.helpers.CustomerStatus;
import org.mifos.application.productdefinition.util.helpers.ProductType;
import org.mifos.framework.MifosTestCase;
import org.mifos.framework.exceptions.ServiceException;
import org.mifos.framework.hibernate.helper.HibernateUtil;
import org.mifos.framework.util.helpers.TestObjectFactory;

public class TestCheckListBusinessService extends MifosTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetCheckListMasterData() throws Exception {
		List<CheckListMasterView> checkListMasterDataView = new CheckListBusinessService()
				.getCheckListMasterData(TestObjectFactory.getContext());
		assertNotNull(checkListMasterDataView);
		assertEquals(checkListMasterDataView.size(), 5);
	}

	public void testRetreiveAllAccountCheckLists() throws Exception {
		CheckListBO checkList = TestObjectFactory.createAccountChecklist(
				ProductType.LOAN.getValue(),
				AccountState.LOANACC_ACTIVEINGOODSTANDING, (short) 1);
		CheckListBO checkList1 = TestObjectFactory.createCustomerChecklist(
				CustomerLevel.CENTER.getValue(), CustomerStatus.CENTER_ACTIVE
						.getValue(), (short) 1);
		List<AccountCheckListBO> checkLists = new CheckListBusinessService()
				.retreiveAllAccountCheckLists();
		assertNotNull(checkLists);
		assertEquals(1, checkLists.size());
		TestObjectFactory.cleanUp(checkList);
		TestObjectFactory.cleanUp(checkList1);
	}

	public void testRetreiveAllAccountCheckListsForInvalidConnection() {
		TestObjectFactory.simulateInvalidConnection();
		try {
			List<AccountCheckListBO> checkLists = new CheckListBusinessService()
					.retreiveAllAccountCheckLists();
			fail();
		} catch (ServiceException e) {
			assertTrue(true);
		}
		HibernateUtil.closeSession();
	}

	public void testRetreiveAllCustomerCheckLists() throws Exception {
		CheckListBO checkList = TestObjectFactory.createAccountChecklist(
				ProductType.LOAN.getValue(),
				AccountState.LOANACC_ACTIVEINGOODSTANDING, (short) 1);
		CheckListBO checkList1 = TestObjectFactory.createCustomerChecklist(
				CustomerLevel.CENTER.getValue(), CustomerStatus.CENTER_ACTIVE
						.getValue(), (short) 1);
		List<CustomerCheckListBO> checkLists = new CheckListBusinessService()
				.retreiveAllCustomerCheckLists();
		assertNotNull(checkLists);
		assertEquals(1, checkLists.size());
		TestObjectFactory.cleanUp(checkList);
		TestObjectFactory.cleanUp(checkList1);
	}

	public void testRetreiveAllCustomerCheckListsForInvalidConnection() {
		TestObjectFactory.simulateInvalidConnection();
		try {
			List<CustomerCheckListBO> checkLists = new CheckListBusinessService()
					.retreiveAllCustomerCheckLists();
			fail();
		} catch (ServiceException e) {
			assertTrue(true);
		}
		HibernateUtil.closeSession();
	}

	public void testGetCheckList() throws Exception {
		CheckListBO checkList = TestObjectFactory.createAccountChecklist(
				ProductType.LOAN.getValue(),
				AccountState.LOANACC_ACTIVEINGOODSTANDING, (short) 1);
		HibernateUtil.closeSession();
		checkList = new CheckListBusinessService().getCheckList(checkList
				.getChecklistId());
		assertNotNull(checkList);
		assertEquals("productchecklist", checkList.getChecklistName());
		TestObjectFactory.cleanUp(checkList);
	}

	public void testGetCheckListForInvalidConnection() throws Exception {
		CheckListBO checkList = TestObjectFactory.createAccountChecklist(
				ProductType.LOAN.getValue(),
				AccountState.LOANACC_ACTIVEINGOODSTANDING, (short) 1);
		HibernateUtil.closeSession();
		TestObjectFactory.simulateInvalidConnection();
		try {
			checkList = new CheckListBusinessService().getCheckList(checkList
					.getChecklistId());
			fail();
		} catch (ServiceException e) {
			assertTrue(true);
		}
		HibernateUtil.closeSession();
		HibernateUtil.closeSession();
		TestObjectFactory.cleanUp(checkList);
	}
}
