#!/usr/bin/env bash
# Eisenbahn-Fahrplan Simulation -- alle Testfälle ausführen
# Voraussetzungen: Java 17+, Apache Maven 3.6+
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "======================================"
echo " Eisenbahn-Fahrplan Simulation"
echo " Automatische Testausführung"
echo "======================================"
echo ""

# 1. Kompilieren und JAR erzeugen
echo "[1/3] Kompiliere mit Maven..."
mvn package -q -DskipTests
echo "      -> target/eisenbahn-fahrplan-1.0-SNAPSHOT.jar erstellt"
echo ""

# 2. Alle Testfälle ausführen
TESTDIR="${1:-testfaelle}"
echo "[2/3] Verarbeite Testdateien in: $TESTDIR"
java -cp target/eisenbahn-fahrplan-1.0-SNAPSHOT.jar de.matse.ihk.Main "$TESTDIR"
echo ""

# 3. Ergebnisse anzeigen
echo "[3/3] Ergebnisdateien in: ausgabe/"
ls -1 ausgabe/ | grep "_ergebnis.txt" | grep -v "_ergebnis_ergebnis" | sort
echo ""
echo "Fertig. Alle Ergebnisse wurden in ausgabe/ gespeichert."
