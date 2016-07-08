package com.upsocl.appupsocl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.upsocl.appupsocl.keys.ButtonOptionKeys;

import java.util.ArrayList;

public class CreatePerfil extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private Button btn_culture, btn_community, btn_travel, btn_quiz, btn_worl, btn_animals ,
            btn_women,btn_cook, btn_inspiration, btn_health, btn_relations, btn_family,
            btn_creativity, btn_beauty, btn_diversity, btn_movies, btn_styleLive;
    ArrayList<String> listOptions =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_create_perfil);

        btn_culture = (Button) findViewById(R.id.btn_culture);
        btn_community = (Button) findViewById(R.id.btn_community);
        btn_travel = (Button) findViewById(R.id.btn_travel);
        btn_quiz = (Button) findViewById(R.id.btn_quiz);
        btn_worl = (Button) findViewById(R.id.btn_worl);
        btn_animals = (Button) findViewById(R.id.btn_animals);
        btn_women = (Button) findViewById(R.id.btn_women);
        btn_cook = (Button) findViewById(R.id.btn_cook);
        btn_inspiration = (Button) findViewById(R.id.btn_inspiration);
        btn_health = (Button) findViewById(R.id.btn_health);
        btn_family = (Button) findViewById(R.id.btn_family);
        btn_creativity = (Button)findViewById(R.id.btn_creativity );
        btn_beauty =  (Button) findViewById(R.id.btn_beauty);
        btn_diversity = (Button) findViewById(R.id.btn_diversity);
        btn_movies = (Button) findViewById(R.id.btn_movies);
        btn_styleLive = (Button) findViewById(R.id.btn_styleLive);
        btn_relations = (Button) findViewById(R.id.btn_relations);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("User id: " + loginResult.getAccessToken().getUserId() +
                        " Token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("Error : " + e.getMessage() );

            }
        });
    }

    public void btnCLickInit(View view){
        if (listOptions.size()>=3){
            Intent  intent = new Intent(this,HomeActivity.class);
            CreatePerfil.this.startActivity(intent);
        }else
            Toast.makeText(CreatePerfil.this, "Debe seleccionar al menos 3 categorias", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void btnCuture(View view){

        changeStatusButton(ButtonOptionKeys.OPT_CULTURA,btn_culture);
    }

    public void btnCommunity(View view){

        changeStatusButton(ButtonOptionKeys.OPT_COMMUNITY,btn_community);
    }

    public void btnQuiz(View view){
        changeStatusButton(ButtonOptionKeys.OPT_QUIZ,btn_quiz);


    }
    public void bntWorl(View view){
        changeStatusButton(ButtonOptionKeys.OPT_WORL,btn_worl);
    }

    public void btnWomen(View view){
        changeStatusButton(ButtonOptionKeys.OPT_WOMEN,btn_women);
    }

    public void btnTravel(View view){
        changeStatusButton(ButtonOptionKeys.OPT_TRAVEL,btn_travel);
    }

    public void btnAnimals(View view){
        changeStatusButton(ButtonOptionKeys.OPT_ANIMALS,btn_animals);
    }

    public void btnCook(View view){

        changeStatusButton(ButtonOptionKeys.OPT_COOK,btn_cook);
    }

    public void btnInspiration(View view){

        changeStatusButton(ButtonOptionKeys.OPT_INSPIRATION,btn_inspiration);
    }

    public void btnHealth(View view){
        changeStatusButton(ButtonOptionKeys.OPT_HEALTH, btn_health);
    }

    public void btnRelations(View view){

        changeStatusButton(ButtonOptionKeys.OPT_RELATIONS, btn_relations);
    }

    public void btnFamily(View view){
        changeStatusButton(ButtonOptionKeys.OPT_FAMILY, btn_family);
    }

    public void btnCreativity(View view){
        changeStatusButton(ButtonOptionKeys.OPT_CREATIVITY, btn_creativity);
    }
    public void btnBeauty(View view){
        changeStatusButton(ButtonOptionKeys.OPT_BEAUTY, btn_beauty);
    }
    public void btnDiversity(View view){
        changeStatusButton(ButtonOptionKeys.OPT_DIVERSITY, btn_diversity);
    }
    public void btnMovies(View view){
        changeStatusButton(ButtonOptionKeys.OPT_MOVIES, btn_movies);
    }

    public void btnStyleLive(View view){
        changeStatusButton(ButtonOptionKeys.OPT_STYLELIVE, btn_styleLive);
    }

    private void changeStatusButton(String optCultura, Button button) {

        if (changeListOpt(optCultura))
            button.setBackground(getResources().getDrawable(R.drawable.boton_desahabilitado));
        else
            button.setBackground(getResources().getDrawable(R.drawable.boton_normal));
    }

    private Boolean changeListOpt(String option) {
        boolean flag= true;

        for (int i=0;i< listOptions.size(); i++){
            if (listOptions.get(i).equals(option)){
                listOptions.remove(i);
                System.out.println("Remover: " + option);
                return false;
            }
        }

        listOptions.add(option);
        System.out.println("Tamaño de la lista: " +listOptions.size());

        return flag;
    }


}
