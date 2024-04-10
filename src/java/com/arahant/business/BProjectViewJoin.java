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

import com.arahant.beans.Person;
import com.arahant.beans.ProjectView;
import com.arahant.beans.ProjectViewJoin;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.HashSet;
import java.util.List;

public class BProjectViewJoin extends SimpleBusinessObjectBase<ProjectViewJoin> {

	public static void delete(HibernateSessionUtil hsu, String[] ids, boolean removeAll) {
		for (String id : ids)
			new BProjectViewJoin(id).delete(removeAll);
	}

	@Override
	public void delete() {
		//I need to resequence everybody else
		//first set my seq number to something safe
		int seq = bean.getSeqNum();
		bean.setSeqNum(-999);
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.saveOrUpdate(bean);
		hsu.flush();

		shiftUp(seq);


		// Are there any other ProjectViewJoins with the same child ProjectView?
		List<ProjectViewJoin> l = hsu.createCriteria(ProjectViewJoin.class).ne(ProjectViewJoin.ID, bean.getProjectViewJoinId()).eq(ProjectViewJoin.CHILD, bean.getChild()).list();
		if (l.isEmpty()) {
			// Nope - so it is safe to delete my child ProjectView's ProjectViewJoins
			l = hsu.createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getChild()).list();

			for (ProjectViewJoin pvj : l)
				new BProjectViewJoin(pvj).delete();
		}

		super.delete();

