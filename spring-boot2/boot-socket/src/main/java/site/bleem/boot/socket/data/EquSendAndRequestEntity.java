package site.bleem.boot.socket.data;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EquSendAndRequestEntity {
    private String equIdentity;
    private AtomicInteger seq;
    private ConcurrentHashMap<String, SendSessionEntity> unFinishedCmd;

    public EquSendAndRequestEntity(String equIdentity) {
        this.equIdentity = equIdentity;
        this.unFinishedCmd = new ConcurrentHashMap();
        this.seq = new AtomicInteger();
    }

    public synchronized int newSeq() {
        int currentSeq = this.seq.get();
        if (currentSeq == 255) {
            this.seq.set(0);
            return this.seq.get();
        } else {
            return this.seq.addAndGet(1);
        }
    }

    public synchronized int currentSeq() {
        return this.seq.get();
    }

    public synchronized void reset() {
        this.unFinishedCmd.clear();
        this.seq.set(0);
    }

    public void addCmd(String key, SendSessionEntity cmdEntity) {
        if (!this.unFinishedCmd.containsKey(key)) {
            this.unFinishedCmd.put(key, cmdEntity);
        }

    }

    public SendSessionEntity getCmd(String key) {
        return (SendSessionEntity)this.unFinishedCmd.get(key);
    }

    public void delCmd(String key) {
        this.unFinishedCmd.remove(key);
    }

    public Set<Map.Entry<String, SendSessionEntity>> toArray() {
        return this.unFinishedCmd.entrySet();
    }
}
