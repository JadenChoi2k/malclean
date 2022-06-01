package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.area.Area;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class LotteryResult extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lottery_id")
    private Lottery lottery;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    private LocalDateTime pickDate;

    public LotteryResult(Lottery lottery, Member member, Area area, LocalDateTime pickDate) {
        this(lottery, member, area);
    }

    public LotteryResult(Lottery lottery, Member member, Area area) {
        this.lottery = lottery;
        this.member = member;
        this.area = area;
    }
}
