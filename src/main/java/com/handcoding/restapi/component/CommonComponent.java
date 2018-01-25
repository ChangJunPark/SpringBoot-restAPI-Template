package com.handcoding.restapi.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.handcoding.restapi.bean.HandlerAsync;

/**
 * 공통 컴포넌트
 * @author 이승환
 * @version 2018.01.25 v1.0
 */
@Component
public class CommonComponent {
	
	@Autowired
	private HandlerAsync async;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private MessageSource msg;
	
	
	public HandlerAsync getAsync() {
		return async;
	}
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	public MessageSource getMsg() {
		return msg;
	}

	
}