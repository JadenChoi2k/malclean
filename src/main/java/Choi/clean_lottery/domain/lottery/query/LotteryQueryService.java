package Choi.clean_lottery.domain.lottery.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LotteryQueryService {
    LotteryQueryInfo.Main retrieve(Long lotteryId);

    Page<LotteryQueryInfo.PageItem> retrievePage(Long teamId, Pageable pageable);
}
