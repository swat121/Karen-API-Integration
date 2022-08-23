# Karen
This repository contain the source code of the message broker, that is able to help you get connect to other home mircoservice.

## Desciption
___
***Karen*** - is a service, that creates a connection between apps (telegram bots, website, android app) and smart things (ESP8266, Arduino UNO). Karen have a standard array of commands that control microcontrollers:
* `help`
* `status`
* `setting`
  * relay1
  * relay2
  * relay3
  * backlight
* `sensor`
  * temperature
  * light

This array of commands **must** be in the source code of every service running microcontrollers, in order to ensure normal communication.

For cluster management of microcontrollers in Karen used application.yaml file with local ip address every microcontroller.

*Example:*
```yaml
service:
  resource:
    garry: "http://192.168.0.100:80/"
    patric: "http://192.168.0.101:80/"
```
## Requirements
___
* java 17.0.3
* maven 3.8.4
## Instalation and running
___
Clone repository:
```cmd
git clone https://github.com/swat121/karen.git
cd karen
```
Build project:
```cmd
mvn clean compile
mvn clean package [or mvn package -DskipTests] 
```
Launch jar file:
```cmd
java -jar Karen-0.0.1-SNAPSHOT.jar
```