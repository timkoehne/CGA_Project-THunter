# THunter

THunter ist ein First-Person Hunting Game, indem der Spieler einen Jäger spielt der in einem Wald spawnt und bestimmte
Tiere abschießen muss, um die Population im Einklang zu halten.

# Teammitglieder
- Stephan Wallraven
- Tim Köhne

# Featureliste
- Prozedurale Mapgeneriereung anhand von Perlin-Noise in Chunks mit Bäumen und einigen weiteren Waldobjekten. Es werden
  nur die Chunks in Nähe des Spielers gerendert
- Die Bodentextur wird dynamisch anhand er Höhe des Bodens bestimmt und durch Textureblending wird ein angenehmer
  Übergang zwischen diesen erzeugt
- Skybox mit dynamischem Tag-Nacht Wechsel und davon abhängiger Ambientebeleuchtung auf der gesamten Map
- Shadow Mapping für Schatten von der Sonne mithilfe einer Depthmap
- Cel-Shading (Toonshader) mit dynamischer Levelanzahl die durch "↑" und "↓" festgelegt werden können. Ausgeschaltet bei
  Level 0 (Standardeinstellung).
--- 

- Player mit First- und Thirdperson Modell abhängig von der Kameraperspektive, Schießen "LMB", Gewehrzoom "RMB" und
  Nachladen "R" mit UI Element um die Munition anzuzeigen
- Verschiedene Tiere mit eigenen Modellen, Bewegungsmustern, Geschwindigkeit sowie Gravitation abhängig vom Gewicht

- Drone bestehend aus mehreren einzelnen Modellen, die zusammen ein animiertes Objekt ergeben. Sie kann mit "N" an- und
  ausgeschaltet werden, hierbei bewegen sich die Arme und Rotoren in abhängigkeit von der Drone
- Wechsel zwischen First-Person Fly-through Kamera auf dem Charakter und einer Third-Person Orbitkamera um die Drone
  mit "C"
- Kollisionserkennung vom Spieler, Tieren, Bäumen und Steinen anhand des Axis-Aligned-Bounding-Box Algorithmus (AABB)
  mit seperat geladenen Würfeln als Bounding-Box. Anzeige der Bounding-Box mit "P"

---
- UI Element zur Anzeige der Steuerung mit "X"
- Timer UI Element der verbleibenden Zeit anzeigt und Game-over anzeigt, falls er ausläuft
- Wanted-Poster UI Element zur Anzeige des Jagdfortschritts, einklappbar mit "E"

- Wechsel zu Wireframe anzeige zu Debugging Zwecken möglich mit "0"
- OpenAL Audioimplementation mit passenden Sounds für viele Aktionen
- ---

# Quellenverzeichnis

- Tiermodelle - https://davidoreilly.itch.io/everything-library-animals
- Bäume - https://unity-of-fantom.itch.io/low-poly-trees-pack
- Player - https://adamzhajdu.itch.io/free-low-poly-character
- Drone - https://sketchfab.com/3d-models/drone-dccc3772dffe4daba458cf5ad75f6752
- Dronen Sound - https://freesoundstock.com/products/flying-drone-sound-effect
- Soundeffekte - https://mixkit.co/free-sound-effects/
- Font - https://www.fontspace.com/damageplan-font-f62062
- Wanted-Poster - https://clipartart.com/images/wanted-poster-clipart-transparent-8.png
- Kreuz im Wanted-Poster - https://de.depositphotos.com/50708127/stock-illustration-x-red-handwritten-letter.html
