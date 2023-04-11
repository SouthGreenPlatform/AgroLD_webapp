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
FROM tomcat:7 

# We renew arguments
ARG GIT_COMMIT
ARG AGROLD_NAME
ARG AGROLD_DESCRIPTION
ARG TOMCAT_PASSWORD=password

WORKDIR /usr/local/tomcat/webapps

# "It turns out that all default tomcat apps have been removed from the webapps directory for all tomcat images starting with Tomcat version 7. All apps have been moved to /usr/local/tomcat/webapps.dist"
# https://stackoverflow.com/questions/61972819/cannot-access-tomcat-manager-or-any-other-default-tomcat-app-when-running-tomcat
RUN mv /usr/local/tomcat/webapps.dist/manager /usr/local/tomcat/webapps/

# To access manager
RUN sed -i "$ i\<role rolename=\"manager-gui\"/><user username=\"tomcat\" password=\"${TOMCAT_PASSWORD}\" roles=\"manager-gui\"/>" /usr/local/tomcat/conf/tomcat-users.xml
RUN sed -i '19,20d' /usr/local/tomcat/webapps/manager/META-INF/context.xml

COPY --from=build /build_dir/target/${AGROLD_NAME}.war /usr/local/tomcat/webapps/${AGROLD_NAME}.war

ENV JAVA_OPTS="-Dagrold.name=${AGROLD_NAME} -Dagrold.description=${AGROLD_DESCRIPTION}" 