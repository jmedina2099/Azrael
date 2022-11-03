/**
 * 
 */
package org.azrael.hash;

import java.math.BigInteger;

/**
 * @author jmedina
 *
 */
public class Tupla<X,Y> {

	public X x;
	public Y y;
	public BigInteger hash;

	public Tupla(X x, Y y, BigInteger hash) { 
		this.x = x;
		this.y = y;
		this.hash = hash;
	} 
}
