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

package com.arahant.edi;

import com.arahant.utils.DateUtils;
import org.kissweb.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Blake McBride
 */
public class EDI {
    private BufferedWriter out;
    private boolean In_X12;
    private char X12_Field_delim;
    private char X12_Record_delim;
    private char X12_eol;
    
    public int X12_numb_segments;
    public int X12_numb_st_segments;
    public int X12_numb_gs_segments;
    
    public EDI(String file) throws IOException {
		out = new BufferedWriter(new PrintWriter(file));
        X12_reset();
    }
    
    public final void close() {
        try {
			out.close();
		}
		catch(IOException ioe) {
			System.err.println("Failed to close file.");
		}
    }
    
    private final boolean StartsWith(String p, String x) {
        return startsWithIgnoreCase(p, x);
    }
    
    private final boolean isrtn(char x) {
        return x == '\r'  ||  x == '\n';
    }
    
    private final boolean isend(char x) {
        return x == X12_Record_delim;
    }
    
    private final boolean issep(char x) {
        return x == X12_Field_delim  ||  isend(x);
    }
    
    private void X12_reset() {
	X12_eol = X12_Field_delim = X12_Record_delim = '\0';
	In_X12 = false;
	X12_numb_gs_segments = X12_numb_st_segments = X12_numb_segments = 0;        
    }
    
    /**
     * This method is used to initialize the particular EDI characteristics.
     * 
     * @param fld the field delimiter character to use
     * @param rec  the record delimiter character to use
     * @param eol the end-of-line character to use (this can be '\0' if none)
     * <br><br>
     * Sample values are:<br>
     * <br>
     *    fld rec eol<br>
     *      ~  \n  \0<br>
     *      *  \\  \n
     */
    
    public void X12_setup(char fld, char rec, char eol) {
	X12_reset();
	X12_Field_delim = fld;
	X12_Record_delim = rec;
	X12_eol = eol;        
    }
    
    private final boolean startsWithIgnoreCase(String s1, String s2) {
        int len = s2.length();
        if (s1.length() < len)
            len = s1.length();
        s1 = s1.substring(0, len);
        return s1.equalsIgnoreCase(s2);
    }

    private boolean X12_is_type(String rec, String txt) {
        if (txt.equalsIgnoreCase("ISA"))
            return X12_is_start(rec);
        if (X12_Field_delim == '\0')
            return false;
        return startsWithIgnoreCase(rec, txt)  &&  issep(rec.charAt(txt.length()));
    }
    
    private boolean X12_is_end(String rec) {
        return X12_is_type(rec, "IEA");
    }
/*    
    public boolean X12_is_type2(String rec, String f1, String f2) {
        if (!X12_is_type(rec, f1))
            return false;
        String seg = X12_get_field(rec, 1);
        return f2.equalsIgnoreCase(seg);
    }
*/
    private boolean X12_is_start(String buf) {
        if (!StartsWith(buf, "ISA"))
            return false;
        int len;
        for (len=0 ; len < buf.length()  &&  buf.charAt(len) != '\r'  &&  buf.charAt(len) != '\n'  && len < 110 ; )
            len++;
        if (len < 105  ||  buf.length() <= 105)
            return false;
        X12_Field_delim = buf.charAt(3);
        X12_Record_delim = buf.charAt(105);
        In_X12 = true;
        return true;
    }
    
    private void X12_date_out(int date) {
        if (date == 0) {
            ST(0, "");
            return;
        }
        int y = DateUtils.year(date);
        int m = DateUtils.month(date);
        int d = DateUtils.day(date);
        String buf = StringUtils.sprintf("%02d%02d%02d", y%100, m, d);
        ST(0, buf);
    }
    
    private void X12_date_out8(int date) {
        if (date == 0) {
            ST(0, "");
            return;
        }
        int y = DateUtils.year(date);
        int m = DateUtils.month(date);
        int d = DateUtils.day(date);
        String buf = StringUtils.sprintf("%04d%02d%02d", y, m, d);
        ST(0, buf);
    }
    
    private void X12_cur_date() {
        int date = DateUtils.today();
        int y = DateUtils.year(date);
        int m = DateUtils.month(date);
        int d = DateUtils.day(date);
        String buf = StringUtils.sprintf("%02d%02d%02d", y%100, m, d);
        ST(0, buf);
    }
    
    private void X12_cur_time() {
        Calendar c = Calendar.getInstance();
        int h = c.get(c.HOUR_OF_DAY);
        int m = c.get(c.MINUTE);
        String buf = StringUtils.sprintf("%02d%02d", h, m);
        ST(0, buf);
    }
    
    private void X12_cur_date8() {
        int date = DateUtils.today();
        int y = DateUtils.year(date);
        int m = DateUtils.month(date);
        int d = DateUtils.day(date);
        String buf = StringUtils.sprintf("%04d%02d%02d", y, m, d);
        ST(0, buf);
    }
    
    /**
     * This method is used to make sure a string doesn't contain any field or record delimiters in it.
     * If it does, they will be replaces with spaces.
     * 
     * @param s input string
     * @return a new string with the field and record delimiters replaces with spaces
     */
        
