package com.dk0124.cdr.app.service.upbit.tick;


import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.dto.upbit.tick.UpbitTickDto;
import com.dk0124.cdr.entity.upbit.orderbook.UpbitOrderbook;
import com.dk0124.cdr.entity.upbit.tick.UpbitTick;
import com.dk0124.cdr.mapper.upbit.UpbitTickMapper;
import com.dk0124.cdr.repository.upbit.upbitOrderBookRepository.UpbitOrderbookCommonRepository;
import com.dk0124.cdr.repository.upbit.upbitTickRepository.UpbitTickRepository;
import com.dk0124.cdr.repositoryPicker.upbit.UpbitTickRepositoryPicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpbitTickService {

    private final UpbitTickRepositoryPicker upbitTickRepositoryPicker;
    private final UpbitTickMapper upbitTickMapper;
    private final ModelMapper modelMapper;

    public UpbitTickDto save(UpbitTickDto upbitTickDto) throws IllegalArgumentException{
        UpbitTickRepository repository = getRepo(upbitTickDto.getCode());
        return modelMapper.map( repository.save(upbitTickMapper.mapTick(upbitTickDto)), UpbitTickDto.class);
    }


    public UpbitTickDto findByCodeAndSequentialId(String code , Long sequentialId ) throws IllegalArgumentException {
        UpbitTickRepository repository = getRepo(code);
        return modelMapper.map(repository.findBySequentialId(sequentialId), UpbitTickDto.class );
    }

    @Transactional
    public UpbitTickDto saveWhenNotExist(UpbitTickDto upbitTickDto) {
        UpbitTickRepository repository = getRepo(upbitTickDto.getCode());
        if (repository.findBySequentialId(upbitTickDto.getSequentialId()) != null)
            return null;
        return save(upbitTickDto);
    }

    public List<UpbitTickDto> findByTimestampBetweenOrderByTimestampDesc(UpbitCoinCode code, Long old, Long now) {
        UpbitTickRepository repository = getRepo(code);
        return repository
                .findByTimestampBetweenOrderByTimestampDesc(old, now)
                .stream()
                .map(t->modelMapper.map(t,UpbitTickDto.class)).collect(Collectors.toList());
    }

    private  UpbitTickRepository getRepo(String code){
        return upbitTickRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.fromString(code));
    }

    private  UpbitTickRepository getRepo(UpbitCoinCode code){
        return upbitTickRepositoryPicker.getRepositoryFromCode(code);
    }

}
