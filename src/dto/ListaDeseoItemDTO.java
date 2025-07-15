package dto;

public class ListaDeseoItemDTO {
    private int id;
    private int listaDeseosId;
    private String listaNombre;
    private int varianteId;
    private String varianteSku;
    private String productoNombre;
    private int cantidad;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getListaDeseosId() { return listaDeseosId; }
    public void setListaDeseosId(int listaDeseosId) { this.listaDeseosId = listaDeseosId; }

    public String getListaNombre() { return listaNombre; }
    public void setListaNombre(String listaNombre) { this.listaNombre = listaNombre; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public String getVarianteSku() { return varianteSku; }
    public void setVarianteSku(String varianteSku) { this.varianteSku = varianteSku; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(listaDeseosId).append(' ');
        if(listaNombre != null) sb.append(listaNombre).append(' ');
        sb.append(varianteId).append(' ');
        if(varianteSku != null) sb.append(varianteSku).append(' ');
        if(productoNombre != null) sb.append(productoNombre).append(' ');
        sb.append(cantidad);
        return sb.toString().trim();
    }
}
