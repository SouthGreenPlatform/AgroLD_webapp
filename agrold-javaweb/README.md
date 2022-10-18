### Cloner l'appli
*  `git clone --branch dev `
*   

### Deployer l'appli web

#### Pré requis
* Tomcat 7/8
* mysql (sera bientôt agnostique ou retiré)

#### Paramétrage

Le déploiement de l'application se fait premièrement avec des variables d'environnement passées à tomcat.

|Name|Description|valeur par défaut|
|:--:|:--:|:--:|
|``AGROLD_NAME``|Nom de l'archive et du contexte|'agrold'|
|``AGROLD_DESCRIPTION``|Description affichée dans Tomcat|''|

Pour injecter ces variables d'environnement dans tomcat il faut mettre ceci dans ``$CATALINA_BASE/bin/setenv.sh``

```bash
#Les variables d'environnement sont passés en tant que des options JDK
export CATALINA_OPTS="-Dagrold.name=$AGROLD_NAME -Dagrold.description=$AGROLD_DESCRIPTION"
```


#### Compilation
```bash
# Avec AGROLD_NAME définie précédemment 
mvn clean install 
```

### Sauvegarde des activités
*  Le fichier agrold-javaweb/agrold-api.json est la version de base de la spécification Swagger qui décrit les webservices et qui est fourni en ligne par le webservice GET /api/webservice. 
Ce fichier est placé hors de l'application pour éviter que les modifications de l'application ne supprime les nouveaux services créés à partir de l'interface HTTP
* La sauvegarde de l'historique des activités est inaccessible pour l'instant car les boutons de login et d'enregistrement sont désactivés 
*  les informations de connexion à MySql sont dans le fichier `/home/virtuoso/agrold-mysql.conf` 
sur le serveur pour éviter de les partager avec le code sur github. Il s'agit de l'url de mysql, du nom et pwd de l'admin de la BD. 
*  pour recréer un utilisateur: supprimer les lignes correspondant à son adresse email dans les tables `user`, `u_info`, `h_settings` de la BD `agrold`:

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
*  https://www.ebi.ac.uk/proteins/api/doc


### Issues (ou *to fix*)

*italic*: peut-être résolu

**gras**: résolu

#### En général
* implémenter des test unitaires pour vérifier que les màj ne crèent pas d'erreur
* configurer le Tomcat de Volvestre dans Netbeans pour directement déployer la version dev. en ligne

#### Advanced Search
*  le fichier `agrold-api.json` est téléchargé plusieurs fois ???
*  *corriger la requête sparql du service describe et celle des genesByPathwayIds*
*  Laisser l'utilisateur spécifier les paramètres `page`et `pageSize`?
*  *Les tables YASR apparaissent souvent aminçis dans les fenêtres de description d'entités*
*  *La navigation entre page ne marche pas ("Next page")*: quand on fait un *display* puis on revient aux résultats de la recherche
*  *les requêtes sur les QTL retournent peu de résultats*
*  advancedsearch.jsp incompatible avec includes.html?
*  gérer les erreur HTTP (eg. 204, 404, 500, etc.) de l'API dans l'advancedSearch (màj de Swagger-client dans tous les appel aux web services)
*  Recherche des QTL: la seul description fournie est le label et has_trait (très souvent une URI TO_... dans le graphe gramene.qtl, mais texte dans qtaro.qtl). Donc il est difficile de les retrouver par mot-clé
*  **Corriger la requête de recherche pour trouver plus de QTL y compris ceux de qtaro.qtl (voir sparql query du web service getQTLs i.e. `rdf:type|rdfs:subclassOf)**
*  Corriger la recherche des QTL par mot clé (la requête BNL6.32 ne retourne rien)
*  Permettre de pouvoir passer les paramètres par GET (url)
*  View as Graph: en faire un point à partir duquel la KB peut être explorée
*  View as Graph with kenetmaps.js: ajouter des types spécifiques à AgroLD comme chromosome, CDS, ou mRNA (le css décrit le style des types d'entités supportés) 
*  **knetmaps: ajout d'un nouveau type** (concept, e.g. mRNA) = ajout du style du type, ajout du style de ses relations avec les autres concept, et ajout de l'image de son symbole dans le css et le dossier img de knetmaps
*  Interaction AgroLD <> KnetMaps.JS: voir knetmaps.js ligne 309 (Pour accéder à un attribut de données d'un noeud : console.log("my.showLinks.selectedNode: " + this.data("iri"));)
```
sparql
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
*  Elliminer les exemples qui ne marchent pas
*  rendre les exemples cliquables pour éviter l'effort des copier-coller

#### Historique utilisateur
*  Problème de connexion admin à la BD
*  Détection d'une duplication de clé à la création d'un nouvel utilisateur

#### Web services
*  gestion de web services: ajouter les nouveaux tags, ajouter automatiquement le tag customizable
*  GUI de gestion des création,modif, suppr de services web: indexer les sparql, url, et paramètres de services dans un xml. une seule implémentation en java. L'url d'un service particulier est un paramètre dans l'adresse comme le format. la description dans le json pour swagger se fait en fixant les paramètres in URL
*  problème d'encodage dans l'affichage des publications
*  Certains services ne retournent que le format .json (il faut convertir la sortie pour d'autres formats)
*  L'implémentation des services n'est pas documentée sur Swagger (code SPARQL, sources externes des données, etc.)
*  Il n'y a pas de cache
*  Il n'y a pas de gestion explicite des erreurs avec les codes HTTP
*  Ajouter un web service `getPathways`
*  Ajouter un web service `getGenesPublicationsById`: les reférences à pubmed semblent n'être liées qu'aux entités 
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
*  Ajouter un bouton pour ajouter les préfixes
*  **D3SPARQL dans YASR**
*  le plein écran de YASQE et YASR est caché par la barre de menu
*  le graphe des résultats n'est pas téléchargeable
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
*  **le *describe* des URI ne fonctionne pas à cause de la redirection des URL de l'appli**



Accessions
 = échantillon parenté (espèce)
 = identifiant d'un gène (KB)