## Introduction ##


Download kafka from here: https://kafka.apache.org/downloads

**Start zookeeper**

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

**Start Kafka Server**

```bash
bin/kafka-server-start.sh config/server.properties
```

**Create topic**

```bash
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic locationlog
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic bookinglog
```



