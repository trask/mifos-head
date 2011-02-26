[#ftl]
[#--
* Copyright (c) 2005-2011 Grameen Foundation USA
*  All rights reserved.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
*  implied. See the License for the specific language governing
*  permissions and limitations under the License.
*
*  See also http://www.apache.org/licenses/LICENSE-2.0.html for an
*  explanation of the license and how it is applied.
--]

[@layout.webflow currentTab="ClientsAndAccounts"
                 currentState="createLoanAccount.flowState.enterAccountInfo" 
                 states=["createLoanAccount.flowState.selectCustomer", 
                         "createLoanAccount.flowState.enterAccountInfo",
                         "createLoanAccount.flowState.reviewInstallments", 
                         "createLoanAccount.flowState.reviewAndSubmit"]]

<h1>[@spring.message "createLoanAccount.wizard.title" /] - <span class="standout">[@spring.message "createLoanAccount.selectProduct.pageSubtitle" /]</span></h1>
<p>[@spring.message "createLoanAccount.selectProduct.instructions" /]</p>
<p><span class="mandatory">*</span>[@spring.message "requiredFieldsInstructions" /]</p>
<br/>

<p><span class="standout">[@spring.message "selectProduct.accountOwnerName" /]</span> ${loanCreationProductDetailsDto.customerDetailDto.displayName}</p>
<br/>

[@form.errors "selectProductFormBean.*"/]
<form action="${flowExecutionUrl}" method="post" class="two-columns">
    <fieldset>
    <div class="row">
        [@form.label "productId" true][@spring.message "selectProduct.selectProductLabel" /][/@form.label]
        [@form.singleSelectWithPrompt path="selectProductFormBean.productId" options=loanCreationProductDetailsDto.productOptions id="createloanaccount.select.loanProduct" selectPrompt="selectPrompt" /]
    </div>
    </fieldset>
    <div class="row webflow-controls">
        [@form.submitButton label="widget.form.buttonLabel.continue" id="createloanaccount.button.continue" webflowEvent="productSelected" /]
        [@form.cancelButton label="widget.form.buttonLabel.cancel" webflowEvent="cancel" /]
    </div>
</form>

[/@layout.webflow]