package com.equipo1.DeliciasUrbanas.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import com.equipo1.DeliciasUrbanas.R;
import com.equipo1.DeliciasUrbanas.ui.adapters.CartAdapter;
import com.equipo1.DeliciasUrbanas.data.models.PedidoModel;
import com.equipo1.DeliciasUrbanas.data.models.Producto;
import com.equipo1.DeliciasUrbanas.viewmodel.CartViewModel;
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

    private CartViewModel cartViewModel;
    List<Producto> list;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    TextView importeTotal;
    Button realizarPedido;
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

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        importeTotal = root.findViewById(R.id.textViewPrecioTotal);
        realizarPedido = root.findViewById(R.id.buttonRealizarPedido);
        progressBar = root.findViewById(R.id.progress_bar);

        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        cartViewModel.getProductos().observe(getViewLifecycleOwner(), productos -> {
            list = productos;
            cartAdapter = new CartAdapter(getContext(), list, MyCartFragment.this);
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();
        });

        cartViewModel.getTotal().observe(getViewLifecycleOwner(), total -> importeTotal.setText(String.valueOf(total)));

        cartViewModel.cargarCarrito();

        realizarPedido.setOnClickListener(v -> {
            int cantidadProductos = list.size();
            if (cantidadProductos == 0) {
                Toast.makeText(getContext(), "El carrito está vacío!", Toast.LENGTH_LONG).show();
                return;
            }
            realizarPagoPayPal();
        });

        return root;
    }

    private void procesarPago(){
        mostrarIndicadorDeCarga();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    cartViewModel.registrarPedido(currentUser, list, cartViewModel.getTotal().getValue());
                    cartViewModel.limpiarCarrito(currentUser);
                }
                ocultarIndicadorDeCarga();
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

    @Override
    public void onProductoEliminado(double nuevoTotal) {
        cartViewModel.actualizarTotal(nuevoTotal);
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
