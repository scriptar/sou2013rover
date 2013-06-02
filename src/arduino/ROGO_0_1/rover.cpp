/*
	rover.c: rover state and movement routine
*/

#include <WProgram.h>
#include "rover.h"
#include "pitches.h"

Servo servoR; //create servo objects
Servo servoL;
Servo servoLZ;
int setR = 90; //set servo speed to 0
int setL = 90;
static unsigned long sendThresh = SENSOR_CHECK_SPEED;
unsigned long currentSend = 0;
boolean safetyFlagUltra = true;
boolean safetyFlagIR = true;
NewPing sonar(PIN_ULTRA_TRIGGER, PIN_ULTRA_ECHO, ULTRA_MAX_DISTANCE); // NewPing setup of pins and maximum distance.
ROVERSTATE rover = {0};
  
// ***************************************
// Functions
// ***************************************
void forward(int countF)
{
  countF *= CHECK_RATE;
  for (int i = 0; i < countF; i++)
  {
    if (Serial.available())
    {
      pause(0);
      return;
    }
    setR = 180; //servo1 full forward
    setL = 0; //servo2 full forward
    sensorCheck();
    sensorSend();
    if (safetyFlagUltra && safetyFlagIR)
    {
      servoR.write(setR); //feed servos speed setting
      servoL.write(setL); 
      delay(UNIT_CALIBRATION);
      rover.x += (1.0 / (double)CHECK_RATE) * sin((double)rover.heading * PI / 180.0); // sin & cos switched because heading is clockwise from vertical
      rover.y += (1.0 / (double)CHECK_RATE) * cos((double)rover.heading * PI / 180.0); // sin & cos switched because heading is clockwise from vertical
    }
    else
    {
      pause(0);
      tone(PIN_BUZZER, NOTE_C6, 250);
      digitalWrite(PIN_LED_INDICATOR, HIGH);
      delay(350);
      noTone(PIN_BUZZER);
      tone(PIN_BUZZER, NOTE_C4, 250);
      digitalWrite(PIN_LED_INDICATOR, LOW);
      delay(350);
      noTone(PIN_BUZZER);
      Serial.println("sensorTrip");
    }
  }
  pause(250);
}

void reverse(int countR)
{
  for (int i = 0; i < countR; i++)
  {
    if (Serial.available())
    {
      pause(0);
      return;
    }
    setR = 0; //servo1 full reverse
    setL = 180; //servo2 full reverse
    servoR.write(setR); //feed servos speed setting
    servoL.write(setL); 
    delay(UNIT_CALIBRATION * CHECK_RATE);
    rover.x -= sin((double)rover.heading * PI / 180.0); // sin & cos switched because heading is clockwise from vertical
    rover.y -= cos((double)rover.heading * PI / 180.0); // sin & cos switched because heading is clockwise from vertical
  }
  pause(250);
}

void left(int degreeL)
{
  setR = 180;
  setL = 180;
  for(int i = 0; i < degreeL; i++)
  {
    if (Serial.available())
    {
      pause(0);
      return;
    }
    servoR.write(setR); 
    servoL.write(setL);
    delay(6);
    rover.heading = (rover.heading - 1) % 360;
  }
  pause(250);
}

void right(int degreeR)
{
  setR = 0;
  setL = 0;
  for (int i = 0; i < degreeR; i++)
  {
    if (Serial.available())
    {
      pause(0);
      return;
    }
    servoR.write(setR);
    servoL.write(setL);
    delay(6);
    rover.heading = (rover.heading + 1) % 360;
  }
  pause(250);
}

void pause(int Us)
{
  setR = 90; //servo1 stop
  setL = 90; //servo2 stop
  servoR.write(setR); //feed servos speed setting
  servoL.write(setL);
  delay(Us);
}

void LZAim(int degreeLZ)
{
  servoLZ.write(degreeLZ);
  delay(350);
}

