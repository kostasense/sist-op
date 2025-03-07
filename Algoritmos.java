import java.util.*;

public class Algoritmos {
    static Random rd = new Random();

    public static void loteriaApropiativa(ArrayList<Proceso> procesos, int simulacion, int quantum) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();


        int i = 0;

        for(Proceso p : procesos) {
            
        }

        while (simulacion > 0 && !procesos.isEmpty()) {

        }
    }

    /*public static int[] generarLoteria() {
        int[] loteria = new int[20];
    }*/

    public static void planificacionGarantizada(ArrayList<Proceso> procesos, int simulacion) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();

        int i = 0;
        int quantum = simulacion / procesos.size();

        System.out.printf("  %s%n", "• Quantum: " + quantum);
        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {
            Proceso p = procesos.get(i);
            int tiempo = Planificador.asignarCPU(simulacion,  quantum, p);
                
            simulacion -= tiempo;
            p.setTiempoRestante((p.getTiempoRestante() - tiempo));

            try {
                if (tiempo != 0 && pila.peek().getIdProceso() != p.getIdProceso()) 
                    pila.push(p);
            } catch (Exception e) {
                pila.push(p);
            } 
    
            if (p.getTiempoRestante() == 0) {
                p.setEstado("Terminado");
                terminados.add(p.getIdProceso());
                procesos.remove(i);
            } else {
                i++;
            }

            System.out.printf("%n%n • Proceso %d: Ejecuta %d unidades. %n • Estado: %s. %n • Simulación restante: %d unidades. %n", p.getIdProceso(), tiempo, p.getEstado(), simulacion);
            Planificador.pcb(procesos);  

            if (i == procesos.size()) 
                i = 0;
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void participacionEquitativa(ArrayList<Proceso> procesos, int simulacion) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();
        HashSet<Integer> activos = new HashSet<>();

        int [] procesosPorUsuario = new int[5];

        for(Proceso p : procesos) {
            int u = rd.nextInt(4) + 1;
            p.setUsuario(u);
            activos.add(u);
            procesosPorUsuario[u]++;
        }

        int tiempoAsignado = simulacion / activos.size();
        int i = 0;

        System.out.printf("  %s%n", "• Tiempo asignado a cada usuario: " + tiempoAsignado);
        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {
            Proceso p = procesos.get(i);

            int quantum = Math.max(tiempoAsignado / procesosPorUsuario[p.getUsuario()], 1);
            int tiempo = Planificador.asignarCPU(simulacion, quantum, p);

            simulacion -= tiempo;
            p.setTiempoRestante((p.getTiempoRestante() - tiempo));
            
            try {
                if (tiempo != 0 && pila.peek().getIdProceso() != p.getIdProceso()) 
                    pila.push(p);
            } catch (Exception e) {
                pila.push(p);
            }  

            if (p.getTiempoRestante() == 0) {
                p.setEstado("Terminado");
                terminados.add(p.getIdProceso());
                procesos.remove(i);
                procesosPorUsuario[p.getUsuario()]--;

                if (procesosPorUsuario[p.getUsuario()] == 0 && activos.size() == 1) {
                    activos.remove(Integer.valueOf(p.getUsuario()));
                    tiempoAsignado = Math.max(simulacion / activos.size(), 1);
                }
            } else {
                i++;
            }

            System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", p.getIdProceso(), 
            (tiempo == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", tiempo)), p.getEstado(), simulacion);
            Planificador.pcb(procesos); 

            if (i == procesos.size()) 
                i = 0;
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void roundRobinA(ArrayList<Proceso> procesos, int sim, int quantum) {

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();

        for (int cont = 0; cont < procesos.size(); cont++) {

            if (sim <= 0 || terminados.size() == procesos.size()) {
                break;
            }

            Proceso p = procesos.get(cont);

            if (!p.getEstado().equals("Terminado")) {

                int exe = Planificador.asignarCPU(sim, quantum, p);

                sim -= exe;
                p.setTiempoRestante(p.getTiempoRestante() - exe);

                try {
                    if (exe != 0 && pila.peek().getIdProceso() != p.getIdProceso()) 
                        pila.push(p);
                } catch (Exception e) {
                    pila.push(p);
                }

                if (p.getTiempoRestante() <= 0) {
                    p.setEstado("Terminado");
                    terminados.add(p.getIdProceso());
                }

                System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                  p.getIdProceso(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim);
                Planificador.pcb(procesos);
            }

            if (cont == (procesos.size() - 1)) {
                cont = -1;
            }
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void roundRobinNA(ArrayList<Proceso> procesos, int sim) {

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();

        for (int cont = 0; cont < procesos.size(); cont++) {

            if (sim <= 0 || terminados.size() == procesos.size()) {
                break;
            }

            Proceso p = procesos.get(cont);

            if (!p.getEstado().equals("Terminado")) {

                int tiempoRestante = p.getTiempoRestante();

                int exe = Planificador.asignarCPUNoApropiativo(sim, p);

                if (p.getTiempoRestante() != tiempoRestante || exe == 0) {

                    sim -= exe;

                    try {
                        if (exe != 0 && pila.peek().getIdProceso() != p.getIdProceso()) 
                            pila.push(p);
                    } catch (Exception e) {
                        pila.push(p);
                    }

                    if (p.getTiempoRestante() <= 0) {
                        p.setEstado("Terminado");
                        terminados.add(p.getIdProceso());
                    } else {
                        cont = cont - 1;
                    }

                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getIdProceso(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim);                      
                    Planificador.pcb(procesos);
                }

                if (sim == exe && (p.getTiempoRestante() == tiempoRestante)) {
                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getIdProceso(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim);                    
                    Planificador.pcb(procesos);
                    System.out.println("MUERTE POR INANICION");
                    Planificador.informe(procesos, terminados, pila);
                    return;
                }
            }
        }

        Planificador.informe(procesos, terminados, pila);
    }
}