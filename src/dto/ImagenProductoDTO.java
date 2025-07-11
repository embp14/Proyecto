package dto;

public class ImagenProductoDTO {
    private int id;
    private int productoId;
    private String productoNombre;
    private String url;
    private boolean esPrincipal;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public boolean isEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(boolean esPrincipal) { this.esPrincipal = esPrincipal; }
}
