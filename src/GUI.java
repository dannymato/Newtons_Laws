import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.ArrayList;

public class GUI extends Application{
	
	private double mWeight1;
	private double mWeight2;
	private double mAccelG;
	private double mLength;
	private static double uFrk;
	private double dAngle;
	protected static double rAngle;
	private double mAccelH;
	public static volatile int mSecs;
	
	
	protected static volatile boolean running = false;
	
	
	protected static volatile double pAccel;
	protected static volatile double pAccelx;
	protected static volatile double pAccely;

	private int isKinetic = 0;
	
	private String[] mUnits = {"kg", "N"};
	
	private TextField mass1;
	private TextField mass2;
	private TextField accel;
	private TextField angle;
	private TextField length;
	private TextField friction;
	private TextField pulley;

	private Button vector;

	private static Label timeField;
	
	private int massIndex = 0;
	private int massIndex1 = 0;
	private int heightIndex = 1;
	private static double botLeftx;
	private static double botLefty;
	private double topRightx;
	private static double topRighty;
	private static double pulleyRadius = 50;
	private static double pulleyCenterX;
	private static double pulleyCenterY;

	private static double pConvert;
	
	protected static boolean stopRequested = false;
	protected static boolean wantEmDead = false;

	protected static volatile double pVelx;
	protected static volatile double pVely;
	protected static volatile double pVely2;
	protected static volatile double box2y;
	
	protected static boolean neg = false;

	private static Polygon massRect1;
	private static Polygon massRect2;
	
	private static Polygon massRect3;
	private static Polygon massRect4;
	
	private static Polygon rope1;
	private static Polygon rope2;

	private static Polygon vectorNF;
	private static Polygon vectorMG1;
	private static Polygon vectorMG2;
	private static Polygon vectorT1;
	private static Polygon vectorT2;
	private static Polygon vectorF;
	private static Polygon vectorC1;
	private static Polygon vectorC2;

	private boolean hasDrawn = false;
	public static boolean isPulley = true;
	private static boolean created = false;

	private boolean hasAnimated = false;
	
	private boolean showLAX = false;
	private boolean showRAX = false;
	private boolean showNF = false;
	private boolean showMG1 = false;
	private boolean showMG2 = false;
	private boolean showT1 = false;
	private boolean showT2 = false;
	private boolean showF = false;
	private boolean showC = false;

	private static Group group;
	
	private Polygon plane;
	
	private Circle pulleyPlane;

	private ImageView newton;
	
	private Rectangle backgroundRectangle;
	
	private static Line line;

	private double xPos = 0;
	private double yPos1 = 0;
	private double yPos2 = 0;
	private double time = 0;

	private double pHeight;

	protected static final int tickRate = 60;

	private static final double boxHeight = 100;
	private static final double boxWidth = 100;

	private final Image lAxisImg = new Image(Main.class.getResourceAsStream("AxisL.png"));
	private final Image rAxisImg = new Image(Main.class.getResourceAsStream("AxisR.png"));

	private ImageView axis1;
	private ImageView axis2;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		
		Image background = new Image(Main.class.getResourceAsStream("background.jpg"));
		backgroundRectangle = new Rectangle();
		backgroundRectangle.setWidth(3000);
		backgroundRectangle.setHeight(3000);
		
		backgroundRectangle.setStroke(Color.BLACK);
		
		backgroundRectangle.setFill(Color.LIGHTSKYBLUE);
		
		
		
		group = new Group();
		group.setStyle("-fx-background-color :#5F9EA0;");
		
		group.getChildren().add(backgroundRectangle);
		
		
		
		
		final GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		//grid.setBackground(new Background(myBI));

		newton = new ImageView(new Image(Main.class.getResourceAsStream("Newton.jpg")));
		newton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

		primaryStage.setTitle("Newton's Laws");
		
		mass1 = new TextField();
		mass1.setPromptText("kg");

