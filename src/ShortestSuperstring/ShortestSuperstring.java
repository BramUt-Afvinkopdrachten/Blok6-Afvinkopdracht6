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
            // Leest fasta.
            readFasta("C:\\Users\\Bram\\Java\\Blok6-Afvinkopdracht6\\rosalind_long.txt");
            // Lijst met keys om te kunnen itereren.
            mapKeys = nodes.keySet().toArray(new String[0]);
            // Zoekt overlap tussen nodes.
            mapMaker();
            // Zoekt optimale path tussen nodes.
            ArrayList<String> path = mapReader();
            // Maakt superstring met behulp van path.
            String seq = makeSuperString(path);
            System.out.println(seq);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zoekt de optimale "path" tussen alle nodes.
     * @return ArrayList met keys op volgorde.
     */
    public static ArrayList<String> mapReader(){
        ArrayList<String> path = new ArrayList<>();
        HashSet<String> usedKeys = new HashSet<>();

        // Zoekt sequentie zonder sequenties aan de linker kant.
        String startKey = getStartKey();
        if (startKey == null) {
            System.out.println("Unable to locate first seq.");
            System.exit(0);
        } else {
            path.add(startKey);
            usedKeys.add(startKey);
        }

        while (true){
            String bestKey = null;
            int bestOverlap = 0;

            Node last = nodes.get(path.get(path.size()-1));

            // Haalt alle sequenties die al gebruikt zijn uit usableKeys.
            Set<String> usableKeys = last.getRightList().keySet();
            usableKeys.removeAll(usedKeys);

            // Zoekt in bruikbare sequenties naar de grootste overlap.
            for (String key: usableKeys) {
                if (bestKey == null || last.getOverlapRight(key) > bestOverlap) {
                    bestKey = key;
                    bestOverlap = last.getOverlapRight(key);
                }
            }
            // Er zijn geen sequenties rechts.
            if (bestKey == null) {
                System.out.println("End reached, no matches found.");
                break;
            } else {
                // Voegt beste overlap toe aan path.
                path.add(bestKey);
                usedKeys.add(bestKey);
            }
        }
        System.out.println(path);
        return path;
    }

    /**
     * Maakt superstring met behulp van een gegeven path.
     * @param path Arraylist met keys op volgorde
     * @return Kortste superstring.
     */
    public static String makeSuperString(ArrayList<String> path){
        StringBuilder seq = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            Node node = nodes.get(path.get(i));
            if (i == 0){
                seq.append(node.getSeq());
            } else {
                seq.append(node.getSeq().substring(node.getOverlapLeft(path.get(i-1))));
            }
        }
        return seq.toString();
    }

    /**
     * Zoekt naar de sequentie zonder items in leftlist.
     * @return Key van start sequentie.
     */
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

    /**
     * Vergelijkt iedere Node uit "nodes" met elke andere.
     * Slaat de overlap tussen twee nodes op in de Node objects.
     */
    public static void mapMaker(){
        Node node1, node2;
        String seq1, seq2, name1, name2;
        boolean foundOverlap1, foundOverlap2;
        int halfway;
        int shortest;

        for (int i = 0; i < mapKeys.length; i++) {
            // Informatie uit de eerste node.
            name1 = mapKeys[i];
            node1 = nodes.get(name1);
            seq1 = node1.getSeq();
            halfway = seq1.length() / 2;
            for (int j = i + 1; j < mapKeys.length; j++) {
                // Informatie uit de tweede node.
                name2 = mapKeys[j];
                node2 = nodes.get(name2);
                seq2 = node2.getSeq();
                // Booleans om te kijken of de overlap al gevonden is.
                foundOverlap1 = false;
                foundOverlap2 = false;
                //Voorkomt index error als de twee strings niet even lang zijn.
                shortest = Math.min(seq1.length(), seq2.length());
                for (int k = shortest; k > halfway; k--) {
                    // Slaat overlap op als seq1 (tot k) begint met het einde (vanaf k) van seq 2.
                    if (!foundOverlap1 && seq1.startsWith(seq2.substring(seq2.length() - k))){
                        node1.addLeft(name2, k);
                        node2.addRight(name1, k);
                        foundOverlap1 = true;
                    }
                    // Slaat overlap op als seq2 (tot k) begint met het einde (vanaf k) van seq 1.
                    if (!foundOverlap2 && seq2.startsWith(seq1.substring(seq1.length() - k))){
                        node2.addLeft(name1, k);
                        node1.addRight(name2, k);
                        foundOverlap2 = true;
                    }
                    // Stop als Overlap gevonden is.
                    if (foundOverlap1 && foundOverlap2) {
                        break;
                    }
                }
            }
        }
        System.out.println(nodes);
    }

    /**
     * Leest een fasta file en zet de sequenties als Node objects in nodes met headers als keys.
     * @param filepath Path naar de fasta file.
     * @throws IOException File kan niet gevonden/geopened worden.
     */
    public static void readFasta(String filepath) throws IOException {
        String line;
        String header = null;
        StringBuilder seq = new StringBuilder();

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
    }
}
