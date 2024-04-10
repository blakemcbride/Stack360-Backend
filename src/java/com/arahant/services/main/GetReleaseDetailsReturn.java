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

package com.arahant.services.main;

import com.arahant.services.TransmitReturnBase;
import org.kissweb.StringUtils;

import java.util.Date;

public class GetReleaseDetailsReturn extends TransmitReturnBase {
	private String detail;
	private String name;
	private String version;
	private String buildDate;
	private String sourceCodeRevisionNumber;
	private String sourceCodeRevisionPath;
	private String applicationPath;
	private String database;
    
    private long maxMemory;                     // max Java will ever allocate from the OS
    private long totalMemory;                   // total amount of memory currently allocated from the OS
    private long freeMemory;                    // amount of memory allocated from the OS but not being used by the application
    private long usedMemory;                    // amount of memory used by the application
	private static long memoryStatPeriodStart;  // when the working memory set calc period started in seconds
	private static long workingSetMinimum;      // the minimum amount of memory used by the system during the calc period
	private static long workingSetMaximum;      // the maximum amount of memory used by the system during the calc period
	private String timeToNextReset;             // time left in the calc period shown as MM:SS
	private static long maxUsed;                // the maximum amount of memory used by the system
	private static long minUsed;                // the minimum amount of memory used by the system
    
    void setMemoryStats() {
		final long updateFrequency = 3600L; // in seconds, 1 hour
        final Runtime rt = Runtime.getRuntime();
        maxMemory = rt.maxMemory();
        totalMemory = rt.totalMemory();
        freeMemory = rt.freeMemory();
        usedMemory = totalMemory - freeMemory;
		long memoryStatTime = (new Date()).getTime() / 1000L;
		if (memoryStatPeriodStart == 0L || memoryStatTime - memoryStatPeriodStart > updateFrequency) {
			workingSetMinimum = workingSetMaximum = 0L;
			memoryStatPeriodStart = memoryStatTime;
		}
		workingSetMinimum = workingSetMinimum == 0 ? usedMemory : Math.min(workingSetMinimum, usedMemory);
		workingSetMaximum = Math.max(workingSetMaximum, usedMemory);

		long secondsTillNextReset = updateFrequency - (memoryStatTime - memoryStatPeriodStart);
		long minutesTillNextReset = secondsTillNextReset / 60L;
		timeToNextReset = minutesTillNextReset + ":" + StringUtils.take("0" + (secondsTillNextReset % 60L), -2);

		maxUsed = Math.max(maxUsed, usedMemory);
		minUsed = minUsed == 0 ? usedMemory : Math.min(minUsed, usedMemory);
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail=detail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getSourceCodeRevisionNumber() {
		return sourceCodeRevisionNumber;
	}

	public void setSourceCodeRevisionNumber(String sourceCodeRevisionNumber) {
		this.sourceCodeRevisionNumber = sourceCodeRevisionNumber;
	}

	public String getSourceCodeRevisionPath() {
		return sourceCodeRevisionPath;
	}

	public void setSourceCodeRevisionPath(String sourceCodeRevisionPath) {
		this.sourceCodeRevisionPath = sourceCodeRevisionPath;
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

	public long getMemoryStatPeriodStart() {
		return memoryStatPeriodStart;
	}

	public void setMemoryStatPeriodStart(long memoryStatPeriodStart) {
		GetReleaseDetailsReturn.memoryStatPeriodStart = memoryStatPeriodStart;
	}

	public long getWorkingSetMinimum() {
		return workingSetMinimum;
	}

	public void setWorkingSetMinimum(long workingSetMinimum) {
		GetReleaseDetailsReturn.workingSetMinimum = workingSetMinimum;
	}

	public long getWorkingSetMaximum() {
		return workingSetMaximum;
	}

	public void setWorkingSetMaximum(long workingSetMaximum) {
		GetReleaseDetailsReturn.workingSetMaximum = workingSetMaximum;
	}

	public String getTimeToNextReset() {
		return timeToNextReset;
	}

	public void setTimeToNextReset(String timeToNextReset) {
		this.timeToNextReset = timeToNextReset;
	}

	public long getMaxUsed() {
        return maxUsed;
    }

	public void setMaxUsed(long maxUsed) {
		GetReleaseDetailsReturn.maxUsed = maxUsed;
    }

	public long getMinUsed() {
        return minUsed;
    }

	public void setMinUsed(long minUsed) {
		GetReleaseDetailsReturn.minUsed = minUsed;
    }


}

	
