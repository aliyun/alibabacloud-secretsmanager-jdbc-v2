package com.aliyun.kms.secretsmanager.v2;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class MariaDBSecretsManagerDriverTest {

    @Test
    public void testContactUrl() {
        MariaDBSecretsManagerDriver driver = new MariaDBSecretsManagerDriver();
        assertEquals("jdbc:mariadb://host:3306/db", driver.contactUrl("host", "3306", "db"));
        assertEquals("jdbc:mariadb://host/db", driver.contactUrl("host", null, "db"));
        assertEquals("jdbc:mariadb://host:3306", driver.contactUrl("host", "3306", null));
        assertEquals("jdbc:mariadb://host", driver.contactUrl("host", null, null));
    }

    @Test
    public void testGetRealDriverClass() {
        MariaDBSecretsManagerDriver driver = new MariaDBSecretsManagerDriver();
        assertEquals("org.mariadb.jdbc.Driver", driver.getRealDriverClass());
    }

    @Test
    public void testIsAuthError() {
        MariaDBSecretsManagerDriver driver = new MariaDBSecretsManagerDriver();
        SQLException authError = new SQLException("access denied", "28000", 1045);
        assertTrue(driver.isAuthError(authError));

        SQLException otherError = new SQLException("syntax error", "42000", 1064);
        assertFalse(driver.isAuthError(otherError));

        assertFalse(driver.isAuthError(new RuntimeException("other")));
    }
}
