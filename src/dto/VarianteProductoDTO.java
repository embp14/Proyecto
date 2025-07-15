package dto;

public class VarianteProductoDTO {
    private int id;
    private int productoId;
    private String sku;
    private String nombre;
    private String productoNombre;
    private double precio;
    private int stock;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        String codigo = sku != null ? sku : "";
        String nomVar = nombre != null ? nombre : "";
        String prod = productoNombre != null ? productoNombre : "";
        String detalle = !nomVar.isEmpty() ? nomVar + " - " : "";
        return codigo + " - " + detalle + prod + " - ID " + id;
    }
}
