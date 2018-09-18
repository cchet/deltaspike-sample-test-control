package com.gepardec.samples.test.deltaspike.test.control;

import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.jboss.weld.context.ContextNotActiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Thomas Herzog <thomas.herzog@gepardec.com>
 * @since 9/18/18
 */
@RunWith(CdiTestRunner.class)
public class ContextControlTest {

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
    private ContextControl contextControl;

    @Inject
    private RequestScopedBean requestScopedBean;

    @Inject
    private SessionScopedBean sessionScopedBean;

    @Before
    public void before() {
        // Stop all open context before test execution
        contextControl.stopContexts();
    }

    // -- Then --
    @Test(expected = ContextNotActiveException.class)
    public void test_session_context_not_active() {
        // -- Given / When --
        sessionScopedBean.doStuff();
    }

    // -- Then --
    @Test(expected = ContextNotActiveException.class)
    public void test_request_context_not_active() {
        // -- Given / When --
        sessionScopedBean.doStuff();
    }

    @Test
    public void test_session_context_active() {
        // -- Given --
        final String expected = DO_STUFF_RESULT;

        // -- When --
        contextControl.startContext(SessionScoped.class);
        final String actual = sessionScopedBean.doStuff();
        contextControl.stopContext(SessionScoped.class);

        // -- Then --
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_request_context_active() {
        // -- Given --
        final String expected = DO_STUFF_RESULT;

        // -- When --
        contextControl.startContext(RequestScoped.class);
        final String actual = requestScopedBean.doStuff();
        contextControl.stopContext(RequestScoped.class);

        // -- Then --
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test_request_then_session_context_active() {
        // -- Given --
        final String expected = DO_STUFF_RESULT;
        Class<? extends Throwable> expectedExceptionCLass = ContextNotActiveException.class;
        Class<? extends Throwable> actualExceptionRequest = Throwable.class;
        Class<? extends Throwable> actualExceptionSession = Throwable.class;

        // -- When --
        contextControl.startContext(RequestScoped.class);
        final String actualRequest = requestScopedBean.doStuff();
        try {
            sessionScopedBean.doStuff();
        } catch (Throwable t) {
            actualExceptionSession = t.getClass();
        }
        contextControl.stopContext(RequestScoped.class);

        contextControl.startContext(SessionScoped.class);
        final String actualSession = sessionScopedBean.doStuff();
        try {
            requestScopedBean.doStuff();
        } catch (Throwable t) {
            actualExceptionRequest = t.getClass();
        }
        contextControl.stopContext(SessionScoped.class);

        // -- Then --
        Assert.assertEquals(expected, actualRequest);
        Assert.assertEquals(expected, actualSession);
        Assert.assertEquals(expectedExceptionCLass, actualExceptionRequest);
        Assert.assertEquals(expectedExceptionCLass, actualExceptionSession);
    }
}
