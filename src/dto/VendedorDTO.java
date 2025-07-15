package dto;

import java.math.BigDecimal;

public class VendedorDTO {
    private int id;
    private int usuarioId;
    private String usuarioNombre;
    private String nombreTienda;
    private String descripcion;
    private BigDecimal calificacionPromedio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(BigDecimal calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(usuarioId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        if(nombreTienda != null) sb.append(nombreTienda).append(' ');
        if(descripcion != null) sb.append(descripcion).append(' ');
        if(calificacionPromedio != null) sb.append(calificacionPromedio).append(' ');
        return sb.toString().trim();
    }
}
