package com.ucentral.proyectopractica3.ui.familiar

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.familiar.notificaciones.FamiliarNotificacionesActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FamiliarNotificacionesActivityUiTest {

    @Test
    fun componentesBasicos_seMuestran() {
        ActivityScenario.launch(FamiliarNotificacionesActivity::class.java)

        onView(withId(R.id.fragmentFamiliarNotificacionesContainer))
            .check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigationFamiliarNotificaciones))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btnVolverFamiliarNotificaciones))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navegacionBottomNav_funcionaSinErrores() {
        ActivityScenario.launch(FamiliarNotificacionesActivity::class.java)

        onView(withId(R.id.nav_buscar_paciente_notificaciones)).perform(click())
        onView(withId(R.id.nav_agregar_recordatorio_familiar)).perform(click())
        onView(withId(R.id.nav_lista_recordatorios_familiar)).perform(click())
        onView(withId(R.id.nav_historial_recordatorios_familiar)).perform(click())

        onView(withId(R.id.bottomNavigationFamiliarNotificaciones))
            .check(matches(isDisplayed()))
    }

    @Test
    fun botonVolver_cierraActivitySinErrores() {
        val scenario = ActivityScenario.launch(FamiliarNotificacionesActivity::class.java)

        onView(withId(R.id.btnVolverFamiliarNotificaciones)).perform(click())

        scenario.close()
    }
}
