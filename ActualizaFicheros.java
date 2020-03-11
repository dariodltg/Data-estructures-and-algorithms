//Autor: Dario de la Torre Guinaldo, T1
package Proyecto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ActualizaFicheros {
	public static void main(String[] args) throws IOException {
		Actualizar_subfichero();

	}

	public static void Actualizar_subfichero() throws IOException {
		System.out.println("Introduzca la URL que desea añadir:");
		Scanner entrada = new Scanner(System.in);
		String URL = entrada.nextLine(); // nombre de la URL que se quiere añadir
		System.out.println("Introduzca la ruta al subfichero que desea actualizar:");
		String ruta_al_subfichero = entrada.nextLine(); // ruta del subfichero
		File subfichero = new File(ruta_al_subfichero);

		FileWriter escribir = new FileWriter(subfichero, true);
		escribir.write("\r\n");
		escribir.write(URL); // añade la URL al subfichero
		escribir.close();

		entrada.close();

	}

}
