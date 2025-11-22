package com.ucentral.proyectopractica3.ui.familiar

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.FamiliarHomeActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FamiliarMedicamentosActivityUiTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * 1) Verifica que los elementos básicos existan:
     *    - Botón Volver
     *    - Contenedor de fragments
     *    - BottomNavigation
     */
    @Test
    fun componentesBasicos_seMuestran() {
        ActivityScenario.launch(FamiliarMedicamentosActivity::class.java)

        onView(withId(R.id.btnVolverFamiliarMedicamentos)).check(matches(isDisplayed()))
        onView(withId(R.id.fragmentContainerFamiliarMedicamentos)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationFamiliarMedicamentos)).check(matches(isDisplayed()))
    }

    /**
     * 2) Al abrir la Activity, el fragment inicial debe ser BuscarPacienteFragment.
     *    Lo validamos por la presencia de etCorreoPaciente y btnBuscarPaciente.
     */
    @Test
    fun fragmentInicial_esBuscarPaciente() {
        ActivityScenario.launch(FamiliarMedicamentosActivity::class.java)

        onView(withId(R.id.etCorreoPaciente)).check(matches(isDisplayed()))
        onView(withId(R.id.btnBuscarPaciente)).check(matches(isDisplayed()))
        onView(withId(R.id.tvResultadoPaciente)).check(matches(isDisplayed()))
    }

    /**
     * 3) Navegar a "Agregar medicamento" debe cargar AgregarMedicamentoFamiliarFragment.
     *    Lo validamos usando el título y el campo etNombreMedicamentoFamiliar.
     */
    @Test
    fun navegarAAgregarMedicamento_cargaAgregarMedicamentoFamiliarFragment() {
        ActivityScenario.launch(FamiliarMedicamentosActivity::class.java)

        onView(withId(R.id.nav_agregar_medicamento)).perform(click())

        onView(withId(R.id.tvTituloAgregarMedFamiliar))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.etNombreMedicamentoFamiliar))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.btnGuardarMedicamentoFamiliar))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 4) Navegar a "Lista de medicamentos" debe cargar ListaMedicamentosFamiliarFragment.
     *    Validamos usando el LinearLayout donde se agregan las tarjetas.
     */
    @Test
    fun navegarAListaMedicamentos_cargaListaMedicamentosFamiliarFragment() {
        ActivityScenario.launch(FamiliarMedicamentosActivity::class.java)

        onView(withId(R.id.nav_lista_medicamentos)).perform(click())

        onView(withId(R.id.layoutListaMedicamentosFamiliar))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 5) Navegar a "Configuración" debe cargar ConfiguracionMedicamentosFamiliarFragment.
     *    Validamos usando el título y el contenedor de configuración.
     */
    @Test
    fun navegarAConfiguracion_cargaConfiguracionMedicamentosFamiliarFragment() {
        ActivityScenario.launch(FamiliarMedicamentosActivity::class.java)

        onView(withId(R.id.nav_configuracion_medicamentos)).perform(click())

        onView(withId(R.id.tvTituloConfiguracionMedFamiliar))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.layoutConfiguracionMedicamentosFamiliar))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 6) Botón VOLVER debe regresar a FamiliarHomeActivity.
     */
    @Test
    fun botonVolver_regresaAHomeFamiliar() {
        ActivityScenario.launch(FamiliarMedicamentosActivity::class.java)

        onView(withId(R.id.btnVolverFamiliarMedicamentos)).perform(click())

        Intents.intended(hasComponent(FamiliarHomeActivity::class.java.name))
    }
}
