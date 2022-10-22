/**
 * 
 */
package main;

import java.io.IOException;
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

	public static void readFile( TablaHash<String,Object> tabla ) {
		
		String fileName = "/esp.txt";
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int tope = 174851;
		TablaHash<String,Object> tabla = new TablaHash<String,Object>(tope);

		readFile( tabla );
		
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
