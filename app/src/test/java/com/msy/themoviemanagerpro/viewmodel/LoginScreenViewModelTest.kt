package com.msy.themoviemanagerpro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.msy.themoviemanagerpro.MainCoroutineRule
import com.msy.themoviemanagerpro.util.Status
import getOrAwaitValueTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LoginScreenViewModel


    @Before
    fun setup() {
        viewModel = LoginScreenViewModel()
    }

    @Test
    fun `email is invalid`() {
        assertThat(viewModel.mailCheck("a")).isFalse()
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `email is empty`() {
        assertThat(viewModel.mailCheck("")).isFalse()
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `email is valid`() {
        assertThat(viewModel.mailCheck("a@a.com")).isTrue()
    }

    @Test
    fun `password is invalid - empty`() {
        assertThat(viewModel.passwordCheck("")).isFalse()
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().message).isEqualTo("empty")
    }

    @Test
    fun `password is invalid - length`() {
        assertThat(viewModel.passwordCheck("a")).isFalse()
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().message).isEqualTo("length")
    }

    @Test
    fun `password is invalid - contains`() {
        assertThat(viewModel.passwordCheck("aaaaaa")).isFalse()
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().message).isEqualTo("contains")
    }


    @Test
    fun `password is return valid`() {
        assertThat(viewModel.passwordCheck("Aa123456+")).isTrue()
    }

    @Test
    fun `login is return error`() {
        viewModel.login("a","A")
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `login is return success`() {
        viewModel.login("a@a.com","Aa123456+")
        assertThat(viewModel.loginMessage.getOrAwaitValueTest().status).isEqualTo(Status.SUCCESS)
    }


}