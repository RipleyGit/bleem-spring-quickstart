package site.bleem.boot.socket.dispatch;

import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.ConnectionEntity;

public abstract class AbstractDataDispatch {
    public AbstractDataDispatch() {
    }

    public abstract void dispatchHeartData(BaseSDKData var1, ConnectionEntity var2);

    public abstract void dispatchResponseData(BaseSDKData var1, ConnectionEntity var2);

    public abstract void dispatchReportData(BaseSDKData var1, ConnectionEntity var2);
}
