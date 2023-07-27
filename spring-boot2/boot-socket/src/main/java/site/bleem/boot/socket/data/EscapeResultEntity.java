package site.bleem.boot.socket.data;

public class EscapeResultEntity {
    private boolean isEscape;
    private byte[] escapedData;

    public EscapeResultEntity() {
    }

    public boolean isEscape() {
        return this.isEscape;
    }

    public byte[] getEscapedData() {
        return this.escapedData;
    }

    public void setEscape(boolean isEscape) {
        this.isEscape = isEscape;
    }

    public void setEscapedData(byte[] escapedData) {
        this.escapedData = escapedData;
    }


    protected boolean canEqual(Object other) {
        return other instanceof EscapeResultEntity;
    }
}
