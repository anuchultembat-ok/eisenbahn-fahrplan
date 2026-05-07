package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;

/**
 * Only the return train waits at collision points; the forward journey is unchanged.
 * Penalty = (sum of all {@code wartezeitRueck})².
 */
public class EinseitigesWartenStrategie extends FahrplanStrategie {

    @Override
    public String getStrategieName() { return "Einseitiges Warten"; }

    /**
     * Reads the forward-journey arrival time left by {@link EinfacheFahrtStrategie},
     * then runs the two-phase return calculation with collision correction.
     */
    @Override
    public void berechneFahrplan(BahnhofPlan plan, int startZeit) {
        this.aktuellerPlan = plan;
        int startZeitRueck = (aktuellerPlan.getTail().getAnkunfthin() + 1) % 60;
        rueckfahrtMitKollision(aktuellerPlan, startZeitRueck);
    }

    /** Returns (Σ wartezeitRueck)². */
    @Override
    public int getSummeStrafen() {
        int summe = 0;
        BahnhofNode current = aktuellerPlan.getHead();
        while (current.getNext() != null) {
            summe += current.getWartezeitRueck();
            current = current.getNext();
        }
        return (int) Math.pow(summe, 2);
    }

    @Override
    public int getSummeWarteZeit() {
        throw new UnsupportedOperationException("Unimplemented method 'getSummeWarteZeit'");
    }
}
