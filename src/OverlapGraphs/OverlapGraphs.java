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
            for (String[] item: overlapGraph) {
                System.out.println(Arrays.toString(item));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readFasta(String filepath) throws IOException {
        String line;
        String header = null;
        StringBuilder seq = new StringBuilder();
        ArrayList<String[]> seqMap = new ArrayList<>();

        BufferedReader inFile = new BufferedReader(new FileReader(filepath));
        while ((line = inFile.readLine()) != null) {
            if (line.startsWith(">")) {
                if (header != null) {
                    seqMap.add(new String[]{header, seq.toString()});
                    seq = new StringBuilder();
                }
                header = line.strip().substring(1);
            } else {
                seq.append(line.strip());
            }
        }
        seqMap.add(new String[]{header, seq.toString()});
        return seqMap;
    }

    public static ArrayList<String[]> overlapGraphMaker(ArrayList<String[]> seqList){
        String[] item1, item2;
        String seq1, seq2, name1, name2;
        ArrayList<String[]> overlapGraph = new ArrayList<>();

        for (int i = 0; i < seqList.size(); i++) {
            item1 = seqList.get(i);
            name1 = item1[0];
            seq1 = item1[1];
            for (int j = i+1; j < seqList.size(); j++) {
                item2 = seqList.get(j);
                name2 = item2[0];
                seq2 = item2[1];
                if (!(seq1.equals(seq2))) {
                    if (seq1.substring(seq1.length()-OVERLAP).equals(seq2.substring(0, OVERLAP))) {
                        overlapGraph.add(new String[]{name1, name2});
                    } else if (seq2.substring(seq2.length()-OVERLAP).equals(seq1.substring(0, OVERLAP))) {
                        overlapGraph.add(new String[]{name2, name1});
                    }
                }
            }
        }
        return overlapGraph;
    }


}
