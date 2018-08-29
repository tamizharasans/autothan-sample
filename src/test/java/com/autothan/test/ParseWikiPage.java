package com.autothan.test;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;

/**
 * Created by dsastry on 29/08/18.
 */
public class ParseWikiPage extends TestHelper{

    @Test()
    public void testParsePageContent() throws Exception
    {
        String imdbURL = null;
        String wikiDirectorName = null;
        Invocation.Builder wikiPageBuilder = createBuilder("https://en.wikipedia.org/wiki/The_Godfather");
        wikiPageBuilder.header("Accept", MediaType.APPLICATION_JSON_TYPE);
        Response response = wikiPageBuilder.get();
        assertEquals(response.getStatus(),200);

        Document doc = Jsoup.parse(response.readEntity(String.class), "UTF-8");
        Elements wikiElements = doc.select("a");

        //Find the IMDB URL
        for(Element element : wikiElements){
            //System.out.println(element.absUrl("href"));
            if(element.absUrl("href").contains("imdb")) {
                imdbURL = element.absUrl("href");
                break;
            }
        }

        //Find the Director name
        for(Element directors : wikiElements)
        {
            if(String.valueOf(directors.getElementsContainingText("Films directed by")).length() >0){
                wikiDirectorName =  StringUtils.substringAfter(String.valueOf(directors.getElementsContainingText("Films directed by")),">Films directed by").replace("</a>","");
                break;
            }
        }

        System.out.println("IMDB URL is::" + imdbURL);
        System.out.println("Director :: " + wikiDirectorName) ;

        //Request to IMDB Page
        Invocation.Builder imdbBuilder = createBuilder(imdbURL);
        imdbBuilder.header("Accept", MediaType.APPLICATION_JSON_TYPE);
        Response imdbResponse = wikiPageBuilder.get();
        assertEquals(imdbResponse.getStatus(),200);


        Document imdbDoc = Jsoup.parse(imdbResponse.readEntity(String.class), "UTF-8");
        Elements imdbElements = imdbDoc.select("a");

        //Compare the Director names obatained from WIKI and IMDB
        for(Element element : imdbElements){
            if(String.valueOf(element.getElementsByAttributeValue("title",wikiDirectorName)).length()>0) {
                String imdbDirectorName=element.getElementsByAttributeValue("title",wikiDirectorName).get(0).childNode(0).toString().trim();
                assertEquals(imdbDirectorName,wikiDirectorName.trim(),"Director Assertion Passed");
                break;
            }

        }

    }


}
