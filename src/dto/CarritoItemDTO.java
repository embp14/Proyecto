package dto;

public class CarritoItemDTO {
    private int id;
    private int carritoId;
    private int varianteId;
    private String varianteSku;
    private String productoNombre;
    private int cantidad;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCarritoId() { return carritoId; }
    public void setCarritoId(int carritoId) { this.carritoId = carritoId; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public String getVarianteSku() { return varianteSku; }
    public void setVarianteSku(String varianteSku) { this.varianteSku = varianteSku; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
