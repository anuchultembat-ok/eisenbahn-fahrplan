package de.matse.ihk;

import java.io.IOException;

import de.matse.ihk.io.AusgabeManager;
import de.matse.ihk.io.EingabeLeser;

public class Main {
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("MATSE Fahrplan-Simulation");
        System.out.println("====================================\n");

        SimulationsDaten daten;
        try {
            daten = EingabeLeser.leseDatei("eisenbahn-fahrplan/datei.txt");
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

        for(FahrplanStrategie strategie : strategien){
            strategie.berechneFahrplan(plan, startZeit);
            AusgabeManager.druckeErgebnis(strategie, plan);
        }
    }
}