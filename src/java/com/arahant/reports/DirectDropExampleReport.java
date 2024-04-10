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

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;

/**
 * 
 * Created on Oct 27, 2008
 *
 */
public class DirectDropExampleReport extends ReportBaseDirectDrop {
	private int lines;
	private static final ArahantLogger logger=new ArahantLogger(DirectDropExampleReport.class);
	
	public DirectDropExampleReport(int lines) throws ArahantException {
		super("DDExample", "/Users/arahant/Desktop/instructions_form.pdf");
		this.lines = lines;
	}
	
	@Override
	public void build() throws Exception {
		for (int idx = 0; idx < lines; idx++) {
			int x = 20;
			int y = 600 + (20 * (idx + 1));
			
			super.writeTextLeft("This text is left-aligned on the point " + x + ", " + y, x, y);
		}
			
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		super.nextDirectDropPage();
		
		super.writeTextRotated("Inspector Number 9", 95, 490, 270);
	}
	
	public static void main(String[] args) {
		try	{
			logger.info(new DirectDropExampleReport(2).executeReport());
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
}

	
