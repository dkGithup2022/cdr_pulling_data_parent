package com.dk0124.cdr.app.service.upbit.candle;


import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.dto.upbit.candle.UpbitCandleDto;
import com.dk0124.cdr.entity.upbit.candle.UpbitCandle;
import com.dk0124.cdr.mapper.upbit.UpbitCandleMapper;
import com.dk0124.cdr.repository.upbit.upbitCandleRepository.UpbitCandleCommonJpaInterface;
import com.dk0124.cdr.repositoryPicker.upbit.UpbitCandleRepositoryPicker;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpbitCandleService {
    private final UpbitCandleRepositoryPicker upbitCandleRepositoryPicker;
    private final UpbitCandleMapper upbitCandleMapper;
    private final ModelMapper modelMapper;


    public UpbitCandleDto save(UpbitCandleDto candleDto) throws IllegalArgumentException{
        UpbitCandleCommonJpaInterface repo = getRepo(candleDto.getMarket());
        return modelMapper.map( repo.save(upbitCandleMapper.mapCandle(candleDto)) , UpbitCandleDto.class);
    }

    public UpbitCandleDto findOneByMarketAndTimestamp(UpbitCoinCode code, Long timestamp) throws IllegalArgumentException {
        UpbitCandleCommonJpaInterface repo = getRepo(code);
        return  modelMapper.map(repo.findOneByTimestamp(timestamp), UpbitCandleDto.class );
    }

    public UpbitCandleDto safeSave(UpbitCandleDto candle) {
        if (findOneByMarketAndTimestamp( UpbitCoinCode.fromString(candle.getMarket()), candle.getTimestamp()) == null) {
            return save(candle);
        } else {
            return null;
        }
    }

    private UpbitCandleCommonJpaInterface getRepo(String code){
        return upbitCandleRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.fromString(code));
    }


    private UpbitCandleCommonJpaInterface getRepo(UpbitCoinCode code){
        return upbitCandleRepositoryPicker.getRepositoryFromCode(code);

    }


}
