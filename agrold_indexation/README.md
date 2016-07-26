# Indexation (ElasticSearch) for AgroLD

agrold_indexation is an application that enables to communicate easily with an ElasticSearch cluster, index files, update and delete indexes without using cURL. We use this application to automate the indexation task.

# Contact

For bug tracking purpose you can use the GitHub or you can contact the maintainers using the following email addresses:

* nordine.elhassouni_at_cirad.fr
* pierre.larmande_at_ird.fr

# Contributing

* Written by Stella Zevio.

# Installation

# How to use agrold_indexation

You can use agrold_indexation in four scenarios.

###configuration

```
java -jar indexation.jar configuration
```

###indexation

```
java -jar indexation.jar indexation path/to/data index type
```

###update of an index

```
java -jar indexation.jar update index type id field new_value
```

###deletion of an index

```
java -jar indexation.jar deletion index type id
```