		if(isPulley) {
			mass2 = new TextField();
			mass2.setPromptText("kg");
		}
		final Button btn = new Button();
		btn.setText("Animate");
		btn.setOnAction(event -> {
            created = false;
            running = true;
            if(hasDrawn){
                group.getChildren().remove(massRect1);
                group.getChildren().remove(massRect2);
                group.getChildren().remove(massRect3);
                group.getChildren().remove(massRect4);
                group.getChildren().remove(line);
                group.getChildren().remove(plane);
                group.getChildren().remove(pulleyPlane);
                group.getChildren().remove(rope1);
                group.getChildren().remove(rope2);
                group.getChildren().remove(vectorNF);
                group.getChildren().remove(vectorMG1);
                group.getChildren().remove(vectorF);
                group.getChildren().remove(vectorT1);
                group.getChildren().remove(vectorMG2);
                group.getChildren().remove(vectorT2);
                group.getChildren().remove(axis1);
                group.getChildren().remove(axis2);

                restart();
                xPos = 0;
                yPos2 = 0;
                yPos1 = 0;
            }
            if(startAnimation()){
                drawRamp(primaryStage);
                drawBox();
                btn.setDisable(true);
                if(calculateAcc() == 0){
                        time = 0;
                    }
                    else{
                        time = calculateTime();
                    }
                    Animation animator = new Transition(){
                        {
                            setCycleDuration(Duration.seconds(time));
                            setDelay(Duration.millis(16));
                            System.out.print(time/.016);
                            setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    showTime();
                                    hasAnimated = true;
                                    btn.setDisable(false);
                                }
                            });
                            setInterpolator(Interpolator.LINEAR);

                        }


                        @Override
                        protected void interpolate(double v) {
                            if(getCurrentTime().lessThan(getTotalDuration())) {
                                if(!isPulley) {
                                    System.out.println(getCurrentTime());
                                    double currTime = getCurrentTime().toSeconds();
                                    double x = pAccelx * 0.5 * currTime * currTime;
                                    double deltax = x - xPos;
                                    xPos = x;
                                    moveRect1X(-deltax);
                                    double y = pAccely * 0.5 * currTime * currTime;
                                    double deltay = y - yPos1;
                                    yPos1 = y;
                                    moveRect1Y(deltay);
                                    System.out.println(getCurrentTime());
                                }
                                else{
                                    System.out.println(getCurrentTime());
                                    double currTime = getCurrentTime().toSeconds();
                                    double x1 = pAccelx * 0.5 * currTime * currTime;
                                    double deltax = x1 - xPos;
                                    xPos = x1;
                                    moveRect1X(-deltax);
                                    double y1 = pAccely * 0.5 * currTime * currTime;
                                    double deltay1 = y1 - yPos1;
                                    yPos1 = y1;
                                    moveRect1Y(deltay1);

                                    double y2 = pAccel * 0.5 * currTime * currTime;
                                    double deltay2 = y2 - yPos2;
                                    yPos2 = y2;
                                    moveRect2Y(-deltay2);


                                }
                            }


                        }
                    };
                    animator.play();



                }
            }
		);
		
		ObservableList<String> options = FXCollections.observableArrayList ("Mass 1", "Weight 1");
		
		final ComboBox<String> massCombo = new ComboBox<>(options);
		massCombo.setValue("Mass 1");
		
		massCombo.setOnAction(event -> {
            massIndex = massCombo.getSelectionModel().getSelectedIndex();
            mass1.setPromptText(mUnits[massIndex]);
        });
		ObservableList<String> options1 = FXCollections.observableArrayList("Mass 2", "Weight 2");
		final ComboBox<String> massCombo1 = new ComboBox<>(options1);
		if(isPulley) {

			massCombo1.setValue("Mass 2");

			massCombo1.setOnAction(event -> {
                massIndex1 = massCombo1.getSelectionModel().getSelectedIndex();
                mass2.setPromptText(mUnits[massIndex1]);
            });
		}
		
		accel = new TextField();
		accel.setPromptText("m/s\u00b2");
		
		final Label tAccel = new Label();
		tAccel.setText("Acceleration of Gravity");
		
		final Label tAngle = new Label();
		tAngle.setText("Angle");
		
		angle = new TextField();
		angle.setPromptText("Degrees");
		
		final Label tLength = new Label();
		tLength.setText("Length of Incline");
		
		length = new TextField();
		length.setPromptText("meters");
		
		ObservableList<String> optionsFriction = FXCollections.observableArrayList ("\u00B5\u2096", "\u00B5\u209B");

		final ComboBox<String> frictionCombo = new ComboBox<>(optionsFriction);
		frictionCombo.setValue("\u00B5\u2096");
		
		frictionCombo.setOnAction(event -> isKinetic = frictionCombo.getSelectionModel().getSelectedIndex());
		
		ObservableList<String> optionsPulley = FXCollections.observableArrayList ("Pulley", "No Pulley");
		
		final ComboBox<String> pulleyCombo = new ComboBox<>(optionsPulley);
		pulleyCombo.setValue("Pulley");
		
		pulleyCombo.setOnAction(event -> isPulley = pulleyCombo.getValue().equals("Pulley"));
		
		ObservableList<String> optionsStart = FXCollections.observableArrayList ("Top", "Middle", "Bottom");
		
		final ComboBox<String> boxCombo = new ComboBox<>(optionsStart);
		boxCombo.setValue("Middle");
		
		boxCombo.setOnAction(event -> {
            if(boxCombo.getValue().equals("Top")){
                heightIndex = 0;
            } else if(boxCombo.getValue().equals("Middle")){
                heightIndex = 1;
            } else {
                heightIndex = 2;
            }
        });

		Button solutionButton = new Button("Solutions");
		solutionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openSolvingPage();
			}
		});
		
		final Label pulleyField = new Label();
		pulleyField.setText("Use a Pulley?");
		
		ObservableList<String> opIsPulley = FXCollections.observableArrayList ("Yes", "No");
		
		final ComboBox<String> comboPulley = new ComboBox<>(opIsPulley);
		comboPulley.setValue("Yes");

		
		comboPulley.setOnAction(event -> {
            if(comboPulley.getValue().equals("Yes")){
                isPulley = true;
                mass2 = new TextField();
                mass2.setPromptText("kg");
                grid.getChildren().clear();
                grid.add(newton,0,0,2,3);
                grid.add(mass1, 1, 3);
                grid.add(massCombo, 0, 3);
                grid.add(mass2, 1, 4);
                grid.add(massCombo1, 0, 4);
                grid.add(accel, 1, 5);
                grid.add(tAccel, 0, 5);
                grid.add(tAngle, 0, 6);
                grid.add(angle, 1, 6);
                grid.add(tLength, 0, 7);
                grid.add(length, 1, 7);
                grid.add(frictionCombo, 0, 8);
                grid.add(friction, 1, 8);
                //grid.add(pulleyCombo, 0, 9);
                //grid.add(pulley, 1, 9);
                grid.add(btn, 1, 9);
                grid.add(timeField,1,10);
                grid.add(boxCombo, 0, 11);
                grid.add(pulleyField, 0, 12);
                grid.add(comboPulley, 1, 12);

                grid.add(vector,0,9);
				grid.add(solutionButton, 1,11);
            } else {
                isPulley = false;
                grid.getChildren().clear();
                grid.add(newton,0,0,2,3);
                grid.add(mass1, 1, 3);
                grid.add(massCombo, 0, 3);
                grid.add(accel, 1, 4);
                grid.add(tAccel, 0, 4);
                grid.add(tAngle, 0, 5);
                grid.add(angle, 1, 5);
                grid.add(tLength, 0, 6);
                grid.add(length, 1, 6);
                grid.add(frictionCombo, 0, 7);
                grid.add(friction, 1, 7);
                //grid.add(pulleyCombo, 0, 8);
                //grid.add(pulley, 1, 8);
                grid.add(btn, 1, 9);
                grid.add(timeField,1,10);
                grid.add(pulleyField, 0, 12);
                grid.add(comboPulley, 1, 12);
                grid.add(vector,0,8);
				grid.add(solutionButton, 1,11);
            }
        });
		
		
		
		friction = new TextField();

		timeField = new Label();

		vector = new Button("Vector Controls");
		vector.setOnAction(actionEvent -> showVectors());


		
		if(isPulley) {
			grid.add(newton,0,0,2,3);
			grid.add(mass1, 1, 3);
			grid.add(massCombo, 0, 3);
			grid.add(mass2, 1, 4);
			grid.add(massCombo1, 0, 4);
			grid.add(accel, 1, 5);
			grid.add(tAccel, 0, 5);
			grid.add(tAngle, 0, 6);
			grid.add(angle, 1, 6);
			grid.add(tLength, 0, 7);
			grid.add(length, 1, 7);
			grid.add(frictionCombo, 0, 8);
			grid.add(friction, 1, 8);
			//grid.add(pulleyCombo, 0, 9);
			//grid.add(pulley, 1, 9);
			grid.add(btn, 1, 9);
			grid.add(timeField,1,10);
			grid.add(boxCombo, 0, 11);
			grid.add(pulleyField, 0, 12);
			grid.add(comboPulley, 1, 12);
			grid.add(vector,0,9);
			grid.add(solutionButton, 1,11);
		}
		else{
			grid.add(newton,0,0,2,3);
			grid.add(mass1, 1, 3);
			grid.add(massCombo, 0, 3);
			grid.add(accel, 1, 4);
			grid.add(tAccel, 0, 4);
			grid.add(tAngle, 0, 5);
			grid.add(angle, 1, 5);
			grid.add(tLength, 0, 6);
			grid.add(length, 1, 6);
			grid.add(frictionCombo, 0, 7);
			grid.add(friction, 1, 7);
			//grid.add(pulleyCombo, 0, 8);
			//grid.add(pulley, 1, 8);
			//grid.add(btn, 1, 9);
			//grid.add(timeField,1,10);
			grid.add(pulleyField, 0, 12);
			grid.add(comboPulley, 1, 12);
			grid.add(btn, 1, 8);
			grid.add(timeField,1,9);
			grid.add(vector,0,8);
			grid.add(solutionButton,1,11);
		}
		
		primaryStage.setOnCloseRequest(e -> Platform.exit());




		group.getChildren().add(grid);

		primaryStage.setScene(new Scene(group, 1280,1024));
		primaryStage.setMaximized(true);
		primaryStage.show();
		
	}
	
	private boolean startAnimation(){
		
		String temp;
		
		temp = accel.getText();
		if(temp.equalsIgnoreCase("delrio")) {
			newton.setImage(new Image(Main.class.getResourceAsStream("Delrio.jpg")));
			timeField.setText("\"Before they discovered the strong force, they didn't know about the strong force\""+
								"\n\t\t-Delrio");
		}
		else if(!temp.equals("")) {
			mAccelG = Double.parseDouble(temp);
			mAccelG = Math.abs(mAccelG);
			temp = "";
		}
		else
			return false;
		
		temp = mass1.getText();
		if(!temp.equals("")){
			mWeight1 = Double.parseDouble(temp);
			if(massIndex == 0){
				mWeight1 = mWeight1*mAccelG;
			}
			temp = "";
		}
		else
			return false;
		if(isPulley) {
			temp = mass2.getText();
			if (!temp.equals("")) {
				mWeight2 = Double.parseDouble(temp);
				if (massIndex == 0) {
					mWeight2 = mWeight2 * mAccelG;
				}
				temp = "";
			} else
				return false;
		}
		
		temp = friction.getText();
		if(!temp.equals("")){
			uFrk = Double.parseDouble(temp);
			temp = "";
		}
		else
			return false;
		
		temp = angle.getText();
		if(!temp.equals("")){
			dAngle = Double.parseDouble(temp);
			if(dAngle > 90) {
				return false;
			}
			rAngle = (dAngle*Math.PI)/180;
			temp = "";
		}
		else
			return false;
		
		temp = length.getText();
		if(!temp.equals("")){
			mLength = Double.parseDouble(temp);
			temp = "";
		}
		else
			return false;
		
		//System.out.println(mWeight1 + " " + mWeight2 + " " + uFrk + " " + uFrs + " " + mAccelG + " " + mLength + " " + dAngle + " " + rAngle);
		
		return true;
		
	}
	
	private void drawRamp(Stage stage){
		
		int windowH = (int)stage.getHeight();
		int windowW = (int)stage.getWidth();
		
		Image wood = new Image(Main.class.getResourceAsStream("wood_floor.jpg"));
		
		Image pulley = new Image(Main.class.getResourceAsStream("pulley.jpg"));
		
		botLeftx = windowW/3;
		botLefty = (int)(windowH*.8);

		plane = new Polygon();
		
		pulleyPlane = new Circle();
		
		if(dAngle < 45){
		
			double mWidth = (int) (mLength*Math.cos(rAngle));
			System.out.println("Width" + mWidth + " Angle" + rAngle);
			pConvert = 600/mWidth;
				
			double mHeight = (int)(mLength*Math.sin(rAngle));
			pHeight = mHeight*pConvert;

			plane.getPoints().addAll(new Double[]{
					botLeftx,botLefty,
					botLeftx+600,botLefty,
					botLeftx+600,botLefty-pHeight
			});

			topRightx = botLeftx+600;
			topRighty = botLefty-pHeight;
			
			pulleyCenterX = topRightx + pulleyRadius;
			pulleyCenterY = topRighty - pulleyRadius;
		}
		else{
			int mHeight = (int)(mLength*Math.sin(rAngle));

			pHeight = botLefty - 200;

			pConvert = pHeight/mHeight;


			double mWidth = (int)(mLength*Math.cos(rAngle));
			double pWidth = mWidth*pConvert;
			
			plane.getPoints().addAll(new Double[]{
					botLeftx+600,200.0,
					botLeftx+600,botLefty,
					(botLeftx+600)-pWidth,botLefty
			});

			topRightx = botLeftx + 600;
			topRighty = 200.0;
			
			pulleyCenterX = topRightx + pulleyRadius;
			pulleyCenterY = topRighty - pulleyRadius;
			
		}
		
		plane.setStroke(Color.BLACK);
		
		plane.setFill(new ImagePattern(wood,0,0,2,2,true));
		
		pulleyPlane.setStroke(Color.BLACK);
		
		pulleyPlane.setFill(new ImagePattern(pulley));
		
		if(isPulley){
			pulleyPlane.setCenterX(pulleyCenterX);
			pulleyPlane.setCenterY(pulleyCenterY);
			pulleyPlane.setRadius(pulleyRadius);
		}
		
		System.out.println(hasDrawn);
		
		if(!hasDrawn){
			group.getChildren().add(plane);
			group.getChildren().add(pulleyPlane);
			hasDrawn = true;
		}
		else{
			group.getChildren().remove(plane);
			group.getChildren().add(plane);
			group.getChildren().remove(pulleyPlane);
			group.getChildren().add(pulleyPlane);
		}
		
		
		
	}
	
	private double calculateAcc(){
		if(!isPulley){
		mAccelH = (mAccelG*Math.sin(rAngle))-(uFrk*mAccelG*Math.cos(rAngle));
		} else {
			if(((mWeight1*Math.sin(rAngle)) >= mWeight2)){
				if((mWeight2 + (uFrk*mWeight1*Math.cos(rAngle))) > (mWeight1*Math.sin(rAngle))){
					mAccelH = 0;
				} else {
					mAccelH = -((mWeight2) - (mWeight1*Math.sin(rAngle)) + (uFrk*mWeight1*Math.cos(rAngle)))/((mWeight1+mWeight2)/mAccelG);
				}
			} else {
				if(((mWeight1*Math.sin(rAngle)) + (uFrk*mWeight1*Math.cos(rAngle))) > (mWeight2)){
					mAccelH = 0;
				} else {
					mAccelH = ((mWeight1*Math.sin(rAngle))-mWeight2+(uFrk*mWeight1*Math.cos(rAngle)))/((mWeight1+mWeight2)/mAccelG);
				}
			}
		}
		pAccel = mAccelH*pConvert;
		pAccelx = pAccel*Math.cos(rAngle);
		pAccely = pAccel*Math.sin(rAngle);

		
		if(!isPulley){
			if(mAccelH < 0)
				return 0;
		}
		//else
			return mAccelH;
	}

	private double calculateTime(){
		if(isPulley) {
			ArrayList<Double> times = new ArrayList<>();

			if (pAccel < 0)
				times.add(Math.sqrt(((((mLength*pConvert)/2))*2)/Math.abs(pAccel)));
			else
				times.add(Math.sqrt(((((mLength*pConvert)/2) - (boxWidth))*2)/Math.abs(pAccel)));

			switch (heightIndex){
				case 0:
					if (pAccel < 0)
						times.add(Math.sqrt(((pHeight-(boxWidth))*2)/Math.abs(pAccel)));
					else
						return 0.0;
					break;
				case 1:
					if (pAccel > 0)
						times.add(Math.sqrt(((pHeight/2)*2)/Math.abs(pAccel)));
					else
						times.add(Math.sqrt((((pHeight/2)-boxWidth)*2)/Math.abs(pAccel)));
					break;
				case 2:
					if (pAccel < 0)
						return 0.0;
					break;
			}
			System.out.println(times);
			return max(times.toArray(new Double[times.size()]));
		}
		else
			return Math.sqrt((((mLength*pConvert)-boxWidth*1.5)*2)/Math.abs(pAccel));
	}

	private void drawBox() {

		double rBotLeftx;
		double rBotLefty;
		
		double rBotLeftx2;
		double rBotLefty2;
		double boxHeight = 100;
		double boxWidth = 100;
		
		double boxWidth2 = 1000;
		
		double ropeBotLeftx;
		double ropeBotLefty;
		
		double ropeHeight = 5;
		
		box2y = 0;
		
		if(heightIndex == 0){
			box2y = topRighty;
		} else if(heightIndex == 1){
			box2y = topRighty + ((0.5)*(botLefty - topRighty));
		} else {
			box2y = botLefty - 100;
		}
		if((box2y + 100) > botLefty){
			box2y = botLefty + 100;
		}


		double ropeStartX = pulleyPlane.getCenterX();
		double ropeStartY = pulleyPlane.getCenterY();
		
		
		
		
		if(!isPulley){
			massRect1 = new Polygon();
			massRect1.getPoints().addAll(new Double[] {
					/*bot Right*/ topRightx, topRighty,
					/*bot left*/  rBotLeftx = topRightx - (boxWidth * Math.cos(rAngle)), rBotLefty = topRighty + (boxWidth * Math.sin(rAngle)),
					/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
					/*top right*/ topRightx - (boxHeight * Math.sin(rAngle)), topRighty - (boxHeight * Math.cos(rAngle))
						}
					);
		

			Image metal = new Image(Main.class.getResourceAsStream("metal-texture.jpg"));
		massRect1 = new Polygon();
		massRect1.getPoints().addAll(new Double[] {
		/*bot Right*/ topRightx, topRighty,
		/*bot left*/  rBotLeftx = topRightx - (boxWidth * Math.cos(rAngle)), rBotLefty = topRighty + (boxWidth * Math.sin(rAngle)),
		/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
		/*top right*/ topRightx - (boxHeight * Math.sin(rAngle)), topRighty - (boxHeight * Math.cos(rAngle))
				}
		);

			massRect1.setFill(new ImagePattern(metal));

			massRect1.setStroke(Color.BLACK);

			group.getChildren().add(massRect1);

			System.out.println(String.valueOf(massRect1.getScaleX()));
			System.out.println(String.valueOf(massRect1.localToScene(massRect1.getLayoutBounds())));
		} else {
			
			massRect1 = new Polygon();
			massRect3 = new Polygon();
			rope1 = new Polygon();
			if(dAngle < 45){
			massRect1.getPoints().addAll(new Double[] {
					/*bot Right*/ (botLeftx + topRightx)/2, topRighty + (0.5*(botLefty - topRighty)),
					/*bot left*/  rBotLeftx = (botLeftx + topRightx)/2 - (boxWidth * Math.cos(rAngle)), rBotLefty = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth * Math.sin(rAngle)),
					/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
					/*top right*/ (botLeftx + topRightx)/2 - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
						}
					);
			
			massRect3.getPoints().addAll(new Double[] {
					/*bot Right*/ (botLeftx + topRightx)/2, topRighty + (0.5*(botLefty - topRighty)),
					/*bot left*/  rBotLeftx2 = (botLeftx + topRightx)/2 - (boxWidth2 * Math.cos(rAngle)), rBotLefty2 = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth2 * Math.sin(rAngle)),
					/*top left*/  rBotLeftx2 - (boxHeight * Math.sin(rAngle)), rBotLefty2 - (boxHeight * Math.cos(rAngle)),
					/*top right*/ (botLeftx + topRightx)/2 - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
						}
					);
			
			//line = new Line();
			
			//line.startXProperty().bind(pulleyPlane.centerXProperty().add(pulleyPlane.translateXProperty()));
			//line.startYProperty().bind(pulleyPlane.centmassRect2.equals(null)erYProperty().add(pulleyPlane.translateYProperty()));
			//line.setEndX(massRect1.getLayoutBounds().getMaxX());
			//line.setEndY(massRect1.getLayoutBounds().getMaxY());
			
//			rope1.getPoints().addAll(new Double[] {
//					/*bot Right*/ pulleyCenterX + (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY + (ropeHeight/2)*Math.sin(rAngle),
//					/*bot left*/  ropeBotLeftx = ((((botLeftx + topRightx)/2 - (boxHeight * Math.sin(rAngle))) + ((botLeftx + topRightx)/2))/2) + (ropeHeight/2)*Math.cos(rAngle), ropeBotLefty = ((((topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle)) + (topRighty + (0.5*(botLefty - topRighty))))/2) + (ropeHeight/2)*Math.sin(rAngle)),
//					/*top left*/  ropeBotLeftx - (ropeHeight/2)*Math.sin(rAngle), ropeBotLefty - ((ropeHeight/2) * Math.cos(rAngle)),
//					/*top right*/ pulleyCenterX - (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY - (ropeHeight/2)*Math.sin(rAngle)
//						}
//					);
//			
//			}
			
			rope1.getPoints().addAll(new Double[] {
					/*bot Right*/ pulleyCenterX + (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY + (ropeHeight/2)*Math.sin(rAngle),
					/*bot left*/  ropeBotLeftx = botLeftx - ((pulleyCenterX + (ropeHeight/2)*Math.cos(rAngle)) - topRightx), ropeBotLefty = botLefty,
					/*top left*/  ropeBotLeftx - (ropeHeight/2)*Math.sin(rAngle), ropeBotLefty - ((ropeHeight/2) * Math.cos(rAngle)),
					/*top right*/ pulleyCenterX - (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY - (ropeHeight/2)*Math.sin(rAngle)
						}
					);
			
			}
			else {
				massRect1.getPoints().addAll(new Double[] {
						/*bot Right*/ ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)
								/(int)(mLength*Math.sin(rAngle)))/2)), topRighty + (0.5*(botLefty - topRighty)),
						/*bot left*/  rBotLeftx = ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)) - (boxWidth * Math.cos(rAngle)), rBotLefty = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth * Math.sin(rAngle)),
						/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
						/*top right*/ ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)) - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
							}
						);
				
				massRect3.getPoints().addAll(new Double[] {
						/*bot Right*/ ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)), topRighty + (0.5*(botLefty - topRighty)),
						/*bot left*/  rBotLeftx2 = ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)) - (boxWidth2 * Math.cos(rAngle)), rBotLefty2 = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth2 * Math.sin(rAngle)),
						/*top left*/  rBotLeftx2 - (boxHeight * Math.sin(rAngle)), rBotLefty2 - (boxHeight * Math.cos(rAngle)),
						/*top right*/ ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)) - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
							}
						);
				rope1.getPoints().addAll(new Double[] {
						botLeftx+600,pulleyCenterY,
						((botLeftx+600)-(((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle))))) - (-pulleyCenterY + topRighty),botLefty,
						((botLeftx+600)-(((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle))))) - (-pulleyCenterY + topRighty) - ((ropeHeight)*Math.cos(rAngle)),botLefty,
						(botLeftx+600),pulleyCenterY - (ropeHeight)*Math.sin(rAngle)
							}
						);
				//line = new Line();
				
				//line.startXProperty().bind(pulleyPlane.centerXProperty().add(pulleyPlane.translateXProperty()));
				//line.startYProperty().bind(pulleyPlane.centerYProperty().add(pulleyPlane.translateYProperty()));
				//line.setEndX(massRect1.getLayoutBounds().getMaxX());
				//line.setEndY(massRect1.getLayoutBounds().getMaxY());
			}
			
			
		

			Image metal = new Image(Main.class.getResourceAsStream("metal-texture.jpg"));
			Image rope = new Image(Main.class.getResourceAsStream("rope.jpg"));

			massRect1.setFill(new ImagePattern(metal));

			massRect1.setStroke(Color.BLACK);
			
			massRect3.setFill(Color.LIGHTSKYBLUE);
			
			rope1.setFill(new ImagePattern(rope));
			
			rope1.setStroke(Color.BLACK);
			
			
			
			group.getChildren().add(rope1);
			group.getChildren().add(massRect3);
			group.getChildren().add(massRect1);
			//group.getChildren().add(line);
			

			System.out.println(String.valueOf(massRect1.getScaleX()));
			System.out.println(String.valueOf(massRect1.localToScene(massRect1.getLayoutBounds())));
		
			massRect2 = new Polygon();
			massRect4 = new Polygon();
			rope2 = new Polygon();
			
			massRect2.getPoints().addAll(new Double[] {
				/*bot Right*/ topRightx + 100, box2y + 100,
				/*bot left*/  rBotLeftx2 = topRightx, rBotLefty2 = box2y + 100,
				/*top left*/  topRightx, box2y,
				/*top right*/ topRightx + 100, box2y
			});
			
			massRect4.getPoints().addAll(new Double[] {
					/*bot Right*/ topRightx + 100, box2y + 1000,
					/*bot left*/  rBotLeftx2 = topRightx, rBotLefty2 = box2y + 1000,
					/*top left*/  topRightx, box2y,
					/*top right*/ topRightx + 100, box2y
				});
			
			rope2.getPoints().addAll(new Double[] {
					/*bot Right*/ pulleyCenterX + ropeHeight/2, botLefty,
					/*bot left*/  pulleyCenterX - ropeHeight/2, botLefty,
					/*top left*/  pulleyCenterX - ropeHeight/2, pulleyCenterY,
					/*top right*/ pulleyCenterX + ropeHeight/2, pulleyCenterY
				});
		
			massRect2.setFill(new ImagePattern(metal));
		
			massRect2.setStroke(Color.BLACK);
			
			massRect4.setFill(Color.LIGHTSKYBLUE);
			
			rope2.setFill(new ImagePattern(rope));
			
			rope2.setStroke(Color.BLACK);
			
			group.getChildren().add(rope2);
			group.getChildren().add(massRect4);
			group.getChildren().add(massRect2);
			created = true;
			
		}
		
