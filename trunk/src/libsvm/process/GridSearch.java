package libsvm.process;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.openlab.publicopinion.DataClassify.FeatureSelection.*;
import com.openlab.publicopinion.DataClassify.FeatureWeighting.*;
import com.openlab.publicopinion.DataClassify.Sentiment.SemanticFeatureMaker;
import com.openlab.publicopinion.DataClassify.Sentiment.SentiDictionary;
import com.openlab.publicopinion.DataClassify.Subject.SubjectFeatureCounter;
import com.openlab.publicopinion.DataClassify.Subject.SubjectFeatureMaker;




public class GridSearch {

	
	private  int fold=5;
	private  double c_begin=-5,c_end=15,c_step=2;
	private  double g_begin=3,g_end=-15,g_step=-2;
	private  String dataset_pathname, pass_through_string="";
	
	private int nr_local_worker = 2;
	
	private  void process_options(String[] argv){
		//String usage = "Usage:  [-log2c begin,end,step] [-log2g begin,end,step] [-v fold] [additional parameters for svm-train] dataset";
		dataset_pathname = argv[argv.length-1];
		int i = 0;
		while(i < argv.length-1){
			if(argv[i] == "-log2c"){
				i = i + 1;
				String[] tmp=argv[i].split(",");
				c_begin=Double.valueOf(tmp[0]);
				c_end=Double.valueOf(tmp[1]);
				c_step=Double.valueOf(tmp[2]);
			}else if(argv[i] == "-log2g"){
				i = i + 1;
				String[] tmp=argv[i].split(",");
				g_begin=Double.valueOf(tmp[0]);
				g_end=Double.valueOf(tmp[1]);
				g_step=Double.valueOf(tmp[2]);
			}else if(argv[i] == "-v"){
				i = i + 1;
				fold = Integer.valueOf(argv[i]);
			}else{
				pass_through_string+=argv[i]+" ";
			}
			i=i+1;
		}
		if(!pass_through_string.isEmpty())
			pass_through_string=pass_through_string.trim();
	}
	
	private  double[] range_f(double begin,double end,double step){
		double[] seq=new double[(int) Math.round(((end-begin)/step)+1)];
		int i=0;
		while(true){
			if(step>0 && begin>end)break;
			if(step<0 && begin<end)break;
			seq[i++]=begin;
			begin=begin+step;
		}
		return seq;
	}
	
	
	private  double[] permute_sequence(double[] seq){
		int n=seq.length;
		if(n<=1)return seq;
		
		int mid=n/2;
			double[] left=new double[mid];
			System.arraycopy(seq, 0, left, 0, left.length);
		left=permute_sequence(left);
			double[] right=new double[n-mid-1];
			System.arraycopy(seq, mid+1, right, 0, right.length);
		right=permute_sequence(right);
		seq[0]=seq[mid];
		int L=0,R=0,K=1;
		while(L<left.length || R<right.length){
			if(L<left.length)	seq[K++]=left[L++];
			if(R<right.length)seq[K++]=right[R++];
		}
		return seq;
	}
	
	private  ConcurrentLinkedQueue<double[]> calculate_jobs(){
		double[] c_seq = permute_sequence(range_f(c_begin,c_end,c_step));
		double[] g_seq = permute_sequence(range_f(g_begin,g_end,g_step));
		double nr_c = c_seq.length;
		double nr_g = g_seq.length;
		int i = 0;
		int j = 0;
		ConcurrentLinkedQueue<double[]> jobs=new ConcurrentLinkedQueue<double[]>();

		while(i < nr_c || j < nr_g){
			if(i/nr_c < j/nr_g){
				// increase C resolution
				for(int k=0;k<j;k++)
					jobs.add(new double[]{c_seq[i],g_seq[k]});
				i = i + 1;
			}else{
				// increase g resolution
				for(int k=0;k<i;k++)
					jobs.add(new double[]{c_seq[k],g_seq[j]});
				j = j + 1;
			}
		}
		return jobs;
	}
	
	
	private  double trainAccuracy(double c,double g)throws Exception{
		String cmdline="-c "+c+" -g "+g+" -v "+fold+" "+pass_through_string+" "+dataset_pathname;
		Double re;
		re= (new svm_train()).run(cmdline.split(" +"));//��ֹ���߳��໥Ӱ�죬��������svm_train������м���
			//System.out.println("c= "+c+" g="+g+" re="+re);
		if(re==Double.NaN)
			throw new Exception("svm_train error!");
		return re;
	}
	
