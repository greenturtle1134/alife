package genome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.random.RandomGenerator;

/**
 * An object representing the DNA sequence.
 * The DNA is represented as a bit string with 2 bits assigned to each nucleotide, stored with an array of ints.
 * 
 * This file contains the logic for start/end recognition and splicing.
 */
public class DNA {
	public static final char[] DEFAULT_NUCLEOTIDES = {'A', 'T', 'G', 'C'};
//	public static final char[] NUCLEOTIDES = {'K', 'X', 'P', 'M'};
//	public static final char[] NUCLEOTIDES = {'E', 'J', 'P', 'X'};
	
	public static final int[] TATA = {1, 0, 1, 0, 0, 0}; // currently for testing purposes
	public static final int STOP_CODON = 19;
	
	private byte[] dna;
	
	private DNA(byte[] dna) {
		this.dna = dna;
	}

	/**
	 * Retrieves a nucleotide
	 * @param i - the index within the DNA to retrieve
	 * @return int value representing the nucleotide (0-3)
	 */
	public int idx(int i) {
		if (i < 0 || i >= this.length()) {
			throw new IndexOutOfBoundsException("Index " + i + " out of bounds for length " + dna.length);
		}
		return dna[i];
	}

	/**
	 * Retrieves a codon (group of 3 nucleotides).
	 * Note that the endianess is reversed (i.e. the first nucleotide is the most significant bit in the output, but the least significant internally).
	 * @param i - the starting index for the codon
	 * @return int value representing the codon
	 */
	public int idxCodon(int i) {
		return (idx(i) << 4) + (idx(i+1) << 2) + idx(i+2);
	}
	
	/**
	 * Gets the length of the DNA sequence
	 */
	public int length() {
		return dna.length;
	}
	
