package bloomfilters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BloomFiltersTest {

	@Test
	public void test() {
		BloomFilters bfs = new BloomFilters(5000,22);
		bfs.generateMD5("abc");
	}
	@Test
	public void testAddThenCheckWord() {
		BloomFilters bfs = new BloomFilters(5000,22);
		bfs.addWord("abc");
		bfs.addWord("向");
		assertEquals(44, bfs.numSetBits());
		assertTrue(bfs.checkWord("abc"));
		assertTrue(bfs.checkWord("向"));
		assertFalse(bfs.checkWord("cde"));
	}
}
