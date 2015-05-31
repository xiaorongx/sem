package catalan;

import static org.junit.Assert.*;

import org.junit.Test;

public class CatalanTest {

	@Test
	public void test5() {
		assertEquals( Catalan.prtParen(5, 5, ""), 42);
	}
	@Test
	public void test4() {
		assertEquals( Catalan.prtParen(4, 4, ""), 14);
	}
	@Test
	public void test3() {
		assertEquals( Catalan.prtParen(3, 3, ""), 5);
	}
	
	@Test
	public void test2() {
		assertEquals( Catalan.prtParen(2, 2, ""), 2);
	}
}
