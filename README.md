# cer_gabriel_sere
## Creați un controler Rest care vă permite să gestionați următoarele operațiuni în cont:
1. [Citirea soldului]
2. [Lista tranzacțiilor]
3. [Bonific Bancar]
### Aplicația trebuie dezvoltată folosind API-urile expuse de Fabrick: https://developers.fabrick.com/hc/it
* Pentru faza de dezvoltare și testare, utilizarea versiunii SANDBOX a API-ului va fi suficientă.
* Nu este nevoie să dezvoltați o interfață pentru  utilizatorul;
* Va fi evaluat pozitiv, dar nu obligatoriu în scopul testului, scrierea pe DB și istoricizarea mișcărilor obținute cu lista tranzacțiilor.
### Acreditări și intrări
###### Exercițiul trebuie făcut folosind Sandbox cu următoarele acese:
* BaseUrl: https://sandbox.platfr.io
* Auth-Schema: S2S
* Api-Key: FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP
* Id chiave: 3202 (per uso interno, non utile al fine del test)
* accountId: 14537780
### Proprietăți/constante ale aplicației
###### {accountId} : Long, este numărul contului de referință, în API-uri este întotdeauna indicat ca {accountId}, utilizați valoarea 14537780
***
* Operatiunea: 1. Citirea soldului API: https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0
* Output: Vizualizarea soldului
***
***
* Operatiunea: 2. Bonific Bancar API: https://docs.fabrick.com/platform/apis/gbs-banking-payments-moneyTransfers-v4.0
* Output: Starea operațiunii, transferul va fi KO din cauza unei limitări a contului de testare. Rezultatul preconizat ar trebui să fie:
"code": "API000",
"description": "Errore tecnico La condizione BP049 non e' prevista per il conto id 14537780"
***
***
* Operatiunea: 3. Citirea tranzacțiilor API: https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0
* Output: Lista tranzacțiilor, la datele sugerate pe exemplu există tranzacții.
***
### Tecnologie
Testul trebuie efectuat în Java. În mod ideal, este necesară utilizarea Spring.
### Artefatto
Codul sursă al artefactului produsului trebuie publicat și partajat prin:
GitHub;
### Alte info:
Ambiente: Sandbox
Id-Chiave: 3202
### Puncte cheie
L’obiettivo è valutare la capacità di sviluppo software con particolare attenzione a:
* Structura codului;
* Abordarea problemei;
* Teste automate JUNIT;
* Tratarea erorilor
