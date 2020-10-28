package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author Raymond Phillips
 *
 */
public class Polynomial {
	
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
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		Node curr1 = poly1, curr2 = poly2, front = null, last = null;
		while(curr1 != null && curr2 != null) {
			if (curr2.term.degree < curr1.term.degree){													//checks to see if poly2's degree is less than poly1's degree
				Node newNode = new Node(curr2.term.coeff, curr2.term.degree, null);
				if(front != null && last != null) {														//checks to see if first node is empty
					last.next = newNode;
					last = newNode;
				} else {
					front = newNode;
					last = newNode;
				}
				curr2 = curr2.next;
			} else if(curr1.term.degree < curr2.term.degree) {											//checks to see if poly1's degree is less than poly2's degree.
				Node newNode = new Node(curr1.term.coeff, curr1.term.degree, null);
				if(front != null && last != null) {														//checks to see if first node is empty
					last.next = newNode;
					last = newNode;
				} else {
					front = newNode;
					last = newNode;
				}
				curr1 = curr1.next;	
			} else if(curr1.term.degree == curr2.term.degree) {											//checks to see if the degrees match
				Node newNode = new Node(curr1.term.coeff + curr2.term.coeff, curr1.term.degree, null);
				if(front == null && last == null) {														//checks to see if first node is empty.
					front = newNode;
					last = newNode;
				} else {
					last.next = newNode;
					last = newNode;
				}
				curr1 = curr1.next;
				curr2 = curr2.next;
			} 
		}
		if(curr1 == null && curr2 != null) {															//checks to see if poly1 is shorter than poly2. if so then adds remaining poly2 to the new poly.
			while(curr2 != null) {
				Node newNode = new Node(curr2.term.coeff, curr2.term.degree, null);
				last.next = newNode;
				last = newNode;
				curr2 = curr2.next;
			}
		} else if (curr2 == null && curr1 != null){														//checks to see if poly2 is shorter than poly1. if so then adds remaining poly1 to the new poly.
			while(curr1 != null) {
				Node newNode = new Node(curr1.term.coeff, curr1.term.degree, null);
				last.next = newNode;
				last = newNode;
				curr1 = curr1.next;
			}
		}
		front = removeZero(front);																		//removes any zero nodes from the polynomial.
		return front;			
	}
	
	/**
	 * removes zero nodes
	 */
	private static Node removeZero(Node front) {
		Node curr = front;
		while(curr != null) {
			if(curr.term.coeff == 0) {
				if(curr == front) {
					front = front.next;
					curr = front;
				} else {
					curr = curr.next;
				}
			}
			curr = curr.next;
		}
		return front;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		if (poly1 == null || poly2 == null) {
	        return null;
		}
	    Node curr1 = poly1, curr2 = poly2, allTerms = null, newTerms = null;
	    int mDeg = 0;
	    while (curr1 != null) {												//multiplies two terms together
	        while (curr2 != null) {
	            allTerms = new Node(curr1.term.coeff * curr2.term.coeff, curr1.term.degree + curr2.term.degree, allTerms);					//creates a new node pointing to itself.
	            if (curr1.term.degree + curr2.term.degree > mDeg) {
	                mDeg = curr1.term.degree + curr2.term.degree;
	            }
	            curr2 = curr2.next;
	        }
	        curr1 = curr1.next;																												//moves to the next term in the first poly.
	        curr2 = poly2;																													//resets poly2 so that foiling can happen again.
	    }

	    for (int i = 0; i<= mDeg; i++) {									//combines like terms
	        Node temp = allTerms;
	        float fSum = 0;
	        while (temp != null) {											//goes through the whole list to find like items and then adds it to a new list and repeats that for the entire list of i up to mDeg.
	            if (temp.term.degree == i)
	                fSum+=temp.term.coeff;
	            temp = temp.next;
	        }
	        if (fSum != 0) {
	            newTerms = new Node(fSum, i, newTerms);
	        }
	    }
	    newTerms = reverse(newTerms);										//reverses the polynomial ordering
	    newTerms = removeZero(newTerms);									//removes any zero terms from the polynomial
	    return newTerms;
	}
	/**
	 * reverses a linked list
	 */
	private static Node reverse(Node node) {
		Node prev = null, current = node, next = null;  
	    while (current != null) { 
	        next = current.next; 
	        current.next = prev; 
	        prev = current; 
	        current = next; 
	    } 
	    node = prev; 
	    return node; 
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE TH IS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float num = 0;
		Node curr = poly;
		if(poly == null) {
			return 0;
		} else {
			while(curr != null) {
				num += Math.pow(x,curr.term.degree)*curr.term.coeff;
				curr = curr.next;
			}
		}
		return num;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
