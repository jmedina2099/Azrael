/**
 * 
 */
package org.azrael.hash;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jmedina
 *
 */
public class TablaHash<K,V> {

	private int sizeCasillas = 10;
	private int numberOfElements = 0;
	private int numberOfRehash = 0;
	private Object[] casillas = null;
	private FuncionHash azrael = new Azrael64();
	//private FuncionHash azrael = new JavaHash();

	public TablaHash() {
		this.casillas = new Object[this.sizeCasillas];
	}
	
	public TablaHash( int initialCapacity, Charset charset ) {
		this.sizeCasillas = initialCapacity;
		this.casillas = new Object[this.sizeCasillas];
	}

	/**
	 * Return the value for the corresponding key.
	 * @param K the key
	 * @return V the value
	 */
	public V get( K key ) {
		Optional<ArrayList<Tupla<K,V>>> listaCasilla = getListaCasilla( key );
		if( !listaCasilla.isPresent() )
			return null;

		Optional<Tupla<K,V>> tupla = getFirstWithKey( listaCasilla.get(),key );
		return ( tupla.isPresent()? tupla.get().y: null );
	}

	/**
	 * Add a new pair of key and value.
	 * @param K the key
	 * @param V the value
	 */
	public V put( K key, V value ) {
		
		if( Math.floor( 1.5d*this.sizeCasillas ) == this.numberOfElements ) {
			System.out.println( "rehash="+this.numberOfRehash);
			rehashAndIncrement();
		}
		
		BigInteger[] indexAndHash = new BigInteger[2];
		Optional<ArrayList<Tupla<K,V>>> listaCasilla = getListaCasilla( key, indexAndHash );
		if( listaCasilla.isPresent() ) {
			
			listaCasilla.get().forEach( tupla -> {
				if( indexAndHash[1].equals( tupla.hash ) ) {
					System.out.println( "HASH COLISION!!! ("+
							tupla.x+","+
							key+")-["+tupla.hash+"]-["+indexAndHash[1]+"]-["+this.numberOfElements+"]" );
				}
			});
			
			Optional<Tupla<K,V>> tupla = getFirstWithKey( listaCasilla.get(),key );
			if( tupla.isPresent() ) {
				Tupla<K,V> t = tupla.get();
				V oldValue = t.y;
				t.x = key;
				t.y = value;
				return oldValue;
			}
		} else {
			listaCasilla = Optional.of( new ArrayList<Tupla<K,V>>(15) );
			this.casillas[indexAndHash[0].intValue()] = listaCasilla.get();
		}
		//System.out.println( key );
		listaCasilla.get().add( new Tupla<K,V>(key,value,indexAndHash[1]) );
		this.numberOfElements++;		
		return null;
	}
	
	public int getNumberOfElements() {
		return this.numberOfElements;
	}
	
	public int getSizeCasillas() {
		return this.sizeCasillas;
	}
	
	public Integer[] getStats() {
		@SuppressWarnings("unchecked")
		List<Integer> listaSizes = Arrays.stream(this.casillas)
			.mapToInt( obj -> obj == null? 0: ((ArrayList<Tupla<K,V>>)obj).size() )
			.boxed()
			.collect( Collectors.toList() );

		return listaSizes.toArray( new Integer[0] );
	}
	
	public double getAverage() {
		int[] avg = {0};
		Arrays.stream(this.casillas).forEach( obj -> {
			if( obj != null ) {
				@SuppressWarnings("unchecked")
				ArrayList<Tupla<K,V>> listaCasilla = (ArrayList<Tupla<K,V>>)obj;
				avg[0] += listaCasilla.size();
			}
		});
		return avg[0]/(double)this.sizeCasillas;
	}
	
	public int getMax() {
		int[] max = {0};
		Arrays.stream(this.casillas).forEach( obj -> {
			if( obj != null ) {
				@SuppressWarnings("unchecked")
				ArrayList<Tupla<K,V>> listaCasilla = (ArrayList<Tupla<K,V>>)obj;
				int size = listaCasilla.size();
				if( size > max[0] ) {
					max[0] = size;
				}
			}
		});
		return max[0];
	}
	
	public int getEmpties() {
		int[] empties = {0};
		Arrays.stream(this.casillas).forEach( obj -> {
			if( obj == null ) {
				empties[0]++;
			}
		});
		return empties[0];
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Optional<ArrayList<Tupla<K,V>>> getListaCasilla( K key, BigInteger... indexAndHash ) {
		indexAndHash[0] = this.azrael.compute((String)key, this.sizeCasillas, indexAndHash );
		@SuppressWarnings("unchecked")
		ArrayList<Tupla<K,V>> listaCasilla = (ArrayList<Tupla<K,V>>) this.casillas[indexAndHash[0].intValue()];
		return Optional.ofNullable(listaCasilla);
	}
	
	private Optional<Tupla<K,V>> getFirstWithKey( ArrayList<Tupla<K,V>> listaCasilla, K key ) {
		List<Tupla<K, V>> listaWithKey = listaCasilla.stream().filter( obj -> obj != null && obj.x.equals(key) ).collect( Collectors.toList() );
		return Optional.ofNullable( listaWithKey.isEmpty()? null: listaWithKey.get(0) ); 
	}
	
	private void rehashAndIncrement() {
		
		Object[] oldCasillas = this.casillas;

		this.sizeCasillas *= 2;
		this.casillas = new Object[this.sizeCasillas];
		this.numberOfElements = 0;
		
		Arrays.stream(oldCasillas).forEach( obj -> {
			if( obj != null ) {
				@SuppressWarnings("unchecked")
				ArrayList<Tupla<K,V>> listaCasilla = (ArrayList<Tupla<K,V>>)obj;
				listaCasilla.forEach( tupla -> {
					put( tupla.x, tupla.y );
				});
			}
		});
		
		this.numberOfRehash++;
	}
	
}
