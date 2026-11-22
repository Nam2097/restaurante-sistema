package modelo.estructuras;

public class NodoAVL<T> {
    private T dato;
    private int altura;
    private NodoAVL<T> izquierdo;
    private NodoAVL<T> derecho;

    public NodoAVL(T dato) {
        this.dato = dato;
        this.altura = 1;
        this.izquierdo = null;
        this.derecho = null;
    }

    public T getDato() {
        return dato;
    }

    public int getAltura() {
        return altura;
    }

    public NodoAVL<T> getIzquierdo() {
        return izquierdo;
    }

    public NodoAVL<T> getDerecho() {
        return derecho;
    }
}