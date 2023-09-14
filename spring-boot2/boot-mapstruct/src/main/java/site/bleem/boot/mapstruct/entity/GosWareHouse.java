package site.bleem.boot.mapstruct.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GosWareHouse {

    private Long id;

    private String houseName;

    private String houseAddress;

    private LocalDateTime cleanFinishedTime;

    private Integer deviceCount;

    private Integer doStatus;
}