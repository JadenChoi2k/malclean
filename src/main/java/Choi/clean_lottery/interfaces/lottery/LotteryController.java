package Choi.clean_lottery.interfaces.lottery;

import Choi.clean_lottery.application.lottery.LotteryFacade;
import Choi.clean_lottery.application.member.MemberFacade;
import Choi.clean_lottery.application.role.RoleFacade;
import Choi.clean_lottery.domain.lottery.LotteryCommand;
import Choi.clean_lottery.domain.lottery.LotteryInfo;
import Choi.clean_lottery.domain.lottery.query.LotteryQueryInfo;
import Choi.clean_lottery.domain.member.query.MemberQueryInfo;
import Choi.clean_lottery.common.constant.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@Controller
@RequestMapping("team/lottery")
@RequiredArgsConstructor
public class LotteryController {
    private final LotteryFacade lotteryFacade;
    private final MemberFacade memberFacade;
    private final RoleFacade roleFacade;

    @GetMapping("/pick")
    public String lotteryPickForm(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam(memberId);
        model.addAttribute("members", withTeam.getTeamInfo().getMembers());
        model.addAttribute("roles", withTeam.getTeamInfo().getRoles());
        model.addAttribute("currentRole", withTeam.getTeamInfo().getCurrentRole());
        model.addAttribute("lotteryForm", new LotteryForm());
        return "lottery/lottery-pick";
    }

    @PostMapping("/pick")
    public String pickLottery(HttpServletRequest request, Model model,
                              @ModelAttribute @Valid LotteryForm lotteryForm,
                              BindingResult bindingResult) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return lotteryPickForm(session, model);
        }
        MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
        LotteryInfo.Main lotteryInfo = lotteryFacade.drawLottery(LotteryCommand.DrawLotteryRequest.builder()
                .lotteryName(lotteryForm.getLotteryName())
                .teamId(withTeam.getTeamInfo().getTeamId())
                .roleId(lotteryForm.getRoleId())
                .pickAreaIdList(lotteryForm.getPick())
                .participantIdList(new ArrayList<>(lotteryForm.getParticipants()))
                .build());
        return "redirect:/team/lottery/result/" + lotteryInfo.getLotteryId();
    }

    @GetMapping("/history")
    public String history(HttpServletRequest request, Model model,
                          @RequestParam(required = false, defaultValue = "10", value = "size") Integer size,
                          @RequestParam(required = false, defaultValue = "0", value = "page") Integer page) {
        if (page < 0 || size <= 0) {
            return history(request, model, 10, 0);
        }
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/?redirectURI=/team/lottery/history";
        }
        var withTeam = memberFacade.withTeam((Long) memberId);
        var pageResult = lotteryFacade.retrievePageQuery(withTeam.getTeamInfo().getTeamId(), PageRequest.of(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("count", size);
        model.addAttribute("firstCount", Math.toIntExact(page / 5) * 5);
        model.addAttribute("lastCount", pageResult.getTotalPages());
        model.addAttribute("team", withTeam.getTeamInfo());
        model.addAttribute("lotteryList", pageResult.toList());
        return "lottery/lottery-history";
    }

    @GetMapping("/result/{lotteryId}")
    public String result(HttpServletRequest request, @PathVariable Long lotteryId, Model model) {
        LotteryQueryInfo.Main lotteryInfo = lotteryFacade.retrieveQuery(lotteryId);
        model.addAttribute("lottery", lotteryInfo);
        model.addAttribute("role", lotteryInfo.getRole());
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId != null) {
            MemberQueryInfo.WithTeam withTeam = memberFacade.withTeam((Long) memberId);
            model.addAttribute("member", withTeam.getMemberInfo());
            model.addAttribute("team", withTeam.getTeamInfo());
            model.addAttribute("myAreas", lotteryInfo.getMyAreaNameList((Long) memberId));
            model.addAttribute("companions", lotteryInfo.getMyCompanionList((Long) memberId));
        }
        return "lottery/lottery-result";
    }
}
