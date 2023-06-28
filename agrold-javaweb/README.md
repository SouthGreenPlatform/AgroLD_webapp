### Cloner l'appli

- `git clone --branch dev `
-

### Deployer l'appli web

#### Pré requis

- Tomcat 7/8
- mysql (sera bientôt agnostique ou retiré)

#### Paramètres

Le déploiement de l'application se fait premièrement avec des propriétés Java passées à tomcat.

|            Name            |                                             Description                                              |       Valeur par défaut       |
|:--------------------------:|:----------------------------------------------------------------------------------------------------:|:-----------------------------:|
|       `agrold.name`        | Nom de l'archive et du contexte (the name after the base URL, for instance `https://someurl/agrold`) |            `aldp`             |
|    `agrold.description`    |                                   Description affichée dans Tomcat                                   |              :x:              |
|      `agrold.baseurl`      |                                        L'URL de base de l'app                                        |   `http://localhost:8080/`    |
|  `agrold.sparql_endpoint`  |                                       Url de l'endpoint SPARQL                                       | `http://sparql.southgreen.fr` |
| `agrold.db_connection_url` |                       Url de la base de données ex: `[host]:[port]/[db]?[opt]`                       |         :x: (requis)          |
|    `agrold.db_username`    |                                  Utilisateur de la base de données                                   |         :x: (requis)          |
|    `agrold.db_password`    |                                  Mot de passe de la base de données                                  |         :x: (requis)          |


Pour injecter ces variables dans tomcat, il faut déclarer les sous forme d'argument en ligne de commande sous cette forme `-Dnomdelapropriété=valeur` et les placer dans la variable d'environnement `CATALINA_OPTS` 

> :warning: Non ce n'est pas une faute de frappe le `-D` est bien collé au nom de la propriété, c'est les arguments de java

Par exemple: 

```bash
# Directement sur l'hôte
export CATALINA_OPTS="-Dagrold.db_connection_url=someurlhere -Dagrold.db_username=usr -Dagrold.db_password=pwd" 
catalina.sh
```

```java
...
String URL = System.getProperty("agrold.db_connection_url");
String usr = System.getProperty("agrold.db_username");
String pwd = System.getProperty("agrold.db_password");
...
```


Si vous lancez tomcat avec systemd le remplissage des variables d'environnement se fait ainsi

```bash
sudo systemctl edit tomcat8 #ou tomcat7

# dans l'éditeur
[Service]
Environment="CATALINA_OPTS='-Dagrold.db_connection_url=someurlhere -Dagrold.db_username=usr -Dagrold.db_password=pwd'"
```

Pour compiler: la variable d'environnement `AGROLD_NAME`, égale à `agrold.name` doit être mise en place 

```bash
# Avec AGROLD_NAME définie précédemment
mvn clean install
```

Si vous utilisez docker:
```bash
cd agrold-javaweb/

# Vous pouvez utiliser le dockerfile
#                          le mot de passe du manager 
docker build . -t <tag> --build-arg TOMCAT_PASSWORD=a --build-arg AGROLD_NAME=agrolddev

# Ou pull l'image stockée depuis le registre dans l'environnement de développement,
docker login 10.9.2.21:8080 -u <user> -p <password>
docker pull 10.9.2.21:8080

# Lancer le conteneur
docker run -p 8080:8080 -e CATALINA_OPTS="-Dagrold.db_connection_url=someurl -Dagrold.db_username=usr -Dagrold.db_password=pwd -Dagrold.baseurl=http://localhost:8080/ -Dagrold.sparql_endpoint=a" <tag>
```

### Sauvegarde des activités

- Le fichier agrold-javaweb/ressources/agrold-api.json est la version de base de la spécification Swagger qui décrit les webservices et qui est fourni en ligne par le webservice GET /api/webservice.
  Ce fichier est placé hors de l'application pour éviter que les modifications de l'application ne supprime les nouveaux services créés à partir de l'interface HTTP
- La sauvegarde de l'historique des activités est inaccessible pour l'instant car les boutons de login et d'enregistrement sont désactivés
- pour recréer un utilisateur: supprimer les lignes correspondant à son adresse email dans les tables `user`, `u_info`, `h_settings` de la BD `agrold`:

```sql
delete from u_info where mail="tagny@ymail.com";
delete from user where mail="tagny@ymail.com";
delete from h_settings where mail="tagny@ymail.com";
delete from h_visited_page where visitor="tagny@ymail.com";
delete from h_sparql_editor where mail="tagny@ymail.com";
delete from h_quick_search where mail="tagny@ymail.com";
delete from h_advanced_search where mail="tagny@ymail.com";
```

### Services externes potentiels

- https://www.ebi.ac.uk/proteins/api/doc

### Issues (ou _to fix_)

_italic_: peut-être résolu

**gras**: résolu

#### En général

- implémenter des test unitaires pour vérifier que les màj ne crèent pas d'erreur
- configurer le Tomcat de Volvestre dans Netbeans pour directement déployer la version dev. en ligne

#### Advanced Search

