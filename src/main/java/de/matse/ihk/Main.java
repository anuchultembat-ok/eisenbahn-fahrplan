package de.matse.ihk;

import java.io.File;
import java.io.IOException;

import de.matse.ihk.io.AusgabeManager;
import de.matse.ihk.io.EingabeLeser;
import de.matse.ihk.model.BahnhofPlan;
import de.matse.ihk.model.SimulationsDaten;
import de.matse.ihk.strategie.BeidseitigesWartenStrategie;
import de.matse.ihk.strategie.EinfacheFahrtStrategie;
import de.matse.ihk.strategie.EinseitigesWartenStrategie;
import de.matse.ihk.strategie.FahrplanStrategie;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("====================================");
        System.out.println("MATSE Fahrplan-Simulation");
        System.out.println("====================================\n");

        SimulationsDaten daten;
        String eingabeOrnder = args.length > 0 ? args[0] : "testfaelle";

        File inputDir = new File(eingabeOrnder); 
        if(!inputDir.exists() || !inputDir.isDirectory()){
            System.err.println("Der angegebene Eingabeordner existiert nicht oder ist kein Verzeichnis: " + inputDir.getAbsolutePath()  );
            return;
        } 
        File[] files = inputDir.listFiles((dir, name)-> name.toLowerCase().endsWith(".txt"));
        if(files == null || files.length == 0){
            System.err.println("Keine .txt Dateien im Eingabeordner gefunden: " + inputDir.getAbsolutePath());
            return;
        }

        for(File file: files){
            System.out.println("Verarbeite Datei: " + file.getName());
        try {
            daten = EingabeLeser.leseDatei(file.getPath());

        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
            return;
        }

        BahnhofPlan plan = daten.plan(); 
        int startZeit = daten.startZeit();
        FahrplanStrategie[] strategien = {
            new EinfacheFahrtStrategie(),
            new EinseitigesWartenStrategie(), 
            new BeidseitigesWartenStrategie()
            // Weitere Strategien hier hinzufügen
        };
        StringBuilder gesamtausgabe = new StringBuilder();
        for(FahrplanStrategie strategie : strategien){
            strategie.berechneFahrplan(plan, startZeit);
            gesamtausgabe.append(AusgabeManager.druckeErgebnis(strategie, plan)).append(System.lineSeparator());
        }
        String safeName = file.getName();
        AusgabeManager.speicherErgebnis(safeName, gesamtausgabe.toString());
    }
}
}