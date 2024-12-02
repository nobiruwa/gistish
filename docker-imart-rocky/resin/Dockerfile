FROM myrocky:latest

EXPOSE 22 6800 8080 9009 5200 5701 40608

ENV DEBIAN_FRONTEND noninteractive

# yum
RUN yum -y update && yum clean all
RUN yum -y upgrade && yum -y update && yum clean all
RUN yum install -y --setopt=protected_multilib=false ld-linux.so.2 libstdc++.so.6 libpng12

# JDK
COPY openjdk-11.0.2_linux-x64_bin.tar.gz /openjdk-11.0.2_linux-x64_bin.tar.gz
COPY setup_jdk.sh /setup_jdk.sh
RUN chmod u+x /setup_jdk.sh
RUN /setup_jdk.sh
RUN rm -f /setup_jdk.sh
ENV JAVA_HOME /usr/local/java/jdk
ENV PATH /usr/local/java/jdk/bin:$PATH
RUN echo 'JAVA_HOME=/usr/local/java/jdk' >> /root/.bashrc
RUN echo 'PATH=$PATH:/usr/local/java/jdk/bin' >> /root/.bashrc
RUN rm -f /openjdk-11.0.2_linux-x64_bin.tar.gz

# Resin
COPY resin-pro-4.0.66.zip /resin-pro-4.0.66.zip
COPY setup_resin.sh /setup_resin.sh
RUN chmod u+x /setup_resin.sh
RUN /setup_resin.sh
RUN rm -f /setup_resin.sh

COPY run.sh /run.sh
RUN chmod +x /run.sh

RUN updatedb

CMD /run.sh
