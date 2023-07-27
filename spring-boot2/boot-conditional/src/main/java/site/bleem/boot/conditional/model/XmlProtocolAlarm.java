package site.bleem.boot.conditional.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "Root")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlProtocolAlarm {
    @XmlElement(name = "AlarmList")
    private List<ProtocolAlarm> alarmList;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "AlarmList")
@XmlAccessorType(XmlAccessType.FIELD)
class ProtocolAlarm {
    @XmlAttribute(name = "Type")
    private String type;
    @XmlAttribute(name = "Key")
    private String key;
    @XmlAttribute(name = "KeyType")
    private String keyType;
    @XmlAttribute(name = "Description")
    private String description;
}
