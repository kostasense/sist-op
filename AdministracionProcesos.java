import java.util.*;

public class AdministracionProcesos {
    static Random rd = new Random();

    public static void loteriaNoApropiativa(ArrayList<Proceso> procesos, int simulacion) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();
        HashMap<Integer, Proceso> boletoPorProceso = new HashMap<>();
        int [] boletosPorPrioridad = {0, 8, 6, 4, 2};
        int [] loteria = new int[101];

        int boletoActual = 1;

        for(Proceso p : procesos) {
            int prioridad = rd.nextInt(4) + 1;
            p.setPrioridad(prioridad);

            ArrayList<Integer> boletosProceso = new ArrayList<>();
            for(int i = 0; i < boletosPorPrioridad[prioridad]; i++) {
                boletosProceso.add(boletoActual);
                boletoPorProceso.put(boletoActual, p);
                boletoActual++;
            }
            p.setBoleto(boletosProceso);
        }

        for(int i = 0; i < loteria.length; i++) {
            loteria[i] = i + 1;
        }

        for(int i = loteria.length - 1; i > 0; i--) {
            int j = rd.nextInt(i + 1);
            int temp = loteria[i];
            loteria[i] = loteria[j];
            loteria[j] = temp;
        }

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {
            int boletoGanador = loteria[rd.nextInt(loteria.length)];

            while (!boletoPorProceso.containsKey(boletoGanador))
                boletoGanador = loteria[rd.nextInt(loteria.length)];

            Proceso p = boletoPorProceso.get(boletoGanador);
            int tiempoRestante = 0, tiempoEjecucion = 0;

            //do {
                System.out.printf("%n%n • Boleto ganador: [%d]%n • Proceso %d entra al procesador.", boletoGanador, p.getId());

                tiempoRestante = p.getTiempoRestante();
                tiempoEjecucion = Planificador.asignarCPUNoApropiativo(simulacion, p);   
                p.setTiempoRestante((p.getTiempoRestante() - tiempoEjecucion));  
                AdministracionES.fscan(p, 0);

                /*if (p.getTiempoRestante() == tiempoRestante && simulacion == tiempoEjecucion) {
                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", p.getId(), 
                                     "No se ejecuta.", p.getEstado(), simulacion);
                    System.out.println("""
                                        ╔═══════════════════════════════════╗
                                        ║ MUERTE POR INANICION.             ║
                                        ╚═══════════════════════════════════╝
                                       """);

                    Planificador.informe(procesos, terminados, pila);
                    return;
                }*/

                simulacion -= tiempoEjecucion;

                if (p.getTiempoRestante() == 0) {
                    p.setEstado("Terminado");
                    terminados.add(p.getId());
                    procesos.remove(p);
                }

                System.out.printf("%n • Unidades ejecutadas: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                (tiempoEjecucion == 0 ? "No se ejecuta." : String.format("%d", tiempoEjecucion)), p.getEstado(), simulacion);
                Planificador.pcb(procesos);  

                try {
                    if (tiempoEjecucion != 0 && pila.peek().getId() != p.getId()) 
                        pila.push(p);
                } catch (Exception e) {
                    pila.push(p);
                } 

            //} while (p.getTiempoRestante() != 0 && simulacion > 0);

            Iterator<Map.Entry<Integer, Proceso>> iter = boletoPorProceso.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, Proceso> boleto = iter.next();
                if (boleto.getValue().getTiempoRestante() == 0) iter.remove();
            }
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void loteriaApropiativa(ArrayList<Proceso> procesos, int simulacion, int quantum) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();
        HashMap<Integer, Proceso> boletoPorProceso = new HashMap<>();
        int [] boletosPorPrioridad = {0, 8, 6, 4, 2};
        int [] loteria = new int[101];

        int boletoActual = 1;

        for(Proceso p : procesos) {
            int prioridad = rd.nextInt(4) + 1;
            p.setPrioridad(prioridad);

            ArrayList<Integer> boletosProceso = new ArrayList<>();
            for(int i = 0; i < boletosPorPrioridad[prioridad]; i++) {
                boletosProceso.add(boletoActual);
                boletoPorProceso.put(boletoActual, p);
                boletoActual++;
            }
            p.setBoleto(boletosProceso);
        }

        for(int i = 0; i < loteria.length; i++) {
            loteria[i] = i + 1;
        }

        for(int i = loteria.length - 1; i > 0; i--) {
            int j = rd.nextInt(i + 1);
            int temp = loteria[i];
            loteria[i] = loteria[j];
            loteria[j] = temp;
        }

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {
            int boletoGanador = loteria[rd.nextInt(loteria.length)];

            while (!boletoPorProceso.containsKey(boletoGanador))
                boletoGanador = loteria[rd.nextInt(loteria.length)];

            Proceso p = boletoPorProceso.get(boletoGanador);

            System.out.printf("%n%n • Boleto ganador: [%d]%n • Proceso %d entra al procesador.", boletoGanador, p.getId());

            int tiempo = Planificador.asignarCPU(simulacion,  quantum, p);
            simulacion -= tiempo;
            p.setTiempoRestante((p.getTiempoRestante() - tiempo));   
            AdministracionES.scanN(p, 0, 5);

            try {
                if (tiempo != 0 && pila.peek().getId() != p.getId()) 
                    pila.push(p);
            } catch (Exception e) {
                pila.push(p);
            } 
                
            if (p.getTiempoRestante() == 0) {
                p.setEstado("Terminado");
                terminados.add(p.getId());
                procesos.remove(p);

                Iterator<Map.Entry<Integer, Proceso>> iter = boletoPorProceso.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<Integer, Proceso> boleto = iter.next();
                    if (boleto.getValue().getTiempoRestante() == 0) iter.remove();
                }
            }
            
            System.out.printf("%n • Unidades ejecutadas: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                             (tiempo == 0 ? "No se ejecuta." : String.format("%d", tiempo)), p.getEstado(), simulacion);
            Planificador.pcb(procesos);  
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void planificacionGarantizada(ArrayList<Proceso> procesos, int simulacion) {
        Stack<Proceso> pila = new Stack<>(); 
        ArrayList<Integer> terminados = new ArrayList<>();

        int i = 0;
        int quantum = simulacion / procesos.size();

        System.out.printf("%n  • Tiempo asignado a cada proceso: %d unidades.%n", quantum);
        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {

            Proceso p = procesos.get(i);

            System.out.printf("%n%n • Proceso %d entra al procesador.", p.getId());

            int tiempo = Planificador.asignarCPU(simulacion,  quantum, p);
            simulacion -= tiempo;
            p.setTiempoRestante((p.getTiempoRestante() - tiempo));
            AdministracionES.scanN(p, 0, 3);

            try {
                if (tiempo != 0 && pila.peek().getId() != p.getId()) 
                    pila.push(p);
            } catch (Exception e) {
                pila.push(p);
            } 
    
            if (p.getTiempoRestante() == 0) {
                p.setEstado("Terminado");
                terminados.add(p.getId());
                procesos.remove(i);
            } else {
                i++;
            }

            System.out.printf("%n • Unidades ejecutadas: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                            (tiempo == 0 ? "No se ejecuta." : String.format("%d", tiempo)), p.getEstado(), simulacion);
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

        System.out.printf("%n  %s%n", "• Tiempo asignado a cada usuario: %d unidades.", tiempoAsignado);
        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        while (simulacion > 0 && !procesos.isEmpty()) {
            Proceso p = procesos.get(i);

            System.out.printf("%n%n • Proceso %d entra al procesador.", p.getId());

            int quantum = Math.max(tiempoAsignado / procesosPorUsuario[p.getUsuario()], 1);
            int tiempo = Planificador.asignarCPU(simulacion, quantum, p);

            simulacion -= tiempo;
            p.setTiempoRestante((p.getTiempoRestante() - tiempo));
            AdministracionES.fscan(p, 0);
            
            try {
                if (tiempo != 0 && pila.peek().getId() != p.getId()) 
                    pila.push(p);
            } catch (Exception e) {
                pila.push(p);
            }  

            if (p.getTiempoRestante() == 0) {
                p.setEstado("Terminado");
                terminados.add(p.getId());
                procesos.remove(i);
                procesosPorUsuario[p.getUsuario()]--;

                if (procesosPorUsuario[p.getUsuario()] == 0 && activos.size() == 1) {
                    activos.remove(Integer.valueOf(p.getUsuario()));
                    tiempoAsignado = Math.max(simulacion / activos.size(), 1);
                }
            } else {
                i++;
            }

            System.out.printf("%n • Unidades ejecutadas: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                            (tiempo == 0 ? "No se ejecuta." : String.format("%d", tiempo)), p.getEstado(), simulacion);
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

            if (sim <= 0 || procesos.isEmpty()) {
                break;
            }

            Proceso p = procesos.get(cont);

            if (!p.getEstado().equals("Terminado")) {

                int exe = Planificador.asignarCPU(sim, quantum, p);

                sim -= exe;
                p.setTiempoRestante(p.getTiempoRestante() - exe);

                try {
                    if (exe != 0 && pila.peek().getId() != p.getId()) 
                        pila.push(p);
                } catch (Exception e) {
                    pila.push(p);
                }

                if (p.getTiempoRestante() <= 0) {
                    p.setEstado("Terminado");
                    terminados.add(p.getId());
                }

                AdministracionES.cScan(p);

                System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                  p.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim);
                
                if (p.getEstado().equals("Terminado")) {
                    procesos.remove(cont);
                    cont = cont - 1;
                }

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

        int aleatorio = rd.nextInt(procesos.size()) + 1;
        System.out.println("Random generado: " + aleatorio);

        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();

        for (int cont = 0; cont < procesos.size(); cont++) {

            if (sim <= 0 || procesos.isEmpty()) {
                break;
            }

            Proceso p = procesos.get(cont);

            if (!p.getEstado().equals("Terminado")) {

                int tiempoRestante = p.getTiempoRestante();

                int exe = Planificador.asignarCPUNoApropiativo(sim, p);

                p.setTiempoRestante(p.getTiempoRestante() - exe);

                if (p.getTiempoRestante() != tiempoRestante) {

                    sim -= exe;

                    try {
                        if (exe != 0 && pila.peek().getId() != p.getId()) 
                            pila.push(p);
                    } catch (Exception e) {
                        pila.push(p);
                    }

                    if (p.getTiempoRestante() <= 0) {
                        p.setEstado("Terminado");
                        terminados.add(p.getId());
                    } else {
                        cont = cont - 1;
                    }

                    AdministracionES.cScan(p);

                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim); 
                                      
                    if (p.getEstado().equals("Terminado")) {
                        procesos.remove(cont);
                        cont = cont - 1;
                    }

                    Planificador.pcb(procesos);
                }

                if (sim == exe && (p.getTiempoRestante() == tiempoRestante)) {
                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getId(), "No se ejecuta.", p.getEstado(), sim);                    
                    Planificador.pcb(procesos);

                    System.out.println("""
                                ╔═══════════════════════════════════╗
                                ║ MUERTE POR INANICION.             ║
                                ╚═══════════════════════════════════╝
                                  """);
                    Planificador.informe(procesos, terminados, pila);
                    return;
                }

                if (p.getId() == aleatorio) {
                    Proceso pnuevo = new Proceso(20, rd.nextInt(8) + 3, (rd.nextInt(2) == 1 ? "Listo" : "Bloqueado"));
                    procesos.add(pnuevo);
                    System.out.println("Se crea nuevo proceso: " + pnuevo.getId());
                }
            }
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void multiplesColas(ArrayList<Proceso> procesos, int sim) {

        Map<Integer, Stack<Proceso>> pilas = new TreeMap<>();

        for (Proceso p: procesos) {
            p.setPrioridad(rd.nextInt(4) + 1);
            pilas.computeIfAbsent(p.getPrioridad(), k -> new Stack<>()).add(p);
        }

        procesos.sort(Comparator.comparing(Proceso::getPrioridad));

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);

        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();

        for (int prioridad : pilas.keySet()) {

            Stack<Proceso> stack = pilas.get(prioridad);
            List<Proceso> lista = new ArrayList<>(stack);

            Collections.reverse(lista);

            stack.clear();
            stack.addAll(lista);

            if (sim <= 0 || procesos.isEmpty()) {
                break;
            }

            while (!stack.isEmpty()) {

                if (sim <= 0 || procesos.isEmpty()) {
                    break;
                }

                Proceso p = stack.pop();

                int tiempoRestante = p.getTiempoRestante();

                int exe = Planificador.asignarCPUNoApropiativo(sim, p);

                p.setTiempoRestante(p.getTiempoRestante() - exe);
                
                if (p.getTiempoRestante() != tiempoRestante) {

                    sim -= exe;

                    try {
                        if (exe != 0 && pila.peek().getId() != p.getId()) 
                            pila.push(p);
                    } catch (Exception e) {
                        pila.push(p);
                    } 

                    if (p.getTiempoRestante() <= 0) {
                        p.setEstado("Terminado");
                        terminados.add(p.getId());
                    } else {
                        stack.push(p);
                    }

                    AdministracionES.cScan(p);

                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim); 
                                      
                    if (p.getEstado().equals("Terminado")) {
                        procesos.remove(p);
                    }

                    Planificador.pcb(procesos);
                }

                if (sim == exe && (p.getTiempoRestante() == tiempoRestante)) {
                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getId(), "No se ejecuta.", p.getEstado(), sim);                    
                    Planificador.pcb(procesos);

                    System.out.println("""
                                ╔═══════════════════════════════════╗
                                ║ MUERTE POR INANICION.             ║
                                ╚═══════════════════════════════════╝
                                  """);
                    Planificador.informe(procesos, terminados, pila);
                    return;
                }
            }
        }

        Planificador.informe(procesos, terminados, pila);
    }

    public static void prioridadA(ArrayList<Proceso> procesos, int sim, int quantum) {
        
        for (Proceso p : procesos) {
            int prioridad = rd.nextInt(4) + 1;
            p.setPrioridad(prioridad);
        }
        
        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);
        
        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();

        for (int i = 0; i < procesos.size() - 1; i++) {
            for (int j = i + 1; j < procesos.size(); j++) {
                if (procesos.get(i).getPrioridad() > procesos.get(j).getPrioridad()) {
                    Collections.swap(procesos, i, j);
                }
            }
        }

        int prioridadActual = procesos.get(0).getPrioridad();

        while(!procesos.isEmpty())
        {
            if (sim <= 0 || procesos.isEmpty()) {
                break;
            }

            ArrayList<Proceso> procesosMismaPrioridad = new ArrayList<>();

            for (Proceso p : procesos) {
                if (p.getPrioridad() == prioridadActual) {
                    procesosMismaPrioridad.add(p);
                }
            }

            for (int cont = 0; cont < procesosMismaPrioridad.size(); cont++) {

                if (sim <= 0 || procesosMismaPrioridad.isEmpty()) {
                    break;
                }
    
                Proceso p = procesosMismaPrioridad.get(cont);
                int TiempoRestante=p.getTiempoRestante();
                if (!p.getEstado().equals("Terminado")) {
                    int exe=0;
                    if (procesosMismaPrioridad.size() == 1) {
                        if (p.getEstado().equals("Bloqueado")) {
                            for (int cont2 = 0; cont2 < 3; cont2++) {
                                if (rd.nextInt(2) == 0) { 
                                    p.setEstado("Listo");
                                    if (rd.nextInt(2) == 0) {
                                            exe = Math.min(sim,Math.min(p.getTiempoRestante(),quantum));
                                        p.setTiempoRestante(p.getTiempoRestante() - exe);
                                    break;
                                    } else {
                                        p.setEstado("Bloqueado");
                                            exe = rd.nextInt(Math.min(sim,Math.min(p.getTiempoRestante(),quantum))) + 1;
                                        p.setTiempoRestante(p.getTiempoRestante() - exe);
                                      break;
                                    }
                                }
                            }
                            
                           if (p.getTiempoRestante()==TiempoRestante) {
                                System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                          p.getId(), "No se ejecuta.", p.getEstado(), sim);                    
                                Planificador.pcb(procesos);
                                System.out.println("""
                                    ╔═══════════════════════════════════╗
                                    ║ MUERTE POR INANICION.             ║
                                    ╚═══════════════════════════════════╝
                                        """);
                                Planificador.informe(procesos, terminados, pila);
                                return;
                           } else {
                                sim -= exe;

                                try {
                                    if (exe != 0 && pila.peek().getId() != p.getId()) 
                                        pila.push(p);
                                } catch (Exception e) {
                                    pila.push(p);
                                }

                                if (p.getTiempoRestante() <= 0) {
                                    p.setEstado("Terminado");
                                    terminados.add(p.getId()
                                    );
                                }

                                System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                p.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(),sim);
                                if (p.getEstado().equals("Terminado")) {
                                    procesos.remove(cont);
                                    procesosMismaPrioridad.remove(cont);
                                    cont = cont - 1;
                                }

                                Planificador.pcb(procesos);
                            }

                            break;
                        }
                    }

                    exe = Planificador.asignarCPU(sim, quantum, p);
    
                    sim -= exe;
                    p.setTiempoRestante(p.getTiempoRestante() - exe);
    
                    try {
                        if (exe != 0 && pila.peek().getId() != p.getId()) 
                            pila.push(p);
                    } catch (Exception e) {
                        pila.push(p);
                    }
    
                    if (p.getTiempoRestante() <= 0) {
                        p.setEstado("Terminado");
                        terminados.add(p.getId());
                    }
    
                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                                      p.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim);
                    
                    if (p.getEstado().equals("Terminado")) {
                        procesos.remove(cont);
                        procesosMismaPrioridad.remove(cont);
                        cont = cont - 1;
                    }
    
                    Planificador.pcb(procesos);
                }
    
                if (cont == (procesos.size() - 1)) {
                    cont = -1;
                }
            }

            if (!procesos.isEmpty()) {
                prioridadActual = procesos.get(0).getPrioridad();
            }

        }
        
