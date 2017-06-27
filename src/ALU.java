/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @161250056_李冰 [请将此处修改为“学号_姓名”]
 *
 */

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {

		String result = "";
		int num = Integer.parseInt(number);

		//负数
		if(num < 0){
			int i = -num;
			result = integerRepresentation(Integer.toString(i),length);
			result = negation(result);
			result = oneAdder(result);
			result = result.substring(1);//去除溢出位
		}else{

			//正数
			for(int i = 0;i<length;i++){
				if(num - (int) (Math.pow(2,length-i-1)) >= 0){
					result = result + "1";
					num = num - (int) (Math.pow(2,length-i-1));
				}else{
					result = result + "0";
				}

			}
		}

		return result;
	}

	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {

		 String result = "";
		 String sign = "";//符号位
		 String integerStr = "";
		 String exponentStr = "";//指数
		 String decimalStr = "";//尾数
		 int exponentVal = 0;
		 int offset = (int)(Math.pow(2, eLength-1))-1;
		 int shift = 0;

		 //符号位
		 if(number.charAt(0) == '-'){
			 sign = "1";
			 number = number.substring(1);
		 }
		 else
			 sign = "0";

		//特殊情况
		if(number.equals("+Inf")){
			for(int i = 0;i < eLength;i++){
				result += "1";
			}
			for(int i = 0;i < sLength;i++){
				result += "0";
			}
			result = sign + result;

			return result;
		}else if(number.equals("Inf")){
			for(int i = 0;i < eLength;i++){
				result += "1";
			}
			for(int i = 0;i < sLength;i++){
				result += "0";
			}

			result = sign + result;

			return result;
		}

		//0
		 double num = Double.parseDouble(number);
		 if(num == 0){
			for(int i = 0;i < eLength+sLength;i++){
				result += "0";
			}
			result = sign + result;
			return result;
		}

		 String[] numArr = number.split("\\.");
		 int integer = Integer.parseInt(numArr[0]);

		 int Integer = integer;
		 double decimal = Double.parseDouble("0." + numArr[1]);
		 //小数部分
		 while(decimal > 0){
			 if(2*decimal > 1){
				 decimalStr += 1;
				 decimal = 2*decimal - 1;
			 }
			 else if(2*decimal < 1){
				 decimalStr += 0;
				 decimal = 2*decimal;
			 }
			 else{
				 decimalStr += 1;
				 break;
			 }
		 }

		 if(integer > 0){
			 int temp = integer % 2;
			 while(integer > 0){
				 integerStr = temp + integerStr;
				 integer = integer/2;
				 temp = integer%2;
			 }
		 }


		 if(Integer == 0){
			 if(decimalStr.substring(0,1).equals("0")){
				 while(decimalStr.substring(0,1).equals("0")){
					 shift++;
					 decimalStr = leftShift(decimalStr,1);
			 }

				 decimalStr = "0" + decimalStr;
				 exponentVal = offset - shift;
				 exponentStr = integerRepresentation(exponentVal + "",eLength);

			 if(decimalStr.length() > sLength){
				 decimalStr = decimalStr.substring(0,sLength);
			 }else{
				 while(decimalStr.length() < sLength){
					 decimalStr += "0";
					 }
				 }
			 }
			 else{
				 exponentVal = offset+integerStr.length()-1;
				 exponentStr = integerRepresentation(exponentVal + "",eLength);

				 if(decimalStr.length() > sLength){
					 decimalStr = decimalStr.substring(0,sLength);
				 }
				 else{
					 while(decimalStr.length() < sLength){
						 decimalStr += "0";
					 }

					 decimalStr = "0" + decimalStr.substring(1);
				 }
			 }

			 result = sign + exponentStr + decimalStr;

			 return result;
		 }
		 else{
			 exponentVal = offset + integerStr.length() - 1;
			 exponentStr = integerRepresentation(exponentVal + "",eLength);
			 decimalStr = integerStr.substring(1) + decimalStr;

			 if(decimalStr.length() > sLength){
				 decimalStr = decimalStr.substring(0,sLength);
			 }
			 else{
				 while(decimalStr.length() < sLength){
					 decimalStr += "0";
				 }
			 }
		 }

		 result = sign + exponentStr + decimalStr;

		 return result;



	}

	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {

		if(length == 32){
			return floatRepresentation(number,8,23);
		}else if(length == 64){
			return floatRepresentation(number,11,52);
		}

		return null;
	}

	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		char[] oper = operand.toCharArray();
		int num = 0;

		//正数
		if(oper[0] == '0'){

			for(int i = 0;i<operand.length();i++){
				if(oper[i] == '1'){
					num += (int) (Math.pow(2,operand.length()-i-1));
				}
			}
		}

		//负数
		if(oper[0]=='1'){
			//取反加一
			operand = negation (operand);
			operand = oneAdder (operand);
			char[] Oper = operand.toCharArray();

			for(int i = 0;i<operand.length();i++){
				if(Oper[i] == '1'){
					num += (int) (Math.pow(2,operand.length()-i-1));
				}
			}
			num = -num;
		}

		return Integer.toString(num);

	}

	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {

		char c = operand.charAt(0);
		String symbol = "";//symbol表示符号位

		if(c == '1')
			symbol = "-";

		//无符号位
		int exponent = 0;//指数
		String exponentStr = operand.substring(1,eLength+1);

		for(int i = 0;i < exponentStr.length();i++)
			exponent = exponent * 2 + (exponentStr.charAt(i)-'0');

		// 求出尾数位及其表示的值
		int significandResult = 0;//尾数
		String significandStr = operand.substring(eLength + 1,eLength + sLength + 1);


		for(int i = 0;i < significandStr.length();i++)
			significandResult = significandResult * 2 + (significandStr.charAt(i)-'0');

		double f = significandResult / Math.pow(2, sLength);

		if(exponent == 0){
			// 全0阶码全0尾数：+0/-0
			if(f == 0.0){
				return symbol + "0.0";
			}

			// 全0阶码非0尾数：非规格化数
			else{
				int exponentReal = (int)(exponent - (Math.pow(2, eLength-1)-2));
				return symbol + (f * Math.pow(2, exponentReal));
			}
		}

		else if(exponent == Math.pow(2, eLength)-1){
			// 全1阶码全0尾数：+∞/-∞
			if(f == 0.0){
				if(symbol.equals("-"))
					return "-Inf";
				else
					return "+Inf";
			}
			//全1阶码非0尾数：NaN
			else{
				return "NaN";
			}
		}

		else{
			int exponentReal = (int)(exponent - (Math.pow(2, eLength-1)-1));
			return symbol + ((f+1) * Math.pow(2, exponentReal));
		}

	}

	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {

		String result = "";

		for(int i = 0;i < operand.length(); i++){
			int x = 1 - operand.charAt(i) + '0' ;
			result = result + Integer.toString(x);
		}

		return result;
	}

	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift (String operand, int n) {

		String result = operand;

		while(n != 0){
			result = result.substring(1) + "0";
			n--;
		}

		return result;

	}

	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift (String operand, int n) {

		String result = operand;

		while(n!=0){
			result = "0" + result.substring(0,operand.length()-1);
			n--;
		}

		return result;
	}

	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift (String operand, int n) {
		String result = operand;
		char symbol = operand.charAt(0);//symbol表示符号位

		while(n!=0){
			result = symbol+result.substring(0,operand.length()-1);
			n--;
		}

		return result;
	}

	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder (char x, char y, char c) {

		String result = "";
		String Fi = "";//Fi表示全加和
		String Ci = "";//Ci表示全加进位

		//Fi
		Fi = Integer.toString((x-'0')^(y-'0')^(c-'0'));

		//Ci
		Ci = Integer.toString(((x-'0')&(y-'0'))|((c-'0')&(y-'0'))|((x-'0')&(c-'0')));

		result = Ci + Fi;

		return result;
	}

	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {

		String result = "";
		char[] X = new char[operand1.length()];
		char[] Y = new char[operand2.length()];
		int[] P = new int[4];
		int[] G = new int[4];
		int[] C = new int[5];

		for(int i = 0;i < operand1.length();i++)
			X[i] = operand1.charAt(operand1.length()-1-i);

		for(int j = 0;j < operand2.length();j++)
			Y[j] = operand2.charAt(operand2.length()-1-j);

		for(int i =0;i<operand1.length();i++){
			//Pi
			P[i] = fullAdder(X[i],Y[i],'1').charAt(0) - '0';

			//Gi
			G[i] = fullAdder(X[i],Y[i],'0').charAt(0) - '0';
		}

		C[0] = (int)(c - '0');
		C[1] = G[0]|(P[0] & C[0]);
		C[2] = G[1] | (P[1] & G[0]) | (P[1] & P[0] & C[0]);
		C[3] = G[2] | (P[2] & G[1]) | (P[2] & P[1] & G[0]) | (P[2] & P[1] & P[0] & C[0]);
		C[4] = G[3] | (P[3] & G[2]) | (P[3] & P[2] & G[1]) | (P[3] & P[2] & P[1] & G[0]) | (P[3] & P[2] & P[1] & P[0] & C[0]);

		for(int i =0;i<operand1.length();i++){
			result = Integer.toString((X[i]-'0')^(Y[i]-'0')^C[i]) + result;
		}

		result = Integer.toString(C[4]) + result;

		return result;

	}

	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		String result = "";
		int[] carry = new int[operand.length()+1];

		carry[0] = 1;

		for(int i = 0;i<operand.length();i++){
			result = (char)((operand.charAt(operand.length()-1-i)-'0') ^ carry[i] + '0') + result;
			carry[i+1] = (operand.charAt(operand.length()-1-i)-'0') & carry[i];
		}

		char Overflow = '0';

		//负数加一永远不会溢出

		//正数溢出判断
		if(operand.charAt(0) != '1'){

			Overflow = (char) (Overflow + ((('1'-result.charAt(0)) & (operand.charAt(0)-'0')) | ((result.charAt(0)-'0') & ('1'-operand.charAt(0)))));

		}


		return Overflow + result;



	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {

		String oper1 = operand1;
		String oper2 = operand2;
		String result = "";

		//首先进行符号扩展
		for(int i = 0;i < length-operand1.length();i++){
			oper1 = operand1.charAt(0) + oper1;
		}

		for(int i = 0;i < length-operand2.length();i++){
			oper2 = operand2.charAt(0) + oper2;
		}


		//按照4个数一组划分，利用四位先行进位加法器
		for(int i = 0;i < length/4;i++){
			result = claAdder(oper1.substring(length-4*i-4,length-4*i),oper2.substring(length-4*i-4,length-4*i),c) + result;
			c = result.charAt(0);
			result = result.substring(1);
		}

		char Overflow = '0';
		Overflow = (char) (Overflow+(('1'-result.charAt(0))&(oper1.charAt(0)-'0')&(oper2.charAt(0)-'0')|(result.charAt(0)-'0')&('1'-oper1.charAt(0))&('1'-oper2.charAt(0)-'0')));

		return Overflow + result;
	}

	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {

		return adder(operand1,operand2,'0',length);

	}

	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {

		//operand2取反加一
		String temp = negation(operand2);
		temp = oneAdder(temp);
		String OppoOperand2 = temp.substring(1);//去除溢出判断位

		String result = adder(operand1, OppoOperand2, '0', length);

		return result;
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {

		String oper1 = operand1;
		String oper2 = operand2;

		//高位补符号
		for(int i = 0;i < length-operand1.length();i++){
			oper1 = operand1.charAt(0) + oper1;
		}
		for(int i = 0;i < length-operand2.length();i++){
			oper2 = operand2.charAt(0) + oper2;
		}

		String P = oper2 + "0";

		for(int i = 0;i < length;i++)
			P = "0" + P;

		for(int i = 0;i < length;i++){
			String part = P.substring(0,length);

			//比较Y[i-1]和Y[i]的值
			if(P.charAt(2*length) - P.charAt(2*length-1) == 1){
				part = integerAddition(part,oper1,length).substring(1);
			}else if(P.charAt(2*length) - P.charAt(2*length-1) == -1){

				part = integerSubtraction(part,oper1,length).substring(1);
			}

			//Y[i-1] - Y[i] == 0
			P = part + P.substring(length);
			P = ariRightShift(P,1);
		}

		String result = P.substring(length,2*length);

		for(int i = 0;i < length;i++){
			if(P.charAt(i) != result.charAt(0)){
				return "1" + result;
			}
		}
		return "0" + result;
	}

	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {

		String result = "";
		String overflow = "";
		boolean IsSameSign = true;
		String saveRem = "";

		//符号拓展
		while(operand1.length() < length){
			operand1 = operand1.charAt(0)+operand1;
		}
		while(operand2.length() < length){
			operand2 = operand2.charAt(0) + operand2;
		}

		//除数为0时，返回NaN
		if(operand2.substring(1).contains("1") == false){
			return "NaN";
		}

		for(int i = 0;i < length;i++){
			saveRem = saveRem + operand1.charAt(0);
		}

		String saveQuo = operand1;
		String judge = saveRem + saveQuo;

		//除数，被除数同号，做减法
		if(judge.charAt(0) == operand2.charAt(0)){
			saveRem = integerSubtraction(saveRem,operand2,length).substring(1);
		}
		//除数、被除数异号，做加法
		else{
			saveRem = integerAddition(saveRem,operand2,length).substring(1);
		}

		judge = saveRem + saveQuo;

		//中间余数与除数异号
		if(saveRem.charAt(0) != operand2.charAt(0)){
			IsSameSign = false;
		}

		for(int i=0;i < length;i++){
			if(saveRem.charAt(0) == operand2.charAt(0)){
				judge = judge.substring(1) + "1";
			}
			else{
				judge = judge.substring(1) + "0";
			}
			saveRem = judge.substring(0,length);
			saveQuo = judge.substring(length);

			if(judge.charAt(0) == operand2.charAt(0)){
				saveRem = integerSubtraction(saveRem,operand2, length).substring(1);
			}
			else{
				saveRem = integerAddition(saveRem,operand2, length).substring(1);
			}
			judge = saveRem + saveQuo;
		}
		if(saveRem.charAt(0) == operand2.charAt(0)){
			saveQuo = saveQuo.substring(1) + "1";
		}
		else{
			saveQuo = saveQuo.substring(1) + "0";
		}
		if(operand1.charAt(0) != operand2.charAt(0)){
			saveQuo = oneAdder(saveQuo).substring(1);
		}
		if(saveRem.charAt(0) != operand1.charAt(0)){
			if(operand1.charAt(0) != operand2.charAt(0)){
				saveRem = integerSubtraction(saveRem,operand2, length).substring(1);
			}
			else{
				saveRem = integerAddition(saveRem,operand2, length).substring(1);
			}
		}
		if((operand1.charAt(0) == operand2.charAt(0)
				&& IsSameSign)
				&& (operand1.charAt(0) != operand2.charAt(0)
				&& !IsSameSign)){
			overflow = "1";
		}
		else
			overflow = "0";

		int divisorVal = Integer.parseInt(integerTrueValue(operand2));
		int remVal = Integer.parseInt(integerTrueValue(saveRem));
		if(divisorVal == remVal){
			saveRem = "";
			for(int i = 0;i < length;i++){
				saveRem += "0";
			}
			saveQuo = oneAdder(saveQuo).substring(1);
		}
		else if(divisorVal == -remVal){
			saveRem = "";
			for(int i = 0;i < length;i++){
				saveRem += "0";
			}
			saveQuo = adder(saveQuo,"1",'0',length).substring(1);
		}

		result = overflow + saveQuo + saveRem;

		return result;
	}

	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {

		String result = "";
		String numAddResult = "";
		String Overflow = "0";
		String sign = "";

		String sign1=operand1.substring(0,1);//被加数符号
		String sign2=operand2.substring(0,1);//加数符号

		//去除符号位，比较操作数与length的长度
		operand1 = operand1.substring(1);
		operand2 = operand2.substring(1);

		while(operand1.length() < length){
			operand1 = "0" + operand1;
		}

		while(operand2.length() < length){
			operand2 = "0" + operand2;
		}

		//符号位相同
		if(sign1.equals(sign2)){
			numAddResult = adder(operand1,operand2,'0',length);

			//溢出判断
			if(numAddResult.substring(1).equals("0") && operand1.substring(1,2).equals("1")||
			   numAddResult.substring(1).equals("0") && operand2.substring(1,2).equals("1")||
		       numAddResult.substring(2).contains("1") == false){

				Overflow = "1";

			}
			sign = sign1;
			result = Overflow + sign1 + numAddResult.substring(1);
		}//一正一负
		else{
			//相反数
			if(operand1.substring(1).equals(operand2.substring(1))){
				sign = sign1;

				for(int i = 0;i < length;i++){
					result += "0";
				}

				result = Overflow + sign + result;

				return result;
			}

			//被加数为负数
			if(sign1.equals("1")){
				String temp = operand1;
				String tempSign = sign1;
				//交换字符串
				operand1 = operand2;
				operand2 = temp;
				sign1 = sign2;
				sign2 = tempSign;
			}

			//取反加一
			operand2 = negation(operand2);
			operand2 = oneAdder(operand2).substring(1);

			numAddResult = adder(operand1,operand2,'0',length);

			if(numAddResult.substring(0,1).equals("0")){
				sign = sign1;
				result = numAddResult.substring(0,1) + sign + numAddResult.substring(1);
			}

			//加数为负数
			else{
				//取反加一
				sign = negation(sign1);
				String oper2 = numAddResult.substring(1);
				oper2 = oneAdder(negation(oper2));

				result = numAddResult.substring(0,1) + sign + oper2;
			}
		}

		return result;


	}

	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// 测试4没过
		String result = "";
		String sign1 = operand1.substring(0,1);
		String sign2 = operand2.substring(0,1);
		String sign = "";

		//指数
		String overflow  = "0";
		String decimalOverflow = "";
		String e1 = operand1.substring(1, eLength + 1);
		String e2 = operand2.substring(1, eLength + 1);
		String exponent = "";

		//尾数
		String s1 = operand1.substring(eLength + 1);
		String s2 = operand2.substring(eLength + 1);
		String decimal = "";

		int count = 0;

		for(int i = 0; i < gLength; i++){
			s1 += "0";
			s2 += "0";
		}
		if(operand1.substring(1).contains("1") == false){
			return "0" + operand2;
		}
		else if(operand2.substring(1).contains("1") == false){
			return "0" + operand1;
		}
		if(sign1.equals(sign2) == false && operand1.substring(1).equals(operand2.substring(1))){
			for(int i = 0;i < eLength + sLength + 2;i++){
				result += "0";
			}
			return result;
		}
		
		String gap = integerSubtraction("0" + e1, "0" + e2, eLength + 1).substring(1);
		if(gap.substring(0,1).equals("0")){
			exponent = e1;
			sign = sign1;
			int intgap = Integer.parseInt(integerTrueValue(gap));
			
			while(count < intgap){
				s2 = logRightShift(s2,1);
				count++;
			}
		}
		else{
			exponent = e2;
			sign = sign2;
			int intgap = -Integer.parseInt(integerTrueValue(gap));
			
			while(count < intgap){
				s1 = logRightShift(s1,1);
				count++;
			}
		}
		s1 = sign1 + "1" + s1;
		s2 = sign2 + "1" + s2;
		String addedDecimal = signedAddition(s1,s2,sLength + 2 + gLength);

		decimalOverflow = addedDecimal.substring(0,1);
		if(decimalOverflow.equals("1")){
			exponent = oneAdder(exponent).substring(1);
			addedDecimal = addedDecimal.substring(1,sLength+1);
			decimal = addedDecimal;
			result = overflow + sign + exponent + decimal;
			
			return result;
		}
		if(addedDecimal.contains("1") == false){
				int eVal = Integer.parseInt(integerTrueValue("0" + exponent));
				eVal -= count;
			exponent = integerRepresentation(eVal + "",eLength);

			decimal = addedDecimal.substring(1,1+sLength);
			result = overflow + sign + exponent + decimal;
			return result;
		}
		int eVal2 = Integer.parseInt(integerTrueValue("0" + exponent));;
		addedDecimal = addedDecimal.substring(1);
		
		while(addedDecimal.substring(0,1).equals("0")){
			eVal2--;
			addedDecimal = leftShift(addedDecimal,1);
		}

		exponent = integerRepresentation(eVal2 + "",eLength);
		decimal = (addedDecimal.substring(1)).substring(0,sLength);
		result = overflow + sign + exponent + decimal;
		return result;
	}

	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {

		char symbol2 = (char) ('1' - operand2.charAt(0) + '0');//更改符号位
		operand2 = symbol2 + operand2.substring(1);//相反数

		String result = floatAddition(operand1, operand2, eLength, sLength, gLength);

		return result;
	}

	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		
		int e1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1,1 + eLength)));
		int e2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1,1 + eLength)));
		int bias = (int)(Math.pow(2, eLength-1) - 1);

		char hide1 = '1';//隐藏位初始为1
		char hide2 = '1';
		if(e1 == 0){
			hide1 = '0';
		}
		if(e2 == 0){
			hide1 = '0';
		}

		String sign1 = hide1 + operand1.substring(eLength+1);
		String sign2 = hide2 + operand2.substring(eLength+1);


		char symbol = '0';
		if(operand1.charAt(0) != operand2.charAt(0)){
			symbol = '1';
		}

		//检查两个操作数是否为0，若有0则返回0
		if(Integer.parseInt(operand1.substring(1), 2) == 0 || Integer.parseInt(operand2.substring(1), 2) == 0){
			String result = "";
			for(int i = 0; i < 1 + eLength + sLength; i++){
				result += "0";
			}
			return symbol + result;
		}

		//指数相加，减去偏值
		int exponent = e1 + e2 - bias;

		//如果指数上溢，返回无穷
		if(exponent > Math.pow(2, eLength)-1){
			String expo = "";
			String sign = "";
			for(int i = 0;i < eLength;i++){
				expo += "1";
			}
			for(int i = 0;i < sLength;i++){
				sign += "0";
			}

			return "0" + symbol + expo + sign;
		}
		//指数下溢返回0
		else if(exponent < 0){
			String symAndSign = "";
			for(int i = 0;i < eLength + sLength;i++){
				symAndSign += "0";
			}

			return "1" + symbol + symAndSign;
		}

		//尾数相乘
		String significand = sign2;
		int length = ((1 + sLength) / 4 + 1) * 4 - 1;
		
		//将操作数2的尾数扩展为2*length位
		for(int i = 0; i < 2*length - sign2.length(); i++){
			significand = "0" + significand;
		}
		
		//相乘
		for(int i = length-1; i >= 0; i--){
			if(significand.charAt(2*length-1) == '1'){
				significand = integerAddition("0" + significand.substring(0, length), "0" + sign1, length + 1).substring(2) + significand.substring(length);
			}

			significand = logRightShift(significand, 1);

		}

		significand = significand.substring(length - sLength + 1,2 * length);

		//规则化
		if(significand.charAt(0) == '1'){
			exponent ++;
			//如果指数上溢，报告无穷
			if(exponent > Math.pow(2, eLength)-1){
				String expo = "";
				String sign = "";
				for(int i = 0;i < eLength;i++){
					expo += "1";
				}
				for(int i = 0;i < sLength;i++){
					sign += "0";
				}

				return "1" + symbol + expo+sign;
			}
		}
		significand = leftShift(significand, 1);

		while(significand.charAt(0) != '1'){
			exponent --;
			significand = leftShift(significand, 1);
			if(exponent == 0){
				String expo = integerRepresentation("0", eLength);
				String sign = significand.substring(1,1+sLength);
				return "0" + symbol + expo + sign;
			}
		}

		String expo = integerRepresentation(Integer.toString(exponent), eLength+1).substring(1);
		String sign = significand.substring(1,1+sLength);
		return "0" + symbol + expo + sign;
	}

	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		
		int e1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, 1 + eLength)));
		int e2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, 1 + eLength)));
		int bias = (int)(Math.pow(2, eLength-1) - 1);

		//隐藏位初始为1
		char hide1 = '1';
		char hide2 = '1';
		if(e1 == 0){
			hide1 = '0';
		}
		if(e2 == 0){
			hide1 = '0';
		}

		String sign1 = hide1+operand1.substring(eLength + 1);
		String sign2 = hide2+operand2.substring(eLength + 1);

		char symbol = '0';
		if(operand1.charAt(0) != operand2.charAt(0)){
			symbol = '1';
		}

		//如果被除数为正负0，那么答案即为0
		if(Integer.parseInt(operand1.substring(1), 2) == 0){

			String result = "";
			for(int i = 0; i < 2 + eLength + sLength; i++){
				result += "0";
			}
			return result;
		}
		//如果除数为0，答案为无穷
		else if(Integer.parseInt(operand2.substring(1), 2) == 0){

			String result = "00";
			for(int i = 0; i < eLength; i++){
				result += "1";
			}
			for(int i = 0; i < sLength; i++){
				result += "0";
			}

			return result;
		}

		//指数相减，加上偏值
		int exponent = e1 - e2 + bias;

		//如果指数上溢，返回无穷
		if(exponent > Math.pow(2, eLength) - 1){
			String result = "";

			for(int i = 0; i < eLength; i++){
				result += "1";
			}
			for(int i = 0; i < sLength; i++){
				result += "0";
			}

			return "1" + symbol + result;
		}
		//指数下溢返回0
		else if(exponent < 0){
			String symAndSign = "";
			for(int i = 0; i < eLength+sLength; i++){
				symAndSign += "0";
			}

			return "0" + symbol + symAndSign;
		}

		String sign = sign1;
		int length = ((1+sLength)/4 + 1)*4 - 1;

		//两数相除
		for(int i = 0; i < length-sign1.length(); i++){
			sign = "0" + sign;
		}

		for(int i = 0; i < length; i++){
			sign = sign + "0";
		}


		for(int i = 0; i < length; i++){
			String reminder = sign.substring(0, length);
			char c = '0';

			//如果够减，就减
			String temp = integerSubtraction("0" + reminder, "0" + sign2, length + 1).substring(2);
			if(Integer.parseInt(integerTrueValue(temp)) >= 0){
				reminder = temp;
				c = '1';
			}
			sign = reminder+sign.substring(length, length * 2);
			sign = sign.substring(1) + c;

		}

		sign = sign.substring(length,2 * length);

		while(sign.charAt(0) != '1'){
			exponent --;
			sign = leftShift(sign, 1);
			if(exponent == 0){
				String e = integerRepresentation("0", eLength);
				String s = sign.substring(1,1+sLength);
				return "0" + symbol + e + s;
			}
		}

		String e = integerRepresentation(Integer.toString(exponent), eLength+1).substring(1);
		String s = sign.substring(1,1+sLength);
		return "0" + symbol + e + s;
		}
}
