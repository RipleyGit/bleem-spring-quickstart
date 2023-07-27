package site.bleem.boot.socket.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(
    prefix = "standard-connect.tcp"
)
@ConditionalOnProperty(
    name = {"standard-connect.tcp.enable"},
    havingValue = "true"
)
public class TcpProperties {
    private List<Integer> bindPorts;

    public TcpProperties() {
    }

    public List<Integer> getBindPorts() {
        return this.bindPorts;
    }

    public void setBindPorts(List<Integer> bindPorts) {
        this.bindPorts = bindPorts;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TcpProperties)) {
            return false;
        } else {
            TcpProperties other = (TcpProperties)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$bindPorts = this.getBindPorts();
                Object other$bindPorts = other.getBindPorts();
                if (this$bindPorts == null) {
                    if (other$bindPorts != null) {
                        return false;
                    }
                } else if (!this$bindPorts.equals(other$bindPorts)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof TcpProperties;
    }

}