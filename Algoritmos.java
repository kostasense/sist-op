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

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {
            Proceso p = procesos.get(i);

            //if (!p.getEstado().equals("Terminado")) {
            int quantum = Math.max(simulacion / procesos.size(), 1);
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
                
                System.out.printf("%n%n • Proceso %d: Ejecuta %d unidades. %n • Estado: %s. %n • Simulación restante: %d unidades. %n • Quantum: %d%n", p.getIdProceso(), tiempo, p.getEstado(), simulacion, quantum);
                Planificador.pcb(procesos);   
            //}

            if (i == procesos.size()) 
                i = 0;
        }

        Planificador.informe(procesos, terminados, pila);
    }

    /*public static void participacionEquitativa(ArrayList<Proceso> procesos, int simulacion) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();
        int [] procesosPorUsuario = new int[5];

        int numUsuarios = Math.min(procesos.size(), 4);

        for(Proceso p : procesos) {
            int usuario = rd.nextInt(numUsuarios) + 1;
            p.setUsuario(usuario);
            procesosPorUsuario[usuario]++;
        }

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        //int porUsuario = simulacion / activos.size();
        int i = 0;

        while (simulacion > 0 && !procesos.isEmpty()) {
            Proceso p = procesos.get(i);

            //int quantum = porUsuario / procesosPorUsuario[p.getUsuario()];
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

                /*if (procesosPorUsuario[p.getUsuario()] == 0) {
                    activos.remove(Integer.valueOf(p.getUsuario()));
                    porUsuario = simulacion / activos.size();
                }
            } else {
                i++;
            }

            System.out.printf("%n%n • Proceso %d: Ejecuta %d unidades. %n • Estado: %s. %n • Simulación restante: %d unidades. %n • Quantum: %d%n", p.getIdProceso(), tiempo, p.getEstado(), simulacion, quantum);
            Planificador.pcb(procesos);   

            if (i == procesos.size()) 
                i = 0;
        }

        Planificador.informe(procesos, terminados, pila);
    }*/

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

                System.out.printf("%n%n • Proceso %d: Ejecuta %d unidades. %n • Estado: %s. %n • Simulación restante: %d%n", p.getIdProceso(), exe, p.getEstado(), sim);
                Planificador.pcb(procesos);
            }

            if (cont == (procesos.size() - 1)) {
                cont = -1;
            }
        }

        //Planificador.informe(procesos, pila);
    }
}
