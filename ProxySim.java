//Autor: Dario de la Torre Guinaldo, T1
package Proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProxySim {
	public static long memIni;
	public static Runtime rt;

	public static long usoMemoria() {
		rt.gc();
		return rt.totalMemory() - rt.freeMemory() - memIni;
	}

	public static void main(String[] args) throws IOException {
		rt = Runtime.getRuntime();
		rt.gc();
		memIni = rt.totalMemory() - rt.freeMemory();
		Scanner entrada_fichero = new Scanner(System.in);
		System.out.println("Introduzca la ruta al fichero que quiere analizar: ");
		String ruta_fichero = entrada_fichero.nextLine(); // ruta del fichero que se quiere analizar
		entrada_fichero.close();
		LeerFichero(ruta_fichero);

	}

	public static void LeerFichero(String ruta_fichero) throws IOException {
		long uso_maximo = 0; // Guardara el valor maximo del uso de memoria
		long uso;
		int llamadas_lee_subfichero = 0; // Veces que se lee un subfichero
		long numero_comparaciones = 0; // Numero de comparaciones de string realizadas
		long urls_comprobadas = 0; // URLS comprobadas del fichero que se quiere analizar
		FileReader fr_fichero = new FileReader(ruta_fichero);
		BufferedReader br_fichero = new BufferedReader(fr_fichero);
		Path ruta = Paths.get(ruta_fichero);
		Path ruta_directorio = ruta.getParent();
		String cadena_ruta_directorio = ruta_directorio.toString();
		int numero_subficheros = NumeroSubficheros(cadena_ruta_directorio); // Numero de subficheros
		System.out.println(numero_subficheros);
		while (br_fichero.ready()) { // Lee el fichero que se quiere analizar
			String URL_fichero = br_fichero.readLine(); // Lee una linea del fichero
			urls_comprobadas++;
			int i = 0; // Numero del subfichero
			while (i < numero_subficheros) {
				boolean prohibida=false;
				String cadena_ruta_subfichero = null;
				if (i < 10) {
					cadena_ruta_subfichero = cadena_ruta_directorio + "\\00" + i + ".txt";
				} else if (i >= 10 & i < 100) {
					cadena_ruta_subfichero = cadena_ruta_directorio + "\\0" + i + ".txt";
				} else if (i >= 100) {
					cadena_ruta_subfichero = cadena_ruta_directorio + "\\" + i + ".txt";
				}
				llamadas_lee_subfichero++;
				leeSubfichero(cadena_ruta_subfichero);
				creaIndices();
				uso = usoMemoria();
				int n = 0; // Numero de URLS del subfichero
				for (int j = 0; j < subfich.length; j++) {
					if (subfich[j] == 10) {
						n++; // Cuenta el numero de URLs del subfichero
					}
				}
				int k = 0;
				while (k < n) {
					// Recorre los indices de un subfichero
					String URL_danger = accesoURL(k);
					prohibida = URL_fichero.equals(URL_danger); // Averigua si la URL es prohibida
					numero_comparaciones++;
					if (prohibida == true) {
						System.out.println(URL_fichero);
						break; //Si es prohibida deja de analizar el subfichero
					}
					k++;
				}
				if (uso > uso_maximo) {
					uso_maximo = uso;
				}
				i++;
				if (prohibida==true) {
					break; //Si ha detectado que es prohibida analiza la siguiente URL
				}
				
			}

		}
		br_fichero.close();
		long ratio_lecturas = llamadas_lee_subfichero / urls_comprobadas;
		System.out.println("Urls comprobaddas: " + urls_comprobadas);
		System.out.println("Llamadas a la función leeSubfichero: " + llamadas_lee_subfichero);
		System.out.println("Ratio de lecturas: " + ratio_lecturas);
		System.out.println("Número de comparaciones realizadas: " + numero_comparaciones);
		System.out.println("Uso máximo de la memoria: " + uso_maximo);

	}

	public static int NumeroSubficheros(String cadena_ruta_directorio) throws IOException {
		// Cuenta los subficheros antes de empezar analizar el fichero
		int numero_subficheros = 0;
		int i = 0;
		boolean hay_otro_subfichero = true;
		String cadena_ruta_subfichero = null;
		do {
			if (i < 10) {
				cadena_ruta_subfichero = cadena_ruta_directorio + "\\00" + i + ".txt";
			} else if (i >= 10 & i < 100) {
				cadena_ruta_subfichero = cadena_ruta_directorio + "\\0" + i + ".txt";
			} else if (i >= 100) {
				cadena_ruta_subfichero = cadena_ruta_directorio + "\\" + i + ".txt";
			}
			try {
				FileReader subfichero = new FileReader(cadena_ruta_subfichero);
				subfichero.close();
			} catch (FileNotFoundException e) {
				hay_otro_subfichero = false; // si detecta que no hay mas subficheros saldra de este bucle
			}
			i++;
			numero_subficheros = i - 1;

		} while (hay_otro_subfichero == true);

		return numero_subficheros;
	}

	static byte[] subfich; // Datos del subfichero (var. global)

	static void leeSubfichero(String nomfich) throws IOException {
		File fich = new File(nomfich);
		int tam = (int) fich.length(); // Tamaño en bytes
		subfich = null;
		subfich = new byte[tam];
		try (FileInputStream fis = new FileInputStream(fich)) {
			fis.read(subfich); // Lectura de todo el fichero
		}
	}

	static int[] indURL; // Indices de las URLs en "subfich"

	static void creaIndices() {
		// 1. Contar el numero de URLs
		int n = 0;
		for (int i = 0; i < subfich.length; i++) {
			if (subfich[i] == 10) {
				n++;
			}
		}
		// 2. Almacenar posición de separadores
		indURL = null;
		indURL = new int[n];
		int k = 0;
		for (int i = 0; i < subfich.length; i++) {
			if (subfich[i] == 10) {
				indURL[k++] = i;
			}
		}
	}

	static String accesoURL(int i) {
		int a = i == 0 ? 0 : indURL[i - 1] + 1;
		int b = indURL[i] - 1;
		return new String(subfich, a, b - a + 1, StandardCharsets.US_ASCII);
	}
}