- le fichier `agrold-api.json` est téléchargé plusieurs fois ???
- _corriger la requête sparql du service describe et celle des genesByPathwayIds_
- Laisser l'utilisateur spécifier les paramètres `page`et `pageSize`?
- _Les tables YASR apparaissent souvent aminçis dans les fenêtres de description d'entités_
- _La navigation entre page ne marche pas ("Next page")_: quand on fait un _display_ puis on revient aux résultats de la recherche
- _les requêtes sur les QTL retournent peu de résultats_
- **advancedsearch.jsp incompatible avec includes.jsp -> redondance dans les imports CSS**
- gérer les erreur HTTP (eg. 204, 404, 500, etc.) de l'API dans l'advancedSearch (màj de Swagger-client dans tous les appel aux web services)
- Recherche des QTL: la seul description fournie est le label et has*trait (très souvent une URI TO*... dans le graphe gramene.qtl, mais texte dans qtaro.qtl). Donc il est difficile de les retrouver par mot-clé
- **Corriger la requête de recherche pour trouver plus de QTL y compris ceux de qtaro.qtl (voir sparql query du web service getQTLs i.e. `rdf:type|rdfs:subclassOf)**
- Corriger la recherche des QTL par mot clé (la requête BNL6.32 ne retourne rien)
- Permettre de pouvoir passer les paramètres par GET (url)
- View as Graph: en faire un point à partir duquel la KB peut être explorée
- View as Graph with kenetmaps.js: ajouter des types spécifiques à AgroLD comme chromosome, CDS, ou mRNA (le css décrit le style des types d'entités supportés)
- **knetmaps: ajout d'un nouveau type** (concept, e.g. mRNA) = ajout du style du type, ajout du style de ses relations avec les autres concept, et ajout de l'image de son symbole dans le css et le dossier img de knetmaps
- Interaction AgroLD <> KnetMaps.JS: voir knetmaps.js ligne 309 (Pour accéder à un attribut de données d'un noeud : console.log("my.showLinks.selectedNode: " + this.data("iri"));)

```sparql
BASE <http://www.southgreen.fr/agrold/>
PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>

SELECT distinct ?ts ?p ?to
WHERE {
    ?s ?p ?o .
  	?s a ?ts .
  	?o a ?to .
}
ORDER BY ?p
```

#### Exemple

- Elliminer les exemples qui ne marchent pas
- rendre les exemples cliquables pour éviter l'effort des copier-coller

#### Historique utilisateur

- Problème de connexion admin à la BD
- Détection d'une duplication de clé à la création d'un nouvel utilisateur

#### Web services

- gestion de web services: ajouter les nouveaux tags, ajouter automatiquement le tag customizable
- GUI de gestion des création,modif, suppr de services web: indexer les sparql, url, et paramètres de services dans un xml. une seule implémentation en java. L'url d'un service particulier est un paramètre dans l'adresse comme le format. la description dans le json pour swagger se fait en fixant les paramètres in URL
- problème d'encodage dans l'affichage des publications
- Certains services ne retournent que le format .json (il faut convertir la sortie pour d'autres formats)
- L'implémentation des services n'est pas documentée sur Swagger (code SPARQL, sources externes des données, etc.)
- Il n'y a pas de cache
- Il n'y a pas de gestion explicite des erreurs avec les codes HTTP
- Ajouter un web service `getPathways`
- Ajouter un web service `getGenesPublicationsById`: les reférences à pubmed semblent n'être liées qu'aux entités
  de type `http://www.southgreen.fr/agrold/vocabulary/mRNA` qui ne sont pas explicitement des gènes (`http://www.southgreen.fr/agrold/vocabulary/Gene`), mais sont liées
  aux gènes par la propriété `http://www.southgreen.fr/agrold/vocabulary/develops_from` et `http://semanticscience.org/resource/SIO_010081`
  (des propriétés peut-être équivalentes?)

```sparql
PREFIX uri:<http://identifiers.org/rapdb.mrna/Os05t0125000-01>
SELECT *
WHERE {
  graph ?g{
  { uri: ?property ?hasValue }
  UNION
  { ?isValueOf ?property uri:}
  }
}
```

#### sparql editor

- Ajouter un bouton pour ajouter les préfixes
- **D3SPARQL dans YASR**
- le plein écran de YASQE et YASR est caché par la barre de menu
- le graphe des résultats n'est pas téléchargeable

```sparql
BASE <http://www.southgreen.fr/agrold/>
PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>
PREFIX uniprot:<http://purl.uniprot.org/uniprot/>
SELECT ?property ?hasValue
WHERE {
values (?q){(uniprot:P0C127)}
  { ?q ?property ?hasValue }
}
```

```sparql
PREFIX uri:<http://identifiers.org/ensembl.plant/AT2G19710>
SELECT ?property ?hasValue
WHERE {
  { uri: ?property ?hasValue }
}
```

#### Quick search

- **le _describe_ des URI ne fonctionne pas à cause de la redirection des URL de l'appli**

Accessions
= échantillon parenté (espèce)
= identifiant d'un gène (KB)