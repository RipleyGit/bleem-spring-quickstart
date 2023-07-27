package site.bleem.boot.socket.task;

public abstract class DispatchTask implements Runnable {
    protected int taskDispatchIdentity;

    public DispatchTask() {
    }

    public int getTaskDispatchIdentity() {
        return this.taskDispatchIdentity;
    }

    public void setTaskDispatchIdentity(int taskDispatchIdentity) {
        this.taskDispatchIdentity = taskDispatchIdentity;
    }
}
