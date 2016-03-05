An implementation of bloom filter to check if an elment is an member of a large set.
See: http://codekata.com/kata/kata05-bloom-filters/

To run:
./gradlew spellcheck -Pparams=500000000,500,data/wordlist.txt,ABCss,1

params = <number of bits> <number of hash> <dictionary file name> <search word> <1|0>

Set the last parameter to be 1 if you want to skip false positive rate calculation. 
The false positive rate is calculated with following step:
Random generate 100 words;
Run brute force search to find the number of words in the dictionary;
Run bloom filter to find the number of words in the dictionary;
Calculate rate using the difference divided by 100