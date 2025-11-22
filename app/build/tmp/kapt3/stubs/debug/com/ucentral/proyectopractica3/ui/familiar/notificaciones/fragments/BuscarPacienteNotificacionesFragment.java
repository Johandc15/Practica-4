package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0013H\u0002J$\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0013H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/ucentral/proyectopractica3/ui/familiar/notificaciones/fragments/BuscarPacienteNotificacionesFragment;", "Landroidx/fragment/app/Fragment;", "()V", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "btnUsarPaciente", "Landroid/widget/Button;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "pacientesIds", "", "", "pacientesNombres", "spinnerPacientes", "Landroid/widget/Spinner;", "tvInfo", "Landroid/widget/TextView;", "tvPacienteActual", "cargarPacientesAsociados", "", "mostrarPacienteActual", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "seleccionarPacienteDeSpinner", "app_debug"})
public final class BuscarPacienteNotificacionesFragment extends androidx.fragment.app.Fragment {
    private com.google.firebase.auth.FirebaseAuth auth;
    private com.google.firebase.firestore.FirebaseFirestore db;
    private android.widget.TextView tvPacienteActual;
    private android.widget.Spinner spinnerPacientes;
    private android.widget.Button btnUsarPaciente;
    private android.widget.TextView tvInfo;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.String> pacientesIds;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.String> pacientesNombres;
    
    public BuscarPacienteNotificacionesFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    /**
     * Muestra el texto con el paciente actualmente seleccionado
     */
    private final void mostrarPacienteActual() {
    }
    
    /**
     * Lee familiares/{familiarId}/pacientesAsociados y llena el spinner
     */
    private final void cargarPacientesAsociados() {
    }
    
    /**
     * Guarda en PacienteSeleccionado el paciente elegido en el spinner
     */
    private final void seleccionarPacienteDeSpinner() {
    }
}