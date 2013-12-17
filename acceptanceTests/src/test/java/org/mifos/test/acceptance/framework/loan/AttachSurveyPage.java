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

package org.mifos.test.acceptance.framework.loan;

import org.mifos.test.acceptance.framework.MifosPage;

import com.thoughtworks.selenium.Selenium;
import org.mifos.test.acceptance.framework.questionnaire.QuestionnairePage;
import org.mifos.test.acceptance.framework.savings.SavingsAccountDetailPage;
import org.testng.Assert;

public class AttachSurveyPage extends MifosPage {
    public AttachSurveyPage(Selenium selenium) {
        super(selenium);
    }

    public QuestionnairePage selectSurvey(String questionGroupTitle) {
        select("id=questionGroupId", "label=" + questionGroupTitle);
        selenium.click("_eventId_selectQuestionnaire");
        waitForPageToLoad();
        return new QuestionnairePage(selenium);
    }

    public AttachSurveyPage verifyNoneSelected() {
        Assert.assertEquals(selenium.getSelectedValue("questionGroupId"), "selectOne");
        return this;
    }

    public SavingsAccountDetailPage cancelAttachQuestionGroup(String questionGroupTitle) {
        selenium.select("id=questionGroupId", "label=" + questionGroupTitle);
        selenium.click("_eventId_cancel");
        waitForPageToLoad();
        return new SavingsAccountDetailPage(selenium);
    }
}
