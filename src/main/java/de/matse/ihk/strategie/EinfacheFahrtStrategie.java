package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofPlan;

/**
 * Berechnet den Fahrplan ohne jede Wartezeit.
 * Kollisionen sind in der Ausgabe sichtbar (markiert mit 'x'), werden aber nicht aufgelöst.
 * Die Strafe beträgt stets null.
 */
public class EinfacheFahrtStrategie extends FahrplanStrategie {
    @Override
    public String toString() { return "Einfache Fahrt"; }

    @Override
    public String getStrategieName() { return "Einfache Fahrt"; }

    /** Führt die Hinfahrt ab startZeit durch und startet die Rückfahrt eine Minute nach Ankunft am letzten Bahnhof. */
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
