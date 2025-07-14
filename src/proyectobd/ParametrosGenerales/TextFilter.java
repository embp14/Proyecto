package proyectobd.ParametrosGenerales;

import java.util.Arrays;
import java.util.List;

public class TextFilter {
    private static final List<String> PALABRAS_OFENSIVAS = Arrays.asList(
        "puta", "puto", "idiota", "imbecil", "mierda", "cabron", "verga"
    );

    private static final List<String> PALABRAS_RACISTAS = Arrays.asList(
        "nigger", "negrata", "sudaca", "cholo", "mayate"
    );

    public static boolean contieneOfensas(String texto){
        if(texto == null) return false;
        String lower = texto.toLowerCase();
        for(String p : PALABRAS_OFENSIVAS){
            if(lower.contains(p)) return true;
        }
        return false;
    }

    public static boolean contieneRacismo(String texto){
        if(texto == null) return false;
        String lower = texto.toLowerCase();
        for(String p : PALABRAS_RACISTAS){
            if(lower.contains(p)) return true;
        }
        return false;
    }

    public static boolean contieneLenguajeInapropiado(String texto){
        return contieneOfensas(texto) || contieneRacismo(texto);
    }
}
