package com.ucentral.proyectopractica3.ui.doctor;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\rH\u0002J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ucentral/proyectopractica3/ui/doctor/DoctorDiagnosticoActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "btnGenerar", "Landroid/widget/Button;", "btnGuardar", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "etAntecedentes", "Landroid/widget/EditText;", "etHabitos", "etSintomas", "pacienteId", "", "resultado", "tvResultado", "Landroid/widget/TextView;", "generarDiagnostico", "sintomas", "habitos", "antecedentes", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class DoctorDiagnosticoActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.google.firebase.firestore.FirebaseFirestore db;
    private android.widget.EditText etSintomas;
    private android.widget.EditText etAntecedentes;
    private android.widget.EditText etHabitos;
    private android.widget.TextView tvResultado;
    private android.widget.Button btnGenerar;
    private android.widget.Button btnGuardar;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String resultado = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String pacienteId = "";
    
    public DoctorDiagnosticoActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final java.lang.String generarDiagnostico(java.lang.String sintomas, java.lang.String habitos, java.lang.String antecedentes) {
        return null;
    }
}