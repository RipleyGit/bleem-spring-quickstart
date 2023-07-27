package site.bleem.boot.socket.container;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.data.ConnectionEntity;
import site.bleem.boot.socket.enums.ConnectProtocolType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NettyRpcClientContainer {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcClientContainer.class);
    private ConcurrentHashMap<String, HashMap<String, ConnectionEntity>> addressKeyMap = new ConcurrentHashMap();
    private ConcurrentHashMap<String, ConnectionEntity> equIdentityKeyMap = new ConcurrentHashMap();

    public NettyRpcClientContainer() {
    }

    public Set<Map.Entry<String, ConnectionEntity>> toArray() {
        return this.equIdentityKeyMap.entrySet();
    }

    public ConnectionEntity add(String equIdentity, Channel channel, ConnectProtocolType protocolType, String clientAddress, int clientPort) {
        String key = String.format("%s_%s", equIdentity, protocolType.getCode());
        ConnectionEntity entity = (ConnectionEntity)this.equIdentityKeyMap.get(key);
        if (entity == null) {
            ConnectionEntity newEntity = new ConnectionEntity(channel, equIdentity, protocolType, clientAddress, clientPort);
            this.equIdentityKeyMap.put(key, newEntity);
            log.info("设备[{}]链路新增：{} protocolType:{} ", new Object[]{equIdentity, newEntity.getClientAddress(), protocolType});
            if (!this.addressKeyMap.containsKey(newEntity.getClientAddress())) {
                this.addressKeyMap.put(newEntity.getClientAddress(), new HashMap());
            }

            ((HashMap)this.addressKeyMap.get(newEntity.getClientAddress())).put(key, newEntity);
            return newEntity;
        } else {
            if (entity.getProtocolType() == protocolType) {
                if (!entity.getClientAddress().equals(String.format("%s:%s", clientAddress, clientPort)) || !entity.getChannel().equals(channel)) {
                    synchronized(this) {
                        log.info("设备[{}]链路更新：[{}]->[{}:{}] channel:[{}]->[{}]", new Object[]{equIdentity, entity.getClientAddress(), clientAddress, clientPort, entity.getChannel(), channel});
                        if (entity.getChannel().isActive()) {
                            log.info("Channel disconnect, [{}] channel:[{}]", equIdentity, entity.getChannel());
                            entity.getChannel().disconnect().addListener((future) -> {
                                log.info("disconnect isSuccess:{}", future.isSuccess());
                            });
                        }

                        ((HashMap)this.addressKeyMap.get(entity.getClientAddress())).remove(key);
                        entity.setChannel(channel);
                        entity.setIP(clientAddress);
                        entity.setPort(clientPort);
                        entity.initAnalysis();
                        if (!this.addressKeyMap.containsKey(entity.getClientAddress())) {
                            this.addressKeyMap.put(entity.getClientAddress(), new HashMap());
                        }

                        ((HashMap)this.addressKeyMap.get(entity.getClientAddress())).put(key, entity);
                    }
                }
            } else {
                log.info("设备[{}]链路不进行更新：通信协议变更{}->{} 地址{}->{}", new Object[]{equIdentity, entity.getProtocolType().getDesp(), protocolType.getDesp(), entity.getClientAddress(), clientAddress, clientPort});
            }

            return entity;
        }
    }

    public ConnectionEntity find(String equIdentity, ConnectProtocolType protocolType) {
        return (ConnectionEntity)this.equIdentityKeyMap.get(String.format("%s_%s", equIdentity, protocolType.getCode()));
    }

    public ConnectionEntity removeByEquIdentity(String equIdentity, ConnectProtocolType protocolType) {
        ConnectionEntity delConnect = (ConnectionEntity)this.equIdentityKeyMap.remove(String.format("%s_%s", equIdentity, protocolType.getCode()));
        if (delConnect != null) {
            HashMap<String, ConnectionEntity> addressMap = (HashMap)this.addressKeyMap.get(delConnect.getClientAddress());
            if (addressMap != null) {
                if (addressMap.size() == 1) {
                    this.addressKeyMap.remove(delConnect.getClientAddress());
                } else {
                    addressMap.remove(String.format("%s_%s", equIdentity, protocolType.getCode()));
                }
            }
        }

        return delConnect;
    }
}
