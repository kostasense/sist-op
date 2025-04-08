import java.lang.reflect.Field;
import java.util.*;

public class Planificador {
    static Random rd = new Random();
    static HashMap<String, Pair<String, String>> algoritmos = new HashMap<>();
    static int[] peticiones = new int[50];

    public static void main(String[] args) {
        parametrosAlgoritmos();
        String opcion;

        menu();
        System.out.print("Opcion: ");
        opcion = Keyboard.readString();

        while (!opcion.equalsIgnoreCase("F")) {
            if (opcion.matches("\\d+") && (Integer.parseInt(opcion) >= 1 && Integer.parseInt(opcion) <= 10)) {
                break;
            } else {
                System.out.println("Elija una opcion valida: ");
                opcion = Keyboard.readString();
            }
        }
        

        while (!opcion.equalsIgnoreCase("F")) {
            int sim = rd.nextInt(15) + 20; 
            int quantum = rd.nextInt(6) + 5; 
            System.out.printf("%nSelección de parámetros:%n" + 
                              "  %s%n  %s%n  %s" + (algoritmos.get(opcion).second().equals("Apropiativa") ? (!opcion.equals("10") && !opcion.equals("7") ? "%n  %s%n" : "") : "%n"),
                              "• Tipo de Planificación: " + algoritmos.get(opcion).second(), 
                              "• Algoritmo: " + algoritmos.get(opcion).first(), 
                              "• Tiempo de simulación: " + sim + " unidades", 
                              "• Quantum: " + quantum);
            
            ArrayList<Proceso> procesos = new ArrayList<>();
            generarProcesos(procesos);

            switch (opcion) {
                case "1" -> AdministracionProcesos.roundRobinA(procesos, sim, quantum);
                case "2" -> AdministracionProcesos.roundRobinNA(procesos, sim);
                case "3" -> AdministracionProcesos.prioridadA(procesos, sim, quantum);
                case "4" -> AdministracionProcesos.prioridadNA(procesos, sim);
                case "5" -> AdministracionProcesos.multiplesColas(procesos, sim);
                case "6" -> AdministracionProcesos.masCortoPrimero(procesos, sim, quantum);
                case "7" -> AdministracionProcesos.planificacionGarantizada(procesos, sim);
                case "8" -> AdministracionProcesos.loteriaApropiativa(procesos, sim, quantum);
                case "9" -> AdministracionProcesos.loteriaNoApropiativa(procesos, sim);
                case "10" -> AdministracionProcesos.participacionEquitativa(procesos, sim);
                default -> throw new AssertionError();
            }

            menu();
            System.out.print("Opcion: ");
            opcion = Keyboard.readString();

            while (!opcion.equalsIgnoreCase("F")) {

                if (opcion.matches("\\d+") && (Integer.parseInt(opcion) >= 1 && Integer.parseInt(opcion) <= 10)) {
                    break;
                } else {
                    System.out.println("Elija una opcion valida: ");
                    opcion = Keyboard.readString();
                }
            }
        }
    }

    public static void parametrosAlgoritmos() {
        algoritmos.put("1", new Pair<String, String>("Round-Robin", "Apropiativa"));
        algoritmos.put("2", new Pair<String, String>("Round-Robin", "No apropiativa"));
        algoritmos.put("3", new Pair<String, String>("Prioridades", "Apropiativa"));
        algoritmos.put("4", new Pair<String, String>("Prioridades", "No apropiativa"));
        algoritmos.put("5", new Pair<String, String>("Múltiples Colas de Prioridad", "No apropiativa"));
        algoritmos.put("6", new Pair<String, String>("Proceso Más Corto Primero", "Apropiativa"));
        algoritmos.put("7", new Pair<String, String>("Planificación Garantizada", "Apropiativa"));
        algoritmos.put("8", new Pair<String, String>("Boletos de Lotería", "Apropiativa"));
        algoritmos.put("9", new Pair<String, String>("Boletos de Lotería", "No apropiativa"));
        algoritmos.put("10", new Pair<String, String>("Participación Equitativa", "Apropiativa"));
    }

