/*
	rover.h: rover include file
	Created by: Jeff Miller, 2/10/2013
*/
#ifndef ROVER_H
#define ROVER_H

/*** includes ***/
#include "Arduino.h"
#include <WProgram.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
// Arduino-Supplied Servo Library
// URL: http://arduino.cc/en/Reference/Servo
#include <Servo.h>
// Third-Party Ultrasonic Sensor Library
// URL:http://playground.arduino.cc/Code/NewPing
#include <NewPing.h>
/*
   TCRT5000 sensor connected to Arduino analog input
   TCRT5000 pins: (see datasheet http://www.vishay.com/docs/83760/tcrt5000.pdf )
     C = Arduino analog input pin A1
     E = GND
     A = 100 ohm resistor, other end of resistor to 5V
     C = GND
*/

// ***************************************
// Arduino Pin Declarations                //Note: PWM disabled on Pin
// *************************************** //      3, 9, 10 and 11.
#define PIN_BATT_SENSE          A0
#define PIN_LIR_SENSOR_READ     A5
#define PIN_RIR_SENSOR_READ     A4
//#define PIN_BLUETOOTH_RECEIVE  0
//#define PIN_BLUETOOTH_TRANSMIT 1
#define PIN_ULTRA_ECHO           10 // Arduino pin tied to echo pin on the ultrasonic sensor.
#define PIN_ULTRA_TRIGGER        9  // Arduino pin tied to trigger pin on the ultrasonic sensor.
//#define PIN_BATT_LED_INDICATOR   6 // Indicator for battery check
#define PIN_SERVO_RIGHT          4 // By design, PWM is disabled on these pins because of Servo.h. 
#define PIN_SERVO_LEFT           5 // http://arduino.cc/en/Reference/Servo
#define PIN_SERVO_LASER          8

// ***************************************
// Constant Declarations
// ***************************************
#define ULTRA_MAX_DISTANCE 300 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
#define SERVO_MAX 2000 // Servo Maximum Pulse Width. Maybe need to adjust/test this more. 1500 = stopped
#define SERVO_MIN 1000 // Servo Minimum Pulse Width. Maybe need to adjust/test this more
#define SENSOR_CHECK_SPEED 500 // How frequently are we going to send out sensor data (in milliseconds). 50ms would be 20 times a second.

/*** type definitions ***/
typedef struct rover_state {
	double x, y, heading, irFL, irFR, irBL, irBR;
        unsigned int pingRangeF, pingRangeR;
}ROVERSTATE;

/*** variables ***/
extern Servo servoR; //create servo objects
extern Servo servoL;
extern Servo servoLZ;
/*** function definitions ***/

void forward(int countF);
void reverse(int countR);
void left(int degreeL);
void right(int degreeR);
void pause(int Us);
void LZAim(int degreeLZ);
//void battCheck();
void sensorCheck();
void sensorSend();
void pingCheck();
void irCheck();
#endif
