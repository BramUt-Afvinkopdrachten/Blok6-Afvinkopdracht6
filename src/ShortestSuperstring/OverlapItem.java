package ShortestSuperstring;

public class OverlapItem {
    String naam;
    int overlap;

    public OverlapItem(String naam, int overlap){
        this.naam = naam;
        this.overlap = overlap;
    }

    public String getNaam() {
        return naam;
    }

    public int getOverlap() {
        return overlap;
    }
}
