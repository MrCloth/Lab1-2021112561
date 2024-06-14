import org.junit.jupiter.api.Test;
import org.junit.BeforeClass;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextToGraphTest {
//    @BeforeClass
//    public static void BeforeAll() {
    TextToGraph textToGraph = new TextToGraph();
    // 读取和处理文本文件
    List<String> words = textToGraph.processTextFile("./examples");
    // 构建有向图
    Map<String, Map<String, Integer>> graph = textToGraph.buildGraph(words);
//    }
    @Test
    void calcShortestPath1() {
        String startWord = "";
        String test = textToGraph.calcShortestPath(graph,startWord, "new");

        assertEquals("No " + startWord + " in the graph!",test);
    }
    @Test
    void calcShortestPath2() {
        String startWord = "to";
        String endWord = "and";
        String test = textToGraph.calcShortestPath(graph,startWord, endWord);
        assertEquals("Shortest path from to to and: to -> explore -> strange -> new -> life -> and [Total weight: 5]",test);
    }
    @Test
    void calcShortestPath3() {
        String startWord = "to";
        String endWord = "";
        String test = textToGraph.calcShortestPath(graph,startWord, endWord);

        assertEquals("Shortest path from to to new: to -> explore -> strange -> new [Total weight: 3]\nShortest path from to to worlds: to -> explore -> strange -> new -> worlds [Total weight: 4]\nShortest path from to to explore: to -> explore [Total weight: 1]\nShortest path from to to and: to -> explore -> strange -> new -> life -> and [Total weight: 5]\nShortest path from to to civilizations: to -> explore -> strange -> new -> civilizations [Total weight: 4]\nShortest path from to to seek: to -> seek [Total weight: 1]\nShortest path from to to strange: to -> explore -> strange [Total weight: 2]\nShortest path from to to life: to -> explore -> strange -> new -> life [Total weight: 4]\nShortest path from to to out: to -> seek -> out [Total weight: 2]\n",test);
    }
    @Test
    void calcShortestPath4() {
        String startWord = "civilizations";
        String endWord = "new";
        String test = textToGraph.calcShortestPath(graph,startWord, endWord);
        assertEquals("No path from " + startWord + " to " + endWord + "!",test);
    }
}
