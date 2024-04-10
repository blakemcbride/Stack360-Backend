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

package com.arahant.imports;

import com.arahant.business.BProject;
import com.arahant.business.BProjectViewJoin;
import com.arahant.services.ServiceBase;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Author: Blake McBride
 * Date: 3/24/18
 */
public class ProjectImport {

    public static void importProjectView(String userId, String password, String parentId, String locationTypeRelativeToId, String locationType, String fileName, int importType) {
        try {
            ArahantSession.getHSU().beginTransaction();

            TransmitInputBase transmitInputBase = new TransmitInputBase();

            transmitInputBase.setUser(userId);
            transmitInputBase.setPassword(password);

            ServiceBase sb = new ServiceBase();
            sb.checkLogin(transmitInputBase);

            switch (importType) {
                case 1:
                    importProjectViewFromCarbonFin(parentId, Integer.parseInt(locationType), locationTypeRelativeToId, new File(fileName));
                    break;
                case 2:
                    importProjectViewFromPocketMindmap(parentId, Integer.parseInt(locationType), locationTypeRelativeToId, new File(fileName));
                    break;
                default:
                    throw new Exception("Unsupported Import Type requested.");
            }


            ArahantSession.getHSU().commitTransaction();
        } catch (final Throwable e) {
            ArahantSession.getHSU().rollbackTransaction();
        } finally {
            ArahantSession.clearSession();
        }
    }

    private static void importProjectViewFromPocketMindmap(String parentProjectViewJoinId, int locationType, String locationTypeRelativeToId, Node parentNode) throws Exception {
        // get the Nodes element
        Node nodes = DOMUtils.getNode(parentNode, "Nodes");

        if (nodes != null) {
            // sort Node children by their Order attribute
            DOMUtils.sortElements((Element) nodes, new NodeOrderComparator(), false, false);

            // spin through Node children
            NodeList childNodes = nodes.getChildNodes();
            String projectViewJoinId = "";
            for (int idx = 0; idx < childNodes.getLength(); idx++) {
                Node childNode = childNodes.item(idx);
                String summary = DOMUtils.getAttribute((Element) childNode, "Display");
                Node descriptionNode = DOMUtils.getNode(childNode, "Text");
                String description = "";
                if (descriptionNode != null)
                    if (descriptionNode.getFirstChild() != null && descriptionNode.getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE) {
                        descriptionNode = descriptionNode.getFirstChild();
                        description = DOMUtils.getNodeValue(descriptionNode);
                    }

                // crete under the parent if first, else create after the previous
                if (idx == 0)
                    if (isEmpty(locationTypeRelativeToId))
                        projectViewJoinId = BProjectViewJoin.createUnder(parentProjectViewJoinId, "", summary, description);
                    else
                    if (locationType == 1)
                        // before
                        projectViewJoinId = BProjectViewJoin.createBefore(locationTypeRelativeToId, "", summary, description);
                    else
                        // after
                        projectViewJoinId = BProjectViewJoin.createAfter(locationTypeRelativeToId, "", summary, description);
                else
                    projectViewJoinId = BProjectViewJoin.createAfter(projectViewJoinId, "", summary, description);

                // send this node
                importProjectViewFromPocketMindmap(projectViewJoinId, 0, "", childNode);
            }
        }
    }

    private static void importProjectViewFromCarbonFin(String parentId, int locationType, String locationTypeRelativeToId, File importFile) throws Exception {
        try {
            Document document = DOMUtils.createDocument(importFile);

            // step one is to get the top level node from the title
            Node titleNode = DOMUtils.getNode(document, "/opml/head/title");
            String projectViewJoinId;
            String summary = DOMUtils.getNodeValue(titleNode);
            if (isEmpty(locationTypeRelativeToId))
                projectViewJoinId = BProjectViewJoin.createUnder(parentId, "", summary, "");
            else
            if (locationType == 1)
                // before
                projectViewJoinId = BProjectViewJoin.createBefore(locationTypeRelativeToId, "", summary, "");
            else
                // after
                projectViewJoinId = BProjectViewJoin.createAfter(locationTypeRelativeToId, "", summary, "");

            Node rootNode = DOMUtils.getNode(document, "/opml/body");

            importProjectViewFromCarbonFin(projectViewJoinId, 0, "", rootNode);
        } finally {
            importFile.delete();
        }
    }

