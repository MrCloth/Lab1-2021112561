import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BlackBoxTest {

    @BeforeClass
    public static void set_graph(){
        String filePath = "./examples";
        List<String> words = TextToGraph.processTextFile(filePath);
        graph = TextToGraph.buildGraph(words);
    }
    private static  Map<String, Map<String, Integer>> graph ;

    @Test
    public void queryBridgeWords1() {
        String word1 = "new";
        String word2 = "and";
        String test = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("The bridge words from \"new\" to \"and\" is: \"life\".",test);
    }
    @Test
    public void queryBridgeWords2() {
        String word1 = "new";
        String word2 = "life";
        String words = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("No bridge words from \"new\" to \"life\"!",words);
    }
    @Test
    public void queryBridgeWords3() {
        String word1 = "neww";
        String word2 = "andd";
        String words = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("No \"neww\" and \"andd\" in the graph!",words);
    }
    @Test
    public void queryBridgeWords4() {
        String word1 = "new";
        String word2 = "andd";
        String words = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("No \"andd\" in the graph!",words);
    }
    @Test
    public void queryBridgeWords5() {
        String word1 = "neww";
        String word2 = "and";
        String words = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("No \"neww\" in the graph!",words);
    }
    @Test
    public void queryBridgeWords6() {
        String word1 = "1@";
        String word2 = "2$";
        String words = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("No \"1@\" and \"2$\" in the graph!",words);
    }
    @Test
    public void queryBridgeWords7() {
        String word1 = "New";
        String word2 = "And";
        String words = TextToGraph.queryBridgeWords(graph,word1,word2);
        assertEquals("No \"New\" and \"And\" in the graph!",words);
    }
}