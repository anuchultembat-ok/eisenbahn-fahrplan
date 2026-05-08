package de.matse.ihk.model;

/**
 * One station in the doubly linked list that models the railway track.
 * Stores arrival, departure and waiting-time values for both travel directions,
 * plus the pointer fields {@code next}/{@code prev} and the distance to the next station.
 */
public class BahnhofNode {
    private String name;
    private Integer ankunfthin;
    private Integer abfahrthin;
    /** Waiting time inserted by a strategy for the forward journey (default 0). */
    private Integer wartezeitHin = 0;
    /** Waiting time inserted by a strategy for the return journey (default 0). */
    private Integer wartezeitRueck = 0;

    private Integer ankunftrueck;
    private Integer abfahrtrueck;

    private BahnhofNode next;
    private BahnhofNode prev;
    /** Travel time in minutes from this station to {@code next}. */
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

    /**
     * Returns true if both trains occupy the segment between this node and {@code next}
     * at the same minute. Handles Modulo-60 wrap-around (e.g. interval [55, 5]).
     */
    public boolean isKollision() {
        if (this.next == null) return false;
        int hStart = this.abfahrthin;
        int hEnde  = this.next.getAnkunfthin();
        int rStart = this.next.getAbfahrtrueck();
        int rEnde  = this.ankunftrueck;
        return checkOverlap(hStart, hEnde, rStart, rEnde);
    }

    /** Iterates all 60 minutes and returns true on the first minute both intervals are active. */
    private boolean checkOverlap(int s1, int e1, int s2, int e2) {
        for (int m = 0; m < 60; m++) {
            if (isTimeInInterval(m, s1, e1) && isTimeInInterval(m, s2, e2))
                return true;
        }
        return false;
    }

    /**
     * Checks whether minute {@code t} falls inside [start, ende] on the Modulo-60 clock.
     * When start > ende the interval wraps around midnight (e.g. [55, 5]).
     */
    private boolean isTimeInInterval(int t, int start, int ende) {
        if (start <= ende) return t >= start && t <= ende;
        else               return t >= start || t <= ende;
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

    public BahnhofNode(String name) {
        this.name = name;
    }

    public String getName() {
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
