package com.ucentral.proyectopractica3.ui.paciente

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.RecordatorioNotificacionesActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecordatorioNotificacionesActivityUiTest {

    /**
     * 1) Componentes principales de la Activity
     */
    @Test
    fun componentesBasicos_seMuestran() {
        ActivityScenario.launch(RecordatorioNotificacionesActivity::class.java)

        onView(withId(R.id.fragmentRecordatorioContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationRecordatorio)).check(matches(isDisplayed()))
        onView(withId(R.id.btnVolverRecordatorio)).check(matches(isDisplayed()))
    }

    /**
     * 2) Fragment inicial = AgregarRecordatorioFragment
     *    Validamos todos sus elementos visibles.
     */
    @Test
    fun fragmentInicial_esAgregarRecordatorio_yElementosVisibles() {
        ActivityScenario.launch(RecordatorioNotificacionesActivity::class.java)

        onView(withId(R.id.spinnerMedicamento)).check(matches(isDisplayed()))
        onView(withId(R.id.btnHora)).check(matches(isDisplayed()))
        onView(withId(R.id.etRepeticiones)).check(matches(isDisplayed()))
        onView(withId(R.id.btnGuardarRecordatorio)).check(matches(isDisplayed()))
    }

    /**
     * 3) Navegación → Recordatorio (nav_recordatorio)
     */
    @Test
    fun navegarARecordatorio_cargaAgregarRecordatorioFragment() {
        ActivityScenario.launch(RecordatorioNotificacionesActivity::class.java)

        onView(withId(R.id.nav_recordatorio)).perform(click())

        onView(withId(R.id.spinnerMedicamento)).check(matches(isDisplayed()))
        onView(withId(R.id.btnHora)).check(matches(isDisplayed()))
        onView(withId(R.id.etRepeticiones)).check(matches(isDisplayed()))
        onView(withId(R.id.btnGuardarRecordatorio)).check(matches(isDisplayed()))
    }

    /**
     * 4) Navegación → Notificaciones (nav_notificaciones)
     */
    @Test
    fun navegarANotificaciones_cargaNotificacionesFragment() {
        ActivityScenario.launch(RecordatorioNotificacionesActivity::class.java)

        onView(withId(R.id.nav_notificaciones)).perform(click())

        onView(withId(R.id.layoutNotificaciones))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * 5) Botón Volver
     */
    @Test
    fun botonVolver_cierraActivity() {
        val scenario = ActivityScenario.launch(RecordatorioNotificacionesActivity::class.java)

        onView(withId(R.id.btnVolverRecordatorio)).perform(click())

        scenario.close()
    }
}