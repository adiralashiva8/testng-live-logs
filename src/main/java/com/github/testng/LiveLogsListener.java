package com.github.testng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.Duration;
import java.time.Instant;

import org.testng.IReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.github.internal.HtmlBuilder;

public class LiveLogsListener extends HtmlBuilder implements IReporter, ITestListener{
	String logPath = System.getProperty("user.dir") + File.separator + "LiveLogs.html";
	File htmlFile = new File(logPath);
	Instant start;
	String timeNow = "";
	
	public void onStart(ITestContext context) {
		launchLiveReport(htmlFile);
	}

	public void onTestStart(ITestResult result) {
		timeNow = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().getTime());
		start = Instant.now();
	}

	public void onTestSuccess(ITestResult result) {
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		writeResultsToFile(htmlFile, result, timeNow, "Pass", timeElapsed.toMillis()/1000);
	}

	public void onTestFailure(ITestResult result) {
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		writeResultsToFile(htmlFile, result, timeNow, "Fail", timeElapsed.toMillis()/1000);
	}

	public void onTestSkipped(ITestResult result) {
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		writeResultsToFile(htmlFile, result, timeNow, "Skip", timeElapsed.toMillis()/1000);
	}
	
	public void onFinish(ITestContext context) {
		afterComplete(htmlFile);
	}
}
