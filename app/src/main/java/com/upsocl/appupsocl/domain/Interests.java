package com.upsocl.appupsocl.domain;

import com.google.gson.annotations.SerializedName;
import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.keys.ButtonOptionKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by upsocl on 19-07-16.
 */
public class Interests {

    public static final String INTERESTS= "intereses";
    public static final int INTERESTS_SIZE_VALUE = 15;
    public static final String INTERESTS_SIZE = "interests_size";

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("isCheck")
    private Boolean isCheck;
    private int imagen;

    public Interests(int id, String title, Boolean isCheck, int imagen) {
        this.id = id;
        this.title = title;
        this.isCheck =  isCheck;
        this.imagen = imagen;
    }

    public Interests() {
    }

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Interests> createList(){

        ArrayList<Interests> listOptions  =  new ArrayList<>();

        listOptions.add(new Interests(0, ButtonOptionKeys.OPT_GREEN,false,R.drawable.fondo_verde_opt ));
        listOptions.add(new Interests(1,ButtonOptionKeys.OPT_BEAUTY,false, R.drawable.fondo_belleza_opt));
        listOptions.add(new Interests(2,ButtonOptionKeys.OPT_COLABORATION,false,R.drawable.fondo_colaboracion_opt));
        listOptions.add(new Interests(3,ButtonOptionKeys.OPT_COMMUNITY,false,R.drawable.fondo_comunidad_opt));
        listOptions.add(new Interests(4,ButtonOptionKeys.OPT_CREATIVITY,false, R.drawable.fondo_creatividad_opt));
        listOptions.add(new Interests(5,ButtonOptionKeys.OPT_CULTURA,false,R.drawable.fondo_cultura_opt));
        listOptions.add(new Interests(6,ButtonOptionKeys.OPT_DIVERSITY,false,R.drawable.fondo_diversidad_opt));
        listOptions.add(new Interests(7,ButtonOptionKeys.OPT_FAMILY,false, R.drawable.fondo_familia_opt));
        listOptions.add(new Interests(8,ButtonOptionKeys.OPT_HEALTH,false, R.drawable.fondo_salud_opt));
        listOptions.add(new Interests(9,ButtonOptionKeys.OPT_INSPIRATION,false, R.drawable.fondo_inspiracion_opt));
        listOptions.add(new Interests(10,ButtonOptionKeys.OPT_MOVIES,false, R.drawable.fondo_pelicula_opt));
        listOptions.add(new Interests(11,ButtonOptionKeys.OPT_QUIZ,false, R.drawable.fondo_quiz_opt));
        listOptions.add(new Interests(12,ButtonOptionKeys.OPT_RELATIONS,false, R.drawable.fondo_relaciones_opt));
        listOptions.add(new Interests(13,ButtonOptionKeys.OPT_STYLELIVE,false, R.drawable.fondo_estilo_vida_opt));
        listOptions.add(new Interests(14,ButtonOptionKeys.OPT_WOMEN,false, R.drawable.fondo_mujer_opt));
        listOptions.add(new Interests(15,ButtonOptionKeys.OPT_WORLD,false, R.drawable.fondo_mundo_opt));

        return listOptions;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public Interests getInterestByID(int id){
        List<Interests> listOptions = createList();

        return listOptions.get(id);
    }
}
