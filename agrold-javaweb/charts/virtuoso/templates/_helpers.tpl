{{/*
Expand the name of the chart.
*/}}
{{- define "sparql.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "sparql.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Returns the configmap name
*/}}
{{- define "sparql.configMapName" -}}
{{- printf "%s-config" (include "sparql.fullname" .) -}}
{{- end -}}

{{/*
Returns the scripts configmap name
*/}}
{{- define "sparql.scripts.configMapName" -}}
{{- printf "%s-scripts" (include "sparql.fullname" .) -}}
{{- end -}}

{{/*
Returns the initdb configmap name
*/}}
{{- define "sparql.initdb.configMapName" -}}
{{- printf "%s-%s" (include "sparql.fullname" .) (coalesce .Values.initdb.name "initdb") -}}
{{- end -}}

{{/*
Returns the secret's name
*/}}
{{- define "sparql.secretName" -}}
{{- printf "%s-secret" (include "sparql.fullname" .) -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "sparql.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "sparql.labels" -}}
helm.sh/chart: {{ include "sparql.chart" . }}
{{ include "sparql.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "sparql.selectorLabels" -}}
app.kubernetes.io/name: {{ include "sparql.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
