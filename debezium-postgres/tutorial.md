# debezium-postgres

[Tutorial :: Debezium Documentation](https://debezium.io/documentation/reference/tutorial.html) を参考にして、MySQLをPostgreSQLに置き換えてDebeziumの環境を構築してみました。

また、 [Sending signals to a Debezium connector :: Debezium Documentation](https://debezium.io/documentation/reference/stable/configuration/signalling.html) に記載された signaling table を用いた ad hoc snapshot を試してみました。

## Zookeeper

```bash
docker run -it --rm --name zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 quay.io/debezium/zookeeper:2.7
```

## Kafka

```bash
docker run -it --rm --name kafka -p 9092:9092 --link zookeeper:zookeeper quay.io/debezium/kafka:2.7
```

## Schema Registry

```bash
docker run -it --rm --name schema-registry -p 8081:8081 -p 8181:8181 --link zookeeper --link kafka -e SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS=kafka:9092 -e SCHEMA_REGISTRY_HOST_NAME=schema-registry -e SCHEMA_REGISTRY_LISTENERS=http://localhost:8081 confluentinc/cp-schema-registry:7.0.1
```

## PostgreSQL

```bash
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres quay.io/debezium/example-postgres:2.7
```

## PostgreSQLのpsql

```bash
docker ps
docker exec -it {postgresのCONTAINER_ID} bash

CREATE TABLE public.debezium_signal (id VARCHAR(42) PRIMARY KEY, type VARCHAR(32) NOT NULL, data VARCHAR(2048) NULL);

SELECT * FROM pg_replication_slots;

UPDATE inventory.customers SET first_name='Anne Marie' WHERE id=1004;

// コネクタを再作成する場合
select pg_drop_replication_slot('debezium');

```

## Kafka Connect

```bash
docker run -it --rm --name connect -p 8083:8083 -e GROUP_ID=1 -e CONFIG_STORAGE_TOPIC=my_connect_configs -e OFFSET_STORAGE_TOPIC=my_connect_offsets -e STATUS_STORAGE_TOPIC=my_connect_statuses --link kafka:kafka --link postgres:postgres quay.io/debezium/connect:2.7
```

## Kafka Connector作成

```bash
curl -sSL -H "Accept:application/json" localhost:8083/connectors | jq

curl -sSL -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d @'inventory-connector.json' | jq

curl -sSL -X GET -H "Accept:application/json" localhost:8083/connectors/inventory-connector/config | jq

// コネクタの削除を試す場合
curl -sSL -X DELETE localhost:8083/connectors/inventory-connector
```

## Kafkaトピックの閲覧

```bash
docker ps
docker exec -it {kafkaのCONTAINER_ID} bash

./bin/kafka-topics.sh --bootstrap-server kafka:9092 --list

./bin/kafka-console-consumer.sh --bootstrap-server kafka:9092  --from-beginning --topic dbserver1.inventory.customers
```

## cmakによるKafkaトピックの閲覧

Schema Registryを必要としませんが、メッセージの内容を見ることができません。

```bash
wget https://github.com/yahoo/CMAK/releases/download/3.0.0.6/cmak-3.0.0.6.zip
unzip cmak-3.0.0.6.zip

cmak-3.0.0.6/bin/cmak -Dconfig.file=./cmak-application.conf -Dhttp.port=8080 -J--add-opens=java.base/sun.net.www.protocol.file=ALL-UNNAMED -J--add-exports=java.base/sun.net.www.protocol.file=ALL-UNNAMED
```

## kafka-uiによるトピックの閲覧

UIでメッセージを確認できて便利です。

```bash
docker run -it --rm --name kafka-ui -p 8080:8080 --link kafka --link schema-registry -e spring.config.additional-location=/tmp/config.yml -v `pwd`/kafka-ui-config.yml:/tmp/config.yml provectuslabs/kafka-ui
```

## Signaling Tableを使ったスナップショットの作成

```bash
INSERT INTO public.debezium_signal (id, type, data) VALUES (gen_random_uuid(), 'execute-snapshot', '{ "data-collections": ["inventory.*"], "type": "incremental" }');

INSERT INTO public.debezium_signal (id, type, data) VALUES (gen_random_uuid(), 'execute-snapshot', '{ "data-collections": ["inventory.*"], "type": "blocking" }');
```

## コンテナーの停止

```bash
ocker stop connect schema-registry postgres kafka zookeeper
```
