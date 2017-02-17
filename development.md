# Development

## Stack

* Java 8
* [Maven](https://maven.apache.org)
* [JUnit](http://junit.org/junit4/)
* [JCommander](http://jcommander.org/)
* [Spark](http://sparkjava.com)
* [Gson](https://github.com/google/gson)
* [Sqlite](https://www.sqlite.org/)

## Project Layout

    +---core
    |   +---src
    |   |   \---com.iainsproat.simscaletest.primenumbergenerator
    |   |       +---commandline
    |   |       \---core
    |   \---test
    \---server
        +---src
        |   \---com.iainsproat.simscaletest.primenumbergenerator
        |       \---server
        \---test

The algorithms for calculating prime numbers are in the `core` package.  The `commandline` package sits alongside this to provide command line interface.  The `server` sits in a separate project, but references the `core` package.

## How to add a new Algorithm

The `core` package utilises the strategy design pattern, which allows new algorithms to be implemented by just doing the following:

1. Creating a class in the `core` package which implements the `PrimeNumberGeneratorStrategy` interface.
2. Adding a branch to the `switch` statement in the `StrategySelector` class.  The string used here will be the string which the user can select from either the command line or the GET REST endpoint.
3. Add the class to the `primeNumberGenerators` List in the `setUpBeforeClass` method in `PrimeNumberGeneratorImplementationsTest`, to be found in the `test` folder

(NB. As a future enhancement, Reflection / Dependency Injection could be used to remove the need for steps 2 & 3)

## Deployment

The application currently has only been designed for running on localhost/ developers machine. (Though it has been developed with, and unit-tested on, cloud-hosted Continuous Integration software (Travis CI))

The Web Server runs on the default embedded Jetty server provided by the Spark framework.  A [configuration file](http://sparkjava.com/documentation.html#other-webserver) would have to be created to make this work on a different server, and the Travis and/or Maven configurations similarly changed.

