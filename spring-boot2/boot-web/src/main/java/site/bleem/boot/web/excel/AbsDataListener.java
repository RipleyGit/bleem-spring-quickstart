package site.bleem.boot.web.excel;

import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;

import java.util.List;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/12/4
 */
@Data
public abstract class AbsDataListener extends AnalysisEventListener<Object> {
    private Integer courseId;
    private Integer questionType;

    public AbsDataListener(Integer courseId,Integer questionType){
        this.courseId = courseId;
        this.questionType = questionType;
    }
}
