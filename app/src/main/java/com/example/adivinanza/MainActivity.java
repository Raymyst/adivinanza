package com.example.adivinanza;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumero;
    private Button buttonAdivinar, buttonReiniciar;
    private TextView textViewMensaje;
    private LinearLayout linearLayoutCorazones;
    private int numeroAleatorio, intentos = 10;
    private int corazonId = R.drawable.corazon;
    private List<ImageView> corazones = new ArrayList<>();
    private MediaPlayer sonidoGanar, sonidoPerder, sonidoIntento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        editTextNumero = findViewById(R.id.editTextNumero);
        buttonAdivinar = findViewById(R.id.buttonAdivinar);
        buttonReiniciar = findViewById(R.id.buttonReiniciar);
        textViewMensaje = findViewById(R.id.textViewMensaje);
        linearLayoutCorazones = findViewById(R.id.linearLayoutCorazones);


        generarNumeroAleatorio();
        actualizarCorazones();

        sonidoGanar = MediaPlayer.create(this, R.raw.ganador);
        sonidoPerder = MediaPlayer.create(this, R.raw.perdedor);
        sonidoIntento = MediaPlayer.create(this, R.raw.error);



        buttonAdivinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numeroUsuario = Integer.parseInt(editTextNumero.getText().toString());

                if (intentos > 0) {
                    if (numeroUsuario == numeroAleatorio) {
                        textViewMensaje.setText("¡Acertaste!");
                        sonidoGanar.start();
                        buttonAdivinar.setEnabled(false);
                    } else if (numeroUsuario < numeroAleatorio) {
                        textViewMensaje.setText("El número es mayor.");
                        sonidoIntento.start();
                    } else {
                        textViewMensaje.setText("El número es menor.");
                        sonidoIntento.start();
                    }

                    intentos--;

                    actualizarCorazones();

                    if (intentos == 0) {
                        if (numeroUsuario != numeroAleatorio) {
                            textViewMensaje.setText("¡Se acabaron los intentos!");
                            sonidoPerder.start();
                            buttonAdivinar.setEnabled(false);
                        }

                    }
                    else {
                        // Si todavía hay intentos y el jugador adivinó, entonces ganó
                        if (numeroUsuario == numeroAleatorio) {
                            textViewMensaje.setText("¡Felicidades! ¡Adivinaste!");
                            sonidoGanar.start();
                            buttonAdivinar.setEnabled(false);
                        }
                        }
                }
            }
        });

        buttonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentos = 10;
                textViewMensaje.setText("");
                actualizarCorazones();

                editTextNumero.setText("");

                buttonAdivinar.setEnabled(true); // Habilitar el botón verificar
                Toast.makeText(MainActivity.this, "Juego reiniciado. ¡Adivina el número!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generarNumeroAleatorio() {
        Random random = new Random();
        numeroAleatorio = random.nextInt(100) + 1;
    }


    private void actualizarCorazones() {
        linearLayoutCorazones.removeAllViews();
        corazones.clear();

        // Crear un LinearLayout horizontal para cada fila de corazones (opcional)
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < intentos; i++) {
            ImageView imageViewCorazon = new ImageView(this);
            imageViewCorazon.setImageResource(corazonId);
            imageViewCorazon.setLayoutParams(new LinearLayout.LayoutParams(50, 50)); // Ajusta el tamaño de los corazones
            rowLayout.addView(imageViewCorazon);
            corazones.add(imageViewCorazon);

            // Si se llega al final de la fila, agregar el LinearLayout al layout principal
            if ((i + 1) % 5 == 0) { // Ajusta el número de corazones por fila según sea necesario
                linearLayoutCorazones.addView(rowLayout);
                rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            }
        }

        // Agregar el último LinearLayout si no se llenó una fila completa
        if (rowLayout.getChildCount() > 0) {
            linearLayoutCorazones.addView(rowLayout);
        }

        // Eliminar los corazones adicionales si los intentos han disminuido
        // ... (resto del código para eliminar corazones
    }
}