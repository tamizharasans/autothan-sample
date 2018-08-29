package com.autothan.test;

import com.autothan.base.FileInfoRepo;
import com.autothan.base.RunTCInfo;
import com.autothan.base.TestReportUtil;
import com.autothan.base.TransformTestReport;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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



		@Test
		public void genReport() {


			try {

				String tagetTestClassesPath = this.getClass().getClassLoader().getResource(".").getPath();

				String XSLFLPATH = tagetTestClassesPath+"TestReports/TestReportXSL.xsl";
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


				FileInfoRepo fileInfoRepo = new FileInfoRepo();


				String environment = "http://toolsqa.com/";


				Element report = document.createElement("report");
				document.appendChild(report);

				Map<Integer, String> map = new HashMap();
				map.put(1, "Se7ven");
				map.put(2, "Se7ven");


				//Updating Root Child Element in Dynamic XML to generate Widget Report


				int j = 1;
				for (Map.Entry<Integer, String> entry : map.entrySet()) {



					transformTestReport = new TransformTestReport();
					//Updating data for Root Child Elements in Dynamic XML to generate Widget Report

					Element topic = document.createElement("topic");
					Element movieNo = document.createElement("movieNo");
					movieNo.appendChild(document.createTextNode(String.valueOf(entry.getKey())));
					topic.appendChild(movieNo);

					Element movieName = document.createElement("movieName");
					movieName.appendChild(document.createTextNode(entry.getValue()));
					topic.appendChild(movieName);
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

