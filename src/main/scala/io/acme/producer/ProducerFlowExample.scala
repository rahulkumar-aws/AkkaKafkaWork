package io.acme.producer

import akka.kafka.ProducerMessage
import akka.kafka.ProducerMessage.MultiResultPart
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{Sink, Source}
import io.acme.traits.AppProducer
import org.apache.kafka.clients.producer.ProducerRecord

object ProducerFlowExample extends AppProducer{
  def createMessage[KeyType, ValueType, PassThroughType](key: KeyType, value: ValueType, passThrough: PassThroughType) =
  // #singleMessage
    new ProducerMessage.Message[KeyType, ValueType, PassThroughType](
      new ProducerRecord("topicName", key, value),
      passThrough
    )

  // #singleMessage

  def createMultiMessage[KeyType, ValueType, PassThroughType](key: KeyType,
                                                              value: ValueType,
                                                              passThrough: PassThroughType) = {
    import scala.collection.immutable
    // #multiMessage
    new ProducerMessage.MultiMessage[KeyType, ValueType, PassThroughType](
      immutable.Seq(
        new ProducerRecord("topicName", key, value),
        new ProducerRecord("anotherTopic", key, value)
      ),
      passThrough
    )
    // #multiMessage
  }

  def createPassThroughMessage[KeyType, ValueType, PassThroughType](key: KeyType,
                                                                    value: ValueType,
                                                                    passThrough: PassThroughType) =
  // format:off
  // #passThroughMessage
    new ProducerMessage.PassThroughMessage(
      passThrough
    )
  // #passThroughMessage

  def main(args: Array[String]): Unit = {
    // #flow
    val done = Source(1 to 100)
      .map { number =>
        val partition = 0
        val value = number.toString
        ProducerMessage.Message(
          new ProducerRecord("topic1", partition, "key", value),
          number
        )
      }
      .via(Producer.flexiFlow(producerSettings))
      .map {
        case ProducerMessage.Result(metadata, message) =>
          val record = message.record
          s"${metadata.topic}/${metadata.partition} ${metadata.offset}: ${record.value}"

        case ProducerMessage.MultiResult(parts, passThrough) =>
          parts
            .map {
              case MultiResultPart(metadata, record) =>
                s"${metadata.topic}/${metadata.partition} ${metadata.offset}: ${record.value}"
            }
            .mkString(", ")

        case ProducerMessage.PassThroughResult(passThrough) =>
          s"passed through"
      }
      .runWith(Sink.foreach(println(_)))
  }
}
