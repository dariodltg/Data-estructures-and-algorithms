package Proyecto;

/**
 * @author de la Torre Guinaldo, Darío
 */
public class BloomFilter {

	private int tamFiltro;
	private byte[] vector;
	private int numBits;

	/**
	 * Crea un filtro de Bloom con el tamaño dado por parámetro.
	 * 
	 * @param tam Tamaño en bytes del filtro de Bloom.
	 */
	public BloomFilter(int tam) {
		tamFiltro = tam;
		vector = new byte[tamFiltro];
		numBits = tam * 8;

	}

	/**
	 * Crea un filtro de Bloom a partir de un array de bytes predeterminado.
	 * 
	 * @param vectorPredeterminado Array de bytes para iniciar el filtro de Bloom en
	 * un estado predefinido.
	 * 
	 */
	public BloomFilter(byte[] vectorPredeterminado) {
		tamFiltro = vectorPredeterminado.length;
		vector = vectorPredeterminado;
		numBits = tamFiltro * 8;
	}

	private int getTamFiltro() {
		return tamFiltro;
	}
	
	/**
	 * Devuelve el array de bytes del filtro de Bloom
	 * @return Array de bytes del filtro de Bloom
	 */
	public byte[] getVector() {
		return vector;
	}

	public int getNumBits() {
		return numBits;
	}

	/**
	 * Añade un elemento al conjunto representado por el filtro de Bloom.
	 * 
	 * @param elemento Cadena de caracteres que se quiere añadir al conjunto.
	 */
	public void addElemento(String elemento) {
		int posicion1 = hash1(elemento);
		int posicion2 = hash2(elemento);
		setBit(getVector(), posicion1);
		setBit(getVector(), posicion2);
	}

	/**
	 * Pregunta si un elemento pertenece o no al conjunto representado por el filtro de Bloom.
	 * 
	 * @param elemento Cadena de caracteres que se quiere saber si pertenece al
	 * conjunto
	 * 
	 * @return consulta. false - El elemento no pertenece al conjunto. true - El
	 * elemento puede pertenecer o no al conjunto. Es decir, puede dar falsos positivos.
	 */
	public boolean consultarElemento(String elemento) {
		boolean consulta = false;
		int posicion1 = hash1(elemento);
		int posicion2 = hash2(elemento);
		if ((getBit(getVector(), posicion1) == 1) && (getBit(getVector(), posicion2) == 1)) {
			consulta = true;
		}
		return consulta;
	}

	public int hash1(String txt) { // Hash usado en Java
		int n = txt.length();
		int h = 0;
		for (int i = 0; i < n; i++) {
			h = 31 * h + txt.charAt(i);
		}
		return Math.abs(h%getNumBits());
	}

	public int hash2(String txt) { // Hash usado en C#
		int n = txt.length();
		int h1 = 5381;
		for (int i = 0; i < n; i += 2) {
			h1 = (33 * h1) ^ txt.charAt(i);
		}
		int h2 = 5381;
		for (int i = 1; i < n; i += 2) {
			h2 = (33 * h2) ^ txt.charAt(i);
		}
		return Math.abs((h1 + (h2 * 1566083941)) % getNumBits());
	}

	// Pone a 1 el bit i‐esimo del array v
	public void setBit(byte[] v, int i) {
		v[i / 8] |= 1 << (i % 8);
	}

	// Devuelve el valor (0 o 1) del bit i‐esimo del array v
	public int getBit(byte[] v, int i) {
		return (v[i / 8] >> (i % 8)) & 1;
	}
}
