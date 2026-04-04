public abstract class Carta {
    private String nombre;

    public Carta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }


    public abstract void mostrarInfo();


    public abstract void jugar(Jugador usuario, Jugador oponente);


    public boolean esMonstruo() {
        return false;
    }


    public Monstruo comoMonstruo() {
        return null;
    }
}