		// clean up project views that have no joins
		hsu.createCriteria(ProjectView.class).sizeEq(ProjectView.VIEW_JOINS_WHERE_CHILD, 0).sizeEq(ProjectView.VIEW_JOINS_WHERE_PARENT, 0).delete();
	}

	public static void copy(String[] projectViewJoinIds, String projectViewJoinParentId, int locationType, String locationTypeRelativeToId, boolean deepCopy) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();

		ProjectViewJoin parentProjectViewJoin = hsu.get(ProjectViewJoin.class, projectViewJoinParentId);
		ProjectView parentProjectView = parentProjectViewJoin == null ? null : parentProjectViewJoin.getChild();

		for (String projectViewJoinId : projectViewJoinIds) {
			// we currently don't allow shallow copies that would cause a circular reference, so check if this copy would create that
			if (!deepCopy) {
				ProjectViewJoin projectViewJoin = hsu.get(ProjectViewJoin.class, projectViewJoinId);
				HashSet<String> projectViewIdsSet = new HashSet<String>();

				walkHierarchyForChildProjectViews(projectViewJoin, projectViewIdsSet);

				if (projectViewsExistInParentHierarchy(parentProjectView, projectViewIdsSet))
					throw new ArahantException("The requested Paste would create a circular reference and is therefore not allowed.");
			}

			HashSet<String> projectViewIdsForDeepCopySet = new HashSet<String>();
			switch (locationType) {
				case 0:
					copyAfter(locationTypeRelativeToId, parentProjectView, projectViewJoinId, deepCopy, projectViewIdsForDeepCopySet);
					break;
				case 1:
					copyBefore(locationTypeRelativeToId, parentProjectView, projectViewJoinId, deepCopy, projectViewIdsForDeepCopySet);
					break;
				case 2:
					copyAfter(locationTypeRelativeToId, parentProjectView, projectViewJoinId, deepCopy, projectViewIdsForDeepCopySet);
					break;
			}
		}
	}

	private static void walkHierarchyForChildProjectViews(ProjectViewJoin projectViewJoin, HashSet<String> projectViewIdsSet) {
		ProjectView projectView = projectViewJoin.getChild();
		String projectViewId = projectView.getProjectViewId();

		if (!projectViewIdsSet.contains(projectViewId)) {
			projectViewIdsSet.add(projectViewId);

			// now find all project view joins where parent of the join is projectViewId and call collect on those
			List<ProjectViewJoin> childProjectViewJoins = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, projectViewJoin.getChild()).list();

			for (ProjectViewJoin childProjectViewJoin : childProjectViewJoins)
				walkHierarchyForChildProjectViews(childProjectViewJoin, projectViewIdsSet);
		}
	}

	private static boolean projectViewsExistInParentHierarchy(ProjectView parentProjectView, HashSet<String> projectViewIdsSet) {
		boolean exists;

		// find if any of these ids are used in a parent
		if (parentProjectView == null)
			exists = false;
		else if (projectViewIdsSet.contains(parentProjectView.getProjectViewId()))
			exists = true;
		else {
			exists = false; // false because we assume we won't have any parents

			List<ProjectViewJoin> parentProjectViewJoins = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.CHILD, parentProjectView).list();

			for (ProjectViewJoin parentProjectViewJoin : parentProjectViewJoins) {
				exists = projectViewsExistInParentHierarchy(parentProjectViewJoin.getParent(), projectViewIdsSet);

				if (exists)
					break;
			}
		}

		return exists;
	}

	private static void copyBefore(String locationTypeRelativeToId, ProjectView parent, String projectViewJoinId, boolean deepCopy, HashSet<String> projectViewIdsForDeepCopySet) {
		if (deepCopy) {
			BProjectViewJoin oldbpvj = new BProjectViewJoin(projectViewJoinId);

			// if we have already processed this child (circulary reference of what just got copied), we are done
			if (projectViewIdsForDeepCopySet.contains(oldbpvj.getChild().getProjectViewId()))
				return;

			ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, locationTypeRelativeToId);
			int seq = pv.getSeqNum();
			shiftDown(pv.getParent(), pv.getSeqNum());

			BProjectView bpv = oldbpvj.getChildClone();

			BProjectViewJoin bpvj = new BProjectViewJoin();
			bpvj.create();
			bpvj.setSeq(seq);
			bpvj.setParent(pv.getParent());
			bpvj.setChild(bpv.bean);
			bpvj.insert();

			projectViewIdsForDeepCopySet.add(bpv.getId());

			//recurse down
			bpvj.recurseCopy(oldbpvj, projectViewIdsForDeepCopySet);

		} else {
			ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, locationTypeRelativeToId);
			int seq = pv.getSeqNum();
			shiftDown(pv.getParent(), pv.getSeqNum());

			BProjectViewJoin oldbpvj = new BProjectViewJoin(projectViewJoinId);

			BProjectViewJoin bpvj = new BProjectViewJoin();
			String ret = bpvj.create();
			bpvj.setSeq(seq);
			bpvj.setParent(pv.getParent());
			bpvj.setChild(oldbpvj.getChild());
			bpvj.insert();
		}
	}

	private static void copyAfter(String locationTypeRelativeToId, ProjectView parentProjectView, String projectViewJoinId, boolean deepCopy, HashSet<String> projectViewIdsForDeepCopySet) {
		if (deepCopy) {
			BProjectViewJoin oldbpvj = new BProjectViewJoin(projectViewJoinId);

			// if we have already processed this child (circulary reference of what just got copied), we are done
			if (projectViewIdsForDeepCopySet.contains(oldbpvj.getChild().getProjectViewId()))
				return;

			ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, locationTypeRelativeToId);
			int seq = (pv != null) ? pv.getSeqNum() + 1 : 0;

			shiftDown((pv != null) ? pv.getParent() : parentProjectView, seq);

			BProjectView bpv = oldbpvj.getChildClone();

			BProjectViewJoin bpvj = new BProjectViewJoin();
			bpvj.create();
			bpvj.setSeq(seq);
			bpvj.setParent((pv != null) ? pv.getParent() : parentProjectView);
			bpvj.setChild(bpv.bean);
			bpvj.insert();
			ArahantSession.getHSU().flush();

			projectViewIdsForDeepCopySet.add(bpv.getId());

			//recurse down
			bpvj.recurseCopy(oldbpvj, projectViewIdsForDeepCopySet);
		} else {
			ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, locationTypeRelativeToId);
			int seq = (pv != null) ? pv.getSeqNum() + 1 : 0;

			shiftDown((pv != null) ? pv.getParent() : parentProjectView, seq);

			BProjectViewJoin bpv = new BProjectViewJoin(projectViewJoinId);

			BProjectViewJoin bpvj = new BProjectViewJoin();
			bpvj.create();
			bpvj.setSeq(seq);
			bpvj.setParent((pv != null) ? pv.getParent() : parentProjectView);
			bpvj.setChild(bpv.getChild());
			bpvj.insert();
		}
	}

	public boolean getHasSubProjectViews() {
		//if (isEmpty(getProjectId()))
		//	return false;

		return ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getChild()).exists();
	}

	private BProjectView getChildClone() {
		BProjectView bpv = new BProjectView();
		bpv.create();
		bpv.setDescription(getDescription());
		bpv.setSummary(getSummary());
		bpv.setProjectId(getProjectId());
		bpv.setPerson(getPerson());
		bpv.insert();

		return bpv;
	}

	private void recurseCopy(BProjectViewJoin oldBpvj, HashSet<String> projectViewIdsForDeepCopySet) {
		List<ProjectViewJoin> l = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, oldBpvj.getChild()).orderByDesc(ProjectViewJoin.SEQ).list();

		for (ProjectViewJoin pvj : l)
			//need to copy this into my current one
			copyAfter("", getChild(), pvj.getProjectViewJoinId(), true, projectViewIdsForDeepCopySet);
	}

	public static String createUnder(String parentId, String projectId, String summary, String description) {
		ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, parentId);
		BProjectView bpv = new BProjectView();
		bpv.create();
		bpv.setDescription(description);
		bpv.setSummary(summary);
		bpv.setProjectId(projectId);
		bpv.setPerson(ArahantSession.getCurrentPerson());
		bpv.insert();
		BProjectViewJoin bpvj = new BProjectViewJoin();
		String ret = bpvj.create();
		bpvj.setSeq(0);
		bpvj.setParent((pv == null) ? null : pv.getChild());
		bpvj.setChild(bpv.bean);
		bpvj.insert();
		return ret;
	}

	public static String createBefore(String projectViewJoinId, String projectId, String summary, String description) {
		ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, projectViewJoinId);
		int seq = pv.getSeqNum();
		shiftDown(pv.getParent(), pv.getSeqNum());
		BProjectView bpv = new BProjectView();
		bpv.create();
		bpv.setSummary(summary);
		bpv.setDescription(description);
		bpv.setProjectId(projectId);
		bpv.setPerson(ArahantSession.getCurrentPerson());
		bpv.insert();
		BProjectViewJoin bpvj = new BProjectViewJoin();
		String ret = bpvj.create();
		bpvj.setSeq(seq);
		bpvj.setParent(pv.getParent());
		bpvj.setChild(bpv.bean);
		bpvj.insert();
		return ret;
	}

	public static String createAfter(String projectViewJoinId, String projectId, String summary, String description) {
		ProjectViewJoin pv = ArahantSession.getHSU().get(ProjectViewJoin.class, projectViewJoinId);
		int seq = (pv != null) ? pv.getSeqNum() : 0;
		if (pv != null)
			shiftDown(pv.getParent(), pv.getSeqNum() + 1);
		BProjectView bpv = new BProjectView();
		bpv.create();
		bpv.setDescription(description);
		bpv.setSummary(summary);
		bpv.setProjectId(projectId);
		bpv.setPerson(ArahantSession.getCurrentPerson());
		bpv.insert();
		BProjectViewJoin bpvj = new BProjectViewJoin();
		String ret = bpvj.create();
		bpvj.setSeq(seq + 1);
		bpvj.setParent((pv != null) ? pv.getParent() : null);
		bpvj.setChild(bpv.bean);
		bpvj.insert();
		return ret;
	}

	private void shiftUp(int seq) {
		for (ProjectViewJoin pvj : ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getParent()).ge(ProjectViewJoin.SEQ, seq).orderBy(ProjectViewJoin.SEQ).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, bean.getChild().getPerson()).list()) {
			pvj.setSeqNum(pvj.getSeqNum() - 1);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
		}
	}

	private static void shiftDown(ProjectView parent, int seq) {
		for (ProjectViewJoin pvj : ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, parent).ge(ProjectViewJoin.SEQ, seq).orderByDesc(ProjectViewJoin.SEQ).list()) {
			pvj.setSeqNum(pvj.getSeqNum() + 1);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
		}
	}

	public BProjectViewJoin(String id) {
		internalLoad(id);
	}

	public BProjectViewJoin() {
	}

	public BProjectViewJoin(ProjectViewJoin pvj) {
		bean = pvj;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectViewJoin();
		return bean.generateId();
	}

	public String getDescription() {
		return new BProjectView(bean.getChild()).getDescription();
	}

	public String getId() {
		return bean.getProjectViewJoinId();
	}

	public String getProjectId() {
		return new BProjectView(bean.getChild()).getProjectId();
	}

	public String getSummary() {
		return new BProjectView(bean.getChild()).getSummary();
	}

	public int getType() {
		return new BProjectView(bean.getChild()).getType();
	}

	public String getTypeFormatted() {
		return new BProjectView(bean.getChild()).getTypeFormatted();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectViewJoin.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public void moveDown() {
		if (bean.getSeqNum() != ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getParent()).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, bean.getChild().getPerson()).count() - 1) {
			ProjectViewJoin pvj = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getParent()).eq(ProjectViewJoin.SEQ, bean.getSeqNum() + 1).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, bean.getChild().getPerson()).first();

			int temp = bean.getSeqNum();
			pvj.setSeqNum(999999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setSeqNum(bean.getSeqNum() + 1);
			ArahantSession.getHSU().saveOrUpdate(bean);
			pvj.setSeqNum(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ProjectViewJoin> l = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getParent()).orderBy(ProjectViewJoin.SEQ).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, bean.getChild().getPerson()).list();

			l.get(l.size() - 1).setSeqNum(99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
			ArahantSession.getHSU().flush();

			for (int loop = l.size() - 1; loop > -1; loop--) {
				l.get(loop).setSeqNum(loop + 1);
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(l.size() - 1).setSeqNum(0);
			ArahantSession.getHSU().saveOrUpdate(l.get(l.size() - 1));
		}
	}

	public void moveUp() {
		if (bean.getSeqNum() > 0) {
			ProjectViewJoin pvj = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getParent()).eq(ProjectViewJoin.SEQ, bean.getSeqNum() - 1).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, bean.getChild().getPerson()).first();

			int temp = bean.getSeqNum();
			pvj.setSeqNum(99999);
			ArahantSession.getHSU().saveOrUpdate(pvj);
			ArahantSession.getHSU().flush();
			bean.setSeqNum(bean.getSeqNum() - 1);
			ArahantSession.getHSU().saveOrUpdate(bean);
			ArahantSession.getHSU().flush();
			pvj.setSeqNum(temp);
			ArahantSession.getHSU().saveOrUpdate(pvj);
		} else //shift them all
		{
			List<ProjectViewJoin> l = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.PARENT, bean.getParent()).orderBy(ProjectViewJoin.SEQ).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, bean.getChild().getPerson()).list();

			l.get(0).setSeqNum(99999);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
			ArahantSession.getHSU().flush();
			for (int loop = 1; loop < l.size(); loop++) {
				l.get(loop).setSeqNum(l.get(loop).getSeqNum() - 1);
				ArahantSession.getHSU().saveOrUpdate(l.get(loop));
				ArahantSession.getHSU().flush();
			}

			l.get(0).setSeqNum(l.size() - 1);
			ArahantSession.getHSU().saveOrUpdate(l.get(0));
		}
	}

	public void setDescription(String description) {
		bean.getChild().setNodeDescription(description);
	}

	public void setSummary(String summary) {
		bean.getChild().setNodeTitle(summary);
	}

	public static BProjectViewJoin[] list(String parentId) {

		ProjectViewJoin parentJoin = ArahantSession.getHSU().get(ProjectViewJoin.class, parentId);

		ProjectView parent = null;

		if (parentJoin != null)
			parent = parentJoin.getChild();

		HibernateCriteriaUtil<ProjectViewJoin> hcu = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class);
		hcu.eq(ProjectViewJoin.PARENT, parent);

		return makeArray(hcu.orderBy(ProjectViewJoin.SEQ).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, ArahantSession.getHSU().getCurrentPerson()).list());
	}

	static BProjectViewJoin[] makeArray(List<ProjectViewJoin> list) {
		BProjectViewJoin[] ret = new BProjectViewJoin[list.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectViewJoin(list.get(loop));
		return ret;
	}

	public void delete(boolean removeAll) {
		ProjectView child = bean.getChild();

		if (!removeAll)
			delete();
		else {

			List<ProjectViewJoin> l = ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.CHILD, child).joinTo(ProjectViewJoin.CHILD).eq(ProjectView.PERSON, child.getPerson()).list();

			for (ProjectViewJoin pvj : l)
				new BProjectViewJoin(pvj).delete();

		}
	}

	private ProjectView getChild() {
		return bean.getChild();
	}

	private ProjectView getParent() {
		return bean.getParent();
	}

	private Person getPerson() {
		return bean.getChild().getPerson();
	}

	private int getSeq() {
		return bean.getSeqNum();
	}

	public void setChild(ProjectView pv) {
		bean.setChild(pv);
	}

	public void setParent(ProjectView parent) {
		bean.setParent(parent);

	}

	private void setParentId(String id) {
		bean.setParent(ArahantSession.getHSU().get(ProjectView.class, id));
	}

	public void setSeq(int seq) {
		bean.setSeqNum(seq);
	}

	@Override
	public void insert() throws ArahantException {
		//check for same folder copy
		if (ArahantSession.getHSU().createCriteria(ProjectViewJoin.class).eq(ProjectViewJoin.CHILD, bean.getChild()).eq(ProjectViewJoin.PARENT, bean.getParent()).exists())
			throw new ArahantWarning("A project or folder may not be in the same parent folder more than once.");

		super.insert();
	}
}
