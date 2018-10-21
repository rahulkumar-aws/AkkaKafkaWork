package io.acme.utils

import io.acme.traits.AppProducer
import java.util
object ObserveMetrics extends AppProducer{
  def main(args: Array[String]): Unit = {
    // format:off
    // #producerMetrics
    val metrics: util.Map[org.apache.kafka.common.MetricName, _ <: org.apache.kafka.common.Metric] =
    kafkaProducer.metrics() // observe metrics
    // #producerMetrics
    // format:on

   println( metrics.keySet())
    println(metrics.get("name"))
    println(metrics.get("group"))
    println(metrics.get("description"))
    println(metrics.get("MetricName"))
    //metrics.clear()
  }
}
