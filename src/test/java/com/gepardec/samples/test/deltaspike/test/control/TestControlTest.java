package com.gepardec.samples.test.deltaspike.test.control;

import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Thomas Herzog <thomas.herzog@gepardec.com>
 * @since 9/18/18
 */
@RunWith(CdiTestRunner.class)
public class TestControlTest {

    private static final String DO_STUFF_RESULT = "doStuff called";

    @RequestScoped
    public static class RequestScopedBean {
        public String doStuff() { return DO_STUFF_RESULT;}
    }

    @SessionScoped
    public static class SessionScopedBean implements Serializable {
        public String doStuff() { return DO_STUFF_RESULT;}
    }

    @Inject
    private RequestScopedBean requestScopedBean;

    @Inject
    private SessionScopedBean sessionScopedBean;

    // -- Then --
    @Test(expected = ContextNotActiveException.class)
    @TestControl(startScopes = RequestScoped.class)
    public void test_session_context_not_active() {
        // -- Given / When --
        sessionScopedBean.doStuff();
    }

    // -- Then --
    @Test(expected = ContextNotActiveException.class)
    @TestControl(startScopes = SessionScoped.class)
    public void test_request_context_not_active() {
        // -- Given / When --
        requestScopedBean.doStuff();
    }

    @Test
    @TestControl(startScopes = {SessionScoped.class})
    public void test_session_context_active() {
        // -- Given --
        final String expected = DO_STUFF_RESULT;

        // -- When --
        final String actual = sessionScopedBean.doStuff();

        // -- Then --
        Assert.assertEquals(expected, actual);
    }

    @Test
    @TestControl(startScopes = {RequestScoped.class})
    public void test_request_context_active() {
        // -- Given --
        final String expected = DO_STUFF_RESULT;

        // -- When --
        final String actual = requestScopedBean.doStuff();

        // -- Then --
        Assert.assertEquals(expected, actual);
    }
}
