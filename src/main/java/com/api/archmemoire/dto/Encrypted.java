package com.api.archmemoire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Encrypted {

    private String data;
    private byte[] secretKey;
}
