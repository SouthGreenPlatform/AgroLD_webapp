### Cloner l'appli

- `git clone --branch dev `
-

### Deployer l'appli web

#### Pré requis

- Tomcat 7/8
- mysql (sera bientôt agnostique ou retiré)

#### Paramétrage

Le déploiement de l'application se fait premièrement avec des variables d'environnement passées à tomcat.

|             Name             |                                             Description                                              |      Valeur par défaut     |
| :--------------------------: | :--------------------------------------------------------------------------------------------------: | :------------------------: |
|        `AGROLD_NAME`         | Nom de l'archive et du contexte (the name after the base URL, for instance `https://someurl/agrold`) |           `aldp`           |
|     `AGROLD_DESCRIPTION`     |                                   Description affichée dans Tomcat                                   |             :x:            |
|       `AGROLD_BASEURL`       |                                       L'URL de base de l'app                                         |  `http://localhost:8080/`   |
|   `AGROLD_SPARQL_ENDPOINT`   |                                      Url de l'endpoint SPARQL                                        | `http://sparql.southgreen.fr` |
|  `AGROLD_DB_CONNECTION_URL`  |                        Url de la base de données ex: `[host]:[port]/[db]?[opt]`                      |         :x: (requis)       |
|     `AGROLD_DB_USERNAME`     |                                   Utilisateur de la base de données                                  |         :x: (requis)       |
|     `AGROLD_DB_PASSWORD`     |                                  Mot de passe de la base de données                                  |         :x: (requis)       |


Pour injecter ces variables d'environnement dans tomcat il faut mettre ceci dans `$CATALINA_BASE/bin/setenv.sh`

```bash
#Tomcat can't read env variables but can read system properties
OLDIFS=$IFS
IFS='
'
#Catalina.sh uses sh, however it does not understands setting $'\n' as an IFS because he understands that it is litterally a n, not a newline
# if someone finds a cleaner way fell free to make a PR

var="$(env | grep '^AGROLD_')"

for i in $var; do
    OPT="$OPT -D$(echo "$i" | awk -F '=' '/^AGROLD/ { lwr=tolower($1); sub(/_/, ".", lwr); print lwr }')='$(echo "$i"| cut -d= -f2-)'"
done

IFS=$OLDIFS
echo "Launching Tomcat with options: $OPT" | systemd-cat

export CATALINA_OPTS="$OPT"
```

Grâce à ce script les variables d'environnement seront disponibles sous ces formes respectivement

* ``agrold.name``
* ``agrold.description``
* ``agrold.baseurl``
* ``agrold.sparql_endpoint``
* ``agrold.db_connection_url``
* ``agrold.db_username``
* ``agrold.db_password``

Vous pourrez donc utiliser vos variables de cette manière 

```java
...
String URL = System.getProperty("agrold.db_connection_url");
String usr = System.getProperty("agrold.db_username");
String pwd = System.getProperty("agrold.db_password");
...
```

#### Compilation

```bash
# Avec AGROLD_NAME définie précédemment
mvn clean install
```

Si vous lancez tomcat avec systemd le remplissage des variables d'environnement se fait ainsi

```bash
sudo systemctl edit tomcat8 #ou tomcat7

# dans l'éditeur
[Service]
Environment="AGROLD_NAME=aldp"
Environment="AGROLD_DESCRIPTION=Agrold (Development instance)"
Environment="AGROLD_BASEURL=http://localhost:8080/"
Environment="AGROLD_SPARQL_ENDPOINT=http://sparql.southgreen.fr"
Environment="AGROLD_DB_CONNECTION_URL=172.17.0.1:3306/DB?useSSL=false"
Environment="AGROLD_DB_PASSWORD=my-secret-pw"
Environment="AGROLD_DB_USERNAME=my-user"
```

### Sauvegarde des activités

- Le fichier agrold-javaweb/agrold-api.json est la version de base de la spécification Swagger qui décrit les webservices et qui est fourni en ligne par le webservice GET /api/webservice.
  Ce fichier est placé hors de l'application pour éviter que les modifications de l'application ne supprime les nouveaux services créés à partir de l'interface HTTP
- La sauvegarde de l'historique des activités est inaccessible pour l'instant car les boutons de login et d'enregistrement sont désactivés
- les informations de connexion à MySql sont dans le fichier `/home/virtuoso/agrold-mysql.conf`
  sur le serveur pour éviter de les partager avec le code sur github. Il s'agit de l'url de mysql, du nom et pwd de l'admin de la BD.
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