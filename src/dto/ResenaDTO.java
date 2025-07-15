package dto;

import java.sql.Timestamp;

public class ResenaDTO {
    private int id;
    private int productoId;
    private int usuarioId;
    private String usuarioNombre;
    private String productoNombre;
    private int rating;
    private String comentario;
    private Timestamp fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
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

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(productoId).append(' ');
        if(productoNombre != null) sb.append(productoNombre).append(' ');
        sb.append(usuarioId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        sb.append(rating).append(' ');
        if(comentario != null) sb.append(comentario).append(' ');
        if(fecha != null) sb.append(fecha).append(' ');
        return sb.toString().trim();
    }
}
