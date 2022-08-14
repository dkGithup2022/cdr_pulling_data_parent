package com.dk0124.cdr.app.service.upbit.orderbook;


import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.dto.upbit.orderbook.UpbitOrderbookDto;
import com.dk0124.cdr.entity.upbit.orderbook.UpbitOrderbook;
import com.dk0124.cdr.mapper.upbit.UpbitOrderbookMapper;
import com.dk0124.cdr.repository.upbit.upbitOrderBookRepository.UpbitOrderbookCommonRepository;
import com.dk0124.cdr.repositoryPicker.upbit.UpbitOrderbookRepositoryPicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpbitOrderbookService {

    private final UpbitOrderbookRepositoryPicker upbitOrderbookRepositoryPicker;
    private final UpbitOrderbookMapper upbitOrderbookMapper;
    private final ModelMapper modelMapper;

    public UpbitOrderbookDto save(UpbitOrderbookDto upbitOrderbookDto) throws IllegalArgumentException {
        UpbitOrderbookCommonRepository repository = getRepo(upbitOrderbookDto.getCode());
        return modelMapper.map(repository.save(upbitOrderbookMapper.mapOrderbook(upbitOrderbookDto)),UpbitOrderbookDto.class);
    }
    public UpbitOrderbookDto findByCodeAndTimestamp(UpbitCoinCode code, Long timestamp) throws IllegalArgumentException {
        UpbitOrderbookCommonRepository repository = getRepo(code);
        return modelMapper.map( repository.findOneByTimestamp(timestamp),UpbitOrderbookDto.class ) ;
    }

    private UpbitOrderbookCommonRepository getRepo( String code){
        return  upbitOrderbookRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.fromString(code));
    }
    private UpbitOrderbookCommonRepository getRepo(UpbitCoinCode code){
        return upbitOrderbookRepositoryPicker.getRepositoryFromCode(code);
    }
}
