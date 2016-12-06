import javafx.application.Platform;

/**
 * Created by danny on 11/30/16.
 */
public class Animator implements Runnable{


    @Override
    public void run() {

        while(GUI.getMaxBoxY() <= GUI.getBotLeftY()){

        	int tickRate = GUI.tickRate;

            GUI.pVelx -= ((GUI.pAccel*Math.cos(GUI.rAngle))/tickRate);

            GUI.moveRect1X(GUI.pVelx/tickRate);

            GUI.pVely += ((GUI.pAccel*Math.sin(GUI.rAngle))/tickRate);

            GUI.moveRect1Y(GUI.pVely/tickRate);

            try {
                Thread.sleep(1000/tickRate);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }


        }

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GUI.showTime();
			}
		});
    }

}
