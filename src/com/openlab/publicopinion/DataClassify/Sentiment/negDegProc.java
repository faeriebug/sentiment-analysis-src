package com.openlab.publicopinion.DataClassify.Sentiment;

import java.util.Map;


public interface negDegProc {
	/**
	 * ʶ����Ƿ����Լ����Դ���ķ񶨴���ʽ
	 * @param sent ��������
	 * @return ������Դ����򷵻ش���Ľ��������Ϊnull 
	 */
	String[] match(String[] sent);
	/**
	 * ����̶ȸ��ʶԴ���Ƶ����Ӱ��
	 * @param dic �����ʵ�
	 * @param sent
	 * @return
	 */
	Map<String,Integer> matchDeg(Map<String,Integer> dic,String[] sent);
	
}
