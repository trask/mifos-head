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

package org.mifos.customers.office.business.service;

import java.util.List;

import org.mifos.customers.office.business.OfficeBO;
import org.mifos.customers.office.business.OfficeDetailsDto;
import org.mifos.customers.office.persistence.OfficePersistence;
import org.mifos.customers.office.util.helpers.OfficeLevel;
import org.mifos.customers.personnel.business.PersonnelBO;
import org.mifos.framework.business.AbstractBusinessObject;
import org.mifos.framework.business.service.BusinessService;
import org.mifos.framework.exceptions.PersistenceException;
import org.mifos.framework.exceptions.ServiceException;
import org.mifos.security.util.UserContext;

public class OfficeBusinessService implements BusinessService {

    private OfficePersistence officePersistence = new OfficePersistence();

    @Override
    public AbstractBusinessObject getBusinessObject(UserContext userContext) {
        return null;
    }

    public List<OfficeDetailsDto> getActiveParents(OfficeLevel level, Short localeId) throws ServiceException {
        try {
            return officePersistence.getActiveParents(level, localeId);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeDetailsDto> getConfiguredLevels(Short localeId) throws ServiceException {
        try {
            return officePersistence.getActiveLevels(localeId);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }

    }

    public OfficeBO getOffice(Short officeId) throws ServiceException {
        try {
            return officePersistence.getOffice(officeId);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeDetailsDto> getStatusList(Short localeId) throws ServiceException {
        try {
            return officePersistence.getStatusList(localeId);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeBO> getOfficesTillBranchOffice() throws ServiceException {
        try {
            return officePersistence.getOfficesTillBranchOffice();
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeBO> getBranchOffices() throws ServiceException {
        try {
            return officePersistence.getBranchOffices();
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeDetailsDto> getChildOffices(String searchId) throws ServiceException {
        try {
            return officePersistence.getChildOffices(searchId);
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeBO> getActiveBranchesUnderUser(PersonnelBO personnel) throws ServiceException {
        try {
            return officePersistence.getActiveBranchesUnderUser(personnel.getOfficeSearchId());
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    public List<OfficeBO> getAllofficesForCustomFIeld() throws ServiceException {
        try {
            return officePersistence.getAllofficesForCustomFIeld();
        } catch (PersistenceException pe) {
            throw new ServiceException(pe);
        }
    }
}
