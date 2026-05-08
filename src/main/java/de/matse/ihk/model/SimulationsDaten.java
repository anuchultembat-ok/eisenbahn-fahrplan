package de.matse.ihk.model;

/**
 * Unveränderlicher Behälter, der einen eingelesenen {@link BahnhofPlan} mit der
 * Abfahrtszeit der Hinfahrt bündelt. Wird von {@code EingabeLeser} zurückgegeben
 * und an die Strategien weitergereicht.
 */
public record SimulationsDaten(BahnhofPlan plan, int startZeit) {
}