	/**
	 * Displays the DNA as a string using the supplied nucleotide names
	 * @param nucleotides - nucleotide names to use
	 * @return The string representation
	 */
	public String toString(char[] nucleotides) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<this.length(); i++) {
			int a = idx(i);
			res.append(nucleotides[a]);
		}
		return res.toString();
	}
	
	/**
	 * Displays the DNA as a string using default nucleotide names
	 * @return The string representation
	 */
	public String toString() {
		return this.toString(DEFAULT_NUCLEOTIDES);
	}
	
	/**
	 * Returns a copy of the byte array corresponding to this DNA
	 * @return byte array
	 */
	public byte[] toBytes() {
		return this.dna.clone();
	}
	
	/**
	 * Takes a subsequence between a start and end point
	 * @param a - the start point
	 * @param b - the end point
	 * @return The subsequence
	 */
	public DNA substring(int a, int b) {
		return new DNA(Arrays.copyOfRange(dna, a, b)); // Autofill actually got this exactly right, wow
	}
	
	/**
	 * Takes a subsequence from a start point to the end of the sequence
	 * @param a - the start point
	 * @return The subsequence
	 */
	public DNA substring(int a) {
		return substring(a, length());
	}
	
	/**
	 * Produces the reverse of this DNA fragment
	 * @return the reversed DNA
	 */
	public DNA reverse() {
		byte[] res = new byte[this.length()];
		for (int i = 0; i<res.length; i++) {
			res[res.length - i] = dna[i];
		}
		return DNA.fromBytes(res);
	}
	
	/**
	 * Joins two DNA sequences together. Equivalent to calling joinAll on an array of length 2.
	 * @param a - first sequence
	 * @param b - second sequence
	 * @return The joined sequence
	 */
	public static DNA join(DNA a, DNA b) {
		DNA[] inputs = {a, b};
		return DNA.joinAll(inputs);
	}
	
	/**
	 * Joins three DNA sequences together. Equivalent to calling joinAll on an array of length 3.
	 * This is going to occur often enough that I feel it deserves its own method variant.
	 * @param a - first sequence
	 * @param b - second sequence
	 * @param c - third sequence
	 * @return The joined sequence
	 */
	public static DNA join(DNA a, DNA b, DNA c) {
		DNA[] inputs = {a, b, c};
		return DNA.joinAll(inputs);
	}
	
	/**
	 * Joins a sequence of DNA objects together
	 * @param dnas - array of DNAs
	 * @return The joined DNA
	 */
	public static DNA joinAll(DNA[] dnas) {
		int sum = 0;
		int[] lens = new int[dnas.length];
		for (int i=0; i<dnas.length; i++) {
			lens[i] = dnas[i].length();
			sum += dnas[i].length();
		}
		
		byte[] res = new byte[sum];
		sum = 0;
		for (int i=0; i<dnas.length; i++) {
			for (int j=0; j<dnas[i].length(); j++) {
				res[j+sum] = dnas[i].dna[j];
			}
			sum += dnas[i].length();
		}
		return new DNA(res);
	}
	
	/**
	 * Parses a String to a DNA object using the supplied nucleotides
	 * @param s - the string to parse
	 * @param nucleotides - nucleotide names to use
	 * @return DNA object
	 */
	public static DNA stringToDNA(String s, char[] nucleotides) {
		s = s.toUpperCase().replaceAll("[^ATGC]", "");
		byte[] res = new byte[s.length()];
		for (int i = 0; i<s.length(); i++) {
			for (byte x = 0; x<4; x++) {
				if (nucleotides[x] == s.charAt(i)) {
					res[i] = x;
					break;
				}
			}
		}
		return new DNA(res);
	}
	
	/**
	 * Parses a String to a DNA object using the DEFAULT_NUCLEOTIDES set
	 * @param s - the string to parse
	 * @return DNA object
	 */
	public static DNA stringToDNA(String s) {
		return stringToDNA(s, DEFAULT_NUCLEOTIDES);
	}
	
	/**
	 * Converts a byte array to a DNA object.
	 * @param bytes - the array to convert. Bytes larger than 3 have the excess bits removed
	 * @return DNA object
	 */
	public static DNA fromBytes(byte[] bytes) { 
		for (int i=0; i<bytes.length; i++) {
			if (bytes[i] > 3) {
				bytes[i] = (byte) (bytes[i] & 3);
			}
		}
		return new DNA(bytes);
	}
	
	/**
	 * Generates a random DNA of specified length
	 * @param length - length of DNA to generate
	 * @param rng - random generator to use
	 * @return the generated DNA
	 */
	public static DNA random(int length, RandomGenerator rng) {
		byte[] res = new byte[length];
		for (int i = 0; i<length; i++) {
			res[i] = (byte) rng.nextInt(4);
		}
		return DNA.fromBytes(res);
	}
	
	/**
	 * Generates a random DNA of the same length as a given one, where every nucleotide is different from the given one
	 * @param dna - DNA to be different from
	 * @param rng - random generator to use
	 * @return the generated DNA
	 */
	public static DNA randomUnequal(DNA dna, RandomGenerator rng) {
		byte[] res = new byte[dna.length()];
		for (int i = 0; i<res.length; i++) {
			res[i] = (byte) ((rng.nextInt(3) + dna.dna[i]) % 4);
		}
		return DNA.fromBytes(res);
	}
	
	/**
	 * Helper class representing the start and end of a splice
	 */
	public class SplicePair {
		private final int start, end;
		
		public SplicePair(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public String toString() {
			return "("+start+", "+end+")";
		}
		
		public int codonLength() {
			return (this.end-this.start) / 3;
		}
	}
	
	/**
	 * Finds "starts" based on a substring search
	 * @param startSequence - 
	 * @return ArrayList of the located starts, each index is the first nucleotide after the start sequence
	 */
	public ArrayList<Integer> findStarts(int[] startSequence) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < length()-startSequence.length; i++) {
			boolean skip = false;
			for (int j = 0; j < startSequence.length; j++) {
				if (idx(i+j) != startSequence[j]) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				res.add(i + startSequence.length);
			}
		}
		return res;
	}
	
	/**
	 * Finds splices (subsegments of DNA beginning with the start sequence and terminating on a stop codon)
	 * @param startSequence - Array of nucleotides defining the start sequence to look for
	 * @return Array of SplicePair objects representing all splices in the DNA
	 */
	public SplicePair[] findSplices(int[] startSequence) {
		ArrayList<SplicePair> res = new ArrayList<SplicePair>();
		ArrayList<Integer> starts = findStarts(startSequence);
		for (Integer i : starts) {
			if (i < length() - 2) {
				int end = i + 3;
				while (end < length()-2 && idxCodon(end) != STOP_CODON) {
					end += 3;
				}
				res.add(new SplicePair(i, end));
			}
		}
		
		return res.toArray(new SplicePair[res.size()]);
	}
	
	/**
	 * Takes a list of splices and applies them to create a codon list
	 * @param splices - Array of SplicePair objects, such as produced by findSplices()
	 * @return Array of bytes representing the list of codons produced
	 */
	public byte[] makeSplices(SplicePair[] splices) {
		int resLength = 0;
		for (SplicePair s : splices) {
			resLength += s.codonLength();
		}
		byte[] res = new byte[resLength];
		int a = 0;
		for (SplicePair s : splices) {
			for (int i = s.start; i < s.end; i+=3) {
				res[a] = (byte) idxCodon(i);
				a++;
			}
		}
		return res;
	}
}
