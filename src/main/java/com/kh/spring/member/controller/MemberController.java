package com.kh.spring.member.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.CookieGenerator;

import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.exception.HandleableException;
import com.kh.spring.common.validator.ValidateResult;
import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.validator.JoinForm;
import com.kh.spring.member.validator.JoinFormValidator;

import lombok.RequiredArgsConstructor;

// 1. @Controller : 해당 클래스를 applicationContext에 bean으로 등록
//				  Controller와 관련된 annotation을 상요할 수 있게 해준다.

// 2. @RequestMapping : 요청 URL과 Controller의 메서드 매핑을 지원
//					   클래스 위에 선언할 경우, 해당 클래스의 모든 메서드가 지정된 경로를 상위 경로로 가진다
// 3. @GetMapping : Get방식의 요청 URL과 Controller의 메서드 매핑을 지원
// 4. @PostMapping : Post방식의 요청 URL과 Controller의 메서드 매핑을 지원
// 5. @RequestParam : 요청 파라미터를 컨트롤러 메서드의 매개변수에 바인드
// 					  단 content-type이 application/x-www-form-urlEncoded 인 경우만 가능
//					  FormHttpMessageConverter가 동작
// 6. @ModelAttribute : 요청 파라미터를 setter 사용해 메서드 매개변수에 선언된 객체에 바인드.
//					Model 객체의 attribute에 자동으로 저장
// 7. @RequestBody : 요청 Body를 읽어서 자바의 객체에 바인드
// 					content-type이 application/x-www-form-urlEncoded를 지원하지 않음
// 8. @RequestHeader : 요청 헤더를 메서드의 매개변수에 바인드
// 9. @SessionAttribute : 원하는 session의 속성값을 매개변수에 바인드
// 10. @CookieValue : 원하는 cookie값을 매개변수에 바인드
// 11. @PathVariable : url 템플릿에 담긴 파라미터값을 매개변수에 바인드
// 12. @ResponseBody : 메서드가 반환하는 값을 응답 body에 작성
// 13. Servlet 객체를 컨트롤러의 매개변수에 선언해 주입받을수 있다.
//    HttpSetvletRequest, HttpSetvletResponse, HttpSession

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// Controller 메서드의 return 타입
	// void : 해당 메서드가 호출된 url의 경로와 같은 위치에 있는 jsp파일로 요청을 재지정
	// ex) 요청 url : /index/index -> WEB-INF/views/index/index.jsp (servlet-context에 있는 그거!)
	// String : 반환하는 값이 jsp파일, return "/index/index" -> ->
	// WEB-INF/views/index/index.jsp
	// ModelAndView : model객체 + view(jsp파일 경로)
	// 어떤 view로 경로를 재지정해줄깡
	

	private final MemberService memberService;
	private final JoinFormValidator joinFormValidator;
	
	// Model 속성명 자동 생성 규칙
	// com.myapp.Product becomes "product"
	// com.myapp.MyProduct becomes "myProduct"
	// com.myapp.UKProduct becomes "UKProduct"

	
	@InitBinder(value = "joinForm")
	public void initBinder(WebDataBinder webDataBinder){
		// 넘어오는 요청 파라미터를 바인드해주는 메서드
		// model의 속성 중 속성명이 joinForm인 속성이 있는경우 @InitBinder 실행
		// 컨트롤러로 넘어오는 데이터의 속성명이 이닛 바인더의 value값과 일치할 때! 
		webDataBinder.addValidators(joinFormValidator);
		// joinForm으로 파라미터 넘어오면 validator 돌릴꺼얌
		
	}
	

	@GetMapping("join")
	public void joinForm(Model model) {
		model.addAttribute(new JoinForm()).addAttribute("error", new ValidateResult().getError());
	}
	// 매개변수에 모델객체 필요없긴 한데
	// 나중에 타임리프 쓸려면 필요.
	
	// @PostMapping("/member/join") 
	// public String join(String userId, String password, String tell, String email){
	// return "index";
	// }
	
	// 페이지의 요청 파라미터명과 dto의 변수명이랑 통일! 같아야 바인딩됨!
	@PostMapping("join")
	public String join(@Validated JoinForm form, Errors errors, Model model, HttpSession session, RedirectAttributes redirectAttr){
		// 반드시 검증될(Validator 탈)타입명 바로 뒤에 Error 선언해야함
		
		ValidateResult vr = new ValidateResult();
		model.addAttribute("error", vr.getError());
		
		if (errors.hasErrors()) {
			vr.addError(errors);
			return "member/join";
		}
		// joinTest 썌릴떄(값 이상한거 넣고)
		// ModelAndView에 errors뜨면 성공
		
		
		String token = UUID.randomUUID().toString();
		
		session.setAttribute("persistUser", form);
		session.setAttribute("persistToken", token);
		
		memberService.authenticateByEmail(form, token);
		redirectAttr.addFlashAttribute("message", "이메일이 발송되었습니다");
		
		return "redirect:/";
	}
	
	@GetMapping("join-impl/{token}")
	public String joinImpl(@PathVariable String token, @SessionAttribute(value = "persistToken", required = false) String persistToken, 
			@SessionAttribute(value = "persistUser", required = false) JoinForm form, HttpSession session, RedirectAttributes redirectAttr){
		// {token}값이 매개변수의 token으로 날라옴
		
		if (!token.equals(persistToken)) {
			throw new HandleableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
		}
		
		memberService.insertMember(form);
		redirectAttr.addFlashAttribute("message", "회원가입을 환영합니다");
		session.removeAttribute("persistToken");
		session.removeAttribute("persistUser");
		return "redirect:/member/login";
	}
	
	
	// json 받아오기 실습용 메서드
	@PostMapping("join-json")
	public String joinWithJson(@RequestBody Member member) {
		logger.debug(member.toString());
		return "index";
	}
	
	// 로그인 페이지 이동 메서드 login
	@GetMapping("login")
	public void login(){}
	
	// 로그인 실행 메서드 loginImpl, 재지정 jsp : mypage
	/* @RequestMapping("/login") 애는 get이랑 post랑 둘 다 가져감. 우리가 필요한건 post 하나니까 PostMapping 쓰자*/
	@PostMapping("/login")
	public String loginImpl(Member member, HttpSession session, RedirectAttributes redirectAttr){
		
		Member certifiedUser = memberService.authrnticationUser(member);
		
		if (certifiedUser == null) {
			redirectAttr.addFlashAttribute("message", "아이디나 비밀번호가 정확하지 않습니다");
			// addFlashAttribute : 딱 그 리다이렉트떄만 값 유지
			// addAttribute : 마치 세션처럼 값 유지
			return "redirect:/member/login";
		}
		
		
		session.setAttribute("authentication", certifiedUser);
		logger.debug(certifiedUser.toString());
		//DEBUG에는 내가 넣은값
		//ModelAndView에는 내가 넣은값
		
		// 리다이렉트 안하고 포워드하면 여전히 주소창이 http://localhost:9090/member/login이고 한번 더 mypage가 호출되지도 않음
		// 근데 이 리다이렉트를 받아줄 메소드가 아직 없어서 404뜸. jsp 파일이 있는지 없는지는 여기서 중요(한거같기도)하지 않음
		return "redirect:/member/mypage";
	}
	
	@GetMapping("mypage")
	public String mypage(@CookieValue(name="JSESSIONID", required = false) String sessionId, @SessionAttribute(name = "authentication", required = false) Member member, HttpServletResponse response){
		
		// Cookie 생성 및 응답헤더에 추가
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName("chocoCookie");
		cookieGenerator.addCookie(response, "chocoCookie");
		
		logger.debug("sessionId" + sessionId.toString());
		logger.debug("member" + member.toString());
		
		return "member/mypage";
		// HttpServletResponse없으면 리턴 void 때려도 Forwarded URL 잘 떙겨옴. 근데 서블릿 로우단 어디선가 Response가 Forwarded url 보내는걸 막음.
	}
	
	@GetMapping("id-check") // 당근 매개변수에 @Requestparam 생략된거
	@ResponseBody // 바디에 담아서 보냄. 리턴 타입이 여러개 가능
	public String idCheck(String userId) {
		Member member = memberService.selectMemberByUserId(userId);
		
		if (member == null) {
			return "available";
		} else {
			return "disable";	
		}
		
	}
	
	
	
	
	
	

}
