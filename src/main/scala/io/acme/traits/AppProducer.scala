package io.acme.traits

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait AppProducer {
  val system = ActorSystem("appProducerActor")
  val config = system.settings.config.getConfig("akka.kafka.producer")

  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")

  val locationProducerSettings = ProducerSettings(
    config,
    new StringSerializer,
    new StringSerializer)
    .withBootstrapServers("localhost:9092")
  val locationKafkaProducer = locationProducerSettings.createKafkaProducer()

  val kafkaProducer = producerSettings.createKafkaProducer()

  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer.create(system)

  def terminateWhenDone(result: Future[Done]): Unit =
    result.onComplete {
      case Failure(e) =>
        system.log.error(e, e.getMessage)
        system.terminate()
      case Success(_) => system.terminate()
    }

}
