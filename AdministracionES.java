import java.util.*;

public class AdministracionES {
    public static void fscan(Proceso p, int cabeza) {
        LinkedList<Peticion> p1 = new LinkedList<>(p.getPeticiones());
        LinkedList<Peticion> p2 = new LinkedList<>();

        int sectorActual = 0, distancia = 0, rendimiento = 0, direccion = 0;

        while (!p1.isEmpty() && !p2.isEmpty()) {

            while (!p1.isEmpty()) {
                if (direccion == 0) Collections.sort(p1);
                else p1.sort(Collections.reverseOrder());

                direccion = Math.abs(cabeza-sectorActual);
            }
        }
    }
}
