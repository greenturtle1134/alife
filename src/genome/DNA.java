package genome;

import java.util.ArrayList;

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
	
	public static final int[] TATA = {1, 0, 1, 0, 0, 0}; // TODO currently for testing purposes
	public static final int STOP_CODON = 3;
	
	private int[] dna;
	private int length;
	
	public DNA(int[] dna, int length) {
		// TODO: Throw an exception if array length does not match stated length
		this.dna = dna;
		this.length = length;
	}

	/**
	 * Retrieves a nucleotide
	 * @param i - the index within the DNA to retrieve
	 * @return int value representing the nucleotide (0-3)
	 */
	public int idx(int i) {
		// TODO: throw an exception if index out of bounds
		return dna[i / 16] >> 2*(i % 16) & 3;
	}

	/**
	 * Retrieves a codon (group of 3 nucleotides).
	 * Note that the endianess is reversed (i.e. the first nucleotide is the most significant bits).
	 * @param i - the starting index for the codon
	 * @return int value representing the codon
	 */
	public int idxCodon(int i) {
		return (idx(i) << 4) + (idx(i+1) << 2) + idx(i+2);
	}
	
	/**
	 * Gets the length of the DNA sequence
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Displays the DNA as a string using the supplied nucleotide names
	 * @param nucleotides - nucleotide names to use
	 * @return The string representation
	 */
	public String toString(char[] nucleotides) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<length; i++) {
			int a = idx(i);
			res.append(nucleotides[a]);
		}
		return res.toString();
	}
	
	public String toString() {
		return this.toString(DEFAULT_NUCLEOTIDES);
	}
	
	/**
	 * Parses a String to a DNA object using the supplied nucleotides
	 * @param s - the string to parse
	 * @param nucleotides - nucleotide names to use
	 * @return DNA object
	 */
	public static DNA stringToDNA(String s, char[] nucleotides) {
		s = s.toUpperCase().replaceAll("[^ATGC]", "");
		int[] res = new int[(s.length()-1) / 16 + 1];
		for (int i = 0; i<s.length(); i++) {
			for (int x = 0; x<4; x++) {
				if (nucleotides[x] == s.charAt(i)) {
					res[i / 16] += x << 2*(i % 16);
					break;
				}
			}
		}
		return new DNA(res, s.length());
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
		for (int i = 0; i < length-startSequence.length; i++) {
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
			if (i < length - 2) {
				int end = i + 3;
				while (end < length-2 && idxCodon(end) != STOP_CODON) {
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
