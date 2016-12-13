package com.upsocl.upsoclapp.domain;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emily.pagua on 05-07-16.
 */
public class CategoryList {

    public static final String INTERESTS= "intereses";
    public static final int INTERESTS_SIZE_VALUE = 15;
    public static final String INTERESTS_SIZE = "interests_size";

    public List<Category> categoryList =  new ArrayList<>();

    public static final String OPT_CULTURA = "Cultura";
    public static final String OPT_COMMUNITY = "Comunidad" ;
    public static final String OPT_QUIZ = "Quiz";
    public static final String OPT_WORLD= "Mundo";
    public static final String OPT_GREEN = "Verde";
    public static final String OPT_MOVIES = "Pelicula";
    public static final String OPT_INSPIRATION= "Inspiracion";
    public static final String OPT_HEALTH= "Salud";
    public static final String OPT_RELATIONS = "Relaciones";
    public static final String OPT_WOMEN= "Mujer";
    public static final String OPT_FAMILY= "Familia";
    public static final String OPT_CREATIVITY= "Creatividad";
    public static final String OPT_BEAUTY= "Belleza";
    public static final String OPT_DIVERSITY= "Diversidad";
    public static final String OPT_STYLELIVE= "Estilo De Vida";
    public static final String OPT_COLABORATION= "Colaboracion";
    public static final String OPT_FOOD= "Comida";
    public static final String OPT_POPULARY= "Popular";

    public void createCategoryList() {
        categoryList.add(new Category(2,"Mundo", R.drawable.fondo_mundo, false));
        categoryList.add(new Category(4,"Diversidad", R.drawable.fondo_diversidad, false));
        categoryList.add(new Category(5,"Comunidad", R.drawable.fondo_comunidad, false));
        categoryList.add(new Category(6,"Mujer", R.drawable.fondo_mujer, false));
        categoryList.add(new Category(7,"Cultura", R.drawable.fondo_cultura, false));
        categoryList.add(new Category(10,"Salud", R.drawable.fondo_salud, false));
        categoryList.add(new Category(12,"Inspiracion", R.drawable.fondo_inspiracion, false));
        categoryList.add(new Category(52,"Verde", R.drawable.fondo_verde, false));
        categoryList.add(new Category(54,"Comida", 0, false));
        categoryList.add(new Category(57,"Creatividad", R.drawable.fondo_creatividad, false));
        categoryList.add(new Category(80,"Colaboracion", R.drawable.fondo_colaboracion, false));
        categoryList.add(new Category(303,"Relaciones", R.drawable.fondo_relaciones, false));
        categoryList.add(new Category(304,"Quiz", R.drawable.fondo_quiz, false));
        categoryList.add(new Category(305,"Estilo De Vida", R.drawable.fondo_estilo_vida, false));
        categoryList.add(new Category(306,"Familia", R.drawable.fondo_familia, false));
        categoryList.add(new Category(307,"Pelicula", R.drawable.fondo_pelicula, false));
        categoryList.add(new Category(309,"Belleza", R.drawable.fondo_belleza, false));
        categoryList.add(new Category(311,"Popular", 0, false));
    }

    public List<Category> getCategoryList() {
        createCategoryList();
        return categoryList;
    }

    public Category getIdCategoryByName(String name){
        createCategoryList();
        for (int i=0; i < categoryList.size(); i++){
            if (categoryList.get(i).getName().equals(name))
                return categoryList.get(i);
        }
        return null;
    }

    public Category getCategoryById(int id){
        createCategoryList();
        for (int i=0; i < categoryList.size(); i++){
            if (categoryList.get(i).getId()==id)
                return categoryList.get(i);
        }
        return null;
    }
}