    public String X12_makestr(String s) {
        StringBuilder p = new StringBuilder();
        for (int i=0, m=s.length() ; i < m ; i++) {
            char c = s.charAt(i);
            if (c == X12_Field_delim  ||  c == X12_Record_delim  ||  c == X12_eol  ||  c == ':'  ||
                    Character.isISOControl(c))
                p.append(' ');
            else
                p.append(c);
        }
        return StringUtils.rightStrip((new String(p)).toUpperCase());
    }
    
    private void PUTDELIM() {
		try {
			out.write(X12_Field_delim);
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to introduce a new segment.  It should be the first function called for each new segment.
     * This function would be followed by any of the other field output routines and then terminated with a call to EOS().
     * 
     * @param s the new segment ( "ISA", etc. )
     */
    
    public final void SEG(String s) {
        if(s.equalsIgnoreCase("ST")) {
            X12_numb_st_segments++;
			X12_numb_segments = 0;
        }
		else if(s.equalsIgnoreCase("GS")) {
			X12_numb_gs_segments++;
			X12_numb_st_segments = 0;
		}
		X12_numb_segments++;
		try {
			out.write(s);
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
	
	public static final String makecn(long cn)
	{
		return String.format("%09d", cn);
	}
	
    /**
     * This method is used to output a character field.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param c the character to be output
     */
    
    public final void CH(int n, char c) {
        PUTDELIM();
		try {
			out.write(c);
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output a variable width string field.  The length of the field is the length of the string.
     * The string is NOT processed for reserved characters.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param s the string to be output
     */
    
    public final void ST(int n, String s) {
        PUTDELIM();
		try {
			out.write(s);
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output a fixed width string field.  The output string will be truncated or extended to the indicated width.
     * The string is NOT processed for reserved characters.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param s the string to be output
     * @param w the width of the field
     */
    
    public final void SW(int n, String s, int w) {
        PUTDELIM();
		try {
			out.write(String.format("%-" + w + "s", s));
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output a variable width string field.  The length of the field is the length of the string.
     * The string is first processed to eliminate reserved characters.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param s the string to be output
     */
    
    public final void SF(int n, String s) {
        ST(n, X12_makestr(s));
    }
    
    /**
     * This method is used to output a fixed width string field.  The output string will be truncated or extended to the indicated width.
     * The string is first processed to eliminate reserved characters.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param s the string to be output
     * @param w the width of the field
     */
    
    public final void SV(int n, String s, int w) {
        SW(n, X12_makestr(s), w);
    }
    
    /**
     * This method is used to output the current date using a 2 digit year format.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     */
    
    public final void CD(int n) {
        X12_cur_date();
    }
    
    /**
     * This method is used to output the current time.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     */
    
    public final void CT(int n) {
        X12_cur_time();
    }
    
    /**
     * This method is used to output the current date using a 4 digit year format.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     */
    
    public final void D8(int n) {
        X12_cur_date8();
    }
    
    /**
     * This method is used to output a date using a 2 digit year format.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param dt the date.  Format is an integer in the YYYYMMDD format.
     */
    
    public final void DT(int n, int dt) {
        X12_date_out(dt);
    }
    
    /**
     * This method is used to output a date using a 4 digit year format.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param dt the date.  Format is an integer in the YYYYMMDD format.
     */
    
    public final void DC(int n, int dt) {
        X12_date_out8(dt);
    }
    
    /**
     * This method is used to output a double with 2 decimal places utilizing a variable width field.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param i the number to be output
     */
    
    public final void N2(int n, double i) {
        PUTDELIM();
		try {
			out.write(String.format("%.2f", i));
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output an integer utilizing a variable width field.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param i the number to be output
     */
    
    public final void NL(int n, int i) {
        PUTDELIM();
		try {
			out.write(String.format("%d", i));
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output an integer utilizing a fixed width field.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param i the number to be output
     * @param w the width of the field
     */
    
    public final void LW(int n, int i, int w) {
        PUTDELIM();
		try {
			out.write(String.format("%0*ld", w, i));
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output a (variable width) null value field.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     */
    
    public final void NV(int n) {
        PUTDELIM();
    }
    
    /**
     * This method is used to indicate the end of a segment.  It must be called at the end of each segment.
     */
    
    public final void EOS() {
		try {
			out.write(X12_Record_delim);
			if (X12_eol != '\0')
				out.write(X12_eol);
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output a double with a user supplied number of decimal places and fixed field width.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param i the number to be output
     * @param dp the number of decimal places
     * @param fw the fixed field width
     */
    
    public final void DL(int n, double i, int dp, int fw) {
        PUTDELIM();
		try {
			out.write(String.format("%*.*f", fw, dp, i));
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
    
    /**
     * This method is used to output a double with 3 decimal places utilizing a variable width field.
     * 
     * @param n the field number.  It is not actually used but only serves to document your code.
     * @param i the number to be output
     */
    
    public final void N3(int n, double i) {
        PUTDELIM();
		try {
			out.write(String.format("%.3f", i));
		} catch (IOException ex) {
			Logger.getLogger(EDI.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Could not write to file.");
		}
    }
}
