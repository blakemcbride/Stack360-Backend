/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

package com.arahant.business;

import com.arahant.beans.ProjectTraining;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;

/**
 * Author: Blake McBride
 * Date: 12/16/18
 */
public class BProjectTraining extends BusinessLogicBase implements IDBFunctions {

    private static final ArahantLogger logger = new ArahantLogger(BProjectTraining.class);
    private ProjectTraining projectTraining;

    public BProjectTraining(String projectTrainingId) {
        internalLoad(projectTrainingId);
    }

    public String getProjectTrainingId() {
        return projectTraining.getProjectTrainingId();
    }

    public BProjectTraining setProjectTrainingId(String projectTrainingId) {
        projectTraining.setProjectTrainingId(projectTrainingId);
        return this;
    }

    public BProject getProject() {
        return new BProject(projectTraining.getProject());
    }

    public BProjectTraining setProject(BProject project) {
        projectTraining.setProject(project.getBean());
        return this;
    }

    public BHRTrainingCategory getTrainingCategory() {
        return new BHRTrainingCategory(projectTraining.getTrainingCategory());
    }

    public BProjectTraining setTrainingCategory(BHRTrainingCategory trainingCategory) {
        projectTraining.setTrainingCategory(trainingCategory.getHrTrainingCategory());
        return this;
    }

    public char getRequired() {
        return projectTraining.getRequired();
    }

    public BProjectTraining setRequired(char required) {
        projectTraining.setRequired(required);
        return this;
    }

    @Override
    public void update() throws ArahantException {
        ArahantSession.getHSU().saveOrUpdate(projectTraining);
    }

    @Override
    public void insert() throws ArahantException {
        ArahantSession.getHSU().insert(projectTraining);
    }

    @Override
    public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
        ArahantSession.getHSU().delete(projectTraining);
    }

    @Override
    public String create() throws ArahantException {
        projectTraining = new ProjectTraining();
        projectTraining.generateId();
        return projectTraining.getProjectTrainingId();
    }

    private void internalLoad(final String key) throws ArahantException {
        projectTraining = ArahantSession.getHSU().get(ProjectTraining.class, key);
    }

    @Override
    public void load(String key) throws ArahantException {
        internalLoad(key);
    }
}
