package de.matse.ihk.model;

import java.util.List;

public class BahnhofPlan{
    private BahnhofNode head; 
    private BahnhofNode tail; 
    private int groesse = 0; 

    public void addBahnhof(String name) {
        BahnhofNode newNode = new BahnhofNode(name);
        if(head == null){
            head = newNode; 
            tail = newNode; 
        }else {
            tail.setNext(newNode); 
            newNode.setPrev(tail); 
            tail = newNode; 
        }
        groesse++;
    }
    public BahnhofPlan(){}
    public void setAbstaende(int[] abstaende){
        BahnhofNode current = head; 
        int index = 0; 
        while(current != null && index < abstaende.length){
            if(current.getNext() != null){
                current.setDistanceToNext(abstaende[index]);
            }
            current = current.getNext();
            index++;
        }
    }
    public BahnhofNode getHead() {
        return head;
    }

     public BahnhofNode getTail() {
        return tail;
    }

     public int getGroesse() {
        return groesse;
    }

public void reset() {
    BahnhofNode current = this.head;
    while (current.getNext() != null) {
        // Zeiten auf null setzen (Wichtig: Nutze Integer statt int, wie besprochen)
        current.setAnkunfthin(null);
        current.setAbfahrthin(null);
        current.setAnkunftrueck(null);
        current.setAbfahrtrueck(null);
        
        current.setWartezeitHin(0);
        current.setWartezeitRueck(0);
        
        current = current.getNext();
    }
}
// Copy-Constructor
    public BahnhofPlan(BahnhofPlan original) {
        if (original.getHead() == null) return;
        
        // Ersten Knoten kopieren
        this.head = new BahnhofNode(original.getHead().getName());
        BahnhofNode currentOrig = original.getHead();
        BahnhofNode currentNew = this.head;
        
        // Die restliche Kette nachbauen
        while (currentOrig.getNext() != null) {
            BahnhofNode nextOrig = currentOrig.getNext();
            BahnhofNode nextNew = new BahnhofNode(nextOrig.getName());
            
            // Verbindung setzen
            currentNew.setNext(nextNew);
            nextNew.setPrev(currentNew);
            
            // Wichtig: Auch die Abstände (Distances) kopieren!
            currentNew.setDistanceToNext(currentOrig.getDistanceToNext());
            
            currentOrig = nextOrig;
            currentNew = nextNew;
        }
        this.tail = currentNew;
    }

}


// package de.matse.ihk;

// public class Kette {
//     private BahnhofNode prev; 
//     private BahnhofNode next; 

//     private int distance;
//     private BahnhofNode current;

//     public Kette(BahnhofNode current) {
//         this.current = current;
//     }

//     public BahnhofNode getCurrent() {
//         return current;
//     }
//     public void setCurrent(BahnhofNode current) {
//         this.current = current;
//     }

//     public Kette(BahnhofNode current, BahnhofNode next, int distance) {
//         this.current = current;
//         this.next = next;
//         this.distance = distance;
//     }
//     public void setNext(BahnhofNode next, int distance) {
//         this.next = next;
//         this.distance = distance; 
//     }
//     public void setPrev(BahnhofNode prev) {
//         this.prev = prev;
//     }
//     public BahnhofNode getNext() {
//         return next;
//     }
//     public BahnhofNode getPrev() {
//         return prev;
//     }
//     public boolean isKollision(){
//         if( (current.getAbfahrthin()> next.getAbfahrtrueck() && next.getAnkunfthin()+1 < next.getAbfahrtrueck()) || (current.getAbfahrthin() >= current.getAnkunftrueck() && next.getAnkunfthin() +1< current.getAnkunftrueck()) ){
//             return true;
//         }
//         return false;
//     }
// }
