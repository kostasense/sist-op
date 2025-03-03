import java.util.*;

public class Algoritmos {
    static Random rd = new Random();

    public static void participacionEquitativa(ArrayList<Proceso> procesos, int simulacion) {
        
        Stack<Proceso> pila = new Stack<>(); 
        int numUsuarios = rd.nextInt(Math.min(5, procesos.size())) + 1; // Ehhhhhh todavia no hace lo que quiero.

        for(Proceso p : procesos)
            p.setUsuario(rd.nextInt(numUsuarios) + 1);

        int quantum = simulacion / numUsuarios, i = 0, terminados = 0;

        System.out.printf("  • Quantum: %d%n", quantum);
        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && terminados < procesos.size()) {
            Proceso p = procesos.get(i++);

            if (!p.getEstado().equals("Terminado")) {
                int tiempo = Planificador.asignarCPU(simulacion, quantum, p);
                //System.out.println("Quantum: " + quantum);
                
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
                    terminados++;
                }
                
                System.out.println("\nSimulación restante: " + simulacion);
                Planificador.pcb(procesos);
                System.out.printf(" • Proceso %d: Ejecuta %d unidades. %n • Estado: %s%n%n", p.getIdProceso(), tiempo, p.getEstado());
                //System.out.println("Simulación restante: " + simulacion);
            }

            if (i == procesos.size()) 
                i = 0;
        }

        Planificador.informe(procesos, pila);
    }

    public static void roundRobin(ArrayList<Proceso> procesos, int sim, int quantum) {

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        Stack<Proceso> pila = new Stack<>();
        int terminados = 0;

        for (int cont = 0; cont < procesos.size(); cont++) {

            if (sim <= 0 || terminados == procesos.size()) {
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
                    terminados++;
                }

                System.out.println("\nSimulación restante: " + sim);
                Planificador.pcb(procesos);
                System.out.printf(" • Proceso %d: Ejecuta %d unidades. %n • Estado: %s%n%n", p.getIdProceso(), exe, p.getEstado());
            }

            if (cont == (procesos.size() - 1)) {
                cont = -1;
            }
        }

        Planificador.informe(procesos, pila);
    }
}
