import java.util.*;

public class AdministracionES {
    static Random rd = new Random();

    public static void fscan(Proceso p, int cabeza) {

        if (p.getPeticiones().isEmpty() == true) return;

        System.out.println("\n");
        System.out.println("""
                    ┌───────────────────────────┐
                    │         SES-HHDD          │       
                    └───────────────────────────┘""");

        LinkedList<Peticion> q = new LinkedList<>(p.getPeticiones());

        int distancia = 0, rendimiento = 0, direccion = 1;
        Peticion peticionActual = null;

        PriorityQueue<Peticion> derecha = new PriorityQueue<>((a, b) -> Integer.compare(a.getSector(), b.getSector()));
        PriorityQueue<Peticion> izquierda = new PriorityQueue<>((a, b) -> Integer.compare(b.getSector(), a.getSector()));

        while (!q.isEmpty()) {
            System.out.printf("%n • Peticiones a atender: %s", q);

            while (!q.isEmpty()) {
                if (q.peek().getSector() > cabeza)
                    derecha.add(q.pop());
                else
                    izquierda.add(q.pop());
            }

            while (!derecha.isEmpty() || !izquierda.isEmpty()) {
                peticionActual = (direccion == 1 ? derecha.poll() : izquierda.poll());
                distancia = Math.abs(peticionActual.getSector() - cabeza);
                rendimiento += (distancia + peticionActual.getCosto());
                cabeza = peticionActual.getSector();
    
                if (direccion == 1 && derecha.isEmpty()) direccion = -1;
                else if (direccion == -1 && izquierda.isEmpty()) direccion = 1;
    
                Planificador.generarPeticiones(p, q);

                System.out.printf("%n • Petición atendida: %s%n %s%n", peticionActual, (q.isEmpty() ? "" : String.format("• Peticiones en espera: %s%n", q)));
            }
        }

        System.out.println("""
            ╔═══════════════════════════════════╗
            ║ Peticiones terminadas.            ║
            ╚═══════════════════════════════════╝
                        """);

        System.out.printf(" • Rendimiento: %d%n", rendimiento);
        p.getPeticiones().removeAll(p.getPeticiones());
    }
}
