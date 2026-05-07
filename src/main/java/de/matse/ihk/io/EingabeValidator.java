package de.matse.ihk.io;

import de.matse.ihk.model.BahnhofNode;
import de.matse.ihk.model.BahnhofPlan;
import de.matse.ihk.model.SimulationsDaten;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates a parsed {@link SimulationsDaten} object before the strategies run.
 * Returns a list of error messages; an empty list means the data is valid.
 */
public class EingabeValidator {

    /**
     * Runs all validation checks and returns every violation found.
     * Callers should print the list and skip the file when it is non-empty.
     */
    public static List<String> validiere(SimulationsDaten daten) {
        List<String> fehler = new ArrayList<>();
        validiereStartZeit(daten.startZeit(), fehler);
        validierePlan(daten.plan(), fehler);
        return fehler;
    }

    /** Convenience wrapper — returns true only when {@link #validiere} yields no errors. */
    public static boolean istGueltig(SimulationsDaten daten) {
        return validiere(daten).isEmpty();
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private static void validiereStartZeit(int startZeit, List<String> fehler) {
        if (startZeit < 0 || startZeit > 59)
            fehler.add("Startzeit " + startZeit + " ungültig — erlaubter Bereich: 0..59");
    }

    private static void validierePlan(BahnhofPlan plan, List<String> fehler) {
        if (plan.getGroesse() < 2) {
            fehler.add("Mindestens 2 Stationen erforderlich (gefunden: " + plan.getGroesse() + ")");
            return;
        }

        Set<String> gesehen = new HashSet<>();
        BahnhofNode cur = plan.getHead();
        int idx = 0;

        while (cur != null) {
            String name = cur.getName();
            if (name == null || name.isBlank()) {
                fehler.add("Stationsname an Position " + idx + " ist leer");
            } else if (!gesehen.add(name)) {
                fehler.add("Stationsname '" + name + "' kommt mehrfach vor");
            }

            if (cur.getNext() != null) {
                int d = cur.getDistanceToNext();
                if (d <= 0)
                    fehler.add("Abstand " + idx + " (" + d + " min) muss mindestens 1 sein");
                else if (d >= 30)
                    fehler.add("Abstand " + idx + " (" + d + " min) muss kleiner als 30 sein"
                            + " — bei Abstand >= 30 existiert keine kollisionsfreie Lösung");
            }

            cur = cur.getNext();
            idx++;
        }
    }
}
