/**
 *
 */
package com.channelsharing.common.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author liuhangjun
 * @date 2017年2月16日
 */
public class RandomUtil {

	private static SecureRandom random = new SecureRandom();

	public static int getRandomNum(int start, int end) {

		// start－end间随机数包括end
		int randNum = random.nextInt(end - start + 1) + start;

		return randNum;
	}

	/**
	 * 使用SecureRandom随机生成Long.
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}


	/**
     * 获取固定长度的随机数字
	 * @param len
	 * @return
	 */
	public static  String getRandomNumString(int len){

		String ret = "";
		for(int i = 0; i < len; i ++){
			ret = Integer.toString(getRandomNum(0, 9)) + ret;
		}

		return ret;

	}


	// 根据指定长度生成字母和数字的随机数
	// 0~9的ASCII为48~57
	// A~Z的ASCII为65~90
	// a~z的ASCII为97~122
	public static String createRandomCharData(int length) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();// 随机用以下三个随机生成器
		Random randdata = new Random();
		int data = 0;
		for (int i = 0; i < length; i++) {
			int index = rand.nextInt(3);
			// 目的是随机选择生成数字，大小写字母
			switch (index) {
			case 0:
				data = randdata.nextInt(10);// 仅仅会生成0~9
				sb.append(data);
				break;
			case 1:
				data = randdata.nextInt(26) + 65;// 保证只会产生65~90之间的整数
				sb.append((char) data);
				break;
			case 2:
				data = randdata.nextInt(26) + 97;// 保证只会产生97~122之间的整数
				sb.append((char) data);
				break;
			}
		}
		String result = sb.toString();

		return result;
	}

	public static void main(String[] args){
		String ret = RandomUtil.createRandomCharData(17);

		System.out.println(ret);
	}

}
