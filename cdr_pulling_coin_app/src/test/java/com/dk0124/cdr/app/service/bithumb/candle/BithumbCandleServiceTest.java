package com.dk0124.cdr.app.service.bithumb.candle;

import com.dk0124.cdr.tags.IntegrationWithContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationWithContainer
class BithumbCandleServiceTest {

    @Autowired
    BithumbCandleService  bithumbCandleService;

    @Test
    void empty(){
        assertNotNull(bithumbCandleService);
    }
    @Test
    void save() {

    }

    @Test
    void findOneByCodeAndTimestamp() {
    }

    @Test
    void safeSave() {
    }
}