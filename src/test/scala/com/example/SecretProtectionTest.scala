/*
 * Copyright 2020 konstantin.silin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.config.{AbstractConfig, SaslConfigs}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import os.Path

import scala.collection.JavaConverters._

class SecretProtectionTest extends munit.FunSuite {

  val kafkaHost  = "localhost:9093"
  val byteSerdes: Serde[Array[Byte]] = Serdes.ByteArray()

  val resPath: Path = os.pwd / "src" / "test" / "resources"

  test("client secret protection decrypted".only) {

    val configProvider = "securepass"

    val topicName = "_confluent-metrics"

    val consumerJavaProps = new java.util.Properties
    consumerJavaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost)
    consumerJavaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    consumerJavaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroup")
    consumerJavaProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
    consumerJavaProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN")
    consumerJavaProps.put(AbstractConfig.CONFIG_PROVIDERS_CONFIG, configProvider)
    consumerJavaProps.put(
      s"${AbstractConfig.CONFIG_PROVIDERS_CONFIG}.$configProvider.class",
      "io.confluent.kafka.security.config.provider.SecurePassConfigProvider"
    )
    consumerJavaProps.put( SaslConfigs.SASL_JAAS_CONFIG,
      s"""$${securepass:$resPath/local.secret.file:client.properties/sasl.jaas.config}"""
    )
    // """org.apache.kafka.common.security.plain.PlainLoginModule required username="client" password="client-secret";"""
    val consumer = new KafkaConsumer[Array[Byte], Array[Byte]](
      consumerJavaProps,
      byteSerdes.deserializer(),
      byteSerdes.deserializer()
    )
    consumer.subscribe(List(topicName).asJava)
    val pollDuration                                    = java.time.Duration.ofMillis(10000)
    val recs: ConsumerRecords[Array[Byte], Array[Byte]] = consumer.poll(pollDuration)
    assert(recs.count() > 0)
  }
}
