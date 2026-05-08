package de.matse.ihk;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.matse.ihk.io.AusgabeManager;
import de.matse.ihk.io.EingabeLeser;
import de.matse.ihk.io.EingabeValidator;
import de.matse.ihk.model.BahnhofPlan;
import de.matse.ihk.model.SimulationsDaten;
import de.matse.ihk.strategie.BeidseitigesWartenStrategie;
import de.matse.ihk.strategie.EinfacheFahrtStrategie;
import de.matse.ihk.strategie.EinseitigesWartenStrategie;
import de.matse.ihk.strategie.FahrplanStrategie;

/**
 * Einstiegspunkt. Liest alle .txt-Dateien im Eingabeordner, validiert sie,
 * führt alle drei Strategien aus und schreibt je eine Ergebnisdatei.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("====================================");
        System.out.println("MATSE Fahrplan-Simulation");
        System.out.println("====================================\n");

        SimulationsDaten daten;
        String eingabeOrnder = args.length > 0 ? args[0] : "testfaelle";

        File inputDir = new File(eingabeOrnder);
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.err.println("Eingabeordner nicht gefunden: " + inputDir.getAbsolutePath());
            return;
        }
        File[] files = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.err.println("Keine .txt-Dateien im Ordner: " + inputDir.getAbsolutePath());
            return;
        }

        for (File file : files) {
            System.out.println("Verarbeite Datei: " + file.getName());
            try {
                daten = EingabeLeser.leseDatei(file.getPath());
            } catch (IOException e) {
                System.err.println("  Lesefehler: " + e.getMessage());
                continue;
            }

            // Eingabe validieren — ungültige Dateien überspringen
            List<String> fehler = EingabeValidator.validiere(daten);
            if (!fehler.isEmpty()) {
                System.err.println("  Validierungsfehler in " + file.getName() + ":");
                fehler.forEach(f -> System.err.println("    - " + f));
                continue;
            }

            BahnhofPlan plan = daten.plan();
            int startZeit = daten.startZeit();

            FahrplanStrategie[] strategien = {
                new EinfacheFahrtStrategie(),
                new EinseitigesWartenStrategie(),
                new BeidseitigesWartenStrategie()
            };

            StringBuilder gesamtausgabe = new StringBuilder();
            gesamtausgabe.append(AusgabeManager.druckeEingabe(plan, startZeit));

            for (FahrplanStrategie strategie : strategien) {
                strategie.berechneFahrplan(plan, startZeit);
                gesamtausgabe.append(AusgabeManager.druckeErgebnis(strategie, plan));
                gesamtausgabe.append(System.lineSeparator());
            }

            AusgabeManager.speicherErgebnis(file.getName(), gesamtausgabe.toString());
        }
    }
}
