package modelo.estructuras;

public class AVL<T extends Comparable<T>> {
    private NodoAVL<T> raiz;
    private int tamagno;

    public AVL() {
        this.raiz = null;
        this.tamagno = 0;
    }

    public void insertar(T dato) {
        // TODO: Implementar inserción balanceada
    }

    public T buscar(T clave) {
        // TODO: Implementar búsqueda en el árbol
        return null;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int getTamagno() {
        return tamagno;
    }

    private int obtenerAltura(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 : nodo.getAltura();
    }

    private int obtenerFactorBalance(NodoAVL<T> nodo) {
        // TODO: Implementar cálculo de balance
        return 0;
    }

    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        // TODO: Implementar rotación simple derecha
        return y;
    }

    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        // TODO: Implementar rotación simple izquierda
        return x;
    }
}