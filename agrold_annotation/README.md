# Annotation for AgroLD, using AgroPortal

agrold_annotation is an application that enables to annotate files from portals. We use it to annotate files for AgroLD, using AgroPortal.

# Contact

For bug tracking purpose you can use the GitHub or you can contact the maintainers using the following email addresses:

* nordine.elhassouni_at_cirad.fr
* pierre.larmande_at_ird.fr

# Contributing

* Written by Stella Zevio.

# Installation

### Prerequisites

Be sure that your config.properties file contains :
 * REST url to contact the portal
 * an API key (we provide you one for AgroPortal)
 
### agrold_annotation

You will need annotation.jar and config.properties (application/) and you should put them in the same directory.

# How to use agrold_annotation

You can use agrold_indexation in four scenarios.

###get

This scenario helps you to get annotations by GET method.

```
java -jar annotation.jar get path/to/data.json
```

###post

This scenario helps you to get annotations by POST method.

```
java -jar annotation.jar post path/to/data.json
```

###annotation with hierarchy

This scenario helps you to get annotations and classes from hierarchy.

```
java -jar annotation.jar hierarchy path/to/data.json
```

###labels

This scenario helps you to get only pref labels as annotations.

```
java -jar annotation.jar labels path/to/data.json
```

