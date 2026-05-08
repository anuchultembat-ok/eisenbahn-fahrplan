package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;

/**
 * Sucht die Rückfahrt-Startzeit mit der geringsten Strafe durch Testen aller 60 Möglichkeiten
 * und verteilt verbleibende Wartezeit gleichmäßig auf beide Richtungen.
 * Strafe = (Σ wartezeitHin)² + (Σ wartezeitRueck)².
 */
public class BeidseitigesWartenStrategie extends FahrplanStrategie {

    private int besteStrafe = Integer.MAX_VALUE;
    private int besteStartzeitRueck = -1;

    @Override
    public String getStrategieName() { return "Beidseitiges Warten"; }

    /**
     * Vollständige Suche über alle 60 Rückfahrt-Startzeiten; überspringt Kandidaten,
     * bei denen das vorletzte Segment noch eine Kollision hat. Wendet {@link #beidseitigesWarten}
     * an, wenn die beste Startzeit ungleich null ist (d.h. Wartezeit unvermeidbar).
     */
    @Override
    public void berechneFahrplan(BahnhofPlan plan, int startZeitHin) {
        this.aktuellerPlan = plan;
        for (int i = 0; i < 60; i++) {
            rueckfahrtMitKollision(aktuellerPlan, i);
            if (aktuellerPlan.getTail().getPrev().isKollision()) continue;
            int strafe = getSummeStrafen();
            if (strafe < besteStrafe) {
                besteStrafe = strafe;
                besteStartzeitRueck = i;
            }
        }

        rueckfahrtMitKollision(plan, besteStartzeitRueck);
        if (besteStartzeitRueck != 0) {
            beidseitigesWarten(plan, startZeitHin);
        }
    }

    /** Gibt (Σ wartezeitHin)² + (Σ wartezeitRueck)² zurück. */
    @Override
    public int getSummeStrafen() {
        int hinSumme = 0, rueckSumme = 0;
        BahnhofNode curr = aktuellerPlan.getHead();
        while (curr != null && curr.getNext() != null) {
            hinSumme  += curr.getWartezeitHin();
            rueckSumme += curr.getWartezeitRueck();
            curr = curr.getNext();
        }
        return (int) Math.pow(hinSumme, 2) + (int) Math.pow(rueckSumme, 2);
    }

    @Override
    public int getSummeWarteZeit() {
        int summe = 0;
        BahnhofNode curr = aktuellerPlan.getHead();
        while (curr != null && curr.getNext() != null) {
            summe += curr.getWartezeitHin();
            summe += curr.getWartezeitRueck();
            curr = curr.getNext();
        }
        return summe;
    }
}
