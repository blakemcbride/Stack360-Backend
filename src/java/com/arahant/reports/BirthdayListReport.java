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


package com.arahant.reports;

import com.arahant.beans.Employee;
import com.arahant.beans.HrEmployeeStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BirthdayListReport extends ReportBase {

	public BirthdayListReport() {
		super("birthday", "Birthday List");
	}

	public String build(int dateFrom, int dateTo, int month, String[] statusIds) {
		try {
			PdfPTable table = makeTable(new int[]{10, 10, 80});

			String monthFormatted;
			if (month == 0) {
				monthFormatted = "January";
			} else if (month == 1) {
				monthFormatted = "February";
			} else if (month == 2) {
				monthFormatted = "March";
			} else if (month == 3) {
				monthFormatted = "April";
			} else if (month == 4) {
				monthFormatted = "May";
			} else if (month == 5) {
				monthFormatted = "June";
			} else if (month == 6) {
				monthFormatted = "July";
			} else if (month == 7) {
				monthFormatted = "August";
			} else if (month == 8) {
				monthFormatted = "September";
			} else if (month == 9) {
				monthFormatted = "October";
			} else if (month == 10) {
				monthFormatted = "November";
			} else {
				monthFormatted = "December";
			}

			if (month > 0) {
				writeHeaderLine("Month", monthFormatted);
			} else {
				SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
				writeHeaderLine("Date", f.format(DateUtils.getDate(dateFrom)) + " to " + f.format(DateUtils.getDate(dateTo)) );
			}
			Collection<HrEmployeeStatus> statuses = new ArrayList<HrEmployeeStatus>();
			if (statusIds.length > 0) {
				String statusesFormatted = "";
				for (String statusId : statusIds) {
					HrEmployeeStatus status = hsu.get(HrEmployeeStatus.class, statusId);

					statuses.add(status);

					if (statusesFormatted.length() > 0) {
						statusesFormatted += ", ";
					}
					statusesFormatted += status.getName();
				}
				writeHeaderLine("Statuses", statusesFormatted);
			}

			addHeaderLine();

			writeColHeader(table, "Day", Element.ALIGN_RIGHT);
			writeColHeader(table, " ");
			writeColHeader(table, "Name", Element.ALIGN_LEFT);
			table.setHeaderRows(1);

			// build query
			HibernateCriteriaUtil<Employee> hcu = hsu.createCriteria(Employee.class);

			if (statusIds.length > 0) {
				hcu.employeeCurrentStatusIn(statusIds, DateUtils.now());
			}

			// actually process query results
			List<Employee> employees = hcu.list();
			Collections.sort(employees, new EmployeeComparator());
			boolean alternateRow = false;
			String birthDateFrom = "";
			String birthDateTo = "";
			int birthDateFromInteger = 0;
			int birthDatetoInteger = 0;
			if (dateFrom > 0) {
				birthDateFrom = (dateFrom + "").substring(4);
				birthDateTo = (dateTo + "").substring(4);
				birthDateFromInteger = Integer.parseInt(birthDateFrom);
				birthDatetoInteger = Integer.parseInt(birthDateTo);
			}

			int dobInteger = 0;
			int birthDateMonth = 0;

			for (Employee employee : employees) {
				String birthDate = employee.getDob() + "";
				//strip out the month and day
				if (birthDate.length() > 2) {
					birthDate = birthDate.substring(4);
					dobInteger = Integer.parseInt(birthDate);
					birthDateMonth = Integer.parseInt(birthDate.substring(0, 2));
				}

				//if we have month then report by month
				if (month > 0) {
					if (month == (birthDateMonth-1)) {
						super.write(table, DateUtils.getCalendar(employee.getDob()).get(Calendar.DAY_OF_MONTH), alternateRow);
						super.write(table, " ", alternateRow);
						super.write(table, employee.getNameLFM(), alternateRow);
						alternateRow = !alternateRow;
						System.out.println("DOB MONTH " + birthDate);
					}
					//else date range
				} else {
					if (dobInteger >= birthDateFromInteger && dobInteger <= birthDatetoInteger) {
						super.write(table, DateUtils.getCalendar(employee.getDob()).get(Calendar.DAY_OF_MONTH), alternateRow);
						super.write(table, " ", alternateRow);
						super.write(table, employee.getNameLFM(), alternateRow);
						alternateRow = !alternateRow;
						System.out.println("DOB range " + birthDate);
					}
				}
			}

			addTable(table);
		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();
		}

		return getFilename();
	}

	private class EmployeeComparator implements Comparator<Employee> {

		public int compare(Employee employee1, Employee employee2) {
			int emp1Day = DateUtils.getCalendar(employee1.getDob()).get(Calendar.DAY_OF_MONTH);
			int emp2Day = DateUtils.getCalendar(employee2.getDob()).get(Calendar.DAY_OF_MONTH);

			return emp1Day == emp2Day ? (employee1.getNameLFM().compareTo(employee2.getNameLFM())) : (emp1Day < emp2Day ? -1 : 1);
		}
	}

	public static void main(String args[]) {
		try {
			new BirthdayListReport().build(20100801, 20100831, 0, new String[]{"00001-0000081937", "00001-0000081937"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