    private static void importProjectViewFromCarbonFin(String parentProjectViewJoinId, int locationType, String locationTypeRelativeToId, Node parentNode) throws Exception {
        // get the outline elements
        NodeList outlineNodes = parentNode.getChildNodes();

        if (outlineNodes != null) {
            String projectViewJoinId = "";

            // spin through outline nodes
            for (int idx = 0; idx < outlineNodes.getLength(); idx++) {
                Node outlineNode = outlineNodes.item(idx);
                String summary = DOMUtils.getAttribute((Element) outlineNode, "text");
                String description = DOMUtils.getAttribute((Element) outlineNode, "_note");

                if (description == null)
                    description = "";

                // crete under the parent if first, else create after the previous
                if (idx == 0)
                    if (isEmpty(locationTypeRelativeToId))
                        projectViewJoinId = BProjectViewJoin.createUnder(parentProjectViewJoinId, "", summary, description);
                    else
                    if (locationType == 1)
                        // before
                        projectViewJoinId = BProjectViewJoin.createBefore(locationTypeRelativeToId, "", summary, description);
                    else
                        // after
                        projectViewJoinId = BProjectViewJoin.createAfter(locationTypeRelativeToId, "", summary, description);
                else
                    projectViewJoinId = BProjectViewJoin.createAfter(projectViewJoinId, "", summary, description);

                // send this node
                importProjectViewFromCarbonFin(projectViewJoinId, 0, "", outlineNode);
            }
        }
    }

