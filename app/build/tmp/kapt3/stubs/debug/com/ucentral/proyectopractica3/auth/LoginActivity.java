package com.ucentral.proyectopractica3.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0002J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0012\u0010\u001b\u001a\u00020\u00182\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0015H\u0002J\u0010\u0010 \u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020\u0015H\u0002J8\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020\u00152\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00180$2\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00180$H\u0002J\u0010\u0010&\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R#\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/ucentral/proyectopractica3/auth/LoginActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "LOCK_DURATION_MS", "", "MAX_ATTEMPTS", "", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "Lcom/ucentral/proyectopractica3/databinding/ActivityLoginBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "getPrefs", "()Landroid/content/SharedPreferences;", "prefs$delegate", "Lkotlin/Lazy;", "lockKey", "", "userInput", "loginWithEmail", "", "correo", "password", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "registerFailedAttempt", "key", "resetAttempts", "resolverCorreo", "id", "onOk", "Lkotlin/Function1;", "onErr", "toast", "msg", "app_debug"})
public final class LoginActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.ucentral.proyectopractica3.databinding.ActivityLoginBinding binding;
    private com.google.firebase.auth.FirebaseAuth auth;
    private com.google.firebase.firestore.FirebaseFirestore db;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy prefs$delegate = null;
    private final int MAX_ATTEMPTS = 3;
    private final long LOCK_DURATION_MS = 300000L;
    
    public LoginActivity() {
        super();
    }
    
    private final android.content.SharedPreferences getPrefs() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loginWithEmail(java.lang.String correo, java.lang.String password, java.lang.String lockKey) {
    }
    
    /**
     * Si 'id' tiene '@' => es correo.
     * Si es numérico => busca por 'cedula'.
     * Si no, busca por 'usuario'.
     * Devuelve el 'correo' para usar con FirebaseAuth.
     */
    private final void resolverCorreo(java.lang.String id, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOk, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onErr) {
    }
    
    private final void registerFailedAttempt(java.lang.String key) {
    }
    
    private final void resetAttempts(java.lang.String key) {
    }
    
    private final java.lang.String lockKey(java.lang.String userInput) {
        return null;
    }
    
    private final void toast(java.lang.String msg) {
    }
}