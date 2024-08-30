FROM jenkins/jenkins:lts

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY *.groovy /usr/share/jenkins/ref/init.groovy.d/

RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

USER root

RUN set -eux \
  &&  curl -sL https://deb.nodesource.com/setup_lts.x \
  && apt-get update \
  && apt-get -y install nodejs \
  && apt-get clean
