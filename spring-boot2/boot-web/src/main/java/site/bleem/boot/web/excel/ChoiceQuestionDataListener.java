package site.bleem.boot.web.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author yubs
 * @desc todo
 * @date 2023/12/4
 */
public class ChoiceQuestionDataListener extends AbsDataListener {


    private List<LinkedHashMap> recrods;

    public ChoiceQuestionDataListener(Integer courseId,Integer questionType) {
        super(courseId,questionType);
        recrods = new ArrayList<>();
    }

    @Override
    public void invoke(Object list, AnalysisContext analysisContext) {
        LinkedHashMap linkedHashMap = (LinkedHashMap) list;
        recrods.add(linkedHashMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<LinkedHashMap> getRecrods() {
        return recrods;
    }
}
