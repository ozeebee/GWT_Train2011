package com.vt.lab1.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Random;

public class CourseData {
	private static List<String> courses = new ArrayList<String>();
	private static Map<String, List<String>> versionMap = new HashMap<String, List<String>>();
	
	static {
		courses.add("ADP");
		courses.add("AJAX");
		courses.add("AOP");
		courses.add("ASP");
		courses.add("FLEX");
		courses.add("GWT");
		courses.add("JASI");
		courses.add("JEA");
		courses.add("JOD");
		courses.add("JSF");
		courses.add("JWEB");
	}
	
	public static List<String> getCourses() {
		return courses;
	}
	
	public static List<String> getVersions(String course) throws IllegalArgumentException {
		if (! courses.contains(course))
			throw new IllegalArgumentException("course does not exist : " + course);
		
		List<String> versions = versionMap.get(course);
		if (versions == null) {
			versions = new ArrayList<String>();
			
			int numChildren = 5;//Random.nextInt(5) + 2;
			for (int i = 0; i < numChildren; i++) {
				versions.add("1.0." + i);
			}
			
			versionMap.put(course, versions);
		}
		
		return versions;
	}
	
	public static String getVersionContent(String course, String version, boolean isJson) throws IllegalArgumentException {
		List<String> versions = getVersions(course);
		if (! versions.contains(version))
			throw new IllegalArgumentException("version does not exist : " + version);
		
		if (isJson) {
			// we use strict json
			StringBuffer sb = new StringBuffer("(");
			sb.append("{\"cid\":\""+course+"\",\"version\":\""+version+"\",\"lg\":\"fr\",");
			sb.append("\"title\":\"Développer vos applications avec ").append(course).append("\",");
			sb.append("\"duration\":\"5\",");
			sb.append("\"description\":\"Ce cours vraiment top\",");
			sb.append("\"labratio\":\"70\",");
			sb.append("\"prerequis\":[\"Java\",\"Web\"]}");
			sb.append(")");
			return sb.toString();
		}
		else {
			StringBuffer sb = new StringBuffer("<course cid='").append(course).append("' version='").append(version).append("' lg='fr'>");
			sb.append("<title>Developpement d'applications avec ").append(course).append("</title>");
			sb.append("<duration>5</duration>");
			sb.append("<description>Ce cours vraiment top ...</description>");
			sb.append("<labratio>70</labratio>");
			sb.append("<prerequis><requis>Java</requis><requis>Web</requis></prerequis>");
			sb.append("</course>");
			return sb.toString();
		}
	}
}
