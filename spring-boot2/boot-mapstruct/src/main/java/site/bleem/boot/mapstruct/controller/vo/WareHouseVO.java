package site.bleem.boot.mapstruct.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WareHouseVO {

    private Long id;

    private String houseName;

    private String houseAddress;

    private String cleanFinishedTime;

    private Integer predictionNum;
}