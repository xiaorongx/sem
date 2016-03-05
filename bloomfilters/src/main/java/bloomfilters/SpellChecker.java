package bloomfilters;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
/**
 * 
 * @author xiaorong
 *
 */
public class SpellChecker {
	BloomFilters bfs;
	Charset cs = StandardCharsets.UTF_8;
	
	public SpellChecker(BloomFilters bfs) {
		this.bfs = bfs;
	}
	
	public SpellChecker(BloomFilters bfs, Charset cs) {
		this.bfs = bfs;
		this.cs = cs;
		this.bfs.setCs(cs);
	}
	
	public void loadDictionary(String fileName) {
		bfs.resetBitSet();

		try ( Stream<String> stream = Files.lines(Paths.get(fileName), cs)) {
			stream.map(String::trim)
			.forEach(word -> bfs.addWord(word));
		}catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	public boolean isInDictionary(String word) {
		return bfs.checkWord(word);
	}
	
	public boolean bruteforce(String fileName, String word) {
		long frequency = 0;
		try ( Stream<String> stream = Files.lines(Paths.get(fileName), cs)) {
			frequency = stream.map(String::trim).filter(line -> word.equals(line)).count();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return frequency > 0 ? true : false;
	}
	
	public static String[] generateWords(int numOfChar, int count) {
		String[] strs = new String[count];
		for (int i = 0; i < count; i++)
			strs[i] = RandomStringUtils.random(numOfChar);
		return strs;
	}
	
	public static void main(String[] args) {
		if (args.length != 5) {
			System.out.printf("required parameters: <number of bits> <number of hash> <dictionary file name> <search word> <1|0>\n"
					+ " Large number of bits and number of hash can make false positive rate small, but take more memory and longer time.");
			System.exit(1);
		}
		BloomFilters bf = new BloomFilters(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		
		SpellChecker sc = new SpellChecker(bf, StandardCharsets.UTF_8);
		try {
			sc.loadDictionary(args[2]);
		}catch(Exception e) {
			try {
				sc = new SpellChecker(bf, StandardCharsets.ISO_8859_1);
				sc.loadDictionary(args[2]);
			}catch(Exception ef) {
				System.out.println("Can not load dictionary with neither ISO_8859_1 or UTF_8 encoding.");
				System.exit(1);
			}
		}
		if (Integer.parseInt(args[4]) == 1) {
			String[] strs = SpellChecker.generateWords(5, 100);
			int truePositive = 0;
			int possiblePositive = 0;
			for (int i = 0; i < strs.length; i++) {
				truePositive += sc.bruteforce(args[2], strs[i]) ? 1:0;
				possiblePositive += sc.isInDictionary(strs[i]) ? 1:0;
			}
			System.out.printf("\nfalse positive rate: %.2f.\n If false positive rate is high, you should set number of bits and number of hash to be larger numbers, such as 50000000 for number of bits, 500 for number of hash. You can also set the last parameter to be zero to skip calculating the rate.\n", (possiblePositive - truePositive) / 100.0);
		}
		else {
			System.out.printf("\nSkip checking false positive rate\n");
		}
		
		boolean isInDic = sc.isInDictionary(args[3]); 
		if (isInDic) System.out.printf("\nWord %s is in dictionary\n", args[3]) ;
		else System.out.printf("\nWord %s is NOT in dictionary\n", args[3]);
	}
}
