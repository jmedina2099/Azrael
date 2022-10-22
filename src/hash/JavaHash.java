/**
 * 
 */
package hash;

import java.math.BigInteger;

/**
 * @author jmedina
 *
 */
public class JavaHash implements FuncionHash {

	@Override
	public BigInteger getHash(byte[] o) {

		String word = new String( o );
		return BigInteger.valueOf( (long)word.hashCode() );
	}

}