    public static String exportProjectViewToPocketMindmap(String projectViewJoinId) throws IOException {
        File exportFile = FileSystemUtils.createTempFile("pocketmindmap", ".xml");
        BufferedWriter bufferedWriter = null;

        try {
            HibernateSessionUtil hsu = ArahantSession.getHSU();
            String author = hsu.getCurrentPerson().getNameFL();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0000000");
            String dateFormatted = simpleDateFormat.format(new Date());

            bufferedWriter = new BufferedWriter(new FileWriter(exportFile));

            // step 1 - write opening & meta data
            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            bufferedWriter.write("<PocketMindMap Version=\"1.4\" GUID=\"{5CF9DD80-4983-11DD-2120-0301E9DF0C1E}\" Author=\"");
            bufferedWriter.write(author);
            bufferedWriter.write("\" Creation=\"");
            bufferedWriter.write(dateFormatted);
            bufferedWriter.write("\" Modified=\"");
            bufferedWriter.write(dateFormatted);
            bufferedWriter.write("\" LastModifiedBy=\"");
            bufferedWriter.write(author);
            bufferedWriter.write("\" StartmapID=\"{5CF9DD80-4983-11DD-1DCB-0301E9E1721D}\" Revision=\"1\">");
            bufferedWriter.write("<Options LastZoomFactor=\"100\" LastNodeID=\"0\" LastMap=\"{5CF9DD80-4983-11DD-1DCB-0301E9E1721D}\"/>");
            bufferedWriter.write("<Style id=\"{5CF9DD80-4983-11DD-36A1-0301E9DE4328}\" Name=\"Pocket Mindmap\">");
            bufferedWriter.write("<RootNodeStyle DefaultText=\"Subject\">");
            bufferedWriter.write("<Font Family=\"Tahoma\" Bold=\"true\" Italic=\"false\" Color=\"ff000000\" StrikeOut=\"false\" Underline=\"false\" Size=\"10\"/>");
            bufferedWriter.write("<Border LineStyle=\"Solid\" LineColor=\"ff000000\" LineWidth=\"2.000000\" Padding=\"3.000000\" FillColor=\"ffffff00\"/>");
            bufferedWriter.write("</RootNodeStyle>");
            bufferedWriter.write("<FirstLevelNodeStyle DefaultText=\"Firstlevel\">");
            bufferedWriter.write("<Font Family=\"Tahoma\" Bold=\"false\" Italic=\"false\" Color=\"ff000000\" StrikeOut=\"false\" Underline=\"false\" Size=\"10\"/>");
            bufferedWriter.write("<Border LineStyle=\"None\" LineColor=\"ff000000\" LineWidth=\"1.000000\" Padding=\"0.000000\" FillColor=\"00ffffff\"/>");
            bufferedWriter.write("</FirstLevelNodeStyle>");
            bufferedWriter.write("<ChildNodeStyle DefaultText=\"Topic\">");
            bufferedWriter.write("<Font Family=\"Tahoma\" Bold=\"false\" Italic=\"false\" Color=\"ff000000\" StrikeOut=\"false\" Underline=\"false\" Size=\"8\"/>");
            bufferedWriter.write("<Border LineStyle=\"None\" LineColor=\"ff000000\" LineWidth=\"1.000000\" Padding=\"0.000000\" FillColor=\"00000000\"/>");
            bufferedWriter.write("</ChildNodeStyle>");
            bufferedWriter.write("<FloatingNodeStyle DefaultText=\"Floating Subject\">");
            bufferedWriter.write("<Font Family=\"Tahoma\" Bold=\"true\" Italic=\"false\" Color=\"ff000000\" StrikeOut=\"false\" Underline=\"false\" Size=\"7\"/>");
            bufferedWriter.write("<Border LineStyle=\"Solid\" LineColor=\"ff808080\" LineWidth=\"1.000000\" Padding=\"2.000000\" FillColor=\"ffffff99\"/>");
            bufferedWriter.write("</FloatingNodeStyle>");
            bufferedWriter.write("<FloatingFirstLevelStyle DefaultText=\"Floating Topic\">");
            bufferedWriter.write("<Font Family=\"Tahoma\" Bold=\"false\" Italic=\"false\" Color=\"ff000000\" StrikeOut=\"false\" Underline=\"false\" Size=\"8\"/>");
            bufferedWriter.write("<Border LineStyle=\"None\" LineColor=\"ff808080\" LineWidth=\"1.000000\" Padding=\"0.000000\" FillColor=\"00000000\"/>");
            bufferedWriter.write("</FloatingFirstLevelStyle>");
            bufferedWriter.write("<FloatingChildStyle DefaultText=\"Floating Topic\">");
            bufferedWriter.write("<Font Family=\"Tahoma\" Bold=\"false\" Italic=\"false\" Color=\"ff000000\" StrikeOut=\"false\" Underline=\"false\" Size=\"8\"/>");
            bufferedWriter.write("<Border LineStyle=\"None\" LineColor=\"ff808080\" LineWidth=\"1.000000\" Padding=\"0.000000\" FillColor=\"00000000\"/>");
            bufferedWriter.write("</FloatingChildStyle>");
            bufferedWriter.write("<Background Color=\"ffffffff\" BitmapURL=\"\" OffsetX=\"0\" OffsetY=\"0\" Tile=\"false\" Stretch=\"false\"/>");
            bufferedWriter.write("</Style>");
            bufferedWriter.write("<Maps>");
            bufferedWriter.write("<Map id=\"{5CF9DD80-4983-11DD-1DCB-0301E9E1721D}\" Name=\"Map #0\" Creation=\"");
            bufferedWriter.write(dateFormatted);
            bufferedWriter.write("\" Modified=\"");
            bufferedWriter.write(dateFormatted);
            bufferedWriter.write("\" ScrollPosX=\"-1\" ScrollPosY=\"-1\">");
            bufferedWriter.write("<Nodes>");

            // step 2 - write actual outline

            // get sub project view joins and the display name
            BProjectViewJoin[] bProjectViewJoins;
            String displayName;
            BProjectViewJoin bProjectViewJoin = new BProjectViewJoin(projectViewJoinId);
            if (bProjectViewJoin.getType() == 1) {
                // project view
                bProjectViewJoins = BProjectViewJoin.list(projectViewJoinId);
                displayName = bProjectViewJoin.getSummary();
            } else {
                // project
                bProjectViewJoins = new BProjectViewJoin[0];
                displayName = new BProject(bProjectViewJoin.getProjectId()).getSummary();
            }

            displayName = DOMUtils.escapeText(displayName);

            // write top level node
            bufferedWriter.write("<Node id=\"{6B334400-AAB2-11DD-2E40-09230C004944}\" Display=\"");
            bufferedWriter.write(displayName);
            bufferedWriter.write("\" ");
            if (bProjectViewJoins.length == 0)
                bufferedWriter.write("ShowNodeArrow=\"true\" ");
            bufferedWriter.write("Created=\"");
            bufferedWriter.write(dateFormatted);
            bufferedWriter.write("\" Modified=\"");
            bufferedWriter.write(dateFormatted);
            bufferedWriter.write("\" NodeExpanded=\"true\" Order=\"-1\"");

            // check for sub-project-views
            if (bProjectViewJoins.length == 0)
                // no sub-project-views so close start element as empty element
                bufferedWriter.write(" />");
            else {
                // we have sub-project-views so close start element, recurse, and write end element
                bufferedWriter.write(" >");

                exportProjectViewToPocketMindmap(bProjectViewJoins, bufferedWriter, dateFormatted);

                bufferedWriter.write("</Node>");
            }

            // step 3 - write closing data
            bufferedWriter.write("</Nodes>");
            bufferedWriter.write("<Background />");
            bufferedWriter.write("</Map>");
            bufferedWriter.write("</Maps>");
            bufferedWriter.write("</PocketMindMap>");
        } finally {
            if (bufferedWriter != null)
                try {
                    bufferedWriter.close();
                } catch (Exception ignored) {
                }

        }
        return FileSystemUtils.getHTTPPath(exportFile);
    }

