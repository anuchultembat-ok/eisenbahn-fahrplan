package de.matse.ihk.io;

import java.util.function.Function;

import de.matse.ihk.BahnhofNode;
import de.matse.ihk.BahnhofPlan;
import de.matse.ihk.FahrplanStrategie;

public class AusgabeManager {

    public static void druckeErgebnis(FahrplanStrategie strategie, BahnhofPlan plan) {
        System.out.println(strategie.getStrategieName() + ":");
        
        // 1. Die Zeilen für die Hinfahrt drucken
        printRow("An ", n -> n.getAnkunfthin() != null ? String.format("%02d", n.getAnkunfthin()) : "  ", plan, false);
        printRow("Wa ", n -> n.getWartezeitHin() > 0 ? String.format("%02d", n.getWartezeitHin()) : "  ", plan, false);
        printRow("Ab ", n -> n.getAbfahrthin() != null ? String.format("%02d", n.getAbfahrthin()) : "  ", plan, false);
        
        // 2. Die Bahnhofsnamen (und ggf. das 'x' für Kollisionen) in der Mitte
        printRow("   ", n -> String.format("%2s", n.getName()), plan, true);
        
        // 3. Die Zeilen für die Rückfahrt drucken (Achtung: Abfahrt steht oben, Ankunft unten!)
        printRow("Ab ", n -> n.getAbfahrtrueck() != null ? String.format("%02d", n.getAbfahrtrueck()) : "  ", plan, false);
        printRow("Wa ", n -> n.getWartezeitRueck() > 0 ? String.format("%02d", n.getWartezeitRueck()) : "  ", plan, false);
        printRow("An ", n -> n.getAnkunftrueck() != null ? String.format("%02d", n.getAnkunftrueck()) : "  ", plan, false);
        
        // 4. Statistik / Zusammenfassung berechnen
        int mindestdauer = berechneMindestdauer(plan);
        int warteHin = 0;
        int warteRueck = 0;
        
        BahnhofNode current = plan.getHead();
        while(current != null) {
            warteHin += current.getWartezeitHin();
            warteRueck += current.getWartezeitRueck();
            current = current.getNext();
        }
        
        int dauerHin = mindestdauer + warteHin;
        int dauerRueck = mindestdauer + warteRueck;
        
        // Die exakte Ausgabe der Statistik
        System.out.printf("Gesamtdauer Hinfahrt, Rückfahrt       : %d, %d%n", dauerHin, dauerRueck);
        System.out.printf("Summe Wartezeiten Hinfahrt, Rückfahrt : %d, %d%n", warteHin, warteRueck);
        
        // Falls du getSummeStrafen in der Strategie implementiert hast:
        System.out.printf("Summe Strafen                         : %d%n", strategie.getSummeStrafen()); 
        System.out.println();
    }

    // --- Die magische Helper-Methode für das horizontale Layout ---
    private static void printRow(String label, Function<BahnhofNode, String> wertExtraktor, BahnhofPlan plan, boolean isStationRow) {
        // Label drucken (z.B. "An ")
        System.out.print(String.format("%-3s", label));
        
        BahnhofNode current = plan.getHead();
        while (current != null) {
            // Holt den formatierten Wert für diesen Knoten (z.B. "22" oder "  ")
            System.out.print(wertExtraktor.apply(current));
            
            // Lücke zum nächsten Bahnhof drucken
            if (current.getNext() != null) {
                // Nur in der Namens-Zeile drucken wir das 'x' bei einer Kollision
                if (isStationRow && current.isKollision()) {
                    System.out.print("x");
                } else {
                    System.out.print(" ");
                }
            }
            current = current.getNext();
        }
        System.out.println(); // Zeilenumbruch am Ende
    }

    // --- Hilfsmethode, um die Mindestdauer (ohne Wartezeiten) zu errechnen ---
    public static int berechneMindestdauer(BahnhofPlan plan) {
        int dauer = 0;
        int anzahlBahnhoefe = 0;
        
        BahnhofNode current = plan.getHead();
        while(current != null) {
            anzahlBahnhoefe++;
            if (current.getNext() != null) {
                dauer += current.getDistanceToNext();
            }
            current = current.getNext();
        }
        
        // Haltezeiten (1 Min) für alle Bahnhöfe dazwischen addieren
        if (anzahlBahnhoefe > 2) {
            dauer += (anzahlBahnhoefe - 2); 
        }
        return dauer;
    }
}