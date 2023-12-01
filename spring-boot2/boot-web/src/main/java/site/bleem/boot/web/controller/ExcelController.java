package site.bleem.boot.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.bleem.boot.web.service.ExcelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExcelController {

    @Resource
    private ExcelService excelService;


    /**
     * 课程附件上传
     *
     * @param courseFile
     * @return
     */
    @PostMapping(value = "/upload/xlsx", consumes = {"multipart/form-data"})
    public void uploadXlsx(MultipartFile courseFile) {
        //基础校验
        if (courseFile == null || courseFile.isEmpty() || courseFile.getSize() == 0) {
            throw new RuntimeException("文件不能为空！");
        }
        String originalFilename = courseFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名称不能为空！");
        }
        String[] split = originalFilename.split("\\.");
        String fileSuffix = split[split.length - 1];
        if (!"xlsx".equals(fileSuffix)) {
            throw new RuntimeException("仅支持上传pdf和MP4格式文件！");
        }
    }

    @PostMapping("/export/xlsx")
    public void exportForbidden(HttpServletResponse response, @RequestBody Object param) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("学习记录" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 设置取消分页
//            if (CollectionUtils.isEmpty(param.getIds())){
//                param.setPageIndex(-1);
//                param.setPageSize(-1);
//            }
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            List<List<List<Object>>> dataListList = excelService.lists(param);

            for (int sheetIndex = 0; sheetIndex < dataListList.size(); sheetIndex++) {
                List<List<Object>> dataList = dataListList.get(sheetIndex);
                int size = dataList.get(0).size();
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetIndex, "Sheet" + sheetIndex)
                        .registerWriteHandler(new CustomMergeStrategy(size))
//                        .head(dataListList.get(sheetIndex).get(0).getClass())
                        .build();
                // 设置样式策略，包括边框和自动调整列宽
//                HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(getContentCellStyle(excelWriter.writeWorkbook), getHeadCellStyle(excelWriter.writeWorkbook));
//                writeSheet.setTableStyle(styleStrategy);
                excelWriter.write(dataList, writeSheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
    // 获取内容单元格样式，包括边框
    private WriteCellStyle getContentCellStyle(Workbook workbook) {
        WriteCellStyle contentCellStyle = new WriteCellStyle();
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        contentCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return contentCellStyle;
    }

    // 获取表头单元格样式，包括边框
    private WriteCellStyle getHeadCellStyle(Workbook workbook) {
        WriteCellStyle headCellStyle = new WriteCellStyle();
        headCellStyle.setBorderBottom(BorderStyle.THIN);
        headCellStyle.setBorderTop(BorderStyle.THIN);
        headCellStyle.setBorderLeft(BorderStyle.THIN);
        headCellStyle.setBorderRight(BorderStyle.THIN);
        headCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headCellStyle;
    }
}