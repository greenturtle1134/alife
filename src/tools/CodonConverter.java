package tools;

import java.util.Scanner;

import genome.DNA;

public class CodonConverter {
	
	public static int stringToInt(String s) {
		int res = 0;
		for (int i=0; i<s.length(); i++) {
			for (int x = 0; x<4; x++) {
				if (DNA.DEFAULT_NUCLEOTIDES[x] == s.charAt(i)) {
					res += x << 2*i;
				}
			}
		}
		return res;
	}
	
	public static String intToString(int x) {
		StringBuilder res = new StringBuilder();
		for (int i=0; i<3; i++) {
			res.append(DNA.DEFAULT_NUCLEOTIDES[x & 3]);
			x = x >> 2;
		}
		return res.toString();
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
		    String input = scanner.nextLine();

		    if (input.length() == 0) {
		    	System.out.println("Exiting.");
		    	break;
		    }
		    else if (input.matches("\\d+")) {
		        System.out.println("-> " + intToString(Integer.parseInt(input)));
		    } else if (input.matches("[ATGC]+")) {
		        System.out.println("-> " + stringToInt(input));
		    } else {
		        System.out.println("Invalid input.");
		    }
		}
		
		scanner.close();
	}

}
