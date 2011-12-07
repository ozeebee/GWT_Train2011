package com.vt.lab1.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CourseServiceAsync {

	void getVersions(String name, AsyncCallback<List<String>> callback);

}
