package kcert.framework.util;

import java.io.IOException;

public class JRunTimeExec {
	private static JRunTimeExec exec	= null;
	
	private JRunTimeExec(){}
	
	public static JRunTimeExec getInstance(){
		synchronized (JRunTimeExec.class){
			if (exec==null) {
				exec	= new JRunTimeExec();
			}
			return exec;
		}
	}
	
	public boolean exec(String cmd){
		Runtime rt	= Runtime.getRuntime();
		Process p;
		try {
			p	= rt.exec(cmd);
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		JRunTimeExec exec	= JRunTimeExec.getInstance();
		exec.exec("E:/Batch_kcomwel/Batch_kcomwel/start.bat");
	}
}
