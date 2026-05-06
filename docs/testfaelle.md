# Testfälle

## Testfall: Einseitiges Warten – Ungerade Bahnhofsanzahl (7 Bahnhöfe)

### Eingabe

```
Strecke:
A B C D E F G

Abstaende:
5 8 6 7 3 2

Start Hinfahrt:
17
```

### Erwartete Ausgabe (Einseitiges Warten)

```
Einseitiges Warten:
An    22 31 38 46 50 53
Wa                     
Ab 17 23 32 39 47 51 54
    A  B  C  D  E  F  G
Ab 31 41 32 09 01 57 54
Wa       16            
An 46 40 15 08 00 56   
Gesamtdauer Hinfahrt, Rückfahrt       : 36, 52
Summe Wartezeiten Hinfahrt, Rückfahrt : 0, 16
Summe Strafen                         : 256
```

### Erläuterung

- 7 Bahnhöfe (ungerade Anzahl): A B C D E F G
- Abstände: 5 8 6 7 3 2 Minuten
- Mindestdauer: 5+8+6+7+3+2 + 5 (Haltezeiten) = 36 Minuten
- Bei der Strategie „Einseitiges Warten" fährt die Hinfahrt kollisionsfrei durch.
- Der Rückfahrt-Zug muss in Bahnhof C **16 Minuten** zusätzlich warten,
  bis der Gegenzug (Hinfahrt) in C eingetroffen ist und die Sicherheitswartezeit abgelaufen ist.
- Wartezeit Hinfahrt: 0 Min, Wartezeit Rückfahrt: 16 Min → Score = 16² = **256**
