package de.matse.ihk;

public class BahnhofNode {
    private char name;
    private Integer ankunfthin; 
    private Integer abfahrthin;
    private Integer wartezeitHin = 0;
    private Integer wartezeitRueck = 0;

    private Integer ankunftrueck; 
    private Integer abfahrtrueck;

    private BahnhofNode next;
    private BahnhofNode prev;
    private int distanceToNext;

    public BahnhofNode getNext() {
        return next;
    }
    public Integer getWartezeitRueck() {
        return wartezeitRueck;
    }
    public Integer setWartezeitRueck(Integer wartezeitRueck) {
        return this.wartezeitRueck = wartezeitRueck;
    }

    public Integer getWartezeitHin() {
        return wartezeitHin;
    }

    public Integer setWartezeitHin(Integer wartezeitHin) {
        return this.wartezeitHin = wartezeitHin;
    }

    public boolean isKollision() {
    if (this.next == null) return false; // Letzter Bahnhof hat keinen Streckenabschnitt danach

    // Zeitraum Hinfahrt: [H_Start, H_Ende]
    int hStart = this.abfahrthin;
    int hEnde = this.next.getAnkunfthin();

    // Zeitraum Rückfahrt: [R_Start, R_Ende]
    int rStart = this.next.getAbfahrtrueck();
    int rEnde = this.ankunftrueck;

    // Hilfsmethode, um zu prüfen, ob sich Intervalle im 60-Min-Takt schneiden
    return checkOverlap(hStart, hEnde, rStart, rEnde);
}

private boolean checkOverlap(int s1, int e1, int s2, int e2) {
    for (int m = 0; m < 60; m++) {
        if (isTimeInInterval(m, s1, e1) && isTimeInInterval(m, s2, e2)) {
            return true;
        }
    }
    return false;
}

private boolean isTimeInInterval(int t, int start, int ende) {
    if (start <= ende) {
        // Normaler Fall (z.B. 10 bis 20)
        return t >= start && t <= ende;
    } else {
        // Über den Stundenwechsel (z.B. 55 bis 05)
        return t >= start || t <= ende;
    }
}
    public void setDistanceToNext(int distanceToNext) {
        this.distanceToNext = distanceToNext;
    }
    public void setNext(BahnhofNode next) {
        this.next = next;
    }
    public int getDistanceToNext() {
        return distanceToNext;
    }

    public BahnhofNode getPrev() {
        return prev;
    }

    public void setPrev(BahnhofNode prev) {
        this.prev = prev;
    }

    public BahnhofNode(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }
    public Integer getAnkunfthin() {
        return ankunfthin;
    }
    public void setAnkunfthin(Integer ankunfthin) {
        this.ankunfthin = ankunfthin;
    }
    public Integer getAbfahrthin() {
        return abfahrthin;
    }
    public void setAbfahrthin(Integer abfahrthin) {
        this.abfahrthin = abfahrthin;
    }

    public Integer getAnkunftrueck() {
        return ankunftrueck;
    }
    public void setAnkunftrueck(Integer ankunftrueck) {
        this.ankunftrueck = ankunftrueck;
    }
    public Integer getAbfahrtrueck() {
        return abfahrtrueck;
    }
    public void setAbfahrtrueck(Integer abfahrtrueck) {
        this.abfahrtrueck = abfahrtrueck;
    }
    // public String toString() {
    //     return "BahnhofNode{" +
    //             "name=" + name +
    //             ", ankunfthin=" + ankunfthin +
    //             ", abfahrthin=" + abfahrthin +
    //             ", wartezeitHin=" + wartezeit +
    //             ", ankunftrueck=" + ankunftrueck +
    //             ", abfahrtrueck=" + abfahrtrueck +
    //             '}';
    // }
}
