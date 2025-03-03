public class Proceso {
    // Agregar datos requeridos por el algoritmo.
    private Integer idProceso;
    private Integer tiempoRestante;
    private String estado;
    private Integer prioridad;
    private Integer boleto;
    private Integer usuario;

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

    public Integer getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Integer idProceso) {
        this.idProceso = idProceso;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getBoleto() {
        return boleto;
    }

    public void setBoleto(Integer boleto) {
        this.boleto = boleto;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String toString() {
        return "Proceso{" +
                "idProceso=" + idProceso +
                ", tiempoRestante=" + tiempoRestante +
                ", estado='" + estado + '\'' +
                ", prioridad=" + prioridad +
                ", boleto=" + boleto +
                ", usuario=" + usuario +
                '}';
    }
}
