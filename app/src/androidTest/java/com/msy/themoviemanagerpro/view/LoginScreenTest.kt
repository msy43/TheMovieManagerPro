package com.msy.themoviemanagerpro.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.ui.FragmentFactory
import com.msy.themoviemanagerpro.ui.fragment.LoginScreen
import com.msy.themoviemanagerpro.util.Status
import com.msy.themoviemanagerpro.viewmodel.LoginScreenViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import launchFragmentInHiltContainer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class LoginScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    lateinit var testViewModel: LoginScreenViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        testViewModel = LoginScreenViewModel()
    }

    @Test
    fun pressLoginAndCheckSuccess() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<LoginScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }
        Espresso.onView(ViewMatchers.withId(R.id.emailTextInput)).perform(ViewActions.replaceText("a@a.com"))
        Espresso.onView(ViewMatchers.withId(R.id.passwordTextInput)).perform(ViewActions.replaceText("Aaaaaa1+"))

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())

        assertThat(testViewModel.loginMessage.getOrAwaitValue().status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun pressLoginAndCheckError() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<LoginScreen>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }
        Espresso.onView(ViewMatchers.withId(R.id.emailTextInput)).perform(ViewActions.replaceText(""))
        Espresso.onView(ViewMatchers.withId(R.id.passwordTextInput)).perform(ViewActions.replaceText(""))

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())

        assertThat(testViewModel.loginMessage.getOrAwaitValue().status).isEqualTo(Status.ERROR)
    }

}