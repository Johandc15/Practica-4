package com.ucentral.proyectopractica3.ui

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
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PacienteHomeActivityUiTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * Verifica que la pantalla del paciente cargue todos los componentes básicos.
     */
    @Test
    fun componentesBasicos_seMuestran() {
        val scenario = ActivityScenario.launch(PacienteHomeActivity::class.java)
        scenario.onActivity { } // Esperar a que la Activity esté completamente en RESUMED

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.imageView7)).check(matches(isDisplayed()))

        onView(withId(R.id.textView6))
            .check(matches(withText("Bienvenido, paciente")))

        onView(withId(R.id.btnVerMedicamentos)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRecordatorios)).check(matches(isDisplayed()))
        onView(withId(R.id.btnHistorial)).check(matches(isDisplayed()))
    }

    /**
     * Botón "Ver Medicamentos" debe abrir PacienteMedicamentosActivity.
     */
    @Test
    fun navegarVerMedicamentos_abrePacienteMedicamentosActivity() {
        val scenario = ActivityScenario.launch(PacienteHomeActivity::class.java)
        scenario.onActivity { }

        onView(withId(R.id.btnVerMedicamentos)).perform(click())

        Intents.intended(hasComponent(PacienteMedicamentosActivity::class.java.name))
    }

    /**
     * Botón "Notificaciones y Recordatorios" debe abrir RecordatorioNotificacionesActivity.
     */
    @Test
    fun navegarRecordatorios_abreRecordatorioNotificacionesActivity() {
        val scenario = ActivityScenario.launch(PacienteHomeActivity::class.java)
        scenario.onActivity { }

        onView(withId(R.id.btnRecordatorios)).perform(click())

        Intents.intended(hasComponent(RecordatorioNotificacionesActivity::class.java.name))
    }

    /**
     * Botón "Historial Personal" debe abrir PacienteHistorialActivity.
     */
    @Test
    fun navegarHistorial_abrePacienteHistorialActivity() {
        val scenario = ActivityScenario.launch(PacienteHomeActivity::class.java)
        scenario.onActivity { }

        onView(withId(R.id.btnHistorial)).perform(click())

        Intents.intended(hasComponent(PacienteHistorialActivity::class.java.name))
    }
}
