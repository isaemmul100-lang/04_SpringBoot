package org.example.viewreslover;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/*")
public class ResolverController {


    @GetMapping("string")
    public String stringReturning(Model model) {


        model.addAttribute("forwardMessage", "문자열로 뷰 이름 반환함...");


        return "result";
    }

    @GetMapping("string-redirect")
    public String stringRedirect()  {

        // redirect: : 리다이렉트를 지시하는 특별한 접두사
        return "redirect:/";
    }


    @GetMapping("string-redirect-attr")
    public String stringRedirectFlashAttribute(RedirectAttributes rAttr) {

        rAttr.addFlashAttribute("flashMessage1", "RedirectAttribute를 사용해 redirect..");

        /*
        * flash attribute : 리다이렉트 대상의 다음 요청에 자동으로 전달된다.
        *                   - 한 번 사용 뒤 제거되는 일회용 데이터
        * */
        return "redirect:/";
    }

    @GetMapping("modelandview")
    public ModelAndView modelAndViewReturning(ModelAndView mv) {


        mv.addObject("forwardMessage", "ModelAndView를 이용한 모델과 뷰 반환"); // 데이터 저장
        mv.setViewName("result"); // PATH 지정

        return mv;
    }

    @GetMapping("modelandview-redirect")
    public ModelAndView modelAndViewRedirect(ModelAndView mv)  {

        mv.setViewName("redirect:/");

        return mv;
    }

    @GetMapping("modelandview-redirect-attr")
    public ModelAndView modelAndViewRedirect(ModelAndView mv, RedirectAttributes rttr) {

        rttr.addFlashAttribute("flashMessage2", "ModelAndView를 이용한 RedirectAttribute");
        mv.setViewName("redirect:/");

        return mv;
    }
}
