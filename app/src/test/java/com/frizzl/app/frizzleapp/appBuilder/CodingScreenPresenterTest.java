package com.frizzl.app.frizzleapp.appBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by Noga on 02/08/2018.
 */
public class CodingScreenPresenterTest {
    private CodingScreenPresenter codingScreenPresenter;
    @Mock
    CodingScreenFragment codingScreenFragment;

    @Before
    public void setUp() throws Exception {
        this.codingScreenPresenter = new CodingScreenPresenter(codingScreenFragment);
    }

    @Test
    public void prepareCodeForPresenting_valid_assertLineBreaks() throws Exception {
        String codeBefore = "protected void onCreate(Bundle savedInstanceState) {" +
                "super.onCreate(savedInstanceState);" +
                "}";
        String codeAfter = codingScreenPresenter.prepareCodeForPresenting(codeBefore);
        assertEquals("protected void onCreate(Bundle savedInstanceState) {\n" +
                "super.onCreate(savedInstanceState);\n" +
                "}", codeAfter);
    }

}