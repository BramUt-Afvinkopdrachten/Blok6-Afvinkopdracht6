package OverlapGraphs;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class OverlapGraphs {
    private final JFileChooser fileChooser = new JFileChooser();

    public OverlapGraphs(){
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        try {
            ArrayList<String[]> result = readFasta(file.getAbsolutePath());
            for (String[] seq: result) {
                System.out.println(Arrays.toString(seq));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OverlapGraphs app = new OverlapGraphs();
    }

    public static ArrayList<String[]> readFasta(String filepath) throws IOException {
        String line;
        String header = null;
        StringBuilder seq = new StringBuilder();
//        HashMap<String, String> seqMap = new HashMap<>();
        ArrayList<String[]> seqMap = new ArrayList<>();

        BufferedReader inFile = new BufferedReader(new FileReader(filepath));
        while ((line = inFile.readLine()) != null) {
            if (line.startsWith(">")) {
                if (header != null) {
//                    seqMap.put(header, seq.toString());
                    seqMap.add(new String[]{header, seq.toString()});
                    seq = new StringBuilder();
                }
                header = line.strip().substring(1);
            } else {
                seq.append(line.strip());
            }
        }
//        seqMap.put(header, seq.toString());
        seqMap.add(new String[]{header, seq.toString()});
        return seqMap;
    }
}
