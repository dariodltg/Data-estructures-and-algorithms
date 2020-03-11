//Autor: Dario de la Torre Guinaldo, T1

package Proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GeneraFicheros {

	public static void main(String[] args) throws IOException {
		System.out.println("Introduzca la ruta del fichero que quiere dividir en subficheros:");
		Scanner entrada = new Scanner(System.in);
		String ruta_al_fichero = entrada.nextLine();
		LeerFichero(ruta_al_fichero);
		entrada.close();
	}

	public static void LeerFichero(String ruta_al_fichero) throws IOException {
		final char FIN_DE_LINEA = 10;
		FileReader fr;
		String cadena;
		Path ruta_fichero = Paths.get(ruta_al_fichero);
		Path ruta_directorio = ruta_fichero.getParent();// Obtiene la ruta del directorio del fichero a dividir
		fr = new FileReader(ruta_al_fichero);
		BufferedReader br = new BufferedReader(fr);
		String[] fichero = new String[2000000];
		int j = 0;
		while (br.ready()) {
			String linea = br.readLine();
			fichero[j] = linea; // Crea un array de strings para ordenarlo
			j++;
		}
		quicksort(fichero,0,fichero.length-1);
		int espacio_maximo = 0; // espacio maximo de cada subfichero
		int i = 0; // numero de subficheros que se crearan
		int indice = 0; // indice del vector de Strings
		// Bucle de crear subficheros
		while (indice < fichero.length) { // Crea los subficheros
			String numero_fichero = Integer.toString(i);
			if (i < 10) {
				numero_fichero = "00" + numero_fichero;
			} else if (i >= 10 & i < 100) {
				numero_fichero = "0" + numero_fichero;
			} else if (i >= 100) {
			}
			File subfichero = new File(ruta_directorio + "\\" + numero_fichero + ".txt");
			if (subfichero.exists() == true) {
				subfichero.delete();
			}
			FileWriter creador_de_ficheros = new FileWriter(ruta_directorio + "\\" + numero_fichero + ".txt");
			espacio_maximo = 0;
			// Bucle de copiar lineas
			while ((indice < fichero.length) && (espacio_maximo < 700000)) {
				cadena = fichero[indice];
				cadena = cadena + FIN_DE_LINEA;
				espacio_maximo = espacio_maximo + cadena.length(); // acumula la longitud de cada linea
				creador_de_ficheros.write(cadena); // copia cada linea en el subfichero actual recien creado
				indice++;
			}
			creador_de_ficheros.close();
			i++;
		}
		br.close();
	}
	public static void quicksort(String A[], int izq, int der) {

		String pivote = A[izq]; // tomamos primer elemento como pivote
		int i = izq; // i realiza la búsqueda de izquierda a derecha
		int j = der; // j realiza la búsqueda de derecha a izquierda
		String aux;

		while (i < j) { // mientras no se crucen las búsquedas
			while (A[i].compareTo(pivote) <= 0 && i < j) {
				i++;
			} // busca elemento mayor que pivote
			while (A[j].compareTo(pivote) > 0) {
				j--;
			} // busca elemento menor que pivote
			if (i < j) { // si no se han cruzado
				aux = A[i]; // los intercambia
				A[i] = A[j];
				A[j] = aux;
			}
		}
		A[izq] = A[j]; // se coloca el pivote en su lugar de forma que tendremos
		A[j] = pivote; // los menores a su izquierda y los mayores a su derecha
		if (izq < j - 1)
			quicksort(A, izq, j - 1); // ordenamos subarray izquierdo
		if (j + 1 < der)
			quicksort(A, j + 1, der); // ordenamos subarray derecho
	}
}
