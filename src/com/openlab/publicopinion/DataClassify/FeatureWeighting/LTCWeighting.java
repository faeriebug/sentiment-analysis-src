package com.openlab.publicopinion.DataClassify.FeatureWeighting;

import java.util.Map;


public class LTCWeighting extends FeatureWeight {
	public static final String Name="LTCWeighting";
	private int docID_calced=-1;//��ʾ�Ѿ�������ĵ�ID�����Լ��ټ�����,��Ϊͬһ���ĵ���ĸ��һ����
	private double de=0;//�Ѿ�������ĵ��Ľ����
	@Override
	public Number getWight(int docID, int wordID, CategorizingDataManager CDM) {
		// TODO Auto-generated method stub
		Map<Integer, Map<Integer, Integer>> cp=CDM.getMap();
		int 	tf=cp.get(wordID).containsKey(docID)?cp.get(wordID).get(docID):0;
		int 	n=cp.get(wordID).size();
		double  nu=Math.log(tf+1)*Math.log(CDM.N/(double)n+1);
		if(docID_calced!=docID){
			docID_calced=docID;
			de=0;
			for(Integer p:cp.keySet()){
				int tfp=cp.get(p).containsKey(docID)?cp.get(p).get(docID):0;
				int np=cp.get(p).size();
				double tmp=Math.log(tfp+1)*Math.log(CDM.N/(double)np+1);
				de+=tmp*tmp;
			}
		}

		return nu/Math.sqrt(de);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		long start=System.currentTimeMillis();
//		LTCWeighting TFW=new LTCWeighting();
//		TFW.CalcFeaturesForAllFiles_Training("Test\\FeatureDictionary.txt", "TrainingData\\", "Test\\CategoriedFiles");
//		long end=System.currentTimeMillis();
//		System.out.println("����ִ�У�"+(end-start)/1000.0+"s");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LTC";
	}
}
