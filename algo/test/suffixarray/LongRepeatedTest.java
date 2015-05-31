package suffixarray;

import static org.junit.Assert.*;

import org.junit.Test;

public class LongRepeatedTest {

	@Test
	public void testStringToArray() {
		String str = "See how the string change";
		char[] conv = LongRepeated.stringToArray(str);
		conv[4]='t';
	}

}
