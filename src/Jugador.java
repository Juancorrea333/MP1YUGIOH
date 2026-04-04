import java.util.*;

public class Jugador {
    private String nombre;
    private int lp;
    private List<Carta> mazo;
    private List<Carta> mano;
    private List<Monstruo> campo;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.lp = 8000;
        this.mazo = new ArrayList<>();
        this.mano = new ArrayList<>();
        this.campo = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public int getLp() { return lp; }
    public List<Carta> getMazo() { return mazo; }
    public List<Carta> getMano() { return mano; }
    public List<Monstruo> getCampo() { return campo; }

    public void recibirDanio(int danio) {
        this.lp = Math.max(0, this.lp - danio);
    }

    public void recuperarLp(int cantidad) {
        this.lp += cantidad;
    }

    public boolean estaVivo() {
        return this.lp > 0;
    }

    public boolean tieneMazo() {
        return !mazo.isEmpty();
    }

    public void agregarAlMazo(Carta carta) {
        mazo.add(carta);
    }

    public void robarCarta() {
        if (!mazo.isEmpty()) {
            mano.add(mazo.remove(0));
        }
    }

    public Carta jugarCarta(int indice) {
        return mano.remove(indice);
    }

    public void invocarMonstruo(Monstruo monstruo) {
        campo.add(monstruo);
    }

    public void removerMonstruo(Monstruo monstruo) {
        campo.remove(monstruo);
    }

    public boolean tieneMonstruos() {
        return !campo.isEmpty();
    }

    public boolean tieneCarta(int indice) {
        return indice >= 0 && indice < mano.size();
    }

    public boolean tieneMano() {
        return !mano.isEmpty();
    }

    public void agotarAtaquesMonstruos() {
        for (Monstruo m : campo) {
            m.agotarAtaque();
        }
    }
}