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

package com.arahant.utils;

import com.arahant.business.BPropertyKissOnly;
import com.arahant.exceptions.ArahantException;
import org.kissweb.FileUtils;

import java.io.*;

/**
 * This class supports the ability to store files external to the SQL database.
 * <br>
 * Each record in any SQL table can have one or more external files associated with it.
 * These routines keep that relationship intact.
 * <br>
 * The ability to move the files to another machine or location is supported.
 * <br>
 * This class by itself has a serious limitation.  It doesn't handle SQL transactions.
 * What this means is that you could add an SQL record and then add an external file.
 * If the SQL transaction fails and gets rolled back, the external file will remain.
 * The complexity of handling this correctly is beyond the time constraints of the project and
 * will have to be addressed at a later date.
 * <br>
 * Author: Blake McBride<br>
 * Date: 10/12/23
 */
public class ExternalFile {

	private static class ExternalField {
		String tableName;  // the table be associated with
		String fieldName;  // the fictitious field name

		ExternalField(String tableName, String fieldName) {
			this.tableName = tableName;
			this.fieldName = fieldName;
		}
	}

	//  ****************************************************************************************************************

	// The following are the defined external files

	public static final ExternalField APPLICANT_APPLICATION_SIGNED_OFFER = new ExternalField("applicant_application", "signedOffer");
	public static final ExternalField APPLICANT_APPLICATION_OFFER_TEMPLATE = new ExternalField("applicant_application", "offertemplate");
	public static final ExternalField APPLICANT_APPLICATION_OFFER_LETTER = new ExternalField("applicant_application", "offerletter");

	public static final ExternalField APPLICANT_POSITION_OFFER_LETTER = new ExternalField("applicant_position", "offerletter");
	public static final ExternalField APPLICANT_POSITION_DESCRIPTION = new ExternalField("applicant_position", "description");

	public static final ExternalField PROJECT_TASK_PICTURE = new ExternalField("project_task_picture", null);

	public static final ExternalField PERSON_FORM = new ExternalField("person_form", null);

	public static final ExternalField MESSAGE_ATTACHMENT_ATTACHMENT = new ExternalField("message_attachment", "attachment");

	public static final ExternalField PROJECT_FORM_IMAGE = new ExternalField("project_form", "image");

	public static final ExternalField EXPENSE_RECEIPT_PICTURE1 = new ExternalField("expense_receipt", "picture1");
	public static final ExternalField EXPENSE_RECEIPT_PICTURE2 = new ExternalField("expense_receipt", "picture2");

	//  ****************************************************************************************************************


	/**
	 * This routine creates the absolute file name to be used when storing files external to the database.  It doesn't
	 * care if the file actually exists already or not.  It is just returning a name to be used.
	 * <br>
	 * System property <code>EXTERNAL_FILE_ROOT</code> determines the first part of the external file name.
	 * <br>
	 * The second part of the external file name is the passed in <code>tableName</code>.  This must be unique for
	 * each SQL table.  Using the SQL table name is suggested.
	 * <br>
	 * The third part of the external file name is built via the <code>primaryKey</code> argument passed in.  This would
	 * normally be the primary key value of the database table.
	 * <br>
	 * The fourth part of the external file name is built via the <code>fieldName</code> argument passed in. This
	 * represents a fictitious SQL column name that would have been used had it been stored directly in the SQL database.
	 * It allows multiple external files per record to be stored.
	 * <br>
	 * The last part of the external file name is built via the <code>extension</code> argument passed in.  It may  or
	 * may not contain the leading period.  If it is not present it will be added.
	 * <br>
	 * The external file path is calculated based on the passed in arguments and is guaranteed to be unique.
	 * The external files are stored in a way designed to be efficient to the file system.
	 *
	 * @param tableName  the SQL table name
	 * @param primaryKey primary key value for the given table
	 * @param fieldName  fictitious SQL column name (or NULL)
	 * @param extension file extension (or NULL)
	 * @param create     if true, create the directory if it doesn't exist
	 * @return the absolute path of the file on disk
	 *
	 */
	private static String makeExternalFilePath(String tableName, final String primaryKey, String fieldName, String extension, boolean create) {
		tableName = tableName.toLowerCase();
		if (fieldName == null)
			fieldName = "";
		if (!fieldName.isEmpty() &&  fieldName.charAt(0) != '-')
			fieldName = "-" + fieldName;
		extension = fileExtension(extension);
		final String dir = getExternalFileDir(tableName, primaryKey, create);
		return dir + "/" + primaryKey + fieldName + extension;
	}

