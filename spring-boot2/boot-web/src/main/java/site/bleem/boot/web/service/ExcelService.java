package site.bleem.boot.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yubs
 * @desc todo
 * @date 2023/12/1
 */
@Slf4j
@Service
public class ExcelService {

    public List<List<List<Object>>> lists(Object param) {
        List<List<List<Object>>> excelList = new ArrayList<>();
        List<List<Object>> sheet1 =  new ArrayList<>();
        sheet1.add(Arrays.asList("化工安全配置","化工安全配置"));
        sheet1.add(Arrays.asList("课程类型:入厂安全培训","课程类型:入厂安全培训"));
        sheet1.add(Arrays.asList("考试题型：判断题","考试题型：判断题"));
        sheet1.add(Arrays.asList("题目","判断结果"));
        sheet1.add(Arrays.asList("进厂作业是否具备特殊作业证？","对"));
        sheet1.add(Arrays.asList("所有人员可以直接进入危险源区域？","错"));
        sheet1.add(Arrays.asList("备注：1.课程名称、课程类型、考试题型不可修改；2.判断结果只填写对/错","备注：1.课程名称、课程类型、考试题型不可修改；2.判断结果只填写对/错"));
        excelList.add(sheet1);
        List<List<Object>> sheet2 =  new ArrayList<>();
        sheet2.add(Arrays.asList("化工安全配置","化工安全配置","化工安全配置","化工安全配置","化工安全配置","化工安全配置","化工安全配置"));
        sheet2.add(Arrays.asList("课程类型:入厂安全培训","课程类型:入厂安全培训","课程类型:入厂安全培训","课程类型:入厂安全培训","课程类型:入厂安全培训","课程类型:入厂安全培训","课程类型:入厂安全培训"));
        sheet2.add(Arrays.asList("考试题型：多选题","考试题型：多选题","考试题型：多选题","考试题型：多选题","考试题型：多选题","考试题型：多选题","考试题型：多选题"));
        sheet2.add(Arrays.asList("题目","选项A","选项B","选项C","选项D","选项E","正确答案"));
        sheet2.add(Arrays.asList("化工重大危险源有哪些？","有毒有害气体","易燃气体","高出作业","吊装作业场景","吊装作业场景1","AB"));
        sheet2.add(Arrays.asList("化工重大危险源有哪些？","有毒有害气体","易燃气体","高出作业","吊装作业场景","吊装作业场景1","AB"));
        sheet2.add(Arrays.asList("备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC","备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC","备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC","备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC","备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC","备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC","备注：1.课程名称、课程类型、考试题型不可修改；2.答案选项需要按照顺序填写；3.正确答案为多选时，按规范填写AB、ABC"));
        excelList.add(sheet2);
        return excelList;
    }
}
