boolean ledState = 0;

void setup(){
  Serial.begin(9600);
}

void loop(){
int lightVal = analogRead(A5);
if(lightVal < 500) ledState = 1;
digitalWrite(13, ledState);
Serial.println(lightVal);
}
    
