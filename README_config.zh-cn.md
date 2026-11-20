# 阿里云托管凭据客户端v2配置文件设置

在程序运行目录下，通过配置文件（secretsmanager.properties）构建客户端：

1. 采用阿里云ECS Ram Role作为访问鉴权方式

```properties
# 访问凭据类型
credentials_type=ecs_ram_role
# ECS RAM Role名称
credentials_role_name=<your role name>
# 关联的KMS服务地域
cache_client_region_id=[{"regionId":"<regionId>"}]
# 访问KMS实例网关时，使用如下配置
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
```

2. 采用阿里云OIDC Role ARN作为访问鉴权方式

```properties
# 访问凭据类型
credentials_type=oidc_role_arn
# 角色ARN (可选；不填则默认使用环境变量 ALIBABA_CLOUD_ROLE_ARN)
credentials_role_arn=<role arn>
# OIDC提供者ARN (可选；不填则默认使用环境变量 ALIBABA_CLOUD_OIDC_PROVIDER_ARN)
credentials_oidc_provider_arn=<oidc provider arn>
# OIDC令牌文件路径 (可选；不填则默认使用环境变量 ALIBABA_CLOUD_OIDC_TOKEN_FILE)
credentials_oidc_token_file_path=<oidc token file path>
# 关联的KMS服务地域
cache_client_region_id=[{"regionId":"<regionId>"}]
# 访问KMS实例网关时，使用如下配置
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
```

三字段的"可选"以对应环境变量存在为前提：`credentials_role_arn` ↔ `ALIBABA_CLOUD_ROLE_ARN`、
`credentials_oidc_provider_arn` ↔ `ALIBABA_CLOUD_OIDC_PROVIDER_ARN`、
`credentials_oidc_token_file_path` ↔ `ALIBABA_CLOUD_OIDC_TOKEN_FILE`。配置文件与环境变量两处
都取不到值时，客户端构造直接抛 `IllegalArgumentException`（例如
`RoleArn or environment variable ALIBABA_CLOUD_ROLE_ARN cannot be empty.`）。

ACK 环境不会自动提供这三个环境变量：标准 RRSA 配置需要把它们与投影的 ServiceAccount Token
一起写进 Pod spec。如果不注入，就必须在上面的 `secretsmanager.properties` 里把三个字段都填上。

3. 采用阿里云AK SK作为访问鉴权方式

```properties
# 访问凭据类型
credentials_type=ak
# AK
credentials_access_key_id=<access key id>
# SK
credentials_access_secret=<access key secret>
# 关联的KMS服务地域
cache_client_region_id=[{"regionId":"<regionId>"}]
# 访问KMS实例网关时，使用如下配置
# cache_client_region_id=[{"regionId":"<regionId>","endpoint":"<you kms instanceId>.cryptoservice.kms.aliyuncs.com"}]
```

4. 使用阿里云默认凭据链

`secretsmanager.properties` 中不配置 `credentials_type`（或该文件不存在）时，客户端通过
阿里云默认凭据链解析凭据（`ALIBABA_CLOUD_ACCESS_KEY_ID` / `ALIBABA_CLOUD_ACCESS_KEY_SECRET`
等环境变量、实例 RAM 角色等）。地域信息仍必须提供：

```properties
# 不配置 credentials_type（阿里云默认凭据链）
cache_client_region_id=[{"regionId":"<regionId>"}]
```

5. 自定义凭据刷新周期（可选）

```properties
# 凭据缓存刷新周期，单位毫秒
# 默认 6 小时（21600000）；最小 5 分钟（300000）；小于等于 0 时使用默认值
refresh_secret_ttl=21600000
```

凭据在凭据管家侧配置了轮转周期时，以服务端轮转周期为准，`refresh_secret_ttl` 不生效。