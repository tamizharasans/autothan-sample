package com.autothan.test;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.testng.Assert.assertEquals;

/**
 * Created by dsastry on 29/08/18.
 */
public class ParseWikiPage extends TestHelper{

    @DataProvider
    public static Object[][] wikiLinks() {
        Object[][] result = new Object[][]{
                {1,"https://en.wikipedia.org/wiki/The_invalidmovie"},
                {2,"https://en.wikipedia.org/wiki/Goodfellas"},
                {3,"https://en.wikipedia.org/wiki/The_Shawshank_Redemption"},
                {4,"https://en.wikipedia.org/wiki/Seven_(1995_film)"},
                {5,"https://en.wikipedia.org/wiki/Inception"},
                {6,"https://en.wikipedia.org/wiki/The_Godfather"},


        };
        return result;
    }

    @Test(dataProvider = "wikiLinks",singleThreaded = false,threadPoolSize = 5)
    public void testParsePageContent(Integer index,String wikiURL) throws Exception
    {
        String imdbURL = null;
        String movieDirector = null;
        Invocation.Builder wikiPageBuilder = createBuilder(wikiURL);
        wikiPageBuilder.header("Accept", MediaType.APPLICATION_JSON_TYPE);
        Response response = wikiPageBuilder.get();
        if(index.equals(1)){
            assertEquals(response.getStatus(),404);
            return;
        }else
        {
            assertEquals(response.getStatus(),200);
        }
        Document doc = Jsoup.parse(response.readEntity(String.class), "UTF-8");
        Elements wikiElements = doc.select("a");

        //Find the IMDB URL & Director Name from the wikipedia page
        for(Element element : wikiElements){
            //System.out.println(element.absUrl("href"));
            if(element.absUrl("href").contains("imdb")) {
                imdbURL = element.absUrl("href");
            }

            if(String.valueOf(element.getElementsContainingText("Films directed by")).length() >0){
                movieDirector =  StringUtils.substringAfter(String.valueOf(element.getElementsContainingText("Films directed by")),">Films directed by").replace("</a>","");
            }
        }

        System.out.println("IMDB URL is::" + imdbURL);
        System.out.println("Director :: " + movieDirector) ;
        //System.out.println("Output :: " + response.readEntity(String.class));

        //Request to IMDB Page
        Invocation.Builder imdbBuilder = createBuilder(imdbURL);
        imdbBuilder.header("Accept", MediaType.APPLICATION_JSON_TYPE);
        Response imdbResponse = wikiPageBuilder.get();
        assertEquals(imdbResponse.getStatus(),200);


        Document imdbDoc = Jsoup.parse(imdbResponse.readEntity(String.class), "UTF-8");
        Elements imdbElements = imdbDoc.select("a");

        //Compare the Director names obatained from WIKI and IMDB
        for(Element element : imdbElements){
            if(String.valueOf(element.getElementsByAttributeValue("title",movieDirector)).length()>0) {
                assertEquals(element.getElementsByAttributeValue("title",movieDirector).get(0).childNode(0).toString().trim(),movieDirector.trim(),"Director Assertion Passed");
                break;
            }

        }

    }


}

