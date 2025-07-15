package dto;

public class CategoriaDTO {
    private int id;
    private String nombre;
    private Integer parentId;
    private String parentNombre;

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }

    public Integer getParentId(){ return parentId; }
    public void setParentId(Integer parentId){ this.parentId = parentId; }

    public String getParentNombre() { return parentNombre; }
    public void setParentNombre(String parentNombre) { this.parentNombre = parentNombre; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        if(nombre != null) sb.append(nombre).append(' ');
        if(parentId != null) sb.append(parentId).append(' ');
        if(parentNombre != null) sb.append(parentNombre).append(' ');
        return sb.toString().trim();
    }
}
