# Prime Number Generator
Calculates prime numbers in the range between two given numbers.

## Usage

### Command line

    > java -jar core\target\primenumbergenerator-0.0.1-SNAPSHOT.jar --strategy naive1 --lower 2 --upper 11
    
A comma delimited array of prime numbers between the lower and upper bounds are returned. In this example `3, 5, 7` would be returned.
    
The `--strategy` or `-s` parameter is used to select the algorithm for calculating prime numbers within the given range.  Current available algorithms are:

* `naive1`, a naive brute force algorithm
* `naive2`, a slightly less naive brute force algorithm
* `eratosthenes`, Sieve of Eratosthenes using a boolean array
* `eratosthenesbitset`, Sieve of Eratosthenes using a BitSet array
* `eratosthenesparallel`, parallel implementation of Sieve of Eratosthenes
* `sundaram`, Sieve of Sundaram

The `--lower` or `-l` parameter expect a positive integer which is to be the lower bound of the range.  This value is excluded from the range.

The `--upper` or `-u` parameter expect a positive integer which is to be the lower bound of the range.  This value is excluded from the range.

### REST API

1. Start the Webserver Java application

    > java -jar server\target\primenumbergenerator.server-0.0.1-SNAPSHOT.jar

2. Send a GET request (using curl) `curl "http://localhost:4567/primes/naive1?lower=3&upper=20" -H "Accept: application/json"`
3. The server will respond with the following JSON formatted data:

    > {
    >     "timeStamp":152000000,
    >     "strategy":"Naive1PrimeNumberGenerator",
    >     "lowerBound":2,
    >     "upperBound":11,
    >     "primes":[3,5,7],
    >     "duration":0
    > }

## Building

### Prerequisites

* Oracle JDK 8
* Maven (version 3.6.1 or greater)

### Build instructions for Command Line Application

1. Run `mvn test -B -f ./core/pom.xml` to test
2. Run `mvn package -B -Dmaven.test.skip=true -f ./core/pom.xml ` to produce a jar for the commandline application

### Build instructions for Web Server

1. Run `mvn clean install -B -Dmaven.test.skip=true -f ./core/pom.xml` to build and install the dependency
2. Run `mvn clean package -B -f ./server/pom.xml` to build and produce a jar for the web server

This is currently automated by [Travis CI](https://travis-ci.org/iainsproat/primenumbergenerator)

## Development

See `development.md`

## Status

[![Build Status](https://travis-ci.com/iainsproat/primenumbergenerator.svg?token=rYjyitVciFC8MzR7fqgy&branch=master)](https://travis-ci.com/iainsproat/primenumbergenerator)
