package project;

import project.logic.ReadingRecommendationService;
import project.domain.ReadingRecommendation;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.SourceDataLine;

import project.domain.User;
import project.domain.ReadingRecommendationInterface;

public class ReadingRecommendationServiceTest {
    
    ReadingRecommendationService service;
    ReadingRecommendationInterface recommendation;
    ArrayList<ReadingRecommendationInterface> recommendations;
    HashMap<String, String> blogInfo;
    HashMap<String, String> bookInfo;
    User user;

    @Before
    public void setUp() throws Exception {
        String headline = "Basic";
        String type = "blog";

        recommendation = new ReadingRecommendation(headline, type);
        
        user = new User("pekka", "salasana1");

        //Basic info for blog
        blogInfo = new HashMap<>();
        blogInfo.put("headline", "Blog headline");
        blogInfo.put("type", "blog");
        blogInfo.put("url", "Blog's url");

        //Basic info for book
        bookInfo = new HashMap<>();
        bookInfo.put("headline", "Book headline");
        bookInfo.put("type", "book");
        bookInfo.put("writer", "Kirjoittaja");

        FakeReadingRecommendationDAO fakeDb = new FakeReadingRecommendationDAO();
        recommendations = fakeDb.loadAll();
        
        service = new ReadingRecommendationService(user, fakeDb);
    }

    // CreateRecommendations tests
    @Test
    public void createRecommendationWorksForBlogWithoutWriter() {
        service.createRecommendation(blogInfo);
        ReadingRecommendationInterface addedBlog = recommendations.get(0);
        BlogRecommendation blog = new BlogRecommendation("Blog headline", "blog", "Blog's url");  
        assertEquals(blog.getPrint(), addedBlog.getPrint());
    }

    @Test
    public void createRecommendationWorksForBlogWithWriter() {
        blogInfo.put("writer", "Kirjailija");
        service.createRecommendation(blogInfo);
        ReadingRecommendationInterface addedBlog = recommendations.get(0);
        BlogRecommendation blog = new BlogRecommendation("Blog headline", "blog", "Blog's url");
        blog.setWriter("Kirjailija");  
        assertEquals(blog.getPrint(), addedBlog.getPrint());
    }

    @Test
    public void createRecommendationWorksForBookWithoutIsbn() {
        service.createRecommendation(bookInfo);
        ReadingRecommendationInterface addedBook = recommendations.get(0);
        BookRecommendation book = new BookRecommendation("Book headline", "book", "Kirjoittaja");

        assertEquals(book.getPrint(), addedBook.getPrint());
    }

    @Test
    public void createRecommendationWorksForBookWithIsbn() {
        bookInfo.put("ISBN", "ISBN");
        service.createRecommendation(bookInfo);
        ReadingRecommendationInterface addedBook = recommendations.get(0);
        BookRecommendation book = new BookRecommendation("Book headline", "book", "Kirjoittaja");
        book.setISBN("ISBN");

        assertEquals(book.getPrint(), addedBook.getPrint());
    }

//    //FindIndex tests
//    @Test
//    public void findIndexReturnsIndexIfRecommendationIsThere() {
//        recommendations.add(new ReadingRecommendation("Bonus", "book"));
//        recommendations.add(recommendation);
//        int index = service.findIndex("Basic");
//
//        assertEquals(1, index);
//    }
//
//    @Test
//    public void findIndexReturnsNegativeOneIfRecommendationNotFound() {
//        recommendations.add(new ReadingRecommendation("Bonus", "book"));
//        recommendations.add(recommendation);
//        int index = service.findIndex("Not there");
//
//        assertEquals(-1, index);
//    }

    //RemoveRecommendations tests
    @Test
    public void removeRecommendationRemovesRecommendationFromDatabase() {
        BookRecommendation book = new BookRecommendation("Bonus", "book", "Writer");
        int randomId = 4;
        book.setId(randomId);
        recommendations.add(book);
        recommendations.add(recommendation);
        service.removeRecommendation(randomId);
        assertTrue(recommendations.contains(recommendation));
        assertFalse(recommendations.contains(book));
    }

    @Test
    public void removeRecommendationDoesNothingIfNotFound() {
        ReadingRecommendation r = new ReadingRecommendation("Bonus", "book");
        r.setId(2);
        recommendations.add(r);
        recommendations.add(recommendation);
        service.removeRecommendation(1);

        assertEquals(2, recommendations.size());
    }

    @Test
    public void removeRecommendationRemovesCorrectRecommendation() {
        BookRecommendation book = new BookRecommendation("Bonus", "book", "Writer");
        book.setId(1);
        recommendations.add(book);
        recommendations.add(recommendation);
        service.removeRecommendation(1);

        String headline = recommendations.get(0).getHeadline();

        assertEquals("Basic", headline);
    }

    //FindRecommendation tests
    @Test
    public void findRecommendationReturnsCorrectRecommendation() {
        recommendations.add(recommendation);
        ReadingRecommendationInterface recom = service.findRecommendation("Basic");

        assertEquals("Basic", recom.getHeadline());
    }

    @Test
    public void findRecommendationReturnsNullIfNotFound() {
        recommendations.add(recommendation);
        ReadingRecommendationInterface recom = service.findRecommendation("Not there");

        assertNull(recom);
    }

    @Test
    public void findRecommendationsTest() {
        recommendations.add(recommendation);

        ReadingRecommendationInterface rri1 = new ReadingRecommendation("Basic", "podcast");
        ReadingRecommendationInterface rri2 = new ReadingRecommendation("Non-basic", "book");

        recommendations.add(rri1);
        recommendations.add(rri2);
        ArrayList readingrecommendations = service.findRecommendations("Basic");
        //ArrayList readingrecommendations = service.loadRecommendations();

        //System.out.println("###########################");
        for (Object o : readingrecommendations) {
            ReadingRecommendationInterface rri = (ReadingRecommendationInterface)o;
            System.out.println(rri.getHeadline());
        }
        //System.out.println("###########################");

        int results = readingrecommendations.size();
        assertEquals(results, 2);
    }

    //ShowRecommendation tests
    @Test
    public void showRecommendationPrintsCorrect() {
        recommendations.add(recommendation);
        String recomPrint = service.showRecommendation(recommendation);

        assertEquals(recommendation.getPrint(), recomPrint);
    }

    //ShowAllRecommendations tests
    @Test
    public void showAllRecommendationsReturnsCorrectIfNoRecommendations() {
        String ans = service.showAllRecommendations();
        
        assertEquals("No recommendations", ans);
    }

}
