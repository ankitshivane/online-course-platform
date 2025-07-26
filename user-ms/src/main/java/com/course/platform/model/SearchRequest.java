package com.course.platform.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String name;
    private String email;
    private String roleName;
    private String activityAction;
    private String platform;
    private Integer statusCode;
    private List<Long> referenceId;
    private Date activityDateFrom;
    private Date activityDateTo;

    @Min(value = 1,message = "Page Number must be greater than or equal to 1")
    @Schema(description = "Page Number",example = "1")
    private int pageNumber=1;

    @Max(value = 10, message = "Default page size must be between 2 and 10")
    @Min(value = 2,message = "Default page size must be between 2 and 10")
    @Schema(description = "Page Size",example = "2")
    private int pageSize=2;

}
