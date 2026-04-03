public class Monstruo extends Carta {
    private int atk;
    private int def;
    private int nivel;
    private boolean puedeAtacar;

    public Monstruo(String nombre, int atk, int def, int nivel) {
        super(nombre);
        this.atk = atk;
        this.def = def;
        this.nivel = nivel;
        this.puedeAtacar = false;
    }

    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getNivel() { return nivel; }
    public boolean puedeAtacar() { return puedeAtacar; }

    public void aumentarAtk(int cantidad) {
        this.atk += cantidad;
    }


    public void habilitarAtaque() {
        this.puedeAtacar = true;
    }


    public void agotarAtaque() {
        this.puedeAtacar = false;
    }

    @Override
    public boolean esMonstruo() {
        return true;
    }

    @Override
    public Monstruo comoMonstruo() {
        return this;
    }


    @Override
    public void jugar(Jugador usuario, Jugador oponente) {
        usuario.invocarMonstruo(this);
        this.habilitarAtaque();
        System.out.println("¡" + usuario.getNombre() + " invoca a " + getNombre() + "!");
    }

    @Override
    public void mostrarInfo() {
        System.out.print("[" + getNombre() + " ATK:" + atk + " DEF:" + def + " LVL:" + nivel + "]");
    }
}