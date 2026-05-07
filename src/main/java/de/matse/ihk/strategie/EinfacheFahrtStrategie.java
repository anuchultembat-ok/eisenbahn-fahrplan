package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofPlan;

/**
 * Calculates a collision-free timetable without any waiting time.
 * Collisions are visible in the output (marked with 'x') but not resolved.
 * Penalty is always zero.
 */
public class EinfacheFahrtStrategie extends FahrplanStrategie {
    @Override
    public String toString() { return "Einfache Fahrt"; }

    @Override
    public String getStrategieName() { return "Einfache Fahrt"; }

    /** Runs forward journey from startZeit, then starts the return trip one minute after arrival at the last station. */
    @Override
    public void berechneFahrplan(BahnhofPlan plan, int startZeit) {
        this.aktuellerPlan = plan;
        hinfahrtOhneKollision(plan, startZeit);
        int startZeitRueck = (aktuellerPlan.getTail().getAnkunfthin() + 1) % 60;
        rueckfahrtOhneKollision(aktuellerPlan, startZeitRueck);
    }
    // public void berechneFahrplan(BahnhofPlan plan) {
    //     BahnhofPlan hinfahrt = hinfahrtOhneKollision(plan);
    //     BahnhofPlan rueckfahrt = rueckfahrtOhneKollision(hinfahrt);
    // }

    @Override
    public int getSummeStrafen() { return 0; } // Keine Wartezeit = 0 Strafen
    @Override
    public int getSummeWarteZeit() { return 0; }
}
