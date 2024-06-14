
import java.io.*;
import java.util.*;

public class TextToGraph {    public static void main(String[] args) {
    String filePath;
    filePath = "./examples";

    // 读取和处理文本文件
    List<String> words = processTextFile(filePath);

    // 构建有向图
    Map<String, Map<String, Integer>> graph = buildGraph(words);
    // 打印有向图
    printGraph(graph);
    System.out.println("有向图构建完毕");
//    showDirectedGraph(generateDot(graph),"DotGraph");

    Scanner scanner = new Scanner(System.in);
    String br;
    do {
        System.out.print("功能1：查询桥接词\n功能2：根据bridge word生成新文本\n功能3：计算最短路径\n功能4：随机游走\n你的输入:");
        switch (scanner.next()) {
            case "1":
                // 查询桥接词
                System.out.println("Input first word: ");
                String word1 = scanner.next();
                System.out.println("Input second word: ");
                String word2 = scanner.next();
                System.out.println(queryBridgeWords(graph, word1, word2));
                break;
            case "2":
                // 生成新文本
                scanner.nextLine(); // 清除换行符
                System.out.println("Enter a new line of text: ");
                String newText = scanner.nextLine();
                String generatedText = generateNewText(graph, newText);
                System.out.println("Generated text: " + generatedText);
                break;
            case "3":
                // 计算最短路径
                scanner.nextLine(); // 清除换行符
                System.out.println("Enter the start word for shortest path: ");
                String startWord = scanner.nextLine();
                System.out.println("Enter the end word for shortest path (or leave empty to find paths to all words): ");
                String endWord = scanner.nextLine();
                String shortestPath = calcShortestPath(graph, startWord, endWord);
                System.out.println(shortestPath);
                break;
            case "4":
                // 随机游走
                System.out.println("Starting random walk...");
                String randomWalkResult = randomWalk(graph);
                System.out.println(randomWalkResult);
                break;
            default:
                System.out.println("无效输入，请输入1-4之间的数字。");
                break;
        }
        System.out.print("是否继续(y/n):");
        br = scanner.next();
        if (br.equals("n")) {
            break;
        }
    } while (true);
    scanner.close();

//        String word1 = "to";
//        String word2 = "strange";
//        System.out.println(queryBridgeWords(graph,word1,word2));

//        // 生成新文本
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter a new line of text: ");
//        String newText = scanner.nextLine();
//        scanner.close();
//        String generatedText = generateNewText(graph, newText);
//        System.out.println("Generated text: " + generatedText);

//     // 计算最短路径
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the start word for shortest path: ");
//        String startWord = scanner.nextLine();
//        System.out.println("Enter the end word for shortest path (or leave empty to find paths to all words): ");
//        String endWord = scanner.nextLine();
//        scanner.close();
//        String shortestPath = calcShortestPath(graph, startWord, endWord);
//        System.out.println(shortestPath);

//     // 随机游走
//        System.out.println("Starting random walk...");
//        String randomWalkResult = randomWalk(graph);
//        System.out.println(randomWalkResult);
}

    public static List<String> processTextFile(String filePath) {
        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(processLine(line)).append(" ");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return Collections.emptyList();
        }

