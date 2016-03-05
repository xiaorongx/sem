package bloomfilters;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

/**
 * An implementation of bloom filter http://codekata.com/kata/kata05-bloom-filters/
 * @author xiaorong
 *
 */
public class BloomFilters {
	int numBits;
	int numHashs;
	BitSet bs;
	Charset cs = StandardCharsets.UTF_8;

	public BloomFilters(int numBits, int numHashs) {
		this.numBits = numBits;
		this.numHashs = numHashs;
		this.bs = new BitSet(numBits);
	}
	
	
	
	public byte[] generateMD5(String str){
		MessageDigest md;
		byte[] digest = new byte[0];
		try {
			md = MessageDigest.getInstance("MD5");
			digest = md.digest(str.getBytes(cs));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 is not supported", e);
		} 
		return digest;
	}
	
	public int[] extractHashValue(String word) {
		byte[] digest = generateMD5(word);
		
		int[] hashValues = new int[numHashs];
		
		BigInteger original = new BigInteger(1, digest);
		
		int shiftSize = original.bitLength() / numHashs;
		for (int i = 0; i < numHashs; i++) {
			BigInteger shiftRight = original.shiftRight( i * shiftSize);
			int x = (int) (shiftRight.longValue() % numBits);
			hashValues[i] = x < 0 ? -1 * x:x;
		}
		return hashValues;
	}
	
	public void addWord(String word) {		
		int[] hashV = extractHashValue(word);
		for (int i = 0; i < hashV.length; i++) {
			bs.set(hashV[i]);
		}
	}
	
	public boolean checkWord(String word) {
		int[] hashV = extractHashValue(word);
		for (int i = 0; i < hashV.length; i++) {
			if (!bs.get(hashV[i]))
				return false;
		}
		return true;
	}
	
	public int numSetBits() {
		return bs.cardinality();
	}
	
	public void resetBitSet() {
		bs.clear();
	}
	
	
	public Charset getCs() {
		return cs;
	}

	public void setCs(Charset cs) {
		this.cs = cs;
	}


 }