        Planificador.informe(procesos, terminados, pila);
    }

    public static void prioridadNA(ArrayList<Proceso> procesos, int sim) {

        for (Proceso p : procesos) {
            int prioridad = rd.nextInt(4) + 1;
            p.setPrioridad(prioridad);
        }
    
        System.out.println("\nTabla inicial de procesos:");
    
        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();
    
        for (int i = 0; i < procesos.size() - 1; i++) {
            for (int j = i + 1; j < procesos.size(); j++) {
                if (procesos.get(i).getPrioridad() > procesos.get(j).getPrioridad()) {
                    Collections.swap(procesos, i, j);
                }
            }
        }
    
        Planificador.pcb(procesos);
    
        int prioridadActual = procesos.get(0).getPrioridad();
    
        while (sim > 0 && !procesos.isEmpty()) {
            ArrayList<Proceso> procesosMismaPrioridad = new ArrayList<>();
    
            for (Proceso p : procesos) {
                if (p.getPrioridad() == prioridadActual) {
                    procesosMismaPrioridad.add(p);
                }
            }
    
            for (int cont = 0; cont < procesosMismaPrioridad.size(); cont++) {
    
                Proceso p = procesosMismaPrioridad.get(cont);
    
                int tiempoRestante = p.getTiempoRestante();
                int exe = Planificador.asignarCPUNoApropiativo(sim, p);
                p.setTiempoRestante(tiempoRestante - exe);

                /*if (exe == 0 && (p.getTiempoRestante() == tiempoRestante)) {
                    System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n",
                                    p.getId(), "No se ejecuta.", p.getEstado(), sim);
                    Planificador.pcb(procesos);

                    System.out.println("""
                            ╔═══════════════════════════════════╗
                            ║ MUERTE POR INANICION.             ║
                            ╚═══════════════════════════════════╝
                            """);
                    Planificador.informe(procesos, terminados, pila);
                    return;
                }*/

                sim -= exe;

                try {
                    if (exe != 0 && pila.peek().getId() != p.getId()) 
                        pila.push(p);
                } catch (Exception e) {
                    pila.push(p);
                } 

                //if (p.getTiempoRestante() <= 0) {
                    p.setEstado("Terminado");
                    terminados.add(p.getId());
                    procesos.remove(p);
                //} else {
                    //cont--; 
                //}

                System.out.printf("%n%n • Proceso %d: %s%n • Estado: %s. %n • Simulación restante: %d unidades. %n",
                        p.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), p.getEstado(), sim);

                Planificador.pcb(procesos);
            }
    
            if (!procesos.isEmpty()) {
                prioridadActual = procesos.get(0).getPrioridad();
            }
        }
    
        Planificador.informe(procesos, terminados, pila);
    }
    

    public static void masCortoPrimero(ArrayList<Proceso> procesos, int sim, int quantum) {

        System.out.println("\nTabla inicial de procesos:");
        Planificador.pcb(procesos);
    
        Stack<Proceso> pila = new Stack<>();
        ArrayList<Integer> terminados = new ArrayList<>();
    
        while (sim > 0 && !procesos.isEmpty()) {
            Proceso menor = null;
            
            for (Proceso p : procesos) {
                if (!p.getEstado().equals("Terminado") && (menor == null || p.getTiempoRestante() < menor.getTiempoRestante())) {
                    menor = p;
                }
            }
            
            if (menor == null) break; 
    
            int exe = Planificador.asignarCPU(sim, quantum, menor);
            sim -= exe;
            menor.setTiempoRestante(menor.getTiempoRestante() - exe);
            
            try {
                if (exe != 0 && pila.peek().getId() != menor.getId()) 
                    pila.push(menor);
            } catch (Exception e) {
                pila.push(menor);
            }
    
            if (menor.getTiempoRestante() <= 0) {
                menor.setEstado("Terminado");
                terminados.add(menor.getId());
                procesos.remove(menor);
            }
    
            System.out.printf("%n%n • Proceso %d: %s %n • Estado: %s. %n • Simulación restante: %d unidades. %n", 
                              menor.getId(), (exe == 0 ? "No se ejecuta." : String.format("Ejecuta %d unidades.", exe)), menor.getEstado(), sim);
            
            Planificador.pcb(procesos);
        }
    
        Planificador.informe(procesos, terminados, pila);
    }
}