### Hexlet tests and linter status:
[![Actions Status](https://github.com/VictorGotsenko/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/VictorGotsenko/java-project-72/actions)
[![Page Validator](https://github.com/VictorGotsenko/java-project-72/actions/workflows/JavaCI.yml/badge.svg)](https://github.com/VictorGotsenko/java-project-72/actions/workflows/JavaCI.yml)
## Description
#### Application for analyzing links for SEO suitability, is enable on <a href="https://pagesanalyzer.onrender.com" target="_blank">Link Page Analyzer</a>
Приложение – сайт, который анализирует указанные страницы на SEO пригодность.

![Main page screen](app/img/page1.png)  
![Site page screen](app/img/page2.png)

### How to use:
```shell
make build-run
```

+ Used technologies:
  - Frontend: Bootstrap, JTE
  - Backend: Java, Javalin, LOMBOK, PostgreSQL, H2database, unirest-java, jsoup
  - Tests: JUnit, MockWebServer
