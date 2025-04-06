import java.util.*;

public class AdministracionES {
    public static void fscan(Proceso p, int cabeza) {
        LinkedList<Peticion> p1 = new LinkedList<>(p.getPeticiones());
        LinkedList<Peticion> p2 = new LinkedList<>();

        int sectorActual = 0, distancia = 0, rendimiento = 0, direccion = 1;

        /*while (!p1.isEmpty() && !p2.isEmpty()) {
            p1.sort((a, b)-> {
                return direccion == 1
                    ? Integer.compare(a.getSector(), b.getSector())
                    : Integer.compare(b.getSector(), a.getSector());
            });
        }*/
    }
}
