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

package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;

import javax.persistence.*;

/**
 * Author: Blake McBride
 * Date: 12/16/18
 */
@Entity
@Table(name = ProjectTraining.TABLE_NAME)
public class ProjectTraining extends ArahantBean implements java.io.Serializable {

    public static final String TABLE_NAME = "project_training";

    private String projectTrainingId;
    public static final String PROJECT_TRAINING_ID = "projectTrainingId";
    private Project project;
    public static final String PROJECT = "project";
    private HrTrainingCategory trainingCategory;
    public static final String TRAINING_CATEGORY = "trainingCategory";
    private char required;

    public ProjectTraining () { }

    @Id
    @Column(name = "project_training_id")
    public String getProjectTrainingId() {
        return projectTrainingId;
    }

    public void setProjectTrainingId(String projectTrainingId) {
        this.projectTrainingId = projectTrainingId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    public HrTrainingCategory getTrainingCategory() {
        return trainingCategory;
    }

    public void setTrainingCategory(HrTrainingCategory trainingCategory) {
        this.trainingCategory = trainingCategory;
    }

    public char getRequired() {
        return required;
    }

    public void setRequired(char required) {
        this.required = required;
    }

    @Override
    public String tableName() {
        return TABLE_NAME;
    }

    @Override
    public String keyColumn() {
        return "project_training_id";
    }

    @Override
    public String generateId() throws ArahantException {
        setProjectTrainingId(IDGenerator.generate(this));
        return projectTrainingId;
    }
}
