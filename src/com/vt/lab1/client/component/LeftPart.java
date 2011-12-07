package com.vt.lab1.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.vt.lab1.client.Course;
import com.vt.lab1.client.CourseService;
import com.vt.lab1.client.CourseServiceAsync;

public class LeftPart extends Composite /* implements HasText */{

	private static LeftPartUiBinder uiBinder = GWT.create(LeftPartUiBinder.class);

	interface LeftPartUiBinder extends UiBinder<Widget, LeftPart> {
	}

	private List<String> courses;

	public LeftPart(List<String> courses) {
		initWidget(uiBinder.createAndBindUi(this));
		this.courses = courses;
		// initialize tree contents
		fillTree();
	}

	@UiField
	TextBox filterTxt;
	@UiField
	Tree tree;

	private TabLayoutPanel tabLayoutPanel;

	public LeftPart(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		// button.setText(firstName);
	}

	public void setTabLayoutPanel(TabLayoutPanel tabLayoutPanel) {
		this.tabLayoutPanel = tabLayoutPanel;
	}

	// ~~~~~~~~~~~~~~~~~ Handlers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@UiHandler("filterTxt")
	public void onKeyUp(KeyUpEvent event) {
		String txt = filterTxt.getText();
		filterTree(txt.trim());
	}

	@UiHandler("tree")
	public void onOpen(OpenEvent<TreeItem> event) {
		final TreeItem item = event.getTarget();
		if (item.getChildCount() == 1) {
			// Close the item immediately
			// item.setState(false, false);

			String courseName = item.getText();

			// RPC call
			CourseServiceAsync courseService = GWT.create(CourseService.class);
			courseService.getVersions(courseName, new AsyncCallback<List<String>>() {
				@Override
				public void onSuccess(List<String> result) {
					GWT.log("success " + result);
					addVersionsToItem(result, item);

					// Remove the temporary item when we finish loading
					item.getChild(0).remove();

					// Reopen the item
					// item.setState(true, false);
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("failure " + caught);

					// Reopen the item
					// item.setState(true, false);
				}
			});

		}
	}

	@UiHandler("tree")
	public void onSelection(SelectionEvent<TreeItem> event) {
		final TreeItem item = event.getSelectedItem();
		if (item.getParentItem() == null) {
			// course selected
			String courseName = item.getText();
			GWT.log("course selected : " + courseName);
			// open/close tree item
			//GWT.log("  node state = " + item.getState());
			item.setState(! item.getState(), true);
		} else {
			// version selected
			String courseName = item.getParentItem().getText();
			String version = item.getText();

			showContent(courseName, version);
		}
	}

