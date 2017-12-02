# bajtahack HOTELKO

Projekt hotelko je sestavljen iz: web odjemalca, strežnika, baze MySql in srm modulov. Za kar največjo prožnost in sigurnost delovanja smo izbrali javanski strežnik (multithreading by default), ter programski jezik Java za strežnik.

Razlog za izbiro Jave večplastna. Predvsem dobra obavnava izjem (res nočete, da se centralni sistem pametne hiše razsuje), dobra podpora za večnitnost, strukturirano logiranje in vse kar potrebujemo za enterprise-grade sistem, prevajanje in strong-typing. Navsezadnje pa tudi izredno bogat nabor knjižnic in orodij za razvijanje, ter platformna neodvisnost.    

## Namestitev sistema

Namestili bomo javo, maven (orodje za upravljanje z java projekti, iz interneta naloži vse knjižnice in jih zapakira v javanske arhive) in jboss. Potrebujemo tudi orodje portecle.jar za izdelavo keystora (java shrambe za digitalno potrdilo).

Projekt uporablja tudi bazo, uporabili bomo mysql. Potrebujemo java 1.8 (Lahko tudi nižjo ali višjo verzijo), vendar compiler compliance level 1.7. Naložite iz tukaj:

1. [java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. [Maven](https://maven.apache.org/download.cgi)
3. [jboss / wildfly community edition 11](http://wildfly.org/downloads/)
4. [MySql Java Connector](https://dev.mysql.com/downloads/mysql/)

## Postopek namestitve jave

Java je exe datoteke. Namestite na znano lokacijo, in takoj zatem kreirajte "Environment variable" JAVA_HOME, ki naj kaže na krovni direktorij, kjer je java. Popravite tudi sistemsko variablo PATH, naj vsebuje lokacijo JAVA_HOME/bin.

Preverite namestitev jave z ukazom v ukaznem pozivu:

    java -version

Mora izpisati verzijo jave, ki ste jo namestili. Preverite še variablo JAVA_HOME in PATH. 

    set J
    
Mora izpisati pravilno lokacijo, kjer je nameščena java.

    set P
    
Pa mora izpisati variablo PATH, kjer mora biti JAVA_HOME/bin.         

## Postopek namestitve Maven

Orodje Maven prenesite v ZIP obliki. Razpakirajte ga. Nato v sistemsko variablo PATH dodajte pot na MAVEN_INSTALL/bin direktorij. 

Preverite namestitev v ukaznem pozivu

    mvn -v

Izpisati mora verzijo programa in tako dalje. Maven bo kopiral vse knjižnice, ki jih referenirajo projekti v USER_HOME/.m2 direktorij.

## Postopek namestitve Jboss

Uporabili bomo Jboss / Wildfly strežnik. Potegnimo dol ZIP in ga razpakirajmo na ustrezno mesto. Nato bo potrebno skonfigurirati 2 modula (interni knjižnici) : SSL in MySQl ter še povezavo na bazo. 

Projekt ne potrebuje tega strežnika, lahko je katerikoli java strežnik (Tomcat, Jetty). Uporabljamo pa tega, ker ima zelo dobro urejeno konfiguracijo in nadzor nad aplikacijami.   

Naš centralni modul je zgrajen kot SinglePage Web Aplikacija, storitveni del pa teče kot RESTAPI na strežniku. Aplikaciji sta ločena v celoti. V resni produkciji, bi UI del aplikacije živel na nekem HTTP strežiku, nakar bi se zahteve za dostop do RESTFUL API-ja proxirale na jboss. Temu lahko rečemo razdelitev namestitve v statično in aplikacijsko domeno. Statična teče na nekem HTTP strežniku, kjer imamo nastavljeno vse za maksimalne performance serviranja. Aplikacijska domena pa se ukvarja le z procesiranjem API zahtev, dostopom do baze, ipd. 

----------------             ----------------              ----------------
|              |   TCP/80    |              |              |              |
| web client   |   ---- >>   | APACHE       |              | JBOSS        |
|              |             |              |   TCP/8080   |              |
----------------             | REV. PROXY   |  ---->>>>    ----------------
                            ---------------- 

Za razvojne namene lahko koristimo jboss za obe vlogi. Jboss direktorij welcome-content uporabimo za web projekt, nameščen WAR na jboss-u pa kot storitveni RESTFUL del. Vse skupaj teče na portu 8080, tako da ne bomo imeli težav glede XHR requestov v brskalniku. Web projekt bo na lokaciji /, RESTFUL API pa na /bajtahack. 

Tak način uporabe je pogost v razvoju projektov.


## Konfiguracija jboss strežnika

### SSL konfiguracija 

JBOSS ima vse sistemske knižnice zawrapane v t.i. module. Tudi celotni rt.jar (runtime java jar) vidi kot modul. Ves JDK seveda ni videm, manjka tudi implementacijski del za SSL. Zato je potrebno dodati sledeče: 

Odprite datoteko : JBOSS_HOME\modules\system\layers\base\sun\jdk\main\module.xml. Dodajte sledeče znotraj //module/dependencies/system/paths:

    <path name="com/sun/ssl/internal/ssl"/>
    <path name="com/sun/net/ssl"/>

Sedaj bo jboss videl tudi SSL implementacijske razrede iz java rt.jar-a. Razlog za takšno modularizacijo je v temu, da lahko različne instance jboss-a vidijo in dostopajo do knjižnic različno; predvsem je tukaj pomembna varnost in performance. 

### Datasources konfiguracija

Po prevzetih nastavitvah, jboss nima nobenega sriverja JDBC za dostop do baze. Ima pa svoj visokoperformančni, vedno razpoložljivi Connection Pool (komponenta, ki ima vedno na voljo več odprth in nadzorovanih povezav do baze; tipično 4-150). To je zelo pomembno za večnitno okolje kakršno je sevlet container, kjer je vedno več hkratnih zahtev. Datasources (oz. Connection Pool) je zelo kritična komponenta v okolju, kjer se podatki ne smejo izgubiti.

Najprej bomo dodali JAR kjer je driver za mysql. To naredimo takole:

1. naredimo direktorij  JBOSS_HOME\modules\system\layers\base\com\mysql\
2. naredimo poddirektorij v njemu  JBOSS_HOME\modules\system\layers\base\com\mysql\main
3. V poddirektorij dodamo jar z mysql java connectorjem in datoteko module.xml z sledečo vsebino

    <?xml version="1.0" encoding="UTF-8"?>
    <module xmlns="urn:jboss:module:1.1" name="com.mysql">
        <resources>
            <resource-root path="mysql-connector-java-5.1.44-bin.jar"/>
        </resources>
        <dependencies>
            <module name="javax.api"/>
        </dependencies>
    </module>
4. Sedaj dodamo datasource in driver nastavitve v JBOSS_HOME\standalone\configuration\standalone.xml v sekcijo urn:jboss:domain:datasources:5.0. Driver definiramo v sekciji `<drivers>`


    <driver name="mysql" module="com.mysql">
        <driver-class>com.mysql.jdbc.Driver</driver-class>
    </driver>

Nato dodamo še konfiguracijo resource poola, ki ga bomo nato klicali v Java kodi:

    <datasource jndi-name="java:jboss/datasources/bajtahack" pool-name="bajtahack" enabled="true" use-java-context="true">
        <connection-url>jdbc:mysql://localhost/bajtahack</connection-url>
        <driver>mysql</driver>
        <pool>
            <min-pool-size>5</min-pool-size>
            <max-pool-size>10</max-pool-size>
            <prefill>false</prefill>
        </pool>
        <security>
            <user-name>bajtahack</user-name>
            <password>bajtahack</password>
        </security>
    </datasource>

Sedaj je vse pripravljeno.

### preveri jboss

S tem je nastavitev končana. Zaženite jboss in preverite v konzoli, ali se je datasource zagnal:

INFO  [org.jboss.as.connector.subsystems.datasources] (ServerService Thread Pool -- 36) WFLYJCA0005: Deploying non-JDBC-compliant driver class com.mysql.jdbc.Driver (version 5.1)

### Optimizacija jboss strežnika

Za optimalne performance in čim manjšo porabo pomnilnika svetujemo, da v konfiguraciji jboss izklopite vse module, ki jih ne potrebujete za naš projekt.

Končna predlagana konfiguracija je v datoteki standalone.xml. Enostavno prepišite obstoječo konfiguracijo. 

    config\standalone.xml

Takšna prilagojena instanca zasede komaj nekaj več kot 100 MB RAM, kar je izredno malo.


## Konfiguracija baze podatkov

Lahko uporabljamo katerokoli verzijo MySql ali MariaDB. Kreirajte bazo in poženite skripto, ki naredi uporabnika:

    CREATE DATABASE bajtahack CHARACTER SET utf8 COLLATE utf8_bin;
    CREATE USER 'bajtahack'@'localhost' IDENTIFIED BY 'bajtahack';
    GRANT ALL PRIVILEGES ON bajtahack.* TO 'bajtahack'@'localhost';
    flush privileges;

In tabelo:

    CREATE TABLE `devicestate` (
        `id` INT(11) NOT NULL AUTO_INCREMENT,
        `device` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8_bin',
        `service` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8_bin',
        `dtype` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8_bin',
        `dvalue` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8_bin',
        `ddate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`id`)
    );

Preverite povezavo na bazo

# Izgradnja in namestitev projekta

Projekt prevedemo z

    mvn package
    
V direktoriju /target dobimo WAR arhiv za namestitev na strežnik. Vržemo ga preprosto v JBOSS direktorij, /standalone/deployments. Sam od sebe se bo namestiv.


# Zagon projekta

Odpremo spletno mesto [](http://localhost:8080/bajtahack/) 

# Ostalo

Za pripravo skriptov za srm module, je na voljo program webscripter. Odpremo ga z:

    webscripter/index.html    
    







