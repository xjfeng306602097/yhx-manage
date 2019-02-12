package com.yhx.manage.common.handler;

import com.yhx.front.common.annotation.TokenUuid;
import com.yhx.front.common.dto.BaseResponse;
import com.yhx.front.common.exception.DuomanRuntimeException;
import com.yhx.front.common.util.ReturnInfo;
import com.yhx.front.user.model.SysToken;
import com.yhx.front.user.service.SysTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

	private static final Logger loger = LoggerFactory.getLogger(ControllerAspect.class);


	@Autowired
	private SysTokenService sysTokenService ;
	
	@Resource
	private HttpServletRequest request;

	// Controller层切点
	@Pointcut("execution(* com.yhx.front.*.controller..*.*(..))")
	public void controllerAspect() {
	}

	@Around("controllerAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		boolean isJson = isJson(pjp);
		try {
			TokenUuid tokenUuid = (TokenUuid) getMethodAnnotation(TokenUuid.class, pjp);
			if (tokenUuid!=null && tokenUuid.checkLogin()) {
				String token = request.getHeader("token");
				if (!StringUtils.isNotEmpty(token))
					return BaseResponse.fail( 403, "信息已失效，请重新登录" );// 尚未配置权限
				SysToken userU = sysTokenService.verify(token);
				if (userU==null)//token 验证失败
					return BaseResponse.fail( 403, "信息已失效，请重新登录" );// 尚未配置权限
			}
			Object result = pjp.proceed();
			return result;
		} catch (DuomanRuntimeException e) {
			if (isJson) {
				return new ReturnInfo(e.getCode(), (String) e.getMessages());
			} else {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}
	}


	private boolean isJson(ProceedingJoinPoint pjp) throws Throwable {
		ResponseBody responseBody = (ResponseBody) getAnnotation(ResponseBody.class, pjp);
		RestController restController = (RestController) getClassAnnotation(RestController.class, pjp);

		return responseBody != null || restController != null;
	}

	/**
	 * 获取方法注解
	 * 
	 * @param an
	 * @param pjp
	 * @return
	 */
	private Annotation getMethodAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
		// 获取执行的方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();

		return method.getAnnotation(an);
	}

	/**
	 * 
	 * @param an
	 * @param pjp
	 * @return
	 */
	private Annotation getClassAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
		// 获取执行的方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();

		return method.getDeclaringClass().getAnnotation(an);
	}

	private Annotation getAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
		Annotation a = getMethodAnnotation(an, pjp);
		return a == null ? getClassAnnotation(an, pjp) : a;
	}

}
