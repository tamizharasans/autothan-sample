package com.autothan.test;

import com.autothan.base.MovieObj;
import com.autothan.base.RunTCInfo;
import com.autothan.base.TestReportUtil;
import com.autothan.base.TransformTestReport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class GenerateReport {


	//List of objected initialized
	private List<String> widgetActionInfo = null;
	private RunTCInfo runTCInfo = null;
	private TransformTestReport transformTestReport = null;


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
				String HTML_FLPATH;
				String XML_REPORT;
				if("GUI".equals(modeOfExec)) {
					 HTML_FLPATH = HTML_FILE + "GUITestAutomationReport.html";
					 XML_REPORT = XML_FILE + "GUITestAutomationReport.xml";
				}else{
					 HTML_FLPATH = HTML_FILE + "RESTTestAutomationReport.html";
					 XML_REPORT = XML_FILE + "RESTTestAutomationReport.xml";
				}


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
					if(movieObj1.getMovieId()!=null) {
						movieNo.appendChild(document.createTextNode(String.valueOf(movieObj1.getMovieId())));
					}else{
						movieNo.appendChild(document.createTextNode(""));
					}
					topic.appendChild(movieNo);

					Element movieName = document.createElement("movieName");
					if(movieObj1.getMovieName()!=null) {
						movieName.appendChild(document.createTextNode(movieObj1.getMovieName()));
					}else{
						movieName.appendChild(document.createTextNode(""));
					}
					topic.appendChild(movieName);

					Element wikiUrl = document.createElement("wikiUrl");
					if(movieObj1.getWikiUrl()!=null) {
						wikiUrl.appendChild(document.createTextNode(movieObj1.getWikiUrl()));
					}else{
						wikiUrl.appendChild(document.createTextNode(""));
					}
					topic.appendChild(wikiUrl);

					Element wikiSnapShotUrl = document.createElement("wikiSnapShotUrl");
					if(movieObj1.getWikiSnapShotUrl()!=null) {
						wikiSnapShotUrl.appendChild(document.createTextNode(movieObj1.getWikiSnapShotUrl()));
					}else{
						wikiSnapShotUrl.appendChild(document.createTextNode(""));
					}
					topic.appendChild(wikiSnapShotUrl);

					Element wikiDirName = document.createElement("wikiDirName");
					if(movieObj1.getWikiDirName()!=null) {
						wikiDirName.appendChild(document.createTextNode(movieObj1.getWikiDirName()));
					}else{
						wikiDirName.appendChild(document.createTextNode(""));
					}
					topic.appendChild(wikiDirName);

					Element imdbUrl = document.createElement("imdbUrl");
					if(movieObj1.getImdbUrl()!=null) {
						imdbUrl.appendChild(document.createTextNode(movieObj1.getImdbUrl()));
					}else{
						imdbUrl.appendChild(document.createTextNode(""));
					}
					topic.appendChild(imdbUrl);

					Element imdbSnapShotUrl = document.createElement("imdbSnapShotUrl");
					if(movieObj1.getImdbSnapShotUrl()!=null) {
						imdbSnapShotUrl.appendChild(document.createTextNode(movieObj1.getImdbSnapShotUrl()));
					}else{
						imdbSnapShotUrl.appendChild(document.createTextNode(""));
					}
					topic.appendChild(imdbSnapShotUrl);

					Element imdbDirName = document.createElement("imdbDirName");
					if(movieObj1.getImdbDirName()!=null) {
						imdbDirName.appendChild(document.createTextNode(movieObj1.getImdbDirName()));
					}else{
						imdbDirName.appendChild(document.createTextNode(""));
					}
					topic.appendChild(imdbDirName);

					Element errorStatus = document.createElement("errorStatus");
					if(movieObj1.getErrorMessage()!=null) {
						errorStatus.appendChild(document.createTextNode(movieObj1.getErrorMessage()));
					}else{
						errorStatus.appendChild(document.createTextNode(""));
					}
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

