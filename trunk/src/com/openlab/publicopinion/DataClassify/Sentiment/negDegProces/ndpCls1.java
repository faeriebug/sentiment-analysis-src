package com.openlab.publicopinion.DataClassify.Sentiment.negDegProces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.openlab.publicopinion.DataClassify.Sentiment.*;

public class ndpCls1 implements negDegProc {
	
	//��дʵ�
	private SentiDictionary SD;
	public ndpCls1(SentiDictionary sd){
		this.SD=sd;
	}
	
	@Override
	public String[] match(String[] sent) {
		// TODO Auto-generated method stub
		ArrayList<String> lisF=new ArrayList<String>();
		String[] wrd;
		int preFe=-1;//��ʾ��ȡ����һ���������ڵ�����
		for (int i = 0; i < sent.length; i++) {
			wrd=sent[i].split("/");
			if (isSentiWrd(wrd)) {//���Ϊ��д�,
				String[] fwrd;
				boolean negFlag,termi;
				for (int j = i-1; j>preFe; j--) {//��ǰ������ֱ��������ֹ����;����һ��������ȡλ��ֹͣ
					negFlag=false;termi=false;
					fwrd=sent[j].split("/");
					if(fwrd[1].startsWith("d")){//Ϊ����
						if(SD.getNeg().contains(fwrd[0])){//�жϷ񶨸��ʣ�
							negFlag=true;
						}else if(!SD.getDeg().containsKey(fwrd[0]))//��Ϊ�̶ȸ��ʣ���ֹƥ��
							termi=true;
					}else if(fwrd[1].startsWith("v") || fwrd[1].startsWith("a")){//�ж�VSI��Ҫ��ǰ��һλ
						if(SD.getVSI().contains(fwrd[0])){//���ֱ�Ӱ���VSI
							negFlag=true;
						}else if(--j>preFe){//��ǰһ�����ﹲͬ����VSI
							String[] ffwrd=sent[j].split("/");
							if(SD.getVSI().contains(ffwrd[0]+fwrd[0])){
								negFlag=true;
							}else{
								termi=true;
							}
						}else{
							termi=true;
						}
					}else
						termi=true;
					if(negFlag)//�ҵ������γɷ�
						wrd[0]=getAntonym(wrd[0]);
					else if(termi)
						break;
				}
				preFe=i;
				lisF.add(wrd[0]);
			}
		}
		return lisF.toArray(new String[lisF.size()]);
	}

	/**
	 * �жϴ����Ƿ�Ϊ��д�
	 * @param wrd
	 * @return
	 */
	public boolean isSentiWrd(String[] wrd){
		//�����жϴ��ԣ�Ȼ����Ҵʵ�
		boolean re=wrd[1].startsWith("a")//���ݴ�
								||(wrd[1].startsWith("n") && wrd[1]!="nr" && wrd[1]!="ns" && wrd[1]!="nt")//����
								|| (wrd[1].startsWith("v") && wrd[1]!="vshi" && wrd[1]!="vyou");
		if(re)//������������ʵ�
			return SD.getSenti().contains(wrd[0]);
		return false;
	}
	
	/**
	 * ��ȡ�ʵķ����
	 * @return
	 */
	public String getAntonym(String wrd){
		if (wrd.endsWith("_N"))
			return wrd.substring(0, wrd.length()-2);
		return wrd+"_N";
	}


	@Override
	public Map<String, Integer> matchDeg(Map<String,Integer> dic,String[] sent) {
		// TODO Auto-generated method stub
		Map<String, Integer> mF=new HashMap<String, Integer>();
		DegValue DV=new DegValue1();
		String[] wrd;int degree;
		int preFe=-1;//��ʾ��ȡ����һ���������ڵ�����
		for (int i = 0; i < sent.length; i++) {
			wrd=sent[i].split("/");
			if (dic.containsKey(wrd[0]) || dic.containsKey(getAntonym(wrd[0]))) {//���Ϊ��д�,
				degree=1;
				//������з񶨴ʴ���ͳ̶ȸ��ʵĴ���
				String[] fwrd;
				boolean negFlag,termi;
				//negFlag��ʾ�Ƿ����񶨴��ˣ�termi��ʾ�Ƿ���ֹ��ε�ƥ��
				for (int j = i-1; j>preFe; j--) {//��ǰ������ֱ��������ֹ����;����һ��������ȡλ��ֹͣ
					negFlag=false;termi=false;
					fwrd=sent[j].split("/");
					if(fwrd[1].startsWith("d")){//Ϊ����,������Ҫ���г̶ȸ��ʵĴ�����ӷ񶨸��ʵĴ���
						if(SD.getNeg().contains(fwrd[0])){//�жϷ񶨸��ʣ�
							negFlag=true;
						}else if(SD.getDeg().containsKey(fwrd[0])){//Ϊ�̶ȸ���,�޸����δʵ�Ƶ��
							degree=DV.getValue(SD.getDeg().get(fwrd[0]), degree);
						}else{
							termi=true;
						}
					}//������Ҫ��VSI�񶨴ʵĴ���
					else if(fwrd[1].startsWith("v") || fwrd[1].startsWith("a")){//�ж�VSI��Ҫ��ǰ��һλ
						if(SD.getVSI().contains(fwrd[0])){//���ֱ�Ӱ���VSI
							negFlag=true;
						}else if(--j>preFe){//��ǰһ�����ﹲͬ����VSI
							String[] ffwrd=sent[j].split("/");
							if(SD.getVSI().contains(ffwrd[0]+fwrd[0])){
								negFlag=true;
							}else{
								termi=true;
							}
						}else{
							termi=true;
						}
					}else//ǰ���Ĵ������账������ƥ��
						termi=true;
					if(negFlag)//�ҵ������γɷ�
						wrd[0]=getAntonym(wrd[0]);
					else if(termi)
						break;
				}
				preFe=i;
				//����mF��
				if (mF.containsKey(wrd[0])) {
					mF.put(wrd[0], mF.get(wrd[0])+degree);//ԭ��������ϱ��δ����Ƶ��
				}else
					mF.put(wrd[0], degree);
			}
		}
		return mF;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SentiDictionary SD=new SentiDictionary();
		ndpCls1 ndp1=new ndpCls1(SD);
		String[] fe=ndp1.match(new String[]{"��/rzv","��/q","�ͷ�/n","��/v","��/rr","��/d","��/d","����/v","!/wt"});
		for (String feature : fe) {
			System.out.println(feature);
		}
//		System.out.println(ndp1.isSentiWrd(new String[]{"����","/v"}));
	}
}
