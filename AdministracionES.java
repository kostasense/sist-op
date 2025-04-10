import java.util.*;

public class AdministracionES {
    static Random rd = new Random();

    public static void fscan(Proceso p, int cabeza) {

        if (p.getPeticiones().isEmpty()) return;

        System.out.println("\n");
        System.out.println("""
                    ┌───────────────────────────┐
                    │         SES-HHDD          │       
                    └───────────────────────────┘""");

        LinkedList<Peticion> activa = new LinkedList<>(p.getPeticiones());
        LinkedList<Peticion> espera = new LinkedList<>(p.getPeticiones());

        int distancia = 0, rendimiento = 0, direccion = 1;
        Peticion peticionActual = null;

        PriorityQueue<Peticion> derecha = new PriorityQueue<>((a, b) -> Integer.compare(a.getSector(), b.getSector()));
        PriorityQueue<Peticion> izquierda = new PriorityQueue<>((a, b) -> Integer.compare(b.getSector(), a.getSector()));


        while (!activa.isEmpty()) {
            for (Peticion peticion : espera)
                if (peticion.getSector() > cabeza) derecha.add(peticion); else izquierda.add(peticion);
            espera.clear();

            while (!derecha.isEmpty() || !izquierda.isEmpty()) {
                peticionActual = (direccion == 1 ? derecha.poll() : izquierda.poll());
                distancia = Math.abs(peticionActual.getSector() - cabeza);
                rendimiento += (distancia + peticionActual.getCosto());
                cabeza = peticionActual.getSector();

                if (direccion == 1 && derecha.isEmpty()) {
                    direccion = -1;
                    rendimiento += Math.abs(20 - cabeza);
                    cabeza = 20;
                } else if (direccion == -1 && izquierda.isEmpty()) {
                    direccion = 1;
                    rendimiento += Math.abs(1 - cabeza);
                    cabeza = 1;
                }

                Planificador.generarPeticiones(p, espera);
                System.out.printf("%n • Cola activa: %s", activa);
                System.out.printf("%n • Petición atendida: %s%n • Peticiones en espera: %s%n%n", peticionActual, espera);
            }
            activa = new LinkedList<>(espera);
        }

        System.out.printf("""
            ╔═══════════════════════════════════╗
            ║ Peticiones terminadas.            ║
            ╠═══════════════════════════════════╣
            ║ Rendimiento: %d                   ║
            ╚═══════════════════════════════════╝
                        """, rendimiento);

        p.getPeticiones().removeAll(p.getPeticiones());
    }

    public static void scanN(Proceso p, int cabeza, int elementos) {
        if (p.getPeticiones().isEmpty()) return;

        System.out.println("\n");
        System.out.println("""
                    ┌───────────────────────────┐
                    │         SES-HHDD          │       
                    └───────────────────────────┘""");

        ArrayList<LinkedList<Peticion>> colas = new ArrayList<>();
        LinkedList<Peticion> espera = new LinkedList<>();
        int i = 0;

        while (i < p.getPeticiones().size()) {
            LinkedList<Peticion> grupo = new LinkedList<>();
    
            for (int j = 0; j < elementos && i < p.getPeticiones().size(); j++, i++)
                grupo.add(p.getPeticiones().get(i));
    
            colas.add(grupo);
        }

        p.getPeticiones().clear();

        int distancia = 0, rendimiento = 0, direccion = 1;
        Peticion peticionActual = null;

        PriorityQueue<Peticion> derecha = new PriorityQueue<>((a, b) -> Integer.compare(a.getSector(), b.getSector()));
        PriorityQueue<Peticion> izquierda = new PriorityQueue<>((a, b) -> Integer.compare(b.getSector(), a.getSector()));

        i = 0;
        while (i < colas.size()) {
            LinkedList<Peticion> cola = colas.get(i);
            i++;
        
            for (Peticion peticion : cola)
                if (peticion.getSector() > cabeza) derecha.add(peticion); else izquierda.add(peticion);
        
            while (!derecha.isEmpty() || !izquierda.isEmpty()) {
                peticionActual = (direccion == 1 ? derecha.poll() : izquierda.poll());
                distancia = Math.abs(peticionActual.getSector() - cabeza);
                rendimiento += (distancia + peticionActual.getCosto());
                cabeza = peticionActual.getSector();

                if (direccion == 1 && derecha.isEmpty()) {
                    direccion = -1;
                    rendimiento += Math.abs(20 - cabeza);
                    cabeza = 20;
                } else if (direccion == -1 && izquierda.isEmpty()) {
                    direccion = 1;
                    rendimiento += Math.abs(1 - cabeza);
                    cabeza = 1;
                }
        
                Planificador.generarPeticiones(p, espera);                
        
                LinkedList<Peticion> nueva = new LinkedList<>();
                LinkedList<Peticion> reciente = colas.get(colas.size() - 1);
        
                if (reciente.size() < elementos && !reciente.equals(cola)) {
                    while (reciente.size() < elementos && !espera.isEmpty())
                        reciente.add(espera.poll());

                } else {
                    while (nueva.size() < elementos && !espera.isEmpty())
                        nueva.add(espera.poll());

                    colas.add(nueva);
                }
    
                System.out.printf("%n • Cola activa: %s", cola);
                System.out.printf("%n • Petición atendida: %s%n", peticionActual);
                System.out.printf(" • Peticiones en espera: %n");
        
                for (int j = i, x = 1; j < colas.size(); j++, x++)
                    System.out.printf("     %s%s%n", String.format("Cola %d: ", x), colas.get(j));
        
                System.out.println("\n");
            }
        }

        System.out.printf("""
            ╔═══════════════════════════════════╗
            ║ Peticiones terminadas.            ║
            ╠═══════════════════════════════════╣
            ║ Rendimiento: %d                   ║
            ╚═══════════════════════════════════╝
                        """, rendimiento);

        p.getPeticiones().removeAll(p.getPeticiones());
    }

}
