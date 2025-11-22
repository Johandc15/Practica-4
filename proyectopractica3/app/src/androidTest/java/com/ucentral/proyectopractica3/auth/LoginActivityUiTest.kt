package com.ucentral.proyectopractica3.auth

import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ucentral.proyectopractica3.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityUiTest {

    // ========= MATCHER AUXILIAR PARA INPUTTYPE =========
    private fun withInputType(expected: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("EditText con inputType = $expected")
            }

            override fun matchesSafely(view: View): Boolean {
                return view is EditText && view.inputType == expected
            }
        }
    }

    /**
     * Verifica que al abrir LoginActivity se muestren los componentes básicos.
     */
    @Test
    fun componentesBasicos_seMuestranAlIniciar() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        onView(withId(R.id.textView2)).check(matches(withText("Iniciar sesión")))

        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))

        onView(withId(R.id.btnCopyPass)).check(matches(isDisplayed()))
        onView(withId(R.id.btnTogglePass)).check(matches(isDisplayed()))

        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.tvGoToRegister)).check(matches(isDisplayed()))
        onView(withId(R.id.tvForgotPassword)).check(matches(isDisplayed()))
    }

    /**
     * Verifica que el botón de ver/ocultar contraseña cambie el inputType del EditText.
     */
    @Test
    fun togglePassword_cambiaVisibilidad() {
        ActivityScenario.launch(LoginActivity::class.java)

        // Estado inicial: contraseña oculta
        onView(withId(R.id.etPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
        )

        // Pulsar el ojo -> contraseña visible
        onView(withId(R.id.btnTogglePass)).perform(click())

        onView(withId(R.id.etPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                )
            )
        )

        // Pulsar de nuevo -> vuelve a ocultarse
        onView(withId(R.id.btnTogglePass)).perform(click())

        onView(withId(R.id.etPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
        )
    }

    /**
     * Copiar contraseña sin texto: solo validamos que el botón es clicable
     * y no genera errores (smoke test).
     */
    @Test
    fun copyPassword_sinTexto_noRevienta() {
        ActivityScenario.launch(LoginActivity::class.java)

        // Aseguramos que el campo esté vacío
        onView(withId(R.id.etPassword)).perform(clearText(), closeSoftKeyboard())

        // Clic en copiar (debería mostrar Toast "No hay contraseña para copiar")
        onView(withId(R.id.btnCopyPass)).perform(click())

        // Seguimos en la misma pantalla
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    /**
     * Ir al registro debe abrir RegisterActivity.
     */
    @Test
    fun irARegistro_abreRegisterActivity() {
        ActivityScenario.launch(LoginActivity::class.java)

        Intents.init()
        try {
            onView(withId(R.id.tvGoToRegister)).perform(click())
            Intents.intended(hasComponent(RegisterActivity::class.java.name))
        } finally {
            Intents.release()
        }
    }

    /**
     * Olvidó contraseña sin escribir identificador: se mantiene en LoginActivity.
     */
    @Test
    fun forgotPassword_sinDatos_seMantieneEnLogin() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.etEmail)).perform(clearText(), closeSoftKeyboard())
        onView(withId(R.id.tvForgotPassword)).perform(click())

        // Seguimos viendo el botón de login
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    /**
     * ESQUELETO de login exitoso.
     * Actívalo cuando tengas un usuario de pruebas en Firebase
     * y el documento en "usuarios" con su tipoUsuario.
     */
    @Test
    @Ignore("Quitar @Ignore cuando tengas usuario de pruebas configurado en Firebase")
    fun loginExitoso_redirigeALaPantallaSegunRol() {
        val correoPruebas = "tester@demo.com"
        val passwordPruebas = "Password123*"

        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.etEmail))
            .perform(typeText(correoPruebas), closeSoftKeyboard())
        onView(withId(R.id.etPassword))
            .perform(typeText(passwordPruebas), closeSoftKeyboard())

        Intents.init()
        try {
            onView(withId(R.id.btnLogin)).perform(click())

            // 🚨 Elige UNA según el tipoUsuario del usuario de pruebas:
            // Intents.intended(hasComponent(PacienteHomeActivity::class.java.name))
            // Intents.intended(hasComponent(DoctorHomeActivity::class.java.name))
            // Intents.intended(hasComponent(FamiliarHomeActivity::class.java.name))
        } finally {
            Intents.release()
        }
    }
}
