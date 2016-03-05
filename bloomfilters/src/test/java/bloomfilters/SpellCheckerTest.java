package bloomfilters;

import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SpellCheckerTest {

	@Test
	public void testLoadDictionary() {
		BloomFilters bf = new BloomFilters(500000, 200);
		SpellChecker sc = new SpellChecker(bf, StandardCharsets.ISO_8859_1);
		sc.loadDictionary("data/wordlist.txt");
		assertTrue("More than one bit is set when load the downloaded dictionary", bf.numSetBits() > 0);
	}

	@Test
	public void testCheck() {
		BloomFilters bf = new BloomFilters(500000, 200);
		SpellChecker sc = new SpellChecker(bf, StandardCharsets.ISO_8859_1);
		sc.loadDictionary("data/wordlist.txt");
		assertTrue("xxxx should not in the dictionary, but it is in", sc.isInDictionary("xxxx"));
		assertTrue("AC's is in the dictionary", sc.isInDictionary("AC's"));
	}
	
	@Test
	public void testBruteForce() {
		BloomFilters bf = new BloomFilters(500000, 200);
		SpellChecker sc = new SpellChecker(bf, StandardCharsets.ISO_8859_1);
		assertTrue("xxxx is not in the dictionary", !sc.bruteforce("data/wordlist.txt", "xxxx"));
		assertTrue("AC's is in the dictionary", sc.bruteforce("data/wordlist.txt", "AC's"));
	}
	
	@Test
	public void testAddRandomGenerated() {
		BloomFilters bf = new BloomFilters(500000, 200);
		SpellChecker sc = new SpellChecker(bf);
		String[] strs = SpellChecker.generateWords(5, 100);
		for (int i = 0; i < strs.length; i++) {
			bf.addWord(strs[i]);
		}
		for (int i = 0; i < strs.length; i++) {
			assertTrue("word is in dictionary", sc.isInDictionary(strs[i]));
		}
	}
	
	@Test
	public void testRandomWordIndictionary() {
		BloomFilters bf = new BloomFilters(50000000, 500);
		
		SpellChecker sc = new SpellChecker(bf, StandardCharsets.ISO_8859_1);
		sc.loadDictionary("data/wordlist.txt");
		
		String[] strs = SpellChecker.generateWords(1, 100);
		int truePositive = 0;
		int possiblePositive = 0;
		for (int i = 0; i < strs.length; i++) {
			truePositive += sc.bruteforce("data/wordlist.txt", strs[i]) ? 1:0;
			possiblePositive += sc.isInDictionary(strs[i]) ? 1:0;
		}
		System.out.printf("false positive rate: %.2f", (possiblePositive - truePositive) / 100.0);
	}
}
