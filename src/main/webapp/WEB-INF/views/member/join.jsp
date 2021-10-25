<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">
.valid-msg{
   display:block;
   color:red;   
   font-size:10px;
}

input{
   display:block;
   width:400px;
   border: none;
   outline: none;
}

tr>td:nth-child(1){
   background-color: red;
   color:white;
}

#userId{
   display: inline-block;
   width: 80%;
}

input[type=submit]{
   width: 100%;
}

</style>
</head>
<body>

	<h1>회원 가입 양식</h1>
	<!-- 앞으로 form: 이렇게 쓰면 우리가 라이브러리에 추가하고 헤더에 넣은 spring form 태그임 -->
	<!--  modelAttribute="member" - model객체에 member로 넘어오는 파라미터에 직접 접근 가능 -->
    <form:form modelAttribute="joinForm" action="/member/join" method="post" id="frm_join" >
    <!-- modelAttribute엔 ModelAndView의 Attribute값 넣어줘야함 -->
     <table border="1">
        <tr>
           <td>ID : </td>
           <td >
                <input type="text" name="userId" id="userId" size="10" 
               		<c:if test="${empty error.userId}">
                    	value="${joinForm.userId}"   
                	</c:if>
                required />
                <!-- error에 값이 없다면 정상진행-->
                
                <button type="button" id="btnIdCheck">check</button>
                <c:if test="${empty error.userId}">
                    	<span id="idCheck" class="valid-msg"></span>
                </c:if>
                <form:errors path="userId" cssClass="valid-msg"/>
                <!-- userId가 validator 통과 못하면 span태그 만들어서 미리 지정한 valid-msg 출력 -->
           </td>
        </tr>
        <tr>
           <td>PASSWORD : </td>
           <td>
                <input type="password" name="password" id="password" placeholder="영어,숫자,특수문자 조합의 8글자 이상의 문자열입니다."  
                	<c:if test="${empty error.password}">
                    	value="${joinForm.password}"   
                	</c:if>
                required/>
                <form:errors path="password" cssClass="valid-msg"/>
           </td>
        </tr>
        <tr>
           <td>휴대폰번호 : </td>
           <td>
                <input id="tell" type="tel" name="tell" placeholder="숫자만 입력하세요" 
                	<c:if test="${empty error.tell}">
                    	value="${joinForm.tell}"   
                	</c:if>
                required/>
                <form:errors path="tell" cssClass="valid-msg"/>
           </td>
        </tr>
        <tr>
           <td>EMAIL : </td>
           <td>
                <input type="email" name="email" required/>
           </td>
        </tr>
        <tr>
           <td colspan="2">
              <input type="submit" value="가입" />
           </td>
       </tr>
   </table>
   </form:form>
   
<script type="text/javascript" src = "/resources/js/member/joinForm.js"></script>
   

</body>
</html>