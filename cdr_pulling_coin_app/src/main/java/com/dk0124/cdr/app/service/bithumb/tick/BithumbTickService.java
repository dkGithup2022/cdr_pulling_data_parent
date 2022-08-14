package com.dk0124.cdr.app.service.bithumb.tick;

import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.dto.bithumb.tick.BithumbTickDto;
import com.dk0124.cdr.entity.bithumb.tick.BithumbTick;
import com.dk0124.cdr.mapper.bithumb.BithumbTickMapper;
import com.dk0124.cdr.repository.bithumb.bithumbTickRepository.BithumbTickCommonJpaInterface;
import com.dk0124.cdr.repositoryPicker.bithumb.BithumbTickRepositoryPicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class BithumbTickService {
    private final BithumbTickRepositoryPicker bithumbTickRespositoryPicker;
    private final BithumbTickMapper bithumbTickMapper;
    private final ModelMapper modelMapper;

    public BithumbTickDto save(BithumbTickDto bithumbTick) throws IllegalArgumentException {
        BithumbTickCommonJpaInterface repo = getRepo(bithumbTick.getCode());
        return modelMapper.map(repo.save(bithumbTickMapper.mapTick(bithumbTick)), BithumbTickDto.class);
    }

    public BithumbTickDto findOneByCodeAndId(BithumbCoinCode code, Long id) {
        BithumbTickCommonJpaInterface repo = getRepo(code);
        return modelMapper.map(repo.findOneById(id), BithumbTickDto.class);
    }

    private BithumbTickCommonJpaInterface getRepo(String code) {
        return bithumbTickRespositoryPicker.getRepositoryFromCode(BithumbCoinCode.fromString(code));
    }

    private BithumbTickCommonJpaInterface getRepo(BithumbCoinCode code) {
        return bithumbTickRespositoryPicker.getRepositoryFromCode(code);
    }

}
