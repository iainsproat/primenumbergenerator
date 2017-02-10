# Part 1 - Prime Number Generator

Please provide us with a concise, efficient and robust prime number generator written in Java that gives all prime numbers in the range between two numbers provided by the user (e.g. user gives 1 and 10 and you return "2, 3, 5, 7"). 

You should offer the user at least 3 different prime number generation strategies with different performance optimizations. Please include some simple but slow strategy and some others that increase performance at the expense of increased complexity. Consider also the use of multiple cores: if there is a gain, you could offer also a parallel implementation. Think also about memory usage (besides of execution time).

You are allowed to take ideas from Internet or somewhere else, but not code. You should do the implementation yourself and from scratch. That said, using your own algorithms would be a nice extra.

Include some unit tests to validate the correctness of the program.

Make your program usable via the command-line, allowing the user to select the generation strategy.

# Part 2 - Prime Number Generator Server

Using the code from part 1 as the generation engine, write a server application (also in Java) giving the user the chance to use your prime number generator over a REST API over HTTP (API frameworks/libraries are allowed and encouraged) . 

For all users, record each execution in a database, including timestamp, range, time elapsed, algorithm chosen and number of primes returned. 

For simplicity of implementation, you can use an in-memory database, like (for example) HSQLDB.

# General notes

Please document your code clearly (but without redundancy). Assume that the code will be maintained in the future by people that won’t be able to contact you for explanations. Try to organize the implementation to make your intentions clear and, if necessary, add comments to make it explicit.

Please note that this assignment is about server code, which should run for a potentially long time. Take that into account when thinking about error (i.e., exceptions) and resource (i.e., database connections) management.
