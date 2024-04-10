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
package com.arahant.utils;

import org.kissweb.StringUtils;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class FixedLengthFileWriter {

    private FileWriter fw;
    private String dateFmt = "MM/dd/yyyy";

	public FixedLengthFileWriter(String name) throws IOException {
		this(name, false);
	}

    public FixedLengthFileWriter(String name, boolean append) throws IOException {
        fw = new FileWriter(name,append);
    }

     
    public void setDateFormat(String fmt) {
        dateFmt = fmt;
    }

    public void close() throws IOException {
        fw.flush();
        fw.close();
    }

    private void write(int length, String x) throws IOException {
        fw.write(StringUtils.take(x, length));
    }

    public void writeField(String x) throws IOException {
        if (x == null)
            x = "";
		write(x.length(), x);
    }

    public void writeFieldUpperCase(String x) throws Exception {
        if (x == null)
            x = "";
		write(x.length(), x.toUpperCase());
    }

	public void writeFieldUpperCase(int length, String x) throws Exception {
        if (x == null)
            x = "";
		write(length, x.toUpperCase());
    }

	//For proprietary EDI tracking purposes only
	public void writeField(int seq, int length, String x) throws IOException {
        writeField(length, x);
    }

	//For proprietary EDI tracking purposes only
	public void writeField(int seq, int length, char x) throws IOException {
        writeField(length, x + "");
    }

	//For proprietary EDI tracking purposes only
	public void writeField(int seq, int length, int x) throws IOException {
        writeField(length, x);
    }

	//For proprietary EDI tracking purposes only
	public void writeField(int seq, int length, double x) throws IOException {
        writeField(length, x);
    }

	public void writeField(int length, String x) throws IOException {
        write(length, x);
    }

    public void writeField(int length, double x) throws IOException {
        x *= 100;
        x = Math.round(x);
        x /= 100;
        write(length, x + "");
    }

    public void writeField(int length, int x) throws IOException {
        write(length, x + "");
    }

    public void writeField(int x) throws IOException {
        String str = x + "";
		write(str.length(), str);
    }

    public void endRecord() throws IOException, Exception {
        fw.write("\r\n");
    }

	public void writeField(char c) throws IOException {
		write(1, c + "");
	}
}
