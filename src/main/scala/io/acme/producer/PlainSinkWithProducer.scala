package io.acme.producer

import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import io.acme.traits.AppProducer
import org.apache.kafka.clients.producer.ProducerRecord

object PlainSinkWithProducer extends AppProducer {
  def main(args: Array[String]): Unit = {
    // #plainSinkWithProducer
    val done = Source(1 to 100)
      .map(_.toString)
      .map(value => new ProducerRecord[String, String]("topic1", value))
      .runWith(Producer.plainSink(producerSettings, kafkaProducer))
    // #plainSinkWithProducer
    terminateWhenDone(done)
  }
}
