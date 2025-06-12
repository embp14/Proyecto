package dto;

public class CarritoItemDTO {
    private int id;
    private int carritoId;
    private int varianteId;
    private int cantidad;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCarritoId() { return carritoId; }
    public void setCarritoId(int carritoId) { this.carritoId = carritoId; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
