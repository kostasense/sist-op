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
            for (Peticion peticion : espera) {
                int sector = peticion.getSector();
                (sector == cabeza 
                    ? (direccion == -1 ? izquierda : derecha) 
                    : (sector > cabeza ? derecha : izquierda)
                ).add(peticion);
            }

            espera.clear();

            while (!derecha.isEmpty() || !izquierda.isEmpty()) {
                peticionActual = (direccion == 1 
                                    ? derecha.poll() 
                                    : izquierda.poll());
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
        
            for (Peticion peticion : cola) {
                int sector = peticion.getSector();
                (sector == cabeza 
                    ? (direccion == -1 ? izquierda : derecha) 
                    : (sector > cabeza ? derecha : izquierda)
                ).add(peticion);
            }
        
            while (!derecha.isEmpty() || !izquierda.isEmpty()) {
                peticionActual = (direccion == 1 
                                    ? derecha.poll() 
                                    : izquierda.poll());

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

    public static void cScan(Proceso p) {

        System.out.println("\n");
        System.out.println("""
                    ┌───────────────────────────┐
                    │         SES-HHDD          │       
                    └───────────────────────────┘""");

        ArrayList<Peticion> peticiones = p.getPeticiones();

        if (peticiones == null) {
            System.out.println("""
            ╔═══════════════════════════════════╗
            ║ No se encontraron peticiones.     ║
            ╚═══════════════════════════════════╝
                        """);
            return;
        }

        if (peticiones.isEmpty() == true) {
            System.out.println("""
            ╔═══════════════════════════════════╗
            ║ No se encontraron peticiones.     ║
            ╚═══════════════════════════════════╝
                        """);
            return;
        }

        int retardoGiro = 0;
        int transferencia = 0;
        int sectorActual = 0;

        while (!peticiones.isEmpty()) {
            Collections.sort(peticiones, Comparator.comparingInt(Peticion::getSector));

            System.out.println("\n • Peticiones actuales: " + peticiones.toString());

            Peticion siguiente = null;

            for (Peticion peticion : peticiones) {
                if (peticion.getSector() >= sectorActual) {
                    siguiente = peticion;
                    break;
                }
            }

            if (siguiente == null) {
                retardoGiro += ((20 - sectorActual) + 20);
                sectorActual = 0;
                siguiente = peticiones.get(0);
            }

            System.out.println(" • Petición a atender: " + siguiente.toString());

            retardoGiro += (siguiente.getSector() - sectorActual);
            sectorActual = siguiente.getSector();
            transferencia += siguiente.getCosto();

            peticiones.remove(siguiente);

            Planificador.generarPeticiones(p, peticiones);
        }

        System.out.println("""
            ╔═══════════════════════════════════╗
            ║ Peticiones terminadas.            ║
            ╚═══════════════════════════════════╝
                        """);
        
        System.out.println(" • Retardo de giro: " + retardoGiro);
        System.out.println(" • Tiempo de transferencia: " + transferencia);
    }

    public static void sstf(Proceso p, int cabeza) {
        if (p.getPeticiones().isEmpty()) {
            System.out.println("\nNo hay peticiones de E/S para atender.");
            return;
        }
    
        System.out.println("\n--- Algoritmo SSTF Simplificado ---");
        System.out.println("Posición inicial del cabezal: " + cabeza);
        
        // Copiamos las peticiones para no modificar las originales durante el proceso
        LinkedList<Peticion> peticiones = new LinkedList<>(p.getPeticiones());
        int rendimientoTotal = 0;
        
        while (!peticiones.isEmpty()) {
            // Paso 1: Buscar la petición más cercana al cabezal
            Peticion masCercana = null;
            int distanciaMinima = Integer.MAX_VALUE;
            int indiceMasCercana = -1;
            
            for (int i = 0; i < peticiones.size(); i++) {
                Peticion actual = peticiones.get(i);
                int distancia = Math.abs(actual.getSector() - cabeza);
                
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    masCercana = actual;
                    indiceMasCercana = i;
                }
            }
            
            if (masCercana == null) break;
            
            // Paso 2: Atender la petición
            int costo = masCercana.getCosto();
            rendimientoTotal += (distanciaMinima + costo);
            
            System.out.printf("\nAtendiendo: Sector %d, Tipo %c (Distancia: %d, Costo: %d)",
                             masCercana.getSector(), masCercana.getTipo(), 
                             distanciaMinima, costo);
            
            // Paso 3: Mover el cabezal y eliminar la petición atendida
            cabeza = masCercana.getSector();
            peticiones.remove(indiceMasCercana);
            
            // Paso 4: Posibilidad de generar nuevas peticiones (opcional)
            if (rd.nextBoolean() && peticiones.size() < 5) {
                int nuevoSector = rd.nextInt(20) + 1;
                char tipo = rd.nextBoolean() ? 'L' : 'E';
                int nuevoCosto = (tipo == 'L' ? 1 : 2);
                Peticion nueva = new Peticion(nuevoSector, tipo, nuevoCosto);
                
                // Verificar que no exista ya una petición para este sector
                boolean existe = false;
                for (Peticion pet : peticiones) {
                    if (pet.getSector() == nuevoSector) {
                        existe = true;
                        break;
                    }
                }
                
                if (!existe) {
                    peticiones.add(nueva);
                    System.out.printf("\n → Nueva petición generada: %s", nueva);
                }
            }
        }
        
        System.out.println("\n\n--- Resumen ---");
        System.out.println("Rendimiento total: " + rendimientoTotal);
        System.out.println("Posición final del cabezal: " + cabeza);
        System.out.println("Peticiones atendidas: " + (p.getPeticiones().size() - peticiones.size()));
        
        // Actualizar las peticiones del proceso
        p.getPeticiones().clear();
        p.getPeticiones().addAll(peticiones);
    }
}
