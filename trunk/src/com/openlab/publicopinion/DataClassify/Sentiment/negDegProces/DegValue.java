package com.openlab.publicopinion.DataClassify.Sentiment.negDegProces;

/**
 * ȷ���̶ȸ��ʶԴ���Ӱ���Ƶ������
 * @author HuHaixiao
 *
 */
public interface DegValue {

	/**
	 * ���ݳ̶ȣ�������Ӧ��Ƶ�����ӵı���
	 * @param degree
	 * @return
	 */
	int getValue(int degree,int orignDeg);
}
