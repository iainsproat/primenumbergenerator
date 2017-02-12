# Prime Number Generator
Calculates prime numbers between two given numbers

## Usage

### Command line

    > java -jar target\primenumbergenerator-0.0.1-SNAPSHOT.jar --strategy brute --lower 2 --upper 11
    
A comma delimited array of prime numbers between the lower and upper bounds are returned. In this example `3, 5, 7` would be returned.
    
The `--strategy` or `-s` parameter is used to select the algorithm for calculating prime numbers within the given range.  Current available algorithms are:

* `brute`, a naive brute force algorithm

The `--lower` or `-l` parameter expect a positive integer which is to be the lower bound of the range.

The `--upper` or `-u` parameter expect a positive integer which is to be the lower bound of the range.

### REST API

1. Start the Webserver Java application

    > java -jar target\primenumbergenerator.server-0.0.1-SNAPSHOT.jar
    
2. Send a GET request (or navigate your browser) to `http://localhost:4567/primes/brute?lower=2&upper=11`
3. The server will respond with the following JSON formatted data:

    > {
    >     "timeStamp":152000000,
    >     "strategy":"BruteForcePrimeNumberGenerator",
    >     "lowerBound":2,
    >     "upperBound":11,
    >     "primes":[3,5,7],
    >     "duration":0
    > }

## Building

### Prerequisites

* Oracle JDK 8
* Maven (version 3.6.1 or greater)

### Build instructions

1. Run `mvn test -B -f ./src/pom.xml` to test
2. Run `mvn package -B -Dmaven.test.skip=true -f ./src/pom.xml ` to produce a jar

This is currently automated by Travis CI, see `.travis.yml`

## Development

See `development.md`

## Status

[![Build Status](https://travis-ci.com/iainsproat/primenumbergenerator.svg?token=rYjyitVciFC8MzR7fqgy&branch=master)](https://travis-ci.com/iainsproat/primenumbergenerator)
