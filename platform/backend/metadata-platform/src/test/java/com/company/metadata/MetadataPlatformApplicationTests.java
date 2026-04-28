package com.company.metadata;

import com.company.metadata.system.domain.repository.AuditLogRepository;
import com.company.metadata.system.domain.repository.DataScopeRepository;
import com.company.metadata.system.domain.repository.OrganizationRepository;
import com.company.metadata.system.domain.repository.PermissionRepository;
import com.company.metadata.system.domain.repository.RoleRepository;
import com.company.metadata.system.domain.repository.UserRepository;
import com.company.metadata.metadata.domain.repository.MetadataAssetRepository;
import com.company.metadata.metadata.domain.repository.MetadataFieldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
                + "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
        "metadata.system.persistence.enabled=false",
        "metadata.metadata.persistence.enabled=false"
})
/**
 * 系统基础模块组件，支撑系统管理能力。
 *
 * 作者：Punjab
 */
class MetadataPlatformApplicationTests {

    @MockBean
    private AuditLogRepository auditLogRepository;

    @MockBean
    private DataScopeRepository dataScopeRepository;

    @MockBean
    private OrganizationRepository organizationRepository;

    @MockBean
    private PermissionRepository permissionRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MetadataAssetRepository metadataAssetRepository;

    @MockBean
    private MetadataFieldRepository metadataFieldRepository;

    @Test
    void contextLoads() {
    }
}
