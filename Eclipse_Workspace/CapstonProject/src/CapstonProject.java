import java.util.Scanner;


public class CapstonProject {
	private ControlRobot cr;

    public static void main(String args[]){
    	CapstonProject cp = new CapstonProject(); 
    	cp.cr = new ControlRobot();
    	
    	cp.cr.enableNetworkUsePathString("xml/NRLAB02.xml");
    	System.out.println("Device Num : " + cp.cr.readNetworkDeviceNumber());
    	System.out.println("\nCurrent State : " + cp.cr.readNetworkDeviceNumber());
    	cp.cr.OperationMode(cp.cr.VELOCITY_MODE, cp.cr.VELOCITY_MODE);
    	System.out.println("\nModeChaged to Velocity Mode... \n");
    	cp.cr.enableMotor(true, true);
    	System.out.println("\nOperation Motor");
    	cp.cr.enablePsd(true);
    	System.out.println("\nOperation PSD");
    	
		while(true){
			Scanner sc = new Scanner(System.in);
			int command = sc.nextInt();
			
			switch(command){
				case 1 :
					//cp.cr.enableMotor(true, true);
					cp.cr.kinematicsDrive(0.2F, 0.0F, cp.cr.MPS);
					break;
				case 2 :
					//cp.cr.enableMotor(true, true);
					cp.cr.kinematicsDrive(0.0F, -0.4F, cp.cr.MPS);
					break;
				case 3 :
					//cp.cr.enableMotor(true, true);
					cp.cr.kinematicsDrive(0.0F, 0.4F, cp.cr.MPS);
					break;
				case 4 :
					//cp.cr.enableMotor(true, true);
					cp.cr.kinematicsDrive(-0.2F, 0.0F, cp.cr.MPS);
					break;
				case 5 :
					//cp.cr.enableMotor(true, true);
					cp.cr.kinematicsDrive(0.0F, 0.0F, cp.cr.MPS);
					break;
				case 6 :
					cp.cr.enableMotor(true, true);
					cp.cr.kinematicsDrive(0.0F, 0.0F, cp.cr.MPS);
					break;
				case 7 :
					cp.cr.getPsdDataClockwiseInnerVariable();
					String valueToString = String.format("Sensor[0] : %-4d, Sensor[1] : %-4d, Sensor[2] : %-4d, Sensor[3] : %-4d, Sensor[4] : %-4d, Sensor[5] : %-4d, Sensor[6] : %-4d",
							cp.cr.psdData[0], cp.cr.psdData[1], cp.cr.psdData[2], cp.cr.psdData[3], cp.cr.psdData[4], cp.cr.psdData[5], cp.cr.psdData[6]);
					System.out.println(valueToString + " Direction_cloickwise : " + cp.cr.clockwise);
					break;
				case 8 :
					cp.cr.getPsdDataAnticlockwiseInnerVariable();
					valueToString = String.format("Sensor[0] : %-4d, Sensor[1] : %-4d, Sensor[2] : %-4d, Sensor[3] : %-4d, Sensor[4] : %-4d, Sensor[5] : %-4d, Sensor[6] : %-4d",
							cp.cr.psdData[0], cp.cr.psdData[1], cp.cr.psdData[2], cp.cr.psdData[3], cp.cr.psdData[4], cp.cr.psdData[5], cp.cr.psdData[6]);
					System.out.println(valueToString + " Direction_cloickwise : " + cp.cr.clockwise);
					break;
				case 9 :
					cp.cr.disableNetwork();
					break;
				case 0 :
					cp.cr.restartNetworkUsePathString("xml/NRLAB02.xml");
					break;
			}
		}
   
    }
}
