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
package com.arahant.services.standard.site.screen;

import com.arahant.business.BScreen;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardSiteScreenOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ScreenOps extends ServiceBase {

	private static final ArahantLogger logger = new ArahantLogger(ScreenOps.class);

	@WebMethod()
	public DeleteScreenReturn deleteScreen(/*@WebParam(name = "in")*/final DeleteScreenInput in) {
		final DeleteScreenReturn ret = new DeleteScreenReturn();

		try {
			checkLogin(in);

			BScreen.delete(hsu, in.getScreenIds());

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchScreensReturn searchScreens(/*@WebParam(name = "in")*/final SearchScreensInput in) {
		final SearchScreensReturn ret = new SearchScreensReturn();
		try {
			checkLogin(in);

			ret.setItem(BScreen.search(hsu, in.getName(), in.getExtId(), in.getFileName(), in.getIncludeNormal(), in.getIncludeParent(), in.getIncludeChild(), in.getIncludeWizard(), in.getIncludeWizardPage(), ret.getCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadScreenReturn loadScreen(/*@WebParam(name = "in")*/final LoadScreenInput in) {
		final LoadScreenReturn ret = new LoadScreenReturn();

		try {
			checkLogin(in);

			ret.setValues(new BScreen(in.getScreenId()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadScreenDescriptionReturn loadScreenDescription(/*@WebParam(name = "in")*/final LoadScreenDescriptionInput in) {
		final LoadScreenDescriptionReturn ret = new LoadScreenDescriptionReturn();

		try {
			checkLogin(in);

			ret.setData(new BScreen(in.getId()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewScreenReturn newScreen(/*@WebParam(name = "in")*/final NewScreenInput in) {
		final NewScreenReturn ret = new NewScreenReturn();

		try {
			checkLogin(in);

			final BScreen s = new BScreen();
			ret.setScreenId(s.create());
			ret.setExtId(s.getExtId());
			in.setData(s);
			s.insert();

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveScreenReturn saveScreen(/*@WebParam(name = "in")*/final SaveScreenInput in) {
		final SaveScreenReturn ret = new SaveScreenReturn();

		try {
			checkLogin(in);

			final BScreen s = new BScreen(in.getScreenId());
			in.setData(s);
			s.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
