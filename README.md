# Onion-Router(Løk-Ruter)

Last build: ![example workflow](https://github.com/MadLadsTechnology/Onion-Router/actions/workflows/maven.yml/badge.svg)

Dette er et frivillig prosjekt tatt for å forbedre karakteren vår i emnet IDATT2104 - Nettverksprogrammering.

## Oppgaven:
"Implementer onion routing enten som programvare eller programvarebibliotek i et valgfritt programmeringsspråk"

### Vår løsning
Dette har vi løst i programmeringspråket java. 

Løsningen vår har en klient samt et nettverk av noder som er holdt styr på med en Rest server (https://github.com/MadLadsTechnology/Onion-RouterRestServer)

Klienten kan sende inn en url til en API og motta et svar i form an en String.

### Funksjonalitet:
- Sende og motta meldinger som blir kryptert i et nettverka av noder
- En restServer for å holde oversikt over aktive noder
  - Altså ingen meldinger blir sendt til inaktive noder
- RSA kryptering for henting av AES nøkler fra noder

### Svakheter:
- Kun enkle API kall som returnerer en string
- Dårlig feilhåndtering
- Ingen brukergrensesnitt

## Installasjon

## Kjøring av tester



