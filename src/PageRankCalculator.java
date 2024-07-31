import java.io.*;
import java.util.*;

public class PageRankCalculator {

    private static final String inputCsvFile = "inverted_index.csv"; // Static input CSV file path

    public static List<PageRank> getTopUrlsForKeyword(String keywordToRank) {
        Map<String, RedBlackTree> pageKeywordFrequency = readCsv(inputCsvFile);
        return calculatePageRanks(pageKeywordFrequency, keywordToRank.toLowerCase());
    }

    public static Map<String, RedBlackTree> readCsv(String inputCsvFile) {
        Map<String, RedBlackTree> pageKeywordFrequency = new HashMap<>();
        String linePR;
        String csvSplitBy = ",";

        try (BufferedReader brPgRank = new BufferedReader(new FileReader(inputCsvFile))) {
            brPgRank.readLine();

            while ((linePR = brPgRank.readLine()) != null) {
                String[] dataPR = linePR.split(csvSplitBy);
                if (dataPR.length != 3) {
                    continue; // Skip invalid lines
                }
                String keyword = dataPR[0].trim().toLowerCase();
                String url = dataPR[1].trim().toLowerCase();
                int occurrences;
                try {
                    occurrences = Integer.parseInt(dataPR[2].trim());
                } catch (NumberFormatException e) {
                    continue; // Skip lines with invalid occurrences
                }

                pageKeywordFrequency.putIfAbsent(keyword, new RedBlackTree());
                pageKeywordFrequency.get(keyword).insert(url, occurrences);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageKeywordFrequency;
    }

    public static List<PageRank> calculatePageRanks(Map<String, RedBlackTree> pageKeywordFrequency, String keywordToRank) {
        List<PageRank> pageRanks = new ArrayList<>();
        MaxHeap maxHeap = new MaxHeap();

        if (pageKeywordFrequency.containsKey(keywordToRank)) {
            RedBlackTree tree = pageKeywordFrequency.get(keywordToRank);
            List<PageRank> ranks = tree.getAllPageRanks();
            maxHeap.addAll(ranks);
        }

        while (!maxHeap.isEmpty()) {
            pageRanks.add(maxHeap.poll());
        }

        return pageRanks;
    }

    public static void writeRankedCsv(String outputCsvFile, List<PageRank> rankedUrls) {
        try (BufferedWriter bwPR = new BufferedWriter(new FileWriter(outputCsvFile))) {
            // Write header
            bwPR.write("URL,Occurrences,Rank");
            bwPR.newLine();

            for (int ipg = 0; ipg < rankedUrls.size(); ipg++) {
                PageRank pageRank = rankedUrls.get(ipg);
                bwPR.write(pageRank.url + "," + pageRank.frequency + "," + (ipg + 1));
                bwPR.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTopUrls(List<PageRank> rankedUrls) {
        if (rankedUrls.isEmpty()) {
            System.out.println("No pages found for the given keyword.");
            return;
        }

        for (int i = 0; i < Math.min(10, rankedUrls.size()); i++) {  // Display top 10 pages
            PageRank pageRank = rankedUrls.get(i);
            System.out.println("URL: " + pageRank.url + ", Occurrences: " + pageRank.frequency + ", Rank: " + (i + 1));
        }
    }

    public static String getTopUrl(String keyword) {
        List<PageRank> pageRanks = getTopUrlsForKeyword(keyword.toLowerCase());
        return pageRanks.isEmpty() ? "No URL found" : pageRanks.get(0).url;
    }

    static class RedBlackTree {
        private static final boolean RED = true;
        private static final boolean BLACK = false;

        private class Node {
            String url;
            int frequency;
            Node left, right, prnt;
            boolean color;

            Node(String url, int frequency) {
                this.url = url;
                this.frequency = frequency;
                this.color = RED;
            }
        }

        private Node root;

        private void rotateLeft(Node node) {
            Node rightChild = node.right;
            node.right = rightChild.left;
            if (rightChild.left != null) rightChild.left.prnt = node;
            rightChild.prnt = node.prnt;
            if (node.prnt == null) root = rightChild;
            else if (node == node.prnt.left) node.prnt.left = rightChild;
            else node.prnt.right = rightChild;
            rightChild.left = node;
            node.prnt = rightChild;
        }

        private void rotateRight(Node node) {
            Node leftChild = node.left;
            node.left = leftChild.right;
            if (leftChild.right != null) leftChild.right.prnt = node;
            leftChild.prnt = node.prnt;
            if (node.prnt == null) root = leftChild;
            else if (node == node.prnt.right) node.prnt.right = leftChild;
            else node.prnt.left = leftChild;
            leftChild.right = node;
            node.prnt = leftChild;
        }

        private void fixInsert(Node node) {
            Node prnt, grandParent;

            while (node != root && node.color == RED && node.prnt.color == RED) {
                prnt = node.prnt;
                grandParent = prnt.prnt;

                if (prnt == grandParent.left) {
                    Node uncle = grandParent.right;
                    if (uncle != null && uncle.color == RED) {
                        grandParent.color = RED;
                        prnt.color = BLACK;
                        uncle.color = BLACK;
                        node = grandParent;
                    } else {
                        if (node == prnt.right) {
                            rotateLeft(prnt);
                            node = prnt;
                            prnt = node.prnt;
                        }
                        rotateRight(grandParent);
                        boolean temp = prnt.color;
                        prnt.color = grandParent.color;
                        grandParent.color = temp;
                        node = prnt;
                    }
                } else {
                    Node uncle = grandParent.left;
                    if (uncle != null && uncle.color == RED) {
                        grandParent.color = RED;
                        prnt.color = BLACK;
                        uncle.color = BLACK;
                        node = grandParent;
                    } else {
                        if (node == prnt.left) {
                            rotateRight(prnt);
                            node = prnt;
                            prnt = node.prnt;
                        }
                        rotateLeft(grandParent);
                        boolean temp = prnt.color;
                        prnt.color = grandParent.color;
                        grandParent.color = temp;
                        node = prnt;
                    }
                }
            }
            root.color = BLACK;
        }

        public void insert(String url, int frequency) {
            Node newNode = new Node(url, frequency);
            if (root == null) {
                root = newNode;
                root.color = BLACK;
                return;
            }

            Node current = root, prnt = null;
            while (current != null) {
                prnt = current;
                if (url.compareTo(current.url) < 0)
                    current = current.left;
                else if (url.compareTo(current.url) > 0)
                    current = current.right;
                else {
                    current.frequency += frequency;
                    return;
                }
            }

            newNode.prnt = prnt;
            if (url.compareTo(prnt.url) < 0)
                prnt.left = newNode;
            else
                prnt.right = newNode;

            fixInsert(newNode);
        }

        public Integer getFrequency(String url) {
            Node current = root;
            while (current != null) {
                if (url.compareTo(current.url) < 0)
                    current = current.left;
                else if (url.compareTo(current.url) > 0)
                    current = current.right;
                else
                    return current.frequency;
            }
            return null;
        }

        public List<PageRank> getAllPageRanks() {
            List<PageRank> result = new ArrayList<>();
            inorderTraversal(root, result);
            return result;
        }

        private void inorderTraversal(Node node, List<PageRank> result) {
            if (node == null) return;
            inorderTraversal(node.left, result);
            result.add(new PageRank(node.url, node.frequency));
            inorderTraversal(node.right, result);
        }
    }

    static class MaxHeap {
        private PriorityQueue<PageRank> heap;

        public MaxHeap() {
            heap = new PriorityQueue<>(Collections.reverseOrder());
        }

        public void add(PageRank pageRank) {
            heap.add(pageRank);
        }

        public void addAll(Collection<PageRank> pageRanks) {
            heap.addAll(pageRanks);
        }

        public PageRank poll() {
            return heap.poll();
        }

        public boolean isEmpty() {
            return heap.isEmpty();
        }
    }

    static class PageRank implements Comparable<PageRank> {
        String url;
        int frequency;

        PageRank(String url, int frequency) {
            this.url = url;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(PageRank other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }
}
