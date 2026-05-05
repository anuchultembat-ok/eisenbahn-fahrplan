package de.matse.ihk;

public class BeidseitigesWartenStrategie extends FahrplanStrategie {
    private int besteStrafe = Integer.MAX_VALUE;
    private int besteStartzeitRueck = -1;

    @Override
    public String getStrategieName() { return "Beidseitiges Warten"; }

    @Override
    public void berechneFahrplan(BahnhofPlan plan, int startZeitHin) {
        this. aktuellerPlan = plan; 
        for (int i = 0; i< 60; i++){
             rueckfahrtMitKollision(aktuellerPlan, i );
             if(aktuellerPlan.getTail().getPrev().isKollision()) continue;
             int strafe = getSummeStrafen();
                if(strafe < besteStrafe){
                 besteStrafe = strafe;
                 besteStartzeitRueck = i;
            }
        }
            
        rueckfahrtMitKollision(plan, besteStartzeitRueck);
        if(besteStartzeitRueck != 0){
            beidseitigesWarten(plan, startZeitHin);
        }
    }

    


    @Override
    public int getSummeStrafen() {
        int hinSumme = 0;
        int rueckSumme = 0;
        BahnhofNode curr = aktuellerPlan.getHead();
        while (curr != null && curr.getNext() != null) {
            // Strafe = Wartezeit^2
            hinSumme += curr.getWartezeitHin();
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