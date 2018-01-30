package com.handcoding.restapi.bean;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.handcoding.restapi.domain.UserVO;
import com.handcoding.restapi.domain.out.OutUserLoginVO;
import com.handcoding.restapi.util.TempKey;

/**
 * 로그인 토큰 처리
 * @author 이승환
 * @version 2018.01.30 v1.0
 */
@Component
public class HandlerToken {
	
	@Resource(name="redisTemplate")
	private ValueOperations<String, Object> tokens;
	
	/**
	 * 토큰 생성
	 * @param outUserLoginVO
	 * @return
	 */
	public String getToken(OutUserLoginVO outUserLoginVO) {
		TempKey tempKey = new TempKey();
		String newToken = tempKey.getKey(300);
		String oldToken = getCheckId(outUserLoginVO);
		String token = null;
		if(oldToken == null) {
			token = newToken;
		}else {
			token = oldToken;
		}
		setToken(token, outUserLoginVO);
		setCheckId(token, outUserLoginVO);
		return token;
	}
	
	/**
	 * 유저정보 조회
	 * @param token
	 * @return
	 */
	public UserVO getUserVO(String token) {
		OutUserLoginVO outUserLoginVO = (OutUserLoginVO) tokens.get(token);
		if(outUserLoginVO != null) {
			setToken(token, outUserLoginVO);
			setCheckId(token, outUserLoginVO);
		}
		return outUserLoginVO;
	}
	
	/**
	 * token set 처리
	 * @param token
	 * @param outUserLoginVO
	 */
	private void setToken(String token, OutUserLoginVO outUserLoginVO) {
		if(outUserLoginVO.getTimeout()==0 || outUserLoginVO.getTimeUnit()==null) {
			tokens.set(token, outUserLoginVO, 30, TimeUnit.MINUTES);
		}else {
			tokens.set(token, outUserLoginVO, outUserLoginVO.getTimeout(), outUserLoginVO.getTimeUnit());
		}
	}
	
	/**
	 * 토큰 생성전 oldToken 조회
	 * @param outUserLoginVO
	 * @return
	 */
	private String getCheckId(OutUserLoginVO outUserLoginVO) {
		String oldToken = (String) tokens.get(outUserLoginVO.getId());
		return oldToken;
	}
	
	/**
	 * 토큰 중복 체크용
	 * @param token
	 * @param outUserLoginVO
	 */
	private void setCheckId(String token, OutUserLoginVO outUserLoginVO) {
		if(outUserLoginVO.getTimeout()==0 || outUserLoginVO.getTimeUnit()==null) {
			tokens.set(outUserLoginVO.getId(), token, 30, TimeUnit.MINUTES);
		}else {
			tokens.set(outUserLoginVO.getId(), token, outUserLoginVO.getTimeout(), outUserLoginVO.getTimeUnit());
		}
	}
	
}
