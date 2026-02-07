package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import genome.DNA;
import genome.Program;

public class ProgramHelper {
	public static final Map<String, Integer> COMMAND_TO_CODON = new HashMap<String, Integer>();
	
	public static final Pattern NUMBER = Pattern.compile("-?\\d+");
	public static final Pattern ADDRESS = Pattern.compile("[x$#]?-?\\d+");
	
	static {
		COMMAND_TO_CODON.put("ans", 3);
		COMMAND_TO_CODON.put("repro", 4);
		COMMAND_TO_CODON.put("allstop", 5);
		COMMAND_TO_CODON.put("breakties", 6);
		COMMAND_TO_CODON.put("action", 7);
		COMMAND_TO_CODON.put("label", 8);
		COMMAND_TO_CODON.put("jumpl", 9);
//		COMMAND_TO_CODON.put("jumpd", 10);
//		COMMAND_TO_CODON.put("jumpn", 11);
		COMMAND_TO_CODON.put("skip", 12);
		COMMAND_TO_CODON.put("backskip", 13);
		COMMAND_TO_CODON.put("wait", 14);
		COMMAND_TO_CODON.put("unskip", 15);
		COMMAND_TO_CODON.put("nop", 16);
		COMMAND_TO_CODON.put("endop", 17);
		COMMAND_TO_CODON.put("endline", 18);
		COMMAND_TO_CODON.put("stop", 19);
		COMMAND_TO_CODON.put("int", 20);
		COMMAND_TO_CODON.put("negint", 21);
		COMMAND_TO_CODON.put("int64", 22);
		COMMAND_TO_CODON.put("negint64", 23);
		COMMAND_TO_CODON.put("neg", 24);
		COMMAND_TO_CODON.put("inc", 25);
		COMMAND_TO_CODON.put("not", 26);
//		COMMAND_TO_CODON.put("pow2", 27);
		COMMAND_TO_CODON.put("double", 28);
		COMMAND_TO_CODON.put("half", 29);
//		COMMAND_TO_CODON.put("times64", 30);
//		COMMAND_TO_CODON.put("div64", 31);
		COMMAND_TO_CODON.put("readcodon", 32);
		COMMAND_TO_CODON.put("readc", 32);
		COMMAND_TO_CODON.put("readint", 33);
		COMMAND_TO_CODON.put("readx", 33);
		COMMAND_TO_CODON.put("amount", 34);
		COMMAND_TO_CODON.put("sense", 35);
		COMMAND_TO_CODON.put("add", 36);
		COMMAND_TO_CODON.put("sub", 37);
		COMMAND_TO_CODON.put("mult", 38);
		COMMAND_TO_CODON.put("div", 39);
		COMMAND_TO_CODON.put("and", 40);
		COMMAND_TO_CODON.put("or", 41);
		COMMAND_TO_CODON.put("eq", 42);
		COMMAND_TO_CODON.put("neq", 43);
		COMMAND_TO_CODON.put("xor", 43);
		COMMAND_TO_CODON.put("lt", 44);
		COMMAND_TO_CODON.put("gt", 45);
		COMMAND_TO_CODON.put("leq", 46);
		COMMAND_TO_CODON.put("geq", 47);
		COMMAND_TO_CODON.put("moveup", 48);
		COMMAND_TO_CODON.put("movedown", 49);
		COMMAND_TO_CODON.put("moveleft", 50);
		COMMAND_TO_CODON.put("moveright", 51);
		COMMAND_TO_CODON.put("turnright", 52);
		COMMAND_TO_CODON.put("turnleft", 53);
		COMMAND_TO_CODON.put("fineright", 54);
		COMMAND_TO_CODON.put("fineleft", 55);
		COMMAND_TO_CODON.put("storec", 56);
		COMMAND_TO_CODON.put("storecodon", 56);
		COMMAND_TO_CODON.put("storex", 57);
		COMMAND_TO_CODON.put("storeint", 57);
//		COMMAND_TO_CODON.put("addtoc", 58);
//		COMMAND_TO_CODON.put("addtox", 59);
		COMMAND_TO_CODON.put("build", 60);
		COMMAND_TO_CODON.put("burn", 61);
		COMMAND_TO_CODON.put("expel", 62);
		COMMAND_TO_CODON.put("attack", 63);
	}
	
	public static int[] stringsToCodons(String[] strings) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (String s : strings) {
			s = s.toLowerCase();
			if (NUMBER.matcher(s).matches()) {
				int x = Integer.parseInt(s);
				if (0 <= x && x <= 2) {
					// Literal codons exist for these
					res.add(x);
				}
				else if (0 <= x && x < 64) {
					res.add(COMMAND_TO_CODON.get("int"));
					res.add(x);
				}
				else if (-64 < x && x < 0) {
					res.add(COMMAND_TO_CODON.get("negint"));
					res.add(x);
				}
			}
			else if (ADDRESS.matcher(s).matches()) {
				int x = Integer.parseInt(s.substring(1));
				if (0 <= x && x < 64) {
					res.add(x);
				}
			}
			else if (COMMAND_TO_CODON.containsKey(s)){
				res.add(COMMAND_TO_CODON.get(s));
			}
			else {
				System.out.println("Could not parse: " + s);
			}
		}
		int[] res1 = new int[res.size()];
		for (int i=0; i<res1.length; i++) {
			res1[i] = res.get(i);
		}
		return res1;
	}
	


	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		StringBuilder string = new StringBuilder();

		while (true) {
		    String input = scanner.nextLine();

		    if (input.length() == 0) {
		    	break;
		    }
		    else {
		    	string.append(input);
		    	string.append(" ");
		    }
		}
		
		System.out.println("Compiling program");
		String[] strings = string.toString().split("\\s+");
		System.out.println("Input: " + Arrays.toString(strings));
		int[] codons = stringsToCodons(strings);
		System.out.println("Codons: " + Arrays.toString(codons));
		byte[] res = new byte[codons.length * 3];
		for (int i = 0; i<codons.length; i++) {
			res[3*i] = (byte) ((codons[i] >> 4) & 3);
			res[3*i+1] = (byte) ((codons[i] >> 2) & 3);
			res[3*i+2] = (byte) (codons[i] & 3);
		}
		DNA result = DNA.join(DNA.stringToDNA("TATAAA"), DNA.fromBytes(res));
		System.out.println("DNA: " + result);
		Program testProgram = Program.parseProgram(result);
		System.out.println("Program: " + Arrays.toString(testProgram.getStatements()));
		
		scanner.close();
	}
}
