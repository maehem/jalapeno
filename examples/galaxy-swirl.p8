-- Galaxy Swirl from twitter: https://twitter.com/2DArray/status/1122381788565987328

p={0,5,6,7}::_::x=rnd"128"y=rnd"128"u=x-64v=y-64g=u*u+v*v
a=atan2(u,v)*2+g/700-t()/4h=1-abs(a%2-1)pset(x,y,p[flr(h*h*h*3.9)+1])goto _

