package dto;

public class DireccionDTO {
    private int id;
    private int usuarioId;
    private String usuarioNombre;
    private String alias;
    private String direccion;
    private String ciudad;
    private String canton;
    private String provincia;
    private String codigoPostal;
    private String telefonoContacto;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getCanton() { return canton; }
    public void setCanton(String canton) { this.canton = canton; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(usuarioId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        if(alias != null) sb.append(alias).append(' ');
        if(direccion != null) sb.append(direccion).append(' ');
        if(ciudad != null) sb.append(ciudad).append(' ');
        if(canton != null) sb.append(canton).append(' ');
        if(provincia != null) sb.append(provincia).append(' ');
        if(codigoPostal != null) sb.append(codigoPostal).append(' ');
        if(telefonoContacto != null) sb.append(telefonoContacto).append(' ');
        return sb.toString().trim();
    }
}
