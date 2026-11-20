![](https://aliyunsdk-pages.alicdn.com/icons/AlibabaCloud.svg)
# Alibaba Cloud Secrets Manager JDBC Client for Java v2

[![GitHub version](https://badge.fury.io/gh/aliyun%2Falibabacloud-secretsmanager-jdbc-v2.svg)](https://badge.fury.io/gh/aliyun%2Falibabacloud-secretsmanager-jdbc-v2)

The Alibaba Cloud Secrets Manager JDBC Client for Java v2 enables Java developers to easily connect to databases using dynamic RDS or generic credentials stored in Alibaba Cloud Secrets Manager. You can get started in minutes using Maven .
 
 
Read this in other languages: [English](README.md), [简体中文](README.zh-cn.md)
 
- [Alibaba Cloud Secrets Manager Managed RDS Credentials Summary](https://help.aliyun.com/document_detail/194269.html)
- [Alibaba Cloud Secrets Manager Client](https://help.aliyun.com/document_detail/190269.html)

## License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

## Features

* Provide common JDBC drivers enabling simple database connectivity
* Provides database connection pool support through c3p0, dbcp, HikariCP and Druid
* Provides multiple access ways such as Access Key, ECS RAM Role, OIDC to obtain a dynamic RDS or generic credentials
* Provides the custom refresh time interval of the secret

## Requirements

- Your secret must be a dynamic RDS or generic credentials, it is recommended that you use a dynamic RDS or generic credentials with double database accounts
- Java 1.8 or later
- Maven
- If your secret type is Generic, your secret value data format must be json and your secret value data format should meet the following requirements

```json
{
    "AccountName":"<your database account name>",
    "AccountPassword":"<your database account password>"
}
```


## Client Mechanism

Alibaba Cloud Secrets Manager JDBC client for Java v2 periodically obtains the database account and password according to the dynamic RDS or generic credentials. The client creates a database connection with the latest database user name and password every times.

It is the extreme scenario that failure may occur to create database connection.You need to try to create the database connection again. The sample codes are as follows.

  ```Java
import com.aliyun.kms.secretsmanager.v2.MysqlSecretsManagerSimpleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SecretManagerJDBCRetrySample {

    public static void main(String[] args) {
        Connection connect = null;
        try {
            Class.forName("com.aliyun.kms.secretsmanager.v2.MysqlSecretsManagerSimpleDriver");
            connect = DriverManager.getConnection("secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>", "#your-mysql-secret-name#", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlSecretsManagerSimpleDriver.LOGIN_FAILED_CODE) {
                try {
                    // sleep to wait for refresh secret value
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
                try {
                    connect = DriverManager.getConnection("secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>", "#your-mysql-secret-name#", "");
                } catch (SQLException sqlEX) {
                    sqlEX.printStackTrace();
                }
            }
        }
    }
}
  ```


## Install

The recommended way to use the Alibaba Cloud Secrets Manager JDBC Client for Java v2 in your project is to consume it from Maven. Import it as follows:

```xml
<dependency>
      <groupId>com.aliyun</groupId>
      <artifactId>alibabacloud-secretsmanager-jdbc-v2</artifactId>
      <version>2.0.0</version>
</dependency>
```

## Build

Once you check out the code from GitHub, you can build it using Maven. Use the following command to build:

```sh
mvn clean install -DskipTests -Dgpg.skip=true
```

## Support JDBC categories 

Alibaba Cloud Secrets Manager JDBC client v2 supports the following JDBC categories:

- MySQL
- MSSQL
- PostgreSQL  
- MariaDB

## Sample Code

Alibaba Cloud Secrets Manager JDBC client for Java v2 supports following three ways to connect database:

* use JDBC to connect database
* use database connection pool to connect database
* use open source framework about database to connect database

Take MySQL database, c3p0 database connection pool and database open source architecture JDBC template as examples:

### Use JDBC to connect database

#### Use configuration file(secretsmanager.properties) to access KMS([Configuration file setting for details](README_config.md))

   ```properties
# the type of access credentials
credentials_type=ecs_ram_role
# ECS RAM Role name
credentials_role_name=<your role name>
# the region information
cache_client_region_id=[{"regionId":"<regionId>"}]
# When accessing KMS instance gateway, use the following configuration
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
   ```

#### MySQL sample code using JDBC to connect database 

  ```Java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SecretManagerJDBCSample {

    public static void main(String[] args) throws Exception {
        Class.forName("com.aliyun.kms.secretsmanager.v2.MysqlSecretsManagerSimpleDriver");
        Connection connect = null;
        try {
            connect = DriverManager.getConnection("secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>", "#your-mysql-secret-name#","");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
  ```

### Use database connection pool c3p0 to connect database

#### Use configuration file(secretsmanager.properties) to access KMS([Configuration file setting for details](README_config.md))

   ```properties
# the type of access credentials
credentials_type=ecs_ram_role
# ECS RAM Role name
credentials_role_name=<your role name>
# the region information
cache_client_region_id=[{"regionId":"<regionId>"}]
# When accessing KMS instance gateway, use the following configuration
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
   ```

#### Set c3p0 configuration file(c3p0.properties)


  ```properties
c3p0.user=#your-mysql-secret-name#
c3p0.driverClass=com.aliyun.kms.secretsmanager.v2.MysqlSecretsManagerSimpleDriver
c3p0.jdbcUrl=secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>
  
 ```


### use open source framework about database to connect database


#### Use configuration file(secretsmanager.properties) to access KMS([Configuration file setting for details](README_config.md))

   ```properties
# the type of access credentials
credentials_type=ak
# AccessKey ID
credentials_access_key_id=#credentials_access_key_id#
# AccessKey Secret
credentials_access_secret=#credentials_access_secret#
# the region information
cache_client_region_id=[{"regionId":"<regionId>"}]
# When accessing KMS instance gateway, use the following configuration
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
   ```
   
#### Set the following configuration items in spring config file

  ```xml
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
        <property name="driverClass" value="com.aliyun.kms.secretsmanager.v2.MysqlSecretsManagerSimpleDriver" />
        <property name="user" value="#your-mysql-secret-name#" />
        <property name="jdbcUrl" value="secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>" />
        <property name="maxPoolSize" value="500" />
        <property name="minPoolSize" value="5" />
        <property name="initialPoolSize" value="20" />
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate" >
        <property name="dataSource" ref="dataSource" />
    </bean>
  ```

#### Spring Boot (application.properties)

  ```properties
spring.datasource.url=secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>
spring.datasource.username=#your-mysql-secret-name#
spring.datasource.password=
spring.datasource.driver-class-name=com.aliyun.kms.secretsmanager.v2.MysqlSecretsManagerSimpleDriver
  ```

KMS access credentials are still read from `secretsmanager.properties` in the working
directory ([Configuration file setting for details](README_config.md)). Connection pool
parameters use the `spring.datasource.hikari.*` namespace (starter-jdbc defaults to HikariCP).