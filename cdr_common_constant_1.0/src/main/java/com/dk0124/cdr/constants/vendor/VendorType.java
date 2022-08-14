package com.dk0124.cdr.constants.vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VendorType {
    UPBIT("upbit"),
    BITHUMB("bithumb");

    public String name;
}
