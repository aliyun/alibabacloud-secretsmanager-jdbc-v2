package com.aliyun.kms.secretsmanager.v2;

public class MariaDBSecretManagerSimpleDriver extends MariaDBSecretsManagerDriver {

    static {
        MariaDBSecretManagerSimpleDriver driver = new MariaDBSecretManagerSimpleDriver();
        registerDriver(driver, driver.getRealDriverClass());
    }

    public MariaDBSecretManagerSimpleDriver() {
        super();
    }
}
