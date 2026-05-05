package de.matse.ihk;

public abstract class FahrplanStrategie {
    public abstract String getStrategieName(); 
    public abstract void berechneFahrplan(BahnhofPlan plan, int startZeit); 
    public abstract int getSummeStrafen(); 
    public abstract int getSummeWarteZeit(); 
    protected BahnhofPlan aktuellerPlan; // Hier speichern wir den Plan zwischen


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

public static void hinfahrtMitKollision(BahnhofPlan plan, int startZeit) {
            BahnhofNode current = plan.getHead(); 
            current.setAbfahrthin(startZeit);
            while(current.getNext() != null){
                current.getNext().setAnkunfthin((current.getAbfahrthin()+ current.getDistanceToNext())%60);
                current.getNext().setAbfahrthin((current.getNext().getAnkunfthin() + 1 + current.getNext().getWartezeitHin())%60);
                current = current.getNext();
            }
}

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
    public static void beidseitigesWarten(BahnhofPlan plan, int startZeitHin){
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
