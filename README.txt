

------------------------------------------------------------------------------------
KLEINE ANLEITUNG FÜR DAS PRGRAMM     M I K R O - Z I R K - F R E Q - A N A L Y Z E R
------------------------------------------------------------------------------------


LIZENZ

Copyright (C) 2018 Stefan Eckert 
 
 This program is free software: you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the Free Software
 Foundation, either version 3 of the License, or (at your option) any later
 version.

 This program is distributed in the hope that it will be useful, but WITHOUT ANY
 WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 PARTICULAR PURPOSE.  See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with
 this program.  If not, see <http://www.gnu.org/licenses/>.


INSTALLATION und Programmstart

1. Das Zip-Archiv entpacken, egal wo (wenn nich schon geschehen)
2. Die Datei MikroZirkFreqAnalyzer.jar starten (Unter Windows müsste das mit Doppelklick gehen, wenn auch der Wachhund bellen wird...)
3. Man kann sich dann natürlich auch eine Verknüpfung zur jar-Datei auf den Desktop legen, damit man sie später nicht immer suchen muss.


DATEN LADEN

Die Daten müssen in einem Textfile (Name.txt oder Name.asc) vorliegen, je eine Zahl in einer Zeile. Dezimaltrenner muss ein Komma sein (kein Punkt). Wenn in manchen Zeilen der Datei Müll steht, ist das nicht schlimm. Der wird automatisch durch die Vorgängerzahl ersetzt.
Wenn möglich sollte die Datemenge knapp unter einer Zweierpotenz liegen (ca. 10 kleiner). Dann arbeitet der Transformationsalgorithmus am besten.
Zweierpotenzen sind z.B. 1024, 2048, 4096, 8192, 16384, 32768 oder 65536.
Programm zum Erstelle/Bearbeiten von Textdateien: Windows hat unter der Rubrik Zubehör einen "Texteditor". Vermutlich guckst du diese Textdatei gerade damit an...

        
EINSTELLUNGEN

1. Replace Outliers
Ausreißer können ausgefiltert und ebenfalls durch die Vorgängerzahl ersetzt werden. Je kleiner die Toleranz eingestellt ist (Standard 1) desto strenger wird gefiltert. 
Wenn ein Datenbild "abgeschnitten" erscheint, einfach die Toleranz vergrößern.
Wenn ein Datenbild vorwiegend aus senkrechten Strichen besteht, Toleranz verkleinern.

2. Sampling Rate
Das ist die Frequenz, mit der die Messwerte gemacht wurden. Sie muss unbedingt stimmen, weil auf ihr die ganze Darstellung basiert.

3. Zoom transformed data to ... % of full range
Da die Frequenzen, die beim Thema Mikrozirkulation interessieren recht niedrig sind, muss man immer ans linke Ende der transformierten Daten zoomen. Hier kann man das voreinstellen. Bei einer Datenmenge von ca 65.000 sind 2% ideal.

4. Remove Bias from Data
Sollte man unbedingt machen, wenn man nicht nur die original Daten anschauen möchte. Bias ist die Verschiebung der Schwingung weg von der x-Achse. Wenn das Häkchen gesetzt ist, wird das Datenbild zur Achse heruntergezogen. Wenn man das nicht macht, kommen die niedrigen Frequenzen (langen Perioden) bei der Transformation nicht gut raus.


ZOOMEN/VERSCHIEBEN UND MARKIEREN

Im Bereich, indem die Messdaten angezeigt werden, kann man den Modus wechseln zwischen zoom/shift und mark. Markieren ist voreingestellt. Damit kann man auschnitte aus den Daten markieren und in einer neuen Datei abspeichern (save as). Auf die Transformation hat die Markierung keinen Einfluss. Im anderen Modus kann man mit dem Mouse-Rad zoomen und die Anzeige mit links-drag verschieben.


DAS PROGRAM TESTEN/AUSPROBIEREN

Die Datei simSin.txt im Programmordner enthält Daten, die mit Excel künstlich gemacht wurden. Das sind zwei überlagerte Sinuskurven. Die eine hat eine Periodendauer von 3s und eine Amplitude von 2, die andere eine Periode von 1s und eine Amplitude von 1. 
Die Exceldatei, mit der diese Daten gemacht wurden, ist auch dabei, falls jemand weiter testen bzw. spielen möchte.
