package de.matse.ihk; 

public class EinseitigesWartenStrategie extends FahrplanStrategie{

    @Override
    public String getStrategieName() {
        return "Einseitiges Warten";
    }
    @Override 
    public void berechneFahrplan(BahnhofPlan plan, int startZeit) {
        this.aktuellerPlan = plan; // Speichern des Plans für spätere Berechnungen
        int startZeitRueck = (aktuellerPlan.getTail().getAnkunfthin() + 1) % 60;
        rueckfahrtMitKollision(aktuellerPlan, startZeitRueck);
        
    }

    @Override
    public int getSummeStrafen() {
        // TODO Auto-generated method stub
        int summe = 0;
        BahnhofNode current = aktuellerPlan.getHead();
        while(current.getNext()!= null){
            summe += current.getWartezeitRueck();
            current = current.getNext(); 
        }
        return (int) Math.pow(summe, 2);
    }

    @Override
    public int getSummeWarteZeit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSummeWarteZeit'");
    }
    
}