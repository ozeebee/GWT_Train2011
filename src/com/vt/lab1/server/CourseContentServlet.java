package com.vt.lab1.server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vt.lab1.shared.CourseData;

@SuppressWarnings("serial")
public class CourseContentServlet extends HttpServlet {
	private static final Pattern pattern = Pattern.compile("/([^/]*)/(.*)");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// parse request path
		System.out.println("path = " + req.getPathInfo());
		
		String path = req.getPathInfo();
		Matcher matcher = pattern.matcher(path);
		if (! matcher.matches()) {
			sendNotFound(resp, "Invalid request pattern [" + path + "]");
			return;
		}
		
		String courseName = matcher.group(1);
		String version = matcher.group(2);
		System.out.println(String.format("course = [%s] version=[%s]", courseName, version));
		
		boolean isJson = "application/json".equals(req.getHeader("Accept"));
		
		try {
			resp.setContentType(isJson ? "application/json": "text/xml");
			String content = CourseData.getVersionContent(courseName, version, isJson);
			
			resp.setContentLength(content.length());
			resp.getWriter().write(content);
		}
		catch (IllegalArgumentException ex) {
			sendNotFound(resp, ex.getMessage());
		}
	}

	private void sendNotFound(HttpServletResponse resp, String reason) throws IOException {
		resp.sendError(404, reason);
	}
}
