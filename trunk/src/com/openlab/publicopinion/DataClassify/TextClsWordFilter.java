package com.openlab.publicopinion.DataClassify;

import java.util.Map;


/**
 * ����ѡ��ʹ�õĴ��������
 * <p> �������Ǵ�����Ϣ���й���
 * @author HuHaixiao
 *
 */
public class TextClsWordFilter implements WordFilter {

	/**�ֵ�Ŀ¼*/
	private  String dicPath;
	/**�����ֵ��ӳ���*/
	private  Map<String, Integer> dic;
	 
	/**
	 * �˹��캯�������жϵĴ����Ƚ��дʵ��ѯ��Ȼ����Ǵ����ж�
	 * @param dictionaryPath
	 */
	public TextClsWordFilter(String dictionaryPath){
		this.dicPath=dictionaryPath;
		dic=(new Dictionary()).loadDictionary(dicPath);
	}
	
	/**
	 * �˹��캯�������жϵĴ����Ƚ��дʵ��ѯ��Ȼ����Ǵ����ж�
	 * @param dic
	 */
	public TextClsWordFilter(Map<String,Integer> dic){
		this.dic=dic;
	}
	
	/**
	 * ֱ�ӽ��д����ж�
	 */
	public TextClsWordFilter(){
		this.dic=null;
	}
	
	/**
	 * ����ʵ�������Ϊnull�������ڴʵ��в��Ҹôʣ�������������д��Թ��ˣ�����ֱ�ӹ��˵���
	 * <p>����ʵ�Ϊnull������д��Թ���
	 */
	@Override
	public boolean isStoped(String word, String pos) {
		// TODO Auto-generated method stub
		if (dic!=null && !dic.containsKey(word)) {
			return true;
		}
		return !((pos.startsWith("n")||pos.startsWith("v")||
				pos.startsWith("a")||pos.startsWith("b")||
				pos.startsWith("d")||
				pos.startsWith("z"))&&!pos.equals("vshi")&&
		!pos.equals("vyou")&&!pos.equals("vf"));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