void LZFire(int state)
{
  if (state == 1)
  {
    digitalWrite(PIN_LZ_FIRE, HIGH);
  }
  else
  {
    digitalWrite(PIN_LZ_FIRE, LOW);
  }
}
void battCheck()
{
  //Battery Monitor
  //warning led attached to pin 6
  rover.bLevelLow = analogRead(PIN_BATT_SENSE_LOW); //Read analog input in var
  rover.bLevelHigh = analogRead(PIN_BATT_SENSE_HIGH);
  rover.bLevelLow *= .0049; //convert to corresponding input voltage value
  rover.bLevelHigh *= .0049;
  if (rover.bLevelLow < 2.5 || rover.bLevelHigh < 2.5) //check if below 2.5 input volts(5v), warning led on
  {
    digitalWrite(PIN_LED_INDICATOR, HIGH); 
  }
  else
  {
    digitalWrite(PIN_LED_INDICATOR, LOW);
  }
}

void sensorCheck()
{
  pingCheck();
  irCheck();
  battCheck();
  memCheck();
}

void sensorSend()
{
  if (millis() >= currentSend)
  {
    currentSend = millis() + sendThresh;
    Serial.println(F("start"));
    Serial.println(F("pingF"));
    Serial.println(rover.pingRangeF);
    Serial.println(F("irFL"));
    Serial.println(rover.irFL);
    Serial.println(F("irFR"));
    Serial.println(rover.irFR);
    Serial.println(F("bLevelLow"));
    Serial.println((rover.bLevelLow * 2));
    Serial.println(F("bLevelHigh"));
    Serial.println((rover.bLevelHigh * 2));
    Serial.println(F("freeMem"));
    Serial.println(rover.freeRam);
    Serial.println(F("loc"));
    Serial.print(F("("));
    Serial.print(rover.x);
    Serial.print(F(","));
    Serial.print(rover.y);
    Serial.print(F(","));
    Serial.print(rover.heading);
    Serial.println(F(")"));
    Serial.println(F("end"));
  }
}

void pingCheck()
{
  unsigned int uS = sonar.ping();  // Send ping.
  rover.pingRangeF = uS / US_ROUNDTRIP_CM; // Convert ping time to distance in cm and print result (0 = outside set distance range)
  if (rover.pingRangeF == 0)
  {
//    Serial.println("Out of Range");
    rover.pingRangeF = 9999;
    safetyFlagUltra = true;
  } 

  else if (rover.pingRangeF <= 10) 
  {
//    Serial.print("Ping: ");
//    Serial.print(rover.pingRangeF); // Ping returned, uS result in ping_result, convert to cm with US_ROUNDTRIP_CM.
//    Serial.println("cm, STOP!!!");
    safetyFlagUltra = false;
//    tone(PIN_BUZZER, NOTE_C6, 250);
//    digitalWrite(PIN_LED_INDICATOR, HIGH);
//    delay(350);
//    noTone(PIN_BUZZER);
//    tone(PIN_BUZZER, NOTE_C4, 250);
//    digitalWrite(PIN_LED_INDICATOR, LOW);
//    delay(350);
//    noTone(PIN_BUZZER); 
//    Serial.println("sensorTrip");
  }
  else
  {
//    Serial.print("Ping: ");
//    Serial.print(rover.pingRangeF); // Ping returned, uS result in ping_result, convert to cm with US_ROUNDTRIP_CM.
//    Serial.println("cm");
    safetyFlagUltra = true;
  }
}

void irCheck()
{
  rover.irFL = analogRead(PIN_LIR_SENSOR_READ); // read the state of the tcrt5000
  rover.irFR = analogRead(PIN_RIR_SENSOR_READ);
//  Serial.print(rover.irFL); //Print the state of the tcrt5000
//  Serial.print(", ");
//  Serial.println(rover.irFR);
  if (rover.irFL > 650 | rover.irFR > 650)
  {
    safetyFlagIR = false;
  }
  else
  {
    safetyFlagIR = true;
  }
}

void memCheck () {
  extern int __heap_start, *__brkval; 
  int v; 
  rover.freeRam = (int) &v - (__brkval == 0 ? (int) &__heap_start : (int) __brkval); 
}

