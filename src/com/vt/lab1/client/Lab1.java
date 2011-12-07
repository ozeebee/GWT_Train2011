package com.vt.lab1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.vt.lab1.client.component.CenterPart;
import com.vt.lab1.client.component.LeftPart;
import com.vt.lab1.shared.CourseData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Lab1 implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.add(new Image("./LogoValtech-Training.png"));
		hPanel.add(new HTML("<h1>Course Manager</h1>"));

		DockLayoutPanel p = new DockLayoutPanel(Unit.PX);

		
		LeftPart leftPart = new LeftPart(CourseData.getCourses());
		CenterPart centerPart = new CenterPart();
		leftPart.setTabLayoutPanel(centerPart.getTabLayoutPanel());

		p.addNorth(hPanel, 100);
		p.addWest(leftPart, 250);
		p.add(centerPart);

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(p);
	}

}
