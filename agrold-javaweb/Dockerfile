# Build stage
FROM maven:3.9-amazoncorretto-8 as build
# Shared arguments between build and run stages
ARG GIT_COMMIT=unknown

ARG AGROLD_NAME=agrold
ARG AGROLD_DESCRIPTION=agrold production instance generated from commit ${GIT_COMMIT}

# This is here so we can change it depending of dev/prod
ENV AGROLD_NAME=${AGROLD_NAME}

WORKDIR /build_dir

LABEL "fr.ird.maintainer"="Yann POMIE <yann.pomie@ird.fr>" \
      "version"=${GIT_COMMIT} \
      "description"=${AGROLD_DESCRIPTION}

COPY . /build_dir

RUN AGROLD_NAME=${AGROLD_NAME} mvn clean install

# Run stage
FROM bitnami/tomcat:7

# We renew arguments
ARG GIT_COMMIT
ARG AGROLD_NAME
ARG AGROLD_DESCRIPTION

# COPY --from=build /build_dir/target/${AGROLD_NAME}.war /opt/bitnami/tomcat/webapps
COPY --from=build /build_dir/target/${AGROLD_NAME}.war /opt/bitnami/tomcat/webapps_default

ENV GIT_COMMIT=${GIT_COMMIT}
ENV JAVA_OPTS="-Dagrold.name=${AGROLD_NAME} -Dagrold.description=${AGROLD_DESCRIPTION}" 