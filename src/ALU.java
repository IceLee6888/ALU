/**
 * ģ��ALU���������͸���������������
 * @161250056_��� [�뽫�˴��޸�Ϊ��ѧ��_������]
 *
 */

public class ALU {

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {

		String result = "";
		int num = Integer.parseInt(number);

		//����
		if(num < 0){
			int i = -num;
			result = integerRepresentation(Integer.toString(i),length);
			result = negation(result);
			result = oneAdder(result);
			result = result.substring(1);//ȥ�����λ
		}else{

			//����
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
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {

		 String result = "";
		 String sign = "";//����λ
		 String integerStr = "";
		 String exponentStr = "";//ָ��
		 String decimalStr = "";//β��
		 int exponentVal = 0;
		 int offset = (int)(Math.pow(2, eLength-1))-1;
		 int shift = 0;

		 //����λ
		 if(number.charAt(0) == '-'){
			 sign = "1";
			 number = number.substring(1);
		 }
		 else
			 sign = "0";

		//�������
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
		 //С������
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
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int) floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
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
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue (String operand) {
		char[] oper = operand.toCharArray();
		int num = 0;

		//����
		if(oper[0] == '0'){

			for(int i = 0;i<operand.length();i++){
				if(oper[i] == '1'){
					num += (int) (Math.pow(2,operand.length()-i-1));
				}
			}
		}

		//����
		if(oper[0]=='1'){
			//ȡ����һ
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
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {

		char c = operand.charAt(0);
		String symbol = "";//symbol��ʾ����λ

		if(c == '1')
			symbol = "-";

		//�޷���λ
		int exponent = 0;//ָ��
		String exponentStr = operand.substring(1,eLength+1);

		for(int i = 0;i < exponentStr.length();i++)
			exponent = exponent * 2 + (exponentStr.charAt(i)-'0');

		// ���β��λ�����ʾ��ֵ
		int significandResult = 0;//β��
		String significandStr = operand.substring(eLength + 1,eLength + sLength + 1);


		for(int i = 0;i < significandStr.length();i++)
			significandResult = significandResult * 2 + (significandStr.charAt(i)-'0');

		double f = significandResult / Math.pow(2, sLength);

		if(exponent == 0){
			// ȫ0����ȫ0β����+0/-0
			if(f == 0.0){
				return symbol + "0.0";
			}

			// ȫ0�����0β�����ǹ����
			else{
				int exponentReal = (int)(exponent - (Math.pow(2, eLength-1)-2));
				return symbol + (f * Math.pow(2, exponentReal));
			}
		}

		else if(exponent == Math.pow(2, eLength)-1){
			// ȫ1����ȫ0β����+��/-��
			if(f == 0.0){
				if(symbol.equals("-"))
					return "-Inf";
				else
					return "+Inf";
			}
			//ȫ1�����0β����NaN
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
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
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
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
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
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
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
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		String result = operand;
		char symbol = operand.charAt(0);//symbol��ʾ����λ

		while(n!=0){
			result = symbol+result.substring(0,operand.length()-1);
			n--;
		}

		return result;
	}

	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder (char x, char y, char c) {

		String result = "";
		String Fi = "";//Fi��ʾȫ�Ӻ�
		String Ci = "";//Ci��ʾȫ�ӽ�λ

		//Fi
		Fi = Integer.toString((x-'0')^(y-'0')^(c-'0'));

		//Ci
		Ci = Integer.toString(((x-'0')&(y-'0'))|((c-'0')&(y-'0'))|((x-'0')&(c-'0')));

		result = Ci + Fi;

		return result;
	}

	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
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
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
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

		//������һ��Զ�������

		//��������ж�
		if(operand.charAt(0) != '1'){

			Overflow = (char) (Overflow + ((('1'-result.charAt(0)) & (operand.charAt(0)-'0')) | ((result.charAt(0)-'0') & ('1'-operand.charAt(0)))));

		}


		return Overflow + result;



	}

	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder (String operand1, String operand2, char c, int length) {

		String oper1 = operand1;
		String oper2 = operand2;
		String result = "";

		//���Ƚ��з�����չ
		for(int i = 0;i < length-operand1.length();i++){
			oper1 = operand1.charAt(0) + oper1;
		}

		for(int i = 0;i < length-operand2.length();i++){
			oper2 = operand2.charAt(0) + oper2;
		}


		//����4����һ�黮�֣�������λ���н�λ�ӷ���
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
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {

		return adder(operand1,operand2,'0',length);

	}

	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {

		//operand2ȡ����һ
		String temp = negation(operand2);
		temp = oneAdder(temp);
		String OppoOperand2 = temp.substring(1);//ȥ������ж�λ

		String result = adder(operand1, OppoOperand2, '0', length);

		return result;
	}

	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {

		String oper1 = operand1;
		String oper2 = operand2;

		//��λ������
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

			//�Ƚ�Y[i-1]��Y[i]��ֵ
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
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {

		String result = "";
		String overflow = "";
		boolean IsSameSign = true;
		String saveRem = "";

		//������չ
		while(operand1.length() < length){
			operand1 = operand1.charAt(0)+operand1;
		}
		while(operand2.length() < length){
			operand2 = operand2.charAt(0) + operand2;
		}

		//����Ϊ0ʱ������NaN
		if(operand2.substring(1).contains("1") == false){
			return "NaN";
		}

		for(int i = 0;i < length;i++){
			saveRem = saveRem + operand1.charAt(0);
		}

		String saveQuo = operand1;
		String judge = saveRem + saveQuo;

		//������������ͬ�ţ�������
		if(judge.charAt(0) == operand2.charAt(0)){
			saveRem = integerSubtraction(saveRem,operand2,length).substring(1);
		}
		//��������������ţ����ӷ�
		else{
			saveRem = integerAddition(saveRem,operand2,length).substring(1);
		}

		judge = saveRem + saveQuo;

		//�м�������������
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
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int) integerAddition}��
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {

		String result = "";
		String numAddResult = "";
		String Overflow = "0";
		String sign = "";

		String sign1=operand1.substring(0,1);//����������
		String sign2=operand2.substring(0,1);//��������

		//ȥ������λ���Ƚϲ�������length�ĳ���
		operand1 = operand1.substring(1);
		operand2 = operand2.substring(1);

		while(operand1.length() < length){
			operand1 = "0" + operand1;
		}

		while(operand2.length() < length){
			operand2 = "0" + operand2;
		}

		//����λ��ͬ
		if(sign1.equals(sign2)){
			numAddResult = adder(operand1,operand2,'0',length);

			//����ж�
			if(numAddResult.substring(1).equals("0") && operand1.substring(1,2).equals("1")||
			   numAddResult.substring(1).equals("0") && operand2.substring(1,2).equals("1")||
		       numAddResult.substring(2).contains("1") == false){

				Overflow = "1";

			}
			sign = sign1;
			result = Overflow + sign1 + numAddResult.substring(1);
		}//һ��һ��
		else{
			//�෴��
			if(operand1.substring(1).equals(operand2.substring(1))){
				sign = sign1;

				for(int i = 0;i < length;i++){
					result += "0";
				}

				result = Overflow + sign + result;

				return result;
			}

			//������Ϊ����
			if(sign1.equals("1")){
				String temp = operand1;
				String tempSign = sign1;
				//�����ַ���
				operand1 = operand2;
				operand2 = temp;
				sign1 = sign2;
				sign2 = tempSign;
			}

			//ȡ����һ
			operand2 = negation(operand2);
			operand2 = oneAdder(operand2).substring(1);

			numAddResult = adder(operand1,operand2,'0',length);

			if(numAddResult.substring(0,1).equals("0")){
				sign = sign1;
				result = numAddResult.substring(0,1) + sign + numAddResult.substring(1);
			}

			//����Ϊ����
			else{
				//ȡ����һ
				sign = negation(sign1);
				String oper2 = numAddResult.substring(1);
				oper2 = oneAdder(negation(oper2));

				result = numAddResult.substring(0,1) + sign + oper2;
			}
		}

		return result;


	}

	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// ����4û��
		String result = "";
		String sign1 = operand1.substring(0,1);
		String sign2 = operand2.substring(0,1);
		String sign = "";

		//ָ��
		String overflow  = "0";
		String decimalOverflow = "";
		String e1 = operand1.substring(1, eLength + 1);
		String e2 = operand2.substring(1, eLength + 1);
		String exponent = "";

		//β��
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
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int) floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {

		char symbol2 = (char) ('1' - operand2.charAt(0) + '0');//���ķ���λ
		operand2 = symbol2 + operand2.substring(1);//�෴��

		String result = floatAddition(operand1, operand2, eLength, sLength, gLength);

		return result;
	}

	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int) integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		
		int e1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1,1 + eLength)));
		int e2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1,1 + eLength)));
		int bias = (int)(Math.pow(2, eLength-1) - 1);

		char hide1 = '1';//����λ��ʼΪ1
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

		//��������������Ƿ�Ϊ0������0�򷵻�0
		if(Integer.parseInt(operand1.substring(1), 2) == 0 || Integer.parseInt(operand2.substring(1), 2) == 0){
			String result = "";
			for(int i = 0; i < 1 + eLength + sLength; i++){
				result += "0";
			}
			return symbol + result;
		}

		//ָ����ӣ���ȥƫֵ
		int exponent = e1 + e2 - bias;

		//���ָ�����磬��������
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
		//ָ�����緵��0
		else if(exponent < 0){
			String symAndSign = "";
			for(int i = 0;i < eLength + sLength;i++){
				symAndSign += "0";
			}

			return "1" + symbol + symAndSign;
		}

		//β�����
		String significand = sign2;
		int length = ((1 + sLength) / 4 + 1) * 4 - 1;
		
		//��������2��β����չΪ2*lengthλ
		for(int i = 0; i < 2*length - sign2.length(); i++){
			significand = "0" + significand;
		}
		
		//���
		for(int i = length-1; i >= 0; i--){
			if(significand.charAt(2*length-1) == '1'){
				significand = integerAddition("0" + significand.substring(0, length), "0" + sign1, length + 1).substring(2) + significand.substring(length);
			}

			significand = logRightShift(significand, 1);

		}

		significand = significand.substring(length - sLength + 1,2 * length);

		//����
		if(significand.charAt(0) == '1'){
			exponent ++;
			//���ָ�����磬��������
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
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		
		int e1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, 1 + eLength)));
		int e2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, 1 + eLength)));
		int bias = (int)(Math.pow(2, eLength-1) - 1);

		//����λ��ʼΪ1
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

		//���������Ϊ����0����ô�𰸼�Ϊ0
		if(Integer.parseInt(operand1.substring(1), 2) == 0){

			String result = "";
			for(int i = 0; i < 2 + eLength + sLength; i++){
				result += "0";
			}
			return result;
		}
		//�������Ϊ0����Ϊ����
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

		//ָ�����������ƫֵ
		int exponent = e1 - e2 + bias;

		//���ָ�����磬��������
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
		//ָ�����緵��0
		else if(exponent < 0){
			String symAndSign = "";
			for(int i = 0; i < eLength+sLength; i++){
				symAndSign += "0";
			}

			return "0" + symbol + symAndSign;
		}

		String sign = sign1;
		int length = ((1+sLength)/4 + 1)*4 - 1;

		//�������
		for(int i = 0; i < length-sign1.length(); i++){
			sign = "0" + sign;
		}

		for(int i = 0; i < length; i++){
			sign = sign + "0";
		}


		for(int i = 0; i < length; i++){
			String reminder = sign.substring(0, length);
			char c = '0';

			//����������ͼ�
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
