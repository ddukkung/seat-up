package com.seatup.user.mypage;

import com.seatup.jwt.UserPrincipal;
import com.seatup.reservation.service.ReservationService;
import com.seatup.reservation.dto.ReservationListResponse;
import com.seatup.user.entity.User;
import com.seatup.user.service.UserService;
import com.seatup.user.mypage.dto.ReservationDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/mypage")
public class MyPageViewController {

    private final UserService userService;
    private final ReservationService reservationService;

    @GetMapping
    public String myPage(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = userService.findById(principal.getUserId());
        model.addAttribute("user", user);
        return "mypage/main";
    }

    @GetMapping("/info")
    public String info(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = userService.findById(principal.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("activeMenu", "info");
        return "mypage/info";
    }

    @GetMapping("/reservations")
    public String reservations(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = userService.findById(principal.getUserId());
        List<ReservationListResponse> reservations = reservationService.findByUserId(user);
        model.addAttribute("user", user);
        model.addAttribute("activeMenu", "reservations");
        model.addAttribute("reservations", reservations);
        return "mypage/reservations/list";
    }

    @GetMapping("/reservations/{id}")
    public String reservationDetail(@AuthenticationPrincipal UserPrincipal principal,
                                    @PathVariable("id") Long id, Model model) {
        User user = userService.findById(principal.getUserId());
        ReservationDetailResponse detail = reservationService.findById(user, id);
        model.addAttribute("user", user);
        model.addAttribute("activeMenu", "reservations");
        model.addAttribute("detail", detail);
        return "mypage/reservations/detail";
    }

}
