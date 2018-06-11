package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * It is necessary to convert any file to .txt format for this application to
 * work http://pdftotext.com/pt/
 * 
 * @author Israel Deorce
 * @date/06/2019
 *
 */
public class App {

	// How many files contains the sentence
	public static int nOccurences = 0;

	public static void main(String[] args) {

		 String folderPath = args[0];
		 String ref = args[1];

		//String folderPath = ("C:/Users/Israel/Desktop/folder");
		//String ref = ("Pair Programming Illuminated");

		String[] splited = ref.split("\\s+");

		// For checking if the splited version of the reference is correct
		// for(int i=0; i<splited.length;i++) {
		// System.out.println(splited[i]);
		// }

		findReferencesByArticle(folderPath, ref, splited);

	}

	/**
	 * Method that gets every file from a given folder and send its path to the
	 * streamService Method
	 * 
	 * @param folderPath
	 * @param ref
	 * @param splited
	 */
	public static void findReferencesByArticle(String folderPath, String ref, String[] splited) {
		System.out.println(folderPath);
		try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
			paths.filter(Files::isRegularFile).forEach(p -> streamService(p, ref, splited));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Method that verifies the occurrence of a given string in a given text file
	 * and informs the user
	 * 
	 * @param folderPath
	 * @param ref
	 * @param splited
	 */
	private static void streamService(Path path, String ref, String[] splited) {
		boolean hasRef = false;
		String format = "%-23s%s%n";

		try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("ISO-8859-1")))) {
			sc.useDelimiter("(,|\\s|\\$)+"); // separadores
			while (sc.hasNext()) {
				if (sc.next().equalsIgnoreCase(splited[0])) {
					hasRef = true;
					for (int i = 1; i < splited.length; i++) {
						if (!sc.next().equalsIgnoreCase(splited[i]))
							hasRef = false;
					}
					if (hasRef) {
						nOccurences++;
						System.out.printf(format, nOccurences + " - #CONTAINS!", path);
						return;
					}
				}
			}
			if (!hasRef) {
				nOccurences++;
				System.out.printf(format, nOccurences + " - DO NOT Contains!", path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