	/**
	 * Generates an external file path based on the given field, field name, and extension.
	 *
	 * @param  field       the external field object
	 * @param  primaryKey  the primary key value
	 * @param  extension   the file extension
	 * @return             the generated external file path
	 *
	 * @see #makeExternalFilePath(String, String, String, String, boolean)
	 */
	public static String makeExternalFilePath(ExternalField field, String primaryKey, String extension) {
		return makeExternalFilePath(field.tableName, primaryKey, field.fieldName, extension, true);
	}

	/**
	 * Saves the data to an external file.
	 * If the file name is passed, the extension is calculated.
	 * <br><br>
	 * This method has been superseded by several of the other methods in this class.
	 * They should be used rather than this method when possible.
	 *
	 * @param  field      the ExternalField to save the data to
	 * @param  primaryKey the primary key value
	 * @param  extension  the file extension or the file name of the file
	 * @param  data       the data to be saved
	 * @throws IOException if there is an error writing the data to the file
	 */
	public static void saveData(ExternalField field, String primaryKey, String extension, String data) throws IOException {
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		FileUtils.write(fname, data);
	}

	/**
	 * Saves the data to an external file.
	 * If the file name is passed, the extension is calculated.
	 *
	 * @param  field      the ExternalField to save the data to
	 * @param  primaryKey the primary key value
	 * @param  extension  the file extension or the file name of the file
	 * @param  data       the data to be saved
	 * @throws IOException if there is an error writing the data to the file
	 */
	public static void saveData(ExternalField field, String primaryKey, String extension, byte [] data) throws IOException {
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		FileUtils.write(fname, data);
	}

	/**
	 * Saves the data to an external file.
	 * If the file name is passed, the extension is calculated.
	 *
	 * @param  field      the ExternalField to save the data to
	 * @param  primaryKey the primary key value
	 * @param  extension  the file extension or the file name of the file
	 * @param  data       the data to be saved
	 * @throws IOException if there is an error writing the data to the file
	 */
	public static void saveData(ExternalField field, String primaryKey, String extension, Byte [] data) throws IOException {
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		FileUtils.write(fname, data);
	}

	/**
	 * Deletes an external file.
	 *
	 * @param  field      the external field
	 * @param  primaryKey the primary key
	 * @param  extension  the file extension
	 * @throws IOException if an IO exception occurs
	 */
	public static void deleteExternalFile(ExternalField field, String primaryKey, String extension) throws IOException {
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		(new File(fname)).delete();
	}

	/**
	 * Retrieves the data from an external file.
	 * If the file name is passed, the extension is calculated.
	 * Return a zero length string if the file does not exist.
	 *
	 * @param  field      the ExternalField to retrieve the data from
	 * @param  primaryKey the primary key value
	 * @param  extension  the file extension or the file name of the file
	 * @return            the data from the external file
	 * @throws IOException if there is an error reading the data from the file
	 */
	public static String getString(ExternalField field, String primaryKey, String extension) throws IOException {
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		if (!new File(fname).exists())
			return "";
		return FileUtils.readFile(fname);
	}

	/**
	 * Retrieves the data from an external file.
	 * If the file name is passed, the extension is calculated.
	 * Returns a zero length byte array if the file does not exist.
	 *
	 * @param  field      the ExternalField to retrieve the data from
	 * @param  primaryKey the primary key value
	 * @param  extension  the file extension or the file name of the file
	 * @return            the data from the external file
	 * @throws IOException if there is an error reading the data from the file
	 */
	public static byte [] getBinary(ExternalField field, String primaryKey, String extension) throws IOException {
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		if (!new File(fname).exists())
			return new byte[0];
		return FileUtils.readFileBytes(fname);
	}

	/**
	 * Delete all external files associated with an SQL record.
	 *
	 * @param tableName the SQL table name
	 * @param primaryKey primary key value for the given table
	 */
	public static void deleteAllExternalFiles(final String tableName, final String primaryKey) {
		String dir = getExternalFileDir(tableName, primaryKey, false);
		(new File(dir)).listFiles((dir1, name) -> {
			if (name.startsWith(primaryKey))
				(new File(dir1, name)).delete();
			return false;
		});
	}

	public static void deleteCallback(String table, Object pkval) {
		deleteAllExternalFiles(table, (String) pkval);
	}

	/**
	 * This routine creates the relative file name to be used when storing files external to the database.
	 * This is used when the external's file relative to the base path is desired.
	 * <br>
	 * This should not be needed.
	 * Use <code>makeExternalFilePath</code> instead.  There is no need to store the files relative
	 * path because it can always be calculated based on the table's primary key.
	 *
	 * @param basePath
	 * @param id
	 * @param extension
	 * @return
	 * @see #makeExternalFilePath(String, String, String, String, boolean)
	 */
	@Deprecated
	public static String makeRelativeExternalFilePath(final String basePath, final String id, final String extension) {
		final StringBuilder path = new StringBuilder(basePath);
		path.append("/").append(id, 0, 5);
		final String tid = id.substring(6);
		for (int j=0 ; j < 4 ; j++)
			path.append("/").append(tid, j * 2, j * 2 + 2);
		path.append("/").append(id).append(extension);
		return path.toString();
	}

