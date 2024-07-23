import java.io.*;
import java.util.*;

class AVLNode {
    String key;
    int frequency;
    int height;
    AVLNode left, right;

    AVLNode(String d) {
        key = d;
        frequency = 1;
        height = 1;
    }
}

class AVLTree {
    private AVLNode root;

    private int height(AVLNode N) {
        return N == null ? 0 : N.height;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private int getBalance(AVLNode N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }

    public void insert(String key) {
        root = insertRec(root, key);
    }

    private AVLNode insertRec(AVLNode node, String key) {
        if (node == null) {
            return (new AVLNode(key));
        }

        if (key.compareTo(node.key) < 0) {
            node.left = insertRec(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = insertRec(node.right, key);
        } else {
            node.frequency++;
            return node;
        }

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rightRotate(node);
        }

        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return leftRotate(node);
        }

        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void inOrder(AVLNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.key + " " + node.frequency);
            inOrder(node.right);
        }
    }

    public AVLNode getRoot() {
        return root;
    }
}

public class SearchFrequency {
    private static AVLTree avlTree = new AVLTree();
    private static HashMap<String, Integer> frequencyMap = new HashMap<>();
    private static final String CSV_FILE_PATH = "sf_dataset.csv";

    public static void main(String[] args) {
        // Load the existing CSV data into the AVL tree
        loadCsvData(CSV_FILE_PATH);

        // Example usage
        addString("example");
        addString("test");
        addString("example");

        // Print the AVL tree in ascending order
        System.out.println("AVL Tree in Ascending Order:");
        avlTree.inOrder(avlTree.getRoot());

        // Print the AVL tree based on the highest searches to least
        System.out.println("AVL Tree by Frequency:");
        printTreeByFrequency();

        // Write updated data back to CSV
        writeCsvData(CSV_FILE_PATH);
    }

    public static void addString(String input) {
        avlTree.insert(input);
        frequencyMap.put(input, frequencyMap.getOrDefault(input, 0) + 1);
        writeCsvData(CSV_FILE_PATH);
    }

    public static void loadCsvData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String key = values[0];
                int frequency = Integer.parseInt(values[1]);
                for (int i = 0; i < frequency; i++) {
                    avlTree.insert(key);
                }
                frequencyMap.put(key, frequency);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCsvData(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTreeByFrequency() {
        List<Map.Entry<String, Integer>> nodes = new ArrayList<>(frequencyMap.entrySet());
        nodes.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        for (Map.Entry<String, Integer> entry : nodes) {
            System.out.print("|" + entry.getKey() + " " + entry.getValue() + "| ");
        }
        System.out.println();
    }
}
