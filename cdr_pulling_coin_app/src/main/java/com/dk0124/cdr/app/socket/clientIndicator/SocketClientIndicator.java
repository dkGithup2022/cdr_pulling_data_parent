package com.dk0124.cdr.app.socket.clientIndicator;

import com.dk0124.cdr.constants.coinCode.CoinCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketClientIndicator {
    // List 로
    private CoinCode[] coins;
    private String sessionId;

    public String coinsToString(){
        String result = "";
        for (CoinCode code : coins){
            result += code.toString() + ",";
        }
        return result.substring(0,result.length()-1);
    }

    public CoinCode[] getCoins(){
        return coins;
    }
}
