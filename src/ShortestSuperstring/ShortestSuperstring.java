package ShortestSuperstring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ShortestSuperstring {
    private static HashMap<String, Node> nodes = new HashMap<>();
    private static String[] mapKeys;

    public static void main(String[] args) {
        try {
            HashMap<String, Node> res = readFasta("C:\\Users\\Bram\\Java\\Blok6-Afvinkopdracht6\\SampleDataset (shortest superstring).txt");
            mapKeys = nodes.keySet().toArray(new String[0]);
            mapMaker();
            mapReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mapReader(){
        ArrayList<Node> path = new ArrayList<>();
        HashSet<String> usedKeys = new HashSet<>();

        String startKey = getStartKey();
        if (startKey == null) {
            System.out.println("Unable to locate first seq.");
        } else {
            path.add(nodes.get(startKey));
            usedKeys.add(startKey);
        }


        System.out.println(startKey);
        while (true){
            Node last = path.get(path.size()-1);

            String bestKey = null;
            int bestOverlap = 0;
            Set<String> usableKeys = last.getRightList().keySet();
            usableKeys.removeAll(usedKeys);
            for (String key: usableKeys) {
                if (bestKey == null || last.getOverlapRight(key) > bestOverlap) {
                    bestKey = key;
                    bestOverlap = last.getOverlapRight(key);
                }
            }
            if (bestKey == null) {
                System.out.println("End reached, no matches found.");
                break;
            } else {
                path.add(nodes.get(bestKey));
                usedKeys.add(bestKey);
            }
        }
        System.out.println("");
    }

    public static String getStartKey(){
        String startKey = null;
        for (String key: mapKeys) {
            if ((nodes.get(key)).isStart()) {
                startKey = key;
                break;
            }
        }
        return startKey;
    }

    public static void mapMaker(){
        Node node1, node2;
        String seq1, seq2, name1, name2;
        boolean foundOverlap1, foundOverlap2;
        int halfway;

        for (int i = 0; i < mapKeys.length; i++) {

            name1 = mapKeys[i];
            node1 = nodes.get(name1);
            seq1 = node1.getSeq();
            halfway = seq1.length() / 2;
            for (int j = i + 1; j < mapKeys.length; j++) {
                name2 = mapKeys[j];
                node2 = nodes.get(name2);
                seq2 = node2.getSeq();
                foundOverlap1 = false;
                foundOverlap2 = false;
                for (int k = seq1.length(); k > halfway; k--) {
                    if (!foundOverlap1 && seq1.startsWith(seq2.substring(seq2.length() - k))){
                       node1.addLeft(name2, k);
                       node2.addRight(name1, k);
                       foundOverlap1 = true;
                    }
                    if (!foundOverlap2 && seq2.startsWith(seq1.substring(seq1.length() - k))){
                        node2.addLeft(name1, k);
                        node1.addRight(name2, k);
                        foundOverlap2 = true;
                    }
                    if (foundOverlap1 && foundOverlap2) {
                        break;
                    }
                }
            }
        }
        System.out.println(nodes);
    }

    public static HashMap<String, Node> readFasta(String filepath) throws IOException {
        String line;
        String header = null;
        StringBuilder seq = new StringBuilder();
//        HashMap<String, Node> nodes = new HashMap<String, Node>();

        BufferedReader inFile = new BufferedReader(new FileReader(filepath));
        // Leest regel per file.
        while ((line = inFile.readLine()) != null) {
            // Header
            if (line.startsWith(">")) {
                // Als er al een header is wordt deze met de sequentie toegevoegd aan seqMap.
                if (header != null) {
                    nodes.put(header, new Node(seq.toString()));
                    // Maakt seq leeg.
                    seq = new StringBuilder();
                }
                // Header wordt opgeslagen.
                header = line.strip().substring(1);
            // Sequentie.
            } else {
                // Voegt regel toe aan de huidige sequentie.
                seq.append(line.strip());
            }
        }
        // Voegt laatste header/sequentie toe.
        nodes.put(header, new Node(seq.toString()));
        return nodes;
    }
}
