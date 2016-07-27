# Indexation (ElasticSearch) for AgroLD

agrold_indexation is an application that enables to communicate easily with an ElasticSearch cluster, index files, update and delete indexes without using cURL. We use this application to automate the indexation task.

# Contact

For bug tracking purpose you can use the GitHub or you can contact the maintainers using the following email addresses:

* nordine.elhassouni_at_cirad.fr
* pierre.larmande_at_ird.fr

# Contributing

* Written by Stella Zevio.

# Installation

### Prerequisites

You will need a running ElasticSearch cluster (locally or not).
Our application works with [ElasticSearch 2.2.1](https://www.elastic.co/fr/blog/elasticsearch-2-2-1-released).

If your cluster works with another version of ElasticSearch and you still want to test agrold_indexation : 
  * change ElasticSearch version in Maven dependencies (code/pom.xml) according to your cluster's version
  * recompile a jar file
Please be advised that we can't guarantee that our application will work with latest versions of ElasticSearch.
Check that your ElasticSearch cluster's version and your agrold_indexation's are the same.

agrold_indexation is compatible with Shield security plugin for ElasticSearch.

### agrold_indexation

You will need indexation.jar and config.properties (application/) and you should put them in the same directory.

# How to use agrold_indexation

You can use agrold_indexation in four scenarios.

###configuration

This scenario helps you to easily configure the application so that it can contact your ElasticSearch cluster.
You will be guided step-by-step to edit config.properties file.

```
java -jar indexation.jar configuration
```

###indexation

This scenario enables you to index a JSON file on your ElasticSearch cluster.

```
java -jar indexation.jar indexation path/to/data.json index type
```

###update of an index

This scenario enables you to update an existing index on your ElasticSearch cluster.

```
java -jar indexation.jar update index type id field new_value
```

###deletion of an index

This scenario enables you to delete an existing index on your ElasticSearch cluster.

```
java -jar indexation.jar deletion index type id
```
