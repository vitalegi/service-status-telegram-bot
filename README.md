# README

Aim of this project is to have an utility on telegram that will send notification in case a service http response status changes.

## Compile

```bash
set PATH=C:\a\software\apache-maven-3.5.0\bin;C:\Program Files\Java\jdk1.8.0_131\bin;%PATH%
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_131
mvn clean package
```

## Run

```bash
set PATH=C:\Program Files\Java\jdk1.8.0_131\bin;%PATH%
java -jar .\target\service-status-telegram-bot-0.0.1.jar <OPTIONS>
```

### OPTIONS

`--spring.profiles.active=prod`

Mandatory field, to allow production usage.

#### Example usage

`--spring.profiles.active=prod`
