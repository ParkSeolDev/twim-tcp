package jpabook.jpashop.db.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel(value = "파일", description = "파일의 상세 정보")
public class FileDTO {
    @ApiModelProperty(value = "파일의 PK")
    private long id;

    @ApiModelProperty(value = "파일명", example = "")
    private String name;
    @ApiModelProperty(value = "생성된 일시", example = "2022-06-30T01:48:24.044+0000")
    private Date createdDate;
    // TODO: 절대경로를 보여주지 않고 파일명만 갖도록
    @ApiModelProperty(value = "파일의 경로", example = "C:/Users/TWIM/Desktop/Git/file/contract.pdf")
    private String filePath;

    public FileDTO(){
    }
    FileDTO(long id, String name, Date createdDate, String filePath){
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.filePath = filePath;
    }
}
