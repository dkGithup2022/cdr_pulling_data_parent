package com.dk0124.cdr.constants.coinCode.UpbitCoinCode;

import com.dk0124.cdr.constants.coinCode.CoinCode;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum UpbitCoinCode implements CoinCode {
    KRW_BTC("KRW-BTC"),
    KRW_XRP("KRW-XRP"),
    KRW_ETH("KRW-ETH"),
    KRW_STX("KRW-STX"),
    KRW_SOL("KRW-SOL"),
    KRW_ADA("KRW-ADA"),
    KRW_DOT("KRW-DOT"),
    KRW_BCH("KRW-BCH"),
    KRW_BAT("KRW-BAT"),
    KRW_AVAX("KRW-AVAX"),
    KRW_ETC("KRW-ETC"),
    KRW_AXS("KRW-AXS"),
    KRW_PLA("KRW-PLA"),
    KRW_SAND("KRW-SAND"),
    KRW_SRM("KRW-SRM"),
    KRW_DOGE("KRW-DOGE"),
    KRW_MANA("KRW-MANA"),
    KRW_FLOW("KRW-FLOW"),
    KRW_BTG("KRW-BTG"),
    KRW_ATOM("KRW-ATOM"),
    KRW_MATIC("KRW-MATIC"),
    KRW_ENJ("KRW-ENJ"),
    KRW_CHZ("KRW-CHZ");

    public static UpbitCoinCode fromString(String str){
        for(UpbitCoinCode code :UpbitCoinCode.values()){
            if(code.name.equals(str))
                return code;
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
