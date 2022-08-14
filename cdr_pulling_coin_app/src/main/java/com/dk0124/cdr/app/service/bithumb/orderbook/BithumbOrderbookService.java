package com.dk0124.cdr.app.service.bithumb.orderbook;


import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.dto.bithumb.orderbook.BithumbOrderbookDto;
import com.dk0124.cdr.entity.bithumb.orderbook.BithumbOrderbook;
import com.dk0124.cdr.mapper.bithumb.BithumbOrderbookMapper;
import com.dk0124.cdr.repository.bithumb.bithumbOrderbookRepository.BithumbOrderbookCommonJpaInterface;
import com.dk0124.cdr.repositoryPicker.bithumb.BithumbOrderbookRepositoryPicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BithumbOrderbookService {

    private final BithumbOrderbookRepositoryPicker bithumbOrderbookRepositoryPicker;
    private final BithumbOrderbookMapper bithumbOrderbookMapper;
    private final ModelMapper modelMapper;

    public BithumbOrderbookDto save(BithumbOrderbookDto bithumbTick) {
        BithumbOrderbookCommonJpaInterface repo = getRepo(bithumbTick.getCode());
        return modelMapper.map(repo.save(bithumbOrderbookMapper.mapOrderbook(bithumbTick)), BithumbOrderbookDto.class);
    }

    public BithumbOrderbookDto findOneByCodeAndDatetime(BithumbCoinCode code, Long datetime) {
        BithumbOrderbookCommonJpaInterface repo = getRepo(code);
        return modelMapper.map(repo.findOneByDatetime(datetime), BithumbOrderbookDto.class);
    }

    private BithumbOrderbookCommonJpaInterface getRepo(String code) {
        return bithumbOrderbookRepositoryPicker.getRepositoryFromCode(BithumbCoinCode.fromString(code));
    }

    private BithumbOrderbookCommonJpaInterface getRepo(BithumbCoinCode code) {
        return bithumbOrderbookRepositoryPicker.getRepositoryFromCode(code);
    }
}
