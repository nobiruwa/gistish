FROM debian:latest

EXPOSE 22

# apt
RUN apt update -y && apt install -y curl iproute2 locales mlocate openssh-server openssh-client perl ssh tar unzip vim wget

# locale
RUN sed -i '/ja_JP.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen
RUN update-locale LANG=ja_JP.UTF-8

# timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

# postgresql
RUN apt install -y postgresql-15

COPY setup_postgresql.sh /setup_postgresql.sh
RUN chmod u+x /setup_postgresql.sh
RUN /setup_postgresql.sh
RUN rm /setup_postgresql.sh

COPY run.sh /run.sh
RUN chmod u+x /run.sh

CMD ["/run.sh"]
