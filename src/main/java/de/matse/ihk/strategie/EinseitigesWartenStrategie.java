package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;

/**
 * Nur der Rückzug wartet an Kollisionspunkten; die Hinfahrt bleibt unverändert.
 * Strafe = (Summe aller {@code wartezeitRueck})².
 */
public class EinseitigesWartenStrategie extends FahrplanStrategie {

    @Override
    public String getStrategieName() { return "Einseitiges Warten"; }

    /**
     * Liest die von {@link EinfacheFahrtStrategie} hinterlegte Ankunftszeit der Hinfahrt
     * und führt die zweiphasige Rückfahrtberechnung mit Kollisionskorrektur durch.
     */
    @Override
    public void berechneFahrplan(BahnhofPlan plan, int startZeit) {
        this.aktuellerPlan = plan;
        int startZeitRueck = (aktuellerPlan.getTail().getAnkunfthin() + 1) % 60;
        rueckfahrtMitKollision(aktuellerPlan, startZeitRueck);
    }

    /** Gibt (Σ wartezeitRueck)² zurück. */
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
