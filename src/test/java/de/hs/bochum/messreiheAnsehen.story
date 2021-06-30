EmuMessdatenVerwaltung story
Narrative:
In order to look at messreihe
As a laboringenie
I want to start a messreihe to get messungen


Scenario: Starten einer Messreihe ohne Messungen
Given The current size of messung with <id>, <intervall>, <consumer>, <kind> is 0
When I create messungen for <id> and messreihen are <messungen>
Then I should see <size> messungen for element with <id>

Examples:
|id|messungen|intervall|consumer|kind|size
|1||15|Laptop|Leistung|0|
|2|10|20|Ladekabel|Leistung|1|
|3|10,13|30|Laptop|Arbeit|2|

Scenario: Starten einer Messreihe mit Messungen
Given there is a messung with the id 1 already available
When I create messungen with the id 1
Then the a error message appears, saying: Id already present
