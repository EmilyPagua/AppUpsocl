package com.upsocl.appupsocl.domain;

import com.google.gson.annotations.SerializedName;
import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.keys.CategoryKeys;

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
    @SerializedName("nameCategory")
    private String nameCategory;
    private int imagen;

    public Interests(int id, String title, Boolean isCheck, int imagen, String nameCategory) {
        this.id = id;
        this.title = title;
        this.isCheck =  isCheck;
        this.imagen = imagen;
        this.nameCategory =  nameCategory;
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

        listOptions.add(new Interests(0, CategoryKeys.OPT_GREEN,false,R.drawable.fondo_verde_opt, CategoryKeys.ID_CATEGORY_GREEN  ));
        listOptions.add(new Interests(1, CategoryKeys.OPT_BEAUTY,false, R.drawable.fondo_belleza_opt, CategoryKeys.ID_CATEGORY_BEAUTY));
        listOptions.add(new Interests(2, CategoryKeys.OPT_COLABORATION,false,R.drawable.fondo_colaboracion_opt, CategoryKeys.ID_CATEGORY_COLABORATION));
        listOptions.add(new Interests(3, CategoryKeys.OPT_COMMUNITY,false,R.drawable.fondo_comunidad_opt, CategoryKeys.ID_CATEGORY_COMMUNITY));
        listOptions.add(new Interests(4, CategoryKeys.OPT_CREATIVITY,false, R.drawable.fondo_creatividad_opt, CategoryKeys.ID_CATEGORY_CREATIVITY));
        listOptions.add(new Interests(5, CategoryKeys.OPT_CULTURA,false,R.drawable.fondo_cultura_opt,CategoryKeys.ID_CATEGORY_CULTURA ));
        listOptions.add(new Interests(6, CategoryKeys.OPT_DIVERSITY,false,R.drawable.fondo_diversidad_opt, CategoryKeys.ID_CATEGORY_DIVERSITY));
        listOptions.add(new Interests(7, CategoryKeys.OPT_FAMILY,false, R.drawable.fondo_familia_opt,CategoryKeys.ID_CATEGORY_FAMILY));
        listOptions.add(new Interests(8, CategoryKeys.OPT_HEALTH,false, R.drawable.fondo_salud_opt, CategoryKeys.ID_CATEGORY_HEALTH));
        listOptions.add(new Interests(9, CategoryKeys.OPT_INSPIRATION,false, R.drawable.fondo_inspiracion_opt, CategoryKeys.ID_CATEGORY_INSPIRATION));
        listOptions.add(new Interests(10, CategoryKeys.OPT_MOVIES,false, R.drawable.fondo_pelicula_opt, CategoryKeys.ID_CATEGORY_MOVIES ));
        listOptions.add(new Interests(11, CategoryKeys.OPT_QUIZ,false, R.drawable.fondo_quiz_opt, CategoryKeys.OPT_QUIZ));
        listOptions.add(new Interests(12, CategoryKeys.OPT_RELATIONS,false, R.drawable.fondo_relaciones_opt, CategoryKeys.ID_CATEGORY_RELATIONS));
        listOptions.add(new Interests(13, CategoryKeys.OPT_STYLELIVE,false, R.drawable.fondo_estilo_vida_opt, CategoryKeys.ID_CATEGORY_STYLELIVE));
        listOptions.add(new Interests(14, CategoryKeys.OPT_WOMEN,false, R.drawable.fondo_mujer_opt, CategoryKeys.ID_CATEGORY_WOMEN));
        listOptions.add(new Interests(15, CategoryKeys.OPT_WORLD,false, R.drawable.fondo_mundo_opt, CategoryKeys.ID_CATEGORY_WORLD));

        return listOptions;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
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
