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

package com.arahant.timertasks;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.utils.FileSystemUtils;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CheckRunawayServices extends TimerTaskBase {

	public static final int MINUTES = 30;

	private class ThreadData {
		private Thread thread;
		private String methodName;
		private Date startTime;

		private ThreadData(Thread thread, String name) {
			this.thread = thread;
			methodName = name;
			startTime = new Date();
		}
	}

	final HashMap<Long, ThreadData> threads = new HashMap<Long, ThreadData>();


	@Override
	@SuppressWarnings("deprecation")
	public void execute() throws Exception {

		//spin all the threads and complain if anybody has been running over X minutes

		FileWriter overrun = new FileWriter(FileSystemUtils.makeAbsolutePath("overrun.txt"), true);
		Date threshold = new Date();
		threshold.setTime(threshold.getTime() - (1000 * 60 * MINUTES));
		for (ThreadData d : threads.values()) {
			if (d.startTime.before(threshold)) {
				overrun.write(d.methodName + "\n");
				overrun.write("Stack trace:\n");
				for (StackTraceElement ste : d.thread.getStackTrace())
					overrun.write(ste.toString() + "\n");
				overrun.write("\n Other active thread traces:\n");
				for (StackTraceElement[] st : Thread.getAllStackTraces().values())
					for (StackTraceElement ste : st)
						overrun.write(ste.toString() + "\n");
				overrun.write("\n\n");
			}
		}

		//kill the thread

		threshold.setTime(threshold.getTime() - (1000 * 60 * (MINUTES * BProperty.getInt(StandardProperty.Kill_Thread_Multiplier, 48))));
		List<Long> killed = new LinkedList<Long>();
		for (ThreadData d : threads.values()) {
			if (d.startTime.before(threshold)) {
				//System.out.println("Killing thread");
                d.thread.interrupt();
//				d.thread.stop(new ArahantException("Process " + d.methodName + " was killed because it was taking too long."));  //yes it's deprecated but Interrupt is too gentle and doesn't kill infinite loop
				killed.add(d.thread.getId());
				//	System.out.println("Killed");

			}
		}
		for (long l : killed)
			threads.remove(l);
		overrun.close();
	}

	public void addService(Thread thread, String name) {
		try {
			synchronized (threads) {
				threads.put(thread.getId(), new ThreadData(thread, name));
			}
		} catch (Throwable t) {
			//don't let this stop a working service
		}
	}

	public void removeService(Thread thread) {
		try {
			synchronized (threads) {
				threads.remove(thread.getId());
			}
		} catch (Throwable t) {
			//don't let this stop a working service
		}
	}

	public static void main(String args[]) {
		for (StackTraceElement ste : Thread.currentThread().getStackTrace())
			System.out.println(ste.toString() + "\n");
	}

}
