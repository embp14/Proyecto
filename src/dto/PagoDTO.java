package dto;

import java.sql.Timestamp;

public class PagoDTO {
    private int id;
    private int ordenId;
    private String usuarioNombre;
    private String metodoPago;
    private double monto;
    private Timestamp fechaPago;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(Timestamp fechaPago) { this.fechaPago = fechaPago; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(ordenId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        if(metodoPago != null) sb.append(metodoPago).append(' ');
        sb.append(monto).append(' ');
        if(fechaPago != null) sb.append(fechaPago).append(' ');
        return sb.toString().trim();
    }
}
