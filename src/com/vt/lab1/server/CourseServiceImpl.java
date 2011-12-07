package com.vt.lab1.server;

import java.security.SecureRandom;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vt.lab1.client.CourseService;
import com.vt.lab1.shared.CourseData;

@SuppressWarnings("serial")
public class CourseServiceImpl extends RemoteServiceServlet implements CourseService {

	@Override
	public List<String> getVersions(String name) throws IllegalArgumentException {
		log("getVersions(" + name + ")");
		
		List<String> versions = CourseData.getVersions(name);

		// random wait
		// Add a random number of children to the item
		long time = (new SecureRandom().nextInt(5)) * 1000 ;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return versions;
	}

}
