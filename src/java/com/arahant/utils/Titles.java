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



package com.arahant.utils;

import static org.kissweb.StringUtils.strLength;
import static org.kissweb.StringUtils.take;

/**
 *
 * @author Blake McBride
 */
public class Titles {
	
	/**
	 * Creates column headings
	 * 
	 * @param mp  character string, mask  "C10,B5,L20,C4"
	 * @param sp  character string source  "city,state,zip"
	 * @param d   delimiter character	for source,   ','
	 * @return a String formatted for the column title
	 */
	public static String CreateTitle(String mp, String sp, char d) {
		char [] m = mp.toCharArray();
		char [] s = sp.toCharArray();
		int q;
		char [] v = new char[301];
		int w, i=0, j, tw=300;
		char [] cw = new char[4];
		char t;
		char [] res = new char[300];
		
		int si = 0;  //  index into s
		int resi = 0; //  index into res
		
		while (si < s.length) {
			t = m[i++];
			for (q=0 ; i < m.length && m[i] != ',' ; i++)
				if (q < 3)
					cw[q++] = m[i];
			i = i < m.length ? i+1 : 0;
			cw[q] = '\0';
			w = Integer.parseInt(new String(cw));
			w = Math.min(w, tw);
			if (t != 'B') {
				for (q=0 ; si < s.length  &&  s[si] != d ; si++)
					if (q < tw)
						v[q++] = s[si];
				v[q] = '\0';
				if (si < s.length)
					si++;
			}
			tw -= w;
			switch (t) {
				case 'L':
					take(v, w);
					for (q=0 ; v[q] != '\0' ; )
						res[resi++] = v[q++];
					break;
				case 'R':
					take(v, -w);
					for (q=0 ; v[q] != '\0' ; )
						res[resi++] = v[q++];
					break;
				case 'C':
					q = strLength(v);
					j = w - q;
					take(v, -(q + Math.max(j, 0) / 2));
					take(v, w);
					for (q=0 ; q < v.length && v[q] != '\0';)
						res[resi++] = v[q++];
					break;
				case 'B':
					for (q=0 ; q++ < w ;)
						res[resi++] = ' ';
					break;
			}
		}
		res[resi] = '\0';
		return new String(res);
	}
	
	/**
	 * Creates underline string for column titles
	 * 
	 * @param ms  character string, mask, "C10,B5,L20,C8"
	 * @param d   character, underline character,  '='
	 * @return   the column title string
	 */
	public static String CreateUnderline(String ms, char d) {
		char [] m = ms.toCharArray();
		int w, i=0, q;
		char [] cw = new char[4];
		char t;
		char [] res = new char[300];
		int resi = 0;  // index into res
		
		while (i < m.length) {
			t = m[i++];
			for (q=0 ; i < m.length  &&  m[i] != ',' ; i++)
				if (q < 3)
					cw[q++] = m[i];
			i = i < m.length ? i+1 : i;
			cw[q]= '\0';
			w = Integer.parseInt(new String(cw));
			if (t == 'B')
				for (q=0 ; q++ < w ; )
					res[resi++] = ' ';
			else
				for (q=0 ; q++ < w ; )
					res[resi++] = d;
		}
		res[resi] = '\0';
		return new String(res);
	}
	
	
	public static void main(String argv[]) {
		String msk = "L10,B2,R10";
		String res = CreateTitle(msk, "ABC,DEF", ',');
		
		System.out.println(res);
		res = CreateUnderline(msk, '-');
		System.out.println(res);
		if (true)
			return;
		
		
		
		char [] v = new char[20];
		
		v[0] = 'B';
		v[1] = 'l';
		
		String s = new String(v);
		
		System.out.println(s + " (" + s.length() + ")");
	}
}
