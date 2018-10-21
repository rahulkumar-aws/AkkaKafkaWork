package io.acme.producer

import akka.Done
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import io.acme.traits.AppProducer
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.Future

object SimpleProducer extends AppProducer {
  def main(args: Array[String]): Unit = {
    // #plainSink
    val done: Future[Done] =
      Source(1 to 100)
        .map(_.toString)
        .map(value => new ProducerRecord[String, String]("topic1", value))
        .runWith(Producer.plainSink(producerSettings))
    // #plainSink

    terminateWhenDone(done)
  }
}
