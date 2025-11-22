package com.ucentral.proyectopractica3.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0002J\b\u0010\u0012\u001a\u00020\u000eH\u0002J\b\u0010\u0013\u001a\u00020\u000eH\u0002J\u0012\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u0010\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u000eH\u0002J\u0018\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000eH\u0002J\u0010\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u000eH\u0002J\u0018\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/ucentral/proyectopractica3/auth/RegisterActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "Lcom/ucentral/proyectopractica3/databinding/ActivityRegisterBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "mostrandoConfirm", "", "mostrandoPass", "isStrongPassword", "password", "", "mostrarPrivacidad", "", "mostrarTerminos", "obtenerTextoPrivacidad", "obtenerTextoTerminos", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "passwordRequirementsMessage", "pwd", "setHelperForPassword", "confirm", "toast", "msg", "togglePasswordVisibility", "editText", "Landroid/widget/EditText;", "show", "app_debug"})
public final class RegisterActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.ucentral.proyectopractica3.databinding.ActivityRegisterBinding binding;
    private com.google.firebase.auth.FirebaseAuth auth;
    private com.google.firebase.firestore.FirebaseFirestore db;
    private boolean mostrandoPass = false;
    private boolean mostrandoConfirm = false;
    
    public RegisterActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setHelperForPassword(java.lang.String pwd, java.lang.String confirm) {
    }
    
    private final java.lang.String passwordRequirementsMessage(java.lang.String pwd) {
        return null;
    }
    
    private final boolean isStrongPassword(java.lang.String password) {
        return false;
    }
    
    private final void togglePasswordVisibility(android.widget.EditText editText, boolean show) {
    }
    
    private final void mostrarTerminos() {
    }
    
    private final void mostrarPrivacidad() {
    }
    
    private final java.lang.String obtenerTextoTerminos() {
        return null;
    }
    
    private final java.lang.String obtenerTextoPrivacidad() {
        return null;
    }
    
    private final void toast(java.lang.String msg) {
    }
}