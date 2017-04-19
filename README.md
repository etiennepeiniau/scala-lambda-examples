# AWS Lambda examples coded in Scala 

The project is composed of tree examples:

* a simple echo lambda
* a "Hello World" lambda
* a tree lambda project based on [Http Status Cats](https://http.cat/)

## Build with sbt

```
sbt assembly
```

## Deploy

We use [Serverless Framework](https://serverless.com/) as deploy tool.

To install, Serverless Framework, you can use npm

```
npm install -g serverless
```

The Serverless Framework configuration for each project are placed in the directory ./serverless
