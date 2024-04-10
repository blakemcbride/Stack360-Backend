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



package com.arahant.services.standard.misc.utils;

/**
 *
 * @author Blake McBride
 */

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.FileUtils;
import java.io.File;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardMiscUtilsOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class UtilsOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(UtilsOps.class);

	public UtilsOps() {
		super();
	}

	@WebMethod()
	public WebUtilReturn webUtil(/*@WebParam(name = "in")*/final WebUtilInput in) {
		final WebUtilReturn ret = new WebUtilReturn();
		final String outputFile = FileSystemUtils.makeAbsolutePath("sums.tmp");
		ret.setResult(-100);
		try {
			if (!in.isValid()) {
				ret.setResult(-1);
				return ret;
			}
			switch (in.getFunCode()) {
				case 1:  // generate file hash
					(new File(outputFile)).delete();
					FileSumThread fst = new FileSumThread(outputFile, FileSystemUtils.getWorkingDirectory().getPath());
					fst.start();
					ret.setResult(0);
					break;
				case 2:  //  remove temp file
					(new File(outputFile)).delete();
					(new File(FileSystemUtils.makeAbsolutePath("catalina.out"))).delete();
					ret.setResult(0);
					break;
				case 3:
					StopUtil su = new StopUtil(in.getIntArg1());
					su.start();
					ret.setResult(0);
					break;
				case 4:  //  send catalina.out
					FileUtils.copy(FileSystemUtils.makeAbsolutePath("../../logs/catalina.out"),
							FileSystemUtils.makeAbsolutePath("catalina.out"));
					ret.setResult(0);
					break;
				default:
					ret.setResult(-2);
					break;
			}
		} catch (final Exception e) {
			ret.setResult(-3);
		} finally {
			return ret;
		}
	}

}
