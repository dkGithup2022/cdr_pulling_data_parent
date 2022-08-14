package com.dk0124.cdr.app.dependencyTest;


import com.dk0124.cdr.app.tags.IntegrationWithContainer;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.entity.upbit.candle.UpbitCandle;
import com.dk0124.cdr.entity.upbit.candle.UpbitCandleFactory;
import com.dk0124.cdr.repository.upbit.upbitCandleRepository.UpbitCandleCommonJpaInterface;
import com.dk0124.cdr.repositoryPicker.upbit.UpbitCandleRepositoryPicker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationWithContainer
public class ModuleDependency {

    @Container
    static PostgreSQLContainer container = new PostgreSQLContainer().withDatabaseName("studyTest");

    @Autowired
    UpbitCandleRepositoryPicker upbitTickRepositoryPicker;

    @Test
    void empty(){ assertNotNull( upbitTickRepositoryPicker);}

    @ParameterizedTest
    @DisplayName("주입받은 의존성 기능 테스트 ")
    @MethodSource("get_each_typed_upbit_candles")
    void testRepository(UpbitCandle c){
        UpbitCandleCommonJpaInterface repository = upbitTickRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.fromString(c.getMarket()));
        UpbitCandle saved = repository.save(c);
        assertNotNull(saved);
        assertEquals(c.getClass(),saved.getClass());
    }

    static Stream<Arguments> get_each_typed_upbit_candles() {
        UpbitCoinCode[] codes = UpbitCoinCode.values();
        UpbitCandle[] candles = new UpbitCandle[codes.length];
        for(int i =0 ; i< candles.length ; i++){
            UpbitCandle c = UpbitCandle.builder()
                    .market(codes[i].toString())
                    .timestamp(Long.valueOf(i))
                    .build();
            candles[i] = UpbitCandleFactory.of(c);
        }
        return Arrays.stream(candles).map(c->Arguments.of(c));
    }
}
