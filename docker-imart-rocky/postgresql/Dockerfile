FROM myrocky:latest
 
EXPOSE 22 5432
 
ENV DEBIAN_FRONTEND noninteractive
 
# yum
RUN yum -y update && yum clean all
RUN yum -y upgrade && yum -y update && yum clean all
RUN yum install -y postgresql-server postgresql
RUN yum install -y --setopt=protected_multilib=false epel-release
RUN yum install -y multitail ncurses-devel ncurses-term
 
# PostgreSQL
COPY setup_postgresql.sh /setup_postgresql.sh
RUN chmod u+x /setup_postgresql.sh
RUN /setup_postgresql.sh
RUN rm /setup_postgresql.sh
 
COPY run.sh /run.sh
RUN chmod u+x /run.sh
 
RUN updatedb
 
CMD /run.sh
