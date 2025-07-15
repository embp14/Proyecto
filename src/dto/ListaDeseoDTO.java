package dto;

import java.sql.Timestamp;

public class ListaDeseoDTO {
    private int id;
    private int usuarioId;
    private String usuarioNombre;
    private String nombre;
    private Timestamp creadoEn;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Timestamp getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Timestamp creadoEn) { this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(usuarioId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        if(nombre != null) sb.append(nombre).append(' ');
        if(creadoEn != null) sb.append(creadoEn).append(' ');
        return sb.toString().trim();
    }
}
