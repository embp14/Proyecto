package dto;

public class OrdenItemDTO {
    private int id;
    private int ordenId;
    private String usuarioNombre;
    private int varianteId;
    private String varianteSku;
    private String productoNombre;
    private int cantidad;
    private double precioUnitario;
    private double precioDescuento;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public String getVarianteSku() { return varianteSku; }
    public void setVarianteSku(String varianteSku) { this.varianteSku = varianteSku; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getPrecioDescuento() { return precioDescuento; }
    public void setPrecioDescuento(double precioDescuento) { this.precioDescuento = precioDescuento; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(ordenId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        sb.append(varianteId).append(' ');
        if(varianteSku != null) sb.append(varianteSku).append(' ');
        if(productoNombre != null) sb.append(productoNombre).append(' ');
        sb.append(cantidad).append(' ');
        sb.append(precioUnitario).append(' ');
        sb.append(precioDescuento);
        return sb.toString().trim();
    }
}
