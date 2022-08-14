package com.dk0124.cdr.constants.coinCode.bithumbCoinCode;

import com.dk0124.cdr.constants.coinCode.CoinCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BithumbCoinCode implements CoinCode {
    KRW_BTC("BTC_KRW"),
    KRW_XRP("XRP_KRW"),
    KRW_ETH("ETH_KRW"),
    KRW_SOL("SOL_KRW"),
    KRW_ADA("ADA_KRW"),
    KRW_DOT("DOT_KRW"),
    KRW_BCH("BCH_KRW"),
    KRW_BAT("BAT_KRW"),
    KRW_AVAX("AVAX_KRW"),
    KRW_ETC("ETC_KRW"),
    KRW_AXS("AXS_KRW"),
    KRW_PLA("PLA_KRW"),
    KRW_SAND("SAND_KRW"),
    KRW_SRM("SRM_KRW"),
    KRW_DOGE("DOGE_KRW"),
    KRW_MANA("MANA_KRW"),
    KRW_BTG("BTG_KRW"),
    KRW_ATOM("ATOM_KRW"),
    KRW_MATIC("MATIC_KRW"),
    KRW_ENJ("ENJ_KRW"),
    KRW_CHZ("CHZ_KRW");


    public static BithumbCoinCode fromString(String code){
        for (BithumbCoinCode b : BithumbCoinCode.values()) {
            if (b.name.equalsIgnoreCase(code)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

    private String getName(){
        return name;
    }

    private String name;
}