    public static void menu() {
        System.out.println("""
                            ┌────────────────────────────────────────┐
                            │               Algoritmos               │
                            ├────────────────────────────────────────┤
                            │ 1. Round Robin [Apropiativo]           │
                            ├────────────────────────────────────────┤
                            │ 2. Round Robin [No-Apropiativo]        │
                            ├────────────────────────────────────────┤
                            │ 3. Prioridades [Apropiativo]           │
                            ├────────────────────────────────────────┤
                            │ 4. Prioridades [No-Apropiativo]        │
                            ├────────────────────────────────────────┤
                            │ 5. Múltiples Colas de Prioridad        │
                            ├────────────────────────────────────────┤
                            │ 6. Proceso Más Corto Primero           │
                            ├────────────────────────────────────────┤
                            │ 7. Planificación Garantizada           │
                            ├────────────────────────────────────────┤
                            │ 8. Boletos de Lotería [Apropiativo]    │
                            ├────────────────────────────────────────┤
                            │ 9. Boletos de Lotería [No-Apropiativo] │
                            ├────────────────────────────────────────┤
                            │ 10. Participación Equitativa           │
                            ├────────────────────────────────────────┤
                            │ F. Finalizar                           │
                            └────────────────────────────────────────┘
                        """);
    }

    public static void pcb(Collection<Proceso> procesos) {
        Field[] campos = Proceso.class.getDeclaredFields();
        ArrayList<Field> propiedades = new ArrayList<>();
        ArrayList<String> nombrePropiedades = new ArrayList<>();

        for(Field c : campos) {
            c.setAccessible(true);

            try {
                if (c.get(procesos.toArray()[0]) != null) {
                    propiedades.add(c);
                    String nombre = modificarCadena(c.getName());
                    nombrePropiedades.add((nombre.equals("BOLETOS") || nombre.equals("PETICIONES") ? String.format("%-30s", nombre) : nombre));
                }
            } catch (Exception e) {
                System.out.println("""
                                ╔═══════════════════════════════════╗
                                ║ Todos los procesos han terminado. ║
                                ╚═══════════════════════════════════╝
                                  """);
                return;
            }
        }

        // Encabezado.
        System.out.print("╔");
        for(int i = 0; i < propiedades.size(); i++) {
            int t = nombrePropiedades.get(i).length();
            System.out.printf("%s" + (i == propiedades.size() - 1 ? "╗%n" : "╦"), "═".repeat(t + 7));
        }

        // Imprimir el nombre de las propiedades.
        for(String pr : nombrePropiedades) {
            int t = pr.length() + 5;
            System.out.printf("║ %-" + t +"s ", pr);
        }
        System.out.printf("║%n");

        // Cerrar encabezado.
        System.out.print("╠");

        for(int i = 0; i < propiedades.size(); i++) {
            int t = nombrePropiedades.get(i).length();
            System.out.printf("%s" + (i == propiedades.size() - 1 ? "╣%n" : "╬"), "═".repeat(t + 7));
        }

        // Imprimir propiedades de procesos.
        for (int j = 0; j < procesos.size(); j++) {
            Proceso p = (Proceso) procesos.toArray()[j];

            for(int i = 0; i < propiedades.size(); i++) {
                int t = nombrePropiedades.get(i).length() + 5;
                try {
                    System.out.printf("║ %-" + t +"s ", propiedades.get(i).get(p));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            System.out.printf("║%n" + (j == procesos.size() - 1 ? "╚" : "╠"));
            // Cerrar fila de proceso.
            for(int i = 0; i < propiedades.size(); i++) {
                int t = nombrePropiedades.get(i).length();
                System.out.printf("%s" + (i == propiedades.size() - 1 ? (j == procesos.size() - 1 ? "╝%n" : "╣%n") : (j == procesos.size() - 1 ? "╩" : "╬")), "═".repeat(t + 7));
            }
        }
    }

    public static void generarProcesos(Collection<Proceso> procesos) {
        int numProcesos = rd.nextInt(10) + 1;
        for(int i = 0; i < numProcesos; i++) {
            Proceso p = new Proceso(procesos.size() + 1, rd.nextInt(8) + 3, (rd.nextInt(2) == 1 ? "Listo" : "Bloqueado"));

            int numPeticiones = (p.getEstado().equals("Bloqueado") ? rd.nextInt(5) + 1 : 0);
            Planificador.peticiones[p.getId()] = numPeticiones;

            ArrayList<Peticion> peticiones = new ArrayList<>();

            while (numPeticiones-- > 0) {
                int sector = rd.nextInt(20) + 1;
                char tipo = rd.nextInt(2) == 1 ? 'L' : 'E';
                int costo = (tipo == 'L' ? 1 : 2);

                peticiones.add(new Peticion(sector, tipo, costo));
            }

            p.setPeticiones(peticiones);
            procesos.add(p);
        }
    }

    public static String modificarCadena(String campo) {
        return campo.replaceAll("([a-z])([A-Z])", "$1 $2").toUpperCase();
    }

    public static void informe(Collection<Proceso> procesos, ArrayList<Integer> terminadosLista, Stack<Proceso> pila) {
        String terminados = "";
        String noEjecutados = "";
        String enEjecucion = "";

        for (Proceso p : procesos) {
            if (!pila.contains(p)) noEjecutados += p.getId() + ", ";
            if (pila.contains(p)) enEjecucion += p.getId() + ", ";
        }
        
        for (Integer id : terminadosLista) terminados += id + ", ";

        System.out.printf("%nInforme final:" +
                           "%n  • Procesos terminados: %s" +
                           "%n  • Procesos que nunca se ejecutaron: %s" +
                           "%n  • Procesos en ejecución: %s" + 
                           "%n  • Cambios de proceso registrados: %s%n%n", (terminados.isEmpty() ? "Ninguno" : terminados.substring(0, terminados.length() - 2)), (noEjecutados.isEmpty() ? "Ninguno" : noEjecutados.substring(0, noEjecutados.length() - 2)), (enEjecucion.isEmpty() ? "Ninguno" : enEjecucion.substring(0, enEjecucion.length() - 2)), pila.size());
    }

    public static void generarPeticiones(Proceso p, Collection<Peticion> peticiones) {
        int maxPeticiones = Math.min(4, (10 - Planificador.peticiones[p.getId()]));

        //System.out.println("ENTRO METODO GENERARPETICIONES");

        if (maxPeticiones == 0) return;

        int numPeticiones = rd.nextInt(maxPeticiones);
        Planificador.peticiones[p.getId()] += numPeticiones;
        //System.out.println("Planificador.peticiones[p.getId()]: " + Planificador.peticiones[p.getId()]);

        while (numPeticiones-- > 0) {
            //System.out.println("ENTRO");
            int sector = rd.nextInt(20) + 1;
            char tipo = rd.nextInt(2) == 1 ? 'L' : 'E';
            int costo = (tipo == 'L' ? 1 : 2);

            peticiones.add(new Peticion(sector, tipo, costo));
        }
    }

    public static int asignarCPU(int simulacion, int quantum, Proceso p) {
        if (p.getEstado().equals("Listo")) {
            if (rd.nextInt(2) == 0) generarPeticiones(p, p.getPeticiones());
            return Math.min(Math.min(simulacion, quantum), p.getTiempoRestante());
        }

        p.setEstado("Listo");
        return Math.min(Math.min(simulacion, quantum), p.getTiempoRestante());
    }

    /*public static int asignarCPU(int simulacion, int quantum, Proceso p) {
        if (p.getEstado().equals("Listo")) {
            if (rd.nextInt(2) == 1) {
                return Math.min(Math.min(simulacion, quantum), p.getTiempoRestante());
            }
            else {
                p.setEstado("Bloqueado");
                return rd.nextInt(Math.min(Math.min(simulacion, quantum), p.getTiempoRestante())) + 1;
            }
        }
        
        if (rd.nextInt(2) == 1) 
            if (rd.nextInt(2) == 1) {
                p.setEstado("Listo");
                return Math.min(Math.min(simulacion, quantum), p.getTiempoRestante());
            } else 
                return rd.nextInt(Math.min(Math.min(simulacion, quantum), p.getTiempoRestante())) + 1;

        return 0;
    }*/
    
    public static int asignarCPUNoApropiativo(int sim, Proceso p) {
        if (p.getEstado().equals("Listo")) {
            if (rd.nextInt(2) == 0) {
                int exe = Math.min(sim, p.getTiempoRestante());
                p.setTiempoRestante(p.getTiempoRestante() - exe);
                return exe;
            } else {
                p.setEstado("Bloqueado");
                int exe = rd.nextInt(Math.min(sim, p.getTiempoRestante())) + 1;
                p.setTiempoRestante(p.getTiempoRestante() - exe);
                return exe;
            }
        }
    
        if (p.getEstado().equals("Bloqueado")) {
            for (int i = 0; i < 3; i++) {
                if (rd.nextInt(2) == 0) { 
                    p.setEstado("Listo");
                    if (rd.nextInt(2) == 0) {
                        int exe = Math.min(sim, p.getTiempoRestante());
                        p.setTiempoRestante(p.getTiempoRestante() - exe);
                        return exe;
                    } else {
                        p.setEstado("Bloqueado");
                        int exe = rd.nextInt(Math.min(sim, p.getTiempoRestante())) + 1;
                        p.setTiempoRestante(p.getTiempoRestante() - exe);
                        return exe;
                    }
                }
            }
            return sim;
        }
        return 0;
    }
}