import java.util.*;

public class RoundRobbinA {

    private static Random rd = new Random();

    public static void main(ArrayList<Proceso> procesos, int sim, int quantum) {

        System.out.println("Tabla inicial de procesos");
        printPcb(procesos);

        Stack<Proceso> pila = new Stack<>();
        int terminados = 0;

        for (int cont = 0; cont < procesos.size(); cont++) {

            if (sim <= 0 || terminados == procesos.size()) {
                break;
            }

            Proceso p = procesos.get(cont);

            if (!p.getEstado().equals("Terminado")) {

                int exe = asignarCPU(sim, quantum, p);

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
                printPcb(procesos);
                System.out.printf(" • Proceso %d: Ejecuta %d unidades. %n • Estado: %s%n%n", p.getIdProceso(), exe, p.getEstado());
            }

            if (cont == (procesos.size() - 1)) {
                cont = -1;
            }
        }

        printInforme(procesos, pila);
    }

    public static void printPcb(ArrayList<Proceso> procesos) {

        Planificador.pcb(procesos);
    }

    public static void printInforme(ArrayList<Proceso> procesos, Stack<Proceso> pila) {

        Planificador.informe(procesos, pila);
    }

    public static int asignarCPU(int simulacion, int quantum, Proceso p) {

        if (p.getEstado().equals("Listo")) {
            if (rd.nextInt(2) == 1) {
                return Math.min(Math.min(simulacion, quantum), p.getTiempoRestante());
            }
            else {
                p.setEstado("Bloqueado");
                return rd.nextInt(Math.min(Math.min(simulacion, quantum), p.getTiempoRestante()));
            }
        }

        
        if (rd.nextInt(2) == 1) 
            if (rd.nextInt(2) == 1) {
                p.setEstado("Listo");
                return Math.min(Math.min(simulacion, quantum), p.getTiempoRestante());
            } else 
                return rd.nextInt(Math.min(Math.min(simulacion, quantum), p.getTiempoRestante()));

        return 0;
        
    }
}