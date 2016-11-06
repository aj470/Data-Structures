package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		if(p.poly==null){
			return this;
			
		}else if(this.poly==null){
			return p;
			
		}else{
			
			Polynomial X=new Polynomial ();
			X.poly=new Node (0,0,null);

			Node head=X.poly;
			Node exist=this.poly;
			Node entered=p.poly;

			while (entered !=null && exist!=null){

				boolean equal=exist.term.degree == entered.term.degree;
				boolean greater=entered.term.degree > exist.term.degree;
				boolean less=entered.term.degree < exist.term.degree;

				
				if(equal){
					if(exist.term.coeff + entered.term.coeff == 0){
						entered=entered.next;
						exist=exist.next;
						continue;
					}
					X.poly.term.coeff=exist.term.coeff + entered.term.coeff;
					X.poly.term.degree=entered.term.degree;
					
					entered=entered.next;
					exist=exist.next;

				}else if(greater){
					X.poly.term=exist.term;
					exist=exist.next;
					
				}else if (less){
					X.poly.term=entered.term;
					entered=entered.next;
			
				}
				
				if(entered!=null && exist!=null){
					X.poly.next=new Node (0,0,null);
					X.poly=X.poly.next;
				}

			}

			if(entered!=null){
				
				while(entered!=null){
					X.poly.next=new Node (0,0,null);
					X.poly=X.poly.next;
					
					X.poly.term=entered.term;
					entered=entered.next;
				}
				
			}

			if(exist!=null){
				
				while(exist!=null){
					X.poly.next=new Node (0,0,null);
					X.poly=X.poly.next;
					
					X.poly.term=exist.term;
					exist=exist.next;	
				}
				
			}

			X.poly=head;
			return X;	
			
		}
		
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		if(p.poly==null){
			return this;
	
		}else if (this.poly==null){
			return p;
			
		}else{
			
			Polynomial A=new Polynomial ();
			A.poly=new Node (0,0,null);
			
			Node head=A.poly;
			Node entry = null;
			Node already = null;
			
			
			
			for( entry=p.poly;entry!=null;entry=entry.next){
				
				for( already=this.poly;already!=null;already=already.next){
					
					A.poly.term.coeff=already.term.coeff*entry.term.coeff;
					A.poly.term.degree=already.term.degree+entry.term.degree;
					A.poly.next= new Node (0,0,null);
					A.poly=A.poly.next;
					
				}
				
			}
			
			A.poly=head;
		
			Polynomial Sum= new Polynomial ();
			Sum.poly= new Node (0,0,null);
			Node front= Sum.poly;
			
			for (Node curr=A.poly;curr!=null;curr=curr.next){
				
				for(Node check=curr.next, prev=curr;check!=null;check=check.next){
					
					if(curr.term.degree == check.term.degree){
							Sum.poly.term.coeff=curr.term.coeff+check.term.coeff;
							Sum.poly.term.degree=curr.term.degree;
							curr.term=Sum.poly.term;
							prev.next=check.next;
						}
					
					prev=check;
					
				}
				
				if(Sum.poly.term.coeff==0)
					Sum.poly.term=curr.term;
				
				if(curr.next!=null){
					Sum.poly.next= new Node (0,0,null);
					Sum.poly=Sum.poly.next;
				}
				
			}
			
				
			Sum.poly=front;
	
			Polynomial Result= new Polynomial ();
			Result.poly= new Node (0,0,null);
			
			Node tail=Result.poly;
			Node curr=Sum.poly;
			
		for(int degree=0;Sum.poly!=null;degree++){
		curr=Sum.poly;
		
			for(Node prev=null;curr!=null;curr=curr.next){
				
				if(curr.term.degree==degree){
					Result.poly.term=curr.term;
					
					
					if(prev==null){
						Sum.poly=curr.next;
						curr=curr.next;
						break;
						
					}else{
						prev.next=curr.next;
						break;
					}	
				}
				
				prev=curr;		
			}
			
			if(Sum.poly!=null){
				Result.poly.next= new Node (0,0,null);
				Result.poly=Result.poly.next;
			}
			
		}
			
			Result.poly=tail;
			return Result;		
		
		}
		
	
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		/** COMPLETE THIS METHOD **/

		Node entered=this.poly;
		float sum=0;
		
		while (entered!=null){
			sum+=entered.term.coeff*(Math.pow(x, entered.term.degree));
			entered=entered.next;
		}
		
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
