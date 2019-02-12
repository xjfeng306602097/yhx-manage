package com.yhx.manage.common.handler;

import com.yhx.front.common.constant.ErrorCode;
import com.yhx.front.common.dto.BaseResponse;
import com.yhx.front.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 全局异常处理器 避免直接将错误堆栈信息抛给前端
 * 
 * @author gaolinlou Date: Created in 9:58 2018/8/9
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Value("${yhx.profiles.active}")
	private String active;

	@ExceptionHandler(BaseException.class)
	@ResponseStatus(HttpStatus.OK)
	public BaseResponse<?> handle(BaseException ex) {
		Locale locale = LocaleContextHolder.getLocale();
		String msg = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);
		return new BaseResponse<Object>(ex.getCode(), msg, null);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse<?> handle(Exception ex) {
		if ("debug".equals(active)) {
			log.error(ex.getMessage(), ex);
			return BaseResponse.fail(500, this.getLog(ex));
		}
		log.error(ex.getMessage(), ex);
		Locale locale = LocaleContextHolder.getLocale();
		String msg = messageSource.getMessage("server.internal.error", null, "Server internal error!", locale);
		return new BaseResponse<Object>(500, msg, null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BaseResponse<?> handle(MethodArgumentNotValidException exception) {
		StringBuilder msg = new StringBuilder();
		BindingResult result = exception.getBindingResult();
		if (result.hasFieldErrors()) {
			List<FieldError> fieldErrors = result.getFieldErrors();
			for (FieldError error : fieldErrors) {
				String simpleName = error.getField().substring(error.getField().lastIndexOf(".") + 1);
				log.error("{} {}", error.getField(), error.getDefaultMessage());
				msg.append(String.format("%s %s", simpleName, error.getDefaultMessage()));
			}
			return new BaseResponse<Object>(ErrorCode.PARAMETER_ERROR, msg.toString(), null);
		}

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				msg.append(error.getDefaultMessage());
				log.error("{}", error.getDefaultMessage());
			}
		}
		return new BaseResponse<Object>(ErrorCode.PARAMETER_ERROR, msg.toString(), null);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BaseResponse<?> handle(ValidationException exception) {
		StringBuilder msg = new StringBuilder();
		if (exception instanceof ConstraintViolationException) {
			ConstraintViolationException exs = (ConstraintViolationException) exception;
			Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
			for (ConstraintViolation<?> item : violations) {
				String path = item.getPropertyPath().toString();
				String fieldName = path.substring(path.lastIndexOf(".") + 1);
				msg.append(String.format("%s %s", fieldName, item.getMessage()));
				log.error("{} {}", fieldName, item.getMessage());
			}
		} else {
			msg.append(exception.getMessage());
		}
		return new BaseResponse<Object>(ErrorCode.PARAMETER_ERROR, msg.toString(), null);
	}

	/**
	 * 
	 * @param level
	 * @param e
	 * @param obj
	 *            执行当前的this
	 */
	public String getLog(Exception ex) {
		StackTraceElement stackTraceElement = ex.getStackTrace()[0];
		String msg = (ex.toString() + "--class" + "--Line:" + stackTraceElement.getLineNumber() + "--Method:" + stackTraceElement.getMethodName());
		return msg;
	}

}