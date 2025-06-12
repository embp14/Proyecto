package dto;

public class OrdenItemDTO {
    private int id;
    private int ordenId;
    private int varianteId;
    private int cantidad;
    private double precioUnitario;
    private double precioDescuento;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getPrecioDescuento() { return precioDescuento; }
    public void setPrecioDescuento(double precioDescuento) { this.precioDescuento = precioDescuento; }
}
