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

package org.mifos.clientportfolio.loan.ui;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.platform.validation.MifosBeanValidator;
import org.mifos.ui.validation.StubValidationContext;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.binding.message.Message;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(MockitoJUnitRunner.class)
public class LoanAccountFormBeanTest {

    private LoanAccountFormBean loanAccountFormBean;
    
    private ValidationContext context;
    
    @Before
    public void setUp() {
        MifosBeanValidator validator = new MifosBeanValidator();
        LocalValidatorFactoryBean targetValidator = new LocalValidatorFactoryBean();
        targetValidator.afterPropertiesSet();
        validator.setTargetValidator(targetValidator);

        loanAccountFormBean = new LoanAccountFormBean();
        loanAccountFormBean.setProductId(1);
        loanAccountFormBean.setPurposeOfLoanMandatory(false);
        loanAccountFormBean.setSourceOfFundsMandatory(false);
        
        loanAccountFormBean.setAmount(Integer.valueOf(1000));
        loanAccountFormBean.setMinAllowedAmount(Integer.valueOf(400));
        loanAccountFormBean.setMaxAllowedAmount(Integer.valueOf(20000));
        
        loanAccountFormBean.setInterestRate(Double.valueOf(10.0));
        loanAccountFormBean.setMinAllowedInterestRate(Double.valueOf(1.0));
        loanAccountFormBean.setMaxAllowedInterestRate(Double.valueOf(20.0));
        
        loanAccountFormBean.setDisbursalDateDay(24);
        loanAccountFormBean.setDisbursalDateMonth(02);
        loanAccountFormBean.setDisbursalDateYear(2011);
        
        loanAccountFormBean.setNumberOfInstallments(12);
        loanAccountFormBean.setMinNumberOfInstallments(1);
        loanAccountFormBean.setMaxNumberOfInstallments(12);
        
        loanAccountFormBean.setValidator(validator);
        
        context = new StubValidationContext();
    }
    
    @Test
    public void shouldContainValidationMessageOnAmountFieldWhenAmountViolatesAllowedRange() {

        // setup
        loanAccountFormBean.setAmount(Integer.valueOf(0));
        
        // exercise test
        loanAccountFormBean.validateEnterAccountDetailsStep(context);
        
        // verification
        MessageContext messageContext = context.getMessageContext();
        Message[] messages = messageContext.getAllMessages();

        assertThat(messages.length, is(1));
        Message message = messages[0];
        assertThat(message.getSource().toString(), is("amount"));
        assertThat(message.getText().toString(), containsString("loanAccountFormBean.Amount.invalid"));
    }
    
    @Test
    public void shouldContainValidationMessageOnInterestRateFieldWhenInterestRateViolatesAllowedRange() {

        // setup
        loanAccountFormBean.setInterestRate(200);
        
        // exercise test
        loanAccountFormBean.validateEnterAccountDetailsStep(context);
        
        // verification
        MessageContext messageContext = context.getMessageContext();
        Message[] messages = messageContext.getAllMessages();

        assertThat(messages.length, is(1));
        Message message = messages[0];
        assertThat(message.getSource().toString(), is("interestRate"));
        assertThat(message.getText().toString(), containsString("loanAccountFormBean.InterestRate.invalid"));
    }
    
    @Test
    public void shouldContainValidationMessageOnNumberOfInstallmentsFieldWhenNumberOfInstallmentsViolatesAllowedRange() {

        // setup
        loanAccountFormBean.setNumberOfInstallments(100);
        
        // exercise test
        loanAccountFormBean.validateEnterAccountDetailsStep(context);
        
        // verification
        MessageContext messageContext = context.getMessageContext();
        Message[] messages = messageContext.getAllMessages();

        assertThat(messages.length, is(1));
        Message message = messages[0];
        assertThat(message.getSource().toString(), is("numberOfInstallments"));
        assertThat(message.getText().toString(), containsString("loanAccountFormBean.NumberOfInstallments.invalid"));
    }
    
    @Test
    public void shouldContainValidationMessageOnDisbursementDateDayFieldWhenDateIsInvalid() {

        // setup
        loanAccountFormBean.setDisbursalDateDay(32);
        loanAccountFormBean.setDisbursalDateMonth(1);
        loanAccountFormBean.setDisbursalDateYear(2011);
        
        // exercise test
        loanAccountFormBean.validateEnterAccountDetailsStep(context);
        
        // verification
        MessageContext messageContext = context.getMessageContext();
        Message[] messages = messageContext.getAllMessages();

        assertThat(messages.length, is(1));
        Message message = messages[0];
        assertThat(message.getSource().toString(), is("disbursalDateDay"));
        assertThat(message.getText().toString(), containsString("loanAccountFormBean.DisbursalDate.invalid"));
    }
    
    @Test
    public void shouldContainValidationMessageOnSourceOfFundsFieldWhenFieldIsMandatoryAndNotSelected() {

        // setup
        loanAccountFormBean.setSourceOfFundsMandatory(true);
        loanAccountFormBean.setFundId(null);
        
        // exercise test
        loanAccountFormBean.validateEnterAccountDetailsStep(context);
        
        // verification
        MessageContext messageContext = context.getMessageContext();
        Message[] messages = messageContext.getAllMessages();

        assertThat(messages.length, is(1));
        Message message = messages[0];
        assertThat(message.getSource().toString(), is("fundId"));
        assertThat(message.getText().toString(), containsString("loanAccountFormBean.SourceOfFunds.invalid"));
    }
    
    @Test
    public void shouldContainValidationMessageOnLoanPurposeFieldWhenFieldIsMandatoryAndNotSelected() {

        // setup
        loanAccountFormBean.setPurposeOfLoanMandatory(true);
        loanAccountFormBean.setLoanPurposeId(null);
        
        // exercise test
        loanAccountFormBean.validateEnterAccountDetailsStep(context);
        
        // verification
        MessageContext messageContext = context.getMessageContext();
        Message[] messages = messageContext.getAllMessages();

        assertThat(messages.length, is(1));
        Message message = messages[0];
        assertThat(message.getSource().toString(), is("loanPurposeId"));
        assertThat(message.getText().toString(), containsString("loanAccountFormBean.PurposeOfLoan.invalid"));
    }
}
