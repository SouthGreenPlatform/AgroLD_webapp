# Build stage
FROM maven:3.9-amazoncorretto-17 as build
# Shared arguments between build and run stages

WORKDIR /build_dir

COPY . /build_dir

RUN mvn clean install

# Run stage
FROM bitnami/tomcat:9.0

# This is here so we can change it depending of dev/prod
ARG GIT_COMMIT=unknown
ARG BUILD_DATE=unknown
ARG AGROLD_DESCRIPTION='"agrold production instance"'

LABEL "fr.ird.maintainer"="Yann POMIE <yann.pomie@ird.fr>" \
      "version"=${GIT_COMMIT} \
      "description"=${AGROLD_DESCRIPTION} \
      "org.opencontainers.image.authors"="Yann POMIE <yann.pomie@ird.fr>" \
      "org.opencontainers.image.created"=${BUILD_DATE} \
      "org.opencontainers.image.source"="https://github.com/SouthGreenPlatform/AgroLD_webapp" \
      "org.opencontainers.image.version"=${GIT_COMMIT}   

COPY --from=build /build_dir/target/*.war /opt/bitnami/tomcat/webapps_default

ENV GIT_COMMIT=${GIT_COMMIT}
ENV CATALINA_OPTS="-Dagrold.description=${AGROLD_DESCRIPTION}" 