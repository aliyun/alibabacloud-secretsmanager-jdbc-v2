package com.aliyun.kms.secretsmanager.v2;

import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SecretsManagerDriverTest {

    @Test
    public void testUnwrapUrl() throws Exception {
        Method unwrapUrlMethod = SecretsManagerDriver.class.getDeclaredMethod("unwrapUrl", String.class);
        unwrapUrlMethod.setAccessible(true);
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();

        assertEquals("jdbc:mysql://host:3306/db", unwrapUrlMethod.invoke(driver, "secrets-manager:mysql://host:3306/db"));
        assertEquals("jdbc:mysql://host/db", unwrapUrlMethod.invoke(driver, "secrets-manager:mysql://host/db"));
        assertEquals("jdbc:sqlserver://host:1433;DatabaseName=db", unwrapUrlMethod.invoke(driver, "secrets-manager:sqlserver://host:1433;DatabaseName=db"));
    }

    @Test
    public void testUnwrapUrlInvalid() throws Exception {
        Method unwrapUrlMethod = SecretsManagerDriver.class.getDeclaredMethod("unwrapUrl", String.class);
        unwrapUrlMethod.setAccessible(true);
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        try {
            unwrapUrlMethod.invoke(driver, "jdbc:mysql://host:3306/db");
            fail("Expected IllegalArgumentException");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testAcceptsURLWithSchema() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        assertTrue(driver.acceptsURL("secrets-manager:mysql://host:3306/db"));
    }

    @Test
    public void testAcceptsURLWithJdbcPrefix() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        assertFalse(driver.acceptsURL("jdbc:mysql://host:3306/db"));
    }

    @Test
    public void testAcceptsURLWithPlainUrl() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        assertTrue(driver.acceptsURL("http://example.com"));
    }

    @Test
    public void testAcceptsURLWithUrlUniqueMatch() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver(null, "mysql");
        assertTrue(driver.acceptsURL("secrets-manager:mysql://host:3306/db?urlUnique=mysql"));
    }

    @Test
    public void testAcceptsURLWithUrlUniqueMismatch() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver(null, "postgresql");
        assertFalse(driver.acceptsURL("secrets-manager:mysql://host:3306/db?urlUnique=mysql"));
    }
}
