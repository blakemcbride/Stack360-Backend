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
package com.arahant.business;

import com.arahant.beans.AIProperty;
import com.arahant.beans.FormType;
import com.arahant.beans.Person;
import com.arahant.beans.PersonForm;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.TiffReport;
import com.arahant.utils.*;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.lowagie.tools.plugins.Tiff2Pdf;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;
//import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class BPersonForm extends SimpleBusinessObjectBase<PersonForm> {

	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BPersonForm.class);

	public BPersonForm() {
	}

	public BPersonForm(final PersonForm form) {
		bean = form;
	}

	public BPersonForm(final String id) throws ArahantException {
		internalLoad(id);
	}

	public void append(String secondFormId) {
		try {
			bean.setFormDate(DateUtils.now());
			//write formData out as pdf
			File front = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");
			File back = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");
			File dest = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");

			FileOutputStream fos1 = new FileOutputStream(front);
			FileOutputStream fos2 = new FileOutputStream(back);

			fos2.write(ExternalFile.getBinary(ExternalFile.PERSON_FORM, bean.getPersonFormId(), bean.getFileNameExtension()));
			fos1.write(getImage());

			fos1.flush();
			fos1.close();
			fos2.flush();
			fos2.close();

			concat_pdf(new String[]{front.getAbsolutePath(), back.getAbsolutePath(), dest.getAbsolutePath()});

			byte[] im = new byte[(int) dest.length()];

			FileInputStream fis = new FileInputStream(dest);

			fis.read(im);

			fis.close();

			ExternalFile.saveData(ExternalFile.PERSON_FORM, bean.getPersonFormId(), "pdf", im);

			front.delete();
			back.delete();
			dest.delete();
		} catch (Exception e) {
			throw new ArahantException("Can't append to form.", e);
		}
	}

	public void append(PersonForm secondForm) {
		try {
			bean.setFormDate(DateUtils.now());
			//write formData out as pdf
			File front = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");
			File back = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");
			File dest = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");

			FileOutputStream fos1 = new FileOutputStream(front);
			FileOutputStream fos2 = new FileOutputStream(back);

			fos2.write((new BPersonForm(secondForm)).getImage());
			fos1.write(getImage());

			fos1.flush();
			fos1.close();
			fos2.flush();
			fos2.close();

			concat_pdf(new String[]{front.getAbsolutePath(), back.getAbsolutePath(), dest.getAbsolutePath()});

			byte[] im = new byte[(int) dest.length()];

			FileInputStream fis = new FileInputStream(dest);

			fis.read(im);

			fis.close();

			ExternalFile.saveData(ExternalFile.PERSON_FORM, bean.getPersonFormId(), "pdf", im);

			front.delete();
			back.delete();
			dest.delete();


		} catch (Exception e) {
			throw new ArahantException("Can't append to form.", e);
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new PersonForm();
		return bean.generateId();
	}

	/**
	 * Retrieves the form data in the form of a byte array.
	 * If the form is in Tiff format, it is first converted to a PDF format.
	 *
	 * @return  the form data as a byte array, or null if no image is available
	 */
	public byte[] getFormData() throws IOException {
		byte[] b = getImage();

		if (b == null)
			return null;

		if (b[0] == '%' && b[1] == 'P' && b[2] == 'D' && b[3] == 'F')
			return b;

		try {
			final File pdf = FileSystemUtils.createTempFile("FormRpt", ".pdf");
			final File tif = FileSystemUtils.createTempFile("TiffRpt", ".tif");

			final FileOutputStream fos = new FileOutputStream(tif);
			fos.write(b);
			fos.flush();
			fos.close();

			Tiff2Pdf.main(new String[]{tif.getAbsolutePath(), pdf.getAbsolutePath(), "ORIGINAL"});

			File fyle = new File(pdf.getAbsolutePath());

			FileInputStream fis = new FileInputStream(pdf.getAbsolutePath());

			b = new byte[(int) fyle.length()];

			fis.read(b);

			fis.close();

			pdf.delete();
			tif.delete();
		} catch (Exception e) {
			throw new ArahantException(e);
		}
		return b;
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(PersonForm.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public static BPersonForm[] list(final String employeeId) {

		HibernateCriteriaUtil<PersonForm> hcu = ArahantSession.getHSU().createCriteria(PersonForm.class).orderByDesc(PersonForm.DATE).eq(PersonForm.PERSON_ID, employeeId);




//		HibernateCriteriaUtil<PersonForm> hcu = ArahantSession.getHSU().createCriteria(PersonForm.class).orderByDesc(PersonForm.DATE);
//
//		hcu.joinTo(PersonForm.PERSON).eq(Person.PERSONID, employeeId).list();
//
//		HibernateCriteriaUtil fthcu = hcu.joinTo(PersonForm.FORM_TYPE).orderBy(FormType.FORM_CODE);

		/*
		List<String> restrictedList = AIProperty.getList("RestrictedFormTypes");
		if (restrictedList.size() > 0)
			hcu.joinTo(PersonForm.FORM_TYPE).orderBy(FormType.FORM_CODE).in(FormType.ID, restrictedList);
		else

		 */
			hcu.joinTo(PersonForm.FORM_TYPE).orderBy(FormType.FORM_CODE);
//			fthcu.in(FormType.ID, restrictedList);

		return makeArray(hcu.list());
	}

	public static BPersonForm[] search(final String name, final String employeeId, final String typeId, final int fromDate, final int toDate, final int max) {

		HibernateCriteriaUtil<PersonForm> hcu = ArahantSession.getHSU().createCriteria(PersonForm.class).setMaxResults(max);

		if (!isEmpty(name))
			hcu.like(PersonForm.COMMENTS, name);

		if (!isEmpty(employeeId))
			hcu.joinTo(PersonForm.PERSON).eq(Person.PERSONID, employeeId);

		if (!isEmpty(typeId))
			hcu.eq(PersonForm.FORM_TYPE, new BFormType(typeId).getBean());

		if (fromDate != 0)
			hcu.ge(PersonForm.DATE, fromDate);

		if (toDate != 0)
			hcu.le(PersonForm.DATE, toDate);

		hcu.joinTo(PersonForm.PERSON).orderBy(Person.LNAME);

		HibernateCriteriaUtil<PersonForm> fthcu = hcu.joinTo(PersonForm.FORM_TYPE).orderBy(FormType.FORM_CODE);
/*
		List<String> restrictedList = AIProperty.getList("RestrictedFormTypes");
		if (restrictedList.size() > 0)
			fthcu.in(FormType.ID, restrictedList);
*/
		hcu.orderBy(PersonForm.DATE);

		return makeArray(hcu.list());
	}

	public static BPersonForm[] search(final String name, final String[] employeeIds, final String typeId, final int fromDate, final int toDate, final int max) {

		HibernateCriteriaUtil<PersonForm> hcu = ArahantSession.getHSU().createCriteria(PersonForm.class).setMaxResults(max);

		if (employeeIds.length > 0)
			hcu.joinTo(PersonForm.PERSON).in(Person.PERSONID, employeeIds).orderBy(Person.LNAME);
		else
			hcu.joinTo(PersonForm.PERSON).orderBy(Person.LNAME);

		if (!isEmpty(typeId))
			hcu.eq(PersonForm.FORM_TYPE, typeId);

		if (fromDate != 0)
			hcu.ge(PersonForm.DATE, fromDate);

		if (toDate != 0)
			hcu.le(PersonForm.DATE, toDate);


		HibernateCriteriaUtil<PersonForm> fthcu = hcu.joinTo(PersonForm.FORM_TYPE).orderBy(FormType.FORM_CODE);
/*
		List<String> restrictedList = AIProperty.getList("RestrictedFormTypes");
		if (restrictedList.size() > 0)
			fthcu.in(FormType.ID, restrictedList);
*/
		hcu.orderBy(PersonForm.DATE);

		return makeArray(hcu.list());
	}

	private static BPersonForm[] makeArray(final List<PersonForm> l) {
		final BPersonForm[] ret = new BPersonForm[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BPersonForm(l.get(loop));
		return ret;
	}

	/**
	 * Returns a path suitable for the front-end.
	 *
	 * @return
	 * @throws ArahantException
	 */
	public String getReport() throws ArahantException {
		try {
			return new TiffReport().tifToPdf(getImage(), bean.getFileNameExtension(), getFilename());
		} catch (IOException e) {
			throw new ArahantException(e);
		}
	}

	public String getComments() {
		if (bean.getComments() == null)
			return "";
		return bean.getComments();
	}

	public String getFormCode() {
		return bean.getFormType().getFormCode();
	}

	public String getId() {
		return bean.getPersonFormId();
	}

	public String getDateFormatted() {
		return DateUtils.getDateFormatted(bean.getFormDate());
	}

	public String getDescription() {
		return bean.getFormType().getDescription();
	}

	public String getSource() {
		return bean.getSource();
	}

	public void setSource(final String source) {
		bean.setSource(source);
	}

	public int getDate() {
		return bean.getFormDate();
	}

	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String id : ids) {
			ExternalFile.deleteAllExternalFiles("person_form", id);
			new BPersonForm(id).delete();
		}
	}

	public String getFormTypeId() {
		return bean.getFormType().getFormTypeId();
	}

	public void prepend(byte[] formData) {
		try {
			bean.setFormDate(DateUtils.now());
			//write formData out as pdf
			File front = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");
			File back = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");
			File dest = com.arahant.utils.FileSystemUtils.createTempFile("pre", ".pdf");

			FileOutputStream fos1 = new FileOutputStream(front);
			FileOutputStream fos2 = new FileOutputStream(back);

			fos1.write(formData);
			fos2.write(getImage());

			fos1.flush();
			fos1.close();
			fos2.flush();
			fos2.close();

			concat_pdf(new String[]{front.getAbsolutePath(), back.getAbsolutePath(), dest.getAbsolutePath()});

			byte[] im = new byte[(int) dest.length()];

			FileInputStream fis = new FileInputStream(dest);

			fis.read(im);

			fis.close();

			ExternalFile.saveData(ExternalFile.PERSON_FORM, bean.getPersonFormId(), "pdf", im);

			dest.delete();
			back.delete();
			front.delete();

		} catch (Exception e) {
			throw new ArahantException("Can't append to form.", e);
		}
	}

	/**
	 * This method stores the form data as an external file.
	 *
	 * @param formData
	 * @param extension
	 * @throws IOException
	 */
	public void setFormData(byte [] formData, String extension) throws IOException {
		ExternalFile.saveData(ExternalFile.PERSON_FORM, bean.getPersonFormId(), extension, formData);
	}

	public void setFormDate(int formDate) {
		bean.setFormDate(formDate);
	}

	public void setFormTypeId(final String formTypeId) {
		bean.setFormType(ArahantSession.getHSU().get(FormType.class, formTypeId));
	}

	public void setComments(final String comments) {
		bean.setComments(comments);
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public String getPersonId() {
		return bean.getPersonId();
	}

	private String getFilename() {
		try {
			return bean.getPerson().getNameLFM() + bean.getFormType().getFormCode() + bean.getFormDate() + "_" + (int) (Math.random() * 10000);
		} catch (Exception e) {
			return "FormRpt";
		}
	}

	private int getPageCount() throws IOException {
		com.itextpdf.text.pdf.PdfReader reader = new PdfReader(getImage());
		return reader.getNumberOfPages();
	}

	public void deletePages(int[] pageNumbers) {
		try {
			extractPages(pageNumbers);
		} catch (Exception e) {
			throw new ArahantException(e);
		}
	}

	public byte[] extractPages(int[] pageNumbers) {
		try {
			com.itextpdf.text.pdf.PdfReader reader = new PdfReader(getImage());
			int maxPageNumber = 0;

			HashSet<Integer> ints = new HashSet<>();

			for (int x : pageNumbers) {
				if (maxPageNumber < x)
					maxPageNumber = x;
				ints.add(x);
			}
			int n = reader.getNumberOfPages();
			if (reader.getNumberOfPages() < maxPageNumber)
				throw new ArahantWarning("The maximum number of pages in the document is : " + reader.getNumberOfPages());

			if (reader.getNumberOfPages() == pageNumbers.length)
				throw new ArahantWarning("Operation would leave source document with no pages.");

			// we retrieve the sizes
			float width = (float) 0.0;
			float height = (float) 0.0;
			float oldwidth = (float) 0.0;
			float oldheight = (float) 0.0;
			for (int loop = 1; loop <= n; loop++) {
				com.itextpdf.text.Rectangle psize = reader.getPageSize(loop);

				if (ints.contains(loop)) {
					if (psize.getWidth() > width)
						width = psize.getWidth();
					if (psize.getHeight() > height)
						height = psize.getHeight();
				} else {
					if (psize.getWidth() > oldwidth)
						oldwidth = psize.getWidth();
					if (psize.getHeight() > oldheight)
						oldheight = psize.getHeight();
				}
			}

			// step 1: creation of a document-object
			Document document = new Document(new Rectangle(width, height));
			Document olddocument = new Document(new Rectangle(oldwidth, oldheight));

			ByteOutputStream bos = new ByteOutputStream();
			ByteOutputStream oldDoc = new ByteOutputStream();
			// step 2: we create a writer that listens to the document
			PdfWriter writer = PdfWriter.getInstance(document, bos);
			PdfWriter oldwriter = PdfWriter.getInstance(olddocument, oldDoc);
			// step 3: we open the document
			document.open();
			olddocument.open();
			// step 4: we add content
			PdfContentByte cb = writer.getDirectContent();
			PdfContentByte oldcb = oldwriter.getDirectContent();

			for (int loop = 1; loop <= n; loop++)
				if (ints.contains(loop)) {
					document.newPage();
					PdfImportedPage page1 = writer.getImportedPage(reader, loop);
					cb.addTemplate(page1, 0, 0);
				} else {
					olddocument.newPage();
					PdfImportedPage page1 = oldwriter.getImportedPage(reader, loop);
					oldcb.addTemplate(page1, 0, 0);
				}
			// step 5: we close the document
			document.close();
			olddocument.close();

			ExternalFile.saveData(ExternalFile.PERSON_FORM, bean.getPersonFormId(), "pdf", oldDoc.getBytes());
			oldDoc.close();
			update();
			/*
			 * FileOutputStream fos=new FileOutputStream("/Users/oldout.pdf");
			 * fos.write(oldDoc.getBytes()); fos.flush(); fos.close();
			 */
			byte[] ret = bos.getBytes();

			bos.close();
			return ret;
		} catch (ArahantException e) {
			throw e;
		} catch (Exception e) {
			throw new ArahantException(e);
		}
	}

	public String getCommentPreview() {
		String ret = getComments();

		if (ret.length() > 100)
			ret = ret.substring(0, 100);

		return ret;
	}

	public String getExtension() {
		return bean.getFileNameExtension();
	}

	public void setExtension(String extension) {
		bean.setFileNameExtension(extension);
	}

	public char getElectronicallySigned() {
		return bean.getElectronicallySigned();
	}

	public void setElectronicallySigned(char electronicallySigned) {
        bean.setElectronicallySigned(electronicallySigned);
    }

	public FormType getFormType() {
		return bean.getFormType();
	}

	public void setFormType(FormType formType) {
        bean.setFormType(formType);
    }

	/*
	 * public static void main (String args[]) {
	 *
	 * FileInputStream fis=null; try { PersonForm pf = new PersonForm();
	 *
	 * File inpdf = new File("/Users/TiffRpt22498.pdf");
	 *
	 * int size = (int) inpdf.length();
	 *
	 * byte[] dat = new byte[size];
	 *
	 * fis = new FileInputStream(inpdf);
	 *
	 * fis.read(dat);
	 *
	 * pf.setImage(dat);
	 *
	 * BPersonForm bf = new BPersonForm(pf);
	 *
	 * byte []out=bf.extractPages(new int[]{2});
	 *
	 * FileOutputStream fos=new FileOutputStream("/Users/out.pdf");
	 * fos.write(out); fos.flush(); fos.close();
	 *
	 * } catch (DocumentException ex) {
	 * Logger.getLogger(BPersonForm.class.getName()).log(Level.SEVERE, null,
	 * ex); } catch (IOException ex) {
	 * Logger.getLogger(BPersonForm.class.getName()).log(Level.SEVERE, null,
	 * ex); } finally { try { fis.close(); } catch (IOException ex) {
	 * Logger.getLogger(BPersonForm.class.getName()).log(Level.SEVERE, null,
	 * ex); } }
	 *
	 *
	 * }
	 *
	 */
	@SuppressWarnings("unchecked")
	private static void concat_pdf(String args[]) {
		if (args.length < 2)
			System.err.println("arguments: file1 [file2 ...] destfile");
		else
			try {
				int pageOffset = 0;
				ArrayList master = new ArrayList();
				int f = 0;
				String outFile = args[args.length - 1];
				Document document = null;
				PdfCopy writer = null;
				while (f < args.length - 1) {
					// we create a reader for a certain document
					PdfReader reader = new PdfReader(args[f]);
					reader.consolidateNamedDestinations();
					// we retrieve the total number of pages
					int n = reader.getNumberOfPages();
					List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
					if (bookmarks != null) {
						if (pageOffset != 0)
							SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
						master.addAll(bookmarks);
					}
					pageOffset += n;
					System.out.println("There are " + n + " pages in " + args[f]);

					if (f == 0) {
						// step 1: creation of a document-object
						document = new Document(reader.getPageSizeWithRotation(1));
						// step 2: we create a writer that listens to the document
						writer = new PdfCopy(document, new FileOutputStream(outFile));
						// step 3: we open the document
						document.open();
					}
					// step 4: we add content
					PdfImportedPage page;
					for (int i = 0; i < n;) {
						++i;
						page = writer.getImportedPage(reader, i);
						writer.addPage(page);
						System.out.println("Processed page " + i);
					}
					writer.freeReader(reader);
					f++;
				}
				if (!master.isEmpty())
					writer.setOutlines(master);
				// step 5: we close the document
				document.close();
			} catch (Exception e) {
				logger.error(e);
			}
	}

	public byte [] getImage() throws IOException {
		return ExternalFile.getBinary(ExternalFile.PERSON_FORM, bean.getPersonFormId(), bean.getFileNameExtension());
	}
}
