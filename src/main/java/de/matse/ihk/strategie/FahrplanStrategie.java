package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;

/**
 * Gemeinsame Basisklasse aller Fahrplanstrategien (Strategy Pattern).
 * Konkrete Unterklassen implementieren {@link #berechneFahrplan} und die Strafmethoden;
 * die statischen Hilfsmethoden enthalten die gemeinsamen Traversierungsalgorithmen.
 */
public abstract class FahrplanStrategie {

    /** @return Anzeigename für die Ausgabeüberschriften */
    public abstract String getStrategieName();

    /**
     * Berechnet den vollständigen Fahrplan und schreibt die Ergebnisse in {@code plan}.
     * Wird einmal pro Strategielauf aufgerufen; {@code plan} wurde vorher zurückgesetzt.
     */
    public abstract void berechneFahrplan(BahnhofPlan plan, int startZeit);

    /** @return quadratische Strafe für die berechneten Wartezeiten dieser Strategie */
    public abstract int getSummeStrafen();

    /** @return Gesamtwartezeit in Minuten über alle Bahnhöfe und beide Richtungen */
    public abstract int getSummeWarteZeit();

    /** Referenz auf den Plan, der in {@link #berechneFahrplan} gesetzt wurde; wird von den Strafmethoden verwendet. */
    protected BahnhofPlan aktuellerPlan;


/** Durchläuft head → tail und setzt alle Hinfahrtzeiten ohne Wartezeit. */
public static void hinfahrtOhneKollision(BahnhofPlan plan, int startZeit) {
            BahnhofNode current = plan.getHead(); 
            warteZeitHinReset(plan);
            current.setAbfahrthin(startZeit);
            while(current.getNext() != null){
                current.getNext().setAnkunfthin((current.getAbfahrthin()+ current.getDistanceToNext())%60);
                current.getNext().setAbfahrthin((current.getNext().getAnkunfthin() + 1)%60);
                current = current.getNext();
            }
}

/** Durchläuft tail → head und setzt alle Rückfahrtzeiten ohne Wartezeit. */
public static void rueckfahrtOhneKollision(BahnhofPlan plan, int startZeit) {
            BahnhofNode current = plan.getTail(); 
            warteZeitRueckReset(plan);
            current.setAbfahrtrueck(startZeit);
            while(current.getPrev() != null){
                current.getPrev().setAnkunftrueck((current.getAbfahrtrueck()+ current.getPrev().getDistanceToNext())%60);
                current.getPrev().setAbfahrtrueck((current.getPrev().getAnkunftrueck() + 1)%60);
                current = current.getPrev();
            }
}

/** Vorwärtsdurchlauf, der die {@code wartezeitHin} jedes Knotens zur Abfahrtszeit addiert. */
public static void hinfahrtMitKollision(BahnhofPlan plan, int startZeit) {
            BahnhofNode current = plan.getHead(); 
            current.setAbfahrthin(startZeit);
            while(current.getNext() != null){
                current.getNext().setAnkunfthin((current.getAbfahrthin()+ current.getDistanceToNext())%60);
                current.getNext().setAbfahrthin((current.getNext().getAnkunfthin() + 1 + current.getNext().getWartezeitHin())%60);
                current = current.getNext();
            }
}

/**
 * Zweiphasige Rückfahrtberechnung.
 * Phase 1: Basisberechnung über {@link #rueckfahrtOhneKollision}.
 * Phase 2: Rückwärtsdurchlauf — berechnet die Ankunft jedes Knotens vom Nachfolger neu und
 * fügt Wartezeit ein, wo das Vorgängersegment eine Kollision hat.
 */
public static void rueckfahrtMitKollision(BahnhofPlan plan, int startZeit) {
            BahnhofNode current = plan.getTail(); 
            rueckfahrtOhneKollision(plan, startZeit);
            current = current.getPrev();
          
            while(current.getPrev() != null){
                
                current.setAnkunftrueck((current.getNext().getAbfahrtrueck()+ current.getDistanceToNext())%60);
                current.setAbfahrtrueck((current.getAnkunftrueck() +1)%60);
                current.getPrev().setAnkunftrueck((current.getAbfahrtrueck() + current.getPrev().getDistanceToNext())%60);

                if(current.getPrev().isKollision()){
                    current.setWartezeitRueck((current.getAnkunfthin() - current.getAnkunftrueck() +60 )%60);
                }
                current.setAbfahrtrueck((current.getAnkunftrueck() + current.getWartezeitRueck()+1)%60);      


                current = current.getPrev();
            }
            current.setAnkunftrueck((current.getNext().getAbfahrtrueck()+ current.getDistanceToNext())%60);
    }
    /**
     * Teilt die {@code wartezeitRueck} jedes Knotens gleichmäßig auf beide Richtungen auf
     * (Abrunden/Aufrunden bei ungeraden Werten) und berechnet beide Fahrten neu.
     * Gleiche Aufteilung ergibt stets eine niedrigere kombinierte quadratische Strafe
     * als einseitiges Warten.
     */
    public static void beidseitigesWarten(BahnhofPlan plan, int startZeitHin) {
          BahnhofNode head = plan.getHead(); 
          int zeitverschiebung = 0;
          int gesamtZeit;
            while (head != null && head.getNext()!= null){
            if(head.getWartezeitRueck() > 0 ){
                if(head.getWartezeitRueck() !=1){
                    gesamtZeit = head.getWartezeitRueck(); 
                    head.setWartezeitHin(head.getWartezeitRueck()/2);
                    head.setWartezeitRueck(gesamtZeit - head.getWartezeitHin());
                    zeitverschiebung += head.getWartezeitHin();
                }
            }
            head = head.getNext();
        }
            hinfahrtMitKollision(plan, startZeitHin);
            rueckfahrtMitKollision(plan, plan.getTail().getAbfahrtrueck() + zeitverschiebung);
    }

    public static void warteZeitRueckReset(BahnhofPlan plan){
        BahnhofNode current = plan.getHead();
        while(current.getNext() != null){
            current.setWartezeitRueck(0);
            current = current.getNext();
        }
    }
    public static void warteZeitHinReset(BahnhofPlan plan){
        BahnhofNode current = plan.getHead();
        while(current.getNext() != null){
            current.setWartezeitHin(0);
            current = current.getNext();
        }
    }
}
