package de.matse.ihk.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;
import de.matse.ihk.strategie.FahrplanStrategie;

/**
 * Formatiert und speichert die Simulationsergebnisse.
 * Alle Methoden sind statisch — diese Klasse ist nicht zur Instanziierung gedacht.
 */
public class AusgabeManager {

    /**
     * Gibt eine lesbare Zusammenfassung der Eingabedaten aus: Stationsnamen,
     * Streckenabstände und die geplante Abfahrtszeit.
     * Erscheint einmal pro Datei, vor den drei Strategieblöcken.
     */
    public static String druckeEingabe(BahnhofPlan plan, int startZeit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Eingabe:\n");

        sb.append("  Strecke   : ");
        BahnhofNode cur = plan.getHead();
        while (cur != null) {
            sb.append(cur.getName());
            if (cur.getNext() != null) sb.append(" → ");
            cur = cur.getNext();
        }
        sb.append("\n");

        sb.append("  Abstände  : ");
        cur = plan.getHead();
        boolean first = true;
        while (cur != null && cur.getNext() != null) {
            if (!first) sb.append(", ");
            sb.append(cur.getDistanceToNext()).append(" min");
            first = false;
            cur = cur.getNext();
        }
        sb.append("\n");

        sb.append(String.format("  Start Hin : %02d%n", startZeit));
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Erstellt die vollständige 7-zeilige Fahrplantabelle einer Strategie samt Statistikblock.
     *
     * @param strategie die Strategie, deren berechnete Daten dargestellt werden
     * @param plan      der Plan, dessen Knoten die berechneten Zeiten enthalten
     * @return formatierter Text zum Ausgeben oder Speichern
     */
    public static String druckeErgebnis(FahrplanStrategie strategie, BahnhofPlan plan) {
        StringBuilder sb = new StringBuilder();
        sb.append(strategie.getStrategieName()).append(":\n");

        sb.append(printRow("An ", n -> n.getAnkunfthin()   != null ? String.format("%02d", n.getAnkunfthin())   : "  ", plan, false));
        sb.append(printRow("Wa ", n -> n.getWartezeitHin()  > 0   ? String.format("%02d", n.getWartezeitHin())  : "  ", plan, false));
        sb.append(printRow("Ab ", n -> n.getAbfahrthin()   != null ? String.format("%02d", n.getAbfahrthin())   : "  ", plan, false));
        sb.append(printRow("   ", n -> String.format("%2s", n.getName()), plan, true));
        sb.append(printRow("Ab ", n -> n.getAbfahrtrueck() != null ? String.format("%02d", n.getAbfahrtrueck()) : "  ", plan, false));
        sb.append(printRow("Wa ", n -> n.getWartezeitRueck() > 0  ? String.format("%02d", n.getWartezeitRueck()) : "  ", plan, false));
        sb.append(printRow("An ", n -> n.getAnkunftrueck() != null ? String.format("%02d", n.getAnkunftrueck()) : "  ", plan, false));

        int mindestdauer = berechneMindestdauer(plan);
        int warteHin = 0, warteRueck = 0;
        BahnhofNode current = plan.getHead();
        while (current != null) {
            warteHin   += current.getWartezeitHin();
            warteRueck += current.getWartezeitRueck();
            current = current.getNext();
        }

        sb.append(String.format("Gesamtdauer Hinfahrt, Rückfahrt       : %d, %d%n",
                mindestdauer + warteHin, mindestdauer + warteRueck));
        sb.append(String.format("Summe Wartezeiten Hinfahrt, Rückfahrt : %d, %d%n", warteHin, warteRueck));
        sb.append(String.format("Summe Strafen                         : %d%n", strategie.getSummeStrafen()));
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Erzeugt eine horizontale Zeile der Ausgabetabelle.
     *
     * @param label          3-stellige Zeilenbeschriftung (z.B. {@code "An "})
     * @param wertExtraktor  Lambda, das den formatierten Zellwert aus einem Knoten extrahiert
     * @param isStationRow   bei true wird {@code "x"} nach Knoten mit Kollision im Folgesegment angehängt
     */
    private static String printRow(String label,
                                   Function<BahnhofNode, String> wertExtraktor,
                                   BahnhofPlan plan,
                                   boolean isStationRow) {
        StringBuilder row = new StringBuilder();
        row.append(String.format("%-3s", label));

        BahnhofNode current = plan.getHead();
        while (current != null) {
            row.append(wertExtraktor.apply(current));
            if (current.getNext() != null)
                row.append(isStationRow && current.isKollision() ? "x " : "  ");
            current = current.getNext();
        }
        row.append("\n");
        return row.toString();
    }

    /**
     * Gibt die Mindestfahrtdauer zurück: Summe aller Streckenabstände plus je eine Halteminute
     * pro Zwischenbahnhof (alle Stationen außer der ersten und letzten).
     */
    public static int berechneMindestdauer(BahnhofPlan plan) {
        int dauer = 0;
        int anzahl = 0;
        BahnhofNode cur = plan.getHead();
        while (cur != null) {
            anzahl++;
            if (cur.getNext() != null) dauer += cur.getDistanceToNext();
            cur = cur.getNext();
        }
        if (anzahl > 2) dauer += (anzahl - 2);
        return dauer;
    }

    /**
     * Schreibt {@code inhalt} in {@code ausgabe/<dateiname ohne .txt>_ergebnis.txt}.
     * Erstellt das Ausgabeverzeichnis, falls es noch nicht existiert.
     */
    public static void speicherErgebnis(String dateiname, String inhalt) {
        File verzeichnis = new File("ausgabe");
        if (!verzeichnis.exists()) verzeichnis.mkdir();

        File zielDatei = new File(verzeichnis, dateiname.replace(".txt", "") + "_ergebnis.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(zielDatei))) {
            writer.write(inhalt);
            System.out.println("  Gespeichert: " + zielDatei.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("  Fehler beim Speichern: " + e.getMessage());
        }
    }
}
