# Onion-Router(Løk-Ruter)

Last build: ![example workflow](https://github.com/MadLadsTechnology/Onion-Router/actions/workflows/maven.yml/badge.svg)

Dette er et frivillig prosjekt tatt for å forbedre karakteren vår i emnet IDATT2104 - Nettverksprogrammering.

## Oppgaven:
"Implementer onion routing enten som programvare eller programvarebibliotek i et valgfritt programmeringsspråk"

### Vår løsning

- Utviklet i  Java.

- For kryptering brukes både RSA og AES. RSA for sending av nøkler, og AES for sending av lag-kryptert melding.
Løsningen vår har en klient samt et nettverk av noder som er holdt styr på med en Rest server (https://github.com/MadLadsTechnology/Onion-RouterRestServer)

- Klienten kan sende inn en url som viser til en API og motta et svar i form av en string.

##### Slik ser vår implementasjon ut:

1. Noder sender til serveren at de er aktive. Dersom de blir avslått blir de også fjernet fra serveren

![1](https://user-images.githubusercontent.com/70323886/159466951-373fc490-d940-4092-9fbd-faab83ef55ab.PNG)

2. Klienten gjør et kall til serveren og mottar en liste med alle aktive noder og generer en tilfeldig rute med et gitt antall noder.
 
![2](https://user-images.githubusercontent.com/70323886/159466961-8bb9378b-4c0c-40c8-8e4e-20a940ee340f.PNG)

3. Klienten sender så sin Public Key (RSA) til hver av nodene i ruten. Nodene bruker denne nøkkelen til å kryptere sin AES krypteringsnøkkel og sende den til klienten. Klienten har nå en symmetrisk nøkkel for hver node som kan brukes til kryptering.

![3](https://user-images.githubusercontent.com/70323886/159466965-15d83313-851f-474b-b868-c69bf61c4847.PNG)

4. Klienten krypterer nå adressen til API’en den vil gjøre et kall til med de symmetriske nøklene til nodene i ruten. Med hvert lag sender vi også med adressen til neste node i ruten. For hver node meldingen ankommer fjernes et lag med kryptering. Når vi ankommer siste node gjøres et api kall til den gitte adressen fra klienten.

![4](https://user-images.githubusercontent.com/70323886/159466968-e48bd0d8-6a75-4aab-9a95-8a767389c0d3.PNG)

5. For å sende svaret tilbake brukes samme rute. Hver av nodene legger nå på et lag kryptering med sin symmetriske nøkkel. Når svaret så ankommer klienten kan den dekryptere alle lagene og lese meldingen.

![5](https://user-images.githubusercontent.com/70323886/159466970-fc407c1e-92e2-4028-8a9f-5f655eb62a9a.PNG)



### Implementert funksjonalitet:
- Sende og motta meldinger som blir kryptert i et nettverk av noder
- AES kryptere meldinger
- En restServer for å holde oversikt over aktive noder
  - Altså ingen meldinger blir sendt til inaktive noder
- RSA kryptering for henting av AES nøkler fra noder
- API-kall fra siste node
- Responsen blir kryptert lagvis på vei tilbake, og dekrypteres av klienten.
- Ruten er generert tilfeldig fra et gitt antall noder fra poolen.
  - Ruten er aldri sendt ut fra klienten
  - Nodene kan kun lese neste punkt i ruten, og vet forrige punkt. 


### Svakheter og mulige utvidelser:
- Kun enkle API kall som returnerer en String
- Dårlig feilhåndtering
- Ingen brukergrensesnitt
- Ingen proxy funksjonalitet

### Eksterne Avhengigheter
- Maven
  - Importere JUnit og JSON-Simple
- JUnit
  - Brukes til testing av ulik funksjonalitet i programmet. For eksempel,  testing av kryptering- og dekrypyteringsmetodene.
- JSON-Simple
  - Brukes til å formatere json både hos klienten og rest-serveren.
- SpringBoot
  - Vi bruker en springboot rest server for å lagre alle nodene vi har tilgjengelige. Her kan vi hente ut adressene og public keys til hver node. 



## Installasjon

## Kjøring av tester