	public  double[] ParameterSelection(String[] argv){
		// set parameters
		process_options(argv);
		// put jobs in queue
		ConcurrentLinkedQueue<double[]> job_queue = calculate_jobs();
		ConcurrentLinkedQueue<double[]> result_queue=new ConcurrentLinkedQueue<double[]>();
		//System.out.println("Total jobs: "+job_queue.size());
//		pair temp;
//		for(Iterator<pair>it=job_queue.iterator();it.hasNext();){
//			temp=it.next();
//			System.out.println("["+temp.c+","+temp.g+"]");
//		}
		// fire local workers
		Worker[] localworkers=new Worker[nr_local_worker];
		for(int i=0;i<localworkers.length;i++){
			localworkers[i]=new Worker("worker:"+i,job_queue, result_queue);
			localworkers[i].start();
		}
		
		for(Worker i:localworkers){
			try {
				i.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//�ȴ��߳̽���
		}

		// gather results

		double best_rate = -1;
		double best_c1,best_g1,best_c,best_g;
		best_c1=best_g1=best_c=best_g=0;
		Iterator<double[]> it=result_queue.iterator();
		double[] tmp;
		while(it.hasNext()){
			tmp=it.next();
			if( (tmp[2] > best_rate) || (tmp[2]==best_rate && tmp[1]==best_g1 && tmp[0]<best_c1)){
						best_rate = tmp[2];
						best_c1=tmp[0];
						best_g1=tmp[1];
						best_c = Math.pow(2,tmp[0]);
						best_g = Math.pow(2, tmp[1]);
			}
		}
		return new double[]{best_c, best_g, best_rate};
	}
	
	/**
	 * ���̼߳���
	 * @author HuHaixiao
	 *
	 */
	private class Worker extends Thread{
		private ConcurrentLinkedQueue<double[]> job_queue;
		private ConcurrentLinkedQueue<double[]> result_queue;
		public Worker(String name,ConcurrentLinkedQueue<double[]> job_queue,ConcurrentLinkedQueue<double[]> result_queue){
			super(name);
			this.job_queue=job_queue;
			this.result_queue=result_queue;
		}

		public void run() {
			//System.out.println(this.getName()+" start calcing...");
			double[] job;
			double cexp,gexp,rate;
			while(true){
				try {
					job=job_queue.poll();
					if(job==null)break;//�޼����������˳�
					cexp=job[0];gexp=job[1];
					rate=trainAccuracy(Math.pow(2, cexp),Math.pow(2, gexp));
					result_queue.add(new double[]{cexp, gexp, rate});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
			//System.out.println("������ϣ�"+this.getName()+" calc job:"+jobCalc);
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Feature dictionary constructs...");
		TFCWeighting TFW=new TFCWeighting();
		InformationGainWordSelection CSWS=new InformationGainWordSelection();
		long start=System.currentTimeMillis();
		
		//DFWS.GlobalDFFS("Test\\dictionary.txt","TanCorpMinTrain\\","Test\\FeatureDictionary.txt",500);
		CSWS.ConstructFeatureDictionary(new SemanticFeatureMaker(new SentiDictionary()),"TrainingData\\","Test\\FeatureDictionary.txt",1500);
		long end=System.currentTimeMillis();
		System.out.println("\tFeature dictionary ends. total time��"+(end-start)/1000.0+"s\n");
		
		
		System.out.println("Feature weight starts...");
		start=System.currentTimeMillis();
		
		System.out.println("\tTrainingData weight");
		TFW.CalcFeaturesForAllFiles_Training(new SubjectFeatureMaker(),new SubjectFeatureCounter(),"Test\\FeatureDictionary.txt", "TrainingData\\", "Test\\CategoriedFiles");
		end=System.currentTimeMillis();
		System.out.println("\t\tTrainingData weight ends. total time��"+(end-start)/1000.0+"s");
		start=System.currentTimeMillis();
		System.out.println("\tTestingData weight");
		TFW.CalcFeaturesForAllFiles_Training(new SubjectFeatureMaker(),new SubjectFeatureCounter(),"Test\\FeatureDictionary.txt", "TestingData\\", "Test\\TestFiles");
		end=System.currentTimeMillis();
		System.out.println("\t\tTestingData weight weight ends. total time��"+(end-start)/1000.0+"s");
		end=System.currentTimeMillis();
		System.out.println("Feature weight ends. \n");
		
		
		args=new String[]{"Test\\CategoriedFiles"};
		GridSearch GS=new GridSearch();
		System.out.println("Grid search starts...");
		start=System.currentTimeMillis();
		double[] t=GS.ParameterSelection(args);
		end=System.currentTimeMillis();
		System.out.println("c="+t[0]+" g="+t[1]+" accuracy="+(t[2]*100)+"%");
		System.out.println("Grid search ends.total time��"+(end-start)/1000.0+"s\n");
		
		
		
		System.out.println("svm_train starts...");
		String[] argv;
		//argv=new String[]{"-v","5","Test\\Tdata"};
		svm_train svmtrain = new svm_train();
		try {
			argv=new String[]{"-c",Double.toString(t[0]),"-g",Double.toString(t[1]),"Test\\CategoriedFiles"};
			start=System.currentTimeMillis();
			svmtrain.run(argv);
			end=System.currentTimeMillis();
			System.out.println("svm_train ends. total time��"+(end-start)/1000.0+"s\n");
			start=System.currentTimeMillis();
			System.out.println("svm_predict starts...");
			argv=new String[]{"Test\\TestFiles","Test\\CategoriedFiles.model","Test\\TestFiles.out"};
			end=System.currentTimeMillis();
			int[] tmp=svm_predict.Init(argv);
			System.out.println("svm_predict ends.accuracy:"+100.0*tmp[0]/tmp[1]+" total time��"+(end-start)/1000.0+"s\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
