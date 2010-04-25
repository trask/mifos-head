/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
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

package org.mifos.application.servicefacade;

import java.util.List;
import java.util.Map;

import org.mifos.application.master.business.CustomFieldDto;
import org.mifos.customers.client.business.ClientFamilyDetailDto;
import org.mifos.customers.client.business.ClientNameDetailDto;
import org.mifos.customers.util.helpers.CustomerDetailDto;

public class ClientFamilyInfoDto {

    private final ClientDropdownsDto clientDropdowns;
    private final List<CustomFieldDto> customFieldDtos;
    private final CustomerDetailDto customerDetail;
    private final ClientDetailDto clientDetail;
    private final List<ClientNameDetailDto> familyMembers;
    private final Map<Integer, List<ClientFamilyDetailDto>> clientFamilyDetails;

    public ClientFamilyInfoDto(ClientDropdownsDto clientDropdowns, List<CustomFieldDto> customFieldDtos, CustomerDetailDto customerDetail, ClientDetailDto clientDetail, List<ClientNameDetailDto> familyMembers, Map<Integer, List<ClientFamilyDetailDto>> clientFamilyDetails) {
        this.clientDropdowns = clientDropdowns;
        this.customFieldDtos = customFieldDtos;
        this.customerDetail = customerDetail;
        this.clientDetail = clientDetail;
        this.familyMembers = familyMembers;
        this.clientFamilyDetails = clientFamilyDetails;
    }

    public ClientDropdownsDto getClientDropdowns() {
        return this.clientDropdowns;
    }

    public List<CustomFieldDto> getCustomFieldViews() {
        return this.customFieldDtos;
    }

    public CustomerDetailDto getCustomerDetail() {
        return this.customerDetail;
    }

    public ClientDetailDto getClientDetail() {
        return this.clientDetail;
    }

    public List<ClientNameDetailDto> getFamilyMembers() {
        return this.familyMembers;
    }

    public Map<Integer, List<ClientFamilyDetailDto>> getClientFamilyDetails() {
        return this.clientFamilyDetails;
    }
}