
use <myshapes.scad>
use <skatesensor.scad>

fn = 150;

w = 220;
l = 820;
d = 8;        //board thickness
cd = 50;    //total depth
wb = 420; //wheel base

//concave wheel cuts
con = 560;
conh = 18;

mcon = 2400;
  
  bored();
  
translate([0, wb/3-21, 0 ]) 
  censor();
  
module bored(){
            difference(){
                union(){
                    
        for(j=[0,1]) mirror([j, 0, 0]) for(i=[-1,1]) translate([w/2-cd/2, i*l*.22, -40 ]) rotate([0,90,0]) wh();
     
                intersection(){  
                         translate([0, 0, 0 ]) rotate([0,0,90]) slot(w, cd, l-w);
                         translate([0, 0, 0 ])  cut(); 
                }
            }
             translate([0, 0, d ])  cut(d); 
                tholes();
            }
}

module wh(){
            difference(){
                hull(){
                 cy(50, 33, fn);
                 cy(52, 22, fn);
                }
                 cy(22, 55, fn);
            }
}

 module cut(x = 0){  
    difference(){  
        intersection(){
       translate([0, 0, mcon/2 ]) rotate([90,0,0]) cy(mcon,l+w,  fn/1.25); 
         hull()  {
             translate([0, 0, cd-d/2 ]) c(w, l , d);
             for(i=[-1,1]) translate([0, i*l/2-w*i*.77, w/2 ]) rotate([0,90,0]) cy(w,w+2,  fn); 
             }
     }
     //mostly for long boards i guess?
        for(j=[0,1]) mirror([j, 0, 0])   for(i=[-1,1]) translate([w/2, i*l*.29, conh-con/2-x-2 ]) wb(x);
        }
 }
 
module wb(x){
    hull(){
       translate([0, 0, 0 ]) rotate([0,90,0]) cy(con+x*2,2,  fn); 
       translate([-w/3, 0, 24+x*2.  ]) rotate([0,90,0]) cy(con*.8+x*2,2,  fn); 
    }
}


 module tholes(){  
        for(j=[-1,1])
          for(i=[-1,1]) translate([0, j*wb/2 + i*55/2, -1 ]) holes(6, cd, 42.5);
          
 }
  
 