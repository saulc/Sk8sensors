


use <myshapes.scad>
 use <aa single.scad>
 /*
		 9g servo mount . m2 inserts
 */
  
  x = 78;
  y = 42;
  
z =27;
c = 9;
p = 5;
 f = 3;
 w = 3.2;
 
  
 sh = 42.5;
 hs = 6;
 
    h2 = 2.1; 
    h3 = 3.1;

fn = 150;

//%cy(110, 1, 50);  
// %
// translate([0,0,z+3]) rotate([180, 0, 0])
// sbox();
 
// translate([-10,0])
 cover();
// frame();
 
 module frame(){
     
	difference(){
        union(){ 
            translate([-8,-16,0]) multi(2);
            hull(){ 
               translate([ 25, 0, p/2])    rc(8, y+w, p, c-2); 

            }
             translate([ 35, 0, 0]) rotate([0, 0, 90])     holes(hs*2.4, p*2, sh, 6);
        }
       
             translate([ 35, 0, -1]) rotate([0, 0, 90])     holes(hs, p*3, sh);
//             translate([ 35, 0, p/2]) rotate([0, 0, 90])     holes(hs*1.6, p, sh);
    }
 }
 

module cover( ){ 
            translate([-8,-16,.5]) multi(2);
	difference(){
        union(){
            hull(){ 
               translate([ 0, 0, 1])    rc(x+w, y+w, 2, c); 
                  } 
               translate([ 0, 0, 2])    rc(x-.8, y-.8, 3, c); 
             translate([ 48, 0, 0]) rotate([0, 0, 90])     holes(hs*2.4, p*2, sh, 6);
        }      
               translate([ 0, 0, p/2+1.2])    rc(x-w, y-w, p, c); 
             translate([ 48, 0, -1]) rotate([0, 0, 90])     holes(hs, p*3, sh);
    } 
}
 

module scut(){
        union(){
            mx = 4;
            dx = -12;
            dy = 0;
            
                 for(i=[-1,1])  translate([  dx, dy+ i*18.5/2,1])  holes(h2-.2, 11, 44);
             translate([ x/2-mx, 0, 1.2]) rotate([0, 0, 90])    holes(h3, 9, 30);
                 
             translate([ x/2-1, 0, 10]) rotate([90, 0, 90])    slot(6, 7, 30); 
                 
//                 #  translate([ 0, 0, 11]) c(69, 33, 1);
             translate([ dx, dy, -1])  holes(17.5, z*2, 27.5);
            difference(){
                hull(){
                  translate([ 0, 0, z/2+5.2])    rc(x, y, z, c); 
                  translate([ 0, 0, 1/2+1.2])    rc(x-f, y-f, 1, c); 
                }
             translate([ dx, dy, 0])  holes(20, 3, 27.5);
                 for(i=[-1,1])  translate([  dx,dy+ i*18.5/2,0])  holes(6, 5.5, 44, 6);
                  
             translate([ x/2-mx, 0, 0]) rotate([0, 0, 90])    holes(9, 7, 30, 6);
            }
        } 
}

module sbox(yy = 0){ 
	difference(){
        union(){
            hull(){ 
               translate([ 0, 0, z/2+2])    rc(x+w, y+w, z-4, c); 
               translate([ 0, 0, 1/2])    rc(x, y, 1, c); 
                  } 
        }    
//             translate([ 0, 0, 4]) rotate([90, 0, 0])  holes(3.1, z*2, 27.5);
            translate([ 0, 0, 0])   scut(); 
    } 
}
 


 
 

  


	
	