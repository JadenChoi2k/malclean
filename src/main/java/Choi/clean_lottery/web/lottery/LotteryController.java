package Choi.clean_lottery.web.lottery;

import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.dto.*;
import Choi.clean_lottery.service.LotteryService;
import Choi.clean_lottery.service.MemberService;
import Choi.clean_lottery.service.RoleService;
import Choi.clean_lottery.service.TeamService;
import Choi.clean_lottery.service.query.LotteryQueryService;
import Choi.clean_lottery.service.query.RoleQueryService;
import Choi.clean_lottery.service.query.TeamQueryService;
import Choi.clean_lottery.interfaces.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Controller
@RequestMapping("team/lottery")
@RequiredArgsConstructor
@Slf4j
public class LotteryController {

    private final MemberService memberService;
    private final TeamService teamService;
    private final TeamQueryService teamQueryService;
    private final LotteryService lotteryService;
    private final LotteryQueryService lotteryQueryService;
    private final RoleService roleService;
    private final RoleQueryService roleQueryService;

    @GetMapping("/pick")
    public String lotteryPickForm(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        TeamDto team = teamQueryService.findDtoByMemberId(memberId);
        if (team == null) {
            return "redirect:/team";
        }
        LotteryForm lotteryForm = new LotteryForm();
        model.addAttribute("members", team.getMembers());
        model.addAttribute("roles", team.getRoles());
        model.addAttribute("currentRole", team.getCurrentRole());

        model.addAttribute("lotteryForm", lotteryForm);
        return "lottery/lottery-pick";
    }

    @PostMapping("/pick")
    public String pickLottery(HttpServletRequest request, Model model,
                              @Valid @ModelAttribute LotteryForm lotteryForm, BindingResult bindingResult) {
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            TeamDto team = teamQueryService.findDtoByMemberId((Long) memberId);
            model.addAttribute("members", team.getMembers());
            model.addAttribute("roles", team.getRoles());
            model.addAttribute("currentRole", team.getCurrentRole());
            return "lottery/lottery-pick";
        }
        TeamDto team = teamQueryService.findDtoByMemberId((Long) memberId);
        Lottery lottery = lotteryService.drawLottery(lotteryForm.getLotteryName(), team.getId(), lotteryForm.getParticipants(),
                lotteryForm.getRoleId(), lotteryForm.getPick());
        return "redirect:/team/lottery/result/" + lottery.getId();
    }

    @GetMapping("/history")
    public String history(HttpServletRequest request, Model model,
                          @RequestParam(required = false, defaultValue = "0", value = "page") Integer page) {
        final int SIZE_PER_PAGE = 10;
        final int PAGE_COUNT = 5;
        // 만약 page의 범위가 벗어나면 보정해준다.
        if (page < 0) {
            return "redirect:/team/lottery/history?page=0";
        }
        HttpSession session = request.getSession();
        Object memberId = session.getAttribute(SessionConst.LOGIN_MEMBER);
        // not signed in
        if (memberId == null) {
            return "redirect:/?redirectURI=/team/lottery/history";
        }
        TeamDto teamDto = teamQueryService.findDtoByMemberId((Long) memberId);
        Long totalSize = lotteryService.getTotalSizeInTeam(teamDto.getId());
        if (page > totalSize / SIZE_PER_PAGE) {
            page = Math.toIntExact(totalSize / SIZE_PER_PAGE);
            return "redirect:/team/lottery/history?page=" + page;
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("count", SIZE_PER_PAGE);
        int firstCount = Math.toIntExact(page / PAGE_COUNT) * PAGE_COUNT;
        model.addAttribute("firstCount", firstCount);
        if (firstCount + PAGE_COUNT - 1 > totalSize / SIZE_PER_PAGE)
            model.addAttribute("lastCount", Math.toIntExact(totalSize / SIZE_PER_PAGE));
        else model.addAttribute("lastCount", firstCount + PAGE_COUNT - 1);
        model.addAttribute("team", teamDto);
        model.addAttribute("totalSize", totalSize);
        List<LotteryDto> resultList = null;
        // 기본은 10개씩으로 설정
        if (totalSize > 0) {
            resultList = lotteryQueryService.findAllDtoInTeam(teamDto.getId(), page, SIZE_PER_PAGE);
        }
        model.addAttribute("lotteryList", resultList);
        return "lottery/lottery-history";
    }

    @GetMapping("/result/{lotteryId}")
    public String result(HttpServletRequest request, @PathVariable Long lotteryId, Model model) {
        LotteryDto lotteryDto = lotteryQueryService.findDto(lotteryId);
        model.addAttribute("lottery", lotteryDto);
        model.addAttribute("role", lotteryDto.getRole());
        Object memberId = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        if (memberId != null) {
            Member member = memberService.findOne((Long) memberId);
            model.addAttribute("member", member);
            model.addAttribute("team", teamQueryService.findDtoByMemberId((Long) memberId));
            List<LotteryResultDto> resultByMemberId = lotteryDto.findResultByMemberId((Long) memberId);
            Map<String, List<MemberDto>> companions = lotteryDto.getCompanions(resultByMemberId);
            List<String> myAreas = resultByMemberId.stream().map(r -> r.getArea().getName()).collect(Collectors.toList());
            model.addAttribute("myAreas", myAreas);
            model.addAttribute("companions", companions);
        }
        return "lottery/lottery-result";
    }
}
