package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofPlan;

public class EinfacheFahrtStrategie extends FahrplanStrategie{
    @Override
    public String toString() {
        return "Einfache Fahrt"; 
    }
    @Override
    public String getStrategieName() {
        return "Einfache Fahrt";
    }
    @Override 
    public void berechneFahrplan(BahnhofPlan plan, int startZeit) {
        this.aktuellerPlan = plan; // Speichern des Plans für spätere Berechnungen
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
