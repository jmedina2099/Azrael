package org.azrael.api;

import java.math.BigInteger;
import java.nio.charset.Charset;

import org.azrael.hash.TablaHash;
import org.azrael.hash.Tupla;
import org.azrael.main.Main;
import org.azrael.main.TestHash;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmedina
 *
 */
@RestController
@RequestMapping("/hash")
public class HashTestController {

	@GetMapping("/hello")
	public ResponseEntity<Tupla<String, String>> doHello() {
		return new ResponseEntity<Tupla<String, String>>(new Tupla<String, String>("1", "1", BigInteger.valueOf(1)),
				HttpStatus.OK);
	}

	@PostMapping("/dictionary")
	public ResponseEntity<Estadistica> index(@RequestBody TestHash testHash) {

		String fileName = "/esp.txt";
		int tope = 174851;
		Charset charset = Charset.forName("ISO-8859-1");
		TablaHash<String, Object> tabla;

		Dictionary dict = Dictionary.getEnum(testHash.fileName);
		switch (dict) {
		case EN:
			fileName = "/en.txt";
			tope = 194443;
			charset = Charset.forName("UTF-8");
			break;
		case ESP:
			fileName = "/esp.txt";
			tope = 174851;
			charset = Charset.forName("ISO-8859-1");
			break;
		case ROCKYOU:
			fileName = "/rockyou.txt";
			tope = 10735819;
			charset = Charset.forName("UTF-8");
			break;
		default:
			break;
		}

		tabla = new TablaHash<String, Object>(tope, charset);
		Main.readFileFast(tabla, fileName, charset);

		return new ResponseEntity<Estadistica>(new Estadistica(tabla.getSizeCasillas(), tabla.getNumberOfElements(),
				tabla.getAverage(), tabla.getMax(), tabla.getEmpties()), HttpStatus.OK);

	}

	class Estadistica {

		public int sizeCasillas;
		public int numberOfElements;
		public double average;
		public int max;
		public int empties;

		public Estadistica(int sizeCasillas, int numberOfElements, double average, int max, int empties) {
			this.sizeCasillas = sizeCasillas;
			this.numberOfElements = numberOfElements;
			this.average = average;
			this.max = max;
			this.empties = empties;
		}
	}

	public enum Dictionary {
		ESP("es"), EN("en"), ROCKYOU("ry"), RANDOM("random"), TWOBITSDIFFER("twobitsdiffer");

		private String title;

		Dictionary(String title) {
			this.title = title;
		}

		public static Dictionary getEnum( String value ) {
			for (Dictionary dict : Dictionary.values()) {
				if (dict.title.compareTo(value) == 0) {
					return dict;
				}
			}
			throw new IllegalArgumentException("Invalid Dictionary value: " + value);
		}
	}

}