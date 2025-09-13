FROM debian:latest

EXPOSE 22

# apt
RUN apt update -y && apt install -y curl iproute2 locales plocate openssh-server openssh-client perl ssh tar unzip vim wget

# locale
RUN sed -i '/ja_JP.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen
RUN update-locale LANG=ja_JP.UTF-8

# timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

RUN apt install -y build-essential libpng-dev libssl-dev libstdc++6 openssl

COPY openjdk-11.0.2_linux-x64_bin.tar.gz /openjdk-11.0.2_linux-x64_bin.tar.gz
COPY setup_jdk.sh /setup_jdk.sh
RUN chmod u+x /setup_jdk.sh
RUN /setup_jdk.sh
RUN rm -f /setup_jdk.sh
ENV JAVA_HOME=/usr/local/java/jdk
ENV PATH=/usr/local/java/jdk/bin:$PATH
RUN echo 'JAVA_HOME=/usr/local/java/jdk' >> /root/.bashrc
RUN echo 'PATH=$PATH:/usr/local/java/jdk/bin' >> /root/.bashrc
RUN rm -f /openjdk-11.0.2_linux-x64_bin.tar.gz

COPY resin-pro-4.0.66.zip /resin-pro-4.0.66.zip
COPY setup_resin.sh /setup_resin.sh
RUN chmod u+x /setup_resin.sh
RUN /setup_resin.sh
RUN rm -f /setup_resin.sh

COPY run.sh /run.sh
RUN chmod u+x /run.sh

CMD ["/run.sh"]
