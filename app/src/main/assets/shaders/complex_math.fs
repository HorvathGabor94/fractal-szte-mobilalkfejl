// AUXILIARY FUNCTIONS
bool isZero(float value){
	return length(value) < 0.000001;
}
bool isZero(vec2 value){
	return length(value) < 0.000001;
}
bool isZero(vec3 value){
	return length(value) < 0.000001;
}
bool isZero(vec4 value){
	return length(value) < 0.000001;
}


float cosh(float value){
	float ex = exp(value);
	return (ex + (1.0 / ex)) / 2.0;
}
float sinh(float value){
	float ex = exp(value);
	return (ex - (1.0 / ex)) / 2.0;
}
float tanh(float value){
	return sinh(value) / cosh(value);
}




// COMPLEX MATH
vec2 CartesianToPolar(vec2 z){
	// ( absolute_value , argument )
	return vec2(sqrt((z.x*z.x) + (z.y*z.y)), atan(z.y, z.x));
}
vec2 PolarToCartesian(vec2 z){
	return vec2(z.x * cos(z.y), z.x * sin(z.y));
}
vec2 PolarToRadians(vec2 z){
	return vec2(z.x, radians(z.y));
}
vec2 PolarToDegrees(vec2 z){
	return vec2(z.x, degrees(z.y));
}


vec2 ComplexSymmetrization(vec2 z, float value){
	vec2 result = z;
	if(isZero(value)){
		result = z;
	}
	else{
		vec2 symmetryPolar = vec2(sqrt((z.x*z.x) + (z.y*z.y)), atan(z.y / z.x));
		float invabs = 1.0 / abs(value);
		result = vec2(invabs, invabs) * PolarToCartesian(vec2(value, value) * symmetryPolar);
	}
	return z;
}


vec2 ComplexMul(vec2 z, vec2 o){
	// multiplication
	return vec2(z.x*o.x - z.y*o.y, z.x*o.y + z.y*o.x);
}
vec2 ComplexCon(vec2 z){
	// conjugate
	return vec2(z.x, -z.y);
}
vec2 ComplexInv(vec2 z){
	// inverse
	float divisor = (z.x*z.x + z.y*z.y);
	return ComplexCon(z) / vec2(divisor, divisor);
}
vec2 ComplexDiv(vec2 z, vec2 o){
	// division
	return ComplexMul(z, ComplexInv(o));
}
vec2 ComplexLn(vec2 z){
	// natural logarithm
	z = CartesianToPolar(z);
	return vec2(log(z.x), z.y);
}
vec2 ComplexLog(vec2 z, vec2 o){
	// general logarithm
	return ComplexDiv(ComplexLn(o), ComplexLn(z));
}
vec2 ComplexPow(vec2 z, vec2 o){
	// exponentiation
	float base = z.x*z.x + z.y*z.y;
	float arg = atan(z.y, z.x);
	vec2 result = z;
	if(isZero(o.y)){
		// Complex to a Real Power
		float phi = o.x * arg;
		result = vec2(cos(phi), sin(phi)) * vec2(base, base);
	}
	else{
		// Complex to a Complex Power
		float multiplier = pow(base, o.x / 2.0) * exp(-o.y * arg);
		float phi = (o.x * arg) + (o.y * log(base)) / 2.0;
		result = vec2(cos(phi), sin(phi)) * vec2(multiplier, multiplier);
	}
	return result;
}


vec2 ComplexSin(vec2 z){
	return vec2(sin(z.x) * cosh(z.y), cos(z.x) * sinh(z.y));
}
vec2 ComplexCos(vec2 z){
	return vec2(cos(z.x) * cosh(z.y), -sin(z.x) * sinh(z.y));
}
vec2 ComplexTan(vec2 z){
	return ComplexDiv(ComplexSin(z), ComplexCos(z));
}
vec2 ComplexSinHyp(vec2 z){
	return vec2(sinh(z.x) * cos(z.y), cosh(z.x) * sin(z.y));
}
vec2 ComplexCosHyp(vec2 z){
	return vec2(cosh(z.x) * cos(z.y), sinh(z.x) * sin(z.y));
}
vec2 ComplexTanHyp(vec2 z){
	return ComplexDiv(ComplexSinHyp(z), ComplexCosHyp(z));
}


vec2 ComplexChaotic(vec2 z, int aS, int aT1, int aT2, int bS, int bT1, int bT2){
	vec2 s = vec2(sin(z.x), sin(z.y));
	vec2 c = vec2(cos(z.x), cos(z.y));
	vec2 sh = vec2(sinh(z.x), sinh(z.y));
	vec2 ch = vec2(cosh(z.x), cosh(z.y));
	
	float a1 = 0.0;
	float a2 = 0.0;
	float b1 = 0.0;
	float b2 = 0.0;
	
	if(aT1 == 0){
		a1 = s.x;
	}
	else if(aT1 == 1){
		a1 = c.x;
	}
	else if(aT1 == 2){
		a1 = sh.x;
	}
	else if(aT1 == 3){
		a1 = ch.x;
	}
	
	if(aT2 == 0){
		a2 = s.y;
	}
	else if(aT2 == 1){
		a2 = c.y;
	}
	else if(aT2 == 2){
		a2 = sh.y;
	}
	else if(aT2 == 3){
		a2 = ch.y;
	}
	
	if(bT1 == 0){
		b1 = s.x;
	}
	else if(bT1 == 1){
		b1 = c.x;
	}
	else if(bT1 == 2){
		b1 = sh.x;
	}
	else if(bT1 == 3){
		b1 = ch.x;
	}
	
	if(bT2 == 0){
		b2 = s.y;
	}
	else if(bT2 == 1){
		b2 = c.y;
	}
	else if(bT2 == 2){
		b2 = sh.y;
	}
	else if(bT2 == 3){
		b2 = ch.y;
	}
	
	if(aS == 1){
		a1 *= -1.0;
	}
	if(bS == 1){
		b1 *= -1.0;
	}
	
	return vec2(a1 * a2, b1 * b2);
}
