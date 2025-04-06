import java.util.*;

public class Proceso {
    private Integer idProceso;
    private Integer tiempoRestante;
    private String estado;
    private Integer prioridad;
    private ArrayList<Integer> boletos;
    private Integer usuario;
    private ArrayList<Peticion> peticiones;

    public Proceso(Integer idProceso, Integer tiempoRestante, String estado) {
        this.idProceso = idProceso;
        this.tiempoRestante = tiempoRestante;
        this.estado = estado;
    }

    public Integer getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(Integer tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return idProceso;
    }

    public void setId(Integer idProceso) {
        this.idProceso = idProceso;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Integer> getBoletos() {
        return boletos;
    }

    public void setBoleto(ArrayList<Integer> boletos) {
        this.boletos = boletos;
    }

    public String toString() {
        return "Proceso{" +
                "idProceso=" + idProceso +
                ", tiempoRestante=" + tiempoRestante +
                ", estado='" + estado + '\'' +
                ", prioridad=" + prioridad +
                ", boleto=" + (boletos != null ? boletos.toString() : boletos) +
                ", usuario=" + usuario +
                '}';
    }

    public ArrayList<Peticion> getPeticiones() {
        return peticiones;
    }

    public void setPeticiones(ArrayList<Peticion> peticiones) {
        this.peticiones = peticiones;
    }
}
