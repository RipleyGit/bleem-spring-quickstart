package site.bleem.boot.web.controller;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashSet;

public class CustomMergeStrategy extends AbstractMergeStrategy {
    private Integer size;

    public CustomMergeStrategy(Integer size) {
        this.size = size;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer integer) {
        Row row = cell.getRow();
        if (row.getLastCellNum() < size) {
            return;
        }
// 获取前一行对应列的单元格
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            Cell rowCell = row.getCell(i);
            set.add(rowCell.toString());
        }
        // 判断当前单元格值与前一行是否相同
        if (set.size() == 1) {
            // 合并相邻单元格
            CellRangeAddress cellRangeAddress = new CellRangeAddress(
                    row.getRowNum(),  // 上一行
                    row.getRowNum(),      // 当前行
                    0, // 列
                    size - 1  // 列
            );
            row.getSheet().addMergedRegion(cellRangeAddress);
        }
    }
}
