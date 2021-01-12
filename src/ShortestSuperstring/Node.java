package ShortestSuperstring;

import java.util.LinkedHashMap;

public class Node {
    private final String seq;
    private LinkedHashMap<String, Integer> leftList, rightList;

    /**
     * Getter voor Seq.
     * @return Sequentie.
     */
    public String getSeq() {
        return seq;
    }

    /**
     * Getter voor rightList.
     * @return LinkedHashmap met headers als key en overlap als value.
     */
    public LinkedHashMap<String, Integer> getRightList() {
        return rightList;
    }

    /**
     * Constructor voor Node
     * @param seq Sequentie van de node.
     */
    public Node(String seq) {
        this.seq = seq;
        leftList = new LinkedHashMap<>();
        rightList = new LinkedHashMap<>();
    }

    /**
     * Voegt overlap met een gegeven key (header van de andere node) toe.
     * @param name Key voor de andere node.
     * @param overlap Hoeveelheid overlap.
     */
    public void addLeft(String name, int overlap) {
        leftList.put(name, overlap);
    }

    /**
     * Voegt overlap met een gegeven key (header van de andere node) toe.
     * @param name Key voor de andere node.
     * @param overlap Hoeveelheid overlap.
     */
    public void addRight(String name, int overlap) {
        rightList.put(name, overlap);
    }

    /**
     * Controleert of de Node de start van de superstring is.
     * @return True als Node de eerste van de superstring is.
     */
    public boolean isStart(){
        return leftList.isEmpty();
    }

    /**
     * Returnt overlap met een gegeven Node.
     * @param key Key van de te vergelijken node.
     * @return Hoeveelheid overlap.
     */
    public int getOverlapRight(String key) {
        return rightList.get(key);
    }

    /**
     * Returnt overlap met een gegeven Node.
     * @param key Key van de te vergelijken node.
     * @return Hoeveelheid overlap.
     */
    public int getOverlapLeft(String key) {
        return leftList.get(key);
    }
}