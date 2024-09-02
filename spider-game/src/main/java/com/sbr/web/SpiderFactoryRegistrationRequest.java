package com.sbr.web;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpiderFactoryRegistrationRequest {
    String pathToClass;
    String classBytesBase64;
}
