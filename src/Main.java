import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Random rand = new Random();

    public static void main(String[] args) {
        System.out.println("=== YU-GI-OH! DUELO ===");
        System.out.print("Nombre del duelista 1: ");
        Jugador j1 = new Jugador(sc.nextLine());
        System.out.print("Nombre del duelista 2: ");
        Jugador j2 = new Jugador(sc.nextLine());

        
        List<Carta> mazoCompleto = crearCartas();
        Collections.shuffle(mazoCompleto);
        for (int i = 0; i < 20; i++) j1.agregarAlMazo(mazoCompleto.get(i));
        for (int i = 20; i < 40; i++) j2.agregarAlMazo(mazoCompleto.get(i));

        
        for (int i = 0; i < 5; i++) {
            j1.robarCarta();
            j2.robarCarta();
        }

        Jugador activo   = rand.nextBoolean() ? j1 : j2;
        Jugador oponente = (activo == j1) ? j2 : j1;
        boolean primerTurnoGlobal = true;

        System.out.println("\nEl duelo comienza! Primer turno: " + activo.getNombre());

        
        while (activo.estaVivo() && oponente.estaVivo()) {

            System.out.println("\nTurno de " + activo.getNombre());
            System.out.println(activo.getNombre() + ": " + activo.getLp() + " LP  |  " + oponente.getNombre() + ": " + oponente.getLp() + " LP");
            System.out.println("Cartas en mazo: " + activo.getMazo().size());

            
            if (!activo.tieneMazo()) {
                System.out.println(activo.getNombre() + " no tiene mas cartas para robar... derrota.");
                break;
            }
            activo.robarCarta();

            
            mostrarCampo(activo, oponente);

             
            System.out.println("\nQue hace " + activo.getNombre() + "?");
            System.out.println("1. Jugar una carta");
            System.out.println("2. Pasar");
            int op = leerEntero(1, 2);

            if (op == 1 && activo.tieneMano()) {
                System.out.print("Cual carta juega? (1-" + activo.getMano().size() + "): ");
                int idx = leerEntero(1, activo.getMano().size()) - 1;

                Carta elegida = activo.jugarCarta(idx);

                
                elegida.jugar(activo, oponente);

                
                if (elegida.esMonstruo()) {
                    Monstruo invocado = elegida.comoMonstruo();
                    if (primerTurnoGlobal) {
                        System.out.println("En el primer turno no se puede atacar.");
                        invocado.agotarAtaque(); 
                    } else {
                        ofrecerAtaque(invocado, activo, oponente);
                    }
                }
            }

            
            activo.agotarAtaquesMonstruos();

            
            Jugador temp = activo;
            activo   = oponente;
            oponente = temp;
            primerTurnoGlobal = false;
        }

        
        System.out.println("\nEl duelo ha terminado!");
        String ganador = j1.estaVivo() ? j1.getNombre() : j2.getNombre();
        System.out.println("El ganador es: " + ganador.toUpperCase());
    }

     

    private static void ofrecerAtaque(Monstruo invocado, Jugador activo, Jugador oponente) {
        if (!invocado.puedeAtacar()) return;
        System.out.println("\n" + activo.getNombre() + " quiere atacar con " + invocado.getNombre() + "?");
        System.out.println("1. Si  2. No");
        if (leerEntero(1, 2) == 1) {
            batallar(invocado, activo, oponente);
        }
    }

    private static void mostrarCampo(Jugador activo, Jugador oponente) {
        System.out.print("\nCampo de " + oponente.getNombre() + ": ");
        if (!oponente.tieneMonstruos()) {
            System.out.print("(nada por ahora)");
        } else {
            for (Monstruo m : oponente.getCampo()) m.mostrarInfo();
        }

        System.out.print("\nSu campo: ");
        if (!activo.tieneMonstruos()) {
            System.out.print("(nada por ahora)");
        } else {
            for (Monstruo m : activo.getCampo()) m.mostrarInfo();
        }

        System.out.println("\n\nCartas en la mano de " + activo.getNombre() + ":");
        List<Carta> mano = activo.getMano();
        for (int i = 0; i < mano.size(); i++) {
            System.out.print((i + 1) + ". ");
            mano.get(i).mostrarInfo();
            System.out.println();
        }
    }

    private static void batallar(Monstruo atacante, Jugador activo, Jugador oponente) {
        
        if (!oponente.tieneMonstruos()) {
            System.out.println(atacante.getNombre() + " ataca directo!");
            oponente.recibirDanio(atacante.getAtk());
            System.out.println(oponente.getNombre() + " recibe " + atacante.getAtk() + " de dano. LP: " + oponente.getLp());
            return;
        }
    }
}
