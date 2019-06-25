package ueshooting.script;

public enum ScriptVariable {
	VAR_a,
	VAR_b,
	VAR_c,
	VAR_d,
	VAR_e,
	VAR_f,
	VAR_g,
	VAR_h,
	VAR_i,
	VAR_j,
	VAR_k,
	VAR_l,
	VAR_m,
	VAR_n,
	VAR_o,
	VAR_p,
	VAR_q,
	VAR_r,
	VAR_s,
	VAR_t,
	VAR_u,
	VAR_v,
	VAR_w,
	VAR_x,
	VAR_y,
	VAR_z,
	VAR_X,
	VAR_Y,
	VAR_I,
	VAR_J,
	VAR_V,
	VAR_A,
	VAR_H,
	VAR_P,
	VAR_T,
	VAR_B,
	VAR_C,
	VAR_R,
	VAR_D,
	VAR_E,
	VAR_F,
	VAR_G,
	VAR_N,
	;
	
	public int index;

	public static ScriptVariable getVariable(String body) {
		switch(body){
		case "a":
			return VAR_a;
		case "b":
			return VAR_b;
		case "c":
			return VAR_c;
		case "d":
			return VAR_d;
		case "e":
			return VAR_e;
		case "f":
			return VAR_f;
		case "g":
			return VAR_g;
		case "h":
			return VAR_h;
		case "i":
			return VAR_i;
		case "j":
			return VAR_j;
		case "k":
			return VAR_k;
		case "l":
			return VAR_l;
		case "m":
			return VAR_m;
		case "n":
			return VAR_n;
		case "o":
			return VAR_o;
		case "p":
			return VAR_p;
		case "q":
			return VAR_q;
		case "r":
			return VAR_r;
		case "s":
			return VAR_s;
		case "t":
			return VAR_t;
		case "u":
			return VAR_u;
		case "v":
			return VAR_v;
		case "w":
			return VAR_w;
		case "x":
			return VAR_x;
		case "y":
			return VAR_y;
		case "z":
			return VAR_z;
		case "X":
			return VAR_X;
		case "Y":
			return VAR_Y;
		case "I":
			return VAR_I;
		case "J":
			return VAR_J;
		case "V":
			return VAR_V;
		case "A":
			return VAR_A;
		case "H":
			return VAR_H;
		case "P":
			return VAR_P;
		case "T":
			return VAR_T;
		case "B":
			return VAR_B;
		case "C":
			return VAR_C;
		case "R":
			return VAR_R;
		case "D":
			return VAR_D;
		case "E":
			return VAR_E;
		case "F":
			return VAR_F;
		case "G":
			return VAR_G;
		}
		return null;
	}
}
