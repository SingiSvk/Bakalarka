# Bakalarka
 
# Inštalácia

Z priečinku apk treba stiahnuť a vložiť do telefónu súbor app-debug.apk. Následne stačí tento súbor v mobile spustiť a nainštalovať program (treba povoliť inštaláciu z neznámych zdrojov)

# Používanie

Po spustení aplikácie je potrebné pridať senzory (funkčné sú zariadenia s ID 1,2,3, ale občas nefungujú). Po pripojení sa začne prijímanie dát a po 2 minútach prídu prvé dáta.

 V aplikácií je možné zobraziť všetky izby alebo len tie v ktorých je riziko. V zobrazení izby sa zobrazujú základné informácie o pacientovi, 4 grafy pre rôzne veličiny a posledné udalosti. Pri grafoch je lupa, ktorá po kliknutí zobrazí graf vývoja danej veličiny v závislosti od času. Pri každej izbe je možné nastavovať názov, informácie o pacientovi a rizikové hranice alebo izbu odstrániť pomocou ikoniek vpravo hore. Aplikácia má prednastavené základne rizikové hranice. 
  
  Pridávanie zariadenia je realizované pomocou QR kódu (priečinok "QR codes") alebo manuálne. Pri pridávaní zariadenia je možné zadať meno, vek pacienta a názov izby, ale nie je to povinné. Po pridaní zariadenia začne aplikácia prijímať dáta zo zariadení. Po prijatí aplikácia vyhodnotí situáciu na základe nastavených rizikových hraníc a dáta zobrazí v prehľade izby. Pokiaľ aplikácia situáciu vyhodnotí ako rizikovú, vytvorí notifikáciu ktorá sa zobrazí v zariadení. Po kliknutí na notifikáciu sa v aplikácií zobrazí daná izba. Aplikácia na základe týchto upozornení ukladá udalosti ktoré sa zobrazujú pri každej izbe samostatne alebo na úvodnej obrazovke spolu.
