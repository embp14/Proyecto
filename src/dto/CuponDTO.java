package dto;

import java.sql.Timestamp;

public class CuponDTO {
    private int id;
    private String codigo;
    private int descuentoPct;
    private Timestamp fechaExpiracion;
    private int usoMaximo;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getDescuentoPct() { return descuentoPct; }
    public void setDescuentoPct(int descuentoPct) { this.descuentoPct = descuentoPct; }

    public Timestamp getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Timestamp fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public int getUsoMaximo() { return usoMaximo; }
    public void setUsoMaximo(int usoMaximo) { this.usoMaximo = usoMaximo; }

    @Override
    public String toString() {
        String code = codigo != null ? codigo : "";
        return code + " - ID " + id;
    }
}
