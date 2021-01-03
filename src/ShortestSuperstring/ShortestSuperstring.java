package ShortestSuperstring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ShortestSuperstring {
    private static HashMap<String, Node> nodes = new HashMap<String, Node>();
    private static String[] mapKeys;

    public static void main(String[] args) {
        try {
            HashMap<String, Node> res = readFasta("C:\\Users\\Bram\\Java\\Blok6-Afvinkopdracht6\\SampleDataset (shortest superstring).txt");
            mapKeys = nodes.keySet().toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mapMaker(){
        Node node1, node2;
        String seq1, seq2;
        Boolean foundOverlap1, foundOverlap2;

        for (int i = 0; i < mapKeys.length; i++) {
            node1 = nodes.get(mapKeys[i]);
            seq1 = node1.getSeq();
            for (int j = i + 1; j < mapKeys.length; j++) {
                node2 = nodes.get(mapKeys[j]);
                seq2 = node2.getSeq();
                foundOverlap1 = false;
                foundOverlap2 = false;
                for (int k = seq1.length() / 2; k < seq1.length(); k++) {

                }
            }
        }
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
