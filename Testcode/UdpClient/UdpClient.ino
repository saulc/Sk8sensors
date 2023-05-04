/*
  UDPSendReceive.pde:
  This sketch receives UDP message strings, prints them to the serial port
  and sends an "acknowledge" string back to the sender

  A Processing sketch is included at the end of file that can be used to send
  and received messages for testing with a computer.

  created 21 Aug 2010
  by Michael Margolis

  This code is in the public domain.

  adapted from Ethernet library examples
*/
  
#include <Adafruit_MPU6050.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>
 
Adafruit_MPU6050 mpu;

#include <WiFi.h>
#include <WiFiUdp.h>

#include "secrets.h"

unsigned int localPort = 8889 ;  // local port to listen on

// buffers for receiving and sending data
char packetBuffer[UDP_TX_PACKET_MAX_SIZE + 1];  // buffer to hold incoming packet,
char ReplyBuffer[] = "acknowledged                                  \r\n";        // a string to send back
 
WiFiUDP Udp;
IPAddress ip(10, 0, 0, 89);  

const int il = 11;
String imuData[11];
int ii = 0;

int tt = 0;
int tic(){ 
  if(tt++ > 10000) tt = 0;
  return tt;
}

String logImu(int i, int ii){
  String dat = "";
  /* Get new sensor events with the readings */
  sensors_event_t a, g, temp;
  mpu.getEvent(&a, &g, &temp);

//    char l1[49]; //format c string then convert to regular string...for now..
    sprintf(ReplyBuffer,"%02d-%02d: %.03f %.03f %.03f %.03f %.03f %.03f T:%.02f\n", i, ii,
     a.acceleration.x, a.acceleration.y, a.acceleration.z, g.gyro.x, g.gyro.y, g.gyro.z, temp.temperature ); 
  dat = ReplyBuffer;
//  ReplyBuffer = l1;
  imuData[ii] = dat;
  if(ii++ >= il) ii = 0;
  return dat;
  
}

void setup() {
  Serial.begin(115200);
  for(int i =0; i<33; i++) Serial;
  Serial.print(".");
  Serial.println(".");
  Serial.println("Acme Sk8 sensor test");  
  
//    WiFi.config(ip);
  WiFi.begin(SECRET_SSID, SECRET_PASS);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(500);
  }
  Serial.print("Connected! IP address: ");
  Serial.println(WiFi.localIP());
  Serial.printf("UDP server on port %d\n", localPort);
  Udp.begin(localPort);

  
    Wire.setSDA(8);
    Wire.setSCL(9);
  if (!mpu.begin()) {
    Serial.println("Failed to find MPU6050 chip");
    while (1) {
      delay(10);
    }
  }

  mpu.setAccelerometerRange(MPU6050_RANGE_16_G);
  mpu.setGyroRange(MPU6050_RANGE_250_DEG);
  mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);
  Serial.println("");
  delay(100);

  
}

void loop() {
  // if there's data available, read a packet
  int packetSize = Udp.parsePacket();
  if (packetSize) {
    Serial.printf("Received packet of size %d from %s:%d\n    (to %s:%d)\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort(), Udp.destinationIP().toString().c_str(), Udp.localPort());

    // read the packet into packetBufffer
    int n = Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);
    packetBuffer[n] = 0;
    Serial.println("Contents:");
    Serial.println(packetBuffer);
 
//    sprintf(ReplyBuffer ,"%d: %.04f %.04f %.04f %.04f %.04f %.04f %.04f\r\n", 1, 2., 3., 4., 5., 6., 7., 8.);
//    logImu(tic()); //load the reply with data

    // send a reply, to the IP address and port that sent us the packet we received
    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    for(int i=0;i<10; i++){
        logImu(tic(), i); //load the reply with data
    Udp.write(ReplyBuffer);
    }
    Udp.endPacket();
  }
}

/*
  test (shell/netcat):
  --------------------
    nc -u 192.168.pico.address 8888
*/
