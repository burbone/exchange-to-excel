package com.Exchange.Exchange.service.exel;

import com.Exchange.Exchange.service.exel.Connecter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClearAllData {

    @Autowired
    private KlineBybitConnect bybitConnect;
    @Autowired
    private KlineOKXConnect okxConnect;
    @Autowired
    private KlineBitgetConnect bitgetConnect;
    @Autowired
    private KlineKucoinConnect kucoinConnect;
    //добавить новую биржу


    public void clearAllData() {
        if (bybitConnect != null) {
            bybitConnect.clearBybit();
        }
        if(okxConnect != null) {
            okxConnect.clearOKX();
        }
        if(bitgetConnect != null) {
            bitgetConnect.clearBitget();
        }
        if(kucoinConnect != null) {
            kucoinConnect.clearKucoin();
        }
        //добавить биржу
    }
}