//		if(hasAnimated){
//			if(showNF){
//				vectorNF = new Polygon();
//				if(dAngle < 45){
////					vectorNF.getPoints().addAll(new Double[] {
////						/*bot Right*/ (botLeftx + topRightx)/2, topRighty + (0.5*(botLefty - topRighty)),
////						/*bot left*/  rBotLeftx = (botLeftx + topRightx)/2 - (boxWidth * Math.cos(rAngle)), rBotLefty = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth * Math.sin(rAngle)),
////						/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
////						/*top right*/ (botLeftx + topRightx)/2 - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
////								}
////							);
//					
//					vectorNF.getPoints().addAll(massRect1.getPoints());
//				
//				} else {
//				
//				}
////			vectorNF = new Polygon();
////			vectorNF.getPoints().addAll(new Double[] {
////					/*bot Right*/ topRightx - 30, topRighty + 30,
////					/*bot left*/  rBotLeftx = topRightx - (10 * Math.cos(rAngle)) - 30, rBotLefty = topRighty + (10 * Math.sin(rAngle)) + 30,
////					/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
////					/*top right*/ topRightx - (boxHeight * Math.sin(rAngle)) - 30, topRighty - (boxHeight * Math.cos(rAngle)) + 30
////					}
////			);
//			
//				vectorNF.setFill(Color.RED);
//			
//				group.getChildren().add(vectorNF);
//			//System.exit(0);
//			}
//		}
		
		
		

	}

	public double getMinBoxX(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMinX();}

	public double getMaxBoxX(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMaxX();}

	public static double getMinBoxY(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMinY();}

	public static double getMaxBoxY(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMaxY();}
	
	public static double getMaxBoxY2(){
		
		if(created)
			return massRect2.localToScene(massRect2.getLayoutBounds()).getMaxY();
		else
			return 50;
	}

	public static double getBotLeftY(){return botLefty;}
	
	public static double getTopRightY(){return topRighty;}

	public static void moveRect1X(double deltaX){
		massRect1.setLayoutX(massRect1.getLayoutX()+deltaX);
		if(isPulley)
			massRect3.setLayoutX(massRect1.getLayoutX()+deltaX);
	}

	public static void moveRect1Y(double deltaY){
		massRect1.setLayoutY(massRect1.getLayoutY()+deltaY);
		if(isPulley)
			massRect3.setLayoutY(massRect1.getLayoutY()+deltaY);
	}
	
	public static void moveRect2X(double deltaX){
		massRect2.setLayoutX(massRect2.getLayoutX()+deltaX);
		massRect4.setLayoutX(massRect2.getLayoutX()+deltaX);
	}

	public static void moveRect2Y(double deltaY){
		massRect2.setLayoutY(massRect2.getLayoutY()+deltaY);
		massRect4.setLayoutY(massRect2.getLayoutY()+deltaY);
	}

	public static void restart(){
		pVelx = 0;
		pVely = 0;
		pAccelx = 0;
		pAccely = 0;
		pConvert = 0;
		mSecs = 0;
		timeField.setText("");
	}

	public void showTime(){
		System.out.println(time);
		timeField.setText("Time = " + String.format("%.2f",time) + " s");
	}

	public void showVectors(){

		Stage vStage = new Stage();
		GridPane vGrid = new GridPane();

		vStage.setHeight(430);
		vStage.setWidth(200);

		vGrid.setPadding(new Insets(10));
		vGrid.setAlignment(Pos.CENTER);
		vGrid.setHgap(10);
		vGrid.setVgap(10);

		Button lAxis = new Button();
		lAxis.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("AxisL.png"),50,50,true,true)));
		lAxis.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!showLAX){
					if(showRAX) {
						showRAX = false;
						axis1.toBack();
						group.getChildren().remove(axis1);
						if(isPulley){
							axis2.toBack();
							group.getChildren().remove(axis2);
						}
					}

					axis1 = new ImageView(new Image(Main.class.getResourceAsStream("AxisL.png"),100,100,true,true));
					axis1.setLayoutX(getMinBoxX()-50);
					axis1.setLayoutY(getMinBoxY() - 150);
					axis1.setRotate(-dAngle);
					group.getChildren().add(axis1);

					if(isPulley){
						axis2 = new ImageView(new Image(Main.class.getResourceAsStream("AxisL.png"),100,100,true,true));
						axis2.setLayoutX(pulleyCenterX + 100);
						axis2.setLayoutY(pulleyCenterY - 50);
						axis2.setRotate(90.0);
						group.getChildren().add(axis2);
					}


				}
				else{
					System.out.println("Remove axis");
					axis1.toBack();
					axis2.toBack();
					group.getChildren().remove(axis1);
					group.getChildren().remove(axis2);
				}
				showLAX = !showLAX;
			}
			
		});

		Button rAxis = new Button();
		rAxis.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("AxisR.png"),50,50,true,true)));
		rAxis.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!showRAX) {
					if (showLAX) {
						showLAX = false;
						group.getChildren().remove(axis1);
						if(isPulley){
							axis2.toBack();
							group.getChildren().remove(axis2);
						}
					}

					axis1 = new ImageView(new Image(Main.class.getResourceAsStream("AxisR.png"), 100, 100, true, true));
					axis1.setLayoutX(getMinBoxX() - 50);
					axis1.setLayoutY(getMinBoxY() - 150);
					axis1.setRotate(-dAngle);
					group.getChildren().add(axis1);

					if(isPulley){
						axis2 = new ImageView(new Image(Main.class.getResourceAsStream("AxisR.png"),100,100,true,true));
						axis2.setLayoutX(pulleyCenterX + 100);
						axis2.setLayoutY(pulleyCenterY - 50);
						axis2.setRotate(90.0);
						group.getChildren().add(axis2);
					}

				} else {
					System.out.println("Remove axis");
					axis1.toBack();
					axis2.toBack();
					group.getChildren().remove(axis1);
					group.getChildren().remove(axis2);
				}
				showRAX = !showRAX;
			}
			
		});

		Label m1 = new Label("m\u2081");
		Label m2 = new Label("m\u2082");

		Button normalF = new Button(" N ");
		normalF.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				double privx = (massRect1.getPoints().get(0) - massRect1.getPoints().get(2)) / 1.9;
				double privy = (massRect1.getPoints().get(3) - massRect1.getPoints().get(1)) / 1.9;
				
				vectorNF = new Polygon();
				vectorNF.getPoints().addAll(new Double[] {
						massRect1.getPoints().get(0) - privx, massRect1.getPoints().get(1) + privy, //bot right
						massRect1.getPoints().get(2) + privx, massRect1.getPoints().get(3) - privy,//bot left
						massRect1.getPoints().get(4) + privx, massRect1.getPoints().get(5) - privy,//top left
						massRect1.getPoints().get(6) - privx, massRect1.getPoints().get(7) + privy//top right
				});
				vectorNF.setLayoutX(massRect1.getLayoutX());
				vectorNF.setLayoutY(massRect1.getLayoutY());
				vectorNF.setFill(Color.RED);
			
				group.getChildren().add(vectorNF);
			}
			
		});

		Button mg1 = new Button("m\u2081g");
		mg1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				double privx = (massRect1.getPoints().get(0) - massRect1.getPoints().get(2)) / 1.9;
				double privy = (massRect1.getPoints().get(3) - massRect1.getPoints().get(1)) / 1.9;
				
				vectorMG1 = new Polygon();
				vectorMG1.getPoints().addAll(new Double[] {
						massRect1.getPoints().get(0) - ((massRect1.getPoints().get(0) - massRect1.getPoints().get(4))/2) - 3, massRect1.getPoints().get(3) - ((massRect1.getPoints().get(3) - massRect1.getPoints().get(7))/2) + 100, //bot right
						massRect1.getPoints().get(0) - ((massRect1.getPoints().get(0) - massRect1.getPoints().get(4))/2) + 3, massRect1.getPoints().get(3) - ((massRect1.getPoints().get(3) - massRect1.getPoints().get(7))/2) + 100,//bot left
						massRect1.getPoints().get(0) - ((massRect1.getPoints().get(0) - massRect1.getPoints().get(4))/2) + 3, massRect1.getPoints().get(3) - ((massRect1.getPoints().get(3) - massRect1.getPoints().get(7))/2),//top left
						massRect1.getPoints().get(0) - ((massRect1.getPoints().get(0) - massRect1.getPoints().get(4))/2) - 3, massRect1.getPoints().get(3) - ((massRect1.getPoints().get(3) - massRect1.getPoints().get(7))/2)//top right
				});
				vectorMG1.setLayoutX(massRect1.getLayoutX());
				vectorMG1.setLayoutY(massRect1.getLayoutY());
				vectorMG1.setFill(Color.BLUE);
			
				group.getChildren().add(vectorMG1);
			}
			
		});

		Button mg2 = new Button("m\u2082g");
		mg2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				if(isPulley){
					double privx = (massRect2.getPoints().get(0) - massRect2.getPoints().get(2)) / 2;
					double privy = (massRect2.getPoints().get(3) - massRect2.getPoints().get(5)) / 2;
					
					vectorMG2 = new Polygon();
					vectorMG2.getPoints().addAll(new Double[] {
							massRect2.getPoints().get(0) - 50 + 3, massRect2.getPoints().get(1) + 50, //bot right
							massRect2.getPoints().get(2) + 50 - 3, massRect2.getPoints().get(3) + 50,//bot left
							massRect2.getPoints().get(4) + 50 - 3, massRect2.getPoints().get(5) + 50,//top left
							massRect2.getPoints().get(6) - 50 + 3, massRect2.getPoints().get(7) + 50//top right
					});
					vectorMG2.setLayoutX(massRect2.getLayoutX());
					vectorMG2.setLayoutY(massRect2.getLayoutY());
					vectorMG2.setFill(Color.BLUE);
				
					group.getChildren().add(vectorMG2);
				}
			}
			
		});

		Button tension1 = new Button(" T\u2081 ");
		tension1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				double privx = (massRect1.getPoints().get(0) - massRect1.getPoints().get(2)) / 1.9;
				double privy = (massRect1.getPoints().get(3) - massRect1.getPoints().get(1)) / 1.9;
				
				vectorT1 = new Polygon();
				vectorT1.getPoints().addAll(new Double[] {
						massRect1.getPoints().get(0) + (100*Math.cos(rAngle)) - (35*Math.cos(rAngle)), massRect1.getPoints().get(1) - (100*Math.sin(rAngle)) - (35*Math.cos(rAngle)), //bot right
						massRect1.getPoints().get(0) - (35*Math.cos(rAngle)), massRect1.getPoints().get(1) - (35*Math.cos(rAngle)),//bot left
						massRect1.getPoints().get(0) - (3*Math.tan(rAngle)) - (35*Math.cos(rAngle)), massRect1.getPoints().get(1) - 3 - (35*Math.cos(rAngle)),//top left
						massRect1.getPoints().get(0) + (100*Math.cos(rAngle)) - (3*Math.tan(rAngle)) - (35*Math.cos(rAngle)), massRect1.getPoints().get(1) - (100*Math.sin(rAngle)) - 3 - (35*Math.cos(rAngle))//top right
				});
				vectorT1.setLayoutX(massRect1.getLayoutX());
				vectorT1.setLayoutY(massRect1.getLayoutY());
				vectorT1.setFill(Color.CORAL);
			
				group.getChildren().add(vectorT1);
			}
			
		});

		Button tension2 = new Button(" T\u2082 ");
		tension2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				if(isPulley){
					double privx = (massRect2.getPoints().get(0) - massRect2.getPoints().get(2)) / 2;
					double privy = (massRect2.getPoints().get(3) - massRect2.getPoints().get(5)) / 2;
					
					vectorT2 = new Polygon();
					vectorT2.getPoints().addAll(new Double[] {
							massRect2.getPoints().get(0) - 50 + 3, massRect2.getPoints().get(1) - 100, //bot right
							massRect2.getPoints().get(2) + 50 - 3, massRect2.getPoints().get(3) - 100,//bot left
							massRect2.getPoints().get(4) + 50 - 3, massRect2.getPoints().get(5) - 100,//top left
							massRect2.getPoints().get(6) - 50 + 3, massRect2.getPoints().get(7) - 100//top right
					});
					vectorT2.setLayoutX(massRect2.getLayoutX());
					vectorT2.setLayoutY(massRect2.getLayoutY());
					vectorT2.setFill(Color.CORAL);
				
					group.getChildren().add(vectorT2);
				}
			}
			
		});

		Button friction = new Button(" \u0192\u2096 ");
		friction.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				double privx = (massRect1.getPoints().get(0) - massRect1.getPoints().get(2)) / 1.9;
				double privy = (massRect1.getPoints().get(3) - massRect1.getPoints().get(1)) / 1.9;
				
				vectorF = new Polygon();
				if(isPulley){
					if(((mWeight1*Math.sin(rAngle)) >= mWeight2)){
					
					
					
						vectorF.getPoints().addAll(new Double[] {
								massRect1.getPoints().get(0) + (100*Math.cos(rAngle)), massRect1.getPoints().get(1) - (100*Math.sin(rAngle)), //bot right
								massRect1.getPoints().get(0), massRect1.getPoints().get(1),//bot left
								massRect1.getPoints().get(0) - (3*Math.tan(rAngle)), massRect1.getPoints().get(1) - 3,//top left
								massRect1.getPoints().get(0) + (100*Math.cos(rAngle)) - (3*Math.tan(rAngle)), massRect1.getPoints().get(1) - (100*Math.sin(rAngle)) - 3//top right
						});
					
					} else {
						vectorF.getPoints().addAll(new Double[] {
								massRect1.getPoints().get(2), massRect1.getPoints().get(3), //bot right
								massRect1.getPoints().get(2) - (100*Math.cos(rAngle)), massRect1.getPoints().get(3) + (100*Math.sin(rAngle)),//bot left
								massRect1.getPoints().get(2) - (100*Math.cos(rAngle)) - (3*Math.tan(rAngle)), massRect1.getPoints().get(3) + (100*Math.sin(rAngle)) - 3,//top left
								massRect1.getPoints().get(2) - (100*Math.cos(rAngle)) + (100*Math.cos(rAngle)) - (3*Math.tan(rAngle)), massRect1.getPoints().get(3) + (100*Math.sin(rAngle)) - (100*Math.sin(rAngle)) - 3//top right
						});
					}
				}else {
					vectorF.getPoints().addAll(new Double[] {
							massRect1.getPoints().get(0) + (100*Math.cos(rAngle)), massRect1.getPoints().get(1) - (100*Math.sin(rAngle)), //bot right
							massRect1.getPoints().get(0), massRect1.getPoints().get(1),//bot left
							massRect1.getPoints().get(0) - (3*Math.tan(rAngle)), massRect1.getPoints().get(1) - 3,//top left
							massRect1.getPoints().get(0) + (100*Math.cos(rAngle)) - (3*Math.tan(rAngle)), massRect1.getPoints().get(1) - (100*Math.sin(rAngle)) - 3//top right
					});
				}
				vectorF.setLayoutX(massRect1.getLayoutX());
				vectorF.setLayoutY(massRect1.getLayoutY());
				vectorF.setFill(Color.GREEN);
			
				group.getChildren().add(vectorF);
			}
			
		});

		Button components = new Button("Components");
		components.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				// TODO Auto-generated method stub
				showC = !showC;
			}
			
		});

		if(!hasAnimated){

			normalF.setDisable(true);
			mg1.setDisable(true);
			mg2.setDisable(true);
			tension1.setDisable(true);
			tension2.setDisable(true);
			friction.setDisable(true);
			components.setDisable(true);
			lAxis.setDisable(true);
			rAxis.setDisable(true);

		}

		vGrid.add(lAxis,0,0);
		vGrid.add(rAxis,1,0);
		vGrid.add(m1,0,1);
		vGrid.add(m2,1,1);
		vGrid.add(normalF,0,2);
		vGrid.add(mg1,0,3);
		vGrid.add(mg2,1,3);
		vGrid.add(tension1,0,4);
		vGrid.add(tension2,1,4);
		vGrid.add(friction,0,5);
		//vGrid.add(components,0,6);

		if(!isPulley){
			tension2.setDisable(true);
			tension1.setDisable(true);
			mg2.setDisable(true);
		}

		vStage.setScene(new Scene(vGrid));
		vStage.show();

	}

	private double max(Double[] d){

		double x = Double.MAX_VALUE;

		for(double i : d){
			if (i < x)
				x = i;
		}

		return x;
	}

	private void openSolvingPage(){

		Stage stage = new Stage();

		VBox vBox = new VBox();

		Image image = new Image(Main.class.getResourceAsStream("Solving Page.png"));

		ImageView imageView = new ImageView(image);

		vBox.getChildren().add(imageView);

		stage.setScene(new Scene(vBox));
		stage.show();
	}


}
