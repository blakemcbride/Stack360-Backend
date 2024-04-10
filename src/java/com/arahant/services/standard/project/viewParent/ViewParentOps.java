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


package com.arahant.services.standard.project.viewParent;

import com.arahant.beans.ProjectView;
import com.arahant.beans.ProjectViewJoin;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.imports.ProjectImport;
import com.arahant.reports.ProjectViewReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;
import java.util.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

/**
 *
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardProjectViewParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ViewParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ViewParentOps.class);

	public ViewParentOps() {
	}

	@WebMethod()
	public ListProjectViewsReturn listProjectViews(/*
			 * @WebParam(name = "in")
			 */final ListProjectViewsInput in) {
		final ListProjectViewsReturn ret = new ListProjectViewsReturn();
		try {
			checkLogin(in);

			ret.setItem(BProjectViewJoin.list(in.getParentId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*
			 * @WebParam(name = "in")
			 */final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			//TODO: do report
			//ret.setReportUrl(new BProjectView().getReport());

			ret.setReportUrl(new ProjectViewReport().build(in.getParentId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MoveProjectViewUpReturn moveProjectViewUp(/*
			 * @WebParam(name = "in")
			 */final MoveProjectViewUpInput in) {
		final MoveProjectViewUpReturn ret = new MoveProjectViewUpReturn();
		try {
			checkLogin(in);

			final BProjectViewJoin x = new BProjectViewJoin(in.getId());
			x.moveUp();
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MoveProjectViewDownReturn moveProjectViewDown(/*
			 * @WebParam(name = "in")
			 */final MoveProjectViewDownInput in) {
		final MoveProjectViewDownReturn ret = new MoveProjectViewDownReturn();
		try {
			checkLogin(in);

			final BProjectViewJoin x = new BProjectViewJoin(in.getId());
			x.moveDown();
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public RemoveProjectViewsReturn removeProjectViews(/*
			 * @WebParam(name = "in")
			 */final RemoveProjectViewsInput in) {
		final RemoveProjectViewsReturn ret = new RemoveProjectViewsReturn();
		try {
			checkLogin(in);

			BProjectViewJoin.delete(hsu, in.getIds(), in.getRemoveAll());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CopyProjectViewsReturn copyProjectViews(/*
			 * @WebParam(name = "in")
			 */final CopyProjectViewsInput in) {
		final CopyProjectViewsReturn ret = new CopyProjectViewsReturn();
		try {
			checkLogin(in);

			// due to the nature of the screen's pending copy/cuts that allows other operations to be performed
			// prior to paste, the incoming ids may no longer exist if a delete was done on them, check now
			for (String projectViewJoinId : in.getIds())
				if (hsu.get(ProjectViewJoin.class, projectViewJoinId) == null)
					throw new ArahantException(in.getIds().length == 1 ? "The selected item no longer exists." : "One or more of the selected items no longer exists.");

			BProjectViewJoin.copy(in.getIds(), in.getParentId(), in.getLocationType(), in.getLocationTypeRelativeToId(), in.getDeepCopy());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CutProjectViewsReturn cutProjectViews(/*
			 * @WebParam(name = "in")
			 */final CutProjectViewsInput in) {
		final CutProjectViewsReturn ret = new CutProjectViewsReturn();
		try {
			checkLogin(in);

			// due to the nature of the screen's pending copy/cuts that allows other operations to be performed
			// prior to paste, the incoming ids may no longer exist if a delete was done on them, check now
			for (String projectViewJoinId : in.getIds())
				if (hsu.get(ProjectViewJoin.class, projectViewJoinId) == null)
					throw new ArahantException(in.getIds().length == 1 ? "The selected item no longer exists." : "One or more of the selected items no longer exists.");

			// first shallow copy (we want it to be the same folders since we are moving it)
			BProjectViewJoin.copy(in.getIds(), in.getParentId(), in.getLocationType(), in.getLocationTypeRelativeToId(), false);

			// now delete from the level they are at
			BProjectViewJoin.delete(hsu, in.getIds(), false);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewProjectViewsReturn newProjectViews(/*
			 * @WebParam(name = "in")
			 */final NewProjectViewsInput in) {
		final NewProjectViewsReturn ret = new NewProjectViewsReturn();
		try {
			checkLogin(in);

			List<String> rets = new LinkedList<String>();

			if (in.getLocationType() == 0) {
				ProjectViewJoin parentProjectViewJoin = hsu.get(ProjectViewJoin.class, in.getParentId());
				ProjectView parentProjectView = parentProjectViewJoin == null ? null : parentProjectViewJoin.getChild();

				ProjectViewJoin pvj = hsu.createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, parentProjectView).orderByDesc(ProjectViewJoin.SEQ).first();

				String id = "";

				if (pvj != null)
					id = pvj.getProjectViewJoinId();

				if (!isEmpty(id))
					for (int loop = 0; loop < in.getViews().length; loop++) {
						id = BProjectViewJoin.createAfter(id,
								in.getViews()[loop].getProjectId(), in.getViews()[loop].getSummary(),
								in.getViews()[loop].getDescription());
						rets.add(id);
					}
				else
					for (int loop = 0; loop < in.getViews().length; loop++) {
						id = BProjectViewJoin.createUnder(in.getParentId(),
								in.getViews()[loop].getProjectId(), in.getViews()[loop].getSummary(),
								in.getViews()[loop].getDescription());
						rets.add(id);
					}
			} else if (in.getLocationType() == 1) {
				String id = BProjectViewJoin.createBefore(in.getLocationTypeRelativeToId(),
						in.getViews()[0].getProjectId(), in.getViews()[0].getSummary(),
						in.getViews()[0].getDescription());
				rets.add(id);

				for (int loop = 1; loop < in.getViews().length; loop++) {
					id = BProjectViewJoin.createAfter(id,
							in.getViews()[loop].getProjectId(), in.getViews()[loop].getSummary(),
							in.getViews()[loop].getDescription());
					rets.add(id);
				}
			} else if (in.getLocationType() == 2) {
				String id = in.getLocationTypeRelativeToId();

				for (int loop = 0; loop < in.getViews().length; loop++) {
					id = BProjectViewJoin.createAfter(id,
							in.getViews()[loop].getProjectId(), in.getViews()[loop].getSummary(),
							in.getViews()[loop].getDescription());
					rets.add(id);
				}
			}

			ret.setId(rets.toArray(new String[rets.size()]));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveProjectViewReturn saveProjectView(/*
			 * @WebParam(name = "in")
			 */final SaveProjectViewInput in) {
		final SaveProjectViewReturn ret = new SaveProjectViewReturn();
		try {
			checkLogin(in);

			final BProjectViewJoin x = new BProjectViewJoin(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchCompanyReturn searchCompany(/*
			 * @WebParam(name = "in")
			 */final SearchCompanyInput in) {
		final SearchCompanyReturn ret = new SearchCompanyReturn();

		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.search(in.getName(), false, ret.getHighCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*
			 * @WebParam(name = "in")
			 */final SearchProjectCategoriesInput in) {
		final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*
			 * @WebParam(name = "in")
			 */final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*
			 * @WebParam(name = "in")
			 */final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();

		try {
			checkLogin(in);

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*
			 * @WebParam(name = "in")
			 */final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					0, 0, null, false, in.getUser(), null, null, ret.getCap()));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ExportProjectViewReturn exportProjectView(/*
			 * @WebParam(name = "in")
			 */final ExportProjectViewInput in) {
		final ExportProjectViewReturn ret = new ExportProjectViewReturn();

		try {
			checkLogin(in);

			switch (in.getExportType()) {
				case 1:
					ret.setExportUrl(ProjectImport.exportProjectViewToCarbonFin(in.getId()));
					break;
				case 2:
					ret.setExportUrl(ProjectImport.exportProjectViewToPocketMindmap(in.getId()));
					break;
				default:
					throw new Exception("Unsupported Export Type requested.");
			}


			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ReorderProjectViewsReturn reorderProjectViews(/*
			 * @WebParam(name = "in")
			 */final ReorderProjectViewsInput in) {
		final ReorderProjectViewsReturn ret = new ReorderProjectViewsReturn();
		try {
			checkLogin(in);

			//move them so they won't collide
			for (int loop = 0; loop < in.getIds().length; loop++) {
				final BProjectViewJoin x = new BProjectViewJoin(in.getIds()[loop]);
				x.setSeq(loop + 10000);
				x.update();
			}

			//put them in the right order
			for (int loop = 0; loop < in.getIds().length; loop++) {
				final BProjectViewJoin x = new BProjectViewJoin(in.getIds()[loop]);
				x.setSeq(loop);
				x.update();
			}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadScreenStateReturn loadScreenState(/*
			 * @WebParam(name = "in")
			 */final LoadScreenStateInput in) {
		final LoadScreenStateReturn ret = new LoadScreenStateReturn();
		try {
			checkLogin(in);

			ret.setData(new BPerson(ArahantSession.getCurrentPerson()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveScreenStateReturn saveScreenState(/*
			 * @WebParam(name = "in")
			 */final SaveScreenStateInput in) {
		final SaveScreenStateReturn ret = new SaveScreenStateReturn();
		try {
			checkLogin(in);

			in.setData(new BPerson(ArahantSession.getCurrentPerson()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
