/**
 * Created by danny on 12/2/16.
 */
public class TimeCount implements Runnable{
	
	private boolean stopRequested = false;


	@Override
	public void run() {

		while (GUI.getMaxBoxY() <= GUI.getBotLeftY() && (GUI.getMaxBoxY() >= GUI.getTopRightY() + 10) && GUI.pAccel > 0){

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GUI.mSecs++;

			if(GUI.mSecs%1000 == 0)
				System.out.println(GUI.mSecs);
		}

	}
	
}
