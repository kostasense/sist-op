public class Peticion {
    private int sector;
    private char tipo;
    private int costo;
    
    public Peticion(int sector, char tipo, int costo) {
        this.sector = sector;
        this.tipo = tipo;
        this.costo = costo;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return sector + "" + tipo;
    }
}