    private static void exportProjectViewToPocketMindmap(BProjectViewJoin[] bProjectViewJoins, BufferedWriter bufferedWriter, String dateFormatted) throws IOException {
        bufferedWriter.write("<Nodes>");

        // spin through the joins and write the outline
        for (int idx = 0; idx < bProjectViewJoins.length; idx++) {
            BProjectViewJoin bProjectViewJoin = bProjectViewJoins[idx];

            // check for project view vs project
            if (bProjectViewJoin.getType() == 1) {
                // project view ...

                // write as node
                bufferedWriter.write("<Node id=\"{");
                bufferedWriter.write(UUID.randomUUID().toString().toUpperCase());
                bufferedWriter.write("}\" Display=\"");
                bufferedWriter.write(DOMUtils.escapeText(bProjectViewJoin.getSummary()));
                bufferedWriter.write("\" Created=\"");
                bufferedWriter.write(dateFormatted);
                bufferedWriter.write("\" Modified=\"");
                bufferedWriter.write(dateFormatted);
                bufferedWriter.write("\" NodeExpanded=\"false\" Order=\"");
                bufferedWriter.write(idx + "");
                bufferedWriter.write("\"");

                // check for sub-project-views
                BProjectViewJoin[] bSubProjectViewJoins = BProjectViewJoin.list(bProjectViewJoin.getId());
                if (bSubProjectViewJoins.length > 0) {
                    // we have sub-project-views so close start element and recurse
                    bufferedWriter.write(">");

                    exportProjectViewToPocketMindmap(bSubProjectViewJoins, bufferedWriter, dateFormatted);
                }

                // check for text
                if (isEmpty(bProjectViewJoin.getDescription()))
                    if (bSubProjectViewJoins.length == 0)
                        bufferedWriter.write("/>");
                    else
                        bufferedWriter.write("</Node>");
                else {
                    if (bSubProjectViewJoins.length == 0)
                        bufferedWriter.write(">");

                    bufferedWriter.write("<Text>");
                    bufferedWriter.write(DOMUtils.toCDATA(bProjectViewJoin.getDescription()));
                    bufferedWriter.write("</Text>");

                    bufferedWriter.write("</Node>");
                }
            } else {
                // project ...

                // write as node
                BProject bProject = new BProject(bProjectViewJoin.getProjectId());
                bufferedWriter.write("<Node id=\"{");
                bufferedWriter.write(UUID.randomUUID().toString().toUpperCase());
                bufferedWriter.write("}\" Display=\"");
                bufferedWriter.write(DOMUtils.escapeText(bProject.getSummary()));
                bufferedWriter.write("\" Created=\"");
                bufferedWriter.write(dateFormatted);
                bufferedWriter.write("\" Modified=\"");
                bufferedWriter.write(dateFormatted);
                bufferedWriter.write("\" NodeExpanded=\"false\" Order=\"");
                bufferedWriter.write(idx + "");
                bufferedWriter.write("\"");
                if (isEmpty(bProject.getDetailDesc()))
                    bufferedWriter.write("/>");
                else {
                    bufferedWriter.write("><Text>");
                    bufferedWriter.write(DOMUtils.toCDATA(bProject.getDetailDesc()));
                    bufferedWriter.write("</Text></Node>");
                }
            }
        }

        bufferedWriter.write("</Nodes>");
    }

    public static String exportProjectViewToCarbonFin(String projectViewJoinId) throws IOException {
        File exportFile = FileSystemUtils.createTempFile("carbonfin", ".opml");
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(exportFile));

