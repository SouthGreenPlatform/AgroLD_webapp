# Virtuoso Helm Chart

| Variable Name                                | Description                                                                                 | Default Value                                     |
| -------------------------------------------- | ------------------------------------------------------------------------------------------- | ------------------------------------------------- |
| `replicaCount`                               | Number of replicas for the deployment                                                       | 1                                                 |
| `image.repository`                           | Docker image repository                                                                     | openlink/virtuoso-opensource-7                    |
| `image.pullPolicy`                           | Pull policy for the image                                                                   | IfNotPresent                                      |
| `image.tag`                                  | Tag of the Docker image                                                                     | "7.2.11"                                          |
| `imagePullSecrets `                          | List of secrets to pull the Docker image from private repositories                          | []                                                |
| `nameOverride`                               | Override for the chart name                                                                 | ""                                                |
| `fullnameOverride`                           | Override for the full name of the chart                                                     | ""                                                |
| `podAnnotations`                             | Annotations for the pod                                                                     | {}                                                |
| `podSecurityContext`                         | Security context for the pod                                                                | {}                                                |
| `securityContext`                            | Security context for the container                                                          | {}                                                |
| `service.type`                               | Type of Kubernetes service                                                                  | ClusterIP                                         |
| `service.port`                               | Port number for the service                                                                 | 80                                                |
| `ingress.enabled`                            | Enable Ingress resource                                                                     | true                                              |
| `ingress.className`                          | Class name for Ingress                                                                      | ""                                                |
| `ingress.annotations`                        | Annotations for Ingress resource                                                            | {}                                                |
| `ingress.hosts`                              | List of hosts for Ingress resource                                                          | virtuoso.local                                    |
| `resources`                                  | Resource limits and requests                                                                | {}                                                |
| `autoscaling.enabled`                        | Enable Horizontal Pod Autoscaler                                                            | false                                             |
| `autoscaling.minReplicas`                    | Minimum number of replicas when autoscaling is enabled                                      | 1                                                 |
| `autoscaling.maxReplicas`                    | Maximum number of replicas when autoscaling is enabled                                      | 100                                               |
| `autoscaling.targetCPUUtilizationPercentage` | Target CPU utilization percentage when autoscaling is enabled                               | 80                                                |
| `nodeSelector`                               | Node labels for pod assignment                                                              | {}                                                |
| `tolerations`                                | Tolerations for pod assignment                                                              | []                                                |
| `affinity`                                   | Affinity settings for pod assignment                                                        | {}                                                |
| `DAVPassword`                                | Password for DAV (WebDAV) access                                                            | "password"                                        |
| `DBAPassword`                                | Password for DBA (Database Admin) access                                                    | "password"                                        |
| `serveConductor`                             | Flag to enable serving the conductor                                                        | false                                             |
| `env`                                        | Environment variables                                                                       | `IMPORT_THREAD: 2`                                |
| `initdb.enabled`                             | Enable initialization job                                                                   | true                                              |
| `initdb.name`                                | Name of the initialization job                                                              | sparql-initdb                                     |
| `initdb.kind`                                | Kind of Kubernetes resource used for initialization job                                     | ConfigMap                                         |
| `initdb.data`                                | Files inside of /initdb.d. Sonsists of map with filenames as a key and its content as a key | `"init.sh": "#!/bin/bash\necho \"Ready to go!\""` |
| `persistence.enabled`                        | Enable persistence storage                                                                  | true                                              |
| `persistence.name`                           | Name of the persistence storage                                                             | triplestore                                       |
| `persistence.size`                           | Size of the persistence storage                                                             | 4Gi                                               |