        // 分词
        String processedText = text.toString().replaceAll("\\s+", " ").trim();
        return Arrays.asList(processedText.split(" "));
    }

    public static String processLine(String line) {
        StringBuilder processedLine = new StringBuilder();

        for (char ch : line.toCharArray()) {
            if (Character.isLetter(ch)) {
                processedLine.append(Character.toLowerCase(ch));
            } else {
                processedLine.append(' ');
            }
        }

        return processedLine.toString();
    }

    public static Map<String, Map<String, Integer>> buildGraph(List<String> words) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();

        for (int i = 0; i < words.size() - 1; i++) {
            String wordA = words.get(i);
            String wordB = words.get(i + 1);

            graph.putIfAbsent(wordA, new HashMap<>());
            Map<String, Integer> neighbors = graph.get(wordA);
            neighbors.put(wordB, neighbors.getOrDefault(wordB, 0) + 1);
        }
        graph.putIfAbsent(words.get(words.size() - 1), new HashMap<>());
        return graph;
    }

    public static void printGraph(Map<String, Map<String, Integer>> graph) {
        for (String wordA : graph.keySet()) {
            Map<String, Integer> neighbors = graph.get(wordA);
            for (String wordB : neighbors.keySet()) {
                System.out.println(wordA + " -> "+wordB + " [weight=" + neighbors.get(wordB) + "]");
            }
        }
    }

    public static String queryBridgeWords(Map<String, Map<String, Integer>> graph, String word1, String word2) {
        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            return "No " + (graph.containsKey(word1) ? "word2" : "word1") + " in the graph!";
        }

        Set<String> bridgeWords = new HashSet<>();
        Map<String, Integer> neighborsOfWord1 = graph.get(word1);

        for (String potentialBridge : neighborsOfWord1.keySet()) {
            Map<String, Integer> neighborsOfBridge = graph.get(potentialBridge);
            if (neighborsOfBridge != null && neighborsOfBridge.containsKey(word2)) {
                bridgeWords.add(potentialBridge);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
        }
    }

    public static String generateNewText(Map<String, Map<String, Integer>> graph, String newText) {
        List<String> newWords = Arrays.asList(processLine(newText).split("\\s+"));
        StringBuilder generatedText = new StringBuilder();

        Random rand = new Random();
        for (int i = 0; i < newWords.size() - 1; i++) {
            String word1 = newWords.get(i);
            String word2 = newWords.get(i + 1);
            generatedText.append(word1).append(" ");

            Set<String> bridgeWords = new HashSet<>();
            if (graph.containsKey(word1)) {
                Map<String, Integer> neighborsOfWord1 = graph.get(word1);
                for (String potentialBridge : neighborsOfWord1.keySet()) {
                    Map<String, Integer> neighborsOfBridge = graph.get(potentialBridge);
                    if (neighborsOfBridge != null && neighborsOfBridge.containsKey(word2)) {
                        bridgeWords.add(potentialBridge);
                    }
                }
            }

            if (!bridgeWords.isEmpty()) {
                List<String> bridgeWordList = new ArrayList<>(bridgeWords);
                String bridgeWord = bridgeWordList.get(rand.nextInt(bridgeWordList.size()));
                generatedText.append(bridgeWord).append(" ");
            }
        }
        generatedText.append(newWords.get(newWords.size() - 1));

        return generatedText.toString();
    }

    public static String calcShortestPath(Map<String, Map<String, Integer>> graph, String startWord, String endWord) {
        if (!graph.containsKey(startWord)) {
            return "No " + startWord + " in the graph!";
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<String> visited = new HashSet<>();

        for (String word : graph.keySet()) {
            distances.put(word, Integer.MAX_VALUE);
        }
        distances.put(startWord, 0);
        queue.add(startWord);

        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            if (visited.contains(currentWord)) {
                continue;
            }
            visited.add(currentWord);

            Map<String, Integer> neighbors = graph.get(currentWord);
            if (neighbors == null) {
                continue;  // 如果当前节点没有邻居，则跳过
            }
            int currentDistance = distances.get(currentWord);
            for (String neighbor : neighbors.keySet()) {
                int newDist = currentDistance + neighbors.get(neighbor);
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, currentWord);
                    queue.add(neighbor);
                }
            }
        }

        if (endWord.isEmpty()) {
            // 输出从startWord到所有其他单词的最短路径
            StringBuilder result = new StringBuilder();
            for (String word : graph.keySet()) {
                if (!word.equals(startWord)) {
                    result.append(getPath(startWord, word, predecessors, distances)).append("\n");
                }
            }
            return result.toString();
        } else {
            // 输出从startWord到endWord的最短路径
            if (!distances.containsKey(endWord) || distances.get(endWord) == Integer.MAX_VALUE) {
                return "No path from " + startWord + " to " + endWord + "!";
            }
            return getPath(startWord, endWord, predecessors, distances);
        }
    }

    private static String getPath(String startWord, String endWord, Map<String, String> predecessors, Map<String, Integer> distances) {
        LinkedList<String> path = new LinkedList<>();
        String step = endWord;

        if (predecessors.get(step) == null) {
            return "No path from " + startWord + " to " + endWord + "!";
        }

        while (step != null) {
            path.addFirst(step);
            step = predecessors.get(step);
        }

        return "Shortest path from " + startWord + " to " + endWord + ": " + String.join(" -> ", path) + " [Total weight: " + distances.get(endWord) + "]";
    }

    public static String randomWalk(Map<String, Map<String, Integer>> graph) {
        List<String> nodes = new ArrayList<>(graph.keySet());
        Random random = new Random();
        String currentNode = nodes.get(random.nextInt(nodes.size()));
        Set<String> visitedEdges = new HashSet<>();
        StringBuilder walkPath = new StringBuilder(currentNode);

        while (true) {
            Map<String, Integer> neighbors = graph.get(currentNode);
            if (neighbors == null || neighbors.isEmpty()) {
                break;  // 没有出边
            }

            List<String> neighborList = new ArrayList<>(neighbors.keySet());
            String nextNode = neighborList.get(random.nextInt(neighborList.size()));
            String edge = currentNode + "->" + nextNode;

            if (visitedEdges.contains(edge)) {
                break;  // 出现重复边
            }

            visitedEdges.add(edge);
            walkPath.append(" -> ").append(nextNode);
            currentNode = nextNode;
        }

        // 将遍历的节点输出为文本，并写入文件
        String filePath = "random_walk_output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(walkPath.toString());
            System.out.println("Random walk path saved to: " + new File(filePath).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        return "Random walk path: " + walkPath.toString();
    }
    public static String generateDot(Map<String, Map<String, Integer>> graph) {
        StringBuilder dotBuilder = new StringBuilder("");
        for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            String fromNode = entry.getKey();
            Map<String, Integer> edges = entry.getValue();
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String toNode = edge.getKey();
                Integer weight = edge.getValue();
                dotBuilder.append("    ").append(fromNode).append(" -> ").append(toNode).append(" [label=\"").append(weight).append("\"];");
            }
        }
        return dotBuilder.toString();
    }

    public static void showDirectedGraph(String dotFormat,String fileName)
    {
        GraphViz gv=new GraphViz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File(fileName+"."+ type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }

}
