package com.ucentral.proyectopractica3.ui.notificaciones;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J.\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ.\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ&\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\rR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/ucentral/proyectopractica3/ui/notificaciones/AlarmScheduler;", "", "()V", "TAG", "", "cancel", "", "context", "Landroid/content/Context;", "recordatorioId", "nextOccurrenceMillis", "", "hora24", "", "minuto", "rescheduleNextDaily", "nombreMedicamento", "scheduleDailyExact", "snoozeMinutes", "minutes", "app_debug"})
public final class AlarmScheduler {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AlarmScheduler";
    @org.jetbrains.annotations.NotNull()
    public static final com.ucentral.proyectopractica3.ui.notificaciones.AlarmScheduler INSTANCE = null;
    
    private AlarmScheduler() {
        super();
    }
    
    /**
     * Programa una alarma exacta para la PRÓXIMA vez que toque [hora24:minuto].
     * Si esa hora ya pasó hoy, la agenda para mañana.
     *
     * Esta alarma dispara AlarmaReceiver, que:
     * - muestra notificación intrusiva con pantalla completa
     * - vibra fuerte + sonido de alarma
     * - ofrece Tomar / Posponer
     * - reprograma la alarma para el siguiente día
     */
    public final void scheduleDailyExact(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String recordatorioId, @org.jetbrains.annotations.NotNull()
    java.lang.String nombreMedicamento, int hora24, int minuto) {
    }
    
    /**
     * Llamado desde AlarmaReceiver DESPUÉS de que la alarma normal sonó.
     * Agenda la misma hora para el día siguiente.
     */
    public final void rescheduleNextDaily(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String recordatorioId, @org.jetbrains.annotations.NotNull()
    java.lang.String nombreMedicamento, int hora24, int minuto) {
    }
    
    /**
     * Llamado desde AccionPosponerReceiver (botón "Posponer 10 min").
     * Agenda una alarma TEMPORAL dentro de [minutes] minutos desde AHORA.
     * Esta alarma llega con "snooze"=true para que AlarmaReceiver NO reprograme el día siguiente todavía.
     */
    public final void snoozeMinutes(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String recordatorioId, @org.jetbrains.annotations.NotNull()
    java.lang.String nombreMedicamento, int minutes) {
    }
    
    /**
     * Cancela la alarma principal para un recordatorio en específico.
     * Útil cuando el usuario borra o edita el recordatorio.
     */
    public final void cancel(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String recordatorioId) {
    }
    
    /**
     * Devuelve el próximo timestamp (millis) para cierta hora del día.
     * Si esa hora ya pasó hoy, devuelve mañana a esa hora.
     */
    private final long nextOccurrenceMillis(int hora24, int minuto) {
        return 0L;
    }
}