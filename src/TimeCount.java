import java.awt.*;

/**
 * Created by danny on 12/2/16.
 */
public class TimeCount implements Runnable{


	@Override
	public void run() {

		while (GUI.getMaxBoxY() <= GUI.getBotLeftY()){

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
