package com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannilamarmora.project.portfolio.PortfolioService.api.contentful.model.Contentful;
import io.github.giovannilamarmora.utils.interceptors.LogInterceptor;
import io.github.giovannilamarmora.utils.interceptors.LogTimeTracker;
import io.github.giovannilamarmora.utils.interceptors.Logged;
import io.github.giovannilamarmora.utils.webClient.UtilsUriBuilder;
import io.github.giovannilamarmora.utils.webClient.WebClientRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Logged
public class ContentfulClient {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());
  private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
  private final WebClientRest webClientRest = new WebClientRest();

  @Value(value = "${rest.client.contentful.baseUrl}")
  private String contentfulURL;

  @Value(value = "${rest.client.contentful.entries}")
  private String entries;

  @Value(value = "${rest.client.contentful.authToken}")
  private String authToken;

  @Autowired private WebClient.Builder builder;

  @PostConstruct
  void init() {
    webClientRest.setBaseUrl(contentfulURL);
    webClientRest.init(builder);
  }

  @LogInterceptor(type = LogTimeTracker.ActionType.APP_EXTERNAL)
  public ResponseEntity<Contentful> getCMSData(String locale) {
    Map<String, Object> params = new HashMap<>();
    params.put("locale", locale);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
    //String res =
    //    "{\"sys\":{\"type\":\"Array\"},\"total\":11,\"skip\":0,\"limit\":100,\"items\":[{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"2rXgyPJt3lzDewpnty3j8z\",\"type\":\"Entry\",\"createdAt\":\"2023-02-17T18:49:17.054Z\",\"updatedAt\":\"2023-11-20T17:46:20.517Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":7,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"projects\"}},\"locale\":\"it-IT\"},\"fields\":{\"id\":2,\"img\":\"assets/img/project/send-email.webp\",\"titleSecondPage\":\"Email-Sender\",\"descriptionSecondPage\":[\"Spring Boot Framework\",\"Github Workflow\",\"Oracle Cloud\",\"Docker Container\",\"LogTail\"],\"btn_text\":\"Apri App\",\"btn_href\":\"https://email-sender.hostwebserver.site/email-sender\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"3JHPrhCfDwIPsHv5vTvX8E\",\"type\":\"Entry\",\"createdAt\":\"2023-02-13T11:05:49.109Z\",\"updatedAt\":\"2023-11-20T17:34:16.051Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":6,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"jobs\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Be | Shaping the Future\",\"from\":\"Da Giugno 2021\",\"to\":\"Attuale\",\"role\":\"Specialist\",\"where\":\"Milan, Italy\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"Dopo aver fatto parte del gruppo UniCredit inizialmente, ho recentemente fatto transizione verso Hype, contribuendo a progetti che coinvolgono le tecnologie delle <code>Azure Pipeline</code> e il relativo ecosistema. Durante questa esperienza, ho lavorato su applicazioni basate su <code>Spring Boot</code>, continuando a consolidare le mie competenze in questo framework.<br>\\r\\n\\r\\nIl mio ruolo attuale, di nuovo presso UniCredit, si focalizza su un progetto cruciale dedicato al login per utenti corporate. In questo contesto, mi occupo principalmente dello sviluppo utilizzando <code>Spring Boot</code> e <code>PostgreSQL</code>. Il mio impegno è rivolto a garantire un accesso sicuro e efficiente per gli utenti aziendali, contribuendo al successo complessivo del progetto.<br>\\r\\n\\r\\nNel corso della mia carriera, ho acquisito una solida competenza nella gestione della copertura del codice, eseguendo test accurati con <code>JUnit</code> e <code>Mockito</code>. Ho inoltre affinato le mie abilità nell'esecuzione di test locali e in ambiente completo tramite <code>Postman</code> e relativi ambienti.<br>\\r\\n\\r\\nLa mia esperienza abbraccia diversi ambienti di Pre-Deploy e Deploy, facendo uso di strumenti come <code>Jenkins</code>, <code>Docker</code>, e Pods di <code>OpenShift</code>. Ho lavorato con diversi database, tra cui <code>MySQL</code>, <code>PostgreSQL</code> e <code>Oracle SQL</code>, contribuendo alla solidità delle operazioni dell'applicativo.<br>\\r\\n\\r\\nAttualmente, il mio focus come Full Stack Developer è su un progetto finanziario, dove utilizzo un ampio spettro di tecnologie, tra cui <code>Java EE</code>, <code>Spring Boot</code>, <code>Angular</code>, <code>TypeScript</code>, <code>Node.js</code>, <code>PostgreSQL</code>, <code>Docker</code>, e <code>Google Cloud Platform</code>.\\r\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"work04\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"7L4IajxvnEiMLlS3DPIRk\",\"type\":\"Entry\",\"createdAt\":\"2023-02-17T19:23:14.464Z\",\"updatedAt\":\"2023-09-13T16:06:03.164Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":6,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"projects\"}},\"locale\":\"it-IT\"},\"fields\":{\"id\":3,\"img\":\"https://raw.githubusercontent.com/MoneyStats/App/main/assets/images/logos/logo.png\",\"titleSecondPage\":\"MoneyStats\",\"descriptionSecondPage\":[\"AngularJS\",\"Spring Boot Framework\",\"Github Workflow & Pages\",\"Logtail\",\"Oracle Cloud\",\"Docker Container\"],\"btn_text\":\"Apri App\",\"btn_href\":\"https://moneystats.github.io/App\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"1huWjaN06UNFIijCPhiSjz\",\"type\":\"Entry\",\"createdAt\":\"2023-02-17T18:47:00.187Z\",\"updatedAt\":\"2023-02-17T18:47:00.187Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":1,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"projects\"}},\"locale\":\"it-IT\"},\"fields\":{\"id\":1,\"img\":\"assets/img/project/GitHub-Alternatives.jpg\",\"titleSecondPage\":\"Tutti i progetti su Github\",\"btn_text\":\"Esplora Tutti\",\"btn_href\":\"https://github.com/giovannilamarmora\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"6tjXh6nwp49rSnhacpW95\",\"type\":\"Entry\",\"createdAt\":\"2023-02-13T11:08:09.640Z\",\"updatedAt\":\"2023-02-13T15:05:26.270Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":3,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"courses\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Generation Italy\",\"from\":\"Da Febbraio 2021\",\"to\":\"A Maggio 2021\",\"role\":\"Corso per Sviluppatore Java\",\"where\":\"Milan, Italy\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"Il corso online di 12 settimane, con frequenza giornaliera dalle ore 9 alle 18, organizzato da Generation Italy (Mc Kinsey) in collaborazione con <code>Intesa Sanpaolo</code>, mi ha permesso di acquisire le basi per lo sviluppo di applicazioni software e di gestione dei servizi web.<br>In particolare, abbiamo approfondito le principali tipologie di applicazioni server <code>(Jee, Mvc and Ajax)</code> e i Framework <code>Spring</code> e <code>Hibernate</code>, assieme al FrontEnd come <code>HTML5, CSS and JavaScript</code>.<br> Le settimane finali sono state dedicate ai servizi <code>Soap e Restful web services</code> e alla realizzazione di un progetto. <br> Nel contempo abbiamo lavorato perlo sviluppo di abilità comportamentali quali: <code>lavoro di squadra, adattabilità, comunicazione, persistenza, responsabilità personale, mentalità di crescita e orientamento al futuro.</code>\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"course04\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"2rCIzSMKhpd9CfX833LVJM\",\"type\":\"Entry\",\"createdAt\":\"2023-02-13T11:07:26.728Z\",\"updatedAt\":\"2023-02-13T15:05:17.940Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":3,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"courses\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Cisco Networking Academy\",\"from\":\"Da Settembre 2018\",\"to\":\"A Novembre 2019\",\"role\":\"CCNA 1, 2, 3, 4 Routing And Switching\",\"where\":\"City of Bristol College, Bristol, United Kingdom\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"<strong><h4>CCNA 1: Introduction to Networks, Punteggio: 89.5/100</h4></strong> <br>Ho correttamente completato il corso di CCNA Routing and Switching: Introduction to Networks, ed ho acquisito le seguenti capacità:<br>&bull; Corso Di introduzione alle Reti Cisco.<br>&bull; Spiegazione delle tecnologie di rete.<br>&bull; Descrizione hardware router.<br>&bull; Configurazione parametri iniziali di rete.<br><hr><strong><h4>CCNA 2: Routing and Switching Essentials, Punteggio: 89.1/100</h4></strong> <br>Ho correttamente completato il corso di CCNA Routing and Switching: Routing and Switching Essentials, ed ho acquisito le seguenti capacità:<br>&bull; Configurazione Di un dispositivo di rete.<br>&bull; Implementazione <code>VLANs</code>.<br>&bull; Implementazione <code>DHCP</code>.<br>&bull; Implementazione <code>NAT</code>.<br><hr><strong><h4>CCNA 3: Scaling Networks</h4></strong> <br>Ho correttamente completato il corso di CCNA Routing and Switching: Scaling Networks, ed ho acquisito le seguenti capacità:<br>&bull; Configurazione e risoluzione problemi STP.<br>&bull; VLAN Trunk Protocol (VTP).<br>&bull; Configurazione e risoluzione problemi <code>Cisco VLAN Trunk Protocol (VTP), STP, DTP, e RSTP</code>.<br>&bull; Configurazione <code>inter-VLAN</code>.<br>&bull; Configurazione <code>IPV4 ed IPV6</code>.<br><hr><strong><h4>CCNA 4: Advanced Networks</h4></strong><br>Ho correttamente completato il corso di CCNA Routing and Switching: Advanced Networks, ed ho acquisito le seguenti capacità:<br>&bull; Descrivere le diverse reti WAN ed il loro funzionamento.<br>&bull; Configurazione e risoluzione problemi PPP.<br>&bull; Configurazione PPPoE, GRE, and single-homed eBGP.<br>&bull; Spiegazione su come prevenire LAN security attacks.<br>&bull; Risoluzione problemi di rete da una piccola ad una media impresa di rete.<br>\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"course03\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"6eGFbB3K2DpRc7zw89Te5H\",\"type\":\"Entry\",\"createdAt\":\"2023-02-13T11:06:43.627Z\",\"updatedAt\":\"2023-02-13T15:05:07.450Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":3,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"courses\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Corso Lingua Inglese: Livello B1\",\"from\":\"Da Gennaio 2017\",\"to\":\"A Maggio 2017\",\"role\":\"Corso di Inglese Livello B1\",\"where\":\"City of Bristol College, Bristol, United Kingdom\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"Ho correttamente completato il corso di Lingua Inglese per il Livello B1.\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"course02\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"1v5rAYAR0NFeJdsb3m5UWs\",\"type\":\"Entry\",\"createdAt\":\"2023-02-10T19:27:26.551Z\",\"updatedAt\":\"2023-02-13T15:04:58.043Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":4,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"courses\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"CEH Cyber Security\",\"from\":\"From November 2017\",\"to\":\"To February 2018\",\"role\":\"Cyber Security: Introducing to the World of Cyber Security\",\"where\":\"Smart Learning, Udemy\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"Ho correttamente completato il corso di Cyber Security CEH , ed ho acquisito le seguenti capacità:<br>&bull; Che cos'è un Ethical Hacker.<br>&bull; Quali sono i principali vettori di attacco ad una rete.<br>&bull; Le fasi di un attacco informatico.<br>&bull; Come si effettua un Penetration Testing.<br>&bull; Come creare il proprio laboratorio.<br>&bull; Utilizzare le macchine virtuali.<br>&bull; Installare sistemi operativi <code>Windows e Kali Linux</code>.<br>&bull; Eseguire i principali <code>comandi per Linux</code>.<br>&bull; Creare delle mappe concettuali.<br>&bull; Capire il funzionamento base di una rete.<br>&bull; Cos'è un <code>indirizzo IP</code>.<br>&bull; Come si utilizza il <code>PING-ARP-TRACEROUTE</code>.<br>&bull; Il funzionamento di <code>NAT-DNS-DHCP</code>.<br>&bull; Eseguire operazioni di base con <code>Netcat</code>.<br>&bull; Utilizzare <code>Cisco Packet Tracer</code> per la simulazione delle reti.<br>&bull; Come funziona una switch e cosa sono le <code>VLAN</code>.<br>&bull; La differenza tra un <code>router e un firewall</code>.<br>&bull; Cosa si intende per <code>Google Hacking</code>.<br>&bull; Raccogliere informazioni sui Social Media.<br>&bull; Come si interroga un <code>DNS</code>.<br>&bull; Trovare vulnerabilità con <code>Shodan</code>.<br>&bull; Installare e configurare un <code>honeypot</code>.<br>&bull; Effettuare la scansione di una rete.<br>&bull; Effettuare lo sniffing dei pacchetti con <code>Wireshark</code>.<br>&bull; La differenza tra protocollo <code>TCP e UDP</code>.<br>&bull; Le tecniche del Banner Grabbing.<br>&bull; La cattura dei banner con <code>TELNET,NETCAT e NMAP</code>.<br>&bull; Metodi di fingerprinting attivo e passivo.<br>&bull; Effettuare un vulnerability assessment.<br>&bull; L'installazione e configurazione di <code>Nessus</code>.<br>&bull; Lanciare una scansione con <code>Nexpose</code>.<br>&bull; Differenti tipi di Exploitation.<br>&bull; Introduzione e configurazione di Metasploit.<br>&bull; Utilizzare la shell <code>METERPRETER</code>.<br>&bull; Eludere gli antivirus con Veil.<br>&bull; Come effettuare l'escalation dei privilegi.<br>&bull; Come iniettare codice malevolo.<br>&bull; Installare una backdoor su un sistema bersaglio.<br>&bull; Le tecniche di pivoting di una rete.<br>&bull; Come scrivere il report finale del Penetration Testing.<br>\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"course01\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"3OEKnGTsQqUp8Gr6gGdlQc\",\"type\":\"Entry\",\"createdAt\":\"2023-02-13T11:05:09.929Z\",\"updatedAt\":\"2023-02-13T14:19:36.814Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":4,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"jobs\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Freelance Web Developer\",\"from\":\"Da Gennaio 2017\",\"to\":\"Attuale\",\"role\":\"Graphic Web Design\",\"where\":\"Bristol, United Kingdom\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"Progettazione e manutenzione continua di siti web <code>HTML CSS e Javascript</code> con creazione di contenuti & design delle pagine web con collegamento social e test del web site con differenti Browser e formati.\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"work03\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"2VBF7CBBJ2Pb0sGOCc5CgS\",\"type\":\"Entry\",\"createdAt\":\"2023-02-10T18:21:23.816Z\",\"updatedAt\":\"2023-02-13T14:19:23.250Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":7,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"jobs\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Freelance IT Technician\",\"from\":\"Da Maggio 2012\",\"to\":\"A Gennaio 2020\",\"role\":\"Tecnico IT\",\"where\":\"Bristol, United Kingdom\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"&bull; Diagnosi e risoluzione dei problemi in modo efficiente, preservando l'integrità dei dati durante il processo. <br>&bull; Mantenere aggiornato il sistema operativo <code>Windows e Mac.</code><br>&bull; Aggiornamento dei vecchi laptop con un sistema operativo lite <code>(Linux / Chrome OS / Debian).</code><br>&bull; Upgrade Hardware di componenti come RAM o SSD.<br>&bull; Risoluzione dei problemi del sistema operativo.<br>&bull; Installazione e gestione del software <code>(Adobe, Office, VMWare, Eclipse).</code><br>&bull; Recupero dati per private e Piccole/Medie Aziende.<br>&bull; Riparazione e manutenzione di Smartphone <code>Android ed iOS.</code>\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"work02\"}},{\"metadata\":{\"tags\":[]},\"sys\":{\"space\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"Space\",\"id\":\"uptyiu46x1l9\"}},\"id\":\"LEPOcGFnINlcsFCBpnpvk\",\"type\":\"Entry\",\"createdAt\":\"2023-02-10T16:40:25.424Z\",\"updatedAt\":\"2023-02-13T14:19:11.878Z\",\"environment\":{\"sys\":{\"id\":\"master\",\"type\":\"Link\",\"linkType\":\"Environment\"}},\"revision\":10,\"contentType\":{\"sys\":{\"type\":\"Link\",\"linkType\":\"ContentType\",\"id\":\"jobs\"}},\"locale\":\"it-IT\"},\"fields\":{\"title\":\"Rock N Bowl\",\"from\":\"Da Settembre 2018\",\"to\":\"A Settembre 2019\",\"role\":\"Hostel Duty Manager\",\"where\":\"Bristol, United Kingdom\",\"descriptionValue\":{\"data\":{},\"content\":[{\"data\":{},\"content\":[{\"data\":{},\"marks\":[],\"value\":\"&bull; Responsabile delle prenotazioni.<br>&bull; Responsabile Del personale e alle ore lavorative.<br>&bull; Addetto all'assunzione del personale.<br>&bull; Responsabile d'ufficio.<br>&bull; Chiusura Giornaliera e settimanale e responsabile dei depositi in banca.<br>&bull; Gestione Prezzi dei pernottamenti.<br>&bull; Organizzazione eventi per grandi gruppi.<br>&bull; Responsabile della collaborazione con Expedia, Booking.com, Agoda.<br>&bull; Responsabile dei Social Media.\",\"nodeType\":\"text\"}],\"nodeType\":\"paragraph\"}],\"nodeType\":\"document\"},\"identifier\":\"work01\"}}]}";
//
    //Contentful test = mapper.convertValue(res, new TypeReference<Contentful>() {});
    //return ResponseEntity.ok(test);

    return webClientRest
        .perform(
            HttpMethod.GET,
            UtilsUriBuilder.toBuild().set(entries, params),
            null,
            headers,
            Contentful.class)
        .block();
  }
}
