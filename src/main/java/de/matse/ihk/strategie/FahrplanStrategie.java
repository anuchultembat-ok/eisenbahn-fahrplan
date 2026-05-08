package de.matse.ihk.strategie;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;

/**
 * Common base for all scheduling strategies (Strategy Pattern).
 * Concrete subclasses implement {@link #berechneFahrplan} and the penalty methods;
 * the static helper methods here contain the shared traversal algorithms.
 */
public abstract class FahrplanStrategie {

    /** @return display name used in output headers */
    public abstract String getStrategieName();

    /**
     * Computes the full timetable and writes results into {@code plan}.
     * Called once per strategy run; {@code plan} has been reset beforehand.
     */
    public abstract void berechneFahrplan(BahnhofPlan plan, int startZeit);

    /** @return quadratic penalty for this strategy's computed waiting times */
    public abstract int getSummeStrafen();

    /** @return total waiting minutes summed across all stations and both directions */
    public abstract int getSummeWarteZeit();

    /** Retained reference to the plan set during {@link #berechneFahrplan}, used by penalty methods. */
    protected BahnhofPlan aktuellerPlan;


/** Traverses head → tail, setting all forward-journey times without any waiting time. */
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

/** Traverses tail → head, setting all return-journey times without any waiting time. */
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

/** Forward traversal that adds each node's {@code wartezeitHin} to the departure time. */
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
 * Two-phase return-journey calculation.
 * Phase 1: baseline run via {@link #rueckfahrtOhneKollision}.
 * Phase 2: backwards pass — recomputes each node's arrival from its successor and
 * inserts waiting time wherever the predecessor segment has a collision.
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
     * Splits each node's {@code wartezeitRueck} evenly between both directions
     * (floor/ceil for odd values), then recalculates both journeys.
     * Equal split always yields a lower combined quadratic penalty than one-sided waiting.
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
