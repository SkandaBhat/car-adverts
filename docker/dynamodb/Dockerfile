FROM openjdk:8-jre

RUN mkdir /var/dynamodb_wd
RUN mkdir /var/dynamodb_local
WORKDIR /var/dynamodb_wd

# download and extract local dynamoDB
RUN wget -q http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz && \
    tar zxvf dynamodb_local_latest.tar.gz && \
    rm dynamodb_local_latest.tar.gz

# pip install aws cli
RUN apt-get update && \
    apt-get install -y python-pip && \
    pip install awscli

COPY scripts/run.sh /scripts/run.sh

#get permission to execute script
RUN chmod +x /scripts/run.sh

# local aws credentials, can be dummy because local db
ENV AWS_ACCESS_KEY_ID 3456
ENV AWS_SECRET_ACCESS_KEY asdf

# expose port 8000
EXPOSE 8000

CMD ["/scripts/run.sh"]
