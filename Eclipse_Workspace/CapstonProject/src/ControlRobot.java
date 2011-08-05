
public class ControlRobot {
	//String filePath = "xml/NRLAB02.xml";
	final int VELOCITY_MODE = 1;
	final int  POSITION_MODE = 2;		// NOT Available..... (Future update...)
	final int  POSITION_VELOCITY_MODE = 3;
	final int  TORQUE_MODE = 4;  		// NOT Available..... (Future update...)
	//final float  PI = 3.1415F;
	final float  GEAR_RATIO = 30.0F;	//30:1
	final float  WHL_RADIUS = 114.3F; 	//114.3mm
	///////////////////상태 관련 //////////
	final int  INIT	= 1;                // 초기화 상태
	final int  PREOP = 2;               // 데이터 입력만 가능한 상태
	final int  SAFEOP = 4;              // 데이터 입력만 가능한 상태
	final int  OP = 8;                  // 정상 가동 상태 : 입출력 가능
	/////////////////////////////////////
	final int  MPS = 1;
	final int  MMPS = 2;
	
	public float velocity;
	public boolean clockwise;
	public int[] psdData;  
	
	public ControlRobot(){
		velocity = 0.0F;
		clockwise = true;
		psdData = new int[9];
	}
	
	static {
    	System.loadLibrary("NRLAB02v01");
        System.loadLibrary("ControlRobotLib");
    }
	
	//////////////////  Funtion  //////////////////////////
    public boolean enableNetworkUsePathString(String filePath){
    	byte[] path = filePath.getBytes();
    	return enableNetwork(path, path.length) ;
	}
    
    public boolean enableNetworkUseMacAddrString(String macAddress, String filePath){
    	byte[] address = macAddress.getBytes();
    	byte[] path = filePath.getBytes();
    	return enableNetwork(address, path, address.length, path.length) ;
    }
    
    public void restartNetworkUsePathString(String filePath){
    	byte[] path = filePath.getBytes();
    	restartNetwork(path, path.length) ;
	}
    
    public void getPsdDataClockwiseInnerVariable(){
    	int[] temporary;
    	temporary = new int[10];
    	
    	temporary = getPsdDataClockwise();
    	
    	for(int iteration = 0; iteration < psdData.length; iteration++){
    		psdData[iteration] = temporary[iteration];
    	}
    	
    	clockwise = (temporary[temporary.length-1] == 1);
    }
    
    public void getPsdDataAnticlockwiseInnerVariable(){
    	int[] temporary;
    	temporary = new int[10];
    	
    	temporary = getPsdDataAnticlockwise();
    	
    	for(int iteration = 0; iteration < psdData.length; iteration++){
    		psdData[iteration] = temporary[iteration];
    	}
    	
    	clockwise = (temporary[temporary.length-1] == 1);    	
    }
    
    /////////////////// Native Function (Network) ////////////////
	native boolean enableNetwork(byte[] filePath, int length);
	native boolean enableNetwork(byte[] MacAddress, byte[] filePath, int addrLength, int pathLength);
	native void disableNetwork();
	native void restartNetwork(byte[] filePath, int length);
	native int readNetworkState();
	native int readNetworkDeviceNumber();

    /////////////////// Native Function (Motor) ////////////////
	native void OperationMode(int motor1Mode, int motor2Mode);  
	// 모드 변경시에는 disableMotor 한 후에 사용  ...  EnableMotor(0, 0) << 이 소리 인 듯 
	// 모드는 1 - 속도 모드, 2 - 위치 모드, 3 - 위치_속도 모드 가 있음
	native void enableMotor(boolean Motor1EnOrDisable, boolean Motor2EnOrDisable);
	/*
	native void MotorVelocity(int Motor1Velocity, int Motor2Velocity);
	// 속도 지령 범위는 0 ~ 3000 rpm
	native void MotorPositionVelocity(int Motor1Position, int Motor2Position, int Motor1Velocity, int Motor2Velocity);
	// 모터 위치는 12펄스에 1회전 (휠은 100감속기로 인하여 1회전 1200 펄스) / 속도 범위는 0~3000
	native int[] readMotorVelocity();
	// 속력을 측정한다. [1] = 모터1 [2] = 모터2
	native int[] readMotorPosition();
	// 위치 펄스를 측정한다. [1] = 모터1 [2] = 모터2
	native int[] readMotorCurrent();
	// 모터에서 소비된 전류 값을 측정한다. [1] = 모터1 [2] = 모터2
	native int[] readMotorActualData();
	// 모터의 위치 펄스 및 속력을 측정한다. [1] = 모터1 위치펄스 [2] = 모터2 위치펄스 [3] = 모터1 속력 [4] = 모터2 속력
	native void StopDrive();
	// 로봇 2축 모터의 감속 프로파일이 무시되고 바로 정지
	*/
	
	/////////////////// Native Function (Network) ////////////////
	native void enablePsd(boolean psdEnOrDisable);
	native int[] getPsdDataClockwise();           // [0]~[8] 센서 데이터, [9] 센서 데이터를 읽는 방향  1 : 시계방향 0: 반시계 방향
	native int[] getPsdDataAnticlockwise();       // [0]~[8] 센서 데이터, [9] 센서 데이터를 읽는 방향  1 : 반시계방향 0: 시계 방향
	// 센서 데이터는 0~160cm 사이의 거리를 나타낸다. 센섵는 총 7개가 쓰이므로 0~6개까지만이 올바른 정보이다.
    
	
	/////////////////// Native Function (driveRobot) ////////////////

	native void velocityDrive(float rpmBasedVelocity);      // RPM 기준 로봇 속도 지정(0~30)
	native void velocityRotate(float _rpmBasedRotation);           // RPM 기준 회전 속도 지정(0~30)
	native void translationVelocity(float mpBasedVelocity);      // m/s 기준 전후진 속도 지정 (MPS일 경우 0~0.35)
	native void rotationVelocity(float mpBasedRotation);         // m/s 기준 회전 속도 지정(MPS일 경우 0~0.35)
	native void kinematicsDrive(float tv, float rv, int distanceMode);   // m/s 기준 회전 및 속도 지정 (MPS일 경우 0~0.35)
	native float[] readKinematicsDrive(float[] getedTvRv, int distanceMode);       // [1] m/s 기준 전후진 속도 [2] 회전 속도 획득 (mode 는 m/s(MP), mm/s(MMP)) 
	native void stopDrive();
}
