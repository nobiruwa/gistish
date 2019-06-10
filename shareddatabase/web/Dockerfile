# service on tomcat
#
# VERSION               0.3

FROM tomcat

# const ARGs
ARG APP_NAME=spring-websocket-sample
ARG WAR_NAME=${APP_NAME}.war
ARG WAR_DEST=/usr/local/tomcat/webapps/${WAR_NAME}
ARG APP_DEST=/usr/local/tomcat/webapps/${APP_NAME}

# variable ARGs
ARG CONFIGURATION_SRC
ARG WAR_SRC=${CONFIGURATION_SRC}/${WAR_NAME}
ARG CONFIG_SRC=${CONFIGURATION_SRC}/config.properties
ARG CONFIG_DEST=${APP_DEST}/WEB-INF/classes/config.properties

# copy war, configuration
ADD ${WAR_SRC} ${WAR_DEST}

# extract war
RUN unzip ${WAR_DEST} -d ${APP_DEST}

ADD ${CONFIG_SRC} ${CONFIG_DEST}
