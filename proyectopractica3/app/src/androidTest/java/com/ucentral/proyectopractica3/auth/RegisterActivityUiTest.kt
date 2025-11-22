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
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityUiTest {

    // ==== Matchers auxiliares ====

    private fun withInputType(expected: Int): Matcher<View> =
        object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("EditText con inputType = $expected")
            }

            override fun matchesSafely(view: View): Boolean {
                return view is EditText && view.inputType == expected
            }
        }

    private fun withAlpha(expected: Float): Matcher<View> =
        object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Con alpha = $expected")
            }
            override fun matchesSafely(view: View): Boolean = view.alpha == expected
        }

    // ========= PRUEBAS ========

    @Test
    fun componentesBasicos_seMuestran() {
        ActivityScenario.launch(RegisterActivity::class.java)

        // EditText principales
        onView(withId(R.id.etNombre))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.etApellido))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.etUsuario))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.etEmail))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.etCedula))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.etEps))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        // Spinners
        onView(withId(R.id.spinnerRh))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.spinnerTipoUsuario))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        // Passwords
        onView(withId(R.id.etPassword))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.etConfirmPassword))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnTogglePass))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnToggleConfirm))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        // TyC
        onView(withId(R.id.cbAceptarTyC))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvTextoTyC))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        // Botón registrar
        onView(withId(R.id.btnRegister))
            .perform(scrollTo())
            .check(matches(isDisplayed()))

        // Ir a login
        onView(withId(R.id.tvIrLogin))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
    }

    @Test
    fun botonRegistro_iniciaDesactivado_ySeActivaConTyC() {
        ActivityScenario.launch(RegisterActivity::class.java)

        onView(withId(R.id.btnRegister)).check(matches(not(isEnabled())))
        onView(withId(R.id.btnRegister)).check(matches(withAlpha(0.5f)))

        onView(withId(R.id.cbAceptarTyC)).perform(click())

        onView(withId(R.id.btnRegister)).check(matches(isEnabled()))
        onView(withId(R.id.btnRegister)).check(matches(withAlpha(1.0f)))
    }

    @Test
    fun togglePasswordPrincipal_funciona() {
        ActivityScenario.launch(RegisterActivity::class.java)

        // Oculta
        onView(withId(R.id.etPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
        )

        // Mostrar
        onView(withId(R.id.btnTogglePass)).perform(click())
        onView(withId(R.id.etPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                )
            )
        )

        // Ocultar
        onView(withId(R.id.btnTogglePass)).perform(click())
        onView(withId(R.id.etPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
        )
    }

    @Test
    fun togglePasswordConfirm_funciona() {
        ActivityScenario.launch(RegisterActivity::class.java)

        onView(withId(R.id.etConfirmPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
        )

        onView(withId(R.id.btnToggleConfirm)).perform(click())
        onView(withId(R.id.etConfirmPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                )
            )
        )

        onView(withId(R.id.btnToggleConfirm)).perform(click())
        onView(withId(R.id.etConfirmPassword)).check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
        )
    }

    @Test
    fun helperPassword_muestraMensajesSegunFuerza() {
        ActivityScenario.launch(RegisterActivity::class.java)

        // Débil
        onView(withId(R.id.etPassword)).perform(typeText("abc"), closeSoftKeyboard())
        onView(withId(R.id.tvPasswordHelper))
            .check(matches(withText(org.hamcrest.CoreMatchers.containsString("Tu contraseña debe tener"))))

        // Fuerte
        onView(withId(R.id.etPassword)).perform(replaceText("Aa1234!"), closeSoftKeyboard())
        onView(withId(R.id.tvPasswordHelper)).check(matches(withText("Contraseña fuerte ✅")))
    }

    @Test
    fun registro_contrasenasNoCoinciden() {
        ActivityScenario.launch(RegisterActivity::class.java)

        // Aceptar TyC (con scroll por si está abajo)
        onView(withId(R.id.cbAceptarTyC))
            .perform(scrollTo(), click())

        // Campos mínimos
        onView(withId(R.id.etNombre)).perform(scrollTo(),
            typeText("Juan"), closeSoftKeyboard())
        onView(withId(R.id.etApellido)).perform(scrollTo(),
            typeText("Perez"), closeSoftKeyboard())
        onView(withId(R.id.etUsuario)).perform(scrollTo(),
            typeText("juanito"), closeSoftKeyboard())
        onView(withId(R.id.etEmail)).perform(scrollTo(),
            typeText("juan@demo.com"), closeSoftKeyboard())
        onView(withId(R.id.etCedula)).perform(scrollTo(),
            typeText("123456789"), closeSoftKeyboard())
        onView(withId(R.id.etEps)).perform(scrollTo(),
            typeText("SURA"), closeSoftKeyboard())

        // Contraseñas (fuerte pero NO coinciden)
        onView(withId(R.id.etPassword)).perform(scrollTo(),
            typeText("Aa1234!"), closeSoftKeyboard())
        onView(withId(R.id.etConfirmPassword)).perform(scrollTo(),
            typeText("Bb1234!"), closeSoftKeyboard())

        // 👇 IMPORTANTE: scrollTo() antes del click
        onView(withId(R.id.btnRegister))
            .perform(scrollTo(), click())

        // Asserts
        onView(withId(R.id.etConfirmPassword))
            .check(matches(hasErrorText("Las contraseñas no coinciden")))

        onView(withId(R.id.tvPasswordHelper))
            .check(matches(withText("Las contraseñas no coinciden")))
    }

    @Test
    fun irALogin_abreLoginActivity() {
        ActivityScenario.launch(RegisterActivity::class.java)

        Intents.init()
        try {
            onView(withId(R.id.tvIrLogin))
                .perform(scrollTo(), click())   // 👈 scroll + click

            Intents.intended(hasComponent(LoginActivity::class.java.name))
        } finally {
            Intents.release()
        }
    }
}