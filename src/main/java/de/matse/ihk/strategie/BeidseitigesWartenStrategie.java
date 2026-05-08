package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;

/**
 * Finds the return-journey start time with the lowest penalty by trying all 60 options,
 * then splits any remaining waiting time equally between both directions.
 * Penalty = (Σ wartezeitHin)² + (Σ wartezeitRueck)².
 */
public class BeidseitigesWartenStrategie extends FahrplanStrategie {

    private int besteStrafe = Integer.MAX_VALUE;
    private int besteStartzeitRueck = -1;

    @Override
    public String getStrategieName() { return "Beidseitiges Warten"; }

    /**
     * Exhaustive search over all 60 return start times; skips candidates where the
     * second-to-last segment still has a collision. Applies {@link #beidseitigesWarten}
     * when the best start time is non-zero (i.e. waiting time is unavoidable).
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

    /** Returns (Σ wartezeitHin)² + (Σ wartezeitRueck)². */
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
