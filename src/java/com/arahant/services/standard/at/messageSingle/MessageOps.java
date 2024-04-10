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
 *
 * Created on Feb 4, 2007
 */
package com.arahant.services.standard.at.messageSingle;

import com.arahant.beans.Person;
import com.arahant.business.BMessage;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardAtMessageSingleOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class MessageOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(MessageOps.class);

	/**
	 * This method is no longer used.  Instead, in order to support attachments,
	 * the FileUpload servlet is used instead.
	 *
	 * See com.arahant.servlets.FileUploadServlet#createMessage(HibernateSessionUtil, HashMap, List)
	 */
	@WebMethod()
	public CreateMessageReturn createMessage(/*@WebParam(name = "in")*/final CreateMessageInput in) {
		// ***  THIS METHOD IS NOT USED.  A SERVLET IS USED INSTEAD SO THAT ATTACHMENTS CAN BE USED.
		// See com.arahant.servlets.FileUploadServlet.createMessage()
		final CreateMessageReturn ret = new CreateMessageReturn();

		try {
			checkLogin(in);

			ret.setId(BMessage.send(hsu.getCurrentPerson(), hsu.get(Person.class, in.getToPersonId()), in.getSubject(), in.getMessage()));

			/*
			final BMessage bm=new BMessage();
			ret.setId(bm.create());
			in.makeMessage(bm);
			final BPerson bp = BPerson.getCurrent();
			bm.setFromPerson(bp);
			bm.insert();
			BMessage.createToRecord(bm.getMessageId(), in.getToPersonId());
			 */
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadMessageReturn loadMessage(/*@WebParam(name = "in")*/final LoadMessageInput in) {
		LoadMessageReturn ret = new LoadMessageReturn();

		try {
			checkLogin(in);

			String currentPersonId = hsu.getCurrentPerson().getPersonId();
			Record rec = hsu.getKissConnection().fetchOne("select from_person_id from message where message_id=?", in.getMessageId());
			String senderId = rec.getString("from_person_id");
			boolean includeBcc = currentPersonId.equals(senderId);

			ret.setMessageReturn(new BMessage(in.getMessageId()), includeBcc);
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetAttachmentReturn getAttachment(/*@WebParam(name = "in")*/final GetAttachmentInput in) {
		GetAttachmentReturn ret = new GetAttachmentReturn();
		try {
			checkLogin(in);
			String messageAttachmentId = in.getAttachmentId();
			Connection db = KissConnection.get();
			Record rec = db.fetchOne("select source_file_name from message_attachment where message_attachment_id=?", messageAttachmentId);
			if (rec == null)
				throw new ArahantException("Message attachment not found");
			byte [] bytes = ExternalFile.getBinary(ExternalFile.MESSAGE_ATTACHMENT_ATTACHMENT, messageAttachmentId, rec.getString("source_file_name"));
			File fyle = FileSystemUtils.createTempFile("attachment-", "." + org.kissweb.FileUtils.getExtension(rec.getString("source_file_name")));
			Files.write(fyle.toPath(), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			ret.setFileName(FileSystemUtils.getHTTPPath(fyle));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteMessageReturn deleteMessage(/*@WebParam(name = "in")*/final DeleteMessageInput in) {
		final DeleteMessageReturn ret = new DeleteMessageReturn();
		try {
			checkLogin(in);
			BMessage.delete(hsu, in.getMessageIds(), BPerson.getCurrent());
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchPeopleReturn searchPeople(/*@WebParam(name = "in")*/final SearchPeopleInput in) {
		final SearchPeopleReturn ret = new SearchPeopleReturn();
		try {
			checkLogin(in);
			final Connection db = KissConnection.get();
			ArrayList<Object> args = new ArrayList<>();
			String query =  "with current_applicant_persons as ( " +
							"select p.* from person p " +
							"left join current_employee_status esn " +
							"  on p.person_id = esn.employee_id " +
							"join hr_employee_status es " +
							"  on esn.status_id = es.status_id " +
							"join applicant a " +
							"  on a.person_id = p.person_id " +
							"where (es.active = 'N' or es.active is null)) " +

					"select p.person_id " +
					"from current_applicant_persons p " +
					"where 1=1 ";

			String lname = in.getLastName();
			if (lname != null && !lname.isEmpty()) {
				query += "and lower(p.lname) like ? ";
				args.add(lname.toLowerCase() + "%");
			}
			String fname = in.getFirstName();
			if (fname != null && !fname.isEmpty()) {
				query += "and lower(p.fname) like ? ";
				args.add(fname.toLowerCase() + "%");
			}
			query += "order by p.lname, p.fname";
			List<Record> recs = db.fetchAll(BProperty.getInt("Search Max"), query, args);
			ret.setPeople(recs);
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadPreferencesReturn loadPreferences(/*@WebParam(name = "in")*/final LoadPreferencesInput in) {
		final LoadPreferencesReturn ret = new LoadPreferencesReturn();
		try {
			checkLogin(in);
			ret.setData(new BPerson(hsu.getCurrentPerson()));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SavePreferencesReturn savePreferences(/*@WebParam(name = "in")*/final SavePreferencesInput in) {
		final SavePreferencesReturn ret = new SavePreferencesReturn();
		try {
			checkLogin(in);

			final BPerson x = new BPerson(hsu.getCurrentPerson());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
