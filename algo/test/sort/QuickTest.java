package sort;

import static org.junit.Assert.*;

import org.junit.Test;

public class QuickTest {

	@Test
	public void testSingle() {
		int[] a = new int[] {1};
		int[] expect = new int[] {1};
		Quick.sort(a, 0, a.length-1);
		assertArrayEquals( expect, a);
	}
	
	@Test
	public void testTwo() {
		int[] a = new int[] {2,1};
		int[] expect = new int[] {1,2};
		Quick.sort(a, 0, a.length-1);
		assertArrayEquals( expect, a);
	}
	
	@Test
	public void testTwo1() {
		int[] a = new int[] {1,2};
		int[] expect = new int[] {1,2};
		Quick.sort(a, 0, a.length-1);
		assertArrayEquals( expect, a);
	}
	
	@Test
	public void testMoreThan3() {
		int[] a = new int[] {1,6, 2, 9, 3, 7, 5, 5};
		int[] expect = new int[] {1,2,3,5,5,6,7,9};
		Quick.sort(a, 0, a.length-1);
		assertArrayEquals( expect, a);
	}

}