	// ~~~~~~~~~~~~~~~~~ Internal methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected void filterTree(String txt) {
		if (txt == null | txt.length() == 0) {
			// fill list
			fillTree();
		} else {
			fillTree(txt);
		}
	}

	private void fillTree() {
		tree.removeItems();
		for (String course : courses) {
			TreeItem item = new TreeItem(course);
			tree.addItem(item);
			item.addItem(new TreeItem("<img src='indicator.white.gif'>"));
		}
	}

	private void fillTree(String filter) {
		tree.removeItems();
		for (String course : courses) {
			if (course.startsWith(filter)) {
				TreeItem item = new TreeItem(course);
				tree.addItem(item);
				item.addItem(new TreeItem("<img src='indicator.white.gif'>"));
			}
		}
	}

	private void addVersionsToItem(List<String> versions, TreeItem item) {
		for (String version : versions) {
			TreeItem child = item.addItem(version);
			// child.addItem("");
		}
	}

	private void showContent(String courseName, String version) {
		String resturl = "./lab1/coursecontent/" + courseName + "/" + version;
		GWT.log("sending request to : " + resturl);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, resturl);
		final boolean isJson = true;
		builder.setHeader("Accept", isJson ? "application/json" : "application/xml");
		builder.setCallback(new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getStatusCode() == 200) {
					if (isJson)
						showJsonContent(response.getText());
					else
						showXmlContent(response.getText());
				} 
				else {
					GWT.log(response.getText());
					if (response.getStatusCode() == 404) {
						final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
						simplePopup.setWidth("150px");
						simplePopup.setWidget(new HTML("Cannot find course : " + response.getText()));
						simplePopup.setAutoHideEnabled(true);
						simplePopup.center();
						simplePopup.show();
					}
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				GWT.log(exception.getMessage());
			}
		});
		try {
			builder.send();
		} 
		catch (RequestException e) {
			e.printStackTrace();
		}
	}

	private void showXmlContent(String xml) {
		Document messageDom = XMLParser.parse(xml);
		Element course = (Element) messageDom.getElementsByTagName("course").item(0);

		String courseName = course.getFirstChild().getFirstChild().getNodeValue();
		String version = course.getAttributeNode("version").getValue();
		String duration = course.getElementsByTagName("duration").item(0).getFirstChild().getNodeValue();
		String labratio = course.getElementsByTagName("labratio").item(0).getFirstChild().getNodeValue();
		String description = course.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		List<String> prereqList = null;
		NodeList nl = course.getElementsByTagName("requis");
		if (nl.getLength() != 0) {
			prereqList = new ArrayList<String>();
			for (int i = 0; i < nl.getLength(); i++) {
				prereqList.add(((Element) nl.item(i)).getFirstChild().getNodeValue());
			}
		}
		
//		StringBuffer sb = new StringBuffer("<H2>Title : ").append(course.getFirstChild().getFirstChild().getNodeValue()).append("</H2>")
//				.append("<p>Version : ").append(course.getAttributeNode("version").getValue())
//				.append("<p>Duration : ").append(course.getElementsByTagName("duration").item(0).getFirstChild().getNodeValue()).append(" days")
//				.append("<p>TP : ").append(course.getElementsByTagName("labratio").item(0).getFirstChild().getNodeValue()).append("%")
//				.append("<p>").append(course.getElementsByTagName("description").item(0).getFirstChild().getNodeValue());
//
//		NodeList nl = course.getElementsByTagName("requis");
//		if (nl.getLength() != 0) {
//			sb.append("<p> Pre-requisite : <ol>");
//			for (int i = 0; i < nl.getLength(); i++) {
//				sb.append("<li>" + ((Element) nl.item(i)).getFirstChild().getNodeValue() + "</li>");
//			}
//			sb.append("</ol>");
//		}
		
		String html = generateHtml(courseName, version, duration, labratio, description, prereqList);
		
		tabLayoutPanel.selectTab(0);
		((HTML) tabLayoutPanel.getWidget(0)).setHTML(html);
	}
	
	private void showJsonContent(String json) {
		GWT.log(json);
		Course course = parseCourse(json);
		List<String> prereqList = null;
		if (course.getPrerequisites() != null && course.getPrerequisites().length() > 0) {
			prereqList = new ArrayList<String>();
			for (int i=0; i<course.getPrerequisites().length(); i++)
				prereqList.add(course.getPrerequisites().get(i));
		}
		String html = generateHtml(course.getTitle(), course.getVersion(), course.getDuration(), course.getLabratio(), 
				course.getDescription(), prereqList);
		
		tabLayoutPanel.selectTab(0);
		((HTML) tabLayoutPanel.getWidget(0)).setHTML(html);
	}
	
	private String generateHtml(String courseName, String version, String duration, String labratio, String description, List<String> prereqList) {
		StringBuffer sb = new StringBuffer("<H2>Title : ").append(courseName).append("</H2>")
			.append("<p>Version : ").append(version)
			.append("<p>Duration : ").append(duration).append(" days")
			.append("<p>TP : ").append(labratio).append("%")
			.append("<p>").append(description);
		if (prereqList != null && prereqList.size() > 0) {
			sb.append("<p> Pre-requisite : <ol>");
			for (String prereq : prereqList) {
				sb.append("<li>").append(prereq).append("</li>");
			}
			sb.append("</ol>");
		}
		return sb.toString();
	}
	
	public native Course parseCourse(String jsonStr) /*-{
		//console.log("before jsonStr=["+jsonStr+"]");
		//val course = eval(jsonStr);
		//console.log("after");
		//return course;
		return eval(jsonStr);
		//return {"cid":"AJAX","version":"1.0.0","lg":"fr","title":"Développer vos applications avec AJAX","duration":"5","description":"Ce cours vraiment top","labratio":"70","prerequis":["Java","Web"]};
	}-*/;
}
