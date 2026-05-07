package de.matse.ihk.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.matse.ihk.model.BahnhofPlan;
import de.matse.ihk.model.SimulationsDaten;

/**
 * Reads a plain-text input file and builds a {@link SimulationsDaten} object from it.
 * The file format uses keyword lines ({@code Strecke:}, {@code Abstaende:},
 * {@code Start Hinfahrt:}) followed by their data on the next line.
 */
public class EingabeLeser {

    /**
     * Parses {@code dateiPfad} and returns the station plan together with the start time.
     * Unknown lines are silently ignored; no normalisation of startZeit is performed.
     *
     * @throws IOException if the file cannot be read
     */
    public static SimulationsDaten leseDatei(String dateiPfad) throws IOException {
        List<String> zeilen = Files.readAllLines(Path.of(dateiPfad));

        BahnhofPlan bahnhofPlan = new BahnhofPlan();
        int startZeit = 0;

        // Wir gehen alle Zeilen durch und suchen nach den Schlüsselwörtern
        for (int i = 0; i < zeilen.size(); i++) {
            String aktuelleZeile = zeilen.get(i).trim();

            if (aktuelleZeile.equals("Strecke:")) {
                // Die nächste Zeile enthält die Bahnhöfe, getrennt durch Whitespace
                String[] bahnhoefe = zeilen.get(i + 1).split("\\s+");
                for (String name : bahnhoefe) {
                    bahnhofPlan.addBahnhof(name);
                }
            } 
            else if (aktuelleZeile.equals("Abstaende:")) {
                // Die nächste Zeile enthält die Zeiten zwischen den Bahnhöfen
                String[] abstaendeStr = zeilen.get(i + 1).trim().split("\\s+");
                int[] abstaende = new int[abstaendeStr.length];
                for (int j = 0; j < abstaendeStr.length; j++) {
                    abstaende[j] = Integer.parseInt(abstaendeStr[j]);
                }
                bahnhofPlan.setAbstaende(abstaende);
            } 
            else if (aktuelleZeile.equals("Start Hinfahrt:")) {
                // Die nächste Zeile enthält die Abfahrtszeit[cite: 3]
                startZeit = Integer.parseInt(zeilen.get(i + 1).trim());
            }
        }

        return new SimulationsDaten(bahnhofPlan, startZeit);
    }
}
    
