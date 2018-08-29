package com.autothan.base;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.FileOutputStream;


public class TestReportUtil {
	
  String XSLFLPATH ;	
  String XMLFLPATH ;	
  String HTMLFLPATH ;	
	
		    public TestReportUtil(String XSLFL, String XMLFL, String HTMLRFL){
				this.XSLFLPATH = XSLFL;
				this.XMLFLPATH = XMLFL;
				this.HTMLFLPATH = HTMLRFL;
			}
		    
		    
public void generateTestReport() {
  try {

    TransformerFactory tFactory = TransformerFactory.newInstance();

    Transformer transformer =
      tFactory.newTransformer
         (new javax.xml.transform.stream.StreamSource
            (XSLFLPATH));

    transformer.transform
      (new javax.xml.transform.stream.StreamSource
            (XMLFLPATH),
       new javax.xml.transform.stream.StreamResult
            ( new FileOutputStream(HTMLFLPATH)));
    }
  catch (Exception e) {
    e.printStackTrace( );
    }
  }
}