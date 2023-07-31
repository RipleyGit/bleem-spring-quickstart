package site.bleem.boot.socket.server;

public interface ServerExecutor {
    void start() throws Exception;

    void stop() throws Exception;

    boolean isStarted();
}
