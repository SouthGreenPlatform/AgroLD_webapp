# Build stage
FROM maven:latest as build
# Shared arguments between build and run stages
ARG GIT_COMMIT=unknown

ARG AGROLD_NAME=agrold
ARG AGROLD_DESCRIPTION=agrold production instance generated from commit ${GIT_COMMIT}

WORKDIR /build_dir

LABEL "fr.ird.maintainer"="Yann POMIE <yann.pomie@ird.fr>" \
      "version"=${GIT_COMMIT} \
      "description"=${AGROLD_DESCRIPTION}

COPY . /build_dir

# This is here so we can change it depending of dev/prod
ENV AGROLD_NAME=${AGROLD_NAME}

RUN mvn clean install

# Run stage
FROM tomcat:latest 

# We renew arguments
ARG GIT_COMMIT
ARG AGROLD_NAME
ARG AGROLD_DESCRIPTION

WORKDIR /usr/local/tomcat/webapps

COPY --from=build /build_dir/target/${AGROLD_NAME}.war /usr/local/tomcat/webapps/${AGROLD_NAME}.war

ENV JAVA_OPTS="-Dagrold.name=${AGROLD_NAME} -Dagrold.description=${AGROLD_DESCRIPTION}" 