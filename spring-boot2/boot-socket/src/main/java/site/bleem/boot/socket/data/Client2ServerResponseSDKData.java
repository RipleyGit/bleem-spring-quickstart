package site.bleem.boot.socket.data;

public class Client2ServerResponseSDKData {
    private BaseSDKData baseSDKData;

    public Client2ServerResponseSDKData(BaseSDKData baseSDKData) {
        this.baseSDKData = baseSDKData;
    }

    public BaseSDKData getBaseSDKData() {
        return this.baseSDKData;
    }

    public byte getResponseFlag() {
        return this.baseSDKData.getInnerData()[this.baseSDKData.getInnerData().length - 3];
    }

    public boolean responseSucceed() {
        return this.getResponseFlag() == -128;
    }
}