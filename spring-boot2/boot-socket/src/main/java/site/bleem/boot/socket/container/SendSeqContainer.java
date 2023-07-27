package site.bleem.boot.socket.container;

import site.bleem.boot.socket.data.EquSendAndRequestEntity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SendSeqContainer {
    private ConcurrentHashMap<String, EquSendAndRequestEntity> equSeqContainer = new ConcurrentHashMap();

    public SendSeqContainer() {
    }

    public void del(String equIdentity) {
        this.equSeqContainer.remove(equIdentity);
    }

    public EquSendAndRequestEntity get(String equIdentity) {
        EquSendAndRequestEntity entity1 = (EquSendAndRequestEntity)this.equSeqContainer.get(equIdentity);
        if (entity1 == null) {
            if (!this.equSeqContainer.containsKey(equIdentity)) {
                this.equSeqContainer.put(equIdentity, new EquSendAndRequestEntity(equIdentity));
            }

            return (EquSendAndRequestEntity)this.equSeqContainer.get(equIdentity);
        } else {
            return entity1;
        }
    }

    public Set<Map.Entry<String, EquSendAndRequestEntity>> toArray() {
        return this.equSeqContainer.entrySet();
    }
}
