package com.iainsproat.simscaletest.primenumbergenerator.core.generators;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
/**
 * Sieve of eratosthenes
 * Parallel implementation.
 * BitSet is split into multiple chunks to allow multiple threads to work in parallel
 * Chunking is required so that access to BitSet remains synchronised and only one thread has access at a time.
 */
public class EratosthenesParallelPrimeNumberGenerator implements PrimeNumberGeneratorStrategy {
	private static int NUMBER_OF_PARALLEL_THREADS = 6;
	private static int MAX_CHUNK_SIZE = 500;
	private static int BATCH_TIMEOUT_SECONDS = 1 * 60; //timeout for each batch
	
	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		
		Map<Integer, BitSet> memoryChunks = partitionMemory(lowerBound, upperBound);
		
		final LinkedList<Integer> primesToSieve = new LinkedList<Integer>();
		primesToSieve.add(2); //start by sieving 2
		
		ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_THREADS);
		try{
			while(primesToSieve.getFirst()*primesToSieve.getFirst() <= upperBound){ //we only have to check primes up to the square route of the upperBound
//				System.out.println(String.format("primes : %s", primesToSieve.toString()));
				List<Future> futures = sieveAllMemoryChunksInParallel(exec, memoryChunks, primesToSieve, upperBound);
				
				// Forces this thread to wait until all sieves in the batch have completed.
				// this is necessary as otherwise it's inefficient to try to find new primes if there are still some sieves to be applied
				waitUntilAllThreadsFinished(futures, BATCH_TIMEOUT_SECONDS);
				
				createBatchOfNextUnsievedPrimes(primesToSieve, memoryChunks);

				if(primesToSieve.size() == 0) //no further primes to sieve within the upper bound
				{
					break;
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();  //FIXME return error message
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); //FIXME return error message
		} finally {
			exec.shutdown();
		}
		
		return convertMemoryChunksToIntegerList(memoryChunks, lowerBound, upperBound);
	}


	protected List<Future> sieveAllMemoryChunksInParallel(ExecutorService exec, Map<Integer, BitSet> memoryChunks, LinkedList<Integer> primesToSieve, int upperBound) {
		List<Future> futures = new ArrayList<Future>(NUMBER_OF_PARALLEL_THREADS);
		
		memoryChunks.forEach((offset, chunk) -> {
			futures.add(exec.submit(new Runnable() {
				@Override
				public void run() {
					for(int prime : primesToSieve){
						sieveAllMultiplesOfPrime(chunk, prime, offset, upperBound);
//						System.out.println(String.format("Sieved %d from chunk from offset %d: %s ", prime, offset, chunk.toString()));
					}
				}
			}));
		});
		
		return futures;
	}


	/**
	 * Creates multiple BitSets to represent a block of integers.  The blocks are created sequentially from 0 to the upper bound of the user defined range.
	 * The blocks are stored in a map, where the key is the offset of that chunk's zero-index compared to absolute 0.
	 * @param upperBound The user defined upper bound of the range
	 * @return A map of each memory chunk keyed by its offset from 0
	 */
	private Map<Integer, BitSet> partitionMemory(int lowerBound, int upperBound) {
		Map<Integer, BitSet> memoryChunks = new HashMap<Integer, BitSet>();
		
		for(int i = 0; i * MAX_CHUNK_SIZE < upperBound; i++){
			int chunkSize = MAX_CHUNK_SIZE;
			int offset = i * MAX_CHUNK_SIZE;
			
			//deal with the case where the last chunk in the range may not need to be the full maximum chunk size
			int remainingRangeToChunk = upperBound - i * MAX_CHUNK_SIZE;
			if(remainingRangeToChunk < MAX_CHUNK_SIZE){
				chunkSize = remainingRangeToChunk;
			}
			
			//deal with the case where the chunk is above the square root of the upper bound (and so the primes within this chunk don't need to be applied to the sieve)
			//but is below the lower bound of the range, so no values will ever be returned from this chunk.  In this case we don't need to create a chunk at all.
			if((offset * offset > upperBound) && //start of chunk is greater than square route of upper bound
					(offset + chunkSize < lowerBound)) //end of chunk is less than the lower bound
			{
				continue; //we can ignore this chunk
			}
			
			memoryChunks.put(offset, initializeChunk(chunkSize, offset == 0));
		}
		
		return memoryChunks;
	}


	/**
	 * Continuously loops until all futures in the list are completed.
	 * @param futures
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException 
	 */
	private void waitUntilAllThreadsFinished(List<Future> futures, int timeOutInSeconds) throws InterruptedException, ExecutionException, TimeoutException {
		Instant start = Instant.now();
		boolean allFuturesFinished = false;
		while(!allFuturesFinished){
			allFuturesFinished = true;
			for(Future future : futures){
				if(future.get() != null){
					allFuturesFinished = false;
					break;
				}
			}
			
			Instant current = Instant.now();
			long duration = Duration.between(start, current).getSeconds();
			if(duration > timeOutInSeconds){
				throw new TimeoutException("A batch of sieving undertaken in the parallel implementation of Sieve of Eratosthenes timed out");
			}
		}
	}


	/**
	 * 
	 * @param isPrime passed by reference and modified by this method
	 * @param prime
	 * @param upperBound
	 */
	protected void sieveAllMultiplesOfPrime(BitSet isPrime, int prime, int offset, int upperBound)
	{
//		System.out.println(String.format("Sieving %d from offset %d", prime, offset));
		int relativeLocationOfFirstMultipleOfPrimeAfterOffset = 2*prime;
		if(offset > 0){
			if(offset % prime > 0){
				relativeLocationOfFirstMultipleOfPrimeAfterOffset = prime - offset % prime;
			} else {
				relativeLocationOfFirstMultipleOfPrimeAfterOffset = 0;
			}
		}
		
		for(int i = relativeLocationOfFirstMultipleOfPrimeAfterOffset; i+offset < upperBound; i += prime){
			isPrime.clear(i); //mark this multiple of a prime as a non-prime
		}
		//isPrime is passed by reference, so no need to return anything
	}
	
	/**
	 * 
	 * @param primesToSieve the previous list of primes to sieve
	 * @param memoryChunks 
	 * @param previousLocation
	 */
	protected void createBatchOfNextUnsievedPrimes(LinkedList<Integer> primesToSieve, Map<Integer,BitSet> memoryChunks) {
		int previousPrime = primesToSieve.getLast();
		
		//clear the previously sieved primes from the list
		while (!primesToSieve.isEmpty()) {
			primesToSieve.removeFirst();
	    }
		
		int i = 0;
		for(int offset : memoryChunks.keySet()){
			if(i >= NUMBER_OF_PARALLEL_THREADS) //we've found all the necessary primes for this batch, so move on to the next
				break;
			
			if(previousPrime > offset + MAX_CHUNK_SIZE) //the previous prime 
				continue;
			
			BitSet chunk = memoryChunks.get(offset);
			while(i < NUMBER_OF_PARALLEL_THREADS){
				int candidateForPrime = findNextAvailableUnmarkedNumber(chunk, previousPrime, offset);

				if(candidateForPrime - offset == -1){ // we couldn't find a new prime, so must have exhausted all candidates in this chunk
					break;
				}

				if(isMultipleOfUnsievedPrime(primesToSieve, i, candidateForPrime))
				{
					//this candidate is a multiple of a prime that hasn't been sieved yet, so move to the next candidate
					previousPrime = candidateForPrime;
					continue;
				}

				//none of the previously selected primes were factors of the next candidate, so we can add it.
				primesToSieve.add(candidateForPrime);
				i++;

				previousPrime = candidateForPrime; //move on to find the next one.
			}
		}
	}
	
	/**
	 * Check to determine whether the next candidate is actually a prime.
	 * The candidate number might be a multiple of other candidate primes (which haven't been marked off yet). 
	 * This is slightly brute force, but as we're only building a small number of primes, we'll accept the performance hit.
	 * @return
	 */
	private boolean isMultipleOfUnsievedPrime(Queue<Integer> nextCandidates, int currentIndexOfNextCandidates, int nextCandidateForPrime) {
		boolean isMultipleOfPreviousPrime = false;
		for(int previouslySelectedPrime : nextCandidates) //search through all previous prime numbers saved in array
		{
			//TODO could probably optimise this check?
			if(nextCandidateForPrime % previouslySelectedPrime == 0){ //the next candidate would be marked off as a multiple of one of the previously selected primes
				isMultipleOfPreviousPrime = true;
				break;
			}
		}
		
		return isMultipleOfPreviousPrime;
	}


	/**
	 * Finds the next prime number in the BitSet
	 * 
	 * @return the next prime number, but if no further prime number is found it returns the starting prime number
	 */
	protected int findNextAvailableUnmarkedNumber(BitSet isPrime, int currentLocation, int offset)
	{
		//find next prime number in index. start at the next number from the last prime
		return offset + isPrime.nextSetBit(currentLocation+1);
	}
	
	/**
	 * Converts the Map of BitSets, where a true indicates a prime number, into an integer list of prime numbers
	 */
	protected List<Integer> convertMemoryChunksToIntegerList(Map<Integer, BitSet> memoryChunks, int lowerBound, int upperBound)
	{
		List<Integer> result = new ArrayList<Integer>();
		
		for(Map.Entry<Integer, BitSet> entry : memoryChunks.entrySet()){ //Java 8 Map.forEach does not guarantee order, so using the for loop to ensure primes are ordered.
			int offset = entry.getKey();
			BitSet chunk = entry.getValue();
			
			int firstPrime = lowerBound - offset + 1;
			if(firstPrime < 0){
				firstPrime = 0;
			}
			
			for (int i = chunk.nextSetBit(firstPrime); i >= 0; i = chunk.nextSetBit(i+1)) {
				result.add(i+offset);
				if (i == Integer.MAX_VALUE) {
					break; // or (i+1) would overflow
				}
			}
		}
		
//		System.out.println(String.format("Result : %s", result.toString()));
		return result;
	}
	
	/**
	 * Initialize the chunk. For sieve of Eratosthenes this is done by setting all to true (i.e. assumes all integers are primes)
	 * with the exception of 0 and 1, if this is the first chunk (i.e. offset of zero) 
	 * @param upperBound
	 * @param containsZeroAndOne
	 * @return
	 */
	protected BitSet initializeChunk(int upperBound, boolean containsZeroAndOne)
	{
		BitSet primeArray = new BitSet(upperBound); //all initialized to false
		if(containsZeroAndOne)
			primeArray.set(2, upperBound); //set to true between 2 and upperBound
		else
			primeArray.set(0, upperBound); //set all to true
		
		return primeArray;
	}

}
