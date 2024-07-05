package com.equipo1.DeliciasUrbanas.ui.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.equipo1.DeliciasUrbanas.R;

public class SplashActivity extends AppCompatActivity {
    private boolean isMainActivityStarted = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Cargar las animaciones
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        View logo = findViewById(R.id.imageViewLogo);
        View tagLine = findViewById(R.id.textViewTagLine);
        logo.startAnimation(anim);
        tagLine.startAnimation(anim);

        // Inicializar MediaPlayer y reproducir el sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound);
        mediaPlayer.start();  // Iniciar el sonido

        // Handler para cambiar de actividad después de 4 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isMainActivityStarted) {
                    goToWelcomeActivity();
                }
            }
        }, 3500);

        // Configurar animación para detener el sonido al finalizar
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Nada especial aquí
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Detener y liberar el MediaPlayer al finalizar la animación
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Nada especial aquí
            }
        });
    }

    private void goToWelcomeActivity() {
        if (!isMainActivityStarted) {
            isMainActivityStarted = true;
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarse de liberar el MediaPlayer cuando la Activity se destruya
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Método para cambiar de actividad al tocar la pantalla
    public void onScreenTap(View view) {
        goToWelcomeActivity();
    }
}