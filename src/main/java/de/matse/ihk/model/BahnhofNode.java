package de.matse.ihk.model;

/**
 * Ein Bahnhof in der doppelt verketteten Liste, die die Eisenbahnstrecke abbildet.
 * Speichert Ankunft, Abfahrt und Wartezeit für beide Fahrtrichtungen
 * sowie die Zeigerfelder {@code next}/{@code prev} und den Abstand zum nächsten Bahnhof.
 */
public class BahnhofNode {
    private String name;
    private Integer ankunfthin;
    private Integer abfahrthin;
    /** Wartezeit für die Hinfahrt, eingetragen durch eine Strategie (Standard 0). */
    private Integer wartezeitHin = 0;
    /** Wartezeit für die Rückfahrt, eingetragen durch eine Strategie (Standard 0). */
    private Integer wartezeitRueck = 0;

    private Integer ankunftrueck;
    private Integer abfahrtrueck;

    private BahnhofNode next;
    private BahnhofNode prev;
    /** Fahrtzeit in Minuten von diesem Bahnhof zum nächsten ({@code next}). */
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
     * Gibt true zurück, wenn beide Züge das Segment zwischen diesem Knoten und {@code next}
     * zur selben Minute belegen. Berücksichtigt den Modulo-60-Überlauf (z.B. Intervall [55, 5]).
     */
    public boolean isKollision() {
        if (this.next == null) return false;
        int hStart = this.abfahrthin;
        int hEnde  = this.next.getAnkunfthin();
        int rStart = this.next.getAbfahrtrueck();
        int rEnde  = this.ankunftrueck;
        return checkOverlap(hStart, hEnde, rStart, rEnde);
    }

    /** Prüft alle 60 Minuten und gibt true zurück, sobald beide Intervalle gleichzeitig aktiv sind. */
    private boolean checkOverlap(int s1, int e1, int s2, int e2) {
        for (int m = 0; m < 60; m++) {
            if (isTimeInInterval(m, s1, e1) && isTimeInInterval(m, s2, e2))
                return true;
        }
        return false;
    }

    /**
     * Prüft, ob Minute {@code t} im Intervall [start, ende] auf der Modulo-60-Uhr liegt.
     * Bei start > ende überschreitet das Intervall die Stundengrenze (z.B. [55, 5]).
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
