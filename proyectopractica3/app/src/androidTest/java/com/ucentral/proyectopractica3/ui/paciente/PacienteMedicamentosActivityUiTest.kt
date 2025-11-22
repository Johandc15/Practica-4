package com.ucentral.proyectopractica3.ui.paciente

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.PacienteMedicamentosActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PacienteMedicamentosActivityUiTest {

    /**
     * 1) Prueba que los elementos base de la pantalla existan.
     */
    @Test
    fun componentesBasicos_seMuestran() {
        ActivityScenario.launch(PacienteMedicamentosActivity::class.java)

        onView(withId(R.id.btnVolverPaciente)).check(matches(isDisplayed()))
        onView(withId(R.id.fragmentContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
    }

    /**
     * 2) Al iniciar, el fragmento por defecto debe ser ListaMedicamentosFragment.
     *    Validamos por el ID: layoutListaMedicamentos
     */
    @Test
    fun fragmentInicial_esListaMedicamentos() {
        ActivityScenario.launch(PacienteMedicamentosActivity::class.java)

        onView(withId(R.id.layoutListaMedicamentos))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 3) Navegar a "Agregar medicamento" (nav_agregar) debe cargar el fragment correcto.
     *    Validamos usando: tvTituloAgregarMed y etNombre
     */
    @Test
    fun navegarAAgregar_cargaAgregarMedicamentoFragment() {
        ActivityScenario.launch(PacienteMedicamentosActivity::class.java)

        onView(withId(R.id.nav_agregar)).perform(click())

        onView(withId(R.id.tvTituloAgregarMed)).check(matches(isDisplayed()))
        onView(withId(R.id.etNombre)).check(matches(isDisplayed()))
        onView(withId(R.id.btnGuardarMedicamento)).check(matches(isDisplayed()))
    }

    /**
     * 4) Navegar a "Lista" debe recargar ListaMedicamentosFragment.
     */
    @Test
    fun navegarALista_cargaListaMedicamentosFragment() {
        ActivityScenario.launch(PacienteMedicamentosActivity::class.java)

        // Primero navega a agregar para cambiar el fragment
        onView(withId(R.id.nav_agregar)).perform(click())

        // Ahora a LISTA
        onView(withId(R.id.nav_lista)).perform(click())

        onView(withId(R.id.layoutListaMedicamentos))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 5) Navegar a "Config" debe cargar ConfiguracionMedicamentoFragment.
     *    Validamos por: tvTituloConfiguracionMed y layoutConfiguracion
     */
    @Test
    fun navegarAConfig_cargaConfiguracionMedicamentoFragment() {
        ActivityScenario.launch(PacienteMedicamentosActivity::class.java)

        onView(withId(R.id.nav_config)).perform(click())

        onView(withId(R.id.tvTituloConfiguracionMed))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.layoutConfiguracion))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 6) Botón "← Volver" debe cerrar la Activity (finish())
     */
    @Test
    fun botonVolver_cierraActivity() {
        val scenario = ActivityScenario.launch(PacienteMedicamentosActivity::class.java)

        onView(withId(R.id.btnVolverPaciente)).perform(click())

        scenario.close() // smoke test: no debe lanzar errores
    }
}