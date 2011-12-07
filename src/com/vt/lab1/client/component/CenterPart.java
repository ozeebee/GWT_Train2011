package com.vt.lab1.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class CenterPart extends Composite {

	private static CenterPartUiBinder uiBinder = GWT.create(CenterPartUiBinder.class);

	interface CenterPartUiBinder extends UiBinder<Widget, CenterPart> {
	}

	public CenterPart() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	TabLayoutPanel tabLayoutPanel;

	public CenterPart(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TabLayoutPanel getTabLayoutPanel() {
		return this.tabLayoutPanel;
	}
}
