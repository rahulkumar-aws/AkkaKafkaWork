package io.acme.producer

import akka.Done
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import io.acme.traits.AppProducer
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, _}

object LocationLogProducer extends AppProducer{

  def main(args: Array[String]): Unit = {
    val file = getClass.getResource("/nyc_stations.csv").getFile()
    val myData = scala.io.Source.fromFile(file).getLines()
    val tickSource = Source.tick(Duration.Zero, 500.milliseconds, Unit).map(_ => myData.next)

    // #plainSink
    val done: Future[Done] =
      tickSource
        .map(_.toString)
        .map(value => new ProducerRecord[String, String]("topic1", value))
        .runWith(Producer.plainSink(locationProducerSettings))
    // #plainSink

    terminateWhenDone(done)

  }

}
