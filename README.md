# THunter

THunter ist ein First-Person Hunting Game, indem der Spieler einen Jäger spielt der in einem Wald spawnt und bestimmte
Tiere abschießen muss, um die Population im Einklang zu halten.

# Teammitglieder

- Stephan Wallraven
- Tim Köhne

# Featureliste

- Prozedurale Mapgeneriereung anhand von Perlin-Noise in Chunks mit Bäumen und einigen weiteren Waldobjekten. Es werden
  nur die Chunks in Nähe des Spielers gerendert (Stephan, Tim)
- Die Bodentextur wird dynamisch anhand er Höhe des Bodens bestimmt und durch Textureblending wird ein angenehmer
  Übergang zwischen diesen erzeugt (Tim)
- Skybox mit dynamischem Tag-Nacht Wechsel und davon abhängiger Ambientebeleuchtung auf der gesamten Map (Stephan, Tim)
- Shadow Mapping für Schatten von der Sonne mithilfe einer Depthmap (Stephan, Tim)
- Cel-Shading (Toonshader) mit dynamischer Levelanzahl die durch "↑" und "↓" festgelegt werden können. Ausgeschaltet bei
  Level 0 (Standardeinstellung) (Stephan)

--- 

- Player mit First- und Thirdperson Modell abhängig von der Kameraperspektive, Schießen "LMB", Gewehrzoom "RMB" und
  Nachladen "R" mit UI Element um die Munition anzuzeigen (Stephan, Tim)
- Verschiedene Tiere mit eigenen Modellen, Bewegungsmustern, Geschwindigkeit sowie Gravitation abhängig vom Gewicht (
  Stephan, Tim)

- Drone bestehend aus mehreren einzelnen Modellen, die zusammen ein animiertes Objekt ergeben. Sie kann mit "N" an- und
  ausgeschaltet werden, hierbei bewegen sich die Arme und Rotoren in abhängigkeit von der Drone (Stephan, Tim)
- Wechsel zwischen First-Person Fly-through Kamera auf dem Charakter und einer Third-Person Orbitkamera um die Drone
  mit "C" (Tim)
- Kollisionserkennung vom Spieler, Tieren, Bäumen und Steinen anhand des Axis-Aligned-Bounding-Box Algorithmus (AABB)
  mit seperat geladenen Würfeln als Bounding-Box. Anzeige der Bounding-Box mit "P" (Stephan, Tim)

---

- UI Element zur Anzeige der Steuerung mit "X" (Stephan)
- Timer UI Element der verbleibenden Zeit anzeigt und Game-over anzeigt, falls er ausläuft (Stephan)
- Wanted-Poster UI Element zur Anzeige des Jagdfortschritts, einklappbar mit "E" (Stephan, Tim)

- Wechsel zu Wireframe anzeige zu Debugging Zwecken möglich mit "0" (Stephan, Tim)
- OpenAL Audioimplementation mit passenden Sounds für viele Aktionen (Stephan, Tim)

- ---

# Screenshots
First Person View vom Spieler
![First Person View](project/assets/Screenshots/screenshot%20first%20person.png)

Third Person View von der Drone
![Third Person View](project/assets/Screenshots/screenshot%20third%20person.png)


# Quellenverzeichnis

- Tiermodelle - https://davidoreilly.itch.io/everything-library-animals
- Bäume - https://unity-of-fantom.itch.io/low-poly-trees-pack
- Player - https://adamzhajdu.itch.io/free-low-poly-character
- Skybox - https://steamcommunity.com/sharedfiles/filedetails/?id=2092939146
- Drone - https://sketchfab.com/3d-models/drone-dccc3772dffe4daba458cf5ad75f6752
- Dronen Sound - https://freesoundstock.com/products/flying-drone-sound-effect
- Soundeffekte - https://mixkit.co/free-sound-effects/
- Font - https://www.fontspace.com/damageplan-font-f62062
- Wanted-Poster - https://clipartart.com/images/wanted-poster-clipart-transparent-8.png
- Kreuz im Wanted-Poster - https://de.depositphotos.com/50708127/stock-illustration-x-red-handwritten-letter.html
