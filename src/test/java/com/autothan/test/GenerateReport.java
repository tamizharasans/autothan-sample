package com.autothan.test;

import com.autothan.base.*;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class GenerateReport {


	//List of objected initialized
	private List<String> widgetActionInfo = null;
	private RunTCInfo runTCInfo = null;
	private TransformTestReport transformTestReport = null;

	private String testScenarioId = "NA";
	private String testScenarioName = "NA";
	private String testScenarioBlocker = "NA";
	private String testScenarioStatus = "FAIL";

	private String testStepId =  "NA";
	private String testStepStatus =  "NA";
	private String testStepDetails =  "#STEP_INFO-#";
	private String testStepErrorDetails = "NA";
	private String JSError = "NA";
	private String JSErrorDescription = "NA";

	private int STEPFAIL = 0;
	private int STEPWARNING = 0;
	private int STEPUSERISSUE =0;
	private int STEPSCENARIOWARNING = 0;

	private String osInfo = "NA";
	private String browserInfo = "NA";
	private String builtInfo="JDK 1.8";
	private String hostInfo = "NA";
	private String envInfo = "NA";

	List<RunTCInfo> testStepInfo = new ArrayList<RunTCInfo>();
	String modeOfExec=System.getProperty("modeOfExec");




		public void genReport(List<MovieObj> movieObjs) {

			if(modeOfExec==null){
				modeOfExec="GUI";
			}

			try {

				String tagetTestClassesPath = this.getClass().getClassLoader().getResource(".").getPath();

				String XSLFLPATH;
				if("GUI".equals(modeOfExec)) {
					XSLFLPATH=tagetTestClassesPath + "TestReports/GUITestReportXSL.xsl";
				}else{
					XSLFLPATH=tagetTestClassesPath + "TestReports/RESTTestReportXSL.xsl";
				}
				String XML_FILE =  tagetTestClassesPath+"TestReports/";
				String HTML_FILE = tagetTestClassesPath+"TestReports/";
				String SCREENSHOT_FLPATH = tagetTestClassesPath+"TestRunSceenshots/";
				String HTML_FLPATH = HTML_FILE + "GenerateReport.html";
				String XML_REPORT = XML_FILE + "GenerateReport.xml";

				InetAddress iAddress = InetAddress.getLocalHost();
				String hostName = iAddress.getHostName();
				//To get  the Canonical host name
				String canonicalHostName = iAddress.getCanonicalHostName();
				hostInfo = hostName;


				//Initializing DOM for generating Widget HTML Test Report
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();





				Element report = document.createElement("report");
				document.appendChild(report);

				for (MovieObj movieObj1:movieObjs) {

					transformTestReport = new TransformTestReport();
					//Updating data for Root Child Elements in Dynamic XML to generate Widget Report

					Element topic = document.createElement("topic");

					Element movieNo = document.createElement("movieId");
					movieNo.appendChild(document.createTextNode(String.valueOf(movieObj1.getMovieId())));
					topic.appendChild(movieNo);

					Element movieName = document.createElement("movieName");
					movieName.appendChild(document.createTextNode(movieObj1.getMovieName()));
					topic.appendChild(movieName);

					Element wikiUrl = document.createElement("wikiUrl");
					wikiUrl.appendChild(document.createTextNode(movieObj1.getWikiUrl()));
					topic.appendChild(wikiUrl);

					Element wikiSnapShotUrl = document.createElement("wikiSnapShotUrl");
					wikiSnapShotUrl.appendChild(document.createTextNode(movieObj1.getWikiSnapShotUrl()));
					topic.appendChild(wikiSnapShotUrl);

					Element wikiDirName = document.createElement("wikiDirName");
					wikiDirName.appendChild(document.createTextNode(movieObj1.getWikiDirName()));
					topic.appendChild(wikiDirName);

					Element imdbUrl = document.createElement("imdbUrl");
					imdbUrl.appendChild(document.createTextNode(movieObj1.getImdbUrl()));
					topic.appendChild(imdbUrl);

					Element imdbSnapShotUrl = document.createElement("imdbSnapShotUrl");
					imdbSnapShotUrl.appendChild(document.createTextNode(movieObj1.getImdbSnapShotUrl()));
					topic.appendChild(imdbSnapShotUrl);

					Element imdbDirName = document.createElement("imdbDirName");
					imdbDirName.appendChild(document.createTextNode(movieObj1.getImdbDirName()));
					topic.appendChild(imdbDirName);

					Element errorStatus = document.createElement("errorStatus");
					errorStatus.appendChild(document.createTextNode(movieObj1.getErrorMessage()));
					topic.appendChild(errorStatus);

					report.appendChild(topic);

				}


				//Transforming Test Case Report
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(XML_REPORT));
				transformer.transform(domSource, streamResult);
				System.out.println("Done creating Report-XML File");

				TestReportUtil testReportUtil = new TestReportUtil(XSLFLPATH, XML_REPORT, HTML_FLPATH);
				testReportUtil.generateTestReport();



			} catch (Exception e) {
				e.printStackTrace();

			}
		}
}

