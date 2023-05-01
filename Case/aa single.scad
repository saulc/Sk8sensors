


use <myshapes.scad>
 
 /*
		12 bit rgb ring
		arduino micro 
		5v boost + AA batt
		pot
		spdt double switch
 */
  
 x = 60; 
 y = 18;
 z = 16;
 c = 3;
 
 p = 2;
 w = .4 * 6; 
 fwall =  1.8;
 
 hole = 3.1 ;
 hw = 62/2;
hs = 5;

fn = 150;

//%cy(110, 1, 50); 


//	translate([ 0, 20, 0]) 
// single();
 
// single(2);
 
 
// 	difference(){
//		for(i=[-1,1]) translate([ i* 61, 10.6+w/2, 4]) rotate([0, 0]) cy(9, 8, 6);
//		for(i=[-1,1]) translate([ i* 31, 10.6+w/2, 4]) rotate([0, 0]) cy(hole, 18, fn);
//        }
 multi(2);
// 	difference(){
//	translate([ 0, 15+w/2,   3/2])  rc(75, 10, 3, 9);
//	translate([ 0, 15+w/2,   3/2])  c(60, 12, 5);
//		for(i=[-1,1]) translate([ i* 33, 15+w/2, 0]) cy(hole, z, fn);
//	}
 
//  cover();

module multi(n){
	yy = 15;
	difference(){
		translate([0, (yy*n)/2+w/2]) union(){
			hull(){
		   for(i=[0,1]) translate([0,0, 1/2+(i*(z*.75 - 1))])    rc(x-(1-i)*p, n*yy+w-(1-i)*p, 1+i*z/2, c);
			} 
		} 
		   for(i=[1:n]) translate([0, i*yy-6])   battcut();
			   
		   translate([0, (yy*n)/2+w/2 ,z]) rc(54, n*yy-w, z, c);
	  
	}
}
  
module battcut(s=1){
	 for(i=[-1,1]) translate([s*((x/2-2.5)-s+1)*i,0, z/2+fwall])    rotate([0,90,0]) rc(z, 13, 1, 3);   
 			for(i=[-1,1]) translate([s*(x/2-5)*i,0, z/2+fwall]) {
				c(4, 9, z);
				translate([ 2*i, 0,  -z+1]) c(1.2, 4.6, z);
			}
		
		hull(){
			translate([ 0, 0,  z/2+.8])  rotate([0,90,0]) cy(  15, x-9, fn);  
			translate([ 0, 0,  z])  rotate([0,90,0]) cy(  15, x-9, fn);  
		} 
	}
	


module single(s=1){
	
	difference(){
		union(){
			hull(){
		   for(i=[0,1]) translate([0,0, 1/2+(i*(z*.75 - 1))])    rc(s*x-(1-i+s*2)*p, y-(1-i)*p, 1+i*z/2, c);
			} 
		} 
		 
		  battcut(s);
	   
	}
}
  


module cover(){
	
	difference(){
		union(){ 
		    translate([0,0, 2])    rc(x, y, 4, c);
			 
		}
		      translate([ -(x/2-11), 0,  0])  cy(  4.2, z, fn);   
		      translate([ -(x/2-11), 0,  z/2+2])  cy(  10, z, fn);    
			translate([0,0, 0]) hh();
	   
	}
}


module hh(){
	 
		     for(j=[-1,1])  for(i=[-1,1]) translate([ hw*j, hw*i,  z/2]) cy(  hole, z+1, fn);   
		    
}

module hss(){ 
		       for(j=[-1,1])  for(i=[-1,1]) 
				 hull(){
				 for(k =[0, 1]) translate([ hw*j , hw*i, 1/2 + k*hs]) cy(  11+k*-4, 1, fn);   
				 }  
	
}

	
	 