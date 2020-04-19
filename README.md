# README

Aim of this project is to have an utility on telegram that is able to perform analysis on financial data.

As data provider, has been selected Yahoo!Finance.

## Compile

```bash
set PATH=C:\a\software\apache-maven-3.5.0\bin;C:\Program Files\Java\jdk1.8.0_131\bin;%PATH%
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_131
mvn clean package
```

## Run

```bash
set PATH=C:\Program Files\Java\jdk1.8.0_131\bin;%PATH%
java -jar .\target\finance-telegram-bot-0.0.1.jar <OPTIONS>
```

### OPTIONS

`--a=alias1`

`--a` _alias_

Alias of the next properties file declared.

#### Example usage

`--a=test --f=test-application.properties --a=uat --f=uat-application.properties --a=preprod --f=preprod-application.properties --a=prod --f=prod-application.properties --m=tabular --o=diffs.html`

Will analyze the files _test-application.properties_ (test), _uat-application.properties_ (uat), _preprod-application.properties_ (preprod), _prod-application.properties_ (prod), printing the results to file _diffs.html_.