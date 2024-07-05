package com.equipo1.DeliciasUrbanas.ui.cart;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.equipo1.DeliciasUrbanas.MainActivity;
import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.activities.DetailedDailyMealActivity;
import com.equipo1.DeliciasUrbanas.adapters.CartAdapter;
import com.equipo1.DeliciasUrbanas.models.PedidoModel;
import com.equipo1.DeliciasUrbanas.models.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCartFragment extends Fragment implements CartAdapter.CartAdapterCallback {
    private static final String CLIENT_ID = "AemRsL37DEuyNcTKZumrs2BnAeU6htJU2tR5KoFAMwOSnByISEibMH3Es_4CtB569pzVCfMHmq0smL4T";
    private static final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Cambia a ENVIRONMENT_PRODUCTION en producción
            .clientId(CLIENT_ID);

    List<Producto> list;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    TextView importeTotal;
    Button realizarPedido;
    double importeTotalPedido;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private boolean fromButton;

    public static MyCartFragment newInstance(boolean fromButton) {
        MyCartFragment fragment = new MyCartFragment();
        Bundle args = new Bundle();
        args.putBoolean("fromButton", fromButton);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromButton = getArguments().getBoolean("fromButton", false);
        }
    }

    public boolean isFromButton() {
        return fromButton;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        // Inicializa el MediaPlayer
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.pay_sound);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        importeTotal = root.findViewById(R.id.textViewPrecioTotal);
        realizarPedido = root.findViewById(R.id.buttonRealizarPedido);
        progressBar = root.findViewById(R.id.progress_bar);

        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        cargarCarrito();

        realizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cantidadProductos = list.size();

                if (cantidadProductos == 0) {
                    Toast.makeText(getContext(), "El carrito está vacío!", Toast.LENGTH_LONG).show();
                    return;
                }

                realizarPagoPayPal();
            }
        });

        return root;
    }

    private void procesarPago(){
        mostrarIndicadorDeCarga();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                registrarPedido();
                limpiarCarrito();
                ocultarIndicadorDeCarga();

                /*
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("openFragment", "PedidoFragment");
                intent.putExtra("fromButton", true); // Parámetro adicional
                startActivity(intent);
                getActivity().finish();*/

            }
        }, 2000);
    }

    private void mostrarIndicadorDeCarga() {
        progressBar.setVisibility(View.VISIBLE);
        realizarPedido.setEnabled(false); // Desactiva el botón mientras se muestra el indicador de carga
    }

    private void ocultarIndicadorDeCarga() {
        progressBar.setVisibility(View.GONE);
        realizarPedido.setEnabled(true); // Vuelve a activar el botón
    }

    private void realizarPagoPayPal() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal("10.00"), "USD", "Sample Item",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("PaymentExample", paymentDetails);


                        procesarPago();

                    } catch (JSONException e) {
                        Log.e("PaymentExample", "Error: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("PaymentExample", "The user canceled.");
                procesarPago();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("PaymentExample", "Invalid payment / config set");
            }
        }
    }

    private void cargarCarrito() {
        String usuarioId = currentUser.getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(usuarioId)
                .collection("carrito")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            importeTotalPedido = 0;

                            List<Producto> carrito = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto producto = document.toObject(Producto.class);
                                importeTotalPedido += producto.getPrecio();
                                carrito.add(producto);
                            }

                            list = carrito;
                            importeTotal.setText(String.valueOf(importeTotalPedido));

                            cartAdapter = new CartAdapter(getContext(), list, MyCartFragment.this);
                            recyclerView.setAdapter(cartAdapter);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void registrarPedido() {

        String pedidoId = db.collection("usuarios").document().getId(); // Genera un ID único
        PedidoModel pedido = new PedidoModel(pedidoId, new Date(), list.size(), "preparando", list, importeTotalPedido);

        db.collection("usuarios")
                .document(currentUser.getUid())
                .collection("pedidos")
                .document(pedidoId)
                .set(pedido)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for (Producto producto : list) {
                            db.collection("usuarios")
                                    .document(currentUser.getUid())
                                    .collection("pedidos")
                                    .document(pedidoId)
                                    .collection("productos")
                                    .document(producto.getNombre())
                                    .set(producto);
                        }
                        reproducirSonido();
                        Toast.makeText(getContext(), "Pedido registrado correctamente", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Error al registrar pedido: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void limpiarCarrito() {
        db.collection("usuarios")
                .document(currentUser.getUid())
                .collection("carrito")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto producto = document.toObject(Producto.class);
                                String nombreProducto = producto.getNombre();
                                db.collection("usuarios")
                                        .document(currentUser.getUid())
                                        .collection("carrito")
                                        .document(producto.getNombre())
                                        .delete();
                            }

                            List<Producto> carrito = new ArrayList<>();
                            importeTotal.setText(String.valueOf(0));
                            list = carrito;
                            cartAdapter = new CartAdapter(getContext(), list);
                            recyclerView.setAdapter(cartAdapter);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onProductoEliminado(double nuevoTotal) {
        String total = String.valueOf(nuevoTotal);
        importeTotal.setText(total);
    }

    private void reproducirSonido() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
        mediaPlayer.start();
    }

    @Override
    public void onDestroyView() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroyView();
    }
}
