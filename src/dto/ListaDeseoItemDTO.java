package dto;

public class ListaDeseoItemDTO {
    private int id;
    private int listaDeseosId;
    private int varianteId;
    private int cantidad;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getListaDeseosId() { return listaDeseosId; }
    public void setListaDeseosId(int listaDeseosId) { this.listaDeseosId = listaDeseosId; }

    public int getVarianteId() { return varianteId; }
    public void setVarianteId(int varianteId) { this.varianteId = varianteId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
