package com.openlab.publicopinion.DataClassify;

import java.util.Map;

/**
 * ͳ�Ʋ������������ֵ�Ƶ������
 * @author HuHaixiao
 *
 */
public interface FeatureCounter {
	/**
	 * ͳ���ı��е�������Ƶ��������
	 * @param dic
	 * @param text
	 * @return
	 */
	Map<String,Integer> Count(Map<String,Integer> dic,String text);

}
