  /*
   TCRT5000 sensor connected to Arduino analog input
   TCRT5000 pins: (see datasheet http://www.vishay.com/docs/83760/tcrt5000.pdf )
     C = Arduino analog input pin A1
     E = GND
     A = 100 ohm resistor, other end of resistor to 5V
     C = GND
  */
  
#define PIN_LIR_SENSOR_READ A1
#define PIN_RIR_SENSOR_READ A2

int tcrtStateL = 0; // variable for reading the TCRT5000 status
int tcrtStateR = 0;
 
void setup() {
  pinMode(PIN_LIR_SENSOR_READ, INPUT);  // initialize the tcrt5000 pin as an input, and 
  pinMode(PIN_RIR_SENSOR_READ, INPUT);
  digitalWrite(PIN_LIR_SENSOR_READ, HIGH); //turn on the internal pullup resistors
  digitalWrite(PIN_RIR_SENSOR_READ, HIGH);
  Serial.begin(115200); 
}
 
void loop(){
  tcrtStateL = analogRead(PIN_LIR_SENSOR_READ); // read the state of the tcrt5000
  tcrtStateR = analogRead(PIN_RIR_SENSOR_READ);
  Serial.print(tcrtStateL); //Print the state of the tcrt5000
  Serial.print(", ");
  Serial.println(tcrtStateR);
  delay(200);
}

