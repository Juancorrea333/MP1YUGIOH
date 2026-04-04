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

            // RF5: Menú de acciones 
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

        
        System.out.println("A cual monstruo ataca " + activo.getNombre() + "?");
        List<Monstruo> campoOponente = oponente.getCampo();
        for (int i = 0; i < campoOponente.size(); i++) {
            System.out.print((i + 1) + ". ");
            campoOponente.get(i).mostrarInfo();
            System.out.println();
        }
        int idx = leerEntero(1, campoOponente.size()) - 1;
        Monstruo defensor = campoOponente.get(idx);

        
        if (atacante.getAtk() > defensor.getAtk()) {
            int danio = atacante.getAtk() - defensor.getAtk();
            System.out.println(defensor.getNombre() + " fue destruido! Dano: " + danio);
            oponente.recibirDanio(danio);
            oponente.removerMonstruo(defensor);
        } else if (atacante.getAtk() == defensor.getAtk()) {
            System.out.println("Empate! Los dos monstruos se destruyen.");
            activo.removerMonstruo(atacante);
            oponente.removerMonstruo(defensor);
        } else {
            int danio = defensor.getAtk() - atacante.getAtk();
            System.out.println(atacante.getNombre() + " fue destruido. " + activo.getNombre() + " recibe " + danio + " de dano.");
            activo.recibirDanio(danio);
            activo.removerMonstruo(atacante);
        }
    }

    
    private static int leerEntero(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.print("Tiene que ser un numero entre " + min + " y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Eso no es un numero, intente de nuevo: ");
            }
        }
    }

    public static List<Carta> crearCartas() {
        List<Carta> mazo = new ArrayList<>();

         
        mazo.add(new Monstruo("Mago Oscuro",             2500, 2100, 7));
        mazo.add(new Monstruo("Dragon Blanco",            3000, 2500, 8));
        mazo.add(new Monstruo("Gaia el Caballero",        2300, 2100, 7));
        mazo.add(new Monstruo("Summoned Skull",           2500, 1200, 6));
        mazo.add(new Monstruo("Maldicion de Dragon",      2000, 1500, 5));
        mazo.add(new Monstruo("Castor Warrior",           1200, 1500, 4));
        mazo.add(new Monstruo("Celtic Guardian",          1400, 1200, 4));
        mazo.add(new Monstruo("Giant Soldier of Stone",   1300, 2000, 3));
        mazo.add(new Monstruo("Feral Imp",                1300, 1400, 4));
        mazo.add(new Monstruo("Gogiga Gagagigo",          2450, 1500, 8));
        mazo.add(new Monstruo("Silver Fang",              1200,  800, 3));
        mazo.add(new Monstruo("Mystical Elf",              800, 2000, 4));
        mazo.add(new Monstruo("Baby Dragon",              1200,  700, 3));
        mazo.add(new Monstruo("Ryu-Kishin",               1000,  500, 3));
        mazo.add(new Monstruo("Luster Dragon",            1900, 1600, 4));
        mazo.add(new Monstruo("Fireyarou",                1300, 1000, 4));
        mazo.add(new Monstruo("Tyhone",                   1200, 1400, 4));
        mazo.add(new Monstruo("Aquadoor",                  100, 2100, 3));
        mazo.add(new Monstruo("Battle Steer",             1800, 1300, 5));
        mazo.add(new Monstruo("Armored Zombie",           1500,    0, 3));
        mazo.add(new Monstruo("Dragon Zombie",            1600,    0, 3));
        mazo.add(new Monstruo("Clown Zombie",             1350,    0, 2));
        mazo.add(new Monstruo("Judge Man",                2200, 1500, 6));
        mazo.add(new Monstruo("Kojikocy",                 1500, 1200, 4));
        mazo.add(new Monstruo("Torike",                   1200,  600, 3));
        mazo.add(new Monstruo("Uraby",                    1500,  800, 4));
        mazo.add(new Monstruo("Winged Dragon #1",         1400, 1200, 4));
        mazo.add(new Monstruo("Saggi the Dark Clown",      600, 1500, 3));
        mazo.add(new Monstruo("Skull Servant",             300,  200, 1));
        mazo.add(new Monstruo("Hitotsu-Me Giant",         1200, 1000, 4));

         
        mazo.add(new Magica("Pot of Greed",  "usted roba 2 cartas adicionales",
                (u, o) -> { u.robarCarta(); u.robarCarta(); }));
        mazo.add(new Magica("Pot of Greed",  "usted roba 2 cartas adicionales",
                (u, o) -> { u.robarCarta(); u.robarCarta(); }));
        mazo.add(new Magica("Dian Keto",     "usted recupera 1000 LP",
                (u, o) -> u.recuperarLp(1000)));
        mazo.add(new Magica("Dian Keto",     "usted recupera 1000 LP",
                (u, o) -> u.recuperarLp(1000)));
        mazo.add(new Magica("Hinotama",      "el rival recibe 500 de dano",
                (u, o) -> o.recibirDanio(500)));
        mazo.add(new Magica("Hinotama",      "el rival recibe 500 de dano",
                (u, o) -> o.recibirDanio(500)));
        mazo.add(new Magica("Raigeki",       "destruye todos los monstruos del rival",
                (u, o) -> {
                    if (o.tieneMonstruos()) {
                        System.out.println("Todos los monstruos de " + o.getNombre() + " fueron destruidos!");
                        o.getCampo().clear();
                    } else {
                        System.out.println(o.getNombre() + " no tiene monstruos en campo.");
                    }
                }));
        mazo.add(new Magica("Raigeki",       "destruye todos los monstruos del rival",
                (u, o) -> {
                    if (o.tieneMonstruos()) {
                        System.out.println("Todos los monstruos de " + o.getNombre() + " fueron destruidos!");
                        o.getCampo().clear();
                    } else {
                        System.out.println(o.getNombre() + " no tiene monstruos en campo.");
                    }
                }));
        mazo.add(new Magica("Terraforming",  "su monstruo mas fuerte gana 500 ATK",
                (u, o) -> {
                    if (!u.tieneMonstruos()) {
                        System.out.println(u.getNombre() + " no tiene monstruos en campo.");
                        return;
                    }
                    Monstruo objetivo = u.getCampo().stream()
                            .max(Comparator.comparingInt(Monstruo::getAtk))
                            .orElse(null);
                    if (objetivo != null) {
                        objetivo.aumentarAtk(500);
                        System.out.println(objetivo.getNombre() + " ahora tiene " + objetivo.getAtk() + " ATK!");
                    }
                }));
        mazo.add(new Magica("Spark",         "el rival recibe 200 de dano",
                (u, o) -> o.recibirDanio(200)));

        return mazo;
    }
}