            // step 1 - write opening & meta data
            bufferedWriter.write("<opml version='1.0'>");
            bufferedWriter.write("<head>");

            // step 2 - write actual outline

            // get sub project view joins and the display name
            BProjectViewJoin[] bProjectViewJoins;
            String displayName;
            BProjectViewJoin bProjectViewJoin = new BProjectViewJoin(projectViewJoinId);
            if (bProjectViewJoin.getType() == 1) {
                // project view
                bProjectViewJoins = BProjectViewJoin.list(projectViewJoinId);
                displayName = bProjectViewJoin.getSummary();
            } else {
                // project
                bProjectViewJoins = new BProjectViewJoin[0];
                displayName = new BProject(bProjectViewJoin.getProjectId()).getSummary();
            }

            displayName = DOMUtils.escapeText(displayName);

            // write top level node
            bufferedWriter.write("<title>");
            bufferedWriter.write(displayName);
            bufferedWriter.write("</title>");
            bufferedWriter.write("</head>");
            bufferedWriter.write("<body>");

            // check for sub-project-views
            if (bProjectViewJoins.length > 0)
                exportProjectViewToCarbonFin(bProjectViewJoins, bufferedWriter);

            // step 3 - write closing data
            bufferedWriter.write("</body>");
            bufferedWriter.write("</opml>");
        } finally {
            if (bufferedWriter != null)
                try {
                    bufferedWriter.close();
                } catch (Exception ignored) {
                }
        }
        return FileSystemUtils.getHTTPPath(exportFile);
    }

    private static void exportProjectViewToCarbonFin(BProjectViewJoin[] bProjectViewJoins, BufferedWriter bufferedWriter) throws IOException {
        // spin through the joins and write the outline
        for (BProjectViewJoin bProjectViewJoin : bProjectViewJoins) {
            // check for project view vs project
            if (bProjectViewJoin.getType() == 1) {
                // project view ...

                // write as node
                bufferedWriter.write("<outline text='");
                bufferedWriter.write(DOMUtils.escapeText(bProjectViewJoin.getSummary()));
                bufferedWriter.write("' _status=''");

                // check for text
                if (!isEmpty(bProjectViewJoin.getDescription())) {
                    bufferedWriter.write(" _note='");
                    bufferedWriter.write(DOMUtils.escapeText(bProjectViewJoin.getDescription()));
                    bufferedWriter.write("'");
                }

                // check for sub-project-views
                BProjectViewJoin[] bSubProjectViewJoins = BProjectViewJoin.list(bProjectViewJoin.getId());
                if (bSubProjectViewJoins.length > 0) {
                    // we have sub-project-views so close start element and recurse
                    bufferedWriter.write(">");

                    exportProjectViewToCarbonFin(bSubProjectViewJoins, bufferedWriter);

                    bufferedWriter.write("</outline>");
                } else
                    bufferedWriter.write(" />");
            } else {
                // project ...

                // write as node
                BProject bProject = new BProject(bProjectViewJoin.getProjectId());

                bufferedWriter.write("<outline text='");
                bufferedWriter.write(DOMUtils.escapeText(bProject.getSummary()));
                bufferedWriter.write("' _status=''");

                // check for text
                if (!isEmpty(bProject.getDetailDesc())) {
                    bufferedWriter.write(" _note='");
                    bufferedWriter.write(DOMUtils.escapeText(bProject.getDetailDesc()));
                    bufferedWriter.write("' />");
                } else
                    bufferedWriter.write(" />");
            }
        }
    }

    private static void importProjectViewFromPocketMindmap(String parentId, int locationType, String locationTypeRelativeToId, File importFile) throws Exception {
        try {
            Document document = DOMUtils.createDocument(importFile);
            Node rootNode = DOMUtils.getNode(document, "/PocketMindMap/Maps/Map");

            importProjectViewFromPocketMindmap(parentId, locationType, locationTypeRelativeToId, rootNode);

        } finally {
            importFile.delete();
        }
    }

    private static class NodeOrderComparator implements Comparator<Node> {

        @Override
        public int compare(final Node node1, final Node node2) {
            int order1 = Integer.parseInt(DOMUtils.getAttribute((Element) node1, "Order"));
            int order2 = Integer.parseInt(DOMUtils.getAttribute((Element) node2, "Order"));

            return Comparisons.compare(order1, order2);
        }
    }

    private static boolean isEmpty(final String str) {
        return str == null || str.equals("");
    }

}
