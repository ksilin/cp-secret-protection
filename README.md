# cp-secret-protection #

A basic demo of the Confluent Secret Protection feature for clients:

## important points

* add confluent Maven repository: https://packages.confluent.io/maven/ (see build.sbt)
* add dependency on `"io.confluent" % "kafka-client-plugins" % Version.kafka` for the config provider (see build.sbt)
* define client options `AbstractConfig.CONFIG_PROVIDERS_CONFIG` and `s"${AbstractConfig.CONFIG_PROVIDERS_CONFIG}.$configProvider.class"`
* remember to set the `CONFLUENT_SECURITY_MASTER_KEY` env variable (see build.sbt)
* programmatically set the configuration path (aka remote-config-file): `s"""$${$configProvider:$secretFilePath/local.secret.file:client.properties/sasl.jaas.config}"""`

## relevant KIPs

* KIP-297: https://cwiki.apache.org/confluence/display/KAFKA/KIP-297%3A+Externalizing+Secrets+for+Connect+Configurations
* KIP-421: https://cwiki.apache.org/confluence/pages/viewpage.action?pageId=100829515 

## documentation

https://docs.confluent.io/current/security/secrets.html

## Open questions

* how to use the secret protection feature to encode/decode programmatically, probably by using the provider?

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with
any pull requests, please state that the contribution is your original work and that you license
the work to the project under the project's open source license. Whether or not you state this
explicitly, by submitting any copyrighted material via pull request, email, or other means you
agree to license the material under the project's open source license and warrant that you have the
legal authority to do so.

## License ##

This code is open source software licensed under the
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0) license.
