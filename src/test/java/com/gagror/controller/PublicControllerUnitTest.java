package com.gagror.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

@RunWith(MockitoJUnitRunner.class)
public class PublicControllerUnitTest {

	PublicController instance;

	@Mock
	Model model;

	@Test
	public void about_view() {
		assertEquals("Unexpected view from about()", "about", instance.about(model));
	}

	@Before
	public void createInstance() {
		instance = new PublicController();
	}
}
