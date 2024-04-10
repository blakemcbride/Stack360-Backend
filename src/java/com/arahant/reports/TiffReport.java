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
import com.arahant.utils.FileSystemUtils;
import com.lowagie.tools.plugins.Tiff2Pdf;
import java.io.File;
import java.io.FileOutputStream;


public class TiffReport {

	private String fixFilename(String fn) {
		
		//  remove file extension
		int idx = fn.lastIndexOf(".");
		if (idx > 1)
			fn = fn.substring(0, idx);
		
		//  remove file path
		idx = fn.lastIndexOf("/");
		if (idx > 1)
			fn = fn.substring(idx+1);
		
		return fn.replace(",", "").replace("'", "").replace(" ", "").replace(".", "").replace("-", "_");
	}

	/**
	 * Creates a file with the given filename and extension containing the
	 * provided contents.  If the contents are in TIFF format, it is
	 * converted to PDF format.
	 *
	 * @param contents contents of the file
	 * @param extension
	 * @param filename
	 * @return The name of the file created
	 * @throws ArahantException
	 */
	public String tifToPdf(final byte[] contents, String extension, String filename) throws ArahantException {

		try {
			filename = fixFilename(filename);

			final File pdf = FileSystemUtils.createTempDirFile(filename + "." + extension.toLowerCase());

			byte[] b = contents;

			if (b.length > 4)
				if (b[0] == '%' && b[1] == 'P' && b[2] == 'D' && b[3] == 'F') {
					final FileOutputStream fos = new FileOutputStream(pdf);
					fos.write(contents);
					fos.flush();
					fos.close();
				} else
					//Tiff files start with II or MM
					if ((b[0] == 'I' && b[1] == 'I') || (b[0] == 'M' && b[1] == 'M')) {
						final File tif = FileSystemUtils.createTempFile("TiffRpt", ".tif");

						final FileOutputStream fos = new FileOutputStream(tif);
						fos.write(contents);
						fos.flush();
						fos.close();

						Tiff2Pdf.main(new String[]{tif.getAbsolutePath(), pdf.getAbsolutePath(), "ORIGINAL"});
					} else {
						final FileOutputStream fos = new FileOutputStream(pdf);
						fos.write(b);
						fos.flush();
						fos.close();
					}
			else {
				final FileOutputStream fos = new FileOutputStream(pdf);
				fos.write(b);
				fos.flush();
				fos.close();
			}
			return FileSystemUtils.getHTTPPath(pdf);
		} catch (final Exception e) {
			throw new ArahantException(e);
		}
	}
}
