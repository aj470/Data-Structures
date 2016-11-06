package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		if(deckRear==null)
		{
			throw new NoSuchElementException();
		}
		CardNode last = deckRear;
		for(CardNode ptr = deckRear.next; ptr != deckRear; ptr = ptr.next)
		{
			
			
			if(ptr.next == deckRear && ptr.next.cardValue == 27)
			{
				CardNode l = deckRear;
				ptr.next = deckRear.next;
				deckRear = ptr.next;
				CardNode m = deckRear.next;
				deckRear.next = l;
				l.next = m;
				return;
			}
			
			else if(ptr.cardValue == 27)
			{
				CardNode l = ptr;
				CardNode m = ptr.next.next;
				last.next = ptr.next;
				last.next.next = l;
				l.next = m;
				return;
			}
			
			else if(ptr.next == deckRear && ptr.cardValue == 27)
			{
				CardNode l = ptr;
				CardNode top = deckRear.next;
				CardNode bot = deckRear;
				last.next = ptr.next;
				bot.next = l;
				deckRear = l;
				l.next = top;
				return;
			}
			else
				last = last.next;
		}
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		if(deckRear==null)
		{
			throw new NoSuchElementException();
		}
		CardNode last = deckRear;
		for(CardNode ptr = deckRear.next; ptr != deckRear; ptr = ptr.next)
		{
			if(ptr.next == deckRear && ptr.cardValue == 28)
			{
				CardNode l = ptr;
				CardNode m = deckRear.next;
				last.next = ptr.next;
				deckRear = l;
				deckRear.next = m;
				last.next.next = deckRear.next;
				deckRear = last.next.next;
				l = deckRear.next;
				deckRear.next = l;
				l.next = m;
				return;
			}
			if(ptr.next == deckRear && ptr.next.cardValue == 28)
			{
				CardNode l = deckRear;
				ptr.next = deckRear.next;
				deckRear = ptr.next;
				CardNode m = deckRear.next;
				l.next = m;
				CardNode c = m.next;
				m.next = l;
				l.next = c;
				return;
			}
			if ( ptr.next.next == deckRear && ptr.cardValue == 28)
			{
				CardNode bot = deckRear;
				CardNode top = deckRear.next;
				bot.next = ptr;
				deckRear = ptr;
				last.next = ptr.next;
				ptr.next = top;
				return;
			}
			if (ptr.cardValue == 28)
			{
				CardNode l = ptr;
				CardNode m = ptr.next.next.next;
				last.next = ptr.next;
				last.next.next.next = l;
				l.next = m;
				return;
			}
			else
				last = last.next;
		}
	}
	
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		if(deckRear==null)
		{
			return;
		}
		CardNode last = deckRear.next;
		CardNode ptr = deckRear.next.next;
		if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28)
		{
			for(ptr = deckRear.next.next; ptr != deckRear; ptr = ptr.next)
			{
				if(deckRear.cardValue == 27 || deckRear.cardValue == 28)
					return;
				else if(ptr.cardValue == 27 || ptr.cardValue == 28)
				{
					deckRear = ptr;
					deckRear.next = ptr.next;
					return;
				}
				else
					last = last.next;
			}
		}
		else if (deckRear.cardValue == 27 || deckRear.cardValue == 28)
		{
			last = deckRear;
			ptr = deckRear.next;
			for(ptr = deckRear.next; ptr != deckRear; ptr = ptr.next)
			{
				if(deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28)
					return;
				else if(ptr.cardValue == 27 || ptr.cardValue == 28)
				{
					deckRear = last;
					deckRear.next = ptr;
					return;
				}
				else
					last = last.next;
			}
		}
		else
		{
			last = deckRear;
			ptr = deckRear.next;
			while(ptr != deckRear)
			{
				if(ptr.cardValue == 27 || ptr.cardValue == 28)
				{
					CardNode nextptr = ptr.next;
					while(nextptr != deckRear.next)
					{
						if(nextptr.cardValue == 27 || nextptr.cardValue == 28)
						{
							CardNode joker = nextptr;
							CardNode folptr = nextptr.next;
							CardNode head = deckRear.next;
							deckRear.next = ptr;
							joker.next = head;
							deckRear = last;
							deckRear.next = folptr;
							return;
						}
						else
							nextptr = nextptr.next;
					}
				}
				else
				{
					last = last.next;
					ptr = ptr.next;
				}
			}
		}
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		// COMPLETE THIS METHOD
		if(deckRear==null)
		{
			throw new NoSuchElementException();
		}
		
		int calc = 0;
		int end;
		
		if(deckRear.cardValue == 28)
			end = 27;
		else
			end = deckRear.cardValue;
		
		CardNode bot = deckRear;
		CardNode top = deckRear.next;
		CardNode ptr = deckRear.next;
		
		while (calc <= end)
		{
			if(calc == (end - 1))
			{
				if(ptr.next == deckRear)
					return;
				CardNode endptr = ptr;
				CardNode midptr = ptr.next;
				for(CardNode nextptr = ptr.next; nextptr != deckRear; nextptr = nextptr.next)
				{
					if(nextptr.next == deckRear)
					{
						CardNode midendptr = nextptr;
						midendptr.next = null;
						nextptr.next = top;
						endptr.next = deckRear;
						deckRear.next = midptr;
						return;
					}
				}
			}
			bot = bot.next;
			ptr = ptr.next;
			calc++;
		}
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		if(deckRear==null)
		{
			return (Integer)null;
		}
		int key = -1;
		int count = 1;
		int firstcardValue = deckRear.next.cardValue;
		if(firstcardValue == 28)
			firstcardValue = 27;
		CardNode ptr = deckRear.next;
		while(ptr != deckRear)
		{
			if (count == firstcardValue)
			{
				if(ptr.next.cardValue == 27 || ptr.next.cardValue == 28)
				{
					jokerA();
					jokerB();
					tripleCut();
					countCut();
					ptr = deckRear;
					count = 0;
					firstcardValue = deckRear.next.cardValue;
					if(firstcardValue == 28)
						firstcardValue = 27;
				}
				else
				{
					key = ptr.next.cardValue;
					return key;
				}
			}
			ptr = ptr.next;
			count++;
		}
	    return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		if(deckRear==null)
		{
			return message;
		}
		String en = "";    // string to encode
		String raw="";
		for(int j=0;j<message.length();j++)
		{
			if(!Character.isLetter(message.charAt(j)))
				continue;
			else
				raw = raw + message.charAt(j);
		}

		for(int i = 0 ; i < raw.length(); i++)
		{
			if(!Character.isLetter(raw.charAt(i)))
				continue;
			else
			{
				jokerA();
				jokerB();
				tripleCut();
				countCut();
				char ch = Character.toUpperCase(raw.charAt(i));
				int c = ch - 'A'+1;
				int gtkey = getKey();
				int pass = c + gtkey;
				if (pass > 26)
				{
					pass = pass - 26;
				}
				ch = (char)(pass-1+'A');
				en = en + ch;
			}
		}
	    return en;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		if(deckRear==null)
		{
			return message;
		}
		String raw="";
		String de = "";
		for(int j=0;j<message.length();j++)
		{
			if(!Character.isLetter(message.charAt(j)))
				continue;
			else
				raw = raw + message.charAt(j);
		}
		for(int i = 0 ; i < raw.length(); i++)
		{
				jokerA();
				jokerB();
				tripleCut();
				countCut();
				char ch = Character.toUpperCase(raw.charAt(i));
				int c = ch - 'A'+1;
				int gtkey = getKey();
				int pass = c - gtkey;
				if (pass <= 0)
				{
					pass = pass + 26;
				}
				ch = (char)(pass-1+'A');
				de = de + ch;
		}
		return de;
	}
	
}

// Thanks a lot
