package com.ucentral.proyectopractica3.ui.paciente

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.PacienteHistorialActivity
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PacienteHistorialActivityUiTest {

    /**
     * 1) Confirma que la Activity carga correctamente:
     *    - Contenedor de fragmentos
     *    - Botón volver
     */
    @Test
    fun componentesBasicos_seMuestran() {
        ActivityScenario.launch(PacienteHistorialActivity::class.java)

        onView(withId(R.id.historialContainer))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnVolverHistorial))
            .check(matches(isDisplayed()))
    }

    /**
     * 2) Verifica que el fragmento cargado (HistorialFragment) muestre
     *    el contenedor dinámico donde se insertarán las tarjetas del historial.
     */
    @Test
    fun historialFragment_seCargaCorrectamente() {
        ActivityScenario.launch(PacienteHistorialActivity::class.java)

        // Contenedor principal del historial
        onView(withId(R.id.layoutHistorial))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 3) Verifica que inicialmente el historial esté vacío
     *    (antes de que Firebase cargue datos).
     */
    @Test
    fun historial_inicialmenteVacio() {
        ActivityScenario.launch(PacienteHistorialActivity::class.java)

        // El LinearLayout debe existir (VISIBLE) y no tener hijos aún
        onView(withId(R.id.layoutHistorial))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(hasChildCount(0)))
    }

    /**
     * 4) Botón "← Volver" debe cerrar la Activity sin errores.
     */
    @Test
    fun botonVolver_cierraActivitySinErrores() {
        val scenario = ActivityScenario.launch(PacienteHistorialActivity::class.java)

        onView(withId(R.id.btnVolverHistorial)).perform(click())

        // Smoke test: no ocurrió error
        scenario.close()
    }
}
