#### Deployer l'appli web
*  préferer Tomcat 7/8 à d'autres serveurs web
*  remplacer l'url de l'apli web dans `config/config.js`
*  remplacer l'url de l'appli dans `agrold.ogust.servlet.Logout.java`
*  remplacer l'url de base de l'api dans le fichier à l'emplacement `AGROLDAPIJSONURL` définis dans `config/config.js` (`config/agrold.json`): `"host": "localhost:8080/agrold"` ou `"host": "agrold.southgreen.fr"`
*  remplacer le chemin de configuration de la connexion au serveur MySQL pour la gestion de l'historique utilisateur dans le fichier `agrold.ogust.config.MySQLProperties`: (variable `configFilePath`)

Le format du fichier de configuration pour mysql est (**sans ce fichier, l'application web ne peut pas démarrer)**):

```bash
[nom serveur]:[port mysql]/[nom base de données]?useSSL=false
[admin username]
[admin password]
```

*  sauvegarder le fichier `agrold.war` déployé sur le serveur au cas où on voudrait la redéployer : `scp -r virtuoso@volvestre.cirad.fr:/opt/apache-tomcat-8.0.23/webapps/agrold.war /Users/zadmin/agrold/virtuoso-webapps-dir-save/`
*  supprimer les dossier `agrold` des sous-repertoires `webapps` et `work/Catalina/localhost` (ou redémarrer tomcat) de Tomcat
*  choisir le bon context path dans `META_INF/context.xml` (sans `agrold` en ligne et avec en local)


### Sauvegarde des activités
*  les informations de configuration sont dans le fichier `/home/virtuoso/agrold-mysql.conf` 
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

#### Advanced Search
*  Les tables YASR apparaissent souvent aminçis dans les fenêtres de description d'entités
*  La navigation entre page ne marche pas ("Next page")
*  les requêtes sur les QTL retournent peu de résultats
*  advancedsearch.jsp incompatible avec includes.html?
*  gérer les erreur de l'API dans l'advancedSearch (màj de Swagger-client dans tous les appel aux web services)
*  Recherche des QTL: la seul description fournie est le label et has_trait (très souvent une URI TO_... dans le graphe gramene.qtl, mais texte dans qtaro.qtl). Donc il est difficile de les retrouver par mot-clé
*  Corriger la requête de recherche pour trouver plus de QTL y compris ceux de qtaro.qtl (voir sparql query du web service getQTLs i.e. `rdf:type|rdfs:subclassOf)

#### Exemple
*  Elliminer les exemples qui ne marchent pas
*  rendre les exemples cliquables pour éviter l'effort des copier-coller

#### Historique utilisateur
*  Problème de connexion admin à la BD
*  Détection d'une duplication de clé à la création d'un nouvel utilisateur

#### Web services
*  problème d'encodage dans l'affichage des publications
*  Certains services ne retourne que le format .json (il faut convertir la sortie pour d'autres format)
*  L'implémentation des services n'est pas documenté sur Swagger (code SPARQL, sources externes des données, etc.)
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
*  bouton pour ajouter les préfixes
*  D3SPARQL dans YASR
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

### Types:

*  agrold:resource/Chromosome	
*  agrold:resource/Gene
*  agrold:resource/QTL
*  agrold:vocabulary/Gene
*  agrold:vocabulary/Metabolic_Pathway
*  agrold:vocabulary/Protein
*  agrold:vocabulary/Reaction

