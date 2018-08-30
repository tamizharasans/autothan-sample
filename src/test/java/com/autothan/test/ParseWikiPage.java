package com.autothan.test;

import com.autothan.base.MovieObj;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by dsastry on 29/08/18.
 */
public class ParseWikiPage extends TestHelper {


    /**
     * Test to assert the Director name for a movie by comparing the values
     * in WIKI & IMDB pages.
     *
     * @param wikiURL
     * @throws Exception
     */

    public void testParsePageContent(String movieName, String movieId, String wikiURL, List<MovieObj> movieObjList) throws Exception {

        //Build the Movie Object -- Required for reporting
        MovieObj movieObj = new MovieObj();
        movieObj.setMovieId(movieId);
        movieObj.setMovieName(movieName);
        movieObj.setWikiUrl(wikiURL);
        String imdbURL = null;
        String wikiMovieDirector = null;

        //Create the builder for the WIKI Page obtained from the browser
        if (wikiURL.equalsIgnoreCase("Wiki Links NOT found for this movie")) {
            movieObj.setErrorMessage("Wiki Link NOT found for this movie");
            movieObjList.add(movieObj);
            return;
        } else {
            Invocation.Builder wikiPageBuilder = createBuilder(wikiURL);
            wikiPageBuilder.header("Accept", MediaType.APPLICATION_JSON_TYPE);
            Response response = wikiPageBuilder.get();

            //Validate the response
            assertEquals(response.getStatus(), 200);

            Document doc = Jsoup.parse(response.readEntity(String.class), "UTF-8");
            Elements wikiElements = doc.select("a");

            //Find the IMDB URL & Director Name from the wikipedia page
            for (Element element : wikiElements) {
                //System.out.println(element.absUrl("href"));
                if (element.absUrl("href").contains("imdb")) {
                    imdbURL = element.absUrl("href");
                    movieObj.setImdbUrl(imdbURL);
                }
            }

            //Get the director name from the Wiki page using XPath
            String result = Xsoup.compile("//*[@id=\"mw-content-text\"]/div/table[1]/tbody/tr[3]/td/").evaluate(doc).get();
            wikiMovieDirector = StringUtils.substringBetween(result, ">", "</a>").replace("<br>", " , ");
            movieObj.setWikiDirName(wikiMovieDirector);

            /*System.out.println("IMDB URL is::" + imdbURL);
            System.out.println("Director :: " + wikiMovieDirector) ;*/


            //Request to IMDB Page
            imdbURL = StringUtils.replace(imdbURL, "www.", "m.");
            Invocation.Builder imdbBuilder = createBuilder(imdbURL);
            imdbBuilder.header("Accept", MediaType.APPLICATION_JSON_TYPE);
            Response imdbResponse = imdbBuilder.get();
            if (imdbResponse.getStatus() != 200) {
                //Check for the Negative Scenario
                assertTrue(imdbResponse.getStatusInfo().getReasonPhrase().equals("Not Found"), "IMDB Page is not found");
                assertEquals(imdbResponse.getStatus(), 404);
                movieObj.setErrorMessage("Movie Name is not valid !!");
                return;
            } else {
                assertEquals(imdbResponse.getStatus(), 200);
            }

            //Extract the Director Name from the IMDB page
            Document imdbDoc = Jsoup.parse(imdbResponse.readEntity(String.class), "UTF-8");
            String result2 = Xsoup.compile("/*//*[@id=\"cast-and-crew\"]/a[2]/div/span").evaluate(imdbDoc).get();
            String imdbMovieDirector = StringUtils.substringBetween(result2, ">", "</").trim();

            //Compare the values obtained from the WIKI page and IMDB page
            movieObj.setImdbDirName(imdbMovieDirector);
            System.out.println("IMDB: " + imdbMovieDirector + " WIKI:" + wikiMovieDirector);

            if (!StringUtils.equalsIgnoreCase(movieObj.getWikiDirName().trim(), movieObj.getImdbDirName().trim())) {
                movieObj.setErrorMessage("IMDB Director Name did not Match with WIKI page Director Name");
            }
            //Build the movieObj for reporting
            movieObjList.add(movieObj);

        }

    }

}

