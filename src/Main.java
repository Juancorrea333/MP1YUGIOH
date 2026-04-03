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
    }
}
