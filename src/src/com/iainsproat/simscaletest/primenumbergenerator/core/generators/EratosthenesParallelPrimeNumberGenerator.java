package com.iainsproat.simscaletest.primenumbergenerator.core.generators;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
/**
 * Sieve of eratosthenes
 * Parallel
 */
public class EratosthenesParallelPrimeNumberGenerator implements PrimeNumberGeneratorStrategy {

// From Wikipedia:
//  Create a list of consecutive integers from 2 through n: (2, 3, 4, ..., n).
//	Initially, let p equal 2, the smallest prime number.
//	Enumerate the multiples of p by counting to n from 2p in increments of p, and mark them in the list (these will be 2p, 3p, 4p, ...; the p itself should not be marked).
//	Find the first number greater than p in the list that is not marked. If there was no such number, stop. Otherwise, let p now equal this new number (which is the next prime), and repeat from step 3.
//	When the algorithm terminates, the numbers remaining not marked in the list are all the primes below n.
	
	private static int NUMBER_OF_PARALLEL_THREADS = 6;
	private static int MAX_CHUNK_SIZE = 50;
	
	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		
		Map<Integer, BitSet> memoryChunks = new HashMap<Integer, BitSet>();
		
		for(int i = 0; i * MAX_CHUNK_SIZE < upperBound; i++){
			int chunkSize = MAX_CHUNK_SIZE;
			int remainingRangeToChunk = upperBound - i * MAX_CHUNK_SIZE;
			if(remainingRangeToChunk < MAX_CHUNK_SIZE){
				chunkSize = remainingRangeToChunk;
			}
			
			memoryChunks.put(i * MAX_CHUNK_SIZE, initializePrimeArray(chunkSize, i == 0));
		}
		
		final LinkedList<Integer> primes = new LinkedList<Integer>();
		primes.add(2);
		
		ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_THREADS);
		try{
			while(primes.getFirst()*primes.getFirst() <= upperBound){ //we only have to check primes up to the square route of the upperBound
				System.out.println(String.format("primes : %s", primes.toString()));
				List<Future> futures = new ArrayList<Future>(NUMBER_OF_PARALLEL_THREADS);
				
				memoryChunks.forEach((offset, chunk) -> {
					System.out.println(String.format("there is a chunk at offset %d", offset));
					futures.add(exec.submit(new Runnable() {
						@Override
						public void run() {
							System.out.println(String.format("running %s from offset %d", primes.toString(), offset));
							for(int prime : primes){
								sieveAllMultiplesOfPrime(chunk, prime, offset, upperBound);
								System.out.println(String.format("Sieved %d from chunk from offset %d: %s ", prime, offset, chunk.toString()));
							}
						}
					}));
				});
				
				waitUntilAllThreadsFinished(futures);

				System.out.println(String.format("find next set of primes"));
				int previousPrime = primes.getLast();
				System.out.println(String.format("previousPrime : %d", previousPrime));
				
				createBatchOfNextUnsievedPrimes(primes, memoryChunks, previousPrime); //FIXME need to move into next chunk

				if(primes.size() == 0) //no further primes to sieve within the upper bound
				{
					break; //TODO move on to next chunk
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			exec.shutdown();
		}
		
		memoryChunks.forEach((k, v) -> {
			System.out.println(String.format("offset %d, array %s", k, v.toString()));
		});
		
		return convertMemoryChunksToIntegerList(memoryChunks, lowerBound, upperBound); //FIXME only gets the first chunk
	}


	private void waitUntilAllThreadsFinished(List<Future> futures) throws InterruptedException, ExecutionException {
		boolean allFuturesFinished = false;
		while(!allFuturesFinished){
			allFuturesFinished = true;
			for(Future future : futures){
				if(future.get() != null){
					allFuturesFinished = false;
					break;
				}
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
		System.out.println(String.format("Sieving %d from offset %d", prime, offset));
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
	
	
	protected void createBatchOfNextUnsievedPrimes(LinkedList<Integer> primes, Map<Integer,BitSet> memoryChunks, Integer previousLocation) {
		
		while (!primes.isEmpty()) {
			primes.removeFirst();
	    }
		
		int i = 0;
		for(Map.Entry<Integer, BitSet> entry : memoryChunks.entrySet()){
			int offset = entry.getKey();
			BitSet chunk = entry.getValue();
			while(i < NUMBER_OF_PARALLEL_THREADS){
				int candidateForPrime = findNextAvailableUnmarkedNumber(chunk, previousLocation, offset);

				if(candidateForPrime - offset == -1){ // we couldn't find a new prime, so must have exhausted all candidates in this chunk
					break;
				}

				if(isMultipleOfUnsievedPrime(primes, i, candidateForPrime))
				{
					//this candidate is a multiple of a prime that hasn't been sieved yet, so move to the next candidate
					previousLocation = candidateForPrime;
					continue;
				}

				//none of the previously selected primes were factors of the next candidate, so we can add it.
				primes.add(candidateForPrime);
				i++;

				previousLocation = candidateForPrime; //move on to find the next one.
			}
		}
	}
	
	/**
	 * Slightly brute force check to determine whether the next candidate is actually a prime,
	 * or if it is a multiple of other candidate primes (and just hasn't been marked off yet). 
	 * As we're only building a small number of primes, we'll accept the performance hit.
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
	 * Finds the next prime number in the array
	 * 
	 * @return the next prime number, but if no further prime number is found it returns the starting prime number
	 */
	protected int findNextAvailableUnmarkedNumber(BitSet isPrime, int currentLocation, int offset)
	{
		//find next prime number in index. start at the next number from the last prime
		return offset + isPrime.nextSetBit(currentLocation+1);
	}
	
	/**
	 * Converts the boolean array, where a true indicates a prime number, into a list of just the prime integers
	 */
	protected List<Integer> convertMemoryChunksToIntegerList(Map<Integer, BitSet> memoryChunks, int lowerBound, int upperBound)
	{
		List<Integer> result = new ArrayList<Integer>();
		
		memoryChunks.forEach((offset, chunk) -> {
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
		});
		
		System.out.println(String.format("Result : %s", result.toString()));
		return result;
	}
	
	protected BitSet initializePrimeArray(int upperBound, boolean containsZeroAndOne)
	{
		BitSet primeArray = new BitSet(upperBound); //all initialized to false
		if(containsZeroAndOne)
			primeArray.set(2, upperBound); //set to true between 2 and upperBound
		else
			primeArray.set(0, upperBound); //set all to true
		
		return primeArray;
	}

}
