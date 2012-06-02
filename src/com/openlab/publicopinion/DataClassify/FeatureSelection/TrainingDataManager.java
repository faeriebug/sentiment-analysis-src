package com.openlab.publicopinion.DataClassify.FeatureSelection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.openlab.publicopinion.DataClassify.Dictionary;
import com.openlab.publicopinion.DataClassify.FeatureMaker;

/**
 * <p>������ѡȡ�ĸ�����
 * <p>�ṩ����ӳ�����ȡ����ĵ�����Ϣ�ķ�����ʵ��������ౣ�������Ͽ����е�ͳ����Ϣ
 * ���ֻ���ͳ�Ƶ�����ѡ��ά�㷨���������Ϊ��
 * ÿ��������ÿ������е�Ƶ����������ô��ȡ���������ǻ���ʵ�ֵ�
 * @author HuHaixiao
 *
 */
public class TrainingDataManager{
	/**ѵ�����Ͽ�Ŀ¼*/
	private  String libPath;
	/**���������Ϣ������*/
	private  String[] cls;
	/**�����������������ĵ���Ŀ*/
	private int[] Nc;
	/**ѵ�����Ͽ������ĵ���*/
	private  int N;
	/**ӳ�����������ѡȡ����*/
	private Map<String, int[]> mp;
	
	/**
	 * @param libraryPath 			���Ͽ�Ŀ¼
	 */
	public TrainingDataManager(FeatureMaker feature, String libraryPath){
		this.libPath=libraryPath;
		
		File traningTextDir = new File(libPath);
		if (!traningTextDir.isDirectory()) 
		{
			throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� [" +libPath + "]");
		}
		cls=traningTextDir.list();
		Nc=new int[cls.length];
		N=0;
		for(int i=0;i<cls.length;i++){
			Nc[i]= (new File(libPath +File.separator +cls[i])).list().length;
			N+=Nc[i];
		}
		ConstructMap(feature);
	}

	/**
	 * <p>����ӳ�����������ѡȡ����
	 * <p>HashMap<String,  int[]>
	 * <p>��һ��StringΪ����,int[]Ϊ��Ӧ�����������Ƶ��
	 * <p>map��ʽ���£�
	 * <p>term1(����ID)
	 * <p>0:2(��ʾ���������2ƪ�ĵ�����term1)   1:3   
	 * <p>term2
	 * <p>0:45   1:2 
	 * <p>.
	 * <p>.
	 * <p>.
	 * <p>termN
	 * <p>......
	 * @param path ѵ�����Ͽ�Ŀ¼�����´�ŷֺ�������ϡ�
	 * @return 
	 */
	private  void ConstructMap(FeatureMaker feature){
		mp=new HashMap<String,int[]>(feature.estimateMapSize());
		String[] Docs;
		Dictionary dic=new Dictionary();
		for(int i=0;i<cls.length;i++){//��ÿһ�����
			Docs=getFilesPath(cls[i]);
			for(String doc:Docs){//��ÿһ���ĵ�
				String[] feats=feature.getFeatureFromDoc(dic.readAll(doc));
				for(String f : feats){//��ÿһ������
					addWordToMap(f, i, mp);
				}
			}
		}

	}
	
	/**
	 * ���������ӳ����
	 * @param wordID ����ID
	 * @param clsID �����������ID
	 * @param mp Ҫ�����ӳ��
	 * @see #ConstructMap()
	 */
	private void addWordToMap(String f,int clsID,Map<String, int[]> mp){
		if(mp.containsKey(f)){//����¼�˴���
			mp.get(f)[clsID]++;
		}else{//û��¼�˴�����˴�����¼����
			int[] cl=new int[cls.length];
			cl[clsID]++;
			mp.put(f,cl);
		}
	}
	
	/**
	 * ��ȡӳ���
	 * @return
	 */
	public Map<String, int[]> getMap(){
		return mp;
	}
	
	/**
	 * ��ȡѵ�����Ͽ��е���𼯺�
	 * @return
	 */
	public String[] getClassification(){
		return cls;
	}
	
	/**
	 * ��ȡ���Ͽ�Ŀ¼��������Ŀ
	 * @return
	 */
	public int getClsNum(){
		return cls.length;
	}
	
	/**
	 * ��ȡѵ�����Ͽ����ĵ�����Ŀ
	 * @return
	 */
	public  int getTotalDocNum(){
		return N;
	}
	
	/**
	 * ��ȡĳһ����е��ĵ�����Ŀ
	 * @param cls ���
	 * @return
	 */
	public int getDocNumOfCls(int clsID){
		return Nc[clsID];
	}
	
	/**
	* ����ѵ���ı���𷵻��������µ�����ѵ���ı�·����full path��
	* @param classification �����ķ���
	* @return ���������������ļ���·����full path��
	*/
	public String[] getFilesPath(String classification) 
	{
		File classDir = new File(libPath +File.separator +classification);
		String[] ret = classDir.list();
		for (int i = 0; i < ret.length; i++) 
		{
			ret[i] = libPath +File.separator +classification +File.separator +ret[i];
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dictionary dicEx=new Dictionary();
		//HashMap<Integer, HashMap<String, HashSet<String>>> mp=FP.ConstructMap("TanCorpMinTrain\\");
		String[] dicInv=dicEx.loadDictionaryInv("Test\\dictionary.txt");
		for(int i=0;i<dicInv.length;i++)
			System.out.println(i+","+dicInv[i]);
//		FeatureProcess FP=new FeatureProcess();
//		Map<String, Integer> dic=FP.loadDictionary("Test\\dictionary.txt");
//		Iterator<Map.Entry<String, Integer>> it=dic.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<String, Integer> temp=it.next();
//			System.out.println(temp.getKey()+","+temp.getValue());
//		}
//		for(String key:dic.keySet()){
//			System.out.println(key+","+dic.get(key));
//		}
	}

}