	/**
	 * Checks if there exists any files in the specified directory that starts with
	 * the concatenation of the primaryKey and fieldName.
	 *
	 * @param  field      the ExternalField object representing the table
	 * @param  primaryKey the primary key value
	 * @param  fieldName  the fictitious field name or null (for any)
	 * @return            true if a file exists that starts with the specified
	 *                    string, false otherwise
	 */
	public static boolean anyStartsWith(final ExternalField field, final String primaryKey, String fieldName) {
		final String dirName = getExternalFileDir(field.tableName, primaryKey, false);
		final File [] files = new File(dirName).listFiles();
		if (files != null) {
			if (fieldName == null)
				fieldName = "";
			if (!fieldName.isEmpty() &&  fieldName.charAt(0) != '-')
				fieldName = "-" + fieldName;
			final String str = primaryKey + fieldName;
			for (File file : files)
				if (file.getName().startsWith(str))
					return true;
		}
		return false;
	}

	/**
	 * Saves the buffered input stream to an external file and closes the input stream.
	 *
	 * @param  field       the ExternalField object representing the table
	 * @param  primaryKey  the primary key value
	 * @param  ext         the file extension
	 * @param  is          the buffered input stream to save
	 * @throws IOException if an I/O error occurs
	 */
	public static void saveInputStream(final ExternalField field, String primaryKey, String ext, BufferedInputStream is) throws IOException {
		String path = makeExternalFilePath(field, primaryKey, ext);
		try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(ExternalFile.makeExternalFilePath(field, primaryKey, ext)))) {
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				os.write(buf, 0, len);
		};
		is.close();
	}

	/**
	 * Calculates the directory where an external file fill be created.
	 *
	 * @param tableName the SQL table name
	 * @param primaryKey primary key value for the given table
	 * @param create if true, create the directory
	 * @return
	 */
	private static String getExternalFileDir(final String tableName, final String primaryKey, boolean create) {
		String rootPath = BPropertyKissOnly.get(StandardProperty.EXTERNAL_FILE_ROOT);

		if (rootPath == null  ||  rootPath.isEmpty())
			throw new ArahantException("System property " + StandardProperty.EXTERNAL_FILE_ROOT + " not set.");

		if (rootPath.charAt(rootPath.length()-1) != '/')
			rootPath += '/';
		final StringBuilder path = new StringBuilder(rootPath);
		path.append(tableName).append("/").append(primaryKey, 0, 5);
		final String tid = primaryKey.substring(6);
		for (int j=0 ; j < 4 ; j++)
			path.append("/").append(tid, j * 2, j * 2 + 2);
		String spath = path.toString();
		if (create)
			(new File(spath)).mkdirs();
		return spath;
	}

	/**
	 * Returns the file extension from the given file name.
	 * If already being passed a file extension then it is returned.
	 * It also assures that if an extension exists, it starts with a "."
	 *
	 * @param  fname  the file name or a file extension
	 * @return        the file type extension, or an empty string if the file name is null or does not have an extension
	 */
	public static String fileExtension(String fname) {
        if (fname == null || fname.isEmpty())
            return "";
		if (fname.charAt(0) == '.')
			return fname;
        int lastPeriodIndex = fname.lastIndexOf('.');
        if (lastPeriodIndex == -1)
            return fname.length() > 4 ? "" : "." + fname.toLowerCase();
        return fname.substring(lastPeriodIndex).toLowerCase();
    }

	/**
	 * Generates a URL for an external field suitable for the front-end.  It is basically a temporary file that the front-end can access
	 *
	 * @param  field         the external field
	 * @param  primaryKey    the primary key of the field
	 * @param  prefix        the file name prefix
	 * @param  extension     the file name extension
	 * @return               the generated URL
	 * @throws IOException  if an I/O error occurs
	 */
	public static String getURL(ExternalField field, String primaryKey, String prefix, String extension) throws IOException {
		extension = fileExtension(extension);
		final String fname = makeExternalFilePath(field, primaryKey, extension);
		final File tfp = FileSystemUtils.createReportFile(prefix, extension);
		FileUtils.copy(fname, tfp.getAbsolutePath());
        return FileSystemUtils.getHTTPPath(tfp);
	}
}
