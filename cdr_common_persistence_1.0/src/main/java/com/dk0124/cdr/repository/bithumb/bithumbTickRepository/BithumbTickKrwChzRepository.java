package com.dk0124.cdr.repository.bithumb.bithumbTickRepository;

import com.dk0124.cdr.entity.bithumb.tick.coins.BithumbTickKrwChz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BithumbTickKrwChzRepository extends JpaRepository<BithumbTickKrwChz, Long>, BithumbTickCommonJpaInterface {
}
