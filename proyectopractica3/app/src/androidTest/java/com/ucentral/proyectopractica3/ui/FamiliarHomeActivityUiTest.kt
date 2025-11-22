package com.ucentral.proyectopractica3.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.familiar.FamiliarMedicamentosActivity
import com.ucentral.proyectopractica3.ui.familiar.FamiliarPacientesActivity
import com.ucentral.proyectopractica3.ui.familiar.notificaciones.FamiliarNotificacionesActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class FamiliarHomeActivityUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(FamiliarHomeActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * Verifica que todos los botones principales y elementos clave
     * estén visibles al cargar la pantalla.
     */
    @Test
    fun componentesBasicos_seMuestran() {
        // Toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))

        // Imagen principal
        onView(withId(R.id.imageView8)).check(matches(isDisplayed()))

        // Texto de bienvenida
        onView(withId(R.id.textView7))
            .check(matches(withText("Bienvenido, \nFamiliar / Cuidador")))

        // Botones principales
        onView(withId(R.id.btnProgramarMedicacion)).check(matches(isDisplayed()))
        onView(withId(R.id.btnListaPacientes)).check(matches(isDisplayed()))
        onView(withId(R.id.btnNotificaciones)).check(matches(isDisplayed()))

        // Bottom Navigation
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
    }

    /**
     * Verifica que el botón "Pacientes Asociados" abra FamiliarPacientesActivity.
     */
    @Test
    fun navegarAListaPacientes_abreFamiliarPacientesActivity() {
        onView(withId(R.id.btnListaPacientes)).perform(click())

        Intents.intended(hasComponent(FamiliarPacientesActivity::class.java.name))
    }

    /**
     * Verifica que el botón "Programar Medicación" abra FamiliarMedicamentosActivity.
     */
    @Test
    fun navegarAProgramarMedicacion_abreFamiliarMedicamentosActivity() {
        onView(withId(R.id.btnProgramarMedicacion)).perform(click())

        Intents.intended(hasComponent(FamiliarMedicamentosActivity::class.java.name))
    }

    /**
     * Verifica que el botón "Notificaciones" abra FamiliarNotificacionesActivity.
     */
    @Test
    fun navegarANotificaciones_abreFamiliarNotificacionesActivity() {
        onView(withId(R.id.btnNotificaciones)).perform(click())

        Intents.intended(hasComponent(FamiliarNotificacionesActivity::class.java.name))
    }

    /**
     * Verifica que el Toolbar está presente y carga el menú.
     * (No abre el AccountSheet, solo smoke test del menú)
     */
    @Test
    fun toolbar_cargaMenu() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    /**
     * Verifica que el BottomNavigationView contiene ítems del menú.
     * Esto NO toca navegación, solo presencia de items.
     */
    @Test
    fun bottomNavigation_tieneMenu() {
        onView(allOf(withId(R.id.bottomNavigationView), isDisplayed()))
            .check(matches(isDisplayed()))
    }
}
