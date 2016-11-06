package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		HashMap<String,Occurrence> newmap = new HashMap<String, Occurrence>();	
		Scanner scn = new Scanner(new File(docFile));	
		while (scn.hasNext()) {																							
			String key = getKeyWord(scn.next());									
			if(key != null){																					
				Occurrence occr = newmap.get(key);									
				if(occr == null) newmap.put(key, new Occurrence(docFile, 1));  				
				else occr.frequency++;																				
			}
		}
		scn.close();
		return newmap;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		// COMPLETE THIS METHOD
		Iterator<Map.Entry<String, Occurrence>> merge = kws.entrySet().iterator();
		while(merge.hasNext()){																		
			Map.Entry<String, Occurrence> entry = (Map.Entry<String, Occurrence>) merge.next();		
			ArrayList<Occurrence> occurences = keywordsIndex.get(entry.getKey());						
			if(occurences==null) keywordsIndex.put(entry.getKey(), new ArrayList<Occurrence>(Arrays.asList(entry.getValue())));
			else{																								
				occurences.add(entry.getValue());											
				insertLastOccurrence(occurences);											
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		word = word.toLowerCase();											
		char[] array = word.toCharArray();
		int x = 1;																	
		for (int i = array.length-1; i>=0 ; i-- ){ 								
			if(  Alph(array[i]) && (i > x) ) x = i+1;			
			if( !Alph(array[i]) && (i < x) ) return null;   		
		}
		String keyWord = word.substring(0, x);						
		if(noiseWords.containsKey(keyWord))return null; 			
		return keyWord;
	}

	private boolean Alph(char character){
		int y = (int) character;	
		return (97<=y  &&  y <=122);
	}
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		ArrayList<Integer> LastOccurance = new ArrayList<Integer>();
		Occurrence feed = occs.remove(occs.size()-1);						
		int x=0; 
		int y=occs.size()-1;
		int z = (x+y)/2;										
		while(x<=y){								
			z = (x+y)/2;
			LastOccurance.add(z);																	
			if(feed.frequency > occs.get(z).frequency ) 
				y = z-1;		
			if(feed.frequency < occs.get(z).frequency ) 
				x = z+1;
			if(feed.frequency ==occs.get(z).frequency ) 
				break;
						
		}
		if(x>y) 
			z = x;																			
		if(z==occs.size()) 
			occs.add(feed);											
		else 	
			occs.add(z, feed);																
		return LastOccurance;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		ArrayList<String> search = new ArrayList<String>();
		ArrayList<Occurrence> k1 = keywordsIndex.get(getKeyWord(kw1));								
		ArrayList<Occurrence> k2 = keywordsIndex.get(getKeyWord(kw1));								
		while(search.size()<=5){
			Occurrence x[] = {SearchHelp(k1, search), SearchHelp(k2, search)};
			Occurrence y = (x[0].frequency>=x[1].frequency) ? x[0] : x[1];			
			if(y.document==null)break;																		
			else search.add(y.document); 
		}
		return search;
	}
	
  private Occurrence SearchHelp(ArrayList<Occurrence> M, ArrayList<String> N){	
	if(M != null) for(Occurrence occ : M) if(!N.contains(occ.document)) return occ;
	return new Occurrence(null, 0);																				
	}
	
}
//Thank you