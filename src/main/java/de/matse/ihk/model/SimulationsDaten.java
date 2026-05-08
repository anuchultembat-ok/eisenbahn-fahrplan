package de.matse.ihk.model;

/**
 * Immutable container that bundles a parsed {@link BahnhofPlan} with the forward-journey
 * departure time. Returned by {@code EingabeLeser} and passed down to the strategies.
 */
public record SimulationsDaten(BahnhofPlan plan, int startZeit) {
}
