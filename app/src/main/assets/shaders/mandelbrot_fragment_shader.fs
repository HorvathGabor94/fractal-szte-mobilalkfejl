vec2 getAlgorithmicValue(vec2 value, int algorithm){
	vec2 result = 3.1415926535 * value;
	if(algorithm == 0){
		result = value;
	}
	else if(algorithm == 1){
		result = ComplexCos(result);
	}
	else if(algorithm == 2){
		result = ComplexSin(result);
	}
	else if(algorithm == 3){
		result = ComplexTan(result);
	}
	else if(algorithm == 4){
		result = ComplexInv(ComplexCos(result));
	}
	else if(algorithm == 5){
		result = ComplexInv(ComplexSin(result));
	}
	else if(algorithm == 6){
		result = ComplexInv(ComplexTan(result));
	}
	else if(algorithm == 7){
		result = ComplexCosHyp(result);
	}
	else if(algorithm == 8){
		result = ComplexSinHyp(result);
	}
	else if(algorithm == 9){
		result = ComplexTanHyp(result);
	}
	else if(algorithm == 10){
		result = ComplexInv(ComplexCosHyp(result));
	}
	else if(algorithm == 11){
		result = ComplexInv(ComplexSinHyp(result));
	}
	else if(algorithm == 12){
		result = ComplexInv(ComplexTanHyp(result));
	}
	return result;
}

void main(){
	vec2 coords = gl_FragCoord.xy / vDim;
	
	// c is the position of the pixel from the origo
	vec2 c = (coords - vOffset) * vZoomer;
	if(!isZero(vRotation)){
		vec2 cP = CartesianToPolar(c);
		cP.y += vRotation;
		c = PolarToCartesian(cP);
	}
	c = ComplexSymmetrization(c + vPan, vSymmetry);
	c = getAlgorithmicValue(c, vPrefixAlgorithm);
	
	vec2 z = vZ;		// seed
	vec2 p = vP;		// power

	/*if(vSweepAlgorithm > 0){
		vec2 tC = c;
		vec2 tZ = z;
		if(vSweepAlgorithm == 1){
			z.x = tC.x;
			c.x = tZ.x;
		}
		else if(vSweepAlgorithm == 2){
			z.x = tC.y;
			c.y = tZ.x;
		}
	}*/
	if(vIsInverted > 0){
		c = ComplexInv(c);
	}
	if(vIsJulia > 0){
		vec2 temp = c;
		c = z;
		z = temp;
	}
	if(isZero(c.x)){
		c.x = 0.000001;
	}
	if(isZero(z.x)){
		z.x = 0.000001;
	}
	
	
	c = getAlgorithmicValue(c, vInfixAlgorithm);
	// I T E R A T I O N
	float m = 0.0;
	float de = vDe;
	vec2 dz = vec2(0.0, 0.0);
	for(int n=0; n<vMaxIter; n++){
		z = ComplexPow(getAlgorithmicValue(z, vFractalAlgorithm), p) + c;

		de += vDe;
		dz += 2.0 * z * dz + 1.0;

		float d = dot(z, z);
		if(!isZero(vBailout) && d > vBailout){
			break;
		}

		c = ComplexMul(c, vMul);
		c += vAdd;

		if(d < vRad){
			m++;
		}
	}
	de /= vRealIter.y;
	
	
	// C O L O R I N G
	vec4 color = vec4(0,0,0,1);
	if(!isZero(m - vRealIter.y) && m <= vRealIter.x){
		color = vec4(sin(m / 4.0), sin(m / 5.0), sin(m / 7.0), 1.0) / 4.0 + vec4(0.75, 0.75, 0.75, 0.75);
	}

	/*
	if(isZero(m - vRealIter.y)){
		color = vec4(0,0,0,1);
	}
	else if(m > vRealIter.x){
		color = vec4(0,0,0,1);
	}
	else{
		float d = dot(z, z);
		if(vColoringAlgorithm == 0){						// minimal
			color = vExternalColor;
		}
		else if(vColoringAlgorithm == 1){					// discrete
			color = mix(vExternalColor, vPeripheralColor, (m / vRealIter.y));
		}
		else if(vColoringAlgorithm == 2){					// linear
			color = mix(vExternalColor, vPeripheralColor, sqrt(d / dot(dz, dz)) * 0.5 * log(d));
		}
		else if(vColoringAlgorithm == 3){					// colorful
			color = vec4(sin(m / 4.0), sin(m / 5.0), sin(m / 7.0), 1.0) / 4.0 + vec4(0.75, 0.75, 0.75, 0.75);
		}
		else if(vColoringAlgorithm == 4){					// test1
			color = mix(vExternalColor, vPeripheralColor, de);
		}
		else if(vColoringAlgorithm == 5){					// test2
			color = vec4(sin(d), sin(d), sin(d), 1.0) / 4.0 + vec4(0.75, 0.75, 0.75, 0.75);
		}
	}
	*/
	
	gl_FragColor = color;
}







