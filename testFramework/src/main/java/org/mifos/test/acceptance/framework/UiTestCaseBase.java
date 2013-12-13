/*
 * Copyright (c) 2005-2011 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.test.acceptance.framework;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import com.saucelabs.testng.SauceOnDemandTestListener;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

import org.dbunit.DatabaseUnitException;
import org.mifos.framework.util.DbUnitUtilities;
import org.mifos.test.acceptance.remote.InitializeApplicationRemoteTestingService;
import org.mifos.test.framework.util.DatabaseTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({SauceOnDemandTestListener.class})
@ContextConfiguration(locations = {"classpath:test-context.xml", "classpath:ui-test-context.xml"})
@Test(singleThreaded = true)
public class UiTestCaseBase extends AbstractTestNGSpringContextTests implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {
    private static final String ERROR_ELEMENT_ID = "*.errors";

    @Autowired
    protected DriverManagerDataSource dataSource;

    @Autowired
    protected DbUnitUtilities dbUnitUtilities;

    @Autowired
    protected InitializeApplicationRemoteTestingService initRemote;

    private final DatabaseTestUtils dbUtils = new DatabaseTestUtils();

    protected static Selenium selenium;

    @BeforeMethod(alwaysRun = true)
    public void baseSetUp(Method method) throws Exception {
        String testName = getClass().getName() + "." + method.getName();
        System.out.println("running test " + testName);
        String sauceUsername = System.getenv("SAUCE_USERNAME");
        String sauceAccessKey = System.getenv("SAUCE_ACCESS_KEY");
        String browserStartCommand = "{\"username\": \"" + sauceUsername + "\","
                + " \"access-key\": \"" + sauceAccessKey + "\","
                + " \"os\": \"Windows 7\","
                + " \"browser\": \"firefox\","
                + " \"browser-version\": \"21\","
                + " \"name\": \"" + testName + "\"}";
        this.selenium = new DefaultSelenium("ondemand.saucelabs.com", 80, browserStartCommand, "http://localhost:8081/mifos/");
        selenium.start();
        selenium.windowFocus();
        selenium.windowMaximize();
        if (!selenium.getLocation().matches("http://localhost:[0-9]+/mifos.*")) {
            // need to open mifos before setting cookie
            selenium.open("login.ftl");
        }
        selenium.createCookie("test=" + testName, "");
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException") // allow for overriding methods to throw Exception
    @BeforeMethod
    public void setUp() throws Exception {
        // do nothing
    }

    @AfterGroups(groups = {"ui"})
    public void stopSelenium() {
        synchronized (UiTestCaseBase.class) {
            if (seleniumServerIsRunning.booleanValue() && this.selenium != null) {
                this.selenium.stop();
            }
        }
    }

    @Test(enabled = false)
    public Selenium getSelenium() {
        synchronized (UiTestCaseBase.class) {
            return selenium;
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void baseTearDown() throws Exception {
        selenium.stop();
    }

	@Override
	public String getSessionId() {
		return selenium.getEval("selenium.sessionId");
	}

	@Override
	public SauceOnDemandAuthentication getAuthentication() {
		return new SauceOnDemandAuthentication("traskmifos", "bc2e3c15-7643-4194-bcc2-dc8452a22116");
	}

    protected void assertTextFoundOnPage(String text, String message) {
        assertTrue(selenium.isTextPresent(text), message);
    }

    protected void assertTextFoundOnPage(String text) {
        assertTextFoundOnPage(text, "Text \"" + text + "\" was not found.");
    }


    protected void assertElementTextExactMatch(String text, String elementId) {
        assertEquals(
                selenium.getText(elementId),
                text,
                "Text \"" + text + "\" does not match element \"" + elementId + "\":");
    }

    protected void assertErrorTextExactMatch(String text) {
        assertElementTextExactMatch(text, ERROR_ELEMENT_ID);
    }

    protected void assertElementTextIncludes(String text, String elementId) {
        assertTrue(
                selenium.getText(elementId).indexOf(text) >= 0,
                "Expected text \"" + text + "\" not included in element \"" + elementId + "\"");
    }

    protected void assertErrorTextIncludes(String text) {
        assertElementTextIncludes(text, ERROR_ELEMENT_ID);
    }

    protected void assertElementExistsOnPage(String elementId) {
        selenium.isElementPresent(elementId);
    }

    protected void assertElementDoesNotExistOnPage(String elementId, String messageIfFail) {
        Assert.assertFalse(selenium.isElementPresent(elementId), messageIfFail);
    }

    protected void assertElementExistsOnPage(String elementId, String messageIfFail) {
        assertTrue(selenium.isElementPresent(elementId), messageIfFail);
    }

    protected void deleteDataFromTables(String... tableNames)
            throws IOException, DatabaseUnitException, SQLException {
        dbUtils.deleteDataFromTables(dataSource, tableNames);
    }

    protected void assertPage(String pageName) {
        assertEquals(selenium.getAttribute("page.id@title"), pageName);
    }
}
