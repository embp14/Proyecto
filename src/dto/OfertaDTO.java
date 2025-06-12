package dto;

import java.sql.Timestamp;

public class OfertaDTO {
    private int id;
    private int varianteId;
    private double precioDescuento;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public double getPrecioDescuento() { return precioDescuento; }
    public void setPrecioDescuento(double precioDescuento) { this.precioDescuento = precioDescuento; }

    public Timestamp getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

    public Timestamp getFechaFin() { return fechaFin; }
    public void setFechaFin(Timestamp fechaFin) { this.fechaFin = fechaFin; }
}
