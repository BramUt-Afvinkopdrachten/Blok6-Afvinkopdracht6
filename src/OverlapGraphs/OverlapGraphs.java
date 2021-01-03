package OverlapGraphs;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class OverlapGraphs {
    private static final int OVERLAP = 3;

    public static void main(String[] args) {
        // Opent filechooser voor file selection.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        try {
            ArrayList<String[]> result = readFasta(file.getAbsolutePath());
            // Print sequenties met headers.
            for (String[] seq: result) {
                System.out.println(Arrays.toString(seq));
            }
            // Maakt overlapgraph.
            ArrayList<String[]> overlapGraph = overlapGraphMaker(result);
//            for (String[] item: overlapGraph) {
//                System.out.println(Arrays.toString(item));
//            }
            // Schrijft de overlap graph naar een .txt file.
            writeOutput(overlapGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Leest een fasta file in en returnt een Arraylist met Headers en sequenties.
     * @param filepath Path naar de fasta file.
     * @return Arraylist met headers en sequentties.
     * @throws IOException Exception als de file niet gevonden of geopend kan worden.
     */
    public static ArrayList<String[]> readFasta(String filepath) throws IOException {
        String line;
        String header = null;
        StringBuilder seq = new StringBuilder();
        ArrayList<String[]> seqMap = new ArrayList<>();

        BufferedReader inFile = new BufferedReader(new FileReader(filepath));
        // Leest regel per file.
        while ((line = inFile.readLine()) != null) {
            // Header
            if (line.startsWith(">")) {
                // Als er al een header is wordt deze met de sequentie toegevoegd aan seqMap.
                if (header != null) {
                    seqMap.add(new String[]{header, seq.toString()});
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
        seqMap.add(new String[]{header, seq.toString()});
        return seqMap;
    }

    public static ArrayList<String[]> overlapGraphMaker(ArrayList<String[]> seqList){
        String[] item1, item2;
        String seq1, seq2, name1, name2;
        ArrayList<String[]> overlapGraph = new ArrayList<>();

        // Itereert over de lijst met sequenties.
        for (int i = 0; i < seqList.size(); i++) {
            item1 = seqList.get(i);
            name1 = item1[0];
            seq1 = item1[1];
            // Itereert over aal sequenties na i.
            for (int j = i+1; j < seqList.size(); j++) {
                item2 = seqList.get(j);
                name2 = item2[0];
                seq2 = item2[1];
                // Sequenities de hetzelfde zijn worden niet vergeleken.
                if (!(seq1.equals(seq2))) {
                    // Controleerd of de suffix van seq1 (i) gelijk is aan de prefix van seq2 (j).
                    if (seq1.substring(seq1.length()-OVERLAP).equals(seq2.substring(0, OVERLAP))) {
                        // Voegt headers toe aan overlapGraph.
                        overlapGraph.add(new String[]{name1, name2});
                    }
                    // Controleerd of de suffix van seq2 (j) gelijk is aan de prefix van seq1 (i).
                    if (seq2.substring(seq2.length()-OVERLAP).equals(seq1.substring(0, OVERLAP))) {
                        // Voegt headers toe aan overlapGraph.
                        overlapGraph.add(new String[]{name2, name1});
                    }
                }
            }
        }
        return overlapGraph;
    }

    /**
     * Schrijft de overlapGraph naar een file in txt format.
     * @param overlapGraph overlapgraph met namen van sequenties.
     */
    public static void writeOutput(ArrayList<String[]> overlapGraph) {
        try {
            FileWriter fileWriter = new FileWriter("output.txt");
            for (String[] pair: overlapGraph) {
                fileWriter.write(String.format("%s %s\n", pair[0], pair[1]));
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
