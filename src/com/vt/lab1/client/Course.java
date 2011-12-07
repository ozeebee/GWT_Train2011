package com.vt.lab1.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class Course extends JavaScriptObject {
	protected Course() {}
	
	public final native String getTitle() /*-{ return this.title; }-*/;
	public final native String getVersion() /*-{ return this.version; }-*/;
	public final native String getDuration() /*-{ return this.duration; }-*/;
	public final native String getLabratio() /*-{ return this.labratio; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	public final native JsArrayString getPrerequisites() /*-{ return this.prerequis; }-*/;

}
