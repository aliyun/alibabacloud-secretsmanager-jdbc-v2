package com.aliyun.kms.secretsmanager.v2;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class PostgreSQLSecretsManagerDriverTest {

    @Test
    public void testContactUrl() {
        PostgreSQLSecretsManagerDriver driver = new PostgreSQLSecretsManagerDriver();
        assertEquals("jdbc:postgresql://host:5432/db", driver.contactUrl("host", "5432", "db"));
        assertEquals("jdbc:postgresql://host/db", driver.contactUrl("host", null, "db"));
        assertEquals("jdbc:postgresql://host:5432", driver.contactUrl("host", "5432", null));
        assertEquals("jdbc:postgresql://host", driver.contactUrl("host", null, null));
    }

    @Test
    public void testGetRealDriverClass() {
        PostgreSQLSecretsManagerDriver driver = new PostgreSQLSecretsManagerDriver();
        assertEquals("org.postgresql.Driver", driver.getRealDriverClass());
    }

    @Test
    public void testIsAuthError() {
        PostgreSQLSecretsManagerDriver driver = new PostgreSQLSecretsManagerDriver();
        SQLException authError = new SQLException("access denied", "28P01");
        assertTrue(driver.isAuthError(authError));

        SQLException otherError = new SQLException("syntax error", "42601");
        assertFalse(driver.isAuthError(otherError));

        assertFalse(driver.isAuthError(new RuntimeException("other")));
    }
}
