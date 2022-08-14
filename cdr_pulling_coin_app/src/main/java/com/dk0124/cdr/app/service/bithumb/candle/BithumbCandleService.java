package com.dk0124.cdr.app.service.bithumb.candle;


import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.dto.bithumb.candle.BithumbCandleDto;
import com.dk0124.cdr.entity.bithumb.candle.BithumbCandle;
import com.dk0124.cdr.mapper.bithumb.BithumbCandleMapper;
import com.dk0124.cdr.repository.bithumb.bithumbCandleRepository.BithumbCandleCommonJpaInterface;
import com.dk0124.cdr.repositoryPicker.bithumb.BithumbCandleRepositoryPicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BithumbCandleService {
    private final BithumbCandleRepositoryPicker bithumbCandleRepositoryPicker;
    private final BithumbCandleMapper bithumbCandleMapper;
    private final ModelMapper modelMapper;

    public BithumbCandleDto save(BithumbCandleDto bithumbCandleDto) throws IllegalArgumentException {
        BithumbCandleCommonJpaInterface repo =getRepo(BithumbCoinCode.fromString(bithumbCandleDto.getCode()));
        return modelMapper.map( repo.save(bithumbCandleMapper.mapCandle(bithumbCandleDto)), BithumbCandleDto.class);
    }

    public  BithumbCandleDto findOneByCodeAndTimestamp(BithumbCoinCode code, Long timestamp) {
        BithumbCandleCommonJpaInterface repo = getRepo(code);
        return modelMapper.map(repo.findOneByTimestamp(timestamp), BithumbCandleDto.class );
    }

    public BithumbCandleDto safeSave(BithumbCandleDto bithumbCandleDto) {
        BithumbCandleCommonJpaInterface repo = getRepo(bithumbCandleDto.getCode());
        if(repo.findOneByTimestamp(bithumbCandleDto.getTimestamp())!=null)
            return null;
        return modelMapper.map( repo.save(bithumbCandleMapper.mapCandle(bithumbCandleDto)), BithumbCandleDto.class);

    }

    private  BithumbCandleCommonJpaInterface  getRepo(BithumbCoinCode code){
        return  bithumbCandleRepositoryPicker.getRepositoryFromCode(code);
    }

    private  BithumbCandleCommonJpaInterface  getRepo(String code){
        return  bithumbCandleRepositoryPicker.getRepositoryFromCode(BithumbCoinCode.fromString(code));
    }
}
