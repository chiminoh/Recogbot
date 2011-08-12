
/* =================================================================================
File name:      IRONCDEFV01.H                 
                    
Originator:		Redone Technologies co.,ltd

Description:

=====================================================================================
 History:
-------------------------------------------------------------------------------------
2009-04-06		Version 0.1
------------------------------------------------------------------------------------*/
#ifndef __NRLAB02DEFV01_H__
#define __NRLAB02DEFV01_H__

#ifdef __cplusplus
extern "C"
{
#endif

//=================================================================================
//	RALAB Defines
//=================================================================================
#define VELOCITY_MODE					0x1
#define POSITION_MODE					0x2		// NOT Available..... (Future update...)
#define POSITION_VELOCITY_MODE			0x3
#define TORQUE_MODE						0x4		// NOT Available..... (Future update...)
#define SERVO_DISABLE					0x0
#define SERVO_ENABLE					0x1
#define PI								3.1415f
#define GEAR_RATIO						30.0f	//30:1
#define WHL_RADIUS						114.3f	//114.3mm
#define INIT							1
#define PREOP							2
#define SAFEOP							4
#define OP								8
#define MPS                             1
#define MMPS                            2

//=================================================================================
//	Define the structure of the Object
//=================================================================================
typedef struct {
	int psdvalue[7];
	int controlword;
} PSD, *PSD_handle;


//=================================================================================
//	Information
//=================================================================================
_declspec(dllexport) int IRead_LibraryVersion();

//=================================================================================
//	Prototypes for "ETHERCAT NETWORK" Functions
//=================================================================================
_declspec(dllexport) bool NWrite_EnableNetwork();
_declspec(dllexport) bool NWrite_BootEnableNetwork(byte _filename[], int _len);
_declspec(dllexport) bool NWrite_BootEnableNetworkUsingMac(byte _MACAddress[],byte _filename[], int _len);
_declspec(dllexport) void NWrite_DisableNetwork();
_declspec(dllexport) void NWrite_RestartNetwork(byte _filename[], int _len);
_declspec(dllexport) int NRead_NetworkState();
_declspec(dllexport) int NRead_NetworkDeviceNum();


//=================================================================================
//	Prototypes for "MOTOR" Functions
//=================================================================================
_declspec(dllexport) void MWrite_OperationMode(int _m1mode, int _m2mode);
_declspec(dllexport) void MWrite_EnableMotor(bool _m1en, bool _m2en);
_declspec(dllexport) void MWrite_MotorVelocity(int _m1vel, int _m2vel);
_declspec(dllexport) void MWrite_MotorPositionVelocity(int _m1pos, int _m2pos, int _m1vel, int _m2vel);
_declspec(dllexport) void MRead_MotorVelocity(int &_m1vel, int &_m2vel);
_declspec(dllexport) void MRead_MotorPosition(int &_m1pos, int &_m2pos);
_declspec(dllexport) void MRead_MotorCurrent(int &_m1cur, int &_m2cur);
_declspec(dllexport) void MRead_MotorActualData(int &_m1pos, int &_m2pos, int &_m1vel, int &_m2vel);
_declspec(dllexport) void MWrite_StopDrive(void);

//=================================================================================
//	Prototypes for "IR-PSD Sensor" Functions
//=================================================================================
_declspec(dllexport) void SWrite_EnablePsd(bool _runstop);
_declspec(dllexport) void SRead_PsdData(PSD_handle);
_declspec(dllexport) void SRead_PsdDataNeg(PSD_handle);

//=================================================================================
//	Prototypes for "WHEEL" Functions
//=================================================================================
_declspec(dllexport) void WWrite_WhlVelocityRPM(float _whl1vel, float _whl2vel);
_declspec(dllexport) void WWrite_WhlVelocityMPS(float _whl1vel, float _whl2vel);
_declspec(dllexport) void WWrite_WhlPositionCM(float _whl1pos, float _whl2pos, float _whlvellimit);
_declspec(dllexport) void WRead_WhlVelocityRPM(float &_whl1vel, float &_whl2vel); 
_declspec(dllexport) void WRead_WhlVelocityMPS(float &_whl1vel, float &_whl2vel); 
_declspec(dllexport) void WRead_WhlPositionCM(float &_whl1pos, float &_whl2pos);
_declspec(dllexport) void WWrite_StopDrive(void);

//=================================================================================
//	Prototypes for "IRONC-ROBOT" Functions
//=================================================================================
_declspec(dllexport) void RWrite_VelocityDrive(float _whlvel);
_declspec(dllexport) void RWrite_VelocityRotate(float _whlvel);
_declspec(dllexport) void RWrite_TranslationVelocity(float _tarvel);
_declspec(dllexport) void RWrite_RotationVelocity(float _tarvel);
_declspec(dllexport) void RWrite_2W_KinematicsDrive(float tv, float rv, int _mode);
_declspec(dllexport) void RRead_2W_KinematicsDrive(float &tv, float &rv, int _mode);
_declspec(dllexport) void RWrite_StopDrive(void);

#ifdef __cplusplus
} // extern "C"
#endif
#endif