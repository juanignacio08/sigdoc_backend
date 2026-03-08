package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemGraphPieResponse {
    Long id;
    String denomination;
    String abbreviation;
    String color;
    String image;
    Integer value;
    Double percent;

}
