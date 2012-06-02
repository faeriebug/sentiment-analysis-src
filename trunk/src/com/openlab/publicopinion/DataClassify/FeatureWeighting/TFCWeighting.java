package com.openlab.publicopinion.DataClassify.FeatureWeighting;

import java.util.Map;


public class TFCWeighting extends FeatureWeight {
	private int docID_calced=-1;//��ʾ�Ѿ�������ĵ�ID�����Լ��ټ�����,��Ϊͬһ���ĵ���ĸ��һ����
	private double de=0;//�Ѿ�������ĵ��Ľ����
	@Override
	public Number getWight(int docID, int wordID, CategorizingDataManager CDM) {
		// TODO Auto-generated method stub
		Map<Integer, Map<Integer, Integer>> cp=CDM.getMap();
		int 	tf=cp.get(wordID).containsKey(docID)?cp.get(wordID).get(docID):0;
		int 	n=cp.get(wordID).size();
		double  nu=tf*Math.log(CDM.N/(double)n);
		if(docID_calced!=docID){
			docID_calced=docID;//�����ĵ�ID
			de=0;//�ÿգ����¼���
			for(Integer p:cp.keySet()){
				int tfp=cp.get(p).containsKey(docID)?cp.get(p).get(docID):0;
				int np=cp.get(p).size();
				double tmp=tfp*Math.log(CDM.N/(double)np);
				de+=tmp*tmp;
			}
		}

		return nu/Math.sqrt(de);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		long start=System.currentTimeMillis();
//		TFCWeighting TFW=new TFCWeighting();
//		TFW.CalcFeaturesForAllFiles_Training("Test\\FeatureDictionary.txt", "TrainingData\\", "Test\\CategoriedFiles.txt");
//		long end=System.currentTimeMillis();
//		System.out.println("����ִ�У�"+(end-start)/1000.0+"s");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TFC";
	}
}
