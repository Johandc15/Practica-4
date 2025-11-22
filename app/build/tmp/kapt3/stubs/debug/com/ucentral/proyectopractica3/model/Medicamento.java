package com.ucentral.proyectopractica3.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\bX\b\u0086\b\u0018\u00002\u00020\u0001B\u00d7\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\r\u0012\b\b\u0002\u0010\u0010\u001a\u00020\r\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\r\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u001aJ\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\rH\u00c6\u0003J\t\u0010M\u001a\u00020\rH\u00c6\u0003J\t\u0010N\u001a\u00020\rH\u00c6\u0003J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\t\u0010S\u001a\u00020\rH\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\t\u0010U\u001a\u00020\u0003H\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\t\u0010X\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0003H\u00c6\u0003J\t\u0010[\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0003H\u00c6\u0003J\t\u0010]\u001a\u00020\u0003H\u00c6\u0003J\t\u0010^\u001a\u00020\u000bH\u00c6\u0003J\t\u0010_\u001a\u00020\rH\u00c6\u0003J\u00db\u0001\u0010`\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\r2\b\b\u0002\u0010\u0010\u001a\u00020\r2\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\r2\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u00032\b\b\u0002\u0010\u0019\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010a\u001a\u00020\r2\b\u0010b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010c\u001a\u00020\u000bH\u00d6\u0001J\t\u0010d\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0018\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u0011\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001eR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010\u000e\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\"\"\u0004\b&\u0010$R\u001a\u0010\u0010\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010\"\"\u0004\b(\u0010$R\u001a\u0010\u000f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\"\"\u0004\b*\u0010$R\u001a\u0010\u0017\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u001c\"\u0004\b,\u0010\u001eR\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u001c\"\u0004\b.\u0010\u001eR\u001a\u0010\u0014\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u001c\"\u0004\b0\u0010\u001eR\u001a\u0010\u0013\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u001c\"\u0004\b2\u0010\u001eR\u001a\u0010\u0019\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u001c\"\u0004\b4\u0010\u001eR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u001c\"\u0004\b6\u0010\u001eR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u001c\"\u0004\b8\u0010\u001eR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b9\u0010\u001c\"\u0004\b:\u0010\u001eR\u001a\u0010\u0016\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010\u001c\"\u0004\b<\u0010\u001eR\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u001c\"\u0004\b>\u0010\u001eR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u001a\u0010\u0012\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u001c\"\u0004\bD\u0010\u001eR\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010\u001c\"\u0004\bF\u0010\u001eR\u001a\u0010\u0015\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bG\u0010\"\"\u0004\bH\u0010$R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\u001c\"\u0004\bJ\u0010\u001e\u00a8\u0006e"}, d2 = {"Lcom/ucentral/proyectopractica3/model/Medicamento;", "", "id", "", "nombre", "nombreGenerico", "presentacion", "viaAdministracion", "dosisPorToma", "unidadDosis", "tomasPorDia", "", "cicloManana", "", "cicloMediodia", "cicloTarde", "cicloNoche", "cantidadTotal", "unidadCantidadTotal", "fechaInicio", "fechaFin", "usoCronico", "notas", "dosis", "cantidad", "horario", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZZZZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getCantidad", "()Ljava/lang/String;", "setCantidad", "(Ljava/lang/String;)V", "getCantidadTotal", "setCantidadTotal", "getCicloManana", "()Z", "setCicloManana", "(Z)V", "getCicloMediodia", "setCicloMediodia", "getCicloNoche", "setCicloNoche", "getCicloTarde", "setCicloTarde", "getDosis", "setDosis", "getDosisPorToma", "setDosisPorToma", "getFechaFin", "setFechaFin", "getFechaInicio", "setFechaInicio", "getHorario", "setHorario", "getId", "setId", "getNombre", "setNombre", "getNombreGenerico", "setNombreGenerico", "getNotas", "setNotas", "getPresentacion", "setPresentacion", "getTomasPorDia", "()I", "setTomasPorDia", "(I)V", "getUnidadCantidadTotal", "setUnidadCantidadTotal", "getUnidadDosis", "setUnidadDosis", "getUsoCronico", "setUsoCronico", "getViaAdministracion", "setViaAdministracion", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class Medicamento {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String id;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String nombre;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String nombreGenerico;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String presentacion;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String viaAdministracion;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String dosisPorToma;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String unidadDosis;
    private int tomasPorDia;
    private boolean cicloManana;
    private boolean cicloMediodia;
    private boolean cicloTarde;
    private boolean cicloNoche;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String cantidadTotal;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String unidadCantidadTotal;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String fechaInicio;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String fechaFin;
    private boolean usoCronico;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String notas;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String dosis;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String cantidad;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String horario;
    
    public Medicamento(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String nombre, @org.jetbrains.annotations.NotNull()
    java.lang.String nombreGenerico, @org.jetbrains.annotations.NotNull()
    java.lang.String presentacion, @org.jetbrains.annotations.NotNull()
    java.lang.String viaAdministracion, @org.jetbrains.annotations.NotNull()
    java.lang.String dosisPorToma, @org.jetbrains.annotations.NotNull()
    java.lang.String unidadDosis, int tomasPorDia, boolean cicloManana, boolean cicloMediodia, boolean cicloTarde, boolean cicloNoche, @org.jetbrains.annotations.NotNull()
    java.lang.String cantidadTotal, @org.jetbrains.annotations.NotNull()
    java.lang.String unidadCantidadTotal, @org.jetbrains.annotations.NotNull()
    java.lang.String fechaInicio, @org.jetbrains.annotations.NotNull()
    java.lang.String fechaFin, boolean usoCronico, @org.jetbrains.annotations.NotNull()
    java.lang.String notas, @org.jetbrains.annotations.NotNull()
    java.lang.String dosis, @org.jetbrains.annotations.NotNull()
    java.lang.String cantidad, @org.jetbrains.annotations.NotNull()
    java.lang.String horario) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    public final void setId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNombre() {
        return null;
    }
    
    public final void setNombre(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNombreGenerico() {
        return null;
    }
    
    public final void setNombreGenerico(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPresentacion() {
        return null;
    }
    
    public final void setPresentacion(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getViaAdministracion() {
        return null;
    }
    
    public final void setViaAdministracion(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDosisPorToma() {
        return null;
    }
    
    public final void setDosisPorToma(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUnidadDosis() {
        return null;
    }
    
    public final void setUnidadDosis(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final int getTomasPorDia() {
        return 0;
    }
    
    public final void setTomasPorDia(int p0) {
    }
    
    public final boolean getCicloManana() {
        return false;
    }
    
    public final void setCicloManana(boolean p0) {
    }
    
    public final boolean getCicloMediodia() {
        return false;
    }
    
    public final void setCicloMediodia(boolean p0) {
    }
    
    public final boolean getCicloTarde() {
        return false;
    }
    
    public final void setCicloTarde(boolean p0) {
    }
    
    public final boolean getCicloNoche() {
        return false;
    }
    
    public final void setCicloNoche(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCantidadTotal() {
        return null;
    }
    
    public final void setCantidadTotal(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUnidadCantidadTotal() {
        return null;
    }
    
    public final void setUnidadCantidadTotal(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFechaInicio() {
        return null;
    }
    
    public final void setFechaInicio(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFechaFin() {
        return null;
    }
    
    public final void setFechaFin(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean getUsoCronico() {
        return false;
    }
    
    public final void setUsoCronico(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNotas() {
        return null;
    }
    
    public final void setNotas(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDosis() {
        return null;
    }
    
    public final void setDosis(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCantidad() {
        return null;
    }
    
    public final void setCantidad(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHorario() {
        return null;
    }
    
    public final void setHorario(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public Medicamento() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    public final boolean component17() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    public final int component8() {
        return 0;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ucentral.proyectopractica3.model.Medicamento copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String nombre, @org.jetbrains.annotations.NotNull()
    java.lang.String nombreGenerico, @org.jetbrains.annotations.NotNull()
    java.lang.String presentacion, @org.jetbrains.annotations.NotNull()
    java.lang.String viaAdministracion, @org.jetbrains.annotations.NotNull()
    java.lang.String dosisPorToma, @org.jetbrains.annotations.NotNull()
    java.lang.String unidadDosis, int tomasPorDia, boolean cicloManana, boolean cicloMediodia, boolean cicloTarde, boolean cicloNoche, @org.jetbrains.annotations.NotNull()
    java.lang.String cantidadTotal, @org.jetbrains.annotations.NotNull()
    java.lang.String unidadCantidadTotal, @org.jetbrains.annotations.NotNull()
    java.lang.String fechaInicio, @org.jetbrains.annotations.NotNull()
    java.lang.String fechaFin, boolean usoCronico, @org.jetbrains.annotations.NotNull()
    java.lang.String notas, @org.jetbrains.annotations.NotNull()
    java.lang.String dosis, @org.jetbrains.annotations.NotNull()
    java.lang.String cantidad, @org.jetbrains.annotations.NotNull()
    java.lang.String horario) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}