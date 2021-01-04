package ShortestSuperstring;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Node {
    private final String seq;
    private LinkedHashMap<String, Integer> leftList, rightList;

    public String getSeq() {
        return seq;
    }

    public LinkedHashMap<String, Integer> getToList() {
        return leftList;
    }

    public LinkedHashMap<String, Integer> getRightList() {
        return rightList;
    }

    public Node(String seq) {
        this.seq = seq;
        leftList = new LinkedHashMap<>();
        rightList = new LinkedHashMap<>();
    }

    public void addLeft(String name, int overlap) {
        leftList.put(name, overlap);
    }

    public void addRight(String name, int overlap) {
        rightList.put(name, overlap);
    }

    public boolean isStart(){
        return leftList.isEmpty();
    }


}
