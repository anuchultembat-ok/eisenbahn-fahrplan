package de.matse.ihk.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;
import java.io.BufferedWriter;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;
import de.matse.ihk.strategie.FahrplanStrategie;


public class AusgabeManager {
    /**
     * Sammelt das Ergebnis in einem String, druckt es in die Konsole
     * und gibt es zurück, damit es gespeichert werden kann.
     */

    public static String druckeErgebnis(FahrplanStrategie strategie, BahnhofPlan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append(strategie.getStrategieName()).append(":\n");
       
        
        // 1. Die Zeilen für die Hinfahrt drucken
        sb.append(printRow("An ", n -> n.getAnkunfthin() != null ? String.format("%02d", n.getAnkunfthin()) : "  ", plan, false));
        sb.append(printRow("Wa ", n -> n.getWartezeitHin() > 0 ? String.format("%02d", n.getWartezeitHin()) : "  ", plan, false));
        sb.append(printRow("Ab ", n -> n.getAbfahrthin() != null ? String.format("%02d", n.getAbfahrthin()) : "  ", plan, false));
        
        // 2. Die Bahnhofsnamen (und ggf. das 'x' für Kollisionen) in der Mitte
        sb.append(printRow("   ", n -> String.format("%2s", n.getName()), plan, true));
        
        // 3. Die Zeilen für die Rückfahrt drucken (Achtung: Abfahrt steht oben, Ankunft unten!)
        sb.append(printRow("Ab ", n -> n.getAbfahrtrueck() != null ? String.format("%02d", n.getAbfahrtrueck()) : "  ", plan, false));
        sb.append(printRow("Wa ", n -> n.getWartezeitRueck() > 0 ? String.format("%02d", n.getWartezeitRueck()) : "  ", plan, false));
        sb.append(printRow("An ", n -> n.getAnkunftrueck() != null ? String.format("%02d", n.getAnkunftrueck()) : "  ", plan, false));
        
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
        sb.append(String.format("Gesamtdauer Hinfahrt, Rückfahrt       : %d, %d%n", dauerHin, dauerRueck));
        sb.append(String.format("Summe Wartezeiten Hinfahrt, Rückfahrt : %d, %d%n", warteHin, warteRueck));
        
        // Falls du getSummeStrafen in der Strategie implementiert hast:
        sb.append(String.format("Summe Strafen                         : %d%n", strategie.getSummeStrafen()));
        sb.append("\n");
        return sb.toString();
    }

    // --- Die magische Helper-Methode für das horizontale Layout ---
    private static String printRow(String label, Function<BahnhofNode, String> wertExtraktor, BahnhofPlan plan, boolean isStationRow) {
        StringBuilder row = new StringBuilder();
        // Label drucken (z.B. "An ")   
        row.append(String.format("%-3s", label));
        
        BahnhofNode current = plan.getHead();
        while (current != null) {
            // Holt den formatierten Wert für diesen Knoten (z.B. "22" oder "  ")
            row.append(wertExtraktor.apply(current));
            
            // Lücke zum nächsten Bahnhof drucken
            if (current.getNext() != null) {
                //Kollision-X Logik
                row.append(isStationRow && current.isKollision() ? "x " : "  ");
            }
            current = current.getNext();
        }
        row.append("\n");
        return row.toString();
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
    public static void speicherErgebnis(String dateiname, String inhalt){
        File verzeichnis = new File("ausgabe");

        if(!verzeichnis.exists()) verzeichnis.mkdir(); 

        File zielDatei = new File(verzeichnis, dateiname.replace(".txt", "")+ "_ergebnis.txt"); 
        try (BufferedWriter writer  = new BufferedWriter(new FileWriter(zielDatei))){
            writer.write(inhalt); 
            System.out.println("Ergebnis erfolgreich in " + zielDatei.getAbsolutePath() + " gespeichert.");
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }
}