


use <myshapes.scad>
 
 /*
		 9g servo mount . m2 inserts
 */
  
  x = 78;
  y = 50;
  
z =17;
c = 9;
p = 5;
 f = 3;
 w = 3.2;
 
  
    h2 = 2.2; 

fn = 150;

//%cy(110, 1, 50);  
 
 mnt();
 
 
module ms(){
        union(){
            dx = 12;
            dy = 0;
            
                 for(i=[-1,1])  translate([  dx, dy+ i*18.5/2,-1])  holes(h2, 11, 44);
             translate([ dx, dy, -1])  holes(17, z*2, 27.5);
            difference(){
                hull(){
                  translate([ 0, 0, z/2+3.2])    rc(x, y, z, c); 
                  translate([ 0, 0, 1/2+1.2])    rc(x-f, y-f, 1, c); 
                }
                 for(i=[-1,1])  translate([  dx,dy+ i*18.5/2,0])  holes(6, 5.5, 44, 6);
            }
        } 
}

module mnt(yy = 0){ 
	difference(){
        union(){
            hull(){
//               translate([0, -10.5, p/2])    rc(38, 3,p, 2);
               translate([ 0, 0, z/2])    rc(x+w, y+w, z, c);
//                holes(18, z, 52-18, 6);
                  } 
        }    
             translate([ 0, 0, 4]) rotate([90, 0, 0])  holes(3.1, z*2, 27.5);
            translate([ 0, 0, 0])   ms();
//        msp();
    } 
}
 


 
 

  


	
	