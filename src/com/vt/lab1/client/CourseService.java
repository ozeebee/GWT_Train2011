package com.vt.lab1.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("course")
public interface CourseService extends RemoteService {
	
	List<String> getVersions(String name) throws IllegalArgumentException;
	
}
