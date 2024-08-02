import java.io.*;
import java.util.*;
// Important Packages

class AvLNo {
    String K; // Key
    int Fre; // Frequency
    int he; // Height
    AvLNo L, R; // L - Left  R - Right

    AvLNo(String d) { // AvlNo - AVL Node
        K = d;
        Fre = 1;
        he = 1;
    }
}

class AT {
    private AvLNo R; // Root

    private int varForHeight(AvLNo N) {
        return N == null ? 0 : N.he;
    }

    private int varForMax(int a, int b) {
        return (a > b) ? a : b;
    }

    private AvLNo RR(AvLNo varForY) { // RR - Rotate Right
        AvLNo varForX = varForY.L;
        AvLNo T2 = varForX.R;

        varForX.R = varForY;
        varForY.L = T2;

        varForY.he = varForMax(varForHeight(varForY.L), varForHeight(varForY.R)) + 1;
        varForX.he = varForMax(varForHeight(varForX.L), varForHeight(varForX.R)) + 1;

        return varForX;
    }

    private AvLNo lR(AvLNo x) { // lR - Left Rotate
        AvLNo y = x.R;
        AvLNo T2 = y.L;

        y.L = x;
        x.R = T2;

        x.he = varForMax(varForHeight(x.L), varForHeight(x.R)) + 1; // he - Height
        y.he = varForMax(varForHeight(y.L), varForHeight(y.R)) + 1;

        return y;
    }

    private int Gb(AvLNo N) { // GB - getBalance
        return (N == null) ? 0 : varForHeight(N.L) - varForHeight(N.R);
    }

    public void in(String key) { // in - Insert
        R = iNr(R, key);
    }

    private AvLNo iNr(AvLNo varForNode, String varForKey) { // iNr - InSRec
        if (varForNode == null) {
            return (new AvLNo(varForKey));
        }

        if (varForKey.compareTo(varForNode.K) < 0) {
            varForNode.L = iNr(varForNode.L, varForKey);
        } else if (varForKey.compareTo(varForNode.K) > 0) {
            varForNode.R = iNr(varForNode.R, varForKey);
        } else {
            varForNode.Fre++;
            return varForNode;
        }

        varForNode.he = 1 + varForMax(varForHeight(varForNode.L), varForHeight(varForNode.R));

        int varForBalance = Gb(varForNode);

        if (varForBalance > 1 && varForKey.compareTo(varForNode.L.K) < 0) {
            return RR(varForNode);
        }

        if (varForBalance < -1 && varForKey.compareTo(varForNode.R.K) > 0) {
            return lR(varForNode);
        }

        if (varForBalance > 1 && varForKey.compareTo(varForNode.L.K) > 0) {
            varForNode.L = lR(varForNode.L);
            return RR(varForNode);
        }

        if (varForBalance < -1 && varForKey.compareTo(varForNode.R.K) < 0) {
            varForNode.R = RR(varForNode.R);
            return lR(varForNode);
        }

        return varForNode;
    }

    public void Io(AvLNo varForNode) { // Io - inOrder
        if (varForNode != null) {
            Io(varForNode.L);
            System.out.println(varForNode.K + " " + varForNode.Fre);
            Io(varForNode.R);
        }
    }

    public AvLNo gR() { // gR - Get Root
        return R;
    }
}

public class SearchFrequency {
    private static AT AvT = new AT(); // AvT - AVL Tree
    private static HashMap<String, Integer> Fmap = new HashMap<>(); // Fmap - Frequency map
    private static final String CfP = "sf_dataset.csv"; // CfP - CSV File Path

    public static void main(String[] args) {
        try {
            loadCSVData();

            // Print the AVL tree in ascending order
            System.out.println("AVL Tree in Ascending Order:");
            AvT.Io(AvT.gR());

            // Print the AVL tree based on the highest searches to least
            System.out.println("AVL Tree by Frequency:");
            PrintTreeByFrequency();

            // Write updated data back to CSV
            wCd();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static void addString(String input) {
        AvT.in(input);
        Fmap.put(input, Fmap.getOrDefault(input, 0) + 1);
        try {
            wCd();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to CSV: " + e.getMessage());
        }
    }

    public static void loadCSVData() throws IOException { // loadCSVData - loadCsvData
        try (BufferedReader br = new BufferedReader(new FileReader(CfP))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String key = values[0];
                int frequency = Integer.parseInt(values[1]);
                for (int i = 0; i < frequency; i++) {
                    AvT.in(key);
                }
                Fmap.put(key, frequency);
            }
        }
    }

    public static void wCd() throws IOException { // wCd - writeCsvData
        try (BufferedWriter varForBW = new BufferedWriter(new FileWriter(CfP))) {
            for (Map.Entry<String, Integer> entry : Fmap.entrySet()) {
                varForBW.write(entry.getKey() + "," + entry.getValue());
                varForBW.newLine();
            }
        }
    }

    public static void PrintTreeByFrequency() { // PrintTreeByFrequency - printTreeByFrequency
        List<Map.Entry<String, Integer>> nodes = new ArrayList<>(Fmap.entrySet());
        nodes.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        int varForCount = 0;
        for (Map.Entry<String, Integer> entry : nodes) {
            if (varForCount < 5) {
                System.out.print("| " + entry.getKey() + " => " + entry.getValue() + " | ");
                varForCount++;
            } else {
                break;
            }
        }
        System.out.println();
    }
}
