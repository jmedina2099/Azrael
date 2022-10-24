/**
 * 
 */
package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import hash.TablaHash;

/**
 * @author jmedina
 *
 */
public class Main {

	public static void readFile( TablaHash<String,Object> tabla, String fileName ) {
		
		URL istream = TablaHash.class.getResource( fileName );
		Path path = Paths.get(istream.getPath());
		Charset charset = Charset.forName("ISO-8859-1");
		
		try ( Stream<String> stream = Files.lines(path,charset) ) {
			stream.forEach( line -> {
				tabla.put(line,line);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println( "-"+tabla.getSizeCasillas()+"-"+tabla.getNumberOfElements()+"-"+tabla.getAverage()+"-"+tabla.getMax()+"-"+tabla.getEmpties() );
	}
	
	public static void readFileFast( TablaHash<String,Object> tabla, String fileName, Charset charset ) {

		long timeIni = System.currentTimeMillis();
		System.out.println( "=====> START FILE="+fileName );
		
		String word = null;
		try ( InputStream istream = TablaHash.class.getResourceAsStream( fileName );
			  BufferedInputStream bis = new BufferedInputStream(istream);
			  BufferedReader reader = new BufferedReader(new InputStreamReader(bis,charset)) ) {
			
			while( (word=reader.readLine() )!=null ) {
				if( word.length() > 0 ) {
					tabla.put( word,null );
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e ) {
			e.printStackTrace();
		}
		
		long timeNow = System.currentTimeMillis() - timeIni;
		System.out.println( "TIME = "+
				"["+(timeNow/(1000.0))+"] secs,"+
				"["+(timeNow/(1000.0*60.0))+"] mins." );
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int tope = 174851;
		Charset charset = Charset.forName("ISO-8859-1");
		TablaHash<String,Object> tabla = new TablaHash<String,Object>(tope,charset);

		String fileName = "/esp.txt";
		//readFileFast( tabla,fileName,charset );
		
		tope = 15000000;
		charset = Charset.forName("UTF-8");
		tabla = new TablaHash<String,Object>(tope,charset);

		fileName = "/rockyou.txt";
		readFileFast( tabla,fileName,charset );

		System.out.println( "-"+tabla.getSizeCasillas()+"-"+tabla.getNumberOfElements()+"-"+tabla.getAverage()+"-"+tabla.getMax()+"-"+tabla.getEmpties() );

		/*
		int tope = 100000;
		TablaHash<String,Object> tabla = new TablaHash<String,Object>(tope);
		for( int i=0; i<tope; i++ ) {
			tabla.put( ""+i, i);
		}
		
		System.out.println( "-"+tabla.getSizeCasillas()+"-"+tabla.getNumberOfElements()+"-"+tabla.getAverage()+"-"+tabla.getMax()+"-"+tabla.getEmpties() );
		*/
		
		
		//System.out.println( Arrays.toString(tabla.getStats()) );
	}

}
