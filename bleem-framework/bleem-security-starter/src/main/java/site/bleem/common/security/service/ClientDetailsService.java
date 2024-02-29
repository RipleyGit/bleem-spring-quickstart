package site.bleem.common.security.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;


public class ClientDetailsService extends JdbcClientDetailsService {
    public ClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Cacheable(
        value = {"mine_oauth:client:details"},
        key = "#clientId",
        unless = "#result == null"
    )
    public ClientDetails loadClientByClientId(String clientId) {
        try {
            return super.loadClientByClientId(clientId);
        } catch (Throwable var3) {
            throw var3;
        }
    }
}