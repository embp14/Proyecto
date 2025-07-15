package dto;

public class VarianteProductoDTO {
    private int id;
    private int productoId;
    private String sku;
    private String productoNombre;
    private double precio;
    private int stock;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(productoId).append(' ');
        if(sku != null) sb.append(sku).append(' ');
        if(productoNombre != null) sb.append(productoNombre).append(' ');
        sb.append(precio).append(' ');
        sb.append(stock);
        return sb.toString().trim();
    }
}
