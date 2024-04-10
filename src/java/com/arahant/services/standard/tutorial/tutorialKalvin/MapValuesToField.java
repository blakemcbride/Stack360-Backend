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

/*
*/


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.services.standard.tutorial.tutorialKalvin;

/**
 *
 * Arahant
 */
public class MapValuesToField {
    public static void main(String[] args) {
		//make there is NO ) in the data, just around the fields
		//make there is NO ) in the data, just around the fields
		//make there is NO ) in the data, just around the fields
		String data = "(cobra_acceptance_date, amount_covered, amount_paid, amount_paid_source, amount_paid_type, benefit_approved, bcr_id, benefit_declined, change_description, comments, coverage_end_date, coverage_start_date, covered_person, benefit_id, benefit_cat_id, benefit_config_id, insurance_id, life_event_id, max_months_on_cobra, other_insurance, other_insurance_is_primary, paying_person, policy_end_date, policy_start_date, project_id, record_change_date, record_change_type, record_person_id, relationship_id, cobra, benefit_join_id) values (0, 0.0, 0.0, C, F, Y, 00001-0000000001, N, Miscellaneous, , 0, 0, 00001-0000387818, NULL, NULL, 00001-0000116272, , NULL, 0, , N, 00001-0000387818, 0, 0, NULL, 2010-07-23 11:10:25.269000 -05:00, N, 00000-0000000000, NULL, N, 00001-0000471418)";
        
		String fields = data.substring(0,data.indexOf(")") +1); // "(cobra_acceptance_date, amount_covered, amount_paid, amount_paid_source, amount_paid_type, benefit_approved, bcr_id, benefit_declined, change_description, comments, coverage_end_date, coverage_start_date, covered_person, benefit_id, benefit_cat_id, benefit_config_id, insurance_id, life_event_id, max_months_on_cobra, other_insurance, other_insurance_is_primary, paying_person, policy_end_date, policy_start_date, project_id, record_change_date, record_change_type, record_person_id, relationship_id, cobra, benefit_join_id) values ";
        String values = data.substring(data.indexOf(")") +1); //"(0, 0.0, 0.0, C, F, Y, 00001-0000000001, N, Miscellaneous, , 0, 0, 00001-0000387818, NULL, NULL, 00001-0000116272, , NULL, 0, , N, 00001-0000387818, 0, 0, NULL, 2010-07-23 11:10:25.269000 -05:00, N, 00000-0000000000, NULL, N, 00001-0000471418)";
        String[] fieldsArray = fields.split(",");
        String[] valueArray = values.split(",");

        int size = fieldsArray.length;
        for (int i=0; i<size; i++){
            System.out.println("Field: " + fieldsArray[i].trim() + " --> " + valueArray[i].trim());
        }
    }
}
