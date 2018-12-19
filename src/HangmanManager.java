import java.util.*;

public class HangmanManager{
	private int mMax;
	private int mLength;
	private int mTurns = 0;
	private Set<String> mDictionary;
	private TreeSet<Character> mGuesses = new TreeSet<Character>();
	private String mPattern;
	
	public HangmanManager(List<String> dictionary, int length, int max){
		if(length < 1 || max < 0){
			throw new IllegalArgumentException("Invalid word size or num Guesses.");
		}
		mMax = max;
		mLength = length;
		Set<String> dictionary2 = new TreeSet<String>();
		for(String word: dictionary){
			if(word.length() == length){
				dictionary2.add(word);
			}
		}
		mDictionary = dictionary2;
	}
	
	public Set<String> words(){
		return mDictionary;
	}
	
	public int guessesLeft(){
		return mMax-mTurns;
	}
	
	public TreeSet<Character> guesses(){
		return mGuesses;
	}
	
	public String pattern(){
		if(mPattern == null){
			String temp = new String();
			for(int i=0; i<mLength; i++){
				temp += "-";
			}
			mPattern = temp;
		}
		return mPattern;
	}
	
	public int record(Character guess){
		mGuesses.add(guess);
		Map<String, TreeSet<String>> allCombinations = new TreeMap<String, TreeSet<String>>();
		for(String word: mDictionary){
			String pattern = mPattern;
			//Overwrite the position of each occurrence of the guessed character onto pattern
			int index = word.indexOf(guess);
			while(index >= 0){
				pattern = replaceCharAtIndex(index, guess, pattern);
				index = word.indexOf(guess, index + 1);
			}
			//If the pattern is new, create a new treeSet and add the word to it. Otherwise
			//find the list matching the pattern and add the word to its associated treeSet.
			if(!allCombinations.containsKey(pattern)){
				TreeSet<String> subDictionary = new TreeSet<String>();
				subDictionary.add(word);
				allCombinations.put(pattern, subDictionary);
			}
			else{
				allCombinations.get(pattern).add(word);
			}
			
		}
		//select the pattern with the largest word list
		Set<String> allKeys = allCombinations.keySet();
		String largestKeySet = null;
		for(String current : allKeys){
			if(largestKeySet == null || allCombinations.get(current).size() > 
			allCombinations.get(largestKeySet).size()){
				largestKeySet = current;
			}
		}
		mPattern = largestKeySet;
		mDictionary = allCombinations.get(largestKeySet);
		int count = countLetterOccurrences(guess, largestKeySet);
		if(count == 0){
			mTurns++;
		}
		return count;
	}
	
	private static String replaceCharAtIndex(int pos, char ch, String str){
		char[] temp = str.toCharArray();
		temp[pos] = ch;
		String out = new String(temp);
		return out;
	}
	
	private static int countLetterOccurrences(char guess, String in){
		int count = 0;
		for(int i=0; i<in.length(); i++){
			if(in.toLowerCase().charAt(i) == guess){
				count++;
			}
		}
		return count;
	}
}