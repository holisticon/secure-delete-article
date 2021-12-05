# no minor and major upgrade by image name!!
FROM quay.io/keycloak/keycloak:15.0.2

ENV SUMMARY="Image for Keycloak"
ENV DESCRIPTION="Keycloak image runtime with GDPR module included"
ENV TZ="Europe/Berlin"
ENV MAINTAINER="Martin Reinhardt <martin.reinhardt@holisticon.de>"

ENV KEYCLOAK_LOGLEVEL=INFO
ENV ROOT_LOGLEVEL=INFO
ENV KEYCLOAK_DIRECTORY=${JBOSS_HOME}
ENV EVENTS_DIRECTORY=/tmp/metrics
ENV PROXY_ADDRESS_FORWARDING=true

LABEL summary="$SUMMARY" \
      description="$DESCRIPTION" \
      maintainer="$MAINTAINER" \
      version="{{ keycloak_version }}" \
      io.k8s.description="$DESCRIPTION" \
      io.k8s.display-name="toolisticon sso" \
      com.redhat.component="toolisticon-sso-container"

USER root

# copy plugin
COPY gdpr-module-for-keycloak.ear ${KEYCLOAK_DIRECTORY}/standalone/deployments/gdpr-module-for-keycloak.ear

# Upgrade packages
RUN microdnf -y update && microdnf clean all && rm -rf /tmp/setup && rm -rf /var/cache/yum

USER 1000
