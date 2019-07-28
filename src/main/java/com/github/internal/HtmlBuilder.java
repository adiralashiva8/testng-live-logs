package com.github.internal;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.testng.ITestResult;

public class HtmlBuilder {

	private void createLiveLogsFiles(File htmlFile) {
		try {
			if (htmlFile.createNewFile()) {
				System.out.println("LiveLogs file is created!");
			} else {
				System.out.println("LiveLogs file already exists.");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void launchLiveReport(File htmlFile) {
		createLiveLogsFiles(htmlFile);
		createTemplate(htmlFile);

		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createTemplate(File htmlFile) {
		StringBuilder builder = new StringBuilder();
		builder
		.append("<html><title>Live Logs</title><meta http-equiv=\"refresh\" content=\"100\" >")
		.append("<link href=\"https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css\" rel=\"stylesheet\"/>")
		.append("<link href=\"https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\"/>")
		.append("<script src=\"https://code.jquery.com/jquery-3.3.1.js\" type=\"text/javascript\"></script>")
		.append("<script src=\"https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js\" type=\"text/javascript\"></script>")
		.append("<script src=\"https://cdn.datatables.net/buttons/1.5.2/js/dataTables.buttons.min.js\" type=\"text/javascript\"></script>")
		.append("<script src=\"https://cdn.datatables.net/buttons/1.5.2/js/buttons.flash.min.js\" type=\"text/javascript\"></script>")
		.append("<script>$(document).ready(function() { $('#live').DataTable({\"order\": [[ 0, \"desc\" ]]});} );</script></html>")
		.append("<body><h3 style=\"color:#009688;text-align:center\"><b>Testng Live Logs</b></h3>")
		.append("<table id=\"live\" class=\"table table-striped table-bordered\">")
		.append("<thead><tr><th>Time</th><th>Class Name</th><th>Test Name</th><th>Status</th>")
		.append("<th>Message</th><th>Duration(s)</th></tr></thead><tbody>")
		.append("");
		writeToFile(htmlFile, builder.toString(), false);
	}

	private void writeToFile(File htmlFile, String text, boolean appendText) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(htmlFile, appendText);
			fileWriter.write(text);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void afterComplete(File htmlFile){
		 String completedText = "<table align=\"center\"><tr><th><h4>Execution completed! </h4></th></tr></table>";
		 writeToFile(htmlFile, completedText, true);
	}

	public void writeResultsToFile(File htmlFile, ITestResult result, String timeNow, String status, long duration) {
		StringBuilder builder = new StringBuilder();
		
		String resultStatus;
		
		switch (status) {
		case "Pass":
			resultStatus = "<td style=\"color: #006400\"><b>PASS</b></td>";
			break;
		case "Fail":
			resultStatus = "<td style=\"color: #B22222\"><b>FAIL</b></td>";
			break;
		case "Skip":
			resultStatus = "<td style=\"color: #DAA520\"><b>SKIP</b></td>";
			break;
		default:
			resultStatus = "<td>Unknown</td>";
			break;
		}
		
		String fullClassName = result.getTestClass().getName();
		String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		String methodName = result.getMethod().getMethodName();
		String errorMessage = "";
		Object[] testParameters = result.getParameters();

		if (testParameters.length > 0 && testParameters != null) {
			methodName = methodName + " (" + Arrays.toString(testParameters) + ")";
		}

		if (result.getThrowable() != null) {
			// errorMessage =
			// org.testng.internal.Utils.longStackTrace(result.getThrowable(),
			// false);
			errorMessage = result.getThrowable().getMessage();
		}
	      
		builder
		.append("<tr>")
		.append(String.format("<td>%s</td>",timeNow))
		.append(String.format("<td style=\"text-align: left;word-wrap: break-word;max-width: 300px; white-space: normal\">%s</td>",className))
		.append(String.format("<td style=\"text-align: left;word-wrap: break-word;max-width: 300px; white-space: normal\">%s</td>",methodName))
		.append(resultStatus)
		.append(String.format("<td style=\"text-align:left;word-wrap: break-word;max-width: 300px; white-space: normal\">%s</td>",errorMessage))
		.append(String.format("<td>%s</td>",duration))
		.append("</tr>");
		
		writeToFile(htmlFile, builder.toString(), true);
	}
}